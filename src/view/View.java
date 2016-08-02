package view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.BevelBorder;

import model.Ship;
import model.ShipType;

public class View{
	private JFrame frame;
	private JPanel player1panel;
	private JPanel player2panel;
	private JPanel panel;
	private JLabel textLabel;
	
	private JButton btnStartGame;
	private JButton btnExit;
	private JButton btnRageQuit;
	private JButton btnQuitGame;
	
	public JPanel[][] player1polje;
	public JPanel[][] player2polje;
	
	public View() {
		makeFrame();
		showMenu();
	}
	
	public void makeFrame() {
		frame = new JFrame("BattleShip");
		//frame.setIconImage(Toolkit.getDefaultToolkit().getImage(View.class.getResource("/com/sun/java/swing/plaf/gtk/icons/File.gif")));
		frame.setSize(800, 600);
		frame.getContentPane().setBackground(new Color(0, 191, 255));
		frame.getContentPane().setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		BufferedImage bg_img = null;
		/*try {
			bg_img = ImageIO.read(new File("bg.jpg"));
		} catch(IOException e) {
			e.printStackTrace();
		}*/
		
		//Graphics g = bg_img.getGraphics();
		//g.drawImage(bg_img, 0, 0, null);
		
		/*try {
			frame.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("bg.jpg")))));
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		//JLabel background = new JLabel(new ImageIcon("../../resources/bg.jpg"));
		//frame.add(background);
	}
	
	public void showMenu() {
		JPanel panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel.setBackground(new Color(0, 0, 255));
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addGap(200)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
					.addGap(200))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(50)
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
					.addGap(50))
		);
		
		JLabel lblNewLabel = new JLabel("BattleShip");
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 26));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		btnStartGame = new JButton("Start game");
		
		btnExit = new JButton("Exit");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(20)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(btnExit, GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
						.addComponent(btnStartGame, GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
						.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE))
					.addGap(20))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
					.addComponent(btnStartGame)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnExit)
					.addGap(56))
		);
		panel.setLayout(gl_panel);
		frame.getContentPane().setLayout(groupLayout);
		frame.setVisible(true);
	}
	
	public void showGame() {
		player1panel = new JPanel();
		player1panel.setBackground(Color.CYAN);
		player1panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK));
		
		player2panel = new JPanel();
		player2panel.setBackground(Color.CYAN);
		player2panel.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 0, Color.BLACK));
		
		panel = new JPanel();
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
		
		btnRageQuit = new JButton("Rage quit!");
		
		btnQuitGame = new JButton("Quit game");
		
		textLabel = new JLabel("Welcome to a game of Battleship!");
		textLabel.setOpaque(true);
		textLabel.setBackground(Color.WHITE);
		textLabel.setBorder(BorderFactory.createLoweredBevelBorder());
		textLabel.setHorizontalAlignment(SwingConstants.LEFT);
		textLabel.setVerticalAlignment(SwingConstants.TOP);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(player1panel, GroupLayout.PREFERRED_SIZE, 392, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(player2panel, GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(textLabel, GroupLayout.DEFAULT_SIZE, 662, Short.MAX_VALUE)
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
						.addComponent(textLabel, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE))
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
		
		player1polje = new JPanel[10][10];
		player1field.setLayout(new GridLayout(10, 10));
		
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				player1polje[i][j] = new JPanel();
				player1polje[i][j].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
				player1polje[i][j].setBackground(Color.BLUE);
				player1polje[i][j].setPreferredSize(new Dimension(37, 37));
				player1field.add(player1polje[i][j]);
				player1polje[i][j].setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			}
		}
		
		player2polje = new JPanel[10][10];
		player2field.setLayout(new GridLayout(10, 10));
		
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				player2polje[i][j] = new JPanel();
				player2polje[i][j].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
				player2polje[i][j].setBackground(Color.BLUE);
				player2polje[i][j].setPreferredSize(new Dimension(37, 37));
				player2field.add(player2polje[i][j]);
				player2polje[i][j].setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			}
		}
		
		frame.getContentPane().setLayout(groupLayout);
	}
	
	//TODO: adds a ship button to panel
	public ShipButton addShipButton(Ship ship){
		//create new ship button
		ShipButton button = new ShipButton(ship);
		panel.add(button);
		//return so controller can add listener to button
		return button;
	}
	
	public String showDialogBox() {
		String s = new String();
		
		do {
			s = (String)JOptionPane.showInputDialog(
					frame,
					"Unesite ime:",
					"Unos imena igrača",
					JOptionPane.PLAIN_MESSAGE);
		}
		while(s.isEmpty());
		
		return s;
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	public JLabel getTextLabel() {
		return textLabel;
	}
	
	public JButton getBtnStartGame() {
		return btnStartGame;
	}
	
	public JButton getBtnExit() {
		return btnExit;
	}
	
	public JButton getBtnQuitGame() {
		return btnQuitGame;
	}
	
	public JButton getBtnRageQuit() {
		return btnRageQuit;
	}
}
