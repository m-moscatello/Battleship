package model;
import java.util.ArrayList;
import java.util.List;

public class Ship {
	//na koju je os smješten broj
	private ShipType type;	
	
	//æelije u koje je smješten brod
	private List<Cell> cells = new ArrayList<Cell>();
	
	private int health;
	
	public Ship(ShipType type) {
		//u enumeraciji su svi podaci o brodu, tako da je dovoljno spremiti u atribut Ship klase
		this.type = type;
		
		//health od broda je isti kao i velièina broda
		this.health=type.getSize();
	}

	public String getName(){
		return type.getName();
	}
	
	public int getHealth(){
		return health;
	}
	
	//ako nije health=0, onda smanji health za 1
	public void takeDamage(){
		if(health!=0){
			health-=1;
		}
	}
	
	//ako je health=0 onda je brod uništen
	public boolean isDestroyed(){
		return health==0;
	}
	
	public int getSize(){
		return type.getSize();
	}
	
	//
	public void addCells(List<Cell> cells){
		this.cells.addAll(cells);
	}
	
	//
	public List<Cell> getCells(){
		return cells;
	}
	
	public ShipType getType(){
		return type;
	}
	
	//override toString for better output
	@Override
	public String toString(){
		return "Ship{name="+getName()+", size="+getSize()+"}";
	}
	
	public void dumpCells(){
		for(int i=0; i<cells.size(); i++){
			System.out.println(cells.get(i));
		}
	}
}
