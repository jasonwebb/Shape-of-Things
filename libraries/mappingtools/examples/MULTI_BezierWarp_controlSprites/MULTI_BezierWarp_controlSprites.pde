// example for the multiple BezierQuads using .controlSprites method to move around
//
//
// WARNING: Doesn't work with P2D renderer because of texture mapping
//          Use OPENGL or GLGraphics instead
// WARNING 2: with multiple instances forget about saving presets functionnalities (still in progress)


import processing.opengl.*;
import mappingtools.*;

int QUAD_COUNT = 5;

BezierWarp[] bw = new BezierWarp[QUAD_COUNT];
PImage img;
PVector[][] vectorCornerArray = new PVector[QUAD_COUNT][4];
float[][] xoff = new float[QUAD_COUNT][4];
float[][] xoffInc = new float[QUAD_COUNT][4];
float[][] yoff = new float[QUAD_COUNT][4];
float[][] yoffInc = new float[QUAD_COUNT][4];

PVector[][] vectorAnchorsArray = new PVector[QUAD_COUNT][8];
float[][] xoffAnchors = new float[QUAD_COUNT][8];
float[][] xoffIncAnchors = new float[QUAD_COUNT][8];
float[][] yoffAnchors = new float[QUAD_COUNT][8];
float[][] yoffIncAnchors = new float[QUAD_COUNT][8];

void setup() {
  size(640, 480, OPENGL);
  smooth();
  img = loadImage("scotland.tif");
  
  // second parameter is the grid resolution of deformation
  for(int i = 0; i< QUAD_COUNT; i++) {
    bw[i] = new BezierWarp(this, 10);
  }
  
  //init control sprties randomly
  for(int i = 0; i< QUAD_COUNT; i++) {
    for(int j = 0; j < 4; j++) {
      vectorCornerArray[i][j] = new PVector(random(0, width), random(0, height));
      xoff[i][j] = random(0, 0.05);
      xoffInc[i][j] = random(0, 0.05);
      yoff[i][j] = random(0, 0.05);
      yoffInc[i][j] = random(0, 0.05);
    }
  }
   for(int i = 0; i< QUAD_COUNT; i++) {
    for(int j = 0; j < 8; j++) {
      vectorAnchorsArray[i][j] = new PVector(random(0, width), random(0, height));
      xoffAnchors[i][j] = random(0, 0.05);
      xoffIncAnchors[i][j] = random(0, 0.05);
      yoffAnchors[i][j] = random(0, 0.05);
      yoffIncAnchors[i][j] = random(0, 0.05);
    }
  }
  
  //hides control sprites
  for(int i = 0; i< QUAD_COUNT; i++) {
   bw[i].showControls(true);
  }
}

void draw() {
  background(0);
  
  //move around with perlin noise
  for(int i = 0; i< QUAD_COUNT; i++) {
    for(int j = 0; j < 4; j++) {
      xoff[i][j] = xoff[i][j] + xoffInc[i][j];
      yoff[i][j] = yoff[i][j] + yoffInc[i][j];
      vectorCornerArray[i][j].x = noise(xoff[i][j]) * width;
      vectorCornerArray[i][j].y = noise(yoff[i][j]) * height;
    }
  }
  for(int i = 0; i< QUAD_COUNT; i++) {
    for(int j = 0; j < 8; j++) {
      xoffAnchors[i][j] = xoffAnchors[i][j] + xoffIncAnchors[i][j];
      yoffAnchors[i][j] = yoffAnchors[i][j] + yoffIncAnchors[i][j];
      vectorAnchorsArray[i][j].x = noise(xoffAnchors[i][j]) * width;
      vectorAnchorsArray[i][j].y = noise(yoffAnchors[i][j]) * height;
    }
  }
  
  //call the shot
  for(int i = 0; i< QUAD_COUNT; i++) {
    bw[i].controlSprites(vectorCornerArray[i], vectorAnchorsArray[i]);
    bw[i].render(img);
  }
}
