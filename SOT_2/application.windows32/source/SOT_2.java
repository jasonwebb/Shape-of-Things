import processing.core.*; 
import processing.xml.*; 

import processing.opengl.*; 
import mappingtools.*; 
import codeanticode.glgraphics.*; 
import codeanticode.gsvideo.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class SOT_2 extends PApplet {

// Libraries





// Objects
QuadWarp qw;
GSMovie mov;
GLTexture tex;

// Images
String imageFilenames[] = {"Bedroom-Modified.png","Bedroom.png","Blank-Frame.jpg","Bedroom-Modified.png","Blank-Frame.jpg","Bedroom-Modified.png","Video_Frame1.png"};
ArrayList<PImage> images = new ArrayList();
PImage currentImage, nextImage;
int currentImageIndex = 0, nextImageIndex = 0;

// Flags
boolean SHOW_CURSOR = false;
boolean FADE_OUT = false;
boolean FADE_IN = true;
boolean SHOW_VIDEO = false;

// Misc vars
int fadeCounter = 0;

public void setup() {
  // Frame setup
  size(screen.width, screen.height, GLConstants.GLGRAPHICS);
  smooth();
  noCursor();
  frameRate(90);
  
  // Movie setup
  mov = new GSMovie(this, "SOT_2_scene.wmv");
  tex = new GLTexture(this);
  mov.setPixelDest(tex);
  tex.setPixelBufferSize(10);
  tex.delPixelsWhenBufferFull(false);
  
  // Load all images into ArrayList
  for(int i=0; i<imageFilenames.length; i++) {
    PImage tImage = loadImage(imageFilenames[i]);
    images.add(tImage);
  }
  
  // Set current image to first image
  currentImage = (PImage) images.get( currentImageIndex );
  
  // Setup QuadWarp object
  qw = new QuadWarp(this, 10);
  qw.showControls(false);
  qw.loadPresets();
}

public void draw() {

  if(!SHOW_VIDEO)
    background(0);  

  // Initiate a fade out (fade to black)
  if(FADE_OUT) {
    // Gradually fade to black
    tint(255,255-fadeCounter);
    fadeCounter += 5; 
    
    // Upon fade finish
    if(fadeCounter>=255) {
      // -1 = move on to video
      if(currentImageIndex == -1) {
        println("playing video");
        SHOW_VIDEO = true;
        mov.play();
      
      // Otherwise, move on to next still image
      } else {
        currentImage = (PImage) images.get(currentImageIndex);
      }
      
      FADE_OUT = false;  // Stop fade out process
      FADE_IN = true;    // Request fade in to next image
      fadeCounter = 0;   // Reset fade counter
    }
  }
  
  // Initiate a fade in to next image
  if(FADE_IN) {
    // Gradually fade in
    tint(255,fadeCounter);
    fadeCounter += 5;

    // Upon fade finish    
    if(fadeCounter >= 255) {
      FADE_IN = false;  // Stop the fade
      fadeCounter = 0;  // Reset the fade counter
    }
  }
  
  // Video playback initiated
  if(SHOW_VIDEO && !FADE_OUT) {
  
    if(mov.available()) {
      mov.read();
      if(tex.putPixelsIntoTexture())
        currentImage = tex; 

      background(0);     
      qw.render(currentImage);
    }
    
    if( !mov.isPlaying() ) {
      println("not plyaing");
    }
  } else {
    qw.render(currentImage);
  }
    
}

public void keyPressed() {
  if(key == CODED) {
    if(keyCode == LEFT) {
      if(currentImageIndex == -1)
        currentImageIndex = images.size()-1;
      else
        currentImageIndex--;
        
      FADE_OUT = true;
    } else if(keyCode == RIGHT) {
      if(currentImageIndex == images.size()-1)
        currentImageIndex = -1;
      else
        currentImageIndex++;
              
      FADE_OUT = true;
    }
   
    fadeCounter = 0; 
  }
  
  else if(key == 'C' || key == 'c') {
    SHOW_CURSOR = !SHOW_CURSOR;
    if(SHOW_CURSOR) {
      cursor();
      qw.showControls(false);
    }else {
      noCursor();
      qw.showControls(true);
    }
  }
}
  static public void main(String args[]) {
    PApplet.main(new String[] { "--present", "--bgcolor=#666666", "--hide-stop", "SOT_2" });
  }
}
