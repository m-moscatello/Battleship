package controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import model.*;
import view.*;

public class Controller {
	private Player player1;
	private View view;
	private ActionListener startBtnListener;
	private ActionListener exitBtnListener;
	private ActionListener rageBtnListener;
	private ActionListener quitBtnListener;
	//TODO: new property for ship button listener
	private ActionListener shipBtnListener;
	private MouseListener placeShipMl;
	private MouseListener gameMl;
	
	public Controller(Player player1, View view) {
		this.view = view;
		this.player1 = player1;
	}
	
	public void control() {
		startBtnListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startBtnPressed();
			}
		};
		
		//TODO: new action listener for ship buttons
		shipBtnListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				shipBtnPressed((ShipButton)e.getSource());
			}
		};
		
		exitBtnListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exitBtnPressed();
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
		
		placeShipMl = new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				//TODO: M: Attempt to place ship
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				//TODO: M: Change color of fields according to size and orientation of ship
			}

			@Override
			public void mouseExited(MouseEvent e) {
				//TODO: M: Change the color of the field back to normal
			}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}
		};
		
		gameMl = new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {
				JComponent jc = (JComponent)e.getSource();
				jc.setBackground(Color.YELLOW);
				jc.repaint();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				JComponent jc = (JComponent)e.getSource();
				jc.setBackground(Color.BLUE);
				jc.repaint();
			}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}
			
		};
		
		this.view.getBtnStartGame().addActionListener(startBtnListener);
		this.view.getBtnExit().addActionListener(exitBtnListener);
	}
	
	//TODO: when ship button is pressed to be placed on board, log info
	public void shipBtnPressed(ShipButton button) {
		System.out.println("Ship "+button.getShip()+" chosen");
	}
	
	public void startBtnPressed() {
		this.player1.name = "test"; // this.view.showDialogBox();
		
		this.view.getFrame().getContentPane().removeAll();
		this.view.getFrame().getContentPane().repaint();
		this.view.showGame();
		
		//TODO: for each ship type create a new button in view
		for(int i=0; i<ShipType.values().length; i++){
			ShipButton button = view.addShipButton(new Ship(ShipType.values()[i]));
			button.addActionListener(shipBtnListener);
		}
		
		this.view.getBtnQuitGame().addActionListener(quitBtnListener);
		this.view.getBtnRageQuit().addActionListener(rageBtnListener);
		
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				this.view.player1polje[i][j].addMouseListener(placeShipMl);
				this.view.player2polje[i][j].addMouseListener(gameMl);
			}
		}
	}
	
	public void exitBtnPressed() {
		System.exit(0);
	}
	
	/*public void registerShips(Ship[] ships){
		ShipButton b = view.addShipButton(ships);
		b.addMouseListener(l);
	}*/
	
	public void quitBtnPressed() {
		this.view.getFrame().getContentPane().removeAll();
		this.view.getFrame().getContentPane().repaint();
		this.view.showMenu();
		
		this.view.getBtnStartGame().addActionListener(startBtnListener);
		this.view.getBtnExit().addActionListener(exitBtnListener);
	}
	
	public void rageBtnPressed() {
		System.exit(1);
	}
}
