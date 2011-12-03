//letter "c" shows or hides the control sprites
// shift-click gives you acces to the Bezier anchor points (yellow sprites)
//
//mouseWheel lets you control the edge blending when the mouse is over a blending region
// mouseWheel+shift displaces the blend edge, mousewheel alone changes the gradient
//
//letter "d" resets the sprites to defaults
//letter "s" saves the sprites positions in a .txt file in the sketch folder
//letter "p" loads the sprites positions presets stored in the .txt file 
//
//
//
// WARNING: Doesn't work with P2D renderer because of texture mapping
//          Use OPENGL or GLGraphics instead
//
//
// This example is a variation of the BezierWarp in a cluster projection set-up 
// Edge blending is added to the bezierWarp, and you can specify texture width, height, x-axis texture offset and
// y-axis texture offset in order to tile your media according to your needs
// You can use the same media for every tiles, or pre-tile a huge film in order to have more pixels than you need
// The edge blend still needs to be improved but I had acceptable results with this formulation.
//


import javax.media.opengl.*;
import processing.opengl.*;
import mappingtools.*;


ClusterBezierWarp clustbw;
GL gl;
PImage img;

void setup() {
  size(640, 480, OPENGL);
  frame.setLocation(0,0);
  smooth();
  img = loadImage("scotland.tif");
  
  // last four parameters define the blending region in number of tiles of the grid
  // the grid resolution is 10 x 10
  // first number: number of LEFT blend tiles
  // second number: number of RIGHT blend tiles
  // third number: number of UP blend tiles
  // fourth number: number of DOWN blend tiles
  //
  // this sketch is set to be blending only on the 2 first tiles counting from right
  clustbw = new ClusterBezierWarp(this, 0, 2, 0, 0);
  //
  // for 4 edge 2 tiles blending
  //clustbw = new ClusterBezierWarp(this, 2, 2, 2, 2);
  //
  
}

void draw() {
  background(0);
  
  PGraphicsOpenGL pgl = (PGraphicsOpenGL) g;  // g may change
  GL gl = pgl.beginGL();  // always use the GL object returned by beginGL
  
  //render mask
  clustbw.renderMask();
  
  // multiply blending
  gl.glBlendFunc(GL.GL_ZERO,GL.GL_SRC_COLOR);
  
  //render mask, parameter is gray color floor value
  clustbw.renderEdges();
  
  //render image
  clustbw.render(img, 0, 0, img.width, img.height);
  
  //normal blending
  gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
  gl.glBlendEquation(GL.GL_FUNC_ADD);
  
  //render sprites
  clustbw.sprites();
  
  pgl.endGL();
}


void init(){
 frame.dispose();  
 frame.setUndecorated(true);
 super.init();  
}
