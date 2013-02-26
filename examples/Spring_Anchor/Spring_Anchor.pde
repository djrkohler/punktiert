// Punktiert is a particle engine based and thought as an extension of Karsten Schmidt's toxiclibs.physics code. 
// This library is developed through and for an architectural context. Based on my teaching experiences over the past couple years.
// (c) 2012 Daniel Köhler, daniel@lab-eds.org

//here: particles with swarm behavior, additional connected with an Anchor-Spring

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

  for (int i = 0; i < 1000; i++) {

    Vec pos = new Vec(0, 0, 0).jitter(10);
    Vec vel = new Vec(random(-1, 1), random(-1, 1), random(-1, 1));
    VBoid p = new VBoid(pos, vel);

    p.swarm.setSeperationScale(3.0f);
    p.swarm.setSeperationRadius(50);
    p.trail.setInPast(1);
    physics.addParticle(p);

    VSpring anchor = new VSpring(new VParticle(p.x(), p.y(), p.z()), p, 400, .05f);
    physics.addSpring(anchor);
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
    
    if (frameCount > 50) {
        boid.trail.setInPast(100);
      }

    strokeWeight(1);
    stroke(200, 0, 0);
    noFill();
    beginShape();
    for (int j=0;j<boid.trail.particles.size();j++) {
      VParticle t = boid.trail.particles.get(j);
      vertex(t.x, t.y, t.z);
    }
    endShape();
  }
}

