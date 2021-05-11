package entities;

public class ContentFilter {

	private ContentCharacteristic characteristic;
	private float min;
	private float max;

	public ContentFilter(ContentCharacteristic characteristic, float min, float max) {
		this.characteristic = characteristic;
		this.min = min;
		this.max = max;
	}

	public ContentCharacteristic getCharacteristic() {
		return characteristic;
	}

	public float getMin() {
		return min;
	}

	public float getMax() {
		return max;
	}

	@Override
	public String toString() {
		return "{characteristic = " + characteristic + ", min= " + min + ", max= " + max + "}";
	}

}
