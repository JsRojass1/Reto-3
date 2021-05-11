package data_structures.map.hash;

import data_structures.map.TablaAbstracta;

public abstract class TablaHash<K extends Comparable<? super K>, V> extends TablaAbstracta<K, V> {
	
	protected int n;
	protected int m;
	
	public TablaHash(int m) {
		this.m = m;
	}
	
	public int hash(K key) {
		return (key.hashCode() & 0x7fffffff) % m;
	}
	
	@Override
	public int size() {
		return n;
	}
	
	@Override
	public void clear() {
		n = 0;
	}
	
	protected abstract void rehash(int modulus);

}
