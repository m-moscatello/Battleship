package model;

public class Ship {
	private ShipType type;
	private int health;
	
	public Ship(ShipType type) {
		this.type = type;
		this.health = type.getSize();
	}
	
	public void takeDamage() {
		this.health -= 1;
	};
	
	public boolean isDestroyed() {
		return health==0;
	}

	public ShipType getShipType() {
		return type;
	}

	public int getHealth() {
		return health;
	}
	
	public String getName() {
		return type.getName();
	}
	
	public int getSize() {
		return type.getSize();
	}
}
