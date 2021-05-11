package data_structures.map.hash;

import java.util.Iterator;

import data_structures.map.Entry;

@SuppressWarnings("unchecked")
public class TablaHashLinearProbing<K extends Comparable<? super K>, V> extends TablaHash<K, V> {
	
	private static final int START_MODULUS = 16;

	private Object[] keys;
	private Object[] values;

	public TablaHashLinearProbing() {
		super(START_MODULUS);
		keys = new Object[m];
		values = new Object[m];
	}
	
	@Override
	public void clear() {
		super.clear();
		m = START_MODULUS;
		keys = new Object[m];
		values = new Object[m];
	}

	@Override
	public void put(K key, V value) {
		if(key == null)
			throw new IllegalArgumentException("Null key");
		int i;
		for (i = hash(key); keys[i] != null; i = (i + 1) % m)
			if (keys[i].equals(key)) {
				values[i] = value;
				return;
		}
		keys[i] = key;
		values[i] = value;
		n++;
		if (n >= m / 2)
			rehash(m * 2);
	}

	@Override
	public V get(K key) {
		for (int i = hash(key); keys[i] != null; i = (i + 1) % m)
			if (keys[i].equals(key))
				return (V) values[i];
		return null;
	}

	@Override
	public V remove(K key) {
		if (!containsKey(key)) {
			return null;
		}
		int i = hash(key);
		while (!key.equals(keys[i])) {
			i = (i + 1) % m;
		}
		V storedValue = (V) values[i];
		keys[i] = null;
		values[i] = null;
		i = (i + 1) % m;
		K tempKey;
		V tempValue;
		while (keys[i] != null) {
			tempKey = (K) keys[i];
			tempValue = (V) values[i];
			keys[i] = null;
			values[i] = null;
			n--;
			put(tempKey, tempValue);
			i = (i + 1) % m;
		}
		n--;
		if (n > 0 || n == m / 8) {
			rehash(m / 2);
		}
		return storedValue;
	}

	@Override
	public boolean containsKey(K key) {
		for (int i = hash(key); keys[i] != null; i = (i + 1) % m)
			if (keys[i].equals(key))
				return true;
		return false;
	}

	@Override
	protected void rehash(int modulus) {
		Object[] tempKeys = keys;
		Object[] tempValues = values;
		keys = new Object[modulus];
		values = new Object[modulus];
		m = modulus;
		System.arraycopy(tempKeys, 0, keys, 0, tempKeys.length);
		System.arraycopy(tempValues, 0, values, 0, tempValues.length);
	}

	@Override
	public String toString() {
		String description = "{\n";
		for (int i = 0; i < keys.length; i++) {
			if (keys[i] != null) {
				description += " {K=" + keys[i] + ", " + "V=" + values[i] + "}";
			} else {
				description += " {}";
			}
			if (i < keys.length - 1) {
				description += ",\n";
			}
		}
		description += "\n}";
		description += "\nN = " + n + " M = " + m + " N/M = " + (n / (float) m);
		return description;
	}

	@Override
	public Iterator<Entry<K, V>> iterator() {
		return new EntryIterator();
	}

	private class EntryIterator implements Iterator<Entry<K, V>> {

		private int count;
		private int index = -1;

		@Override
		public boolean hasNext() {
			return index < m - 1 && count < n;
		}

		@Override
		public Entry<K, V> next() {
			if (!hasNext()) {
				return null;
			}
			index++;
			while (index < m) {
				if (keys[index] != null) {
					count++;
					return new Entry(keys[index], values[index]);
				}
				index++;
			}
			return null;
		}

	}

}
