// Punktiert is a particle engine based and thought as an extension of Karsten Schmidt's toxiclibs.physics code. 
// This library is developed through and for an architectural context. Based on my teaching experiences over the past couple years.
// (c) 2012 Daniel Köhler, daniel@lab-eds.org

//here: particles with swarm behavior, additional connected with a Range-Spring
//notice: a trail/ tracing curve can be adjusted in Length and Resolution

import peasy.PeasyCam;
import punktiert.math.Vec;
import punktiert.physics.*;

  VPhysics physics;

  PeasyCam cam;

  public void setup() {

    size(800, 600, P3D);
    smooth();

    cam = new PeasyCam(this, 800);

    physics = new VPhysics();

    for (int i = 0; i < 500; i++) {

      Vec pos = new Vec(0, 0, 0).jitter(1);
      Vec vel = new Vec(random(-1, 1), random(-1, 1), random(-1, 1));
      VBoid p = new VBoid(pos, vel);

      p.swarm.setSeperationScale(1.0f);
      p.swarm.setSeperationRadius(20);
      p.trail.setInPast(1);
      p.trail.setreductionFactor(80);
      physics.addParticle(p);

      physics.addSpring(new VSpringRange(p, new VParticle(), 300, 400, 0.00005f));
    }
  }

  public void draw() {
    background(240);

    physics.update();
    for (int i = 0; i < physics.particles.size(); i++) {
      VBoid boid = (VBoid) physics.particles.get(i);

      strokeWeight(5);
      stroke(0);
      point(boid.x, boid.y, boid.z);

      if (frameCount > 100) {
        boid.trail.setInPast(20);
      }
      strokeWeight(1);
      stroke(200, 0, 0);
      noFill();
      beginShape();
      for (int j = 0; j < boid.trail.particles.size(); j++) {
        VParticle t = boid.trail.particles.get(j);
        vertex(t.x, t.y, t.z);
      }
      endShape();
    }
  }

