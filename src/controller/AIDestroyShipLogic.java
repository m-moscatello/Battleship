package controller;

import model.Cell;
import model.Orientation;

//kad AI pogodi random �eliju, onda preko ove klase uni�tava brod do kraja
public class AIDestroyShipLogic {
	//slu�i da bi se znalo u kojem smjeru treba ga�ati �elije
	private Orientation orientation=Orientation.HORIZONTAL;
	//u kojem smjeru na osi se kre�e
	private int increment=1;
	
	//treba zbog getNearbyAvailable metode da bi se na�la �elija na koju AI mo�e pucati (nije HIT ni MISS) 
	private AIBoardController controller;
	
	//tu se spremi po�etna �elija koju je AI ga�ao i u kojoj je pogodio dio broda
	private Cell originalHit;
	//slu�i da se kontroleru da �elija da bi se na�la �elija u blizini koju se mo�e ga�ati
	private Cell currentCell;	
	
	public AIDestroyShipLogic(AIBoardController controller, Cell startingHit){
		this.controller=controller;
		
		//to je po�etna �elija koju je AI random pogodio
		this.originalHit=startingHit;		
	}
	
	//vrati sljede�u �eliju koju AI �eli ga�ati
	protected Cell seek(Cell hit){
		//ako je pogodak, onda je to trenutna �elija
		if(hit!=null)
			currentCell=hit;
		//ako nije pogodak, onda se treba vratiti na originalnu �eliju koju je AI pogodio
		else
			currentCell=originalHit;
		
		while(true){
			System.out.println("Looping inside AIDestroyShip");
			
			//preko AI kontrolera, na�e se �elija u blizini koja se mo�e ga�ati (mo�e biti SEA ili SHIP)
			//ova si klasa bilje�i orijentaciju i increment, i mijenja ih kako bi se na�la �elija u blizini da bi se brod uni�tio do kraja
			currentCell=controller.getNearbyAvailable(currentCell, orientation, increment);
			
			//ako nije prona�ena �elija za orientation i increment, promijeni ih
			if(currentCell==null){
				changeDirection();
				//nema slobodnih �elija na tom orientation i increment, pa se vrati na prvu �eliju koju je AI random pogodio
				currentCell=originalHit;
			}else{
				return currentCell;
			}			
		}		
	}
	
	//moglo bi biti vi�e random
	//za sad se uvijek po�ne horizontalno sa pozitivnim inkrementom
	//inkrement se mijenja kad je increment pozitivan
	//os kad je inkrement negativan	i os=vertikalno
	protected void changeDirection(){
		if(orientation==Orientation.VERTICAL && increment<0){
			orientation=Orientation.HORIZONTAL;
			increment=1;
			return;
		}	
		
		if(increment>0){
			increment=-1;
			return;
		}
		else{
			orientation=Orientation.VERTICAL;
			increment = 1;
			return;
		}	
		
		
	}
}
