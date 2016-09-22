package model;

import java.awt.Color;

public enum ShipType {
	CARRIER("Carrier", 5, Color.GREEN),
	BATTLESHIP("Battleship", 4, Color.CYAN),
	DESTROYER("Destroyer", 3, Color.WHITE),
	SUBMARINE("Submarine", 3, Color.GRAY),
	PATROL("Patrol", 2, Color.PINK);
	
	private String name;
	private int size;
	private Color color;
	
	private ShipType(String name, int size, Color color) {
		this.name = name;
		this.size = size;
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public int getSize() {
		return size;
	}

	public Color getColor() {
		return color;
	}
}
