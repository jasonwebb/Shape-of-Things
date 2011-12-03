/**
 * ClusterBezierWarp
 *
 *	The edge blend still needs to be improved. But you can have decent results with this.
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


public class ClusterBezierWarp {

	
	
	// myParent is a reference to the parent sketch
	PApplet myParent;
	
	public final static String VERSION = "0.0.3";
	
	// globales
	float blendLenght = 1;
	int gridRes=10;
	PVector[][] coords = new PVector[gridRes+1][gridRes+1];
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
	int rad = 40;
	float incrementor = 1.0f;
	int lastKey = 0;
	int key;
	boolean keyBoardStatus, lastKeyBoardStatus, mouseStatus, lastMouseStatus;
	int parentWidth;
	int parentHeight;
	String ligne;
	int leftBlendTiles;
	int rightBlendTiles;
	int upBlendTiles;
	int downBlendTiles;
	float leftblendLenght;
	float rightblendLenght;
	float upblendLenght;
	float downblendLenght;
	float leftmidRamp;
	float rightmidRamp;
	float upmidRamp;
	float downmidRamp;
	


		
	/**
	 * 
	 * Constructor, called in the setup() 
	 * also adds a mouseWheelListener to the sketch
	 *
	 * @param theParent
	 * @param gridres
	 * @param nbBlendTilesLeft
	 * @param nbBlendTilesRight
	 * @param nbBlendTilesUp
	 * @param nbBlendTilesDown
	 * 
	 */
	public ClusterBezierWarp(PApplet theParent, int nbBlendTilesLeft, int nbBlendTilesRight, int nbBlendTilesUp, int nbBlendTilesDown) {
		myParent = theParent;
		parentWidth = theParent.width * 2;
		parentHeight = theParent.height * 2;
		
		leftBlendTiles = nbBlendTilesLeft;
		rightBlendTiles = nbBlendTilesRight;
		upBlendTiles = nbBlendTilesUp;
		downBlendTiles = nbBlendTilesDown;
		
		theParent.addMouseWheelListener(new java.awt.event.MouseWheelListener() { 
		    public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) { 
		      mouseWheel(evt.getWheelRotation());
		  }}); 
		
		
		
		defaults();
		welcome();
	}
	
	/**
	 * ClusterBezierWarp has 4 methods (.sprites, .renderMask, .renderEdges and .render)
	 * renders the mask
	 * 
	 */
	public void renderMask() {
		
		myParent.noStroke();
		
		// draw mask
		for(int i = 0; i < gridRes; i++) {
			for(int j = 0; j < gridRes; j++) {
				myParent.beginShape();
				myParent.fill(255);
				myParent.vertex(coords[i][j].x,coords[i][j].y);
				myParent.vertex(coords[i+1][j].x,coords[i+1][j].y);
				myParent.vertex(coords[i+1][j+1].x,coords[i+1][j+1].y);
				myParent.vertex(coords[i][j+1].x,coords[i][j+1].y);
				myParent.endShape(); 
			}
		}
	}

	/**
	 * 
	 * edge blending masks
	 * @param floorValue
	 */
	public void renderEdges() {
		
		myParent.noStroke();
		
		// draw blend mask
		if(leftBlendTiles > 0) {
			for(int j = 0; j < gridRes; j++) {
				myParent.beginShape();
				myParent.fill(255);
				myParent.vertex(coords[leftBlendTiles][j].x,coords[leftBlendTiles][j].y);
				myParent.vertex(coords[leftBlendTiles][j+1].x,coords[leftBlendTiles][j+1].y);
				myParent.fill(leftmidRamp);
				myParent.vertex(coords[leftBlendTiles][j].x - leftblendLenght,coords[leftBlendTiles][j].y);
				myParent.vertex(coords[leftBlendTiles][j+1].x - leftblendLenght,coords[leftBlendTiles][j+1].y);
				myParent.endShape();
			}
			for(int j = 0; j < gridRes; j++) {
				myParent.beginShape();
				myParent.fill(leftmidRamp);
				myParent.vertex(coords[leftBlendTiles][j].x - leftblendLenght,coords[leftBlendTiles][j].y);
				myParent.vertex(coords[leftBlendTiles][j+1].x - leftblendLenght,coords[leftBlendTiles][j+1].y);
				myParent.fill(0);
				myParent.vertex(coords[0][j+1].x,coords[0][j+1].y);
				myParent.vertex(coords[0][j].x,coords[0][j].y);
				myParent.endShape();
			}
		}
		if(rightBlendTiles > 0) {
			for(int j = 0; j < gridRes; j++) {
				myParent.beginShape();
				myParent.fill(255);
				myParent.vertex(coords[gridRes-rightBlendTiles][j].x, coords[gridRes-rightBlendTiles][j].y);
				myParent.vertex(coords[gridRes-rightBlendTiles][j+1].x, coords[gridRes-rightBlendTiles][j+1].y);
				myParent.fill(rightmidRamp);
				myParent.vertex(coords[gridRes-rightBlendTiles][j+1].x + rightblendLenght,coords[gridRes][j+1].y);
				myParent.vertex(coords[gridRes-rightBlendTiles][j].x + rightblendLenght,coords[gridRes][j].y);
				myParent.endShape();				
			}
			for(int j = 0; j < gridRes; j++) {
				myParent.beginShape();
				myParent.fill(rightmidRamp);
				myParent.vertex(coords[gridRes-rightBlendTiles][j].x + rightblendLenght,coords[gridRes-rightBlendTiles][j].y);
				myParent.vertex(coords[gridRes-rightBlendTiles][j+1].x + rightblendLenght,coords[gridRes-rightBlendTiles][j+1].y);
				myParent.fill(0);
				myParent.vertex(coords[gridRes][j+1].x,coords[gridRes][j+1].y);
				myParent.vertex(coords[gridRes][j].x,coords[gridRes][j].y);
				myParent.endShape();				
			}
		}
		if(upBlendTiles > 0) {
				for(int i = 0; i < gridRes; i++) {
					myParent.beginShape();
					myParent.fill(255);
					myParent.vertex(coords[i][upBlendTiles].x,coords[i][upBlendTiles].y);
					myParent.vertex(coords[i+1][upBlendTiles].x,coords[i+1][upBlendTiles].y);
					myParent.fill(upmidRamp);
					myParent.vertex(coords[i][upBlendTiles].x,coords[i][upBlendTiles].y - upblendLenght);
					myParent.vertex(coords[i+1][upBlendTiles].x,coords[i+1][upBlendTiles].y - upblendLenght);
					myParent.endShape();
				}
				for(int i = 0; i < gridRes; i++) {
					myParent.beginShape();
					myParent.fill(upmidRamp);
					myParent.vertex(coords[i][upBlendTiles].x,coords[i][upBlendTiles].y - upblendLenght);
					myParent.vertex(coords[i+1][upBlendTiles].x,coords[i+1][upBlendTiles].y - upblendLenght);
					myParent.fill(0);
					myParent.vertex(coords[i+1][0].x,coords[i+1][0].y);
					myParent.vertex(coords[i][0].x,coords[i][0].y);
					myParent.endShape();
				}
			
		}
		if(downBlendTiles > 0) {
			for(int i = 0; i < gridRes; i++) {
				myParent.beginShape();
				myParent.fill(255);
				myParent.vertex(coords[i][gridRes-downBlendTiles].x,coords[i][gridRes-downBlendTiles].y);
				myParent.vertex(coords[i+1][gridRes-downBlendTiles].x,coords[i+1][gridRes-downBlendTiles].y);
				myParent.fill(downmidRamp);
				myParent.vertex(coords[i][gridRes-downBlendTiles].x,coords[i][gridRes-downBlendTiles].y + downblendLenght);
				myParent.vertex(coords[i+1][gridRes-downBlendTiles].x,coords[i+1][gridRes-downBlendTiles].y + downblendLenght);
				myParent.endShape();
			}
			for(int i = 0; i < gridRes; i++) {
				myParent.beginShape();
				myParent.fill(downmidRamp);
				myParent.vertex(coords[i][gridRes-downBlendTiles].x,coords[i][gridRes-downBlendTiles].y + downblendLenght);
				myParent.vertex(coords[i+1][gridRes-downBlendTiles].x,coords[i+1][gridRes-downBlendTiles].y + downblendLenght);
				myParent.fill(0);
				myParent.vertex(coords[i+1][gridRes].x,coords[i+1][gridRes].y);
				myParent.vertex(coords[i][gridRes].x,coords[i][gridRes].y);
				myParent.endShape();
		}
		}
	}
	
	/**
	 * 
	 * renders the transformed image
	 * @param texture
	 * @param gridRes
	 * @param textureOffsetX
	 * @param textureOffsetY
	 * @param texWidth
	 * @param texHeight
	 * 
	 */
	public void render(PImage img, float textureOffsetX, float textureOffsetY, float texWidth, float texHeight) {
		
	
		
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
		        coords[i][j] = new PVector(x, y);
		        }
		      }
		
		 
		  
		myParent.noStroke();
		for(int i = 0; i < gridRes; i++) {
			for(int j = 0; j < gridRes; j++) {
				myParent.beginShape();
				myParent.texture(img);
				myParent.vertex(coords[i][j].x, coords[i][j].y, ((float)i/gridRes) * texWidth + textureOffsetX,  ((float)j/gridRes) * texHeight + textureOffsetY);
				myParent.vertex(coords[i+1][j].x, coords[i+1][j].y, (((float)i+1)/gridRes) * texWidth + textureOffsetX,  ((float)j/gridRes) * texHeight + textureOffsetY);
				myParent.vertex(coords[i+1][j+1].x, coords[i+1][j+1].y, (((float)i+1)/gridRes) * texWidth + textureOffsetX,  (((float)j+1)/gridRes) * texHeight + textureOffsetY);
				myParent.vertex(coords[i][j+1].x, coords[i][j+1].y, ((float)i/gridRes) * texWidth + textureOffsetX,  (((float)j+1)/gridRes) * texHeight + textureOffsetY);
				myParent.endShape();
		    }
		 }
		
	
		
		
		
	
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
	public void sprites() {
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
		
		myParent.stroke(255, 255, 0);
		myParent.strokeWeight(2);
		
		for(int i = 0; i < gridRes; i++) {
			for(int j = 0; j < gridRes; j++) {
				myParent.point(coords[i][j].x,coords[i][j].y );
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
	    leftblendLenght = 0;
		rightblendLenght = 0;
		upblendLenght = 0;
		downblendLenght = 0;
		leftmidRamp = 0;
		rightmidRamp = 0;
		upmidRamp = 0;
		downmidRamp = 0;
	}
	
	/**
	 * 
	 * saves the actual sprites positions and mask parameters to the presets.txt file in the sketch folder (keyboard : "s")
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
	    presets.println(leftblendLenght);
	    presets.println(rightblendLenght);
	    presets.println(upblendLenght);
	    presets.println(downblendLenght);
	    presets.println(leftmidRamp);
	    presets.println(rightmidRamp);
	    presets.println(upmidRamp);
	    presets.println(downmidRamp);
	    presets.flush(); // Writes the remaining data to the file
	    presets.close(); // Finishes the file
	  
	}
	
	/**
	 * 
	 * loads last saved sprites positions and mask parameters (keyboard : "p")
	 */
	public void loadPresets() {
		BufferedReader reader;
		
		reader = myParent.createReader("presets.txt"); 
	    float[] dataHolder = new float[((nbControlPoints+nbSprites) *2) + 8];
	    for(int i = 0; i < ((nbControlPoints+nbSprites) *2) + 8; i++) {
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
	 
	      leftblendLenght = dataHolder[24];
	      rightblendLenght = dataHolder[25];
	      upblendLenght = dataHolder[26];
	      downblendLenght = dataHolder[27];
	      leftmidRamp = dataHolder[28];
	      rightmidRamp = dataHolder[29];
	      upmidRamp = dataHolder[30];
	      downmidRamp = dataHolder[31];
	  
	}
	
	
	/**
	 * 
	 * handles mouse events, called in .render
	 */
	private void getMouse() {
		mousePosX = myParent.mouseX * 2;
		mousePosY = myParent.mouseY * 2;
		
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
	 * 
	 * handles mouse wheel events for edge blending border and luminosity
	 * @param delta
	 *  
	 */
	private void mouseWheel(int delta) {
			 if(delta == -1) {
				 if(leftBlendTiles > 0) {
					 if(mousePosX >= coords[0][0].x && mousePosX <= coords[leftBlendTiles][0].x && mousePosY >= coords[0][0].y && mousePosY <= coords[leftBlendTiles][gridRes].y) {
						 if(anchorControl == 0) {
							 leftblendLenght = leftblendLenght + 5 ;
							 leftblendLenght = PApplet.constrain(leftblendLenght, 1, PApplet.abs(coords[leftBlendTiles][0].x - coords[0][0].x));
						 }
						 if(anchorControl == 1) {
							 leftmidRamp = leftmidRamp + 5 ;
							 leftmidRamp = PApplet.constrain(leftmidRamp, 0, 255);
						 }
						 }
				 }
				 if(rightBlendTiles > 0) {
					 if(mousePosX >= coords[gridRes-rightBlendTiles][0].x && mousePosX <= coords[gridRes][0].x && mousePosY >= coords[gridRes-rightBlendTiles][0].y && mousePosY <= coords[gridRes-rightBlendTiles][gridRes].y) {
						 if(anchorControl == 0) {
							 rightblendLenght = rightblendLenght + 5 ;
							 rightblendLenght = PApplet.constrain(rightblendLenght, 1, PApplet.abs(coords[gridRes-rightBlendTiles][0].x - coords[gridRes][0].x));
						 }
						 if(anchorControl == 1) {
							 rightmidRamp = rightmidRamp + 5 ;
							 rightmidRamp = PApplet.constrain(rightmidRamp, 0, 255);
						 }
						 }
				 }
				 if(upBlendTiles > 0) {
					 if(mousePosX >= coords[0][0].x && mousePosX <= coords[gridRes][0].x && mousePosY >= coords[0][0].y && mousePosY <= coords[0][upBlendTiles].y) {
						 if(anchorControl == 0) {
							 upblendLenght = upblendLenght + 5 ;
							 upblendLenght = PApplet.constrain(upblendLenght, 1, PApplet.abs(coords[0][upBlendTiles].y - coords[0][0].y));
						 }
						 if(anchorControl == 1) {
							 upmidRamp = upmidRamp + 5 ;
							 upmidRamp = PApplet.constrain(upmidRamp, 0, 255);
						 }
						 }
				 }
				 if(downBlendTiles > 0) {
					 if(mousePosX >= coords[0][gridRes-downBlendTiles].x && mousePosX <= coords[gridRes][gridRes-downBlendTiles].x && mousePosY >= coords[0][gridRes-downBlendTiles].y && mousePosY <= coords[0][gridRes].y) {
						 if(anchorControl == 0) {
							 downblendLenght = downblendLenght + 5 ;
							 downblendLenght = PApplet.constrain(downblendLenght, 1, PApplet.abs(coords[0][gridRes-downBlendTiles].y - coords[0][gridRes].y));
						 }
						 if(anchorControl == 1) {
							 downmidRamp = downmidRamp + 5 ;
							 downmidRamp = PApplet.constrain(downmidRamp, 0, 255);
						 }
						 }
				 }
			 }
			 if(delta == 1) {
				 if(leftBlendTiles > 0) {
					 if(mousePosX >= coords[0][0].x && mousePosX <= coords[leftBlendTiles][0].x && mousePosY >= coords[0][0].y && mousePosY <= coords[leftBlendTiles][gridRes].y) {
						 if(anchorControl == 0) {
							 leftblendLenght = leftblendLenght - 5 ;
							 leftblendLenght = PApplet.constrain(leftblendLenght, 1, PApplet.abs(coords[leftBlendTiles][0].x - coords[0][0].x));
						 }
						 if(anchorControl == 1) {
							 leftmidRamp = leftmidRamp - 5 ;
							 leftmidRamp = PApplet.constrain(leftmidRamp, 0, 255);
						 }
						 }
				 }
				 if(rightBlendTiles > 0) {
					 if(mousePosX >= coords[gridRes-rightBlendTiles][0].x && mousePosX <= coords[gridRes][0].x && mousePosY >= coords[gridRes-rightBlendTiles][0].y && mousePosY <= coords[gridRes-rightBlendTiles][gridRes].y) {
						 if(anchorControl == 0) {
							 rightblendLenght = rightblendLenght - 5 ;
							 rightblendLenght = PApplet.constrain(rightblendLenght, 1, PApplet.abs(coords[gridRes-rightBlendTiles][0].x - coords[gridRes][0].x));
						 }
						 if(anchorControl == 1) {
							 rightmidRamp = rightmidRamp - 5 ;
							 rightmidRamp = PApplet.constrain(rightmidRamp, 0, 255);
						 }
						 }
				 }
				 if(upBlendTiles > 0) {
					 if(mousePosX >= coords[0][0].x && mousePosX <= coords[gridRes][0].x && mousePosY >= coords[0][0].y && mousePosY <= coords[0][upBlendTiles].y) {
						 if(anchorControl == 0) {
							 upblendLenght = upblendLenght - 5 ;
							 upblendLenght = PApplet.constrain(upblendLenght, 1, PApplet.abs(coords[0][upBlendTiles].y - coords[0][0].y));
						 }
						 if(anchorControl == 1) {
							 upmidRamp = upmidRamp - 5 ;
							 upmidRamp = PApplet.constrain(upmidRamp, 0, 255);
						 }
						 }
				 }
				 if(downBlendTiles > 0) {
					 if(mousePosX >= coords[0][gridRes-downBlendTiles].x && mousePosX <= coords[gridRes][gridRes-downBlendTiles].x && mousePosY >= coords[0][gridRes-downBlendTiles].y && mousePosY <= coords[0][gridRes].y) {
						 if(anchorControl == 0) {
							 downblendLenght = downblendLenght - 5 ;
							 downblendLenght = PApplet.constrain(downblendLenght, 1, PApplet.abs(coords[0][gridRes-downBlendTiles].y - coords[0][gridRes].y));
						 }
						 if(anchorControl == 1) {
							 downmidRamp = downmidRamp - 5 ;
							 downmidRamp = PApplet.constrain(downmidRamp, 0, 255);
						 }
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

