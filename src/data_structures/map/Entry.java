package data_structures.map;

public class Entry<K, V> implements Comparable<Entry<K, V>> {

	public K key;
	public V value;

	public Entry(K key, V value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Entry)) {
			return false;
		}
		Entry<?, ?> otherEntry = (Entry<?, ?>) other;
		return key.equals(otherEntry.key) && value.equals(otherEntry.value);
	}

	@Override
	public int hashCode() {
		return key.hashCode();
	}

	@Override
	public int compareTo(Entry<K, V> o) {
		if (key instanceof Comparable && o.key instanceof Comparable) {
			return ((Comparable) key).compareTo(((Comparable) o.key));
		}
		return 0;
	}

	@Override
	public String toString() {
		return "{K=" + key + ", " + "V=" + value + "}";
	}

}