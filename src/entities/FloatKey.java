package entities;

import java.util.Objects;

public class FloatKey implements Comparable<FloatKey> {

	private long id;
	private float value;

	public FloatKey(long id, float value) {
		this.id = id;
		this.value = value;
	}

	public FloatKey(float value) {
		this(0, value);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		FloatKey floatKey = (FloatKey) o;
		return id == floatKey.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public int compareTo(FloatKey o) {
		int comparison =  Float.compare(value, o.value);
		return comparison == 0 ? Long.compare(id, o.id) : comparison;
	}

	@Override
	public String toString() {
		return "id=" + id + ", value=" + value;
	}

}
