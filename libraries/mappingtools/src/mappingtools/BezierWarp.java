/**
 * BezierWarp
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

public class BezierWarp {

	
	
	// myParent is a reference to the parent sketch
	PApplet myParent;
	
	public final static String VERSION = "0.0.3";
	
	// globales
	int mousePosX;
	int mousePosY;
	int nbSprites = 4;
	int nbControlPoints = 8;
	float[] x_pos = {0, 0, 0, 0};
	float[] y_pos = {0, 0, 0, 0};
	float[] x_ctrl = {0, 0, 0, 0, 0, 0, 0, 0};
	float[] y_ctrl = {0, 0, 0, 0, 0, 0, 0, 0};
	int[] selectedSprite = new int[nbSprites];
	int[] selectedControlPoint = new int[nbControlPoints];
	int anchorControl = 1;
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
	int gridRes;
	
		
	/**
	 * 
	 * Constructor, called in the setup() 
	 *
	 * @param theParent
	 * @param gridres
	 */
	public BezierWarp(PApplet theParent,  int gridres) {
		myParent = theParent;
		parentWidth = theParent.width;
		parentHeight = theParent.height;
		gridRes = gridres;
		defaults();
		welcome();
	}
	
	/**
	 * BezierWarp has one method (.render)
	 * renders both the sprites (keyboard "c") and the transformed image
	 * @param texture
	 * 
	 */
	public void render(PImage img) {
		PVector[][] coords = new PVector[gridRes+1][gridRes+1];
		myParent.stroke(0);
		myParent.strokeWeight(2);
		      
		for(int i = 0; i <= gridRes; i++) {
			for(int j = 0; j <= gridRes; j++) {
				float start_x = myParent.bezierPoint(x_pos[0], x_ctrl[0], x_ctrl[7], x_pos[3], (float)j/gridRes);
		        float end_x = myParent.bezierPoint(x_pos[1], x_ctrl[3], x_ctrl[4], x_pos[2], (float)j/gridRes);
		        
		        float start_y = myParent.bezierPoint(y_pos[0], y_ctrl[0], y_ctrl[7], y_pos[3], (float)j/gridRes);
		        float end_y = myParent.bezierPoint(y_pos[1], y_ctrl[3], y_ctrl[4], y_pos[2], (float)j/gridRes);
		        
		        float x = myParent.bezierPoint(start_x, ((x_ctrl[1] - x_ctrl[6]) * (1.0f - (float)j/gridRes)) + x_ctrl[6], ((x_ctrl[2] - x_ctrl[5]) * (1.0f - (float)j/gridRes)) + x_ctrl[5], end_x, (float)i/gridRes);
		        float y = myParent.bezierPoint(start_y, ((y_ctrl[1] - y_ctrl[6]) * (1.0f - (float)j/gridRes)) + y_ctrl[6], ((y_ctrl[2] - y_ctrl[5]) * (1.0f - (float)j/gridRes)) + y_ctrl[5], end_y, (float)i/gridRes);
		        //point(x, y);
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
	 * @param vectorArrayCorners
	 * @param vectorArrayAnchors
	 * 
	 */
	public void controlSprites(PVector[] vectorArrayCorners, PVector[] vectorArrayAnchors) {
		for(int i = 0; i<nbSprites; i++) {
			x_pos[i] = vectorArrayCorners[i].x;
			y_pos[i] = vectorArrayCorners[i].y;
		}
		for(int i = 0; i<nbControlPoints; i++) {
			x_ctrl[i] = vectorArrayAnchors[i].x;
			y_ctrl[i] = vectorArrayAnchors[i].y;
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
				case 65535:
					anchorControl = 0;
					
				break;
				case 115:
					savePresets();
				break;
				}
			} else {
			anchorControl = 1;
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
		
		for(int i = 0; i < nbControlPoints; i++) {		    
		    if(selectedControlPoint[i] == 1) {
		    	if(mouseON == 1) {
				 x_ctrl[i] = mousePosX; 
				 y_ctrl[i] = mousePosY; 
		    	}
		    	
		    	 myParent.fill(0, 255, 0, 120);
		    	 myParent.strokeWeight(1);
			     myParent.stroke(255, 255, 0);
			     myParent.ellipse(x_ctrl[i], y_ctrl[i], rad/2, rad/2);
			     myParent.noFill();
			     if((i% 2) == 0) {
			    	myParent.bezier(x_pos[i/2], y_pos[i/2], x_ctrl[(i+1) % nbControlPoints], y_ctrl[(i+1) % nbControlPoints], x_ctrl[(i+2) % nbControlPoints], y_ctrl[(i+2) % nbControlPoints], x_pos[((i/2)+1) % nbSprites], y_pos[((i/2)+1) % nbSprites]); 
			     }
			     myParent.line(x_pos[i/2], y_pos[i/2], x_ctrl[i], y_ctrl[i]);
		    } else {
			     myParent.noFill();
			     myParent.strokeWeight(1);
			     myParent.stroke(255, 255, 0);
			     myParent.ellipse(x_ctrl[i], y_ctrl[i], rad/2, rad/2);
			     myParent.noFill();
			     if((i% 2) == 0) {
			    	myParent.bezier(x_pos[i/2], y_pos[i/2], x_ctrl[(i+1) % nbControlPoints], y_ctrl[(i+1) % nbControlPoints], x_ctrl[(i+2) % nbControlPoints], y_ctrl[(i+2) % nbControlPoints], x_pos[((i/2)+1) % nbSprites], y_pos[((i/2)+1) % nbSprites]); 
			     }
			     myParent.line(x_pos[i/2], y_pos[i/2], x_ctrl[i], y_ctrl[i]);
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
	  
	    x_ctrl[0] = parentWidth * 0.2f;
	    x_ctrl[1] = parentWidth * 0.35f;
	    x_ctrl[2] = parentWidth * 0.65f;
	    x_ctrl[3] = parentWidth * 0.8f;
	    x_ctrl[4] = parentWidth * 0.8f;
	    x_ctrl[5] = parentWidth * 0.65f;
	    x_ctrl[6] = parentWidth * 0.35f;
	    x_ctrl[7] = parentWidth * 0.2f;
	  
	    y_ctrl[0] = parentHeight * 0.35f;
	    y_ctrl[1] = parentHeight * 0.2f;
	    y_ctrl[2] = parentHeight * 0.2f;
	    y_ctrl[3] = parentHeight * 0.35f;
	    y_ctrl[4] = parentHeight * 0.65f;
	    y_ctrl[5] = parentHeight * 0.8f;
	    y_ctrl[6] = parentHeight * 0.8f;
	    y_ctrl[7] = parentHeight * 0.65f;
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
	     for(int g = 0; g < nbControlPoints; g++) {
	      presets.println(x_ctrl[g]);
	      presets.println(y_ctrl[g]);
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
	    float[] dataHolder = new float[(nbControlPoints+nbSprites) *2];
	    for(int i = 0; i < (nbControlPoints+nbSprites) *2; i++) {
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
	      for(int l = 0; l < nbControlPoints; l++) {
	        x_ctrl[l] = dataHolder[(l * 2) +  (nbSprites*2)];
	        y_ctrl[l] = dataHolder[((l * 2) + 1) +  (nbSprites*2)];
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
				   for(int i = 0; i < nbControlPoints; i++) {
				     selectedControlPoint[i] = 0;
				  }
				  if(spritesON == 1) {
				  for(int i = 0; i < nbSprites; i++) {
				    if(selectedSprite[i] == 0 && anchorControl == 1) {
				  
				      if((mousePosX > x_pos[i] - (rad/2)) && (mousePosX < x_pos[i] + (rad/2)) && (mousePosY >  y_pos[i] - (rad/2)) && (mousePosY <  y_pos[i] + (rad/2))) {
				        selectedSprite[i] = 1;
				      } else {
				        selectedSprite[i] = 0;
				      }
				    
				    }
				  
				  }
				for(int i = 0; i < nbControlPoints; i++) {
				    if(selectedControlPoint[i] == 0 && anchorControl == 0) {
				  
				      if((mousePosX > x_ctrl[i] - (rad/4)) && (mousePosX < x_ctrl[i] + (rad/4)) && (mousePosY >  y_ctrl[i] - (rad/4)) && (mousePosY <  y_ctrl[i] + (rad/4))) {
				        selectedControlPoint[i] = 1;
				      } else {
				        selectedControlPoint[i] = 0;
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

