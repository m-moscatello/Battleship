package view;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class ImagePanel extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private BufferedImage image;
    private int[] coords;
    
    public void setImage(BufferedImage image) {
    	this.image = image;    	
    	repaint();
    }
    
    public void setCoords(int x, int y) {
    	this.coords = new int[]{x,y};
    }
    
    public int[] getCoords() {
    	return coords;
    }
    
    public void removeImage() {
    	this.image = null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if(image!=null)        	
        	g.drawImage(image, 0, 0, null);          
    }
}