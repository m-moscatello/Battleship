package model;

//model class to represent board and its state
public class Board {	
	private Dot[][] board; 
	//size for x and y, same for both
	private int boardSize;
	
	//TODO: M: Changed to public because of Player attributes
	public Board(int boardSize){
		this.boardSize=boardSize;
		
		board = new Dot[boardSize][boardSize];
		
		//initialize each field to SEA
		for(int i=0; i<boardSize; i++) {
			for(int j=0; j<boardSize; j++) {
				board[i][j] = Dot.SEA;				
			}
		}
	}
	
	//tries to place ship, returns false if cannot place, true if placed
	//changes cell value from SEA to SHIP if ship is placed in that cell
	public boolean place(int x, int y, int size, Orientation orientation){
		//attempt to place vertically
		if(orientation==Orientation.VERTICAL) {
			//check if enough space to place ship vertically
			if(x+size > boardSize) {
				System.out.println("No space vertically");
				return false;
			}
			
			//check if there's a ship on the way
			for(int i=x; i<size; i++) {
				if(board[i][y]==Dot.SHIP) {
					System.out.println("Cannot place over ship");
					return false;
				}
			}
			
			//place vertically
			System.out.println("Place vertically");
			for(int i=x; i<size+x;i++){
				board[i][y]=Dot.SHIP;
			}
			
		//attempt to place horizontally
		}else if(orientation==Orientation.HORIZONTAL){
			//check if enough space to place ship horizontally
			if(y+size > boardSize) {
				System.out.println("No space horizontally");
				return false;
			}
			
			//check if there's a ship on the way
			for(int i=y; i<size; i++) {
				if(board[x][i]==Dot.SHIP) {
					System.out.println("Cannot place over ship");
					return false;
				}
			}
			
			//place horizontally
			System.out.println("Place horizontally");
			for(int i=y; i<size+y;i++) {
				board[x][i]=Dot.SHIP;
			}
		}
		
		//TODO: Q: Čemu ova petlja?
		/*for(int i=x; i<boardSize;i++) {
			for(int j=y; j<boardSize;j++) {	
				if(board[i][j]==Dot.SHIP) {
					System.out.println("Cannot place");
					return false;					
				}
			}
		}*/	
		
		return true;
	}
	
	//test method, draws field for console output
	public void drawBoard() {
		for(int i=0; i<boardSize;i++) {
			for(int j=0; j<boardSize;j++) {	
				if(board[i][j]==Dot.SHIP)
					System.out.print("[X]");
				else
					System.out.print("[ ]");
			}
			System.out.println();
		}			
	}
	
	
	/*public static void main(String[] args){
		Board b = new Board(10,10);

		//test place
		b.place(0, 0, 5, Orientation.VERTICAL);			
		b.place(1, 2, 3, Orientation.HORIZONTAL);
		b.place(1, 9, 6, Orientation.VERTICAL);
		
		b.place(0, 3, 5, Orientation.VERTICAL);
		b.place(0, 8, 5, Orientation.HORIZONTAL);
		
		b.drawBoard();
		
		/*boolean val = b.place(1, 2, 3, Orientation.HORIZONTAL);
		val = b.place(1, 0, 4, Orientation.VERTICAL);
		val = b.place(0, 0, 3, Orientation.VERTICAL);
		
		String s = "";*/
		
}

