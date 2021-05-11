package main;
import controller.Controller;

public class Main {
	
	public static void main(String[] args) {
		try {
			Controller controler = new Controller();
			controler.run();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
