package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

public class View {
	private JFrame mainFrame;
	
	private JButton btnPvAI;
	private JButton btnPvP;
	private JButton btnHighScores;
	private JButton btnExitGame;
	
	private JButton btnRageQuit;
	private JButton btnQuitGame;
	private JPanel shipPanel;
	private JLabel feedbackLabel;
	
	private JPanel shotsPanel;
	private JPanel timePanel;
	private JLabel lblScore;
	private JButton btnMainMenu;
	
	public ImagePanel[][] player1Cells;
	public ImagePanel[][] player2Cells;

	public static BufferedImage BG;
	public static BufferedImage HIGHSCORESBG;
	public static BufferedImage MISS;
	public static BufferedImage EXPLOSION;

	static {
		try {
			BG = ImageIO.read(View.class.getResourceAsStream("bg.jpg"));
			HIGHSCORESBG = ImageIO.read(View.class.getResourceAsStream("highScoreBg.jpg"));
			EXPLOSION = ImageIO.read(View.class.getResourceAsStream("explosion.png"));
			MISS = ImageIO.read(View.class.getResourceAsStream("miss.png"));
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
	}

	public View() {
		makeFrame();
		showMenu();
	}

	private void makeFrame() {
		mainFrame = new JFrame("BattleShip");
		mainFrame.setBounds(50, 50, 800, 600);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void showMenu() {
		ImagePanel bgPanel = new ImagePanel();
		bgPanel.setBounds(0, 0, 800, 600);
		bgPanel.setImage(BG);
		mainFrame.getContentPane().add(bgPanel, BorderLayout.CENTER);
		bgPanel.setLayout(null);

		JPanel menuPanel = new JPanel();
		menuPanel.setBounds(250, 75, 300, 400);
		menuPanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		menuPanel.setBackground(new Color(89, 128, 166));
		bgPanel.add(menuPanel);
		menuPanel.setLayout(null);
		
		JLabel titleLabel = new JLabel("BattleShip");
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setBounds(12, 28, 276, 48);
		titleLabel.setFont(new Font("Dialog", Font.BOLD, 32));
		menuPanel.add(titleLabel);

		btnExitGame = new JButton("Exit game");
		btnExitGame.setBounds(50, 340, 200, 27);
		menuPanel.add(btnExitGame);
		
		btnHighScores = new JButton("High scores");
		btnHighScores.setBounds(50, 300, 200, 27);
		menuPanel.add(btnHighScores);
		
		btnPvP = new JButton("Player vs. Player");
		btnPvP.setBounds(50, 260, 200, 27);
		menuPanel.add(btnPvP);
		
		btnPvAI = new JButton("Player vs. AI");
		btnPvAI.setBounds(50, 220, 200, 27);
		menuPanel.add(btnPvAI);
		
		mainFrame.getContentPane().add(bgPanel);
		mainFrame.setVisible(true);
	}
	
	public void showGame() {
		JPanel player1panel = new JPanel();
		player1panel.setBounds(0, 0, 395, 418);
		player1panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK));
		mainFrame.getContentPane().add(player1panel);
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
		mainFrame.getContentPane().add(player2panel);
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
		
		shipPanel = new JPanel();
		shipPanel.setBounds(12, 430, 768, 47);
		shipPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
		mainFrame.getContentPane().add(shipPanel);
		
		feedbackLabel = new JLabel("Posložite brodove na svoje polje. Tipkom 'space' mijenjate orijentaciju broda.");
		feedbackLabel.setBounds(12, 489, 654, 66);
		feedbackLabel.setOpaque(true);
		feedbackLabel.setBackground(Color.WHITE);
		feedbackLabel.setBorder(BorderFactory.createLoweredBevelBorder());
		feedbackLabel.setHorizontalAlignment(SwingConstants.LEFT);
		feedbackLabel.setVerticalAlignment(SwingConstants.TOP);
		mainFrame.getContentPane().add(feedbackLabel);
		
		btnQuitGame = new JButton("Quit game");
		btnQuitGame.setBounds(684, 489, 96, 27);
		mainFrame.getContentPane().add(btnQuitGame);
		
		btnRageQuit = new JButton("Rage quit");
		btnRageQuit.setBounds(684, 528, 96, 27);
		mainFrame.getContentPane().add(btnRageQuit);
		
		player1Cells = new ImagePanel[10][10];
		player2Cells = new ImagePanel[10][10];	
		
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				player1Cells[i][j] = new ImagePanel();
				player1Cells[i][j].setCoords(i, j);
				player1Cells[i][j].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
				player1Cells[i][j].setBackground(Color.BLUE);
				player1field.add(player1Cells[i][j]);
				player1Cells[i][j].setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			}
		}
		
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				player2Cells[i][j] = new ImagePanel();
				player2Cells[i][j].setCoords(i, j);
				player2Cells[i][j].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
				player2Cells[i][j].setBackground(Color.BLUE);
				player2field.add(player2Cells[i][j]);
				player2Cells[i][j].setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			}
		}
		
		mainFrame.getContentPane().setLayout(null);
		mainFrame.setVisible(true);
	}
	
	public void showHighScores() {
		ImagePanel bgPanel = new ImagePanel();
		bgPanel.setImage(HIGHSCORESBG);
		bgPanel.setBounds(0, 0, 800, 567);
		mainFrame.getContentPane().add(bgPanel);
		bgPanel.setLayout(null);
		
		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		contentPanel.setBackground(new Color(0, 64, 255));
		contentPanel.setBounds(150, 50, 500, 480);
		bgPanel.add(contentPanel);
		contentPanel.setLayout(null);
		
		JLabel lblHighScores = new JLabel("High Scores");
		lblHighScores.setVerticalAlignment(SwingConstants.BOTTOM);
		lblHighScores.setForeground(Color.WHITE);
		lblHighScores.setBounds(5, 5, 490, 48);
		lblHighScores.setHorizontalAlignment(SwingConstants.CENTER);
		lblHighScores.setFont(new Font("Dialog", Font.BOLD, 26));
		contentPanel.add(lblHighScores);
		
		JLabel lblPvAI = new JLabel("(isključivo player vs AI)");
		lblPvAI.setVerticalAlignment(SwingConstants.TOP);
		lblPvAI.setForeground(Color.WHITE);
		lblPvAI.setHorizontalAlignment(SwingConstants.CENTER);
		lblPvAI.setFont(new Font("Dialog", Font.BOLD, 14));
		lblPvAI.setBounds(5, 56, 490, 32);
		contentPanel.add(lblPvAI);
		
		shotsPanel = new JPanel();
		shotsPanel.setBounds(5, 89, 245, 330);
		shotsPanel.setBackground(new Color(0, 64, 255));
		contentPanel.add(shotsPanel);
		shotsPanel.setLayout(null);

		timePanel = new JPanel();
		timePanel.setBounds(250, 89, 245, 330);
		timePanel.setBackground(new Color(0, 64, 255));
		contentPanel.add(timePanel);
		timePanel.setLayout(null);
		
		JLabel lblShootText = new JLabel("Igre sa najmanje poteza");
		lblShootText.setForeground(Color.WHITE);
		lblShootText.setHorizontalAlignment(SwingConstants.CENTER);
		lblShootText.setFont(new Font("Dialog", Font.BOLD, 16));
		lblShootText.setBounds(0, 0, 200, 50);
		shotsPanel.add(lblShootText);
		
		JLabel lblTimeText = new JLabel("Igre sa najkraćim vremenom");
		lblTimeText.setForeground(Color.WHITE);
		lblTimeText.setHorizontalAlignment(SwingConstants.LEFT);
		lblTimeText.setFont(new Font("Dialog", Font.BOLD, 16));
		lblTimeText.setBounds(0, 0, 245, 50);
		timePanel.add(lblTimeText);
		
		lblScore = new JLabel("");
		lblScore.setForeground(Color.WHITE);
		lblScore.setHorizontalAlignment(SwingConstants.LEFT);
		lblScore.setFont(new Font("Dialog", Font.BOLD, 16));
		lblScore.setBounds(5, 423, 378, 45);
		contentPanel.add(lblScore);
		
		btnMainMenu = new JButton("Main menu");
		btnMainMenu.setBounds(380, 431, 96, 27);
		contentPanel.add(btnMainMenu);
		
		mainFrame.getContentPane().setLayout(null);
		mainFrame.setVisible(true);
	}
	
	public String nameInputDialogBox() {
		String s = new String();
		
		do {
			s = (String)JOptionPane.showInputDialog(
					mainFrame,
					"Unesite ime (mora početi velikim slovom):",
					"Unos imena igrača",
					JOptionPane.PLAIN_MESSAGE);
		}
		while(!s.matches("[A-Z].+"));
		
		return s.trim();
	}
	
	public JFrame getMainFrame() {
		return mainFrame;
	}

	public JButton getBtnPvAI() {
		return btnPvAI;
	}

	public JButton getBtnPvP() {
		return btnPvP;
	}

	public JButton getBtnStatistics() {
		return btnHighScores;
	}

	public JButton getBtnExitGame() {
		return btnExitGame;
	}

	public JButton getBtnRageQuit() {
		return btnRageQuit;
	}

	public JButton getBtnQuitGame() {
		return btnQuitGame;
	}

	public JPanel getShipPanel() {
		return shipPanel;
	}

	public JLabel getFeedbackLabel() {
		return feedbackLabel;
	}

	public JButton getBtnHighScores() {
		return btnHighScores;
	}

	public JPanel getShotsPanel() {
		return shotsPanel;
	}

	public JPanel getTimePanel() {
		return timePanel;
	}

	public JLabel getLblScore() {
		return lblScore;
	}

	public JButton getBtnMainMenu() {
		return btnMainMenu;
	}
}
