package data_structures.map.hash;

public class Nodo <E> {
	
	private E objeto;
	
	private Nodo<E> siguiente;
	
	public Nodo (E objeto) {
		this.objeto = objeto;
		siguiente = null;
	}
	
	public Nodo<E> getNext() {
		return siguiente;
	}
	
	public void asignarSiguiente (Nodo<E> siguiente) {
		this.siguiente = siguiente;
	}
	
	public E darElemento() {
		return objeto;
	}
	
	public void asignarElemento(E objeto) {
		this.objeto = objeto;
	}
	
}
