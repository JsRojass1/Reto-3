package data_structures.sort;

import java.util.Comparator;
import java.util.Iterator;

import data_structures.map.Entry;
import data_structures.map.Tabla;

@SuppressWarnings("unchecked")
public class MergeSort {

	@SuppressWarnings("rawtypes")
	private static Comparable[] aux;

	private static Object[] aux2;

	public static <K extends Comparable<K>, V> Iterator<Entry<K, V>> sort(
			Tabla<K, V> table,
			Comparator<Entry<K, V>> comparator) {
		return new EntryIterator<>(table, comparator);
	}

	// SORT ARRAYS OF COMPARABLE ELEMENTS

	@SuppressWarnings("rawtypes")
	public static void sort(Comparable[] a) {
		aux = new Comparable[a.length];
		sort(a, 0, a.length - 1); // Allocate space just once.
	}

	@SuppressWarnings("rawtypes")
	private static void sort(Comparable[] a, int lo, int hi) {
		if (hi <= lo)
			return; // Sort a[lo..hi].
		int mid = lo + (hi - lo) / 2;
		sort(a, lo, mid);
		// Sort left half.
		sort(a, mid + 1, hi);
		// Sort right half.
		merge(a, lo, mid, hi);
	}

	private static <T extends Comparable<T>> void merge(Comparable<T>[] a, int lo, int mid, int hi) {
		// Merge a[lo..mid] with a[mid+1..hi].
		int i = lo, j = mid + 1;
		for (int k = lo; k <= hi; k++)
			// Copy a[lo..hi] to aux[lo..hi].
			aux[k] = a[k];
		for (int k = lo; k <= hi; k++)
			// Merge back to a[lo..hi].
			if (i > mid)
				a[k] = aux[j++];
			else if (j > hi)
				a[k] = aux[i++];
			else if (aux[j].compareTo(aux[i]) < 0)
				a[k] = aux[j++];
			else
				a[k] = aux[i++];
	}

	// SORT ARRAYS OF ELEMENTS WITH A COMPARABLE
	
	private static <T> void sort(T[] a, Comparator<T> comparator) {
		aux2 = new Object[a.length];
		sort(a, 0, a.length - 1, comparator);
	}

	private static <T> void sort(T[] a, int lo, int hi, Comparator<T> comparator) {
		if (hi <= lo)
			return; // Sort a[lo..hi].
		int mid = lo + (hi - lo) / 2;
		sort(a, lo, mid, comparator);
		// Sort left half.
		sort(a, mid + 1, hi, comparator);
		// Sort right half.
		merge(a, lo, mid, hi, comparator);
	}

	private static <T> void merge(T[] a, int lo, int mid, int hi, Comparator<T> comparator) {
		// Merge a[lo..mid] with a[mid+1..hi].
		int i = lo, j = mid + 1;
		for (int k = lo; k <= hi; k++)
			// Copy a[lo..hi] to aux[lo..hi].
			aux2[k] = a[k];
		for (int k = lo; k <= hi; k++)
			// Merge back to a[lo..hi].
			if (i > mid)
				a[k] = (T) aux2[j++];
			else if (j > hi)
				a[k] = (T) aux2[i++];
			else if (comparator.compare((T) aux2[j], (T) aux2[i]) < 0)
				a[k] = (T) aux2[j++];
			else
				a[k] = (T) aux2[i++];
	}


	public static class EntryIterator<K extends Comparable<K>, V> implements Iterator<Entry<K, V>> {

		private int index;
		private Entry<K, V>[] sortedArray;

		public EntryIterator(Tabla<K, V> table, Comparator<Entry<K, V>> comparator) {
			sortedArray = new Entry[table.size()];
			int i = 0;
			for (Entry<K, V> entry : table) {
				sortedArray[i] = entry;
				i++;
			}
			sort(sortedArray, comparator);
			index = -1;
		}

		@Override
		public boolean hasNext() {
			return index < sortedArray.length - 1;
		}

		@Override
		public Entry<K, V> next() {
			return sortedArray[++index];
		}

	}

}