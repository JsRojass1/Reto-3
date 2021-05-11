package model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import controller.ControllerException;
import data_structures.list.ArregloDinamico;
import data_structures.list.Lista;
import data_structures.map.Tabla;
import data_structures.map.bst.RedBlackTree;
import data_structures.map.hash.TablaHashLinearProbing;
import data_structures.map.hash.TablaHashSeparateChaining;
import entities.ContentCharacteristic;
import entities.ContentFilter;
import entities.FloatKey;
import entities.Genre;
import entities.GenreType;
import entities.ListenEvent;
import entities.ListenEvent.ListenEventBuilder;

/**
 * Definicion del modelo del mundo
 */
public class Modelo {

	private static final String SENTIMENT = "./data/sentiment_values.csv";

	private static final String CONTENT = "./data/context_content_features";
	private static final String USER = "./data/user_track_hashtag_timestamp";

	//Formato de fechas en context-content
	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private String rutaContent;
	private String rutaUser;

	/**
	 * Listen events by id. Static cache one loaded
	 */
	private Tabla<Long, ListenEvent> events;
	/**
	 * Genres by name
	 */
	private Tabla<String, Genre> genres;
	/**
	 * Events by genre
	 */
	private Tabla<String, Lista<ListenEvent>> eventsByGenre;

	/**
	 * Hashtags
	 */
	private Tabla<String, Lista<String>> hashtagsByTrack;

	/**
	 * VADER by hashtag
	 */
	private Tabla<String, Float> vaderByHashtag;

	/**
	 * Constructor del modelo del mundo
	 */
	public Modelo() {
		events = new TablaHashSeparateChaining<>();
		eventsByGenre = new TablaHashSeparateChaining<>();
		genres = new TablaHashSeparateChaining<>();
		hashtagsByTrack = new TablaHashSeparateChaining<>();
		vaderByHashtag = new TablaHashSeparateChaining<>();
	}

	/**
	 * Constructor del modelo del mundo con capacidad dada
	 * 0 = small
	 * 5 = 5%
	 * 10 = 10%
	 * 20 = 20%
	 * ...
	 * 100 = 100%
	 *
	 * @param tamanoDatos
	 * @throws ControllerException si el tamaño dado es invalido
	 */
	public Modelo(int tamanoDatos) throws ControllerException {
		this();
		String size;
		if (tamanoDatos == 0) {
			size = "small";
		}
		else if (tamanoDatos < 5 || tamanoDatos > 100 || (tamanoDatos != 5 && tamanoDatos % 10 != 0)) {
			throw new ControllerException("Tamaño inválido");
		}
		else {
			size = tamanoDatos + "pct";
		}
		rutaContent = CONTENT + "-" + size + ".csv";
		rutaUser = USER + "-" + size + ".csv";
		cargarDatos();
	}

	private CSVReader getReader(String path) throws ControllerException {
		try {
			CSVParser parser = new CSVParserBuilder().withSeparator(',').build();
			return new CSVReaderBuilder(new FileReader(path)).withCSVParser(parser).build();
		}
		catch (FileNotFoundException e) {
			throw new ControllerException("El archivo '" + path + "' no se encontró");
		}
	}

	private void cargarDatos() throws ControllerException {
		try (
				CSVReader sentimentsCsv = getReader(SENTIMENT);
				CSVReader contentCsv = getReader(rutaContent);
				CSVReader userCsv = getReader(rutaUser)
		) {
			//Leer primera linea de cada archivo
			sentimentsCsv.readNext();
			contentCsv.readNext();
			userCsv.readNext();
			//Lectura de content
			readFile(contentCsv, this::readContent);
			//Lectura de sentimientos
			readFile(sentimentsCsv, this::readSentiment);
			//Lectura de users
			readFile(userCsv, this::readUser);
			
			loadGenresTable();
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new ControllerException("Hubo un error en la lectura en los archivos de datos");
		}
	}

	private void readFile(CSVReader reader, Consumer<String[]> rowConsumer) {
		String[] row;
		long line = 0;
		try {
			while ((row = reader.readNext()) != null) {
				try {
					line = reader.getLinesRead();
					rowConsumer.accept(row);
				}
				catch (Exception e) {
					throw new RuntimeException("Error en linea " + line + ": " + e.getMessage());
				}
			}
		}
		catch (Exception e) {
			throw new RuntimeException("Error al leer archivo " + e.getMessage());
		}
	}

	private void readSentiment(String[] row) {
		try {
			vaderByHashtag.put(row[0], Float.parseFloat(row[4]));
		}
		catch (NumberFormatException e) {
			//Ignored
		}
	}

