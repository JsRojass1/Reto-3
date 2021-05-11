package data_structures.map.hash;

import java.util.Iterator;

import data_structures.map.Entry;

public class TablaHashSeparateChaining<K extends Comparable<? super K>, V> extends TablaHash<K, V> {
	
	private static final int START_MODULUS = 1021;

	private Node[] chains;

	public TablaHashSeparateChaining() {
		super(START_MODULUS);
		 chains = new Node[m];
	}
	
	@Override
	public void clear() {
		super.clear();
		m = START_MODULUS;
		chains = new Node[m];
	}

	@Override
	public void put(K key, V value) {
		if(key == null) {
			throw new IllegalArgumentException("Null key");
		}
		int i = hash(key);
		for (Node x = chains[i]; x != null; x = x.next) {
			if (key.equals(x.key)) {
				x.value = value;
				return;
			}
		}
		chains[i] = new Node(key, value, chains[i]);
		n++;
		if (n / m >= 8) {
			rehash(m * 2);
		}
	}


	@Override
	protected void rehash(int modulus) {
		Node[] oldChains = chains;
		m = modulus;
		n = 0;
		chains = new Node[m];
		for (int i = 0; i < oldChains.length; i++)
			for (Node x = oldChains[i]; x != null; x = x.next)
				put((K) x.key, (V) x.value);
	}

	@Override
	public V get(K key) {
		if(key == null) {
			throw new IllegalArgumentException("Null key");
		}
		int i = hash(key);
		for (Node x = chains[i]; x != null; x = x.next)
			if (key.equals(x.key))
				return (V) x.value;
		return null;
	}

	@Override
	public V remove(K key) {
		if(key == null) {
			throw new IllegalArgumentException("Null key");
		}
		int i = hash(key);
		Node last = null;
		for (Node node = chains[i]; node != null; node = node.next) {
			if (key.equals(node.key)) {
				if (last == null) {
					chains[i] = null;
				} else {
					last.next = node.next;
				}
				n--;
				if (n / m <= 2) {
					rehash(m / 2);
				}
				return (V) node.value;
			}
			last = node;
		}
		return null;
	}

	@Override
	public boolean containsKey(K key) {
		if(key == null) {
			throw new IllegalArgumentException("Null key");
		}
		return get(key) != null;
	}

	@Override
	public Iterator<Entry<K, V>> iterator() {
		return new EntryIterator();
	}

	@Override
	public String toString() {
		String description = "{";
		Node node;
		int rowCount;
		for (int i = 0; i < chains.length; i++) {
			node = chains[i];
			description += "\n {\n";
			rowCount = 0;
			while (node != null) {
				description += "  {K=" + node.key + ", " + "V=" + node.value + "}";
				rowCount++;
				if (node.next != null) {
					description += ",\n";
				}
				node = node.next;
			}
			description += "\n } (" + rowCount + ")";
			if (i < chains.length - 1) {
				description += ",";
			}
		}
		description += "\n}";
		description += "\nN = " + n + " M = " + m + " N/M = " + (n / (float) m);
		return description;
	}

	private static class Node {

		Object key;

		Object value;

		Node next;

		public Node(Object key, Object value, Node next) {
			this.key = key;
			this.value = value;
			this.next = next;
		}

	}

	private class EntryIterator implements Iterator<Entry<K, V>> {

		private int index;
		private Node current;

		@Override
		public boolean hasNext() {
			if (current != null) {
				return current != null;
			}
			for (int i = index; i < m; i++) {
				if (chains[i] != null) {
					return true;
				}
			}
			return false;
		}

		@Override
		public Entry<K, V> next() {
			
			if (current == null) {
				while (index < m && current == null) {
					if (chains[index] != null) {
						current = chains[index];
					}
					index++;
				}
			}
			Entry<K, V> next;
			if(current != null) {
				next = new Entry(current.key, current.value);
				current = current.next;
			}
			else {
				next = null;
			}
			return next;
		}

	}

}
