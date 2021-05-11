package data_structures.map.bst;

import data_structures.map.bst.BinarySearchTree.Node;

public class BinarySearchTree<K extends Comparable<? super K>, V> extends BinaryTree<K, V, Node<K, V>> {

	@Override
	Node<K, V> newNode(K key, V value) {
		return newNode(key, value);
	}

	public static class Node<K, V> extends AbstractNode<K, V, Node<K, V>> {

		public Node(K key, V value) {
			super(key, value);
		}

	}

}
