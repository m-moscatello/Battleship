package controller;

import java.util.ArrayList;
import java.util.List;

import model.Board;
import model.Cell;
import model.Dot;
import model.Orientation;
import model.Ship;

//ovo bi bio kontroller za plo�u od player-a, jer nema random dodavanja brodova kao i AI kontroler
//preko njega se provjerava model da li se mo�e smjestiti brod na plo�u i smje�ta brod na plo�u
public class BoardController {
	protected Board board;
	//lista dodanih brodova
	protected List<Ship> ships = new ArrayList<Ship>();
	
	public BoardController(){
		board= new Board(10,10);		
	}
	
	//slu�i da se dobije 
	public Board getBoard(){
		return board;
	}
	
	//koliko je brodova dodano na player board, AI preko toga sebi doda isti broj brodova, iz ShipType enumeracije
	public int getShipCount(){
		return ships.size();
	}
	
	//ta svaki dodani brod se provjeri da li je uni�ten
	//ako su svi uni�teni onda je return true, AI je dobio game
	public boolean isGameOver(){		
		for(int i=0; i<ships.size(); i++){
			if(!ships.get(i).isDestroyed())
				return false;
		}
		return true;
	}
	
	//dodaje brod na po�etnu poziciju, prema orijentaciji iz parametra
	//preko ship.ShipType na�emo duljinu broda
	//mogu�e je dodati ship preko drugog, ili tako da je dio broda izvan plo�e
	//ali u ovoj aplikaciji je place kontroliran sa canPlace
	public void place(int x, int y, Ship ship, Orientation orientation){
		//uzmemo iz modela �elije jer �emo im mijenjati ship i Dot atribute
		Cell[][] cells = board.getCells();
		
		//lista �elija na koje je dodan prod
		List<Cell> placedCells = new ArrayList<Cell>();
		
		//place vertically		
		if(orientation==Orientation.VERTICAL){
			System.out.println("Place vertically");		
			
			//za dodati na verikalnu os treba x pove�avati za duljinu broda
			//i u svaku �eliju na toj osi dodati brod
			for(int i=x; i<ship.getType().getSize()+x;i++){
				//moglo bi se direktno u modelu izmijeniti ve� postoje�i Cell
				/*
				 * cells[x][i].setShip(ship);
				 * cells[x][i].setDot(Dot.SHIP);
				 */
				//ali ne smeta da se kreira nova instanca
				Cell newCell = new Cell(i, y, Dot.SHIP, ship);
				cells[i][y]=newCell;
				//dodaj �eliju u listu �elija na koje je smje�ten brod
				placedCells.add(newCell);				
			}
			ships.add(ship);
		//za dodati na horizontalnu os treba y pove�avati za duljinu broda
		//i u svaku �eliju na toj osi dodati brod
		}else if(orientation==Orientation.HORIZONTAL){
			System.out.println("Place horizontally");
			for(int i=y; i<ship.getType().getSize()+y;i++){				
				Cell newCell = new Cell(x, i, Dot.SHIP, ship);
				cells[x][i]=newCell;
				placedCells.add(newCell);				
			}
			ships.add(ship);
		}
		ship.addCells(placedCells);
	}
	
	//provjerava da li se na os mo�e dodati brod duljine size 
	public boolean canPlace(int x, int y, int size, Orientation orientation){
		Cell[][] cells = board.getCells();
		//check if can place (already placed ship)
		
		//ako je x ili y negativan, onda bi to bilo izvan plo�e
		if(x<0 || y<0){
			System.out.println("Cannot place outside board");
			return false;
		}
		
		//ako nema dovoljno prostora x>10, ne mo�e se smjestiti brod
		if(orientation==Orientation.VERTICAL && (x+size>board.getX())){			
			System.out.println("No space verticaly");
			return false;
		}
		
		//ako nema dovoljno prostora y>10, ne mo�e se smjestiti brod
		if(orientation==Orientation.HORIZONTAL && (y+size>board.getY())){
			System.out.println("No space horizontaly");
			return false;
		}
		
		//provjeri se da li od x,y do x+size ve� ima smje�ten dio broda
		if(orientation==Orientation.VERTICAL){
			for(int i=x; i<x+size;i++){
				if(cells[i][y].getDot()==Dot.SHIP){
					System.out.println("Cannot place on x="+i+", y="+y);
					return false;
				}
			}
			//provjeri se da li od x,y do y+size ve� ima smje�ten dio broda
		}else if(orientation==Orientation.HORIZONTAL){
			for(int i=y; i<y+size;i++){
				if(cells[x][i].getDot()==Dot.SHIP){					
					System.out.println("Cannot place on x="+x+", y="+i);
					return false;
				}
			}
		}
		
		System.out.println("Can place on x="+x+", y="+y);
		return true;
	}	
	
	//na temelju Dot atributa iz �elije u modelu se mijenja Dot atribut
	public boolean shootCell(int x, int y){	
		Cell[][] cells = board.getCells();
		Cell cell = cells[x][y];		
		switch(cell.getDot()){
		//ako je u �eliji brod, onda nek primi damage, i namjesti Dot na HIT
		case SHIP:
			cell.getShip().takeDamage();
			cell.setDot(Dot.HIT);			
			return true;
		case HIT:
			//hit cell se ne bi vi�e trebao ga�ati
			//jer AI ima logiku da na�e cell koji se mo�e ga�ati
			//a kad player pogodi cell na AI boardu se ukloni listener
			System.out.println("Tried to shoot at destroyed cell?");
			cell.setDot(Dot.HIT);
			break;
		case SEA:
			//ako je pogo�en SEA, onda je proma�eno, pa se sa Dot=SEA Dot postavi na MISS
			cell.setDot(Dot.MISS);			
			break;
		case MISS:
			//ista stvar kao i za MISS
			System.out.println("Tried to shoot at already missed cell?");
			break;			
		}
		return false;
	}
}
