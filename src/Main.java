import javax.swing.SwingUtilities;

import model.*;
import view.*;
import controller.*;

public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Player player1 = new Player();
				View view = new View();
				Controller c = new Controller(player1, view);
				
				c.control();
			}
		});
	}
}
