package data_structures.map;

import data_structures.list.Lista;

public interface TablaOrdenada<K extends Comparable<? super K>, V> extends Tabla<K, V> {

	K min();

	K max();

	int height();

	Lista<K> keySet();

	Lista<K> keysInRange(K init, K end);

	Lista<V> valuesInRange(K init, K end);
	
}
