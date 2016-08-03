package view;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;



public class ImagePanel extends JPanel{
	private static final long serialVersionUID = 1L;
	
	//slika koja se iscrtava na panelu
	private BufferedImage image;
    
    
	//kad se namjesti slika odmah se pozove repaint(), da bi se slika iscrtala (repaint bi trebao negdje u sebi pozvati našu paintComponent metodu)
    public void setImage(BufferedImage image){
    	this.image=image;    	
    	repaint();
    }
    
    //koordinate od panela
    private int[] coords;
    
    public void setCoords(int x, int y){
    	this.coords=new int[]{x,y};
    }
    
    public int[] getCoords(){
    	return coords;
    }
    
    public void removeImage(){
    	this.image=null;    	
    }

    //ako treba mijenjati naèin iscrtavanja, onda se override-a ova metoda
    @Override
    protected void paintComponent(Graphics g) {
    	//pozove se metoda u superklasi (JPanel) da bi se iscrtao background, border i sve ono osnovno od panela
        super.paintComponent(g);
        
        //ako je namještena slika, onda se iscrta
        if(image!=null)        	
        	g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters            
    }

}