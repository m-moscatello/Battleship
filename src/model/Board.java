package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
	private Cell[][] cells;
	
	protected List<Cell> availableHorizontal = new ArrayList<Cell>();
	protected List<Cell> availableVertical = new ArrayList<Cell>();
	protected List<Cell> availableCellsToShoot = new ArrayList<Cell>();
	protected List<Cell> hits = new ArrayList<Cell>();
	
	public Board() {
		this.cells = new Cell[10][10];
		init();
	}
	
	public Cell[][] getCells(){
		return cells;
	}
	
	public void addHit(Cell hit){
		hits.add(hit);
	}
	
	public boolean cellHit(Cell cell){
		return hits.contains(cell);
	}
	
	public void init(){
		availableHorizontal.clear();
		availableVertical.clear();
		availableCellsToShoot.clear();
		
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){	
				Cell cell = new Cell(i, j, Dot.SEA, null);
				cells[i][j] = cell;
				
				availableHorizontal.add(cell);
				availableVertical.add(cell);	
				availableCellsToShoot.add(cell);
			}
		}
	}
	
	public Cell getByXY(int x, int y){		
		return cells[x][y];
	}
	
	public void removeAvailableCell(Cell cell){
		availableHorizontal.remove(cell);
		availableVertical.remove(cell);
	}
	
	public void removeAvailableCellToShoot(Cell cell){		
		availableCellsToShoot.remove(cell);
	}
	
	public boolean availableToShoot(Cell cell){
		Cell originalCell = cells[cell.getX()][cell.getY()];
		
		if(originalCell.getDot()==Dot.SEA || originalCell.getDot()==Dot.SHIP)
			return true;
		return false;	
	}

	public Cell getRandomAvailableHorizontal(){
		if(availableHorizontal.size()==0)
			return null;
		int randomPos = new Random().nextInt(availableHorizontal.size());
		return availableHorizontal.get(randomPos);
	}
	
	public Cell getRandomAvailableVertical(){
		if(availableVertical.size()==0)
			return null;
		int randomPos = new Random().nextInt(availableVertical.size());
		return availableVertical.get(randomPos);
	}
	
	public Cell getRandomCell(){
		int randomPos = new Random().nextInt(availableCellsToShoot.size());
		return availableCellsToShoot.get(randomPos);
	}
}
