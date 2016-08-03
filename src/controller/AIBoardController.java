package controller;

import model.Board;
import model.Cell;
import model.Orientation;
import model.Ship;

//da ne bi trebali ponovno definirati canPlace i place metode, onda naslijedimo BoardController
public class AIBoardController extends BoardController{
	//treba nam model od plo�e od igra�a da bi mogli mijenjati Dot property, tj. ga�ati �elije
	private Board playerBoard;
	public AIBoardController(Board playerBoard){
		super();		
		this.playerBoard=playerBoard;
	}
	
	//poku�ava random smjestiti brod na random os	
	public boolean placeShipRandomly(Ship ship, Orientation orientation){
		while(true){
			Cell cell = null;
			//na�e se random �elija na koju se mo�e dodati brod
			if(orientation==Orientation.HORIZONTAL)
				cell = board.getRandomAvailableHorizontal();
			else
				cell = board.getRandomAvailableVertical();
			
			//no available cells left
			if(cell==null)
				return false;
			
			int size = ship.getType().getSize();
			
			//ako se mo�e dodati brod
			if(canPlace(cell.getX(), cell.getY(), size, orientation)){
				//onda iz modela ukloniti �eliju
				board.removeAvailableCell(cell);
				
				//dodati brod, metoda je definirana u super klasi (BoardController)
				place(cell.getX(), cell.getY(), ship, orientation);
				return true;
			}else{
				//ako se ne mo�e dodati brod, isto ukloniti �eliju 
				board.removeAvailableCell(cell);				
			}
		}
	}
	
	//vrati �eliju u blizini cell iz parametra, na orientation osi, i po increment iz parametra
	public Cell getNearbyAvailable(Cell cell, Orientation orientation, int increment){
		Cell[][] cells = playerBoard.getCells();
		
		if(orientation==Orientation.VERTICAL){
			//ako je os verikalna i cell.x+increment<0 ili cell+increment>9 onda je to ve� van plo�e
			if(cell.getX()+increment<0 || cell.getX()+increment>playerBoard.getX()-1)
				return null;
			//ako je unutar plo�e, uzmi sljede�u �eliju po osi i inkrementu
			Cell nearbyCell = cells[cell.getX()+increment][cell.getY()];	
			
			//ako je �elija ve� pogo�ena, onda preko rekurzije, zovi istu metodu, i proslijedi tu prona�enu blisku �eliju
			//jer �e u tom pozivu biti prona�ena nova �elija na istoj osi blizu te bliske �elije
			if(playerBoard.cellHit(nearbyCell)){
				return getNearbyAvailable(nearbyCell, orientation, increment);
			//ako se mo�e pucati na tu �eliju, onda je to �elija na koju AI treba pucati
			}else if(playerBoard.availableToShoot(nearbyCell)){
				return nearbyCell;
			}
			//ako nema na osi �elija na koje se mo�e pucati, onda AI kontroler treba promijeniti os ili inkrement
			return null;
		}else{
			//isto samo �ta se Y mijenja
			if(cell.getY()+increment<0 || cell.getY()+increment>playerBoard.getY()-1)
				return null;
			Cell nearbyCell = cells[cell.getX()][cell.getY()+increment];
			if(playerBoard.cellHit(nearbyCell))
				return getNearbyAvailable(nearbyCell, orientation, increment);
			else if(playerBoard.availableToShoot(nearbyCell))
				return nearbyCell;			
			return null;
		}
	}
	
	//instanciramo logiku za uni�tavanje brodova kad se pogodi prvi dio broda
	private AIDestroyShipLogic destroyerLogic;
	
	//Controller klasa poziva ovu metodu da bi AI mogao odigrati potez
	//metoda vrati �eliju na koji AI �eli pucati
	public Cell play(Cell hit, boolean destroyed){
		//ako je pogo�en brod, hit nije null, onda se instancira DestroyLogic, i originalni pogodak se proslije u konstruktor
		if(hit!=null && destroyerLogic==null){
			destroyerLogic=new AIDestroyShipLogic(this, hit);
		}
		
		//ako je pogo�en brod, onda se u model od player-a doda pogodak u listu pogodaka
		if(hit!=null){
			playerBoard.addHit(hit);
		}			
		
		//ako je brod uni�ten, ukloni referencu na instancu, jer �emo kod sljede�eg pogodtka napraviti novu
		if(destroyed){
			destroyerLogic=null;
		}
		
		
		//ako nema instance DestroyLogic, onda AI random puca
		if(destroyerLogic==null){
			System.out.println("Shooting randomly");
			//na�i random �eliju na koju se mo�e pucati			
			Cell cell = playerBoard.getRandomCell();
			//ukloni �eliju iz liste �elija na koje se mo�e pucati
			playerBoard.removeAvailableCellToShoot(cell);
			return cell;
		}else{
			//preko destroyer logic, na�i �eliju blizu pogodtka na koju bi AI pucao
			Cell cell = destroyerLogic.seek(hit);
			//ukloni �eliju iz liste �elija na koje se mo�e pucati
			playerBoard.removeAvailableCellToShoot(cell);
			return cell;
		}
	}
}
