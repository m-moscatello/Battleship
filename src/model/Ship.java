package model;

public class Ship {
	private Orientation orientation;
	private ShipType type;
	
	public Ship(ShipType type) {
		this.orientation = Orientation.HORIZONTAL;
		this.type = type;		
	}
	public void changeOrientation() {
		if(this.orientation == Orientation.VERTICAL) {
			this.orientation = Orientation.HORIZONTAL;
		}
		else { this.orientation = Orientation.VERTICAL; }
	}

	public Orientation getOrientation() {
		return orientation;
	}

	public String getName(){
		return type.getName();
	}
	
	public int getSize(){
		return type.getSize();
	}
	
	//override toString for better output
	@Override
	public String toString(){
		return "Ship{name="+getName()+", size="+getSize()+"}";
	}
}
