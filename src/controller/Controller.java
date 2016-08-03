package controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import model.*;
import view.*;

public class Controller {	
	private BoardController playerBoardController;	
	private AIBoardController aiBoardController;	
	private View view;
	private ActionListener startBtnListener;
	private ActionListener exitBtnListener;
	private ActionListener rageBtnListener;
	private ActionListener quitBtnListener;
	private ActionListener shipBtnListener;
	private MouseListener mPlayerBoardListener;
	private MouseListener mAIBoardListener;
	private MouseWheelListener mWListener;
	
	//odabrana orijentacija preko mouse wheel
	private Orientation chosenOrientation;
	//odabran brod, kad se klikne na button od broda za namje�tanje na plo�u
	private Ship chosenShip;
	//treba da se tokom pozicioniranja broda zna iznad koje �elije je igra� ozna�io
	private JPanel lastPanel;
	
	//kad se jednom okrene mouse wheel dobije� dva eventa, tek drugi put treba provjeravat vrijednosti od eventa
	private static final int MOUSE_WHEEL_MOVE_LIMIT=2;
	//pove�ava se za svaki mouse wheel event i kad do�e na MOUSE_WHEEL_MOVE_LIMIT onda se �itaju vrijednost od eventa
	private int mouseWheelMoveCounter=0;
	
	//slu�i da se isklju�e mouse listeneri na ai plo�i
	private boolean aiBoardListenersDisabled=false;
	//slu�i da se isklju�e listeneri na player plo�i, isklju�i se kad player sve brodove doda na plo�u
	private boolean playerBoardListenersDisabled=true;
	
	public Controller(View view) {
		this.view = view;		
		
		//u konstruktoru kreiraju se kontrolleri za igra�a i za ai
		this.playerBoardController=new BoardController();
		this.aiBoardController=new AIBoardController(playerBoardController.getBoard());		
	}
	
