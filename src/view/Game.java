package view;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Font;

public class Game {

	private JFrame frame;

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
		frame.setResizable(false);
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel player1panel = new JPanel();
		player1panel.setBounds(0, 0, 395, 418);
		player1panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK));
		frame.getContentPane().add(player1panel);
		player1panel.setLayout(null);
		
		JPanel player1field = new JPanel();
		player1field.setBounds(52, 36, 331, 370);
		player1field.setLayout(new GridLayout(10, 10, 0, 0));
		player1panel.add(player1field);
		
		JPanel player1NumbersPanel = new JPanel();
		player1NumbersPanel.setBounds(52, 7, 331, 30);
		player1NumbersPanel.setLayout(new GridLayout(1, 10, 0, 0));
		player1panel.add(player1NumbersPanel);
		
		JPanel player1LettersPanel = new JPanel();
		player1LettersPanel.setBounds(23, 36, 30, 370);
		player1LettersPanel.setLayout(new GridLayout(10, 1, 0, 0));
		player1panel.add(player1LettersPanel);
		
		JPanel player2panel = new JPanel();
		player2panel.setBounds(409, 0, 395, 418);
		player2panel.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 0, Color.BLACK));
		frame.getContentPane().add(player2panel);
		player2panel.setLayout(null);
		
		JPanel player2field = new JPanel();
		player2field.setBounds(12, 36, 331, 370);
		player2field.setLayout(new GridLayout(10, 10, 0, 0));
		player2panel.add(player2field);
		
		JPanel player2NumbersPanel = new JPanel();
		player2NumbersPanel.setBounds(12, 7, 331, 30);
		player2NumbersPanel.setLayout(new GridLayout(1, 10, 0, 0));
		player2panel.add(player2NumbersPanel);
		
		JPanel player2LettersPanel = new JPanel();
		player2LettersPanel.setBounds(342, 36, 30, 370);
		player2LettersPanel.setLayout(new GridLayout(10, 1, 0, 0));
		player2panel.add(player2LettersPanel);
		
		JLabel[] player1Brojevi = new JLabel[10];
		JLabel[] player2Brojevi = new JLabel[10];
		JLabel[] player1Slova = new JLabel[10];
		JLabel[] player2Slova = new JLabel[10];
		Integer integer = 1;
		Character character = 'A';
		
		for(int i=0; i<10; i++) {
			player1Brojevi[i] = new JLabel(integer.toString());
			player1Brojevi[i].setFont(new Font("Dialog", Font.BOLD, 18));
			player1Brojevi[i].setHorizontalAlignment(SwingConstants.CENTER);
			player1Brojevi[i].setVerticalAlignment(SwingConstants.CENTER);
			player1NumbersPanel.add(player1Brojevi[i]);
			player2Brojevi[i] = new JLabel(integer.toString());
			player2Brojevi[i].setFont(new Font("Dialog", Font.BOLD, 18));
			player2Brojevi[i].setHorizontalAlignment(SwingConstants.CENTER);
			player2Brojevi[i].setVerticalAlignment(SwingConstants.CENTER);
			player2NumbersPanel.add(player2Brojevi[i]);
			integer++;
			
			player1Slova[i] = new JLabel(character.toString());
			player1Slova[i].setFont(new Font("Dialog", Font.BOLD, 18));
			player1Slova[i].setHorizontalAlignment(SwingConstants.CENTER);
			player1Slova[i].setVerticalAlignment(SwingConstants.CENTER);
			player1LettersPanel.add(player1Slova[i]);
			player2Slova[i] = new JLabel(character.toString());
			player2Slova[i].setFont(new Font("Dialog", Font.BOLD, 18));
			player2Slova[i].setHorizontalAlignment(SwingConstants.CENTER);
			player2Slova[i].setVerticalAlignment(SwingConstants.CENTER);
			player2LettersPanel.add(player2Slova[i]);
			character++;
		}
		
		JPanel shipPanel = new JPanel();
		shipPanel.setBounds(12, 430, 768, 47);
		shipPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
		shipPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		frame.getContentPane().add(shipPanel);
		
		JLabel feedbackLabel = new JLabel("Dobro dosli u potapanje brodova!");
		feedbackLabel.setBounds(12, 489, 654, 66);
		feedbackLabel.setOpaque(true);
		feedbackLabel.setBackground(Color.WHITE);
		feedbackLabel.setBorder(BorderFactory.createLoweredBevelBorder());
		feedbackLabel.setHorizontalAlignment(SwingConstants.LEFT);
		feedbackLabel.setVerticalAlignment(SwingConstants.TOP);
		frame.getContentPane().add(feedbackLabel);
		
		JButton btnQuitGame = new JButton("Quit game");
		btnQuitGame.setBounds(684, 489, 96, 27);
		frame.getContentPane().add(btnQuitGame);
		
		JButton btnRageQuit = new JButton("Rage quit");
		btnRageQuit.setBounds(684, 528, 96, 27);
		frame.getContentPane().add(btnRageQuit);
		
		JPanel[][] player1Cells = new JPanel[10][10];	
		
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				ImagePanel panel = new ImagePanel();
				panel.setCoords(i, j);
				player1Cells[i][j] = panel;
				player1Cells[i][j].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
				player1Cells[i][j].setBackground(Color.BLUE);
				player1field.add(player1Cells[i][j]);
				player1Cells[i][j].setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			}
		}
		
		JPanel[][] player2Cells = new JPanel[10][10];	
		
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				ImagePanel panel = new ImagePanel();
				panel.setCoords(i, j);
				player2Cells[i][j] = panel;
				player2Cells[i][j].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
				player2Cells[i][j].setBackground(Color.BLUE);
				player2field.add(player2Cells[i][j]);
				player2Cells[i][j].setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			}
		}
	}
}
