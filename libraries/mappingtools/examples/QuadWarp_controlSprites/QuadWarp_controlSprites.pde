// example for the .controlSprites method
// usefull if you want to move the quad without showing controls
// or move it "programmactically" or have multiple quads jumping around
//
//
// WARNING: Doesn't work with P2D renderer because of texture mapping
//          Use OPENGL or GLGraphics instead
//


import processing.opengl.*;
import mappingtools.*;


QuadWarp qw;
PImage img;
PVector[] vectorArray = new PVector[4];
float[] xoff = new float[4];
float[] xoffInc = new float[4];
float[] yoff = new float[4];
float[] yoffInc = new float[4];

void setup() {
  size(640, 480, OPENGL);
  smooth();
  img = loadImage("scotland.tif");
  
  // second parameter is the grid resolution of deformation
  qw = new QuadWarp(this, 10);
  
  //init control sprties randomly
  for(int i = 0; i < 4; i++) {
    vectorArray[i] = new PVector(random(0, width), random(0, height));
    xoff[i] = random(0, 0.05);
    xoffInc[i] = random(0, 0.05);
    yoff[i] = random(0, 0.05);
    yoffInc[i] = random(0, 0.05);
  }
  
  //hides control sprites
  qw.showControls(false);
  
}

void draw() {
  background(0);
  
  //move around with perlin noise
  for(int i = 0; i < 4; i++) {
    xoff[i] = xoff[i] + xoffInc[i];
    yoff[i] = yoff[i] + yoffInc[i];
    vectorArray[i].x = noise(xoff[i]) * width;
    vectorArray[i].y = noise(yoff[i]) * height;
  }
  
  //call the shot
  qw.controlSprites(vectorArray);
  qw.render(img);
}