	public void control() {
		startBtnListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startBtnPressed();
			}
		};
		
		//TODO: new action listener for ship buttons
		shipBtnListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//u event se spremi i button, to je ShipButton da znamo koji je brod player odabrao
				shipBtnPressed((ShipButton)e.getSource());
			}
		};
		
		exitBtnListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exitBtnPressed();
			}
		};
		
		mWListener = new MouseWheelListener() {			
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				//ako su eventi isklju�eni onda return da se ni� ne napravi
				if(playerBoardListenersDisabled)
					return;
				
				//pove�aj counter da znamo da li �itamo vrijednosti ili ne
				mouseWheelMoveCounter+=1;
			   
				//ako je counter==MOUSE_WHEEL_MOVE_LIMIT onda promijeni orijentaciju
				if(mouseWheelMoveCounter==MOUSE_WHEEL_MOVE_LIMIT){
					if(e.getWheelRotation()==1)				  
						chosenOrientation=Orientation.VERTICAL;
					else
						chosenOrientation=Orientation.HORIZONTAL;
					//prika�i kako �e se brod dodati na plo�u, tj. koje �e �elije zauzeti
					displayShipForPositioning();
					//postavi counter na 0, da se ponovno mogu �itati mouse wheel eventi
					mouseWheelMoveCounter=0;
				}			   
			}		    
		};
		
		quitBtnListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				quitBtnPressed();
			}
		};
		
		rageBtnListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rageBtnPressed();
			}
		};
		
		mPlayerBoardListener = new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				//ako su eventi isklju�eni onda return da se ni� ne napravi
				if(playerBoardListenersDisabled)
					return;
				lastPanel = (JPanel)e.getSource();
				
				//iz zadnje �elije na koju je player kliknuo na�i njezine koordinate
				int[] coords = ((ImagePanel)lastPanel).getCoords();
				
				//ako je player odabrao brod...
				if(chosenShip!=null){			
					//provjeri u player kontroleru da li se brod mo�e dodati na tu poziciju u model (model=Board)
					boolean available = playerBoardController.canPlace(coords[0], coords[1], chosenShip.getSize(), chosenOrientation);
					//ako se mo�e dodati...
					if(available){						
						System.out.println("Ship to place: "+chosenShip+" on x="+coords[0]+", y="+coords[1]);
						//preko kontrolera dodaj brod u model
						playerBoardController.place(coords[0], coords[1], chosenShip, chosenOrientation);
						
						//ukloni button od broda sa panela
						removeShipButton(chosenShip);
						//resetiraj varijable koje slu�e za dodavanje broda
						chosenShip=null;
						chosenOrientation=null;
						//promijeni boje i slike na �elijama prema modelu na plo�i od playera
						resetBackgroundColorForPlayerBoard();
						
						System.out.println("Placed ships: "+playerBoardController.getShipCount()+", ShipType length: "+ShipType.values().length);
						//ako je player dodao sve brodove, a to znamo preko duljini vrijednosti u enumeraciji od tipova brodova...
						if(playerBoardController.getShipCount()==ShipType.values().length){
							System.out.println("Fill ai board");
							//random napuni brodove za ai-ja
							fillAiBoard();
							
							//ako funkcija randomOneOrZero vrati 1, onda player igra prvi, 0 ai igra prvi
							if(randomOneOrZero()==1){
								//uklju�i listenere sa ai plo�e da player mo�e igrat
								aiBoardListenersDisabled=false;
								updateLog("Your turn");			
							}else{
								aiTurn();
							}
						}
						//isklju�i listenere sa player board da ne bi vi�e klikao na svoju plo�u
						playerBoardListenersDisabled=true;
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				//ako su eventi isklju�eni onda return da se ni� ne napravi
				if(playerBoardListenersDisabled)
					return;
				lastPanel = (JPanel)e.getSource();
				//prika�i brod na plo�i, da player zna gdje ga smje�ta
				displayShipForPositioning();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				//ako su eventi isklju�eni onda return da se ni� ne napravi
				if(playerBoardListenersDisabled)
					return;
				JPanel panel = (JPanel)e.getSource();
				//na�i koordinate od panela
				int[] coords = ((ImagePanel)panel).getCoords();
				
				//na�i �eliju iz modela iz koje je player iza�ao
				Cell cell = playerBoardController.getBoard().getByXY(coords[0], coords[1]);
				//resetiraj background za tu �eliju
				changeBackgroundForPlayerBoard(cell, false, false, false);				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if(playerBoardListenersDisabled)
					return;
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if(playerBoardListenersDisabled)
					return;
			}
			
		};
		
		mAIBoardListener = new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				//ako su listeneri isklju�eni onda return da se ni� ne napravi
				if(aiBoardListenersDisabled)
					return;
				//ovo je samo provjera na kojem se threadu izvr�ava event, to je thread od gui-ja
				System.out.println("MouseClickedEvent on threadID="+Thread.currentThread().getId());
				
				JPanel panel = (JPanel)e.getSource();
				int[] coords = ((ImagePanel)panel).getCoords();
				//kad se klikne na �eliju, reci ai kontroleru da je player "pucao" na tu �eliju
				aiBoardController.shootCell(coords[0], coords[1]);
				//izvuci �eliju iz modela od ai-ja, da bi se moglo provjeriti �to je player "pogodio"
				Cell cell = aiBoardController.getBoard().getByXY(coords[0], coords[1]);
				
				//pogo�en brod...
				if(cell.getDot()==Dot.HIT){
					//ako je brod uni�ten, reci playeru preko labele da je uni�tio brod
					if(cell.getShip().isDestroyed()){
						updateLog("You destroyed ship "+cell.getShip());
						System.out.println("Player destroyed ship "+cell.getShip());
					//ako nije uni�ten brod, reci playeru preko labele da je pogodio brod
					}else{
						updateLog("You hit ship "+cell.getShip()+" on position "+cell);
						System.out.println("Player hit ship "+cell.getShip()+" on position "+cell+". Remaining health "+cell.getShip().getHealth());
					}
					
					//promijeni background za tu �eliju u ai plo�i
					changeBackgroundForAIBoard(cell, false);
					
					//ako su u modelu od AI-ja svi brodovi uni�teni, onda je GG, player je pobjedio
					if(aiBoardController.isGameOver()){
						updateLog("You won. Congratulations!");
						System.out.println("Player won");
						//ukloni sve listenere sa AI plo�e da player ne bi vi�e mogao kliktati
						removeMouseListenersForBoard(view.getAiCells());
					}
				//player fullao brod...
				}else if(cell.getDot()==Dot.MISS){
					//upozori ga preko labele da je fulao
					updateLog("You missed on position "+cell);
					System.out.println("Player missed on position "+cell);
					//promijeni background za �eliju na AI plo�i
					changeBackgroundForAIBoard(cell, false);
					//daj AI-u da igra
					aiTurn();
				}	
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				if(aiBoardListenersDisabled)
					return;
				//samo ofarbaj �eliju u �uto, ako je ve� �elija fulana ili pogo�ena, onda smo uklonili ve� event listener
				//pa player ne mo�e dizati evente na toj �eliji
				JPanel jc = (JPanel)e.getSource();
				jc.setBackground(Color.YELLOW);
				jc.repaint();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if(aiBoardListenersDisabled)
					return;
				//vrati nazad background na SEA_COLOR, ako je �elija pogo�ena ili fulana, ovo ne�e raditi jer smo uklonili ve� event listener
				JPanel jc = (JPanel)e.getSource();
				jc.setBackground(View.SEA_COLOR);
				jc.repaint();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if(aiBoardListenersDisabled)
					return;
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if(aiBoardListenersDisabled)
					return;
			}
			
		};
		
		this.view.getBtnStartGame().addActionListener(startBtnListener);
		this.view.getBtnExit().addActionListener(exitBtnListener);
	}
	
	//kad player klikne na gumb za dodati brod
	public void shipBtnPressed(ShipButton button) {
		//omogu�i da player klik�e i polazi mi�em preko svog polja da bi namjestio brodove
		playerBoardListenersDisabled=false;
		//za po�etak je orijentacija horizontalno
		chosenOrientation = Orientation.HORIZONTAL;
		
		//izvadi brod iz buttona i spremi u varijablu
		chosenShip = button.getShip();
		
		System.out.println("Ship "+button.getShip()+" chosen with orientation "+chosenOrientation);
	}
	
	public void startBtnPressed() {
		aiBoardListenersDisabled=true;
		this.view.getFrame().getContentPane().removeAll();
		this.view.getFrame().getContentPane().repaint();
		this.view.showGame();
		
		//za svaki brod iz enumeracije brodova, napravi jedan button
		for(int i=0; i<ShipType.values().length; i++){
			ShipButton button = addShipButton(new Ship(ShipType.values()[i]));
			button.addActionListener(shipBtnListener);
		}
		
		this.view.getBtnQuitGame().addActionListener(quitBtnListener);
		this.view.getBtnRageQuit().addActionListener(rageBtnListener);
		
		//dodaj mouse listenere na �elije u ai i player plo�ama
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				this.view.getPlayerCells()[i][j].addMouseListener(mPlayerBoardListener);
				this.view.getAiCells()[i][j].addMouseListener(mAIBoardListener);
			}
		}
		
		view.getFrame().addMouseWheelListener(mWListener);
		//reci playeru preko labele da doda svoje brodove na plo�u
		updateLog("Place your ships");
	}
	
	public void exitBtnPressed() {
		System.exit(0);
	}
	
	/*public void registerShips(Ship[] ships){
		ShipButton b = view.addShipButton(ships);
		b.addMouseListener(l);
	} */
	
	public void quitBtnPressed() {
		this.view.getFrame().getContentPane().removeAll();
		this.view.getFrame().getContentPane().repaint();
		this.view.showMenu();
		
		this.view.getBtnStartGame().addActionListener(startBtnListener);
		this.view.getBtnExit().addActionListener(exitBtnListener);
	}
	
	public void rageBtnPressed() {
		System.exit(1);
	}
	
	//pro�e kroz sve �elije u modelu od player-a i namjesti background ili sliku za HIT i MISS
	public void resetBackgroundColorForPlayerBoard(){
		Cell cell = null;
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				//iz modela u player kontroleru na�i �eliju
				cell = playerBoardController.getBoard().getByXY(i, j);
				changeBackgroundForPlayerBoard(cell, false, false, false);				
			}
		}
	}
	
	//pro�e kroz sve �elije u modelu od ai-a i namjesti background ili sliku za HIT i MISS
	public void resetBackgroundColorForAIBoard(){
		Cell cell = null;
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				//iz modela u ai kontroleru na�i �eliju
				cell = aiBoardController.getBoard().getByXY(i, j);
				changeBackgroundForAIBoard(cell, false);				
			}
		}
	}
	
	//mijenja background i sliku za �eliju iz parametra funkcije, za plo�u od AI-ja, 
	// 1. na kojoj player ga�a brodove
	// 2. na kojoj ai smje�ta svoje brodove
	// za oba 2 slu�aja se poziva ova funkcija
	public void changeBackgroundForAIBoard(Cell cell, boolean mouseOver){
		//na�i panel preko koordinata od �elije
		ImagePanel panel = (ImagePanel)view.getAiCells()[cell.getX()][cell.getY()];
		
		switch(cell.getDot()){
		case SEA:
			//ako je SEA onda ukloni sliku jer se samo background mijenja
			panel.removeImage();
			//ako je to samo mouseOver event, onda promijeni background u MOUSE_OVER_COLOR
			if(mouseOver){
				panel.setBackground(View.MOUSE_OVER_COLOR);
			}else{
				//ako nije mouseOver, onda vrati na SEA_COLOR
				panel.setBackground(View.SEA_COLOR);
			}						
			break;
		/*case SHIP:
			panel.removeImage();
			Color color = cell.getShip().getType().getColor();
			panel.setBackground(color);
			break;*/
		case HIT:
			//ako je player pogodio brod, provjeri da li je brod uni�ten
			if(cell.getShip().isDestroyed()){
				//ako je uni�ten onda promijeni background na boju iz ShipType, da bi se lijepo vidjelo da je to brod
				
				//to je foreach petlja, svaku vrijednost iz liste ili polja, spremi u shipCell varijablu 
				//za svaku �eliju u kojoj je dio broda, mijenjaj background
				for(Cell shipCell: cell.getShip().getCells()){		
					//na�i panel iz plo�e od ai-ja preko koordinata
					ImagePanel imPanel = (ImagePanel)view.getAICellPanelByXY(shipCell.getX(), shipCell.getY());
					//promijeni background �elije na boju od broda
					imPanel.setBackground(cell.getShip().getType().getColor());	
				}
				
			}
			//ako je fulao onda je background �elije SEA_COLOR
			else
				panel.setBackground(View.SEA_COLOR);
			//namjesti sliku koja �e se iscrtati preko backgrounda
			panel.setImage(View.EXPLOSION);
			//ukloni listenere jer player vi�e ne mo�e kliknuti na tu �eliju
			removeMouseListeners(panel);
			break;
		case MISS:
			//ako je player fulao, nema broda na �eliji onda namjesti sliku u background
			//kad fula onda je ispod Dot=SEA_COLOR
			panel.setBackground(View.SEA_COLOR);
			panel.setImage(View.MISS);
			//ukloni listenere jer player vi�e ne mo�e kliknuti na tu �eliju
			removeMouseListeners(panel);
			break;
		default:
			break;
		}
	}
	
	//mijenja background i sliku za �eliju iz parametra funkcije, za plo�u od playera-ja, 
		// 1. na kojoj AI ga�a brodove
		// 2. na kojoj player smje�ta svoje brodove
		// za oba 2 slu�aja se poziva ova funkcija	
	
	/***
	 * mijenja background i sliku za �eliju iz parametra funkcije, za plo�u od playera-ja, 
		1. na kojoj AI ga�a brodove
		2. na kojoj player smje�ta svoje brodove
		za oba 2 slu�aja se poziva ova funkcija	
		
	 * @param cell �elija za koju se mijenja pozadina ili slika
	 * @param placing ozna�a da li player dodaje brod ili ne
	 * @param available ozna�ava da li je �elija dostupna, ako nije da se brod kod namje�tanja prika�e crveno (crveno=ne mo�e tu dodati brod)
	 * @param mouseOver ozna�ava da li je pozvana funkcija iz mouseOver eventa
	 */
	public void changeBackgroundForPlayerBoard(Cell cell, boolean placing, boolean available, boolean mouseOver){
		//na�i panel preko �elije iz parametra funkcije
		ImagePanel panel = (ImagePanel)view.getPlayerCells()[cell.getX()][cell.getY()];		
		
		switch(cell.getDot()){
		case SEA:
			//ako je �elija tipa SEA ukloni sliku
			panel.removeImage();
			//ako player namje�ta brod, i mogu�e je na �eliju smjestit brod
			if(placing && available)
				//promijeni background �elije u boju broda
				panel.setBackground(chosenShip.getType().getColor());
			else if(placing && !available)
				//ako player namje�ta brod ali ne mo�e smjestiti na tu �eliju brod
				//onda namjesti background na NOT_AVAILABLE_TO_PLACE_COLOR
				panel.setBackground(View.NOT_AVAILABLE_TO_PLACE_COLOR);			
			else if(mouseOver)
				//ako je ovo mouseOver event onda namjesti background MOUSE_OVER_COLOR, player ne namje�ta brod
				//nego samo ide preko �elija u plo�i
				panel.setBackground(View.MOUSE_OVER_COLOR);
			else
				//ako nije mouseOver event onda na SEA_COLOR, jer ovo nije mouseOver event nego mouseExited i player ne namje�ta brod				
				panel.setBackground(View.SEA_COLOR);
			break;
		case SHIP:
			//ako je �elija tipa SHIP ukloni sliku ako slu�ajno postoji
			panel.removeImage();
			if(placing)
				//ako player name�ta brod, ne mo�e na drugi brod dodati novi pa se background namjesti na NOT_AVAILABLE_TO_PLACE_COLOR
				panel.setBackground(View.NOT_AVAILABLE_TO_PLACE_COLOR);
			else{
				//ne namje�ta brod pa se samo promijeni background da bude kao boja broda 
				Color color = cell.getShip().getType().getColor();
				panel.setBackground(color);
			}
			break;
		case HIT:
			//ako je pogo�en brod (AI je ga�ao) onda se namjesti slika na EXPLOSION
			panel.setImage(View.EXPLOSION);
			break;
		case MISS:
			//ako nije pogo�en brod (AI je ga�ao) onda se namjesti slika na MISS
			panel.setImage(View.MISS);
			break;
				
		}
	}
	
	/*public void displayShipForPositioning(){
		int[] coords = view.getXYForPlayerBoard(lastPanel);				
		//jc.repaint();
		
		Cell cell = playerBoardController.getBoard().getByXY(coords[0], coords[1]);
		if(chosenShip!=null){					
			boolean available = playerBoardController.canPlace(coords[0], coords[1], chosenShip.getSize(), chosenOrientation);					
			changeBackgroundForPlayerBoard(cell, true, available, true);					
			
			if(chosenOrientation==Orientation.HORIZONTAL){
				int limit = 10;
				if(coords[1]+chosenShip.getSize()<limit)
					limit = coords[1]+chosenShip.getSize();
				resetBackgroundColorForPlayerBoard();
				for(int y=coords[1]; y<limit; y++){
					cell = playerBoardController.getBoard().getByXY(coords[0], y);
					changeBackgroundForPlayerBoard(cell, true, available, true);
				}
			}else{
				int limit = 10;
				if(coords[0]+chosenShip.getSize()<limit)
					limit = coords[0]+chosenShip.getSize();
				resetBackgroundColorForPlayerBoard();
				for(int x=coords[0]; x<limit; x++){
					cell = playerBoardController.getBoard().getByXY(x, coords[1]);
					changeBackgroundForPlayerBoard(cell, true, available, true);						
				}
			}
		}else{
			changeBackgroundForPlayerBoard(cell, false, false, true);
		}
	}*/
	
	//prika�e brod preko svih �elija na koje bi bio dodan, da player zna gdje dodaje brod
	public void displayShipForPositioning(){
		//na�i �eliju na kojoj je player mi� pozicionirao (mouseOver)
		int[] coords = ((ImagePanel)lastPanel).getCoords();				
		//jc.repaint();
		
		//izvadi �eliju iz modela od player-a
		Cell cell = playerBoardController.getBoard().getByXY(coords[0], coords[1]);
		if(chosenShip!=null){
			//ako je ship odabran, to zna�i da je player kliknuo na ship button
			
			//provjeri da li se na �eliju mo�e dodati broj
			boolean available = playerBoardController.canPlace(coords[0], coords[1], chosenShip.getSize(), chosenOrientation);
			
			//promijeni background za �eliju
			//placing parametar od funkcije je true
			//mouseover je true
			changeBackgroundForPlayerBoard(cell, true, available, true);					
			
			//limit slu�i da bi se znalo koliko se �elija mo�e zauzeti za prikaz broda na plo�i
			//koristi se u petlji za jednu od osi, tj. os na koju player �eli dodati brod (VERTIKALNU ili HORIZONTALNU)
			//npr. ako je brod dug 2 �elije a player ga �eli dodati vertikalno na zadnji red, onda je limit=10, jer je y=9 i samo se jednom vrti petlja 
			int limit=10;
			
			//resetiraj background na player plo�i za sve �elije
			resetBackgroundColorForPlayerBoard();
			if(chosenOrientation==Orientation.HORIZONTAL){
				limit=10;	
				//ako je os horizontalna onda se mijenja y
				
				//ako je y od �elije + veli�ina broda manje od limita (10), onda je limit y �elije+veli�ina broda
				//ako je ve�e onda je limit 10
				if(coords[1]+chosenShip.getSize()<limit){
					limit=coords[1]+chosenShip.getSize();
				}
				
				//pove�avaj poziciju na y osi
				for(int y=coords[1]; y<limit; y++){
					//na�i �eliju iz modela
					cell = playerBoardController.getBoard().getByXY(coords[0], y);
					//promijeni background
					changeBackgroundForPlayerBoard(cell, true, available, true);
				}
			}else{
				limit=10;	
				//ako je os vertikalna onda se mijenja x
				
				//ako je x od �elije + veli�ina broda manje od limita (10), onda je limit x �elije+veli�ina broda
				//ako je ve�e onda je limit 10
				if(coords[0]+chosenShip.getSize()<limit){
					limit=coords[0]+chosenShip.getSize();
				}
				for(int x=coords[0]; x<limit; x++){
					//na�i �eliju iz modela
					cell = playerBoardController.getBoard().getByXY(x, coords[1]);
					//promijeni background
					changeBackgroundForPlayerBoard(cell, true, available, true);
				}
			}
		}else{
			//ako nije odabrao brod onda promijeni background prema modelu
			//makar se ovaj uvijet nikad ne bi trebao izvr�iti jer se metoda poziva samo kada je brod odabran
			changeBackgroundForPlayerBoard(cell, false, false, true);
		}
	}
	
	//slu�i da se ukloni ship button
	public void removeShipButton(Ship ship){
		//spremi panel s buttons u varijablu
		JPanel panel = view.getButtonPanel();
		
		//vrti onoliko buta koliko panel ima komponenti na sebi (dodali smo samo Ship buttone)
		for(int i=0; i<panel.getComponentCount(); i++){
			//izvuci komponentu na poziciji i
			Component c = panel.getComponent(i);
			
			//provjerava da li je komponenta instanca (objekt) klase ShipButton i da li u tom ShipButton-u attribut ship ima vrijednost
			if(c instanceof ShipButton && ((ShipButton)c).getShip()==ship){
				panel.remove(c);
			}
		}
		//da se panel ponovno validira, tj. namjesti pozicije svoj komponenti nakon �to se jedna ili vi�e njih ukloni 
		panel.revalidate();
		//ponovno iscrtaj komponente u panelu
		panel.repaint();
	}
	
	//doda ShipButton u panel
	public ShipButton addShipButton(Ship ship){
		//create new ship button
		ShipButton button = new ShipButton(ship);
		view.getButtonPanel().add(button);
		//return so controller can add listener to button
		return button;
	}
	
	//iste brodove koje je player dodao, ai dodaje na svoju plo�u nasumi�no
	public void fillAiBoard(){
		//za svaki brod koji je player dodao (a mora ih dodati sve koji su definirani u ShipType enumeraciji
		for(int i=0; i<playerBoardController.getShipCount();i++){
			//enumeracija ima values metodu koja vrati polje sa svim elementima od enumeracije
			ShipType type = ShipType.values()[i];
			
			//napravi novu instancu broda za ShipType			
			Ship aiShip = new Ship(type);
			//ai nasumi�no dodaje brod na neku od slobodnih �elija, i na nasumi�nu os
			if(randomOneOrZero()==1)
				aiBoardController.placeShipRandomly(aiShip, Orientation.HORIZONTAL);
			else
				aiBoardController.placeShipRandomly(aiShip, Orientation.VERTICAL);
		}
		//ponovno "iscrtaj" sve �elije na AI plo�i
		//mo�e se i ukloniti jer player ne vidi brodove od AI-ja, ali ako bi se �eljelo za debugging prikazati
		//brodove onda se treba pustiti. Ako se pusti i �eli prikazati brodove od AI onda se mora odkomentirati
		//case SHIP u changeBackgroundForAIBoard metodi
		resetBackgroundColorForAIBoard();
		removeMouseListenersForBoard(view.getPlayerCells());
	}
	
	//za plo�u sa �elijama, ukloni na svakoj �eliji sve mouse listenere
	private void removeMouseListenersForBoard(JPanel[][] board){
		for(int i=0;i<10;i++){
			for(int j=0;j<10;j++){
				removeMouseListeners(board[i][j]);
			}
		}
	}
	
	//za �eliju ukloni sve mouse listenere
	private void removeMouseListeners(JPanel panel){
		MouseListener[] listeners = panel.getMouseListeners();				
		for( MouseListener ml : listeners) {
			panel.removeMouseListener(ml);
		}	
	}
	
	//kad se pozove, onda AI igra
	private void aiTurn(){
		updateLog("AI's turn!");
		System.out.println("AI turn");
		//disablaj listenere na AI plo�i dok AI ne zavr�i igrati
		aiBoardListenersDisabled=true;
		
		//Runnable je interface koji se implementira, i u run metodi se definira �to se izvr�ava
		//new Runnable(){...} zna�i da je to onda anonimna klasa jer nema ime
		//nama treba da bi pokrenuli taj kod od AI-ja u novom thread-u, jer ako se pokrene na threadu od GUI-a
		//onda su svi event listeneri i refreshanje su�elja blokirani 
		Runnable aiTurnTask = new Runnable() {						
			@Override
			public void run() {
				System.out.println("Ai turn threadID="+Thread.currentThread().getId());
				
				//�elija koju je AI pogodio u prethodnom potezu, treba ju ponovno poslati AI-ju da bi znao od kud da krene tra�iti novu �eliju
				//gdje bi mogao biti dio broda. 
				Cell lastHit = null;
				//ozna�ava da li je AI u prethodnom potezu uni�tio broj
				boolean destroyed = false;	
				
				//vrti sve dok nije gameOver, playerController ima metodu isGameOver s kojom provjeravamo model od player-a
				//ako je gameOver onda su i svi brodovi od player-a uni�teni
				while(!playerBoardController.isGameOver()){		
					System.out.println("Looping inside aiTurn");
					
					//daj AI-ju da igra, i spremi �eliju koju je ga�ao					
					Cell shotCell = aiBoardController.play(lastHit, destroyed);
					System.out.println("AI shot at position "+shotCell);
					
					//ima bug ali nije prebitan, pa treba iz modela od player plo�e na�i pravu �eliju
					Cell realCell = playerBoardController.getBoard().getByXY(shotCell.getX(), shotCell.getY());
					
					//simulacija da AI kao misli, malo se produ�i njegov potez
					//vrijeme koje �e Thread u kojem AI igra, malo �ekati pa krenuti nakon Thread.sleep 
					//vrijednost je u milisekundama
					int sleepTime=0;
					
					//ako je pogodio u prethodnom potezu onda ne "misli" previ�e
					if(lastHit!=null){
						//pola sekunde
						sleepTime=500;
					}else{
						//na�e se nasumi�ni cijeli broj od 0 do 2
						sleepTime = new Random().nextInt(2);
						//ako je 0 onda 1 sekundu se �eka
						if(sleepTime==0)
							sleepTime=1000;
						else
							//ina�e pomno�i sa 1000 da bi dobili milisekunde
							sleepTime=sleepTime*1000;
					}	
					
					try {
						//izvr�avanje se zaustavi za sleepTime
						Thread.sleep(sleepTime);
						System.out.println("AI slept for "+sleepTime+" ms");
					} catch (InterruptedException e) {				 
						e.printStackTrace();
					}
					
					//AI puca na �eliju u modelu u player plo�e, preko kontrolera od te plo�e
					playerBoardController.shootCell(shotCell.getX(), shotCell.getY());
					
					//ako je AI pogodio brod...
					if(realCell.getDot()==Dot.HIT){
						
						//uzmi brod iz te �elije
						Ship ship =realCell.getShip();
						//namjesti tu �eliju u privremenu varijablju, da bi kod sljede�eg poziva play metode
						//od AI kontrolera, AI mogao odlu�iti na koju susjednu �eliju da dalje ga�a
						lastHit=realCell;
						
						//ako je brod uni�ten...
						if(ship.isDestroyed()){
							//spremi vrijednost u privremenu varijablu da bi AI znao da sljede�i potez mo�e
							//nasumi�no ga�ati
							destroyed=true;
							System.out.println("AI destroyed ship "+ship);
							//u ovom threadu se ne smije a�urirati su�elje, pa se Swing-u daje "zadatak" (zapravo runnable sa na�im kodom)
							//da ga izvr�i na svom UI threadu gdje se mo�e mijenjati su�elje
							SwingUtilities.invokeLater(new AppendToLogTask("AI destroyed "+ship));					
						}else{
							System.out.println("AI shot ship "+ship+", remaining health="+ship.getHealth());
							//log labelu se isto iz ovog thread-a ne mo�e mijenjati, pa treba slati task Swing-u
							SwingUtilities.invokeLater(new AppendToLogTask("AI shot "+ship+" at position "+realCell+". Remaining health "+ship.getHealth()));
						}
						//treba promijeniti izgleda �elije koju je AI ga�ao na plo�i od playera, isto treba novi task za Swing
						SwingUtilities.invokeLater(new ChangePlayerBoardTask(realCell));				
						continue;
					}else if(realCell.getDot()==Dot.MISS){
						//ako je AI fulao onda preko loga javi playeru da je AI pogrije�io
						SwingUtilities.invokeLater(new AppendToLogTask("AI missed at position "+realCell));				
						System.out.println("AI missed");
						//poni�ti ove dve varijable, da bi AI znao da je fulao
						lastHit=null;
						destroyed=false;
						
						//treba promijeniti izgleda �elije koju je AI ga�ao na plo�i od playera, isto treba novi task za Swing
						SwingUtilities.invokeLater(new ChangePlayerBoardTask(realCell));				
						break;
					}			
				}
				
				//unutar taska je provjera da li je game over, ako nije se uklju�i plo�a od AI-ja da bi player mogao igrati
				SwingUtilities.invokeLater(new AiTurnDoneTask());								
			}
		};
		
		//treba nam novi thread, ima za parametar Runnable a u aiTurnTask smo dodali kod za AI-ja
		Thread t = new Thread(aiTurnTask);
		//treba pokrenuti thread
		t.start();
	}
	
	//sve te klase koje implementiraju Runnable, se daju Swingu na izvr�avanje, tako da u run metodi mo�emo a�urirati su�elje
	
	protected class AppendToLogTask implements Runnable{
		private String text;
	
		//u konstruktoru spremiti tekst koji ide u log labelu
		public AppendToLogTask(String text) {
			this.text=text;
		}
		
		//izvr�ava ju thread, ako je preko Swing.invokeLater onda swing to izvr�i
		//ako je preko new Thread(new AppendToLogTask("")).start(), onda je to neki drugi thread
		@Override
		public void run() {
			updateLog(text);			
		}
	}
	
	protected class ChangePlayerBoardTask implements Runnable{
		private Cell cell;
	
		//u konstruktoru spremiti �eliju koju treba promijeniti
		public ChangePlayerBoardTask(Cell cell) {
			this.cell=cell;
		}
		
		@Override
		public void run() {
			//samo za info, da se zna koji thread izvr�ava ovu metodu
			System.out.println("ChangePlayerBoardTask on threadID="+Thread.currentThread().getId());
			//svi su parametri false, jer nam treba samo da se prema modelu promijeni izgled �elije koju je AI ga�ao
			changeBackgroundForPlayerBoard(cell, false, false, false);			
		}
	}
	
	protected class AiTurnDoneTask implements Runnable{
		@Override
		public void run() {
			System.out.println("GameOverCheckTask on threadID="+Thread.currentThread().getId());
			//provjeri u player board modelu da li je AI pobijedio
			if(playerBoardController.isGameOver()){
				updateLog("Ai won. Game over!");
				System.out.println("AI won");
				//ako je AI pobijedio, onda je game over i player nema �ta vi�e kliktati po AI plo�i
				removeMouseListenersForBoard(view.getAiCells());
			}else{
				//uklju�i listener na AI plo�i
				aiBoardListenersDisabled=false;
				//i upozori player-a
				updateLog("Your turn!");
			}
		}
	}
	
	//Label ne mo�e prikazati novi red preko \n, ali mo�e preko html-a	
	protected void updateLog(String text){
		//uzmi trenutni tekst iz labele
		String current = view.getTextLabel().getText();
		//makni po�etni <html> tag
		String filtered = current.replace("<html>", "");
		//makni zavr�ni </html> tag
		filtered = current.replace("</html>", "");
		
		//dodaj na po�etak labele novi tekst, i zalijepi <html> tagove
		view.getTextLabel().setText("<html>"+text+"<br>"+filtered+"</html>");
	}
	
	//vrati 0 ili 1, preko Math.Random funkcije, ona daje decimalni broj od 0.0 do 0.9 
	public int randomOneOrZero(){
		if(Math.random()>0.5)
			return 1;
		return 0;
	}
}
