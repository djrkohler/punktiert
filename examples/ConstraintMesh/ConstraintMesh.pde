// Punktiert is a particle engine based and thought as an extension of Karsten Schmidt's toxiclibs.physics code. 
// This library is developed through and for an architectural context. Based on my teaching experiences over the past couple years.
// (c) 2012 Daniel Köhler, daniel@lab-eds.org

// here: relaxational particle mesh / textile with constrained points
// notice: all particles created at a similar position as a constraint 
// will be overwritten by the constraint

import peasy.PeasyCam;
import punktiert.math.Vec;
import punktiert.physics.*;

//physics system for mesh
VPhysicsSimple physics;
//physics for constraints
VPhysicsSimple physicsConstraints;

PeasyCam cam;

boolean pause = true;

public void setup() {

  size(1280, 720, P3D);
  smooth();

  cam = new PeasyCam(this, 800);

  physics = new VPhysicsSimple();
  BConstantForce force = new BConstantForce(0, 0, .1f);
  physics.addBehavior(force);

  physicsConstraints = new VPhysicsSimple();
  physicsConstraints.addBehavior(new BAttraction(new Vec(), 1000, 1f));

  //lock all the Constraints (otherwise the springforce will alter the position
  VParticle a = new VParticle(-width * .5f, -height * .5f).lock();
  VParticle b = new VParticle(width * .5f, -height * .5f).lock();
  VParticle c = new VParticle(width * .5f, height * .5f).lock();
  VParticle d = new VParticle(-width * .5f, height * .5f).lock();

  //add the Particles as Constraints to the mesh physics
  physics.addConstraint(a);
  physics.addConstraint(b);
  physics.addConstraint(c);
  physics.addConstraint(d);

  //add the Particles as Particles to the constraint physics
  physicsConstraints.addParticle(a);
  physicsConstraints.addParticle(b);
  physicsConstraints.addParticle(c);
  physicsConstraints.addParticle(d);


  //create a mesh
  float amountX = 100;
  float amountY = 50;

  float strength = 1f;
  ArrayList<VParticle> particles = new ArrayList<VParticle>();

  for (int i = 0; i <= amountY; i++) {
    Vec a0 = a.interpolateTo(d, i / amountY);
    Vec b0 = b.interpolateTo(c, i / amountY);

    for (int j = 0; j <= amountX; j++) {
      Vec pos = a0.interpolateTo(b0, j / amountX);
      VParticle p = physics.addParticle(new VParticle(pos));
      particles.add(p);

      if (j > 0) {
        //getParticle gives you the equal particle or constraint 
        VParticle previous = physics.getParticle(particles.get(particles.size() - 2));
        VSpring s = new VSpring(p, previous, p.sub(previous).mag(), strength);
        physics.addSpring(s);
      }
      if (i > 0) {
        VParticle above = physics.getParticle(particles.get(particles.size() - (int) amountX - 2));
        VSpring s = new VSpring(p, above, p.sub(above).mag(), strength);
        physics.addSpring(s);
      }
    }
  }
}

public void draw() {
  background(240);

  physics.update();

  // before you update you have to unlock the constarints
  for (VParticle c : physicsConstraints.particles) {
    c.unlock();
    //if you have just local forces
    //c.update();
    //c.lock();
  }
  //if you have forces attached to the physics class update so
  physicsConstraints.update();
  for (VParticle c : physicsConstraints.particles) {
    c.lock();
  }

  strokeWeight(2);
  stroke(100);
  for (VParticle p : physics.particles) {
    point(p.x, p.y, p.z);
  }
  strokeWeight(5);
  stroke(200, 0, 0);
  for (VParticle p : physics.constraints) {
    point(p.x, p.y, p.z);
  }
}

