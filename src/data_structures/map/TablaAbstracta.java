package data_structures.map;

import java.util.Iterator;

import data_structures.list.ArregloDinamico;
import data_structures.list.Lista;

public abstract class TablaAbstracta<K extends Comparable<? super K>, V> implements Tabla<K, V> {

	public TablaAbstracta() { }

	public TablaAbstracta(Iterable<Entry<K, V>> iterable) {
		for (Entry<K, V> entry : iterable) {
			put(entry.key, entry.value);
		}
	}

	@Override
	public void putAll(Iterable<V> iterable, KeyPopulator<K, V> keyPopulator) {
		for(V value : iterable) {
			put(keyPopulator.key(value), value);
		}
	}

	@Override
	public boolean containsKey(K key) {
		return get(key) != null;
	}
	
	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public Iterable<K> keys() {
		Iterator<Entry<K, V>> iterator = iterator();
		return new Iterable<K>() {
			@Override
			public Iterator<K> iterator() {
				return new Iterator<K>() {

					@Override
					public boolean hasNext() {
						return iterator.hasNext();
					}

					@Override
					public K next() {
						return iterator.next().key;
					}
				};
			}
		};
	}

	@Override
	public Iterable<V> values() {
		Iterator<Entry<K, V>> iterator = iterator();
		return new Iterable<V>() {
			@Override
			public Iterator<V> iterator() {
				return new Iterator<V>() {

					@Override
					public boolean hasNext() {
						return iterator.hasNext();
					}

					@Override
					public V next() {
						return iterator.next().value;
					}
				};
			}
		};
	}
	
	@Override
	public Lista<Entry<K, V>> entryList() {
		return new ArregloDinamico<>(iterator());
	}
	
	@Override
	public Lista<K> keyList() {
		return new ArregloDinamico<>(keys());
	}
	
	@Override
	public Lista<V> valueList() {
		return new ArregloDinamico<>(values());
	}

}
