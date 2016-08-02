package model;

public class Player {
	public String name;
	public Board vlastitoPolje;
	public Board protivnickoPolje;
	
	public Player() {
		this.vlastitoPolje = new Board(10);
		this.protivnickoPolje = new Board(10);
	}
}
