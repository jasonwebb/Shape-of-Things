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


BezierWarp bw;
PImage img;
PVector[] vectorCornerArray = new PVector[4];
float[] xoff = new float[4];
float[] xoffInc = new float[4];
float[] yoff = new float[4];
float[] yoffInc = new float[4];

PVector[] vectorAnchorsArray = new PVector[8];
float[] xoffAnchors = new float[8];
float[] xoffIncAnchors = new float[8];
float[] yoffAnchors = new float[8];
float[] yoffIncAnchors = new float[8];

void setup() {
  size(640, 480, OPENGL);
  smooth();
  img = loadImage("scotland.tif");
  
  // second parameter is the grid resolution of deformation
  bw = new BezierWarp(this, 10);
  
  //init control sprties randomly
  for(int i = 0; i < 4; i++) {
    vectorCornerArray[i] = new PVector(random(0, width), random(0, height));
    xoff[i] = random(0, 0.05);
    xoffInc[i] = random(0, 0.05);
    yoff[i] = random(0, 0.05);
    yoffInc[i] = random(0, 0.05);
  }
   for(int i = 0; i < 8; i++) {
    vectorAnchorsArray[i] = new PVector(random(0, width), random(0, height));
    xoffAnchors[i] = random(0, 0.05);
    xoffIncAnchors[i] = random(0, 0.05);
    yoffAnchors[i] = random(0, 0.05);
    yoffIncAnchors[i] = random(0, 0.05);
  }
  
  //hides control sprites
  bw.showControls(true);
  
}

void draw() {
  background(0);
  
  //move around with perlin noise
  for(int i = 0; i < 4; i++) {
    xoff[i] = xoff[i] + xoffInc[i];
    yoff[i] = yoff[i] + yoffInc[i];
    vectorCornerArray[i].x = noise(xoff[i]) * width;
    vectorCornerArray[i].y = noise(yoff[i]) * height;
  }
  for(int i = 0; i < 8; i++) {
    xoffAnchors[i] = xoffAnchors[i] + xoffIncAnchors[i];
    yoffAnchors[i] = yoffAnchors[i] + yoffIncAnchors[i];
    vectorAnchorsArray[i].x = noise(xoffAnchors[i]) * width;
    vectorAnchorsArray[i].y = noise(yoffAnchors[i]) * height;
  }
  
  //call the shot
  bw.controlSprites(vectorCornerArray, vectorAnchorsArray);
  bw.render(img);
}
