package controller;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

import controller.ControllerException;
import data_structures.list.ArregloDinamico;
import data_structures.list.Lista;
import data_structures.map.bst.RedBlackTree;
import entities.ContentCharacteristic;
import entities.ContentFilter;
import entities.Genre;
import entities.ListenEvent;
import model.Modelo;
import view.View;

import static entities.ContentCharacteristic.*;

@SuppressWarnings("ALL")
public class Controller {

	private static final String LINE = new String(new char[100]).replace("\0", "-");
	private static final String HEADER = new String(new char[100]).replace("\0", "*");
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

	/**
	 * Instancia del Modelo
	 */
	private Modelo modelo;

	/**
	 * Instancia de la Vista
	 */
	private View view;

	private Scanner lector;

	public Controller() {
		view = new View();
		try {
			modelo = new Modelo(0);
			mostrarInfo("Carga de datos small exitosa");
		}
		catch (Exception e) {
			mostrarError("Error cargando los datos. " + e.getMessage());
		}
	}

	public void run() throws Exception {
		lector = new Scanner(System.in);
		boolean fin = false;
		while (!fin) {
			view.printMenu();
			try {
				int option = convertirAEntero(lector.nextLine());
				boolean showWait = true;
				switch (option) {
					case 0:
						cargarModelo();
						break;
					case 1:
						caracterizarReproducciones();
						break;
					case 2:
						musicaParaFestejar();
						break;
					case 3:
						musicaParaEstudiar();
						break;
					case 4:
						reproduccionesPorGenero();
						break;
					case 5:
						generoMasPopularPorTiempo();
						break;
					case 9:
						mostrarInfo("Hasta pronto");
						lector.close();
						fin = true;
						showWait = false;
						System.exit(0);
						break;
					default:
						mostrarError("Opcion Invalida");
						showWait = false;
						break;
				}
				if (showWait) {
					mostrarEspera("Enter para continuar...");
				}
				view.printMessage(HEADER);
			}
			catch (ControllerException e) {
				mostrarError(e.getMessage());
				esperar(1000);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void cargarModelo() throws ControllerException {
		mostrarInfo("Cargar datos");
		view.printMenuSizeDatos();
		int tamanoDatos = convertirAEntero(lector.nextLine());
		escribir("Cargando...");
		long startTime = System.currentTimeMillis();
		try {
			modelo = new Modelo(tamanoDatos);
			mostrarInfo("Procedimiento exitoso");
		}
		catch (Exception e) {
			throw new ControllerException("Error cargando los datos. " + e.getMessage());
		}
		long endTime = System.currentTimeMillis();
		double duration = (endTime - startTime) / 1000D;
		Lista<ListenEvent> list = modelo.getEvents().valueList();
		escribir("La duración de la carga de datos fué: " + new DecimalFormat("#.###").format(duration) + " s\n");
		escribir("- Total de registros de eventos de escucha cargados: " + list.size());
		escribir("- Total de artistas únicos cargados: " + modelo.getUniqueArtists(list).size());
		escribir("- Total de pistas de audio únicas cargadas: " + modelo.getUniqueTracks(list).size());
		//Estos primeros 5 y ultimos 5 son según el hasmap, por lo que no son los primeros según lineas leídas sino según hash
		escribir("- Primeros 5 eventos:");
		for (int i = 0; i < list.size() && i < 5; i++) {
			escribir("\t" + list.darElemento(i).toString());
		}
		escribir("- Ultimos 5 eventos:");
		for (int i = list.size() - 5; i < list.size(); i++) {
			escribir("\t" + list.darElemento(i).toString());
		}
	}

	private void caracterizarReproducciones() throws ControllerException {
		verificarModelo();
		mostrarInfo("1. Caracterizar reproducciones");
		ContentFilter filter = readFilter(null);
		RedBlackTree<Long, ListenEvent> events = modelo.filter(filter);
		escribir("- Total de eventos de escucha con caracteristica " + filter.getCharacteristic() + " entre " + filter.getMin() + " y " + filter.getMax() + ": " + events.size());
		escribir("- Total de artistas únicos: " + modelo.getUniqueArtists(events.values()).size());
	}

	private void musicaParaFestejar() throws ControllerException {
		verificarModelo();
		mostrarInfo("2. Encontrar música para festejar");
		encontrarMusicaEntre(ENERGY, DANCEABILITY);
	}

	private void musicaParaEstudiar() throws ControllerException {
		verificarModelo();
		mostrarInfo("3. Encontrar música para estudiar/trabajar");
		encontrarMusicaEntre(INSTRUMENTALNESS, TEMPO);
	}

	private void reproduccionesPorGenero() throws ControllerException {
		verificarModelo();
		mostrarInfo("4. Estimar las reproducciones de los géneros musicales");
		escribir("[0] Buscar por lista de generos existentes");
		escribir("[1] Buscar con parametros nuevos");
		Lista<String> genreNames = new ArregloDinamico<>();
		int option = convertirAEntero(lector.nextLine());
		switch (option) {
			case 0:
				escribir("Escribe la lista de generos a buscar separados por coma, puedes usar el nombre o el indice:");
				int index = 0;
				Lista<String> genres = modelo.getGenres().keyList();
				for (String genreName : genres) {
					Genre genre = modelo.getGenres().get(genreName);
					escribir("- [" + index + "] " + genreName + " BPM entre " + genre.getMinBPM() + " - " + genre.getMaxBPM());
					index++;
				}
				String input = lector.nextLine();
				if(input.isBlank()) {
					throw new ControllerException("Se debe introducir la lista de generos");
				}
				String[] parts = input.split(",");
				for(String part : parts) {
					part = part.trim();
					try {
						index = Integer.parseInt(part);
						if(index < 0 || index >= genres.size()) {
							throw new ControllerException("El índice '" + index + "' inválido");
						}
						genreNames.agregar(genres.darElemento(index));
					}
					catch (NumberFormatException e) {
						try {
							genreNames.agregar(modelo.getGenre(part).getName());
						}
						catch (IllegalArgumentException i) {
							throw new ControllerException("El nombre '" + part + "' no es válido");
						}
					}
				}
				break;
			case 1:
				escribir("Escribe el nombre del nuevo genero personalizado:");
				String genreName = lector.nextLine().trim();
				if(modelo.getGenres().containsKey(genreName)) {
					throw new ControllerException("El nombre '" + genreName + "' ya existe");
				}
				ContentFilter filter = readFilter(TEMPO);
				modelo.addGenre(new Genre(genreName, filter.getMin(), filter.getMax()));
				genreNames.agregar(genreName);
				break;
			default:
				throw new ControllerException("Opción inválida");
		}
		Genre genre;
		Lista<ListenEvent> eventsInGenre;
		for(String genreName : genreNames) {
			genre = modelo.getGenres().get(genreName);
			eventsInGenre = modelo.getEventsByGenre().get(genreName);
			mostrarInfo(genreName);
			escribir("El tempo de " + genreName + " está entre " + genre.getMinBPM() + " y " + genre.getMaxBPM());
			escribir("Reproducciones: " + eventsInGenre.size());
			int i = 0;
			for(String artistId : modelo.getUniqueArtists(eventsInGenre).keys()) {
				escribir("- Artista " + (i + 1) + ": " + artistId);
				i++;
				if(i >= 10) {
					break;
				}
			}
		}
	}

	private void generoMasPopularPorTiempo() throws ControllerException {
		verificarModelo();
		mostrarInfo("5. Indicar el género musical más escuchado en un tiempo");
		escribir("Introduce hora de inicio (HH:mm):");
		LocalTime start = convertirATime(lector.nextLine());
		escribir("Introduce hora final (HH:mm):");
		LocalTime end = convertirATime(lector.nextLine());
		Genre mostListened = null;
		Lista<ListenEvent> eventsMostListeded = new ArregloDinamico<>();
		long totalReproductions = 0;
		for(Genre genre : modelo.getGenres().values()) {
			Lista<ListenEvent> eventsInGenre = modelo.getEventsByGenre().get(genre.getName());
			RedBlackTree<LocalTime, ListenEvent> eventsByTime = new RedBlackTree<>();
			eventsByTime.putAll(eventsInGenre, event -> event.getCreatedAt().toLocalTime());
			Lista<ListenEvent> eventsInTime = eventsByTime.valuesInRange(start, end);
			if(eventsInTime.size() > eventsMostListeded.size()) {
				mostListened = genre;
				eventsMostListeded = eventsInTime;
			}
			totalReproductions += eventsInTime.size();
		}
		if(mostListened == null) {
			throw new ControllerException("No hay generos cargados");
		}
		mostrarInfo("Genero más popular en este tiempo");
		escribir("Hay un total de " + totalReproductions + " reproducciones entre las " + start.format(TIME_FORMATTER) + " y las " + end.format(TIME_FORMATTER));
		escribir("El genero con mas reproducciones es: " + mostListened.getName() + " con " + eventsMostListeded.size() + " reproducciones...");
		mostrarInfo("Analisis de sentimientos para " + mostListened.getName() + " en este rango");
		int i = 0;
		float vaderAvg;
		for(String trackId : modelo.getUniqueTracks(eventsMostListeded).keys()) {
			vaderAvg = modelo.getAverageVader(trackId);
			escribir("- Track " + (i + 1) + ": " + trackId + " con " + modelo.getHashtagsByTrack().get(trackId).size() + " hashtags" + (vaderAvg != 0 ? " y un VADER promedio de " + vaderAvg  : ". No hay VADER registrado"));
			i++;
			if(i >= 10) {
				break;
			}
		}
	}

	private void encontrarMusicaEntre(ContentCharacteristic... characteristics) throws ControllerException {
		ContentFilter[] filters = new ContentFilter[characteristics.length];
		for (int i = 0; i < characteristics.length; i++) {
			filters[i] = readFilter(characteristics[i]);
		}
		Lista<ListenEvent> events = modelo.filter(filters).valueList();
		escribir("- Total de pistas únicas: " + modelo.getUniqueTracks(events).size());
		//Seleccionar 5 elementos aleatorios
		Random random = new Random();
		for (int i = 1; i <= 5 && events.size() > 0; i++) {
			final ListenEvent event = events.darElemento(random.nextInt(events.size()));
			if (event != null) {
				events.eliminar(event);
				escribir("Track " + i + ": " + event.getTrackId() + " con " + String.join(" y ", Arrays.asList(characteristics).stream().map(characteristic -> characteristic + " de " + event.getCharacteristic(characteristic)).collect(Collectors.toList())));
			}
		}
	}

	private ContentFilter readFilter(ContentCharacteristic characteristic) throws ControllerException {
		if (characteristic == null) {
			escribir("Inserta caracteristica de contenido, por nombre o indice los posibles valores son:");
			ContentCharacteristic[] characteristics = ContentCharacteristic.values();
			for (int i = 0; i < characteristics.length; i++) {
				escribir("- [" + i + "] " + characteristics[i].name());
			}
			String input = lector.nextLine().trim();
			try {
				int index = Integer.parseInt(input);
				if (index < 0 || index >= characteristics.length) {
					throw new ControllerException("El índice de la característica no es válido");
				}
				characteristic = characteristics[index];
			}
			catch (NumberFormatException e) {
				try {
					characteristic = ContentCharacteristic.valueOf(input.toUpperCase());
				}
				catch (IllegalArgumentException i) {
					throw new ControllerException("El nombre de la característica dado no es válido");
				}
			}
		}
		escribir("Inserta valor mínimo de la característica " + characteristic + ":");
		float min = convertirAFloat(lector.nextLine());
		escribir("Inserta valor máximo de la característica " + characteristic + ":");
		float max = convertirAFloat(lector.nextLine());
		if (min > max) {
			throw new ControllerException("El valor mínimo es mayor al máximo");
		}
		return new ContentFilter(characteristic, min, max);
	}

	/*
	 * --------------------------------------------------
	 * METODOS DE AYUDA
	 * --------------------------------------------------
	 */

	private void verificarModelo() throws ControllerException {
		if (modelo == null) {
			throw new ControllerException("No se ha cargado el modelo aún");
		}
	}

	private LocalDate convertirADate(String input) throws ControllerException {
		try {
			return LocalDate.parse(input, DATE_FORMATTER);
		}
		catch (DateTimeParseException e) {
			throw new ControllerException("Formato de fecha invalido, debe ser yyyy-MM-dd");
		}
	}

	private LocalTime convertirATime(String input) throws ControllerException {
		try {
			return LocalTime.parse(input, TIME_FORMATTER);
		}
		catch (DateTimeParseException e) {
			throw new ControllerException("Formato de fecha invalido, debe ser HH:mm");
		}
	}

	private int convertirAEntero(String entrada) throws ControllerException {
		try {
			return Integer.parseInt(entrada);
		}
		catch (Exception e) {
			throw new ControllerException("Valor no valido como número entero.");
		}
	}

	private float convertirAFloat(String entrada) throws ControllerException {
		try {
			return Float.parseFloat(entrada);
		}
		catch (Exception e) {
			throw new ControllerException("Valor no valido como número decimal.");
		}
	}

	private void escribir(String mensaje) {
		view.printMessage(mensaje);
	}

	private void mostrarEspera(String mensaje) {
		escribir(mensaje);
		lector.nextLine();
	}

	private void mostrarInfo(String mensaje) {
		view.printMessage(LINE);
		view.printMessage(mensaje);
		view.printMessage(LINE);
	}

	private void mostrarError(String mensaje) {
		view.printMessage(LINE);
		view.printMessage("Error: " + mensaje);
		view.printMessage(LINE);
	}

	private void esperar(long millis) {
		try {
			Thread.sleep(millis);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}