/**
 * QuadWarp
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

public class QuadWarp {

	
	
	// myParent is a reference to the parent sketch
	PApplet myParent;
	
	public final static String VERSION = "0.0.3";
	
	// globales
	int mousePosX;
	int mousePosY;
	int gridRes;
	int nbSprites = 4;
	float[] x_pos = {0, 0, 0, 0};
	float[] y_pos = {0, 0, 0, 0};
	int[] selectedSprite = new int[nbSprites];
	int mouseON = 0;
	int spritesON = 1;
	int rad = 20;
	float incrementor = 1.0f;
	int lastKey = 0;
	int key;
	boolean keyBoardStatus, lastKeyBoardStatus, mouseStatus, lastMouseStatus;
	int parentWidth;
	int parentHeight;
	String ligne;
	
		
	/**
	 * 
	 * Constructor, called in the setup() 
	 *
	 * @param theParent
	 * @param gridres
	 */
	public QuadWarp(PApplet theParent,  int gridres) {
		myParent = theParent;
		parentWidth = theParent.width;
		parentHeight = theParent.height;
		gridRes = gridres;
		defaults();
		welcome();
	}
	
	/**
	 * QuadWarp has one method (.render)
	 * renders both the sprites (keyboard "c") and the transformed image
	 * @param texture
	 * @param gridRes
	 */
	public void render(PImage img) {
		PVector[][] coords = new PVector[gridRes+1][gridRes+1];
		myParent.stroke(0);
		myParent.strokeWeight(2);
		      
		for(int i = 0; i <= gridRes; i++) {
			for(int j = 0; j <= gridRes; j++) {
				float x =   (x_pos[0] + ((x_pos[3] - x_pos[0]) * ((float)j/gridRes))) +  
                                          ((x_pos[1] - ((x_pos[1] - x_pos[2]) * ((float)j/gridRes))) - (x_pos[0] + ((x_pos[3] - x_pos[0]) * ((float)j/gridRes)))  ) * (float)i/gridRes;
		            
		        float y =   (y_pos[0] + ((y_pos[3] - y_pos[0]) * ((float)j/gridRes))) +  
                                          ((y_pos[1] - ((y_pos[1] - y_pos[2]) * ((float)j/gridRes))) - (y_pos[0] + ((y_pos[3] - y_pos[0]) * ((float)j/gridRes)))  ) * (float)i/gridRes;
		        coords[i][j] = new PVector(x, y);
		       	}
		} 
		
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
	public void controlSprites(PVector[] vectorArray) {
		for(int i = 0; i<nbSprites; i++) {
			x_pos[i] = vectorArray[i].x;
			y_pos[i] = vectorArray[i].y;
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
			
		if(spritesON == 1) {
		
		for(int i = 0; i < nbSprites; i++) {
		    if(selectedSprite[i] == 1) {
		    	if(mouseON == 1) {
		    		x_pos[i] = mousePosX; 
		    		y_pos[i] = mousePosY; 
		    	}
				
		        myParent.fill(0, 255, 0, 120);
		        myParent.strokeWeight(1);
		        myParent.stroke(255, 0, 0);
		        myParent.ellipse(x_pos[i], y_pos[i], rad, rad);
		        myParent.line(x_pos[i], y_pos[i] - (rad/2) - 5, x_pos[i], y_pos[i] + (rad/2) + 5);
		        myParent.line(x_pos[i] - (rad/2) - 5, y_pos[i], x_pos[i] + (rad/2) + 5, y_pos[i]);
		    } else {
			    myParent.noFill();
			    myParent.strokeWeight(1);
			    myParent.stroke(255, 0, 0);
			    myParent.ellipse(x_pos[i], y_pos[i], rad, rad);
			    myParent.line(x_pos[i], y_pos[i] - (rad/2) - 5, x_pos[i], y_pos[i] + (rad/2) + 5);
			    myParent.line(x_pos[i] - (rad/2) - 5, y_pos[i], x_pos[i] + (rad/2) + 5, y_pos[i]);
			}		      
		}		
		}	 
	}

	/**
	 * 
	 * sets the sprites to default positions (keyboard : "d")
	 */
	public void defaults() {
		x_pos[0] = parentWidth * 0.2f;
	    x_pos[1] = parentWidth * 0.8f;
	    x_pos[2] = parentWidth * 0.8f;
	    x_pos[3] = parentWidth * 0.2f;  
	  
	    y_pos[0] = parentHeight * 0.2f;
	    y_pos[1] = parentHeight * 0.2f;
	    y_pos[2] = parentHeight * 0.8f;
	    y_pos[3] = parentHeight * 0.8f;
	}
	
	/**
	 * 
	 * saves the actual sprites positions to the presets.txt file in the sketch folder (keyboard : "s")
	 */
	public void savePresets() {
		PrintWriter presets;
		presets = myParent.createWriter("presets.txt");   
	    for(int i = 0; i < nbSprites; i++) {
	      presets.println(x_pos[i]);
	      presets.println(y_pos[i]);
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
		
		reader = myParent.createReader("presets.txt"); 
	    float[] dataHolder = new float[nbSprites *2];
	    for(int i = 0; i < nbSprites *2; i++) {
	      try {
	      ligne = reader.readLine();
	      } catch (IOException e) {
	      e.printStackTrace();
	      }
	     
	      String[] pieces = PApplet.split(ligne, "\\t");
	      dataHolder[i] = Float.valueOf(pieces[0].trim()).floatValue();
	      }
	     
	      for(int j = 0; j < nbSprites; j++) {
	        x_pos[j] = dataHolder[j * 2];
	        y_pos[j] = dataHolder[(j * 2) + 1];
	      }  
	}
	
	
	/**
	 * 
	 * handles mouse events, called in .render
	 */
	private void getMouse() {
		mousePosX = myParent.mouseX;
		mousePosY = myParent.mouseY;
		
		mouseStatus = myParent.mousePressed;
		if(mouseStatus != lastMouseStatus) {
			lastMouseStatus = mouseStatus;
			if(mouseStatus) {
				mouseON = 1;
				  for(int i = 0; i < nbSprites; i++) {
				     selectedSprite[i] = 0;
				  }
				  
				  if(spritesON == 1) {
				  for(int i = 0; i < nbSprites; i++) {
				    if(selectedSprite[i] == 0) {
				  
				      if((mousePosX > x_pos[i] - (rad/2)) && (mousePosX < x_pos[i] + (rad/2)) && (mousePosY >  y_pos[i] - (rad/2)) && (mousePosY <  y_pos[i] + (rad/2))) {
				        selectedSprite[i] = 1;
				      } else {
				        selectedSprite[i] = 0;
				      }
				    
				    }
				  
				  }
				
				  
				  }
				
			} else {
				mouseON = 0;
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

