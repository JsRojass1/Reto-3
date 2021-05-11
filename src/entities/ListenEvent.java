package entities;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

import static entities.ContentCharacteristic.*;

public class ListenEvent implements Comparable<ListenEvent> {

	private long id, userId;
	private float[] characteristics;
	private String
			artistId,
			tweetLang,
			timeZone,
			trackId,
			lang;
	private LocalDateTime createdAt;

	private ListenEvent() {
		characteristics = new float[values().length];
	}

	public float getCharacteristic(ContentCharacteristic characteristic) {
		return characteristics[characteristic.ordinal()];
	}

	public void setCharacteristic(ContentCharacteristic characteristic, Float value) {
		characteristics[characteristic.ordinal()] = value;
	}

	public long getId() {
		return id;
	}

	public String getArtistId() {
		return artistId;
	}

	public String getTweetLang() {
		return tweetLang;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public String getTrackId() {
		return trackId;
	}

	public String getLang() {
		return lang;
	}

	public long getUserId() {
		return userId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ListenEvent event = (ListenEvent) o;
		return id == event.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public int compareTo(ListenEvent o) {
		return Long.compare(id, o.id);
	}

	@Override
	public String toString() {
		return "id=" + id + ", characteristics=" + Arrays.toString(characteristics);
	}

	public static class ListenEventBuilder {

		private ListenEvent event;

		public ListenEventBuilder() {
			event = new ListenEvent();
		}

		public ListenEventBuilder withCharacteristics(float[] characteristics) {
			event.characteristics = characteristics;
			return this;
		}

		public ListenEventBuilder withInstrumentalness(float instrumentalness) {
			event.setCharacteristic(INSTRUMENTALNESS, instrumentalness);
			return this;
		}

		public ListenEventBuilder withLiveness(float liveness) {
			event.setCharacteristic(LIVENESS, liveness);
			return this;
		}

		public ListenEventBuilder withSpeechiness(float speechiness) {
			event.setCharacteristic(SPEECHINESS, speechiness);
			return this;
		}

		public ListenEventBuilder withDanceability(float danceability) {
			event.setCharacteristic(DANCEABILITY, danceability);
			return this;
		}

		public ListenEventBuilder withValence(float valence) {
			event.setCharacteristic(VALENCE, valence);
			return this;
		}

		public ListenEventBuilder withLoudness(float loudness) {
			event.setCharacteristic(LOUDNESS, loudness);
			return this;
		}

		public ListenEventBuilder withTempo(float tempo) {
			event.setCharacteristic(TEMPO, tempo);
			return this;
		}

		public ListenEventBuilder withAcousticness(float acousticness) {
			event.setCharacteristic(ACOUSTICNESS, acousticness);
			return this;
		}

		public ListenEventBuilder withEnergy(float energy) {
			event.setCharacteristic(ENERGY, energy);
			return this;
		}

		public ListenEventBuilder withMode(float mode) {
			event.setCharacteristic(MODE, mode);
			return this;
		}

		public ListenEventBuilder withKey(float key) {
			event.setCharacteristic(KEY, key);
			return this;
		}

		public ListenEventBuilder withId(long id) {
			event.id = id;
			return this;
		}

		public ListenEventBuilder withArtistId(String artistId) {
			event.artistId = artistId;
			return this;
		}

		public ListenEventBuilder withTweetLang(String tweetLang) {
			event.tweetLang = tweetLang;
			return this;
		}

		public ListenEventBuilder withTimezone(String timeZone) {
			event.timeZone = timeZone;
			return this;
		}

		public ListenEventBuilder withTrackId(String trackId) {
			event.trackId = trackId;
			return this;
		}

		public ListenEventBuilder withLang(String lang) {
			event.lang = lang;
			return this;
		}

		public ListenEventBuilder withUserId(long userId) {
			event.userId = userId;
			return this;
		}

		public ListenEventBuilder withCreatedAt(LocalDateTime createdAt) {
			event.createdAt = createdAt;
			return this;
		}

		public ListenEvent build() {
			return event;
		}

	}

}
