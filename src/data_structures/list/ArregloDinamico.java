package data_structures.list;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

/**
 * 2019-01-23 Estructura de Datos Arreglo Dinamico de Strings. El arreglo al
 * llenarse (llegar a su maxima capacidad) debe aumentar su capacidad.
 *
 *
 */
@SuppressWarnings("unchecked")
public class ArregloDinamico<T> implements Lista<T> {
	/**
	 * Capacidad maxima del arreglo
	 */
	private int tamanoMax;
	/**
	 * Numero de elementos presentes en el arreglo (de forma compacta desde la
	 * posicion 0)
	 */
	private int tamanoAct;
	/**
	 * Arreglo de elementos de tamaNo maximo
	 */
	private Object[] elementos;
	
	public ArregloDinamico() {
		this(16);
	}

	public ArregloDinamico(Iterable<T> iterable) {
		this();
		addAll(iterable);
	}
	
	public ArregloDinamico(Iterator<T> iterator) {
		this();
		addAll(iterator);
	}

	/**
	 * Construir un arreglo con la capacidad maxima inicial.
	 * 
	 * @param max
	 *            Capacidad maxima inicial
	 */
	public ArregloDinamico(int max) {
		elementos = new Object[max];
		tamanoMax = max;
		tamanoAct = 0;
	}

	public void agregar(T dato) {
		if(dato == null) {
			throw new RuntimeException("Null element");
		}
		if (tamanoAct == tamanoMax)
			expandirArreglo();
		elementos[tamanoAct] = dato;
		tamanoAct++;
	}

	public int darCapacidad() {
		return tamanoMax;
	}

	public int size() {
		return tamanoAct;
	}

	public T darElemento(int i) {
		return (T) elementos[i];
	}

	public Object[] darElementos() {
		return elementos;
	}

	@Override
	public int isPresent(T dato) {
		// Recomendacion: Usar el criterio de comparacion natural (metodo
		// compareTo())
		// definido en Strings.
		if (tamanoAct == 0)
			return -1;
		for (int i = 0; i < tamanoAct; i++) {
			if (((Comparable<T>) elementos[i]).compareTo(dato) == 0) {
				return i;
			}
		}
		return -1;
	}

	public T eliminar(T dato) {
		// Recomendacion: Usar el criterio de comparacion natural (metodo
		// compareTo())
		// definido en Strings.
		T eliminar = null;
		boolean eliminado = false;

		if (tamanoAct == 0)
			return eliminar;

		for (int i = 0; i < tamanoAct && !eliminado; i++) {
			if (1 < tamanoAct) {
				if (((Comparable<T>) elementos[i]).compareTo(dato) == 0) {
					eliminar = (T) elementos[i];
					for (int j = i; j < tamanoAct - 1; j++) {
						elementos[j] = elementos[j + 1];
					}
					elementos[tamanoAct - 1] = null;
					--tamanoAct;
					eliminado = true;
				}
			} else {
				if (((Comparable<T>) elementos[i]).compareTo(dato) == 0) {
					eliminar = (T) elementos[i];
					elementos[i] = null;
					--tamanoAct;
					eliminado = true;
				}
			}
		}

		return eliminar;
	}

	public boolean isEmpty() {
		if (size() == 0)
			return true;
		return false;
	}

	@Override
	public void addFirst(T element) {
		if (tamanoAct == tamanoMax) { // caso de arreglo lleno (aumentar tamaNo)
			expandirArreglo();
		}
		for (int i = tamanoAct; i > 0; i--) {
			elementos[i] = elementos[i - 1];
		}
		elementos[0] = element;
		tamanoAct++;
	}

	@Override
	public void insertElement(T element, int pos) throws Exception {
		if (tamanoAct == tamanoMax)
			expandirArreglo();

		if (pos >= tamanoAct)
			throw new Exception("La posici�n insertada no es valida.");

		for (int i = tamanoAct; i > pos; i--) {
			elementos[i] = elementos[i - 1];
		}
		elementos[pos] = element;
		tamanoAct++;
	}

	@Override
	public T removeFirst() {
		T cabezaActual = (T) elementos[0];
		for (int i = 0; i < tamanoAct; i++) {
			elementos[i] = elementos[i + 1];
		}
		tamanoAct--;
		return cabezaActual;
	}

	@Override
	public T removeLast() {
		T ultimo = (T) elementos[tamanoAct - 1];
		elementos[tamanoAct - 1] = null;
		tamanoAct--;

		return ultimo;
	}

	@Override
	public T deleteElement(int pos) throws Exception {
		if (pos >= tamanoAct || pos < 0)
			throw new Exception("La posici�n insterdada no es valida.");
		T eliminado = (T) elementos[pos];
		for (int i = pos; i < tamanoAct; i++) {
			elementos[i] = elementos[i + 1];
		}
		elementos[tamanoAct - 1] = null;
		tamanoAct--;
		return eliminado;
	}
	
	public void vaciar() {
		for(int i = 0; i < tamanoAct; i++) {
			elementos[i] = null;
		}
		tamanoAct = 0;
	}

	@Override
	public T firstElement() {
		return (T) elementos[0];
	}

	@Override
	public T lastElement() {
		return (T) elementos[tamanoAct - 1];
	}

	@Override
	public void exchange(int pos1, int pos2) throws Exception {
		if (pos1 >= tamanoAct || pos2 >= tamanoAct || pos1 < 0 || pos2 < 0)
			throw new Exception("La posici�n insterdada no es valida.");
		T elementoPos1 = (T) elementos[pos1];
		elementos[pos1] = elementos[pos2];
		elementos[pos2] = elementoPos1;
	}

	@Override
	public void changeInfo(int pos, T elem) throws Exception {
		if (pos >= tamanoAct || pos < 0)
			throw new Exception("La posici�n insterdada no es valida.");
		elementos[pos] = elem;
	}

	public void expandirArreglo() {
		tamanoMax = 2 * tamanoMax;
		Object[] copia = elementos;
		elementos = new Object[tamanoMax];
		for (int i = 0; i < tamanoAct; i++) {
			elementos[i] = copia[i];
		}
	}

	@Override
	public Iterator<T> iterator() {
		return new ArregloDinamicoIterator();
	}

	public boolean equals(ArregloDinamico<T> o) {
		boolean equal = true;
		if (this.size() != o.size()) {
			equal = false;
			return equal;
		}
		for (int i = 0; i < size() && equal; i++) {
			if (!this.darElemento(i).equals(o.darElemento(i)))
				equal = false;
		}
		return equal;
	}

	@Override
	public String toString() {
		return "size: " + size();
	}

	private class ArregloDinamicoIterator implements Iterator<T> {

		private int index;

		public ArregloDinamicoIterator() {
			index = -1;
		}

		@Override
		public boolean hasNext() {
			return index < tamanoAct - 1;
		}

		@Override
		public T next() {
			return (T) elementos[++index];
		}

	}

	@Override
	public void sort(Comparator<T> comparator) {
		// TODO Auto-generated method stub
		
	}

}