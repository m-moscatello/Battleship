package controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import model.Cell;
import model.Dot;
import model.Orientation;
import model.Ship;
import model.ShipType;
import view.ImagePanel;
import view.ShipButton;
import view.View;

public class Controller {
	private View view;
	private BoardController player1BoardController;
	private AIBoardController player2BoardController;

	private ActionListener pvaiBtnListener;
	private ActionListener pvpBtnListener;
	private ActionListener highBtnListener;
	private ActionListener exitBtnListener;

	private ActionListener shipBtnListener;
	private ActionListener quitBtnListener;
	private ActionListener rageBtnListener;
	
	private ActionListener mainMenuBtnListener;

	private MouseListener setShipMouseListener;
	private MouseListener shootMouseListener;

	private KeyListener keyListener;

	private String mode;
	private Orientation chosenOrientation = Orientation.HORIZONTAL;
	private Ship chosenShip;
	private ImagePanel currentPanel;
	
	private String currentPlayer;
	private String player1Name;
	private String player2Name;
	
	private long startTime = 0;
	private long endTime = 0;
	private long timeElapsed = 0;
	
	private int shootNumber = 0;
	
	private FileController fileController;
	private List<String> dbLines;

	public Controller(View view) {
		this.view = view;
		
		this.player1BoardController = new BoardController();
		this.player2BoardController = new AIBoardController(player1BoardController.getBoard());
		
		File baza = new File("baza.txt");
		String path = baza.getAbsolutePath();
		
		this.fileController = new FileController(path);
		this.dbLines = new ArrayList<String>();
	}

	public void control() {
		pvaiBtnListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pvaiBtnPressed();
			}
		};

		pvpBtnListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pvpBtnPressed();
			}
		};
		
		highBtnListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				highBtnPressed();
			}
		};

		exitBtnListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exitBtnPressed();
			}
		};

		shipBtnListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				shipBtnPressed((ShipButton) e.getSource());
			}
		};

		quitBtnListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				quitBtnPressed();
			}
		};

		rageBtnListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rageBtnPressed();
			}
		};
		
		mainMenuBtnListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainMenuBtnPressed();
			}
		};

		setShipMouseListener = new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (chosenShip == null)
					return;

				BoardController boardController = getPlayerBoardController(currentPlayer);

				currentPanel = (ImagePanel) e.getSource();

				int[] coords = currentPanel.getCoords();
				int x = coords[0];
				int y = coords[1];
				boolean avaliable = boardController.canPlace(x, y, chosenShip.getSize(), chosenOrientation);

				if (avaliable) {
					boardController.place(x, y, chosenShip, chosenOrientation);
				} else {
					updateLog("Brod se ne može ovdje smjestiti!");
					return;
				}

				removeShipButton(chosenShip);
				chosenShip = null;
				chosenOrientation = Orientation.HORIZONTAL;

				if (boardController.getShipCount() == ShipType.values().length) {
					if (mode == "pvai") {
						fillAiBoard();

						disableMouseListeners(player1Name, setShipMouseListener);

						if (coinToss() == 0) {
							enableMouseListeners(player2Name, shootMouseListener);
							updateLog("Igrač je prvi na potezu.");
						} else {
							updateLog("AI je prvi na potezu.");
							aiTurn();
						}
						
						Date time = new Date();
						startTime = time.getTime();
					} else if (mode == "pvp" && currentPlayer == player1Name) {
						for (int i = 0; i < ShipType.values().length; i++) {
							ShipButton button = addShipButton(new Ship(ShipType.values()[i]));
							button.setFocusable(true);
							button.addKeyListener(keyListener);
							button.addActionListener(shipBtnListener);
						}
						view.getShipPanel().repaint();
						hideShips();
						
						try {
							player2Name = view.nameInputDialogBox();
						} catch (NullPointerException e1) {
							player2Name = "Player2";
						}

						disableMouseListeners(currentPlayer, setShipMouseListener);
						currentPlayer = player2Name;
						enableMouseListeners(currentPlayer, setShipMouseListener);
						updateLog("Drugi igrač slaže brodove na svoje polje.");
					} else if (currentPlayer == player2Name) {
						hideShips();
						disableMouseListeners(currentPlayer, setShipMouseListener);

						if (coinToss() == 0) {
							currentPlayer = player1Name;
							enableMouseListeners(player2Name, shootMouseListener);
							updateLog(currentPlayer +" igra prvi potez.");
						} else {
							currentPlayer = player2Name;
							enableMouseListeners(player1Name, shootMouseListener);
							updateLog(currentPlayer +" igra prvi potez.");
						}
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				if (chosenShip == null)
					return;

				currentPanel = (ImagePanel) e.getSource();

				displayShipForPositioning(chosenShip, currentPanel, currentPlayer);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (chosenShip == null)
					return;

				repaintPlayerField(currentPlayer);
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}
		};

		shootMouseListener = new MouseListener() {

			@SuppressWarnings("incomplete-switch")
			@Override
			public void mouseClicked(MouseEvent e) {
				currentPanel = (ImagePanel) e.getSource();

				int[] coords = currentPanel.getCoords();
				int x = coords[0];
				int y = coords[1];

				if (mode == "pvai") {
					Dot dot = player2BoardController.getBoard().getByXY(x, y).getDot();
					Ship ship = player2BoardController.getBoard().getByXY(x, y).getShip();

					shootNumber++;

					switch (dot) {
					case SEA:
						player2BoardController.getBoard().getByXY(x, y).setDot(Dot.MISS);
						currentPanel.setBackground(Color.BLUE);
						currentPanel.setImage(View.MISS);
						disableMouseListeners(player2Name, shootMouseListener);

						aiTurn();

						break;
					case SHIP:
						player2BoardController.getBoard().getByXY(x, y).setDot(Dot.HIT);
						ship.takeDamage();
						currentPanel.setBackground(Color.BLUE);
						currentPanel.setImage(View.EXPLOSION);
						currentPanel.removeMouseListener(shootMouseListener);

						if (ship.isDestroyed()) {
							updateLog("Potopili ste protivnikov "+ ship.getName());
							paintShip(ship);
						}

						if (player2BoardController.isGameOver()) {
							Date time = new Date();
							endTime = time.getTime();
							timeElapsed = (endTime - startTime)/1000;
							updateLog("Partija je trajala "+ timeElapsed +"s");
							
							disableMouseListeners(player1Name, shootMouseListener);
							disableMouseListeners(player2Name, shootMouseListener);
							
							updateLog("Odigrali ste "+ shootNumber +" poteza.");
							updateLog("Pobjedili ste!");
							
							String record = player1Name +","+ shootNumber +","+ timeElapsed;
							try {
								fileController.appendToFile(record);
							} catch (IOException IOe) {
								updateLog("Unable to write to file!");
							}
						}

						break;
					}
				} else if (mode == "pvp") {
					BoardController boardController;
					if (currentPlayer == player1Name)
						boardController = player2BoardController;
					else
						boardController = player1BoardController;

					Dot dot = boardController.getBoard().getByXY(x, y).getDot();

					switch (dot) {
					case SEA:
						boardController.getBoard().getByXY(x, y).setDot(Dot.MISS);
						currentPanel.setBackground(Color.BLUE);
						currentPanel.setImage(View.MISS);

						if (currentPlayer == player1Name)
							disableMouseListeners(player2Name, shootMouseListener);
						else
							disableMouseListeners(player1Name, shootMouseListener);

						changeCurrentPlayer();

						if (currentPlayer == player1Name)
							enableMouseListeners(player2Name, shootMouseListener);
						else
							enableMouseListeners(player1Name, shootMouseListener);

						break;
					case SHIP:
						Ship ship = boardController.getBoard().getByXY(x, y).getShip();

						boardController.getBoard().getByXY(x, y).setDot(Dot.HIT);
						ship.takeDamage();
						currentPanel.setBackground(Color.BLUE);
						currentPanel.setImage(View.EXPLOSION);
						currentPanel.removeMouseListener(shootMouseListener);

						if (boardController.isGameOver()) {
							disableMouseListeners(player1Name, shootMouseListener);
							disableMouseListeners(player2Name, shootMouseListener);
							repaintPlayerField(player1Name);
							repaintPlayerField(player2Name);

							updateLog(currentPlayer + " je pobjednik!");
						}

						break;
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				currentPanel = (ImagePanel) e.getSource();
				currentPanel.setBackground(Color.YELLOW);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				ImagePanel panel = (ImagePanel) e.getSource();
				panel.setBackground(Color.BLUE);
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}
		};

		keyListener = new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (chosenShip == null)
					return;

				// space
				if (e.getKeyCode() == 32) {
					if (chosenOrientation == Orientation.HORIZONTAL)
						chosenOrientation = Orientation.VERTICAL;
					else
						chosenOrientation = Orientation.HORIZONTAL;

					repaintPlayerField(currentPlayer);
					displayShipForPositioning(chosenShip, currentPanel, currentPlayer);
				}
				// escape
				else if (e.getKeyCode() == 27) {
					chosenShip = null;
					repaintPlayerField(currentPlayer);
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
		};

		this.view.getBtnPvAI().addActionListener(pvaiBtnListener);
		this.view.getBtnPvP().addActionListener(pvpBtnListener);
		this.view.getBtnHighScores().addActionListener(highBtnListener);
		this.view.getBtnExitGame().addActionListener(exitBtnListener);
	}

	public void pvaiBtnPressed() {
		this.mode = "pvai";
		try {
			this.player1Name = this.view.nameInputDialogBox();
		} catch (NullPointerException e) {
			return;
		}
		this.currentPlayer = this.player1Name;

		this.view.getMainFrame().getContentPane().removeAll();
		this.view.showGame();
		this.view.getMainFrame().getContentPane().repaint();

		for (int i = 0; i < ShipType.values().length; i++) {
			ShipButton button = addShipButton(new Ship(ShipType.values()[i]));
			button.setFocusable(true);
			button.addKeyListener(keyListener);
			button.addActionListener(shipBtnListener);
		}
		updateLog("Dobro došli u potapanje brodova!");
		enableMouseListeners(currentPlayer, setShipMouseListener);
		
		Date time = new Date();
		this.startTime = time.getTime();

		this.view.getBtnQuitGame().addActionListener(quitBtnListener);
		this.view.getBtnRageQuit().addActionListener(rageBtnListener);
	}

	public void pvpBtnPressed() {
		this.mode = "pvp";
		try {
			this.player1Name = this.view.nameInputDialogBox();
		} catch (NullPointerException e) {
			return;
		}
		this.currentPlayer = this.player1Name;

		this.view.getMainFrame().getContentPane().removeAll();
		this.view.showGame();
		this.view.getMainFrame().getContentPane().repaint();

		for (int i = 0; i < ShipType.values().length; i++) {
			ShipButton button = addShipButton(new Ship(ShipType.values()[i]));
			button.setFocusable(true);
			button.addKeyListener(keyListener);
			button.addActionListener(shipBtnListener);
		}
		updateLog("Dobro došli u potapanje brodova!");
		enableMouseListeners(currentPlayer, setShipMouseListener);

		this.view.getBtnQuitGame().addActionListener(quitBtnListener);
		this.view.getBtnRageQuit().addActionListener(rageBtnListener);
	}
	
	public void highBtnPressed() {
		this.view.getMainFrame().getContentPane().removeAll();
		this.view.showHighScores();
		this.view.getMainFrame().getContentPane().repaint();
		
		getScores();
		
		this.view.getBtnMainMenu().addActionListener(mainMenuBtnListener);
	}

	public void exitBtnPressed() {
		System.exit(0);
	}

	public void shipBtnPressed(ShipButton ship) {
		this.chosenShip = ship.getShip();
	}

	public void quitBtnPressed() {
		this.view.getMainFrame().getContentPane().removeAll();
		this.view.showMenu();
		this.view.getMainFrame().getContentPane().repaint();

		this.player1BoardController.reset();
		this.player2BoardController.reset();
		
		this.shootNumber = 0;

		this.view.getBtnPvAI().addActionListener(pvaiBtnListener);
		this.view.getBtnPvP().addActionListener(pvpBtnListener);
		this.view.getBtnHighScores().addActionListener(highBtnListener);
		this.view.getBtnExitGame().addActionListener(exitBtnListener);
	}

	public void rageBtnPressed() {
		System.exit(1);
	}
	
	public void mainMenuBtnPressed() {
		this.view.getMainFrame().getContentPane().removeAll();
		this.view.showMenu();
		this.view.getMainFrame().getContentPane().repaint();

		this.view.getBtnPvAI().addActionListener(pvaiBtnListener);
		this.view.getBtnPvP().addActionListener(pvpBtnListener);
		this.view.getBtnHighScores().addActionListener(highBtnListener);
		this.view.getBtnExitGame().addActionListener(exitBtnListener);
	}

	public void removeShipButton(Ship ship) {
		JPanel panel = view.getShipPanel();

		for (int i = 0; i < panel.getComponentCount(); i++) {
			Component c = panel.getComponent(i);

			if (c instanceof ShipButton && ((ShipButton) c).getShip() == ship) {
				panel.remove(c);
			}
		}
		
		panel.revalidate();
		panel.repaint();
	}

	public ShipButton addShipButton(Ship ship) {
		ShipButton button = new ShipButton(ship);
		this.view.getShipPanel().add(button);

		return button;
	}

	protected void hideShips() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				this.view.player1Cells[i][j].setBackground(Color.BLUE);
				this.view.player2Cells[i][j].setBackground(Color.BLUE);
			}
		}
	}

	protected void repaintPlayerField(String player) {
		BoardController boardController = getPlayerBoardController(player);
		ImagePanel[][] field = getPlayerCells(player);

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				Dot dot = boardController.getBoard().getByXY(i, j).getDot();
				Ship ship = boardController.getBoard().getByXY(i, j).getShip();

				switch (dot) {
				case SEA:
					field[i][j].setBackground(Color.BLUE);
					break;
				case SHIP:
					field[i][j].setBackground(ship.getShipType().getColor());
					break;
				case HIT:
					field[i][j].setImage(View.EXPLOSION);
					field[i][j].setBackground(ship.getShipType().getColor());
					break;
				case MISS:
					field[i][j].setImage(View.MISS);
					break;
				}
			}
		}
	}

	protected void displayShipForPositioning(Ship ship, ImagePanel polje, String player) {
		BoardController boardController;
		ImagePanel[][] field = null;

		if (currentPlayer == player1Name) {
			boardController = player1BoardController;
			field = this.view.player1Cells;
		} else {
			boardController = player2BoardController;
			field = this.view.player2Cells;
		}

		int choords[] = polje.getCoords();
		int x = choords[0];
		int y = choords[1];

		if (this.chosenOrientation == Orientation.HORIZONTAL) {
			if (boardController.canPlace(x, y, ship.getSize(), Orientation.HORIZONTAL)) {
				for (int i = y; i < y + ship.getSize(); i++) {
					field[x][i].setBackground(ship.getShipType().getColor());
				}
			} else {
				for (int i = y; i < y + ship.getSize() && i < 10; i++) {
					field[x][i].setBackground(Color.RED);
				}
			}
		} else if (this.chosenOrientation == Orientation.VERTICAL) {
			if (boardController.canPlace(x, y, ship.getSize(), Orientation.VERTICAL)) {
				for (int i = x; i < x + ship.getSize(); i++) {
					field[i][y].setBackground(ship.getShipType().getColor());
				}
			} else {
				for (int i = x; i < x + ship.getSize() && i < 10; i++) {
					field[i][y].setBackground(Color.RED);
				}
			}
		}
	}
	
	public void paintShip(Ship ship) {
		for (int i = 0; i<10; i++) {
			for (int j = 0; j<10; j++) {
				Ship aShip = player2BoardController.getBoard().getByXY(i, j).getShip();
				
				if(aShip == ship) {
					view.player2Cells[i][j].setBackground(ship.getShipType().getColor());
				}
			}
		}
	}

	public void fillAiBoard() {
		for (int i = 0; i < player1BoardController.getShipCount(); i++) {
			ShipType type = ShipType.values()[i];

			Ship aiShip = new Ship(type);

			if (coinToss() == 1)
				player2BoardController.placeShipRandomly(aiShip, Orientation.HORIZONTAL);
			else
				player2BoardController.placeShipRandomly(aiShip, Orientation.VERTICAL);
		}
	}

	private void aiTurn() {
		Runnable aiTurnTask = new Runnable() {
			@Override
			public void run() {
				Cell lastHit = null;
				boolean destroyed = false;

				while (!player1BoardController.isGameOver()) {
					Cell shotCell = player2BoardController.play(lastHit, destroyed);
					Cell realCell = player1BoardController.getBoard().getByXY(shotCell.getX(), shotCell.getY());

					int sleepTime = 0;

					if (lastHit != null) {
						sleepTime = 500;
					} else {
						sleepTime = new Random().nextInt(2);

						if (sleepTime == 0)
							sleepTime = 1000;
						else
							sleepTime = sleepTime * 1000;
					}

					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					player1BoardController.shootCell(shotCell.getX(), shotCell.getY());

					if (realCell.getDot() == Dot.HIT) {
						Ship ship = realCell.getShip();
						lastHit = realCell;

						if (ship.isDestroyed()) {
							destroyed = true;

							SwingUtilities.invokeLater(new AppendToLogTask("AI je potopio vaš " + ship.getName()));
						}

						SwingUtilities.invokeLater(new RepaintPlayerFieldTask(player1Name));
						continue;
					} else if (realCell.getDot() == Dot.MISS) {
						lastHit = null;
						destroyed = false;

						SwingUtilities.invokeLater(new RepaintPlayerFieldTask(player1Name));
						break;
					}
				}

				SwingUtilities.invokeLater(new AiTurnDoneTask());
			}
		};

		Thread t = new Thread(aiTurnTask);
		t.start();
	}

	protected class AppendToLogTask implements Runnable {
		private String text;

		public AppendToLogTask(String text) {
			this.text = text;
		}

		@Override
		public void run() {
			updateLog(text);
		}
	}

	protected class RepaintPlayerFieldTask implements Runnable {
		private String player;

		public RepaintPlayerFieldTask(String player) {
			this.player = player;
		}

		@Override
		public void run() {
			repaintPlayerField(player);
		}
	}

	protected class AiTurnDoneTask implements Runnable {
		@Override
		public void run() {
			if (player1BoardController.isGameOver()) {
				Date time = new Date();
				endTime = time.getTime();
				timeElapsed = (endTime - startTime)/1000;
				updateLog("Partija je trajala "+ timeElapsed +"s");
				
				updateLog("Odigrali ste "+ shootNumber +" poteza.");
				updateLog("AI je pobjedio. Game over!");
				
				ImagePanel[][] field = view.player2Cells;
				for (int i = 0; i < 10; i++) {
					for (int j = 0; j < 10; j++) {
						Dot dot = player2BoardController.getBoard().getByXY(i, j).getDot();
						Ship ship = player2BoardController.getBoard().getByXY(i, j).getShip();

						switch (dot) {
						case SEA:
							field[i][j].setBackground(Color.BLUE);
							break;
						case SHIP:
							field[i][j].setBackground(ship.getShipType().getColor());
							break;
						case HIT:
							field[i][j].setImage(View.EXPLOSION);
							field[i][j].setBackground(ship.getShipType().getColor());
							break;
						case MISS:
							field[i][j].setImage(View.MISS);
							break;
						}
					}
				}
			} else {
				enableMouseListeners(player2Name, shootMouseListener);
			}
		}
	}
	
	public BoardController getPlayerBoardController(String player) {
		if(player == player1Name)
			return player1BoardController;
		return player2BoardController;
	}
	
	public ImagePanel[][] getPlayerCells(String player) {
		if(player == player1Name)
			return view.player1Cells;
		return view.player2Cells;
	}

	protected void updateLog(String text) {
		String current = view.getFeedbackLabel().getText();
		String filtered = current.replace("<html>", "");
		filtered = current.replace("</html>", "");

		view.getFeedbackLabel().setText("<html>" + text + "<br>" + filtered + "</html>");
	}

	private void enableMouseListeners(String player, MouseListener ml) {
		BoardController boardController;
		if (player == player1Name)
			boardController = player1BoardController;
		else
			boardController = player2BoardController;

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				Dot dot = boardController.getBoard().getByXY(i, j).getDot();
				if (dot == Dot.SEA || dot == Dot.SHIP)
					this.getPlayerCells(player)[i][j].addMouseListener(ml);
			}
		}
	}

	private void disableMouseListeners(String player, MouseListener ml) {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				this.getPlayerCells(player)[i][j].removeMouseListener(ml);
			}
		}
	}

	private void changeCurrentPlayer() {
		if (currentPlayer == player1Name)
			currentPlayer = player2Name;
		else
			currentPlayer = player1Name;
	}
	
	private void getScores() {
		try {
			this.dbLines = this.fileController.readFile();
		} catch (IOException e) {
			System.out.println("Unable to open file!");
		}
		
		JPanel shotsPanel = this.view.getShotsPanel();
		JPanel timePanel = this.view.getTimePanel();
		
		if(dbLines.isEmpty()) {
			JLabel noShots = new JLabel("Nema zapisa");
			noShots.setForeground(Color.WHITE);
			noShots.setFont(new Font("Dialog", Font.BOLD, 14));
			noShots.setBounds(20, 50, 245, 50);
			shotsPanel.add(noShots);
			
			JLabel noTime = new JLabel("Nema zapisa");
			noTime.setForeground(Color.WHITE);
			noTime.setFont(new Font("Dialog", Font.BOLD, 14));
			noTime.setBounds(10, 50, 245, 50);
			timePanel.add(noTime);
			return;
		}
		else {
			String[] top5Shots = getTop5(1);
			String[] top5Times = getTop5(2);
			Integer n = 0;
			
			for(Integer i=0; i<5 && i<dbLines.size(); i++) {
				for(String record : dbLines) {
					String[] recordPiece = record.split(",");
					n = i+1;
					
					if(recordPiece[1].equals(top5Shots[i])) {
						JLabel lblRecord = new JLabel(n.toString() +". "+ recordPiece[0] +" .......... "+ recordPiece[1]);
						lblRecord.setForeground(Color.WHITE);
						lblRecord.setFont(new Font("Dialog", Font.BOLD, 14));
						lblRecord.setBounds(20, n*20+50, 245, 20);
						shotsPanel.add(lblRecord);
					}
					if(recordPiece[2].equals(top5Times[i])) {
						JLabel lblRecord = new JLabel(n.toString() +". "+ recordPiece[0] +" .......... "+ recordPiece[2] +"s");
						lblRecord.setForeground(Color.WHITE);
						lblRecord.setFont(new Font("Dialog", Font.BOLD, 14));
						lblRecord.setBounds(10, n*20+50, 245, 20);
						timePanel.add(lblRecord);
					}
				}
			}
		}
	}
	
	private String[] getTop5(int column) {
		String[] top5 = new String[5];
		List<Integer> allGames = new ArrayList<Integer>();
		
		for(String record : dbLines ) {
			String[] recordPiece = record.split(",");
			allGames.add(Integer.parseInt(recordPiece[column]));
		}
		Collections.sort(allGames);
		
		for(int i=0; i<5 && i<dbLines.size(); i++)
			top5[i] = allGames.get(i).toString();
		
		return top5;
	}

	public int coinToss() {
		if (Math.random() > 0.5)
			return 1;
		return 0;
	}
}
