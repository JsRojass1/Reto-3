package data_structures.list;

import java.util.Comparator;
import java.util.Iterator;

public interface Lista<T> extends Iterable<T> {

	void addFirst(T element);

	void agregar(T element);

	void insertElement(T element, int pos) throws Exception;

	T removeFirst();

	T removeLast();

	T deleteElement(int pos) throws Exception;

	T eliminar(T dato);

	T firstElement();

	T lastElement();

	T darElemento(int pos);

	int size();

	boolean isEmpty();

	int isPresent(T element);

	void exchange(int pos1, int pos2) throws Exception;

	void changeInfo(int pos, T elem) throws Exception;

	void sort(Comparator<T> comparator);

	default void addAll(Iterable<T> iterable) {
		for(T element : iterable) {
			agregar(element);
		}
	}

	default void addAll(Iterator<T> iterator){
		while (iterator.hasNext()) {
			agregar(iterator.next());
		}
	}

}
