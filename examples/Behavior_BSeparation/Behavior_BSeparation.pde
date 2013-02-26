// Punktiert is a particle engine based and thought as an extension of Karsten Schmidt's toxiclibs.physics code. 
// This library is developed through and for an architectural context. Based on my teaching experiences over the past couple years.
// (c) 2012 Daniel Köhler, daniel@lab-eds.org

//here: seperation (part of flocking) function as behavior

import punktiert.math.Vec;
import punktiert.physics.*;

// world object
VPhysics physics;

// number of particles in the scene
int amount = 100;

public void setup() {
  size(800, 600);
frameRate(60);

  physics = new VPhysics(new Vec(50,50), new Vec(width-50, height-50));
  physics.setfriction(.1f);

  for (int i = 0; i < amount; i++) {
    // val for arbitrary radius
    float rad = random(2, 10);
    // vector for position
    Vec pos = new Vec(random(100, width - 100), random(100, height - 100));
    // create particle (Vec pos, mass, radius)
    Sepp particle = new Sepp(pos, 5, rad);
    // add particle to world
    physics.addParticle(particle);
  }
}

public void draw() {

  background(255);

  physics.update();

  for (int i = 0; i < physics.particles.size(); i++) {
    Sepp p =  (Sepp) physics.particles.get(i); 

    float sep = frameCount*.07;
    p.setSeparation(sep);
    stroke(230,150);
    noFill();
    ellipse(p.x, p.y, sep, sep);
    
    noStroke();
    fill(0);
    ellipse(p.x, p.y, p.getRadius() * 2, p.getRadius() * 2);  
  }
}

