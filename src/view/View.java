package view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.BevelBorder;

public class View{
	private JFrame frame;
	private JPanel playerPanel;
	private JPanel aiPanel;
	private JPanel panel;
	private JLabel textLabel;
	
	private JButton btnStartGame;
	private JButton btnExit;
	private JButton btnRageQuit;
	private JButton btnQuitGame;
	
	private JPanel[][] playerCells;
	private JPanel[][] aiCells;
	
	private JPanel player1field;
	private JPanel player2field;
	
	//slike za prikazati u panelu
	public static BufferedImage MISS;
	public static BufferedImage EXPLOSION;
	
	
	//static block, da bi se te static varijable za slike mogle inicijalizirat 
	//to se pozove kad se uloada klasa	
	static{
		try {
			//valjda taj class loader od klase zna gdje tražiti slike, vrati stream preko kojeg se mogu èitati byte-ovi od slike
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
	
	public void makeFrame() {
		frame = new JFrame("BattleShip");
		//frame.setIconImage(Toolkit.getDefaultToolkit().getImage(View.class.getResource("/com/sun/java/swing/plaf/gtk/icons/File.gif")));
		frame.setSize(800, 600);
		frame.getContentPane().setBackground(new Color(0, 191, 255));
		frame.getContentPane().setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		
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
			// TODO Auto-generated catch block
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
		playerPanel = new JPanel();
		playerPanel.setBackground(Color.CYAN);
		playerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK));
		
		aiPanel = new JPanel();
		aiPanel.setBackground(Color.CYAN);
		aiPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 0, Color.BLACK));
		
		panel = new JPanel();
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK));
		
		btnRageQuit = new JButton("Rage quit!");
		
		btnQuitGame = new JButton("Quit game");
		
		textLabel = new JLabel("<html>Welcome to a game of Battleship!</html>");
		textLabel.setOpaque(true);
		textLabel.setBackground(Color.WHITE);
		textLabel.setBorder(BorderFactory.createLoweredBevelBorder());
		textLabel.setHorizontalAlignment(SwingConstants.LEFT);
		textLabel.setVerticalAlignment(SwingConstants.TOP);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(playerPanel, GroupLayout.PREFERRED_SIZE, 392, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(aiPanel, GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE))
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
		playerPanel.setBackground(Color.CYAN);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(aiPanel, GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)
						.addComponent(playerPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE))
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
		
		player2field = new JPanel();
		GroupLayout gl_player2panel = new GroupLayout(aiPanel);
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
		aiPanel.setLayout(gl_player2panel);
		
		player1field = new JPanel();
		GroupLayout gl_player1panel = new GroupLayout(playerPanel);
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
		playerPanel.setLayout(gl_player1panel);
		
		playerCells = new JPanel[10][10];
		player1field.setLayout(new GridLayout(10, 10));		
		
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				//umjesto obiènog panela napravi ImagePanel jer taj zna iscrtati sliku
				ImagePanel panel = new ImagePanel();
				//namjesti mu i koordinate, da se ne treba kroz cijelo polje tražiti na kojoj je poziciji taj panel
				//npr. u MouseClicked preko (ImagePanel)event.getSource(), dobiješ taj panel i getCoords() ti vrati koordinate
				panel.setCoords(i, j);
				playerCells[i][j] = panel;
				playerCells[i][j].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
				playerCells[i][j].setBackground(SEA_COLOR);
				playerCells[i][j].setPreferredSize(new Dimension(37, 37));
				player1field.add(playerCells[i][j]);
				playerCells[i][j].setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			}
		}
		
		aiCells = new JPanel[10][10];
		player2field.setLayout(new GridLayout(10, 10));
		
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				ImagePanel panel = new ImagePanel();
				panel.setCoords(i, j);
				aiCells[i][j]=panel;
				aiCells[i][j].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
				aiCells[i][j].setBackground(SEA_COLOR);
				aiCells[i][j].setPreferredSize(new Dimension(37, 37));
				player2field.add(aiCells[i][j]);
				aiCells[i][j].setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			}
		}
		
		frame.getContentPane().setLayout(groupLayout);
	}
	
	public String showDialogBox() {
		String s = new String();
		
		do {
			s = (String)JOptionPane.showInputDialog(
					frame,
					"Unesite ime:",
					"Unos imena igraèa",
					JOptionPane.PLAIN_MESSAGE);
		}
		while(s.isEmpty());
		
		return s;
	}
	
	public int chooseShipOrientation(){
		Object[] options = {"Horizontal",
                "Vertical"};
		return JOptionPane.showOptionDialog(frame,
		"",
		"Choose ship orientation",
		JOptionPane.YES_NO_CANCEL_OPTION,
		JOptionPane.QUESTION_MESSAGE,
		null,
		options,
		options[0]);
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
	
	//ovdje su definirane boje koje æe biti background od æelija
	public static final Color SEA_COLOR = Color.BLUE;
	public static final Color AVAILABLE_TO_PLACE_COLOR = Color.GREEN;
	public static final Color NOT_AVAILABLE_TO_PLACE_COLOR = Color.RED;
	public static final Color MOUSE_OVER_COLOR = Color.YELLOW;	
	
	public JPanel getButtonPanel(){
		return panel;
	}
	
	//treba kontroleru da bi preko koordinate mogao naæi æeliju i mijenjati izgled
	public JPanel getAICellPanelByXY(int x, int y){
		return aiCells[x][y];
	}
	
	public JPanel[][] getPlayerCells(){
		return playerCells;
	}
	
	public JPanel[][] getAiCells(){
		return aiCells;
	}
}
