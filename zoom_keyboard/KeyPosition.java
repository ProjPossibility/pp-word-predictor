/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package absolute;

public class KeyPosition {

		int xCoordTopLeft;
		int yCoordTopLeft;
		int xArrayPos;
		int yArrayPos; 
		int height;
		int width;
		int size;
		
		public KeyPosition(int xC, int yC, int xA, int yA, int w, int h, int s){
			
			xCoordTopLeft = xC;
			yCoordTopLeft = yC;
			xArrayPos = xA;
			yArrayPos = yA;
			height = h;
			width = w;
			size = s;
			
		}
		
		public KeyPosition(int xA, int yA, int w, int h){
			
			xArrayPos = xA;
			yArrayPos = yA;
			height = h;
			width = w;
		}
		
		public KeyPosition(int x, int y){
			
			xArrayPos = x;
			yArrayPos = y;
			
		}
		
                public void setXCoord(int xC){
                    xCoordTopLeft = xC;
                }
		public int getXCoord(){
			
			return xCoordTopLeft;
			
		}
		
                public void setYCoord(int yC){
                    yCoordTopLeft = yC;
                }
		public int getYCoord(){
			
			return yCoordTopLeft;
			
		}
		
		public int getXArrayPos(){
		
			return xArrayPos;
			
		}
		public int getYArrayPos(){
			
			return yArrayPos;
			
		}
		public int getHeight(){
			
			return height;
			
		}
		
		public int getWidth(){
			
			return width;
			
		}
	
}
