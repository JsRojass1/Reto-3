package data_structures.map.bst;

import data_structures.map.Entry;

public abstract class AbstractNode<K, V, N extends AbstractNode<K, V, N>> {

	public Entry<K, V> entry;
	public N left;
	public N right;

	public AbstractNode(K key, V value) {
		entry = new Entry<>(key, value);
	}

	@Override
	public String toString() {
		return entry.key + (
				(left != null && right != null) ? "" :
				(
						"(" +
						(left != null ? "L:" + left.toString() + ", " : "") +
						(right != null ? "R:" + right.toString() : "")
				) + ")"
		);
	}

}