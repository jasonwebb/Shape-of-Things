/**
 * FreeWarp
 *
 * ©Patrick Saint-Denis
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 * 
 * @author		Patrick Saint-Denis
 * @modified	09/2011
 * @version		0.0.1
 * 
 */

package mappingtools;

import java.io.*;
import processing.core.*;

public class FreeWarp {

	// myParent is a reference to the parent sketch
	PApplet myParent;
	
	public final static String VERSION = "0.0.3";
	
	// globales
	int mousePosX;
	int mousePosY;
	int gridRes = 4;
	PVector[][] coords = new PVector[gridRes+1][gridRes+1];
	int selectedVector[][] = new int[gridRes+1][gridRes+1];
	int mouseDeltaX = 0; 
	int mouseDeltaY = 0;
	int lastMousePosX = 0;
	int lastMousePosY = 0;
	int mouseON = 0;
	int spritesON = 1;
	int rad = 20;
	int lastKey = 0;
	int key;
	boolean keyBoardStatus, lastKeyBoardStatus, mouseStatus, lastMouseStatus;
	int parentWidth;
	int parentHeight;
	String ligne;
	
	/**
	 * 
	 * Constructor, called in the setup() 
	 * adds also a mouseWheelListener to the sketch
	 *
	 * @param theParent
	 */
	public FreeWarp(PApplet theParent) {
		myParent = theParent;
		parentWidth = theParent.width;
		parentHeight = theParent.height;
		theParent.addMouseWheelListener(new java.awt.event.MouseWheelListener() { 
		    public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) { 
		      mouseWheel(evt.getWheelRotation());
		  }}); 
		defaults();
		welcome();
	}
	
	/**
	 * freeWarp has one method (.render)
	 * renders both the sprite (keyboard "c") and the transformed image
	 * @param texture
	 * 
	 */
	public void render(PImage img) {

		myParent.noStroke();
		for(int i = 0; i < gridRes; i++) {
			for(int j = 0; j < gridRes; j++) {
				myParent.beginShape();
				myParent.texture(img);
				myParent.vertex(coords[i][j].x, coords[i][j].y, ((float)i/gridRes) * img.width,  ((float)j/gridRes) * img.height);
				myParent.vertex(coords[i+1][j].x, coords[i+1][j].y, (((float)i+1)/gridRes) * img.width,  ((float)j/gridRes) * img.height);
				myParent.vertex(coords[i+1][j+1].x, coords[i+1][j+1].y, (((float)i+1)/gridRes) * img.width,  (((float)j+1)/gridRes) * img.height);
				myParent.vertex(coords[i][j+1].x, coords[i][j+1].y, ((float)i/gridRes) * img.width,  (((float)j+1)/gridRes) * img.height);
				myParent.endShape();
		    }
		 }
		sprites();
	}
	/**
	 * Sets the position of the control points
	 * @param vectorArray
	 * 
	 */
	public void controlSprites(PVector[][] vectorArray) {
		for(int i = 0; i<gridRes; i++) {
			for(int j = 0; j<gridRes; j++) {
				coords[i][j].x = vectorArray[i][j].x;
				coords[i][j].y = vectorArray[i][j].y;
			}
		}
	}
	/**
	 * turns control points on and off
	 * @param show
	 * 
	 */
	public void showControls(Boolean show) {
		if(show) {
			spritesON = 1;
		} else {
			spritesON = 0;
		}
	}
	
	/**
	 * 
	 * handles keyboard events, called in .render
	 */
	private void getKeyboard() {
		keyBoardStatus = myParent.keyPressed;
		if(keyBoardStatus != lastKeyBoardStatus) {
			lastKeyBoardStatus = keyBoardStatus;
			if(keyBoardStatus) {
			key = myParent.key;
			switch(key) {
			
				case 99:
					switch(spritesON) {
					case 1:
						spritesON = 0;
						break;
					case 0:
						spritesON = 1;
						break;
					}
				break;
				case 100:
					defaults();
				break;
				case 112:
					loadPresets();
				break;
				case 115:
					savePresets();
				break;
				}
			}
		}
	}
	
	/**
	 * 
	 * draws sprites, called in .render
	 */
	private void sprites() {
		getMouse();
		getKeyboard();
		
		myParent.stroke(255,255,0);
		myParent.strokeWeight(5);
		
		if(spritesON == 1) {
			for(int i = 0; i <= gridRes; i++) {
				for(int j = 0; j <= gridRes; j++) {
					myParent.point(coords[i][j].x, coords[i][j].y);
				}
			}
			myParent.noStroke();
			myParent.fill(0, 255, 0, 100);
			myParent.ellipse(mousePosX, mousePosY, rad, rad);				
		}	
	}

	/**
	 * 
	 * sets the sprites to default positions (keyboard : "d")
	 */
	public void defaults() {
		float[] x_pos = {parentWidth * 0.2f, parentWidth * 0.8f, parentWidth * 0.8f, parentWidth * 0.2f};
		float[] y_pos = {parentHeight * 0.2f, parentHeight * 0.2f, parentHeight * 0.8f, parentHeight * 0.8f};
		
		for(int i = 0; i <= gridRes; i++) {
			for(int j = 0; j <= gridRes; j++) {
				float x =   (x_pos[0] + ((x_pos[3] - x_pos[0]) * ((float)j/gridRes))) +  
                        ((x_pos[1] - ((x_pos[1] - x_pos[2]) * ((float)j/gridRes))) - (x_pos[0] + ((x_pos[3] - x_pos[0]) * ((float)j/gridRes)))  ) * (float)i/gridRes;
  
				float y =   (y_pos[0] + ((y_pos[3] - y_pos[0]) * ((float)j/gridRes))) +  
                        ((y_pos[1] - ((y_pos[1] - y_pos[2]) * ((float)j/gridRes))) - (y_pos[0] + ((y_pos[3] - y_pos[0]) * ((float)j/gridRes)))  ) * (float)i/gridRes;
		        
				coords[i][j] = new PVector(x, y);
			}
		}
	}
	
	/**
	 * 
	 * saves the actual sprites positions to the presets.txt file in the sketch folder (keyboard : "s")
	 */
	public void savePresets() {
		PrintWriter presets;
		presets = myParent.createWriter("presets.txt");   
		for(int i = 0; i <= gridRes; i++) {
			for(int j = 0; j <= gridRes; j++) {
				presets.println(coords[i][j].x);
				presets.println(coords[i][j].y);
			}
		}
	    presets.flush(); // Writes the remaining data to the file
	    presets.close(); // Finishes the file
	  
	}
	
	/**
	 * 
	 * loads last saved sprites positions (keyboard : "p")
	 */
	public void loadPresets() {
		BufferedReader reader;
		int indexer = 0;
		
		reader = myParent.createReader("presets.txt"); 
	    float[] dataHolder = new float[((gridRes+1) * (gridRes+1))*2];
	    for(int i = 0; i < ((gridRes+1) * (gridRes+1))*2; i++) {
	      try {
	      ligne = reader.readLine();
	      } catch (IOException e) {
	      e.printStackTrace();
	      }
	     
	      String[] pieces = PApplet.split(ligne, "\\t");
	      dataHolder[i] = Float.valueOf(pieces[0].trim()).floatValue();
	      }
	    
	    for(int i = 0; i <= gridRes; i++) {
			for(int j = 0; j <= gridRes; j++) {
				coords[i][j] = new PVector(dataHolder[indexer * 2], dataHolder[(indexer * 2 ) + 1]);
				indexer++;
			}
	    }
	}
	
	
	/**
	 * 
	 * handles mouse events, called in .render
	 */
	private void getMouse() {
		mousePosX = myParent.mouseX;
		mousePosY = myParent.mouseY;
		
		if((mousePosX != lastMousePosX) || (mousePosY != lastMousePosY)) {
            mouseDeltaX = mousePosX - lastMousePosX;
            mouseDeltaY = mousePosY - lastMousePosY;
            lastMousePosX = mousePosX;
            lastMousePosY = mousePosY;
          } else {
            mouseDeltaX = 0;
            mouseDeltaY = 0;
          }
		if(spritesON == 1) {
			for(int i = 0; i <= gridRes; i++) {
				for(int j = 0; j <= gridRes; j++) {
					float x = coords[i][j].x;
					float y = coords[i][j].y;
					if(selectedVector[i][j] == 1) {
						coords[i][j] = new PVector(x + mouseDeltaX, y + mouseDeltaY);
					}
				}
			}
		}
		
		mouseStatus = myParent.mousePressed;
		if(mouseStatus != lastMouseStatus) {
			lastMouseStatus = mouseStatus;
			if(mouseStatus) {
				mouseON = 1;
				
				  if(spritesON == 1) {
					  selectVectors();
					  }
			} else {
				mouseON = 0;
				mouseDeltaX = 0;
		        mouseDeltaY = 0;
		        for(int i = 0; i <= gridRes; i++) {
					for(int j = 0; j <= gridRes; j++) {
						selectedVector[i][j] = 0;
					}
		        }
			}
		}
	}
	
	/**
	 * 
	 * handles mouse wheel events
	 */
	private void mouseWheel(int delta) {
		  if(delta > 0) {
		    rad = rad - 5 ;
		    rad = PApplet.constrain(rad, 10, PApplet.max(parentWidth, parentHeight));
		  }
		   if(delta < 0) {
		    rad = rad + 5 ;
		    rad = PApplet.constrain(rad, 10, PApplet.max(parentWidth, parentHeight));
		  }
		}
		
	/**
	 * selects which vectors (sprites) are selected
	 * 
	 */
	private void selectVectors() {
		  for(int i = 0; i <= gridRes; i++) {
			for(int j = 0; j <= gridRes; j++) {
				float distTest;
		        float x = coords[i][j].x;
		        float y = coords[i][j].y;
		        distTest = PApplet.sqrt(PApplet.pow((mousePosX - x), 2) +  PApplet.pow((mousePosY - y), 2));
		        if(distTest <= (rad/2)) {
		        	selectedVector[i][j] = 1;	                           
		        } else {
		        	selectedVector[i][j] = 0;
		        }
			}
		  }
	}
	
	/**
	 * return the version of the library.
	 * @return String
	 */
	public static String version() {
		return VERSION;
	}	
	        
    
	
	private void welcome() {
		System.out.println("mappingtools 0.0.3 by Patrick Saint-Denis http://www.patricksaintdenis.com");
	}

}

