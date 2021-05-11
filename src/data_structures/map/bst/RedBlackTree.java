package data_structures.map.bst;

import data_structures.map.bst.RedBlackTree.Node;

public class RedBlackTree<K extends Comparable<? super K>, V> extends BinaryTree<K, V, Node<K, V>> {

	public static final boolean RED = true;
	public static final boolean BLACK = false;

	@Override
	Node<K, V> newNode(K key, V value) {
		return root == null ? new Node<>(key, value, RED) : new Node<>(key, value);
	}

	@Override
	public void put(K key, V value) {
		super.put(key, value);
		root.color = BLACK;
	}

	@Override
	protected Node<K, V> put(Node<K, V> head, K key, V value) {
		return balance(super.put(head, key, value));
	}

	private Node<K, V> rotateRight(Node<K, V> pivot) {
		Node<K, V> rotated = pivot.left;
		pivot.left = rotated.right;
		rotated.right = pivot;
		rotated.color = rotated.right.color;
		rotated.right.color = RED;
		return rotated;
	}

	private Node<K, V> rotateLeft(Node<K, V> pivot) {
		Node<K, V> rotated = pivot.right;
		pivot.right = rotated.left;
		rotated.left = pivot;
		rotated.color = rotated.left.color;
		rotated.left.color = RED;
		return rotated;
	}

	private void flipColors(Node<K, V> node) {
		node.recolor();
		node.left.recolor();
		node.right.recolor();
	}

	private Node<K, V> moveRedLeft(Node<K, V> node) {
		flipColors(node);
		if (isRed(node.right.left)) {
			node.right = rotateRight(node.right);
			node = rotateLeft(node);
			flipColors(node);
		}
		return node;
	}

	private Node<K, V> moveRedRight(Node<K, V> node) {
		flipColors(node);
		if (isRed(node.left.left)) {
			node = rotateRight(node);
			flipColors(node);
		}
		return node;
	}

	private Node balance(Node<K, V> node) {
		if (isRed(node.right)) {
			node = rotateLeft(node);
		}
		if (isRed(node.left) && isRed(node.left.left)) {
			node = rotateRight(node);
		}
		if (isRed(node.left) && isRed(node.right)) {
			flipColors(node);
		}
		return node;
	}

	private boolean isRed(Node x) {
		if (x == null) {
			return false;
		}
		return x.color == RED;
	}

	public static class Node<K, V> extends AbstractNode<K, V, Node<K, V>> {

		public boolean color;

		public Node(K key, V value) {
			super(key, value);
		}

		public Node(K key, V value, boolean color) {
			this(key, value);
			this.color = color;
		}

		public void recolor() {
			color = !color;
		}

	}

}
