package model;

//služi da se u dvodimenzionalno polje može spremiti informacija
//o tipu æelije i brodu koji je na njoj smješten
public class Cell{	
	private Dot dot;
	private Ship ship;
	private int x;
	private int y;	
	
	public Cell(int x, int y, Dot dot, Ship ship){
		this.dot=dot;
		this.ship=ship;
		this.x=x;
		this.y=y;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}

	public Dot getDot(){
		return dot;
	}
	
	//kad se npr. pogodi brod onda dot za æeliju preðe sa SHIP na HIT
	//i npr. SEA na MISS
	public void setDot(Dot dot){
		this.dot=dot;
	}
	
	public Ship getShip(){
		return ship;
	}
	
	@Override
	public String toString(){
		return "Cell{x="+x+", y="+y+"}";
	}
}