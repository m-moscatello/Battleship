import javax.swing.SwingUtilities;

import controller.Controller;
import view.View;

public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {				
				View view = new View();
				Controller controller;
				
				controller = new Controller(view);
				controller.control();
			}
		});
	}

}
