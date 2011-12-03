import processing.core.*; 
import processing.xml.*; 

import processing.opengl.*; 
import javax.media.opengl.*; 
import mappingtools.*; 

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

public class SOT_3 extends PApplet {





QuadWarp qw;
String imageFilenames[] = {"Living-Room-Modified.png","Living-Room.png","Living-Room-Modified.png","Blank-Frame.jpg","Living-Room-Modified.png","Blank-Frame.jpg","Living-Room-Modified.png"};
ArrayList<PImage> images = new ArrayList();
PImage currentImage, nextImage;
int currentImageIndex = 0, nextImageIndex = 0;
boolean SHOW_CURSOR = false, FADE_OUT = false, FADE_IN = true;
int fadeCounter = 0;

public void setup() {
  size(screen.width, screen.height, OPENGL);
  smooth();
  noCursor();
  
  for(int i=0; i<imageFilenames.length; i++) {
    PImage tImage = loadImage(imageFilenames[i]);
    images.add(tImage);
  }
    
  currentImage = (PImage) images.get( currentImageIndex );
  
  // second parameter is the grid resolution of deformation
  qw = new QuadWarp(this, 10);
  qw.showControls(false);
  qw.loadPresets();
}

public void draw() {
  background(0);

  if(FADE_OUT) {
    tint(255,255-fadeCounter);
    
    fadeCounter += 5; 
    
    if(fadeCounter>=255) {
      currentImage = (PImage) images.get(currentImageIndex);
      FADE_OUT = false;
      FADE_IN = true;
      fadeCounter = 0;
    }
  }
  
  if(FADE_IN) {
    tint(255,fadeCounter);
    
    fadeCounter += 5;
    
    if(fadeCounter>=255) {
      FADE_IN = false;
      fadeCounter = 0;
    }
  }
 
  qw.render(currentImage);
  
}

public void keyPressed() {
  if(key == CODED) {
    if(keyCode == LEFT) {
      if(currentImageIndex==0)
        currentImageIndex = images.size()-1;
      else
        currentImageIndex--;
    } else if(keyCode == RIGHT) {
      if(currentImageIndex==images.size()-1)
        currentImageIndex = 0;
      else
        currentImageIndex++;    
    }
    
    FADE_OUT = true;
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
    PApplet.main(new String[] { "--present", "--bgcolor=#666666", "--hide-stop", "SOT_3" });
  }
}
