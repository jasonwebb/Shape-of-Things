//letter "c" shows or hides the control sprites
//letter "d" resets the sprites to defaults
//letter "s" saves the sprites positions in a .txt file in the sketch folder
//letter "p" loads the sprites positions presets stored in the .txt file 
//
// mouseWheel expands the sprite selection area (green circle)
//
// WARNING: Doesn't work with P2D renderer because of texture mapping
//          Use OPENGL or GLGraphics instead
//
// You will need the GLGraphics Library (http://glgraphics.sourceforge.net/)
// to make this example work.
//
//


import processing.opengl.*;
import codeanticode.glgraphics.*;
import mappingtools.*;

GLGraphicsOffScreen offscreen;
FreeWarp fw;
PImage img;

void setup() {
  size(640, 480, GLConstants.GLGRAPHICS);
  smooth();
  img = loadImage("scotland.tif");
  offscreen = new GLGraphicsOffScreen(this, width, height);
  
  fw = new FreeWarp(this);
  
  
}

void draw() {
  background(0);
  
  //draw something in offscreen buffer
  offscreen.beginDraw();
  offscreen.background(0);
  offscreen.image(img, 0, 0, offscreen.width, offscreen.height);
  offscreen.endDraw();
  
  fw.render(offscreen.getTexture());
}
