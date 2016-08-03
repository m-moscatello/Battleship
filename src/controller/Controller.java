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
	//odabran brod, kad se klikne na button od broda za namještanje na ploèu
	private Ship chosenShip;
	//treba da se tokom pozicioniranja broda zna iznad koje æelije je igraè oznaèio
	private JPanel lastPanel;
	
	//kad se jednom okrene mouse wheel dobiješ dva eventa, tek drugi put treba provjeravat vrijednosti od eventa
	private static final int MOUSE_WHEEL_MOVE_LIMIT=2;
	//poveæava se za svaki mouse wheel event i kad doðe na MOUSE_WHEEL_MOVE_LIMIT onda se èitaju vrijednost od eventa
	private int mouseWheelMoveCounter=0;
	
	//služi da se iskljuèe mouse listeneri na ai ploèi
	private boolean aiBoardListenersDisabled=false;
	//služi da se iskljuèe listeneri na player ploèi, iskljuèi se kad player sve brodove doda na ploèu
	private boolean playerBoardListenersDisabled=true;
	
	public Controller(View view) {
		this.view = view;		
		
		//u konstruktoru kreiraju se kontrolleri za igraèa i za ai
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
				//ako su eventi iskljuèeni onda return da se niš ne napravi
				if(playerBoardListenersDisabled)
					return;
				
				//poveæaj counter da znamo da li èitamo vrijednosti ili ne
				mouseWheelMoveCounter+=1;
			   
				//ako je counter==MOUSE_WHEEL_MOVE_LIMIT onda promijeni orijentaciju
				if(mouseWheelMoveCounter==MOUSE_WHEEL_MOVE_LIMIT){
					if(e.getWheelRotation()==1)				  
						chosenOrientation=Orientation.VERTICAL;
					else
						chosenOrientation=Orientation.HORIZONTAL;
					//prikaži kako æe se brod dodati na ploèu, tj. koje æe æelije zauzeti
					displayShipForPositioning();
					//postavi counter na 0, da se ponovno mogu èitati mouse wheel eventi
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
				//ako su eventi iskljuèeni onda return da se niš ne napravi
				if(playerBoardListenersDisabled)
					return;
				lastPanel = (JPanel)e.getSource();
				
				//iz zadnje æelije na koju je player kliknuo naði njezine koordinate
				int[] coords = ((ImagePanel)lastPanel).getCoords();
				
				//ako je player odabrao brod...
				if(chosenShip!=null){			
					//provjeri u player kontroleru da li se brod može dodati na tu poziciju u model (model=Board)
					boolean available = playerBoardController.canPlace(coords[0], coords[1], chosenShip.getSize(), chosenOrientation);
					//ako se može dodati...
					if(available){						
						System.out.println("Ship to place: "+chosenShip+" on x="+coords[0]+", y="+coords[1]);
						//preko kontrolera dodaj brod u model
						playerBoardController.place(coords[0], coords[1], chosenShip, chosenOrientation);
						
						//ukloni button od broda sa panela
						removeShipButton(chosenShip);
						//resetiraj varijable koje služe za dodavanje broda
						chosenShip=null;
						chosenOrientation=null;
						//promijeni boje i slike na æelijama prema modelu na ploèi od playera
						resetBackgroundColorForPlayerBoard();
						
						System.out.println("Placed ships: "+playerBoardController.getShipCount()+", ShipType length: "+ShipType.values().length);
						//ako je player dodao sve brodove, a to znamo preko duljini vrijednosti u enumeraciji od tipova brodova...
						if(playerBoardController.getShipCount()==ShipType.values().length){
							System.out.println("Fill ai board");
							//random napuni brodove za ai-ja
							fillAiBoard();
							
							//ako funkcija randomOneOrZero vrati 1, onda player igra prvi, 0 ai igra prvi
							if(randomOneOrZero()==1){
								//ukljuèi listenere sa ai ploèe da player može igrat
								aiBoardListenersDisabled=false;
								updateLog("Your turn");			
							}else{
								aiTurn();
							}
						}
						//iskljuèi listenere sa player board da ne bi više klikao na svoju ploèu
						playerBoardListenersDisabled=true;
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				//ako su eventi iskljuèeni onda return da se niš ne napravi
				if(playerBoardListenersDisabled)
					return;
				lastPanel = (JPanel)e.getSource();
				//prikaži brod na ploèi, da player zna gdje ga smješta
				displayShipForPositioning();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				//ako su eventi iskljuèeni onda return da se niš ne napravi
				if(playerBoardListenersDisabled)
					return;
				JPanel panel = (JPanel)e.getSource();
				//naði koordinate od panela
				int[] coords = ((ImagePanel)panel).getCoords();
				
				//naði æeliju iz modela iz koje je player izašao
				Cell cell = playerBoardController.getBoard().getByXY(coords[0], coords[1]);
				//resetiraj background za tu æeliju
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
				//ako su listeneri iskljuèeni onda return da se niš ne napravi
				if(aiBoardListenersDisabled)
					return;
				//ovo je samo provjera na kojem se threadu izvršava event, to je thread od gui-ja
				System.out.println("MouseClickedEvent on threadID="+Thread.currentThread().getId());
				
				JPanel panel = (JPanel)e.getSource();
				int[] coords = ((ImagePanel)panel).getCoords();
				//kad se klikne na æeliju, reci ai kontroleru da je player "pucao" na tu æeliju
				aiBoardController.shootCell(coords[0], coords[1]);
				//izvuci æeliju iz modela od ai-ja, da bi se moglo provjeriti što je player "pogodio"
				Cell cell = aiBoardController.getBoard().getByXY(coords[0], coords[1]);
				
				//pogoðen brod...
				if(cell.getDot()==Dot.HIT){
					//ako je brod uništen, reci playeru preko labele da je uništio brod
					if(cell.getShip().isDestroyed()){
						updateLog("You destroyed ship "+cell.getShip());
						System.out.println("Player destroyed ship "+cell.getShip());
					//ako nije uništen brod, reci playeru preko labele da je pogodio brod
					}else{
						updateLog("You hit ship "+cell.getShip()+" on position "+cell);
						System.out.println("Player hit ship "+cell.getShip()+" on position "+cell+". Remaining health "+cell.getShip().getHealth());
					}
					
					//promijeni background za tu æeliju u ai ploèi
					changeBackgroundForAIBoard(cell, false);
					
					//ako su u modelu od AI-ja svi brodovi uništeni, onda je GG, player je pobjedio
					if(aiBoardController.isGameOver()){
						updateLog("You won. Congratulations!");
						System.out.println("Player won");
						//ukloni sve listenere sa AI ploèe da player ne bi više mogao kliktati
						removeMouseListenersForBoard(view.getAiCells());
					}
				//player fullao brod...
				}else if(cell.getDot()==Dot.MISS){
					//upozori ga preko labele da je fulao
					updateLog("You missed on position "+cell);
					System.out.println("Player missed on position "+cell);
					//promijeni background za æeliju na AI ploèi
					changeBackgroundForAIBoard(cell, false);
					//daj AI-u da igra
					aiTurn();
				}	
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				if(aiBoardListenersDisabled)
					return;
				//samo ofarbaj æeliju u žuto, ako je veæ æelija fulana ili pogoðena, onda smo uklonili veæ event listener
				//pa player ne može dizati evente na toj æeliji
				JPanel jc = (JPanel)e.getSource();
				jc.setBackground(Color.YELLOW);
				jc.repaint();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if(aiBoardListenersDisabled)
					return;
				//vrati nazad background na SEA_COLOR, ako je æelija pogoðena ili fulana, ovo neæe raditi jer smo uklonili veæ event listener
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
		//omoguæi da player klikæe i polazi mišem preko svog polja da bi namjestio brodove
		playerBoardListenersDisabled=false;
		//za poèetak je orijentacija horizontalno
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
		
		//dodaj mouse listenere na æelije u ai i player ploèama
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				this.view.getPlayerCells()[i][j].addMouseListener(mPlayerBoardListener);
				this.view.getAiCells()[i][j].addMouseListener(mAIBoardListener);
			}
		}
		
		view.getFrame().addMouseWheelListener(mWListener);
		//reci playeru preko labele da doda svoje brodove na ploèu
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
	
	//proðe kroz sve æelije u modelu od player-a i namjesti background ili sliku za HIT i MISS
	public void resetBackgroundColorForPlayerBoard(){
		Cell cell = null;
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				//iz modela u player kontroleru naði æeliju
				cell = playerBoardController.getBoard().getByXY(i, j);
				changeBackgroundForPlayerBoard(cell, false, false, false);				
			}
		}
	}
	
	//proðe kroz sve æelije u modelu od ai-a i namjesti background ili sliku za HIT i MISS
	public void resetBackgroundColorForAIBoard(){
		Cell cell = null;
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				//iz modela u ai kontroleru naði æeliju
				cell = aiBoardController.getBoard().getByXY(i, j);
				changeBackgroundForAIBoard(cell, false);				
			}
		}
	}
	
	//mijenja background i sliku za æeliju iz parametra funkcije, za ploèu od AI-ja, 
	// 1. na kojoj player gaða brodove
	// 2. na kojoj ai smješta svoje brodove
	// za oba 2 sluèaja se poziva ova funkcija
	public void changeBackgroundForAIBoard(Cell cell, boolean mouseOver){
		//naði panel preko koordinata od æelije
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
			//ako je player pogodio brod, provjeri da li je brod uništen
			if(cell.getShip().isDestroyed()){
				//ako je uništen onda promijeni background na boju iz ShipType, da bi se lijepo vidjelo da je to brod
				
				//to je foreach petlja, svaku vrijednost iz liste ili polja, spremi u shipCell varijablu 
				//za svaku æeliju u kojoj je dio broda, mijenjaj background
				for(Cell shipCell: cell.getShip().getCells()){		
					//naði panel iz ploèe od ai-ja preko koordinata
					ImagePanel imPanel = (ImagePanel)view.getAICellPanelByXY(shipCell.getX(), shipCell.getY());
					//promijeni background æelije na boju od broda
					imPanel.setBackground(cell.getShip().getType().getColor());	
				}
				
			}
			//ako je fulao onda je background æelije SEA_COLOR
			else
				panel.setBackground(View.SEA_COLOR);
			//namjesti sliku koja æe se iscrtati preko backgrounda
			panel.setImage(View.EXPLOSION);
			//ukloni listenere jer player više ne može kliknuti na tu æeliju
			removeMouseListeners(panel);
			break;
		case MISS:
			//ako je player fulao, nema broda na æeliji onda namjesti sliku u background
			//kad fula onda je ispod Dot=SEA_COLOR
			panel.setBackground(View.SEA_COLOR);
			panel.setImage(View.MISS);
			//ukloni listenere jer player više ne može kliknuti na tu æeliju
			removeMouseListeners(panel);
			break;
		default:
			break;
		}
	}
	
	//mijenja background i sliku za æeliju iz parametra funkcije, za ploèu od playera-ja, 
		// 1. na kojoj AI gaða brodove
		// 2. na kojoj player smješta svoje brodove
		// za oba 2 sluèaja se poziva ova funkcija	
	
	/***
	 * mijenja background i sliku za æeliju iz parametra funkcije, za ploèu od playera-ja, 
		1. na kojoj AI gaða brodove
		2. na kojoj player smješta svoje brodove
		za oba 2 sluèaja se poziva ova funkcija	
		
	 * @param cell æelija za koju se mijenja pozadina ili slika
	 * @param placing oznaèa da li player dodaje brod ili ne
	 * @param available oznaèava da li je æelija dostupna, ako nije da se brod kod namještanja prikaže crveno (crveno=ne može tu dodati brod)
	 * @param mouseOver oznaèava da li je pozvana funkcija iz mouseOver eventa
	 */
	public void changeBackgroundForPlayerBoard(Cell cell, boolean placing, boolean available, boolean mouseOver){
		//naði panel preko æelije iz parametra funkcije
		ImagePanel panel = (ImagePanel)view.getPlayerCells()[cell.getX()][cell.getY()];		
		
		switch(cell.getDot()){
		case SEA:
			//ako je æelija tipa SEA ukloni sliku
			panel.removeImage();
			//ako player namješta brod, i moguæe je na æeliju smjestit brod
			if(placing && available)
				//promijeni background æelije u boju broda
				panel.setBackground(chosenShip.getType().getColor());
			else if(placing && !available)
				//ako player namješta brod ali ne može smjestiti na tu æeliju brod
				//onda namjesti background na NOT_AVAILABLE_TO_PLACE_COLOR
				panel.setBackground(View.NOT_AVAILABLE_TO_PLACE_COLOR);			
			else if(mouseOver)
				//ako je ovo mouseOver event onda namjesti background MOUSE_OVER_COLOR, player ne namješta brod
				//nego samo ide preko æelija u ploèi
				panel.setBackground(View.MOUSE_OVER_COLOR);
			else
				//ako nije mouseOver event onda na SEA_COLOR, jer ovo nije mouseOver event nego mouseExited i player ne namješta brod				
				panel.setBackground(View.SEA_COLOR);
			break;
		case SHIP:
			//ako je æelija tipa SHIP ukloni sliku ako sluèajno postoji
			panel.removeImage();
			if(placing)
				//ako player namešta brod, ne može na drugi brod dodati novi pa se background namjesti na NOT_AVAILABLE_TO_PLACE_COLOR
				panel.setBackground(View.NOT_AVAILABLE_TO_PLACE_COLOR);
			else{
				//ne namješta brod pa se samo promijeni background da bude kao boja broda 
				Color color = cell.getShip().getType().getColor();
				panel.setBackground(color);
			}
			break;
		case HIT:
			//ako je pogoðen brod (AI je gaðao) onda se namjesti slika na EXPLOSION
			panel.setImage(View.EXPLOSION);
			break;
		case MISS:
			//ako nije pogoðen brod (AI je gaðao) onda se namjesti slika na MISS
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
	
	//prikaže brod preko svih æelija na koje bi bio dodan, da player zna gdje dodaje brod
	public void displayShipForPositioning(){
		//naði æeliju na kojoj je player miš pozicionirao (mouseOver)
		int[] coords = ((ImagePanel)lastPanel).getCoords();				
		//jc.repaint();
		
		//izvadi æeliju iz modela od player-a
		Cell cell = playerBoardController.getBoard().getByXY(coords[0], coords[1]);
		if(chosenShip!=null){
			//ako je ship odabran, to znaèi da je player kliknuo na ship button
			
			//provjeri da li se na æeliju može dodati broj
			boolean available = playerBoardController.canPlace(coords[0], coords[1], chosenShip.getSize(), chosenOrientation);
			
			//promijeni background za æeliju
			//placing parametar od funkcije je true
			//mouseover je true
			changeBackgroundForPlayerBoard(cell, true, available, true);					
			
			//limit služi da bi se znalo koliko se æelija može zauzeti za prikaz broda na ploèi
			//koristi se u petlji za jednu od osi, tj. os na koju player želi dodati brod (VERTIKALNU ili HORIZONTALNU)
			//npr. ako je brod dug 2 æelije a player ga želi dodati vertikalno na zadnji red, onda je limit=10, jer je y=9 i samo se jednom vrti petlja 
			int limit=10;
			
			//resetiraj background na player ploèi za sve æelije
			resetBackgroundColorForPlayerBoard();
			if(chosenOrientation==Orientation.HORIZONTAL){
				limit=10;	
				//ako je os horizontalna onda se mijenja y
				
				//ako je y od æelije + velièina broda manje od limita (10), onda je limit y æelije+velièina broda
				//ako je veæe onda je limit 10
				if(coords[1]+chosenShip.getSize()<limit){
					limit=coords[1]+chosenShip.getSize();
				}
				
				//poveæavaj poziciju na y osi
				for(int y=coords[1]; y<limit; y++){
					//naði æeliju iz modela
					cell = playerBoardController.getBoard().getByXY(coords[0], y);
					//promijeni background
					changeBackgroundForPlayerBoard(cell, true, available, true);
				}
			}else{
				limit=10;	
				//ako je os vertikalna onda se mijenja x
				
				//ako je x od æelije + velièina broda manje od limita (10), onda je limit x æelije+velièina broda
				//ako je veæe onda je limit 10
				if(coords[0]+chosenShip.getSize()<limit){
					limit=coords[0]+chosenShip.getSize();
				}
				for(int x=coords[0]; x<limit; x++){
					//naði æeliju iz modela
					cell = playerBoardController.getBoard().getByXY(x, coords[1]);
					//promijeni background
					changeBackgroundForPlayerBoard(cell, true, available, true);
				}
			}
		}else{
			//ako nije odabrao brod onda promijeni background prema modelu
			//makar se ovaj uvijet nikad ne bi trebao izvršiti jer se metoda poziva samo kada je brod odabran
			changeBackgroundForPlayerBoard(cell, false, false, true);
		}
	}
	
	//služi da se ukloni ship button
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
		//da se panel ponovno validira, tj. namjesti pozicije svoj komponenti nakon što se jedna ili više njih ukloni 
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
	
	//iste brodove koje je player dodao, ai dodaje na svoju ploèu nasumièno
	public void fillAiBoard(){
		//za svaki brod koji je player dodao (a mora ih dodati sve koji su definirani u ShipType enumeraciji
		for(int i=0; i<playerBoardController.getShipCount();i++){
			//enumeracija ima values metodu koja vrati polje sa svim elementima od enumeracije
			ShipType type = ShipType.values()[i];
			
			//napravi novu instancu broda za ShipType			
			Ship aiShip = new Ship(type);
			//ai nasumièno dodaje brod na neku od slobodnih æelija, i na nasumiènu os
			if(randomOneOrZero()==1)
				aiBoardController.placeShipRandomly(aiShip, Orientation.HORIZONTAL);
			else
				aiBoardController.placeShipRandomly(aiShip, Orientation.VERTICAL);
		}
		//ponovno "iscrtaj" sve æelije na AI ploèi
		//može se i ukloniti jer player ne vidi brodove od AI-ja, ali ako bi se željelo za debugging prikazati
		//brodove onda se treba pustiti. Ako se pusti i želi prikazati brodove od AI onda se mora odkomentirati
		//case SHIP u changeBackgroundForAIBoard metodi
		resetBackgroundColorForAIBoard();
		removeMouseListenersForBoard(view.getPlayerCells());
	}
	
	//za ploèu sa æelijama, ukloni na svakoj æeliji sve mouse listenere
	private void removeMouseListenersForBoard(JPanel[][] board){
		for(int i=0;i<10;i++){
			for(int j=0;j<10;j++){
				removeMouseListeners(board[i][j]);
			}
		}
	}
	
	//za æeliju ukloni sve mouse listenere
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
		//disablaj listenere na AI ploèi dok AI ne završi igrati
		aiBoardListenersDisabled=true;
		
		//Runnable je interface koji se implementira, i u run metodi se definira što se izvršava
		//new Runnable(){...} znaèi da je to onda anonimna klasa jer nema ime
		//nama treba da bi pokrenuli taj kod od AI-ja u novom thread-u, jer ako se pokrene na threadu od GUI-a
		//onda su svi event listeneri i refreshanje suèelja blokirani 
		Runnable aiTurnTask = new Runnable() {						
			@Override
			public void run() {
				System.out.println("Ai turn threadID="+Thread.currentThread().getId());
				
				//æelija koju je AI pogodio u prethodnom potezu, treba ju ponovno poslati AI-ju da bi znao od kud da krene tražiti novu æeliju
				//gdje bi mogao biti dio broda. 
				Cell lastHit = null;
				//oznaèava da li je AI u prethodnom potezu uništio broj
				boolean destroyed = false;	
				
				//vrti sve dok nije gameOver, playerController ima metodu isGameOver s kojom provjeravamo model od player-a
				//ako je gameOver onda su i svi brodovi od player-a uništeni
				while(!playerBoardController.isGameOver()){		
					System.out.println("Looping inside aiTurn");
					
					//daj AI-ju da igra, i spremi æeliju koju je gaðao					
					Cell shotCell = aiBoardController.play(lastHit, destroyed);
					System.out.println("AI shot at position "+shotCell);
					
					//ima bug ali nije prebitan, pa treba iz modela od player ploèe naæi pravu æeliju
					Cell realCell = playerBoardController.getBoard().getByXY(shotCell.getX(), shotCell.getY());
					
					//simulacija da AI kao misli, malo se produži njegov potez
					//vrijeme koje æe Thread u kojem AI igra, malo èekati pa krenuti nakon Thread.sleep 
					//vrijednost je u milisekundama
					int sleepTime=0;
					
					//ako je pogodio u prethodnom potezu onda ne "misli" previše
					if(lastHit!=null){
						//pola sekunde
						sleepTime=500;
					}else{
						//naðe se nasumièni cijeli broj od 0 do 2
						sleepTime = new Random().nextInt(2);
						//ako je 0 onda 1 sekundu se èeka
						if(sleepTime==0)
							sleepTime=1000;
						else
							//inaèe pomnoži sa 1000 da bi dobili milisekunde
							sleepTime=sleepTime*1000;
					}	
					
					try {
						//izvršavanje se zaustavi za sleepTime
						Thread.sleep(sleepTime);
						System.out.println("AI slept for "+sleepTime+" ms");
					} catch (InterruptedException e) {				 
						e.printStackTrace();
					}
					
					//AI puca na æeliju u modelu u player ploèe, preko kontrolera od te ploèe
					playerBoardController.shootCell(shotCell.getX(), shotCell.getY());
					
					//ako je AI pogodio brod...
					if(realCell.getDot()==Dot.HIT){
						
						//uzmi brod iz te æelije
						Ship ship =realCell.getShip();
						//namjesti tu æeliju u privremenu varijablju, da bi kod sljedeæeg poziva play metode
						//od AI kontrolera, AI mogao odluèiti na koju susjednu æeliju da dalje gaða
						lastHit=realCell;
						
						//ako je brod uništen...
						if(ship.isDestroyed()){
							//spremi vrijednost u privremenu varijablu da bi AI znao da sljedeæi potez može
							//nasumièno gaðati
							destroyed=true;
							System.out.println("AI destroyed ship "+ship);
							//u ovom threadu se ne smije ažurirati suèelje, pa se Swing-u daje "zadatak" (zapravo runnable sa našim kodom)
							//da ga izvrši na svom UI threadu gdje se može mijenjati suèelje
							SwingUtilities.invokeLater(new AppendToLogTask("AI destroyed "+ship));					
						}else{
							System.out.println("AI shot ship "+ship+", remaining health="+ship.getHealth());
							//log labelu se isto iz ovog thread-a ne može mijenjati, pa treba slati task Swing-u
							SwingUtilities.invokeLater(new AppendToLogTask("AI shot "+ship+" at position "+realCell+". Remaining health "+ship.getHealth()));
						}
						//treba promijeniti izgleda æelije koju je AI gaðao na ploèi od playera, isto treba novi task za Swing
						SwingUtilities.invokeLater(new ChangePlayerBoardTask(realCell));				
						continue;
					}else if(realCell.getDot()==Dot.MISS){
						//ako je AI fulao onda preko loga javi playeru da je AI pogriješio
						SwingUtilities.invokeLater(new AppendToLogTask("AI missed at position "+realCell));				
						System.out.println("AI missed");
						//poništi ove dve varijable, da bi AI znao da je fulao
						lastHit=null;
						destroyed=false;
						
						//treba promijeniti izgleda æelije koju je AI gaðao na ploèi od playera, isto treba novi task za Swing
						SwingUtilities.invokeLater(new ChangePlayerBoardTask(realCell));				
						break;
					}			
				}
				
				//unutar taska je provjera da li je game over, ako nije se ukljuèi ploèa od AI-ja da bi player mogao igrati
				SwingUtilities.invokeLater(new AiTurnDoneTask());								
			}
		};
		
		//treba nam novi thread, ima za parametar Runnable a u aiTurnTask smo dodali kod za AI-ja
		Thread t = new Thread(aiTurnTask);
		//treba pokrenuti thread
		t.start();
	}
	
	//sve te klase koje implementiraju Runnable, se daju Swingu na izvršavanje, tako da u run metodi možemo ažurirati suèelje
	
	protected class AppendToLogTask implements Runnable{
		private String text;
	
		//u konstruktoru spremiti tekst koji ide u log labelu
		public AppendToLogTask(String text) {
			this.text=text;
		}
		
		//izvršava ju thread, ako je preko Swing.invokeLater onda swing to izvrši
		//ako je preko new Thread(new AppendToLogTask("")).start(), onda je to neki drugi thread
		@Override
		public void run() {
			updateLog(text);			
		}
	}
	
	protected class ChangePlayerBoardTask implements Runnable{
		private Cell cell;
	
		//u konstruktoru spremiti æeliju koju treba promijeniti
		public ChangePlayerBoardTask(Cell cell) {
			this.cell=cell;
		}
		
		@Override
		public void run() {
			//samo za info, da se zna koji thread izvršava ovu metodu
			System.out.println("ChangePlayerBoardTask on threadID="+Thread.currentThread().getId());
			//svi su parametri false, jer nam treba samo da se prema modelu promijeni izgled æelije koju je AI gaðao
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
				//ako je AI pobijedio, onda je game over i player nema šta više kliktati po AI ploèi
				removeMouseListenersForBoard(view.getAiCells());
			}else{
				//ukljuèi listener na AI ploèi
				aiBoardListenersDisabled=false;
				//i upozori player-a
				updateLog("Your turn!");
			}
		}
	}
	
	//Label ne može prikazati novi red preko \n, ali može preko html-a	
	protected void updateLog(String text){
		//uzmi trenutni tekst iz labele
		String current = view.getTextLabel().getText();
		//makni poèetni <html> tag
		String filtered = current.replace("<html>", "");
		//makni završni </html> tag
		filtered = current.replace("</html>", "");
		
		//dodaj na poèetak labele novi tekst, i zalijepi <html> tagove
		view.getTextLabel().setText("<html>"+text+"<br>"+filtered+"</html>");
	}
	
	//vrati 0 ili 1, preko Math.Random funkcije, ona daje decimalni broj od 0.0 do 0.9 
	public int randomOneOrZero(){
		if(Math.random()>0.5)
			return 1;
		return 0;
	}
}
