package view;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import java.awt.Color;

import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.JButton;

public class HighScores {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HighScores window = new HighScores();
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
	public HighScores() {
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
		
		ImagePanel bgPanel = new ImagePanel();
		bgPanel.setBounds(0, 0, 800, 567);
		frame.getContentPane().add(bgPanel);
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
		
		JPanel shotsPanel = new JPanel();
		shotsPanel.setBounds(5, 89, 245, 330);
		shotsPanel.setBackground(Color.BLUE);
		contentPanel.add(shotsPanel);
		shotsPanel.setLayout(new GridLayout(6, 1, 0, 0));

		JPanel timePanel = new JPanel();
		timePanel.setBounds(250, 89, 245, 330);
		timePanel.setBackground(Color.BLUE);
		contentPanel.add(timePanel);
		timePanel.setLayout(new GridLayout(6, 1, 0, 0));
		
		JLabel lblShootText = new JLabel("Igre sa najkraćim vremenom");
		lblShootText.setForeground(Color.WHITE);
		lblShootText.setHorizontalAlignment(SwingConstants.CENTER);
		lblShootText.setFont(new Font("Dialog", Font.BOLD, 16));
		timePanel.add(lblShootText);
		
		JLabel lblTimeText = new JLabel("Igre sa najmanje poteza");
		lblTimeText.setForeground(Color.WHITE);
		lblTimeText.setHorizontalAlignment(SwingConstants.CENTER);
		lblTimeText.setFont(new Font("Dialog", Font.BOLD, 16));
		shotsPanel.add(lblTimeText);
		
		JLabel lblScore = new JLabel("");
		lblScore.setForeground(Color.WHITE);
		lblScore.setHorizontalAlignment(SwingConstants.CENTER);
		lblScore.setFont(new Font("Dialog", Font.BOLD, 16));
		lblScore.setBounds(5, 423, 378, 45);
		contentPanel.add(lblScore);
		
		JButton btnMainMenu = new JButton("Main menu");
		btnMainMenu.setBounds(392, 431, 96, 27);
		contentPanel.add(btnMainMenu);
	}
}
