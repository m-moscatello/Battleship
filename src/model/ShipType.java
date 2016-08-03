package model;

import java.awt.Color;

public enum ShipType {
	CARRIER("Carrier", new Color(230,133,48), "C",5), 
	BATTLESHIP("Battleship",new Color(48,230,178), "B",4), 
	DESTROYER("Destroyer",new Color(48,178,230), "D",3), 
	SUBMARINE("Submarine",new Color(199,230,48),"S", 3), 
	PATROL("Patrol",new Color(158,157,168), "P",2);
	
	private String name;
	private int size;
	//background od �elije kad se prikazuje brod (kod pozicioniranja, playeru na svojoj plo�i, ili kad je uni�ten)
	private Color color;
	private String mark;
	
	//enumeracija mo�e imati konstruktor kao klasa, pa je mogu�e spremiti vi�e nego jedan podatak (vi�e nego jedna vrijednost, npr. kod obi�ne
	//enumeracije za svaki �lan se doda jedan integer)
	private ShipType(String name, Color color, String mark, int size){
		this.name=name;
		this.size=size;
		this.color=color;
		this.mark=mark;
	}
	
	public String getName(){
		return name;
	}
	
	public int getSize(){
		return size;
	}
	
	public Color getColor(){
		return color;
	}
	
	public String getMark(){
		return mark;
	}
}