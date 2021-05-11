package view;

import static java.lang.System.out;

public class View {
	/**
	 * Metodo constructor
	 */
	public View() {

	}

	public void printMessage(String message) {
		System.out.println(message);
	}

	public void printMenu() {
		out.println("[0]. Realizar la carga de datos");
		out.println("[1]. Caracterizar las reproducciones.");
		out.println("[2]. Encontrar música para festejar.");
		out.println("[3]. Encontrar música para estudiar/trabajar.");
		out.println("[4]. Estimar las reproducciones de los géneros musicales.");
		out.println("[5]. Indicar el género musical más escuchado en un tiempo.");
		out.println("[9]. Cerrar programa.");
		out.println();
		out.println("Dar el numero de opción para ejecutar, luego oprimir ENTER:");
	}

	public void printMenuSizeDatos() {
		System.out.println("Seleccionar el tamaño de datos a usar: ");
		System.out.println("[0]. Datos Small.");
		System.out.println("[5]. 5% de datos originales.");
		System.out.println("[10]. 10% de datos originales.");
		System.out.println("[20]. 10% de datos originales.");
		System.out.println("...");
		System.out.println("[100]. 100% de datos originales.");
		out.println("Dar el numero de opción para ejecutar, luego oprimir ENTER:");
	}

}