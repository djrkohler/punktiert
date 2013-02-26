// Punktiert is a particle engine based and thought as an extension of Karsten Schmidt's toxiclibs.physics code. 
// This library is developed through and for an architectural context. Based on my teaching experiences over the past couple years.
// (c) 2012 Daniel Köhler, daniel@lab-eds.org

//here: cohesion (part of flocking) fuction as behavior

import punktiert.math.Vec;
import punktiert.physics.*;

// world object
VPhysics physics;

// attractor
BAttraction attr;

// number of particles in the scene
int amount = 300;

public void setup() {
  size(800, 600);
  noStroke();
  fill(0, 255);

  physics = new VPhysics();

  for (int i = 0; i < amount; i++) {
    // val for arbitrary radius
    float rad = random(2, 20);
    // vector for position
    Vec pos = new Vec(random(rad, width - rad), random(rad, height - rad));
    // create particle (Vec pos, mass, radius)
    VParticle particle = new VParticle(pos, 5, rad);
    // add Collision Behavior
   // particle.addBehavior(new BCollision());
    //add Cohesion Behavior to each Particle (radius, maxSpeed, maxForce)
    particle.addBehavior(new BCohesion(100, 1.5f, .5f));
    // add particle to world
    physics.addParticle(particle);
  }
}

public void draw() {
  background(255);

  physics.update();

  for (VParticle p : physics.particles) {
    ellipse(p.x, p.y, p.getRadius() * 2, p.getRadius() * 2);
  }
}


