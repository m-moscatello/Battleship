package model;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
	//dvodimenzinalno polje "modela"
	private Cell[][] cells; 
	private int x;
	private int y;	
	
	public Board(int x, int y){
		this.x=x;
		this.y=y;
		cells = new Cell[x][y];
		
		init();
	}
	
	//x i y bi bile kao duljine za to dvodimenzionalno polje, ali je negdje hardkodirano 10,10 pa nema neke koristi
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	//koristi se lista za dostupne æelije za raspored da se ne bi svaki put kad se želi smjestiti
	//brod moralo gledati kroz polje u 2 for petlje
	protected List<Cell> availableHorizontal = new ArrayList<Cell>();
	protected List<Cell> availableVertical = new ArrayList<Cell>();
	
	//ovo je za AI board kontroller, isto da se ne bi koristile 2 for petlje
	//ima sve æelije koje se mogu gaðati (Dot im je SEA ili SHIP)
	protected List<Cell> availableCellsToShoot = new ArrayList<Cell>();
	
	//spreme se svi hitovi na ploèi, da se ne bi koristile 2 for petlje, kad se gleda da li je æelija pogodðena
	protected List<Cell> hits = new ArrayList<Cell>();
	
	public void addHit(Cell hit){
		hits.add(hit);
	}
	
	//contains metoda od liste provjeri da li ta instanca iz parametra postoji u listi
	public boolean cellHit(Cell cell){
		return hits.contains(cell);
	}
	
	//ovu metodu koristi samo AI kontroller da bi provjerio da li se može gaðati æelija
	public boolean availableToShoot(Cell cell){
		//vjerojatno bi se mogla æelija iz parametra provjeravati bez da se
		//traži po koordinatama æelija iz modela u dvodimenzioalnom polju
		Cell originalCell = cells[cell.getX()][cell.getY()];
		
		//ako je Dot SEA ili SHIP, može se gaðati, HIT i MISS znaèe da su veæ uklonjeni listeneri sa æelije
		//pa se ne može nju gaðati više
		if(originalCell.getDot()==Dot.SEA || originalCell.getDot()==Dot.SHIP)
			return true;
		return false;
		//return availableCellsToShoot.contains(cell);		
	}

	//ove dve metode ispod koristi samo AI kontroler da bi mogao random smjestiti brodove na AI board	
	//ako ima koja slobodna æelija, vrati random æeliju iz availableHorizontal liste
	//random index je od 0 do velièine liste odabran preko Random klase
	public Cell getRandomAvailableHorizontal(){
		if(availableHorizontal.size()==0)
			return null;
		int randomPos = new Random().nextInt(availableHorizontal.size());
		return availableHorizontal.get(randomPos);
	}
	
	//ako ima koja slobodna æelija, vrati random æeliju iz availableVertical liste
	//random index je od 0 do velièine liste odabran preko Random klase
	public Cell getRandomAvailableVertical(){
		if(availableVertical.size()==0)
			return null;
		int randomPos = new Random().nextInt(availableVertical.size());
		return availableVertical.get(randomPos);
	}
	
	//kad AI uništi brod, ili je poèeo game, onda random gaða æelije, tako da se uzme iz liste æelija po random indeksu
	public Cell getRandomCell(){
		int randomPos = new Random().nextInt(availableCellsToShoot.size());
		return availableCellsToShoot.get(randomPos);
	} 
	
	//kad se pogodi dio ship-a, onda se makne æelija iz liste, da AI više ne bi nju gaðao
	public void removeAvailableCellToShoot(Cell cell){		
		availableCellsToShoot.remove(cell);
	}
	
	public Cell[][] getCells(){
		return cells;
	}
	
	//samo za debug
	public void dumpAvailableCells(){
		for(Cell cell: availableCellsToShoot){
			System.out.println(cell);
		}
	}
	
	//kad se brod smjesti na neku poziciju, onda se iz ove dve liste uklone sve æelije koje je brod zauzeo
	//ovo koristi samo AI kontroller
	public void removeAvailableCell(Cell cell){
		availableHorizontal.remove(cell);
		availableVertical.remove(cell);
	}
	
	//inicijalizira se dvodimenzionalno polje, i u liste za pomoæ AI kontroleru, se ubace sve æelije iz modela
	protected void init(){
		for(int i=0; i<this.x;i++){
			for(int j=0; j<this.y;j++){	
				Cell cell = new Cell(i, j, Dot.SEA, null);
				cells[i][j]=cell;
				availableHorizontal.add(cell);
				availableVertical.add(cell);	
				availableCellsToShoot.add(cell);
			}
		}
	}
	
	//u mouse listenerima se preko koordinata spremljenih u ImagePanel-u, naðe preko ove metode model
	public Cell getByXY(int x, int y){		
		return cells[x][y];
	}	
}

