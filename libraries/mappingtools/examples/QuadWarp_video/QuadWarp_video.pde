//letter "c" shows or hides the control sprites
//letter "d" resets the sprites to defaults
//letter "s" saves the sprites positions in a .txt file in the sketch folder
//letter "p" loads the sprites positions presets stored in the .txt file 
//
//
// WARNING: Doesn't work with P2D renderer because of texture mapping
//          Use OPENGL or GLGraphics instead
//
// You will need the GSVideo Library (http://gsvideo.sourceforge.net/)
// to make this example work.
//
//


import processing.opengl.*;
import codeanticode.glgraphics.*;
import codeanticode.gsvideo.*;
import mappingtools.*;



QuadWarp qw;
GSMovie mov;
GLTexture tex;

void setup() {
  size(640, 480, GLConstants.GLGRAPHICS);
   frameRate(90);
  
  mov = new GSMovie(this, "ski-nautique.avi");
  
  // Use texture tex as the destination for the movie pixels.
  tex = new GLTexture(this);
  mov.setPixelDest(tex);
  
  // This is the size of the buffer where frames are stored
  // when they are not rendered quickly enough.
  tex.setPixelBufferSize(10);
  // New frames put into the texture when the buffer is full
  // are deleted forever, so this could lead dropeed frames:
  tex.delPixelsWhenBufferFull(false);
  // Otherwise, they are kept by gstreamer and will be sent
  // again later. This avoids loosing any frames, but increases 
  // the memory used by the application.
  
  mov.loop();
  
  background(0);
  noStroke();
  
  //second parameter is the grid resolution of deformation
  qw = new QuadWarp(this, 10);
  
  
}

void draw() {
  
  
  // Using the available() method and reading the new frame inside draw()
  // instead of movieEvent() is the most effective way to keep the 
  // audio and video synchronization.
  if (mov.available()) {
    background(0);
    mov.read();
    // putPixelsIntoTexture() copies the frame pixels to the OpenGL texture
    // encapsulated by 
    if (tex.putPixelsIntoTexture()) {
      
      qw.render(tex);
    }
  }
}
