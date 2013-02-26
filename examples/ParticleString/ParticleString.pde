// Punktiert is a particle engine based and thought as an extension of Karsten Schmidt's toxiclibs.physics code. 
// This library is developed through and for an architectural context. Based on my teaching experiences over the past couple years.
// (c) 2012 Daniel Köhler, daniel@lab-eds.org

//here: define a string of particles by the equal division between a start and end particle

import punktiert.math.Vec;
import punktiert.physics.*;


// world object
VPhysics physics;

VParticleString string;

public void setup() {
  size(800, 600);
  strokeWeight(10);
  // create world object with bouncing behavior
  physics = new VPhysics(width, height);
  physics.setfriction(.01f);

  //create two particles and add an arbitrary wander behavior for random movement
  VParticle particleA = new VParticle(250, height * .5f);
  particleA.addBehavior(new BWander(-1, 1, 1));
  VParticle particleB = new VParticle(width - 250, height * .5f);
  particleB.addBehavior(new BWander(1, 1, 1));

  //create a ParticleString: input: VPhysics, particleA, particleB, resolution, strength
  string = new VParticleString(physics, particleA, particleB, .1f, .0002f);
  physics.addGroup(string);
}

public void draw() {
  background(255);

  stroke(0);
  physics.update();
  for (VSpring s : physics.springs) {
    line(s.a.x, s.a.y, s.b.x, s.b.y);
  }

  fill(255);
  noStroke();
  VParticle head = string.getHead();
  VParticle tail = string.getTail();
  ellipse(head.x, head.y, 7, 7);
  ellipse(tail.x, tail.y, 7, 7);
}

