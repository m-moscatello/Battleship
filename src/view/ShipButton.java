package view;

import javax.swing.JButton;

import model.Ship;

public class ShipButton extends JButton {
	private static final long serialVersionUID = 1L;
	
	private Ship ship;

	public ShipButton(Ship ship) {
		super();
		setText(ship.getName());
		setBackground(ship.getShipType().getColor());
		setSize(70, 30);
		
		this.ship = ship;
	}
	
	public Ship getShip() {
		return ship;
	}
}