	private void readContent(String[] row) {
		float[] characteristics = new float[ContentCharacteristic.values().length];
		for (int i = 0; i < characteristics.length; i++) {
			characteristics[i] = Float.parseFloat(row[i]);
		}
		ListenEvent event = new ListenEventBuilder()
				.withCharacteristics(characteristics)
				.withArtistId(row[11])
				.withTweetLang(row[12])
				.withTrackId(row[13])
				.withCreatedAt(LocalDateTime.parse(row[14], dateFormatter))
				.withLang(row[15])
				.withTimezone(row[16])
				.withUserId(Long.parseLong(row[17]))
				.withId(Long.parseLong(row[18]))
				.build();
		events.put(event.getId(), event);
	}

	private void readUser(String[] row) {
		Lista<String> hashtagsInTrack = hashtagsByTrack.get(row[1]);
		if (hashtagsInTrack == null) {
			hashtagsInTrack = new ArregloDinamico<>();
			hashtagsByTrack.put(row[1], hashtagsInTrack);
		}
		hashtagsInTrack.agregar(row[2]);
	}

	private void loadGenresTable() {
		for (GenreType genreType : GenreType.values()) {
			addGenre(new Genre(genreType));
		}
	}

	public void addGenre(Genre genre) {
		Tabla<Long, ListenEvent> eventsInGenre = filter(new ContentFilter(ContentCharacteristic.TEMPO, genre.getMinBPM(), genre.getMaxBPM()));
		genres.put(genre.getName(), genre);
		eventsByGenre.put(genre.getName(), eventsInGenre.valueList());
	}

	public Genre getGenre(String name) throws IllegalArgumentException {
		for (Genre genre : genres.values()) {
			if (genre.getName().toLowerCase().equals(name.toLowerCase())) {
				return genre;
			}
		}
		throw new IllegalArgumentException();
	}

	/**
	 * Do a filter with the data loaded and return a map of all the events that match the filters
	 *
	 * @param filters a var-arg or array of filters to apply
	 * @return a tree of all match results
	 */
	public RedBlackTree<Long, ListenEvent> filter(ContentFilter... filters) {
		//Pool of posible values to filter
		Lista<ListenEvent> eventValues = events.valueList();
		for (ContentFilter filter : filters) {
			//Map of events by current filter characteristic float value
			RedBlackTree<FloatKey, ListenEvent> eventsByCharacteristic = new RedBlackTree<>();
			//Load the map with all values by the filter characteristic as key
			eventsByCharacteristic.putAll(eventValues, event -> new FloatKey(event.getId(), event.getCharacteristic(filter.getCharacteristic())));
			//Set the values filtered that match the filter range for next filter
			eventValues = eventsByCharacteristic.valuesInRange(new FloatKey(filter.getMin()), new FloatKey(filter.getMax()));
		}
		//Transform the characteristic value map to id map
		RedBlackTree<Long, ListenEvent> answer = new RedBlackTree<>();
		answer.putAll(eventValues, event -> event.getId());
		return answer;
	}

	public Tabla<Long, ListenEvent> getEvents() {
		return events;
	}

	public Tabla<String, Genre> getGenres() {
		return genres;
	}

	public Tabla<String, Lista<ListenEvent>> getEventsByGenre() {
		return eventsByGenre;
	}

	public Tabla<String, Lista<String>> getHashtagsByTrack() {
		return hashtagsByTrack;
	}

	public Tabla<String, Float> getVaderByHashtag() {
		return vaderByHashtag;
	}

	public Tabla<String, Integer> getUniqueArtists(Iterable<ListenEvent> eventIterable) {
		Tabla<String, Integer> artists = new TablaHashLinearProbing<>();
		int newAmount;
		for (ListenEvent event : eventIterable) {
			newAmount = artists.containsKey(event.getArtistId()) ? artists.get(event.getArtistId()) + 1 : 0;
			artists.put(event.getArtistId(), newAmount);
		}
		return artists;
	}

	public Tabla<String, Integer> getUniqueTracks(Iterable<ListenEvent> eventIterable) {
		Tabla<String, Integer> tracks = new TablaHashLinearProbing<>();
		int newAmount;
		for (ListenEvent event : eventIterable) {
			newAmount = tracks.containsKey(event.getTrackId()) ? tracks.get(event.getTrackId()) + 1 : 0;
			tracks.put(event.getTrackId(), newAmount);
		}
		return tracks;
	}

	public float getAverageVader(String trackId) {
		float total = 0;
		int amount = 0;
		for (String hashTag : hashtagsByTrack.get(trackId)) {
			if(vaderByHashtag.containsKey(hashTag)) {
				total += vaderByHashtag.get(hashTag);
				amount++;
			}
		}
		return amount != 0 ? total / amount : total;
	}

}
