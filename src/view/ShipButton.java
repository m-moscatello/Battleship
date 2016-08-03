package view;

import javax.swing.JButton;

import model.Ship;

//Just a button that has info about ship
public class ShipButton extends JButton {
	//for object serialization in xml or other format
	//not needed for this project
	private static final long serialVersionUID = 1L;

	private Ship ship;
	
	public ShipButton(Ship ship){
		super();
		setText(ship.getName());
		setBackground(ship.getType().getColor());
		
		this.ship=ship;
	}
	
	public Ship getShip(){
		return ship;
	}	
}
