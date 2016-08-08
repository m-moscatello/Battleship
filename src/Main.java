import javax.swing.SwingUtilities;

import view.*;
import controller.*;

public class Main {
	public static void main(String[] args) {
		//test komentar
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {				
				View view = new View();				
				Controller c = new Controller(view);
				
				c.control();
			}
		});
	}
}
