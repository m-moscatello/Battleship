package model;

public class Cell {
	private Dot dot;
	private Ship ship;
	private int x;
	private int y;

	public Cell(int x, int y, Dot dot, Ship ship) {
		this.dot = dot;
		this.ship = ship;
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Dot getDot() {
		return dot;
	}

	public void setDot(Dot dot) {
		this.dot = dot;
	}

	public Ship getShip() {
		return ship;
	}
	
	public void setShip(Ship ship) {
		this.ship = ship;
	}

	@Override
	public String toString() {
		return "Cell{x="+x + ", y="+y + "}";
	}
}