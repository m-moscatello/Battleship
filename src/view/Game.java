package view;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.FlowLayout;

public class Game {

	private JFrame frame;
	private JPanel player1panel;
	private JPanel player2panel;
	private JPanel panel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Game window = new Game();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Game() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		player1panel = new JPanel();
		player1panel.setBackground(Color.CYAN);
		player1panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK));
		
		player2panel = new JPanel();
		player2panel.setBackground(Color.CYAN);
		player2panel.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 0, Color.BLACK));
		
		panel = new JPanel();
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
		
		JButton btnRageQuit = new JButton("Rage quit!");
		
		JButton btnQuitGame = new JButton("Quit game");
		
		JLabel label = new JLabel("Welcome to a game of Battleship!");
		label.setOpaque(true);
		label.setBackground(Color.WHITE);
		label.setBorder(BorderFactory.createLoweredBevelBorder());
		label.setHorizontalAlignment(SwingConstants.LEFT);
		label.setVerticalAlignment(SwingConstants.TOP);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(player1panel, GroupLayout.PREFERRED_SIZE, 392, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(player2panel, GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(label, GroupLayout.DEFAULT_SIZE, 662, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(btnQuitGame, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnRageQuit, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap())
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 768, Short.MAX_VALUE)
					.addContainerGap())
		);
		player1panel.setBackground(Color.CYAN);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(player2panel, GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)
						.addComponent(player1panel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnQuitGame)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnRageQuit))
						.addComponent(label, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		
		JPanel player2field = new JPanel();
		GroupLayout gl_player2panel = new GroupLayout(player2panel);
		gl_player2panel.setHorizontalGroup(
			gl_player2panel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_player2panel.createSequentialGroup()
					.addContainerGap(32, Short.MAX_VALUE)
					.addComponent(player2field, GroupLayout.PREFERRED_SIZE, 333, GroupLayout.PREFERRED_SIZE)
					.addGap(30))
		);
		gl_player2panel.setVerticalGroup(
			gl_player2panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_player2panel.createSequentialGroup()
					.addGap(35)
					.addComponent(player2field, GroupLayout.PREFERRED_SIZE, 333, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(41, Short.MAX_VALUE))
		);
		player2panel.setLayout(gl_player2panel);
		
		JPanel player1field = new JPanel();
		GroupLayout gl_player1panel = new GroupLayout(player1panel);
		gl_player1panel.setHorizontalGroup(
			gl_player1panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_player1panel.createSequentialGroup()
					.addGap(30)
					.addComponent(player1field, GroupLayout.PREFERRED_SIZE, 333, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(27, Short.MAX_VALUE))
		);
		gl_player1panel.setVerticalGroup(
			gl_player1panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_player1panel.createSequentialGroup()
					.addGap(35)
					.addComponent(player1field, GroupLayout.PREFERRED_SIZE, 333, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(46, Short.MAX_VALUE))
		);
		player1panel.setLayout(gl_player1panel);
		
		JPanel[][] player1polje = new JPanel[10][10];
		player1field.setLayout(new GridLayout(10, 10));
		
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				player1polje[i][j] = new JPanel();
				player1polje[i][j].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
				player1polje[i][j].setBackground(Color.blue);
				player1polje[i][j].setPreferredSize(new Dimension(37, 37));
				player1field.add(player1polje[i][j]);
				player1polje[i][j].setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			}
		}
		
		JPanel[][] player2polje = new JPanel[10][10];
		player2field.setLayout(new GridLayout(10, 10));
		
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				player2polje[i][j] = new JPanel();
				player2polje[i][j].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
				player2polje[i][j].setBackground(Color.blue);
				player2polje[i][j].setPreferredSize(new Dimension(37, 37));
				player2field.add(player2polje[i][j]);
				player2polje[i][j].setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			}
		}
		
		/*JPanel test = new JPanel();
		test.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
		test.setBackground(Color.blue);
		test.setPreferredSize(new Dimension(37, 37));
		player1field.add(test);
		test.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));*/
		
		frame.getContentPane().setLayout(groupLayout);
	}
}
