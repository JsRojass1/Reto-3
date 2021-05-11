package data_structures.sort;

import java.util.Comparator;

@SuppressWarnings("unchecked")
public class ShellSort {

	public static <T> void sort(Comparable<T>[] a) {
		int N = a.length;
		int h = 1;
		Comparable<T> temp;
		while (h < N / 3)
			h = 3 * h + 1;
		while (h >= 1) {
			for (int i = h; i < N; i++) {
				for (int j = i; j >= h && a[j].compareTo((T) a[j - h]) < 0; j -= h) {
					temp = a[j];
					a[j] = a[j - h];
					a[j - h] = temp;
				}
			}
			h = h / 3;
		}

	}

	public static <T extends Comparable<T>> void sort(Object[] lista, Comparator<T> comparator) {
		int N = lista.length;
		int h = 1;
		try {
			while (h < N / 3)
				h = 3 * h + 1;
			while (h >= 1) {
				for (int i = h; i < N; i++) {
					for (int j = i; j >= h && comparator.compare((T)lista[j], (T)lista[j - h]) < 0; j -= h)
					{
						T elemento1 = (T) lista[j];
						lista[j] = lista[j - h];
						lista[j - h] = elemento1;
					}
				}
				h = h / 3;
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

	}

}
