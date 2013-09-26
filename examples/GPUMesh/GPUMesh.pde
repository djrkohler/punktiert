// Punktiert is a particle engine based and thought as an extension of Karsten Schmidt's toxiclibs.physics code. 
// This library is developed through and for an architectural context. Based on my teaching experiences over the past couple years. (c) 2013 Daniel Köhler, daniel@lab-eds.org
// this sketch depends on Rootbeer/ The Rootbeer GPU Compiler makes it easy to use Graphics Processing Units from within Java.
// gpu Rootbeer is written by: Phil Pratt-Szeliga, Syracuse University, Rootbeer is licensed under the MIT license,  https://github.com/pcpratts/rootbeer1
// download rootbeer at: http://rbcompiler.com/download.html

//here: particle & spring update on GPU, if your grafic card is 1. from NVidia 2. supports OpenCL 
//still very rudimentary..

import edu.syr.pcpratts.rootbeer.runtime.Kernel;
import peasy.PeasyCam;
import punktiert.math.Vec;
import punktiert.GPUPhysics.*;


// physics system for the mesh
Physics physics;
// physics for the constraints
Physics physicsConstraints;

PeasyCam cam;

boolean pause = true;

public void setup() {
  size(1280, 720, P3D);
  frameRate(60);

  cam = new PeasyCam(this, 800);

  physics = new Physics();
  BConstantForce force = new BConstantForce(0, 0, .1f);
  physics.addBehavior(force);

  physicsConstraints = new Physics();
  physicsConstraints.addBehavior(new BAttraction(new Vec(), 1000, .2f));

  // lock all the Constraints (otherwise the springforce will alter the
  // position
  Particle a = new Particle(-width * .5f, -height * .5f).lock();
  Particle b = new Particle(width * .5f, -height * .5f).lock();
  Particle c = new Particle(width * .5f, height * .5f).lock();
  Particle d = new Particle(-width * .5f, height * .5f).lock();

  // add the Particles as Constraints to the mesh physics
  physics.addConstraint(a);
  physics.addConstraint(b);
  physics.addConstraint(c);
  physics.addConstraint(d);

  // add the Particles as Particles to the constraint physics
  physicsConstraints.addParticle(a);
  physicsConstraints.addParticle(b);
  physicsConstraints.addParticle(c);
  physicsConstraints.addParticle(d);

  // create a mesh
  float amountX = 100;
  float amountY = 100;

  float strength = 1f;
  ArrayList<Particle> particles = new ArrayList<Particle>();

  for (int i = 0; i <= amountY; i++) {

    Vec a0 = a.interpolateTo(d, i / amountY);
    Vec b0 = b.interpolateTo(c, i / amountY);

    for (int j = 0; j <= amountX; j++) {

      Vec pos = a0.interpolateTo(b0, j / amountX);
      Particle p = physics.addParticle(new Particle(pos));
      particles.add(p);

      if (j > 0) {
        //getParticle gives you the equal particle or constraint 
        Particle previous = physics.getParticle(particles.get(particles.size() - 2));
        Spring s = new Spring(p, previous, p.sub(previous).mag(), strength);
        physics.addSpring(s);
      }
      if (i > 0) {
        Particle above = physics.getParticle(particles.get(particles.size() - (int) amountX - 2));
        Spring s = new Spring(p, above, p.sub(above).mag(), strength);
        physics.addSpring(s);
      }
    }
  }
}

public void draw() {
  background(240);
  println(frameRate);

  if (!pause) {
    physics.update();

    // before you update you have to unlock the constarints
    for (Kernel k : physicsConstraints.particles) {
      Particle p = (Particle) k;
      p.unlock();
    }
    //if you have forces attached to the physics class update so
    physicsConstraints.update();
    for (Kernel k : physicsConstraints.particles) {
      Particle p = (Particle) k;
      p.lock();
    }
  }

  strokeWeight(1);
  stroke(100);
  for (Kernel k : physics.particles) {
    Particle p = (Particle) k;
    point(p.x, p.y, p.z);
  }
  strokeWeight(5);
  stroke(200, 0, 0);
  for (Particle p : physics.constraints) {
    point(p.x, p.y, p.z);
  }
}

public void keyPressed() {
  if (key == ' ')
    pause = !pause;
}

