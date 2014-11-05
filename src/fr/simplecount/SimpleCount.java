package fr.simplecount;

import fr.simplecount.exceptions.AllocationException;
import fr.simplecount.window.MainWindow;

public class SimpleCount {

	public static void main(String[] args) {
		MainWindow myWindow;
		
		try {
			myWindow = new MainWindow("Simple Count by Ganitzsh");
			myWindow.init();
			myWindow.display();
		} catch (AllocationException e) {
			e.printStackTrace();
		}		
	}
}