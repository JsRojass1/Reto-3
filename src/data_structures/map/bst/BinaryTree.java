package data_structures.map.bst;

import java.util.Iterator;

import data_structures.list.ArregloDinamico;
import data_structures.list.Lista;
import data_structures.map.TablaAbstracta;
import data_structures.map.Entry;
import data_structures.map.TablaOrdenada;

public abstract class BinaryTree<K extends Comparable<? super K>, V, N extends AbstractNode<K, V, N>> extends TablaAbstracta<K, V> implements TablaOrdenada<K, V> {

	protected N root;
	protected N min;
	protected N max;
	protected int size;

	abstract N newNode(K key, V value);

	@Override
	public void put(K key, V value) {
		if (key == null) {
			throw new IllegalArgumentException("Key is null");
		}
		if (value == null) {
			throw new IllegalArgumentException("Value is null");
		}
		root = put(root, key, value);
	}

	protected N put(N head, K key, V value) {
		if (head == null) {
			head = newNode(key, value);
			if (min == null || min.entry.key.compareTo(key) >= 0) {
				min = head;
			}
			if (max == null || max.entry.key.compareTo(key) <= 0) {
				max = head;
			}
			size++;
			return head;
		}
		int comparison = head.entry.key.compareTo(key);
		//Go left
		if (comparison > 0) {
			head.left = put(head.left, key, value);
		}
		//Go right
		else if (comparison < 0) {
			head.right = put(head.right, key, value);
		}
		//Update value
		else {
			head.entry.value = value;
		}
		return head;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public K min() {
		return min != null ? min.entry.key : null;
	}

	public Entry<K, V> minEntry() {
		return min != null ? min.entry : null;
	}

	@Override
	public K max() {
		return max != null ? max.entry.key : null;
	}

	public Entry<K, V> maxEntry() {
		return max != null ? max.entry : null;
	}

	@Override
	public void clear() {
		root = null;
		min = null;
		max = null;
		size = 0;
	}

	@Override
	public int height() {
		return height(root);
	}

	private int height(N node) {
		int height = 0;
		if (node.left != null) {
			height = height(node.left);
		}
		if (node.right != null) {
			height = Math.max(height, height(node.right));
		}
		return node != null ? height + 1 : height;
	}

	@Override
	public V remove(K key) {
		throw new IllegalArgumentException("Method not implemented yet");
	}

	@Override
	public Lista<K> keySet() {
		ArregloDinamico<K> list = new ArregloDinamico<>(size);
		for (Entry<K, V> entry : entries()) {
			list.agregar(entry.key);
		}
		return list;
	}

	@Override
	public Lista<K> keysInRange(K init, K end) {
		ArregloDinamico<K> list = new ArregloDinamico<>();
		for (Entry<K, V> entry : entriesInRange(init, end)) {
			list.agregar(entry.key);
		}
		return list;
	}

	@Override
	public Lista<V> valuesInRange(K init, K end) {
		ArregloDinamico<V> list = new ArregloDinamico<>();
		for (Entry<K, V> entry : entriesInRange(init, end)) {
			list.agregar(entry.value);
		}
		return list;
	}

	@Override
	public V get(K key) {
		if (key == null) {
			throw new IllegalArgumentException("key is null");
		}
		return get(root, key);
	}

	private V get(N node, K key) {
		int comparison;
		if ((min != null && min.entry.key.compareTo(key) < 0) || (max != null && max.entry.key.compareTo(key) > 0)) {
			return null;
		}
		V value = null;
		while (node != null) {
			comparison = node.entry.key.compareTo(key);
			//Go left
			if (comparison > 0) {
				node = node.left;
			}
			//Go right
			else if (comparison < 0) {
				node = node.right;
			}
			//Match comparison
			else {
				//Value found
				if (node.entry.key.equals(key)) {
					return node.entry.value;
				}
				if (node.left != null) {
					value = get(node.left, key);
					if (value != null) {
						return value;
					}
				}
				if (node.right != null) {
					value = get(node.right, key);
					if (value != null) {
						return value;
					}
				}
				node = null;
			}
		}
		//No match in this subtree
		return null;
	}

	@Override
	public Iterator<Entry<K, V>> iterator() {
		return entries().iterator();
	}

	private Lista<Entry<K, V>> entriesInRange(K init, K end) {
		if (init == null) {
			throw new IllegalArgumentException("init can't be null");
		}
		if (end == null) {
			throw new IllegalArgumentException("end can't be null");
		}
		Lista<Entry<K, V>> range = new ArregloDinamico<>();
		if (root != null) {
			entriesInRange(root, range, init, end);
		}
		return range;
	}

	private void entriesInRange(N node, Lista<Entry<K, V>> range, K init, K end) {
		int initComp = node.entry.key.compareTo(init);
		int endComp = node.entry.key.compareTo(end);
		if (node.left != null && initComp > 0) {
			entriesInRange(node.left, range, init, end);
		}
		if (initComp >= 0 && endComp <= 0) {
			range.agregar(node.entry);
		}
		if (node.right != null && endComp < 0) {
			entriesInRange(node.right, range, init, end);
		}
	}

	private Lista<Entry<K, V>> entries() {
		Lista<Entry<K, V>> list = new ArregloDinamico<>(size);
		if (root != null) {
			traverse(root, list);
		}
		return list;
	}

	private void traverse(N node, Lista<Entry<K, V>> list) {
		if (node.left != null) {
			traverse(node.left, list);
		}
		list.agregar(node.entry);
		if (node.right != null) {
			traverse(node.right, list);
		}
	}

	@Override
	public String toString() {
		return root != null ? "{size: " + size + ", root: " + root.toString() + "}" : "Empty binary tree";
	}

}
