// Punktiert is a particle engine based and thought as an extension of Karsten Schmidt's toxiclibs.physics code. 
// This library is developed through and for an architectural context. Based on my teaching experiences over the past couple years.
// (c) 2012 Daniel Köhler, daniel@lab-eds.org

// here: global constant force 

import punktiert.math.Vec;
import punktiert.physics.*;

  //world object
  VPhysics physics;
  
  BConstantForce force;

  //number of particles in the scene
  int amount = 500;

  public void setup() {
    size(800, 600);
    fill(0, 255);
    noStroke();

    physics = new VPhysics(new Vec(100,100), new Vec(width-100, height-100));
    physics.setfriction(.3f);
    
    force = new BConstantForce(new Vec());
    physics.addBehavior(force);

    for (int i = 0; i < amount; i++) {
      //val for arbitrary radius
      float rad = random(3, 8);
      //vector for position
      Vec pos = new Vec (random(rad, width-rad), random(rad, height-rad));
      float weight = rad;
      //create particle (Vec pos, mass, radius)
      VParticle particle = new VParticle(pos, weight, rad);
      //add Collision Behavior
      particle.addBehavior(new BCollision().setLimit(.04));
      //add particle to world
      physics.addParticle(particle);
    }
  }

  public void draw() {
    background(255);
    
    physics.update();

    //set Force related to mousePos and limit the length to .05
    force.setForce(new Vec(width*.5f-mouseX, height*.5f-mouseY).normalizeTo(.03f));
    
    for (VParticle p : physics.particles) {
      ellipse(p.x, p.y, p.getRadius()*2, p.getRadius()*2);
    }
  }
  

