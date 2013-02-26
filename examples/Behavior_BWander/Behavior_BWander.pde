// Punktiert is a particle engine based and thought as an extension of Karsten Schmidt's toxiclibs.physics code. 
// This library is developed through and for an architectural context. Based on my teaching experiences over the past couple years.
// (c) 2012 Daniel Köhler, daniel@lab-eds.org

//here: wander fuction as behavior

import punktiert.math.Vec;
import punktiert.physics.*;

//world object
VPhysics physics;

Vec mouse;

//number of particles in the scene
int amount = 100;

public void setup() {
  size(800, 600);
  fill(0, 255);

  physics = new VPhysics();

  BWorldBox boxx = new BWorldBox(new Vec(), new Vec(width, height, 500));
  boxx.setWrapSpace(true);
  physics.addBehavior(boxx);

  for (int i = 0; i < amount; i++) {
    //val for arbitrary radius
    float rad = random(2, 20);
    //vector for position
    Vec pos = new Vec (random(rad, width-rad), random(rad, height-rad));
    //create particle (Vec pos, mass, radius)
    VParticle particle = new VParticle(pos, 1, rad);
    //add Collision Behavior
    particle.addBehavior(new BCollision());

    particle.addBehavior(new BWander(1, 1, 1));
    //add particle to world
    physics.addParticle(particle);
  }
}

public void draw() {
  background(255);

  physics.update();

  for (VParticle p : physics.particles) {
    drawRectangle(p);
  }
}


void drawRectangle(VParticle p) {

  float deform = p.getVelocity().mag();
  float rad = p.getRadius();
  deform = map(deform, 0, 1.5f, rad, 0);
  deform = max (rad *.2f, deform);

  float rotation = p.getVelocity().heading();    

  pushMatrix();
  translate(p.x, p.y);
  rotate(HALF_PI*.5f+rotation);
  beginShape();
  vertex(-rad, +rad);
  vertex(deform, deform);
  vertex(rad, -rad);
  vertex(-deform, -deform);
  endShape(CLOSE);
  popMatrix();
}

