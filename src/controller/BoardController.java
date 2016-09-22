package controller;

import java.util.ArrayList;
import java.util.List;

import model.Board;
import model.Cell;
import model.Dot;
import model.Orientation;
import model.Ship;

public class BoardController {
	protected Board board;
	protected List<Ship> ships = new ArrayList<Ship>();

	public BoardController() {
		this.board = new Board();
	}

	public Board getBoard() {
		return board;
	}
	
	public int getShipCount() {
		return ships.size();
	}
	
	public boolean isGameOver() {
		for(int i=0; i<ships.size(); i++) {
			if(!ships.get(i).isDestroyed())
				return false;
		}
		return true;
	}
	
	public boolean canPlace(int x, int y, int size, Orientation orientation) {
		Cell[][] cells = board.getCells();
		
		if(x<0 || x>10 || y<0 || y>10) {
			return false;
		}
		
		if(orientation == Orientation.HORIZONTAL) {
			if(y+size > 10) {
				return false;
			}
			
			for(int i=y; i<y+size; i++) {
				if(cells[x][i].getDot() == Dot.SHIP) {
					return false;
				}
			}
		}
		
		if(orientation == Orientation.VERTICAL) {
			if(x+size > 10) {
				return false;
			}
			
			for(int i=x; i<x+size; i++) {
				if(cells[i][y].getDot() == Dot.SHIP) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public void place(int x, int y, Ship ship, Orientation orientation) {
		Cell[][] cells = board.getCells();
		
		if(orientation == Orientation.HORIZONTAL) {
			for(int i=y; i<y+ship.getSize(); i++) {
				cells[x][i].setDot(Dot.SHIP);
				cells[x][i].setShip(ship);
			}
		}
		else if(orientation == Orientation.VERTICAL) {
			for(int i=x; i<x+ship.getSize(); i++) {
				cells[i][y].setDot(Dot.SHIP);
				cells[i][y].setShip(ship);
			}
		}
		
		ships.add(ship);
	}
	
	@SuppressWarnings("incomplete-switch")
	public boolean shootCell(int x, int y) {
		Cell[][] cells = board.getCells();
		Cell cell = cells[x][y];
		
		switch(cell.getDot()) {
		case SHIP:
			cell.getShip().takeDamage();
			cell.setDot(Dot.HIT);
			return true;
		case SEA:
			cell.setDot(Dot.MISS);
			break;
		}
		
		return false;
	}
	
	public void reset(){
		ships.clear();
		board.init();
	}
}
