package controller;

import model.Board;
import model.Cell;
import model.Orientation;
import model.Ship;

//da ne bi trebali ponovno definirati canPlace i place metode, onda naslijedimo BoardController
public class AIBoardController extends BoardController{
	//treba nam model od ploèe od igraèa da bi mogli mijenjati Dot property, tj. gaðati æelije
	private Board playerBoard;
	public AIBoardController(Board playerBoard){
		super();		
		this.playerBoard=playerBoard;
	}
	
	//pokušava random smjestiti brod na random os	
	public boolean placeShipRandomly(Ship ship, Orientation orientation){
		while(true){
			Cell cell = null;
			//naðe se random æelija na koju se može dodati brod
			if(orientation==Orientation.HORIZONTAL)
				cell = board.getRandomAvailableHorizontal();
			else
				cell = board.getRandomAvailableVertical();
			
			//no available cells left
			if(cell==null)
				return false;
			
			int size = ship.getType().getSize();
			
			//ako se može dodati brod
			if(canPlace(cell.getX(), cell.getY(), size, orientation)){
				//onda iz modela ukloniti æeliju
				board.removeAvailableCell(cell);
				
				//dodati brod, metoda je definirana u super klasi (BoardController)
				place(cell.getX(), cell.getY(), ship, orientation);
				return true;
			}else{
				//ako se ne može dodati brod, isto ukloniti æeliju 
				board.removeAvailableCell(cell);				
			}
		}
	}
	
	//vrati æeliju u blizini cell iz parametra, na orientation osi, i po increment iz parametra
	public Cell getNearbyAvailable(Cell cell, Orientation orientation, int increment){
		Cell[][] cells = playerBoard.getCells();
		
		if(orientation==Orientation.VERTICAL){
			//ako je os verikalna i cell.x+increment<0 ili cell+increment>9 onda je to veæ van ploèe
			if(cell.getX()+increment<0 || cell.getX()+increment>playerBoard.getX()-1)
				return null;
			//ako je unutar ploèe, uzmi sljedeæu æeliju po osi i inkrementu
			Cell nearbyCell = cells[cell.getX()+increment][cell.getY()];	
			
			//ako je æelija veæ pogoðena, onda preko rekurzije, zovi istu metodu, i proslijedi tu pronaðenu blisku æeliju
			//jer æe u tom pozivu biti pronaðena nova æelija na istoj osi blizu te bliske æelije
			if(playerBoard.cellHit(nearbyCell)){
				return getNearbyAvailable(nearbyCell, orientation, increment);
			//ako se može pucati na tu æeliju, onda je to æelija na koju AI treba pucati
			}else if(playerBoard.availableToShoot(nearbyCell)){
				return nearbyCell;
			}
			//ako nema na osi æelija na koje se može pucati, onda AI kontroler treba promijeniti os ili inkrement
			return null;
		}else{
			//isto samo šta se Y mijenja
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
	
	//instanciramo logiku za uništavanje brodova kad se pogodi prvi dio broda
	private AIDestroyShipLogic destroyerLogic;
	
	//Controller klasa poziva ovu metodu da bi AI mogao odigrati potez
	//metoda vrati æeliju na koji AI želi pucati
	public Cell play(Cell hit, boolean destroyed){
		//ako je pogoðen brod, hit nije null, onda se instancira DestroyLogic, i originalni pogodak se proslije u konstruktor
		if(hit!=null && destroyerLogic==null){
			destroyerLogic=new AIDestroyShipLogic(this, hit);
		}
		
		//ako je pogoðen brod, onda se u model od player-a doda pogodak u listu pogodaka
		if(hit!=null){
			playerBoard.addHit(hit);
		}			
		
		//ako je brod uništen, ukloni referencu na instancu, jer æemo kod sljedeæeg pogodtka napraviti novu
		if(destroyed){
			destroyerLogic=null;
		}
		
		
		//ako nema instance DestroyLogic, onda AI random puca
		if(destroyerLogic==null){
			System.out.println("Shooting randomly");
			//naði random æeliju na koju se može pucati			
			Cell cell = playerBoard.getRandomCell();
			//ukloni æeliju iz liste æelija na koje se može pucati
			playerBoard.removeAvailableCellToShoot(cell);
			return cell;
		}else{
			//preko destroyer logic, naði æeliju blizu pogodtka na koju bi AI pucao
			Cell cell = destroyerLogic.seek(hit);
			//ukloni æeliju iz liste æelija na koje se može pucati
			playerBoard.removeAvailableCellToShoot(cell);
			return cell;
		}
	}
}
