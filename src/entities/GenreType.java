package entities;

public enum GenreType {
	REGGAE("Reggae", 60, 90),
	DOWN_TEMPO("Down-tempo",70, 100),
	CHILL_OUT("Chill-out", 90, 120),
	HIP_HOP("Hip-hop", 85, 115),
	JAZZ_AND_FUNK("Jazz and Funk", 120, 125),
	POP("Pop", 100, 130),
	R_AND_B("R&B", 60, 80),
	ROCK("Rock", 110, 140),
	METAL("Metal", 100, 160);

	private String name;
	private float minBPM;
	private float maxBPM;

	GenreType(String name, float minBPM, float maxBPM) {
		this.name = name;
		this.minBPM = minBPM;
		this.maxBPM = maxBPM;
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


