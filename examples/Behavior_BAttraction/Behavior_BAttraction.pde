// Punktiert is a particle engine based and thought as an extension of Karsten Schmidt's toxiclibs.physics code. 
// This library is developed through and for an architectural context. Based on my teaching experiences over the past couple years.
// (c) 2012 Daniel Köhler, daniel@lab-eds.org

//here: global attractor force connected to mouse position

import punktiert.math.Vec;
import punktiert.physics.*;

// world object
VPhysics physics;

// attractor
BAttraction attr;

// number of particles in the scene
int amount = 200;

public void setup() {
  size(800, 600);
  noStroke();

//set up physics
  physics = new VPhysics();
  physics.setfriction(.4f);

  // new AttractionForce: (Vec pos, radius, strength)
  attr = new BAttraction(new Vec(width * .5f, height * .5f), 400, .1f);
  physics.addBehavior(attr);

  for (int i = 0; i < amount; i++) {
    // val for arbitrary radius
    float rad = random(2, 20);
    // vector for position
    Vec pos = new Vec(random(rad, width - rad), random(rad, height - rad));
    // create particle (Vec pos, mass, radius)
    VParticle particle = new VParticle(pos, 4, rad);
    // add Collision Behavior
    particle.addBehavior(new BCollision());
    // add particle to world
    physics.addParticle(particle);
  }
}

public void draw() {

  background(255);

  physics.update();

  noFill();
  stroke(200, 0, 0);
  // set pos to mousePosition
  attr.setAttractor(new Vec(mouseX, mouseY));
  ellipse(attr.getAttractor().x, attr.getAttractor().y, attr.getRadius(), attr.getRadius());

  noStroke();
  fill(0, 255);
  for (VParticle p : physics.particles) {
    ellipse(p.x, p.y, p.getRadius() * 2, p.getRadius() * 2);
  }
}


