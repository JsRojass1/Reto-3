package entities;

public class Genre {

	private String name;
	private float minBPM;
	private float maxBPM;

	public Genre(String name, float minBPM, float maxBPM) {
		this.name = name;
		this.minBPM = minBPM;
		this.maxBPM = maxBPM;
	}

	public Genre(GenreType genreType) {
		this(genreType.getName(), genreType.getMinBPM(), genreType.getMaxBPM());
	}

	public String getName() {
		return name;
	}

	public float getMinBPM() {
		return minBPM;
	}

	public float getMaxBPM() {
		return maxBPM;
	}

}