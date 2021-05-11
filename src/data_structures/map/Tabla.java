package data_structures.map;

import data_structures.list.Lista;

public interface Tabla<K extends Comparable<? super K>, V> extends Iterable<Entry<K, V>> {

	void put(K key, V value);

	void putAll(Iterable<V> iterable, KeyPopulator<K, V> keyPopulator);

	V get(K key);

	V remove(K key);
	
	boolean containsKey(K key);

	int size();
	
	void clear();
	
	boolean isEmpty();

	Iterable<K> keys();
	
	Iterable<V> values();
	
	Lista<Entry<K, V>> entryList();
	
	Lista<K> keyList();
	
	Lista<V> valueList();

}


