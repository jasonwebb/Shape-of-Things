// example for the multiple quads using .controlSprites method to move around
// 
//
// WARNING: Doesn't work with P2D renderer because of texture mapping
//          Use OPENGL or GLGraphics instead
// WARNING 2: with multiple instances forget about saving presets functionnalities (still in progress)


import processing.opengl.*;
import mappingtools.*;

int QUAD_COUNT = 20;

QuadWarp[] qw = new QuadWarp[QUAD_COUNT];
PImage img;
PVector[][] vectorArray = new PVector[QUAD_COUNT][4];
float[][] xoff = new float[QUAD_COUNT][4];
float[][] xoffInc = new float[QUAD_COUNT][4];
float[][] yoff = new float[QUAD_COUNT][4];
float[][] yoffInc = new float[QUAD_COUNT][4];

void setup() {
  size(640, 480, OPENGL);
  smooth();
  img = loadImage("scotland.tif");
  
  // second parameter is the grid resolution of deformation
  for(int i = 0; i< QUAD_COUNT; i++) {
    qw[i] = new QuadWarp(this, 10);
  }
  
  //init control sprties randomly
  for(int i = 0; i< QUAD_COUNT; i++) {
    for(int j = 0; j < 4; j++) {
      vectorArray[i][j] = new PVector(random(0, width), random(0, height));
      xoff[i][j] = random(0, 0.05);
      xoffInc[i][j] = random(0, 0.05);
      yoff[i][j] = random(0, 0.05);
      yoffInc[i][j] = random(0, 0.05);
    }
  }
  
  //hides control sprites
  for(int i = 0; i< QUAD_COUNT; i++) {
    qw[i].showControls(false);
  }
  
}

void draw() {
  background(0);
  
  //move around with perlin noise
  for(int i = 0; i< QUAD_COUNT; i++) {
    for(int j = 0; j < 4; j++) {
      xoff[i][j] = xoff[i][j] + xoffInc[i][j];
      yoff[i][j] = yoff[i][j] + yoffInc[i][j];
      vectorArray[i][j].x = noise(xoff[i][j]) * width;
      vectorArray[i][j].y = noise(yoff[i][j]) * height;
    }
  }
  
  //call the shot
   for(int i = 0; i< QUAD_COUNT; i++) {
    qw[i].controlSprites(vectorArray[i]);
    qw[i].render(img);
   }
}
