package controller;

import model.Cell;
import model.Orientation;

//kad AI pogodi random æeliju, onda preko ove klase uništava brod do kraja
public class AIDestroyShipLogic {
	//služi da bi se znalo u kojem smjeru treba gaðati æelije
	private Orientation orientation=Orientation.HORIZONTAL;
	//u kojem smjeru na osi se kreæe
	private int increment=1;
	
	//treba zbog getNearbyAvailable metode da bi se našla æelija na koju AI može pucati (nije HIT ni MISS) 
	private AIBoardController controller;
	
	//tu se spremi poèetna æelija koju je AI gaðao i u kojoj je pogodio dio broda
	private Cell originalHit;
	//služi da se kontroleru da æelija da bi se našla æelija u blizini koju se može gaðati
	private Cell currentCell;	
	
	public AIDestroyShipLogic(AIBoardController controller, Cell startingHit){
		this.controller=controller;
		
		//to je poèetna æelija koju je AI random pogodio
		this.originalHit=startingHit;		
	}
	
	//vrati sljedeæu æeliju koju AI želi gaðati
	protected Cell seek(Cell hit){
		//ako je pogodak, onda je to trenutna æelija
		if(hit!=null)
			currentCell=hit;
		//ako nije pogodak, onda se treba vratiti na originalnu æeliju koju je AI pogodio
		else
			currentCell=originalHit;
		
		while(true){
			System.out.println("Looping inside AIDestroyShip");
			
			//preko AI kontrolera, naðe se æelija u blizini koja se može gaðati (može biti SEA ili SHIP)
			//ova si klasa bilježi orijentaciju i increment, i mijenja ih kako bi se našla æelija u blizini da bi se brod uništio do kraja
			currentCell=controller.getNearbyAvailable(currentCell, orientation, increment);
			
			//ako nije pronaðena æelija za orientation i increment, promijeni ih
			if(currentCell==null){
				changeDirection();
				//nema slobodnih æelija na tom orientation i increment, pa se vrati na prvu æeliju koju je AI random pogodio
				currentCell=originalHit;
			}else{
				return currentCell;
			}			
		}		
	}
	
	//moglo bi biti više random
	//za sad se uvijek poène horizontalno sa pozitivnim inkrementom
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
