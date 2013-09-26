package punktiert.test;

import processing.core.PApplet;
import punktiert.math.Vec;
import punktiert.physics.*;

@SuppressWarnings("serial")
public class Behavior_BAttractionGroups extends PApplet {

	// world object
	VPhysics physics;

	// attractor
	BAttraction attr;

	// number of particles in the scene
	int amount = 800;

	public void setup() {
	  size(1280, 720);
	  noStroke();

	  //set up physics
	  physics = new VPhysics(width, height);
	  physics.setfriction(.4f);

	  // new AttractionForce: (Vec pos, radius, strength)
	  attr = new BAttraction(new Vec(width * .5f, height * .5f), 700, .1f);
	  physics.addBehavior(attr);

	  for (int i = 0; i < amount; i++) {
	    // val for arbitrary radius
	    float rad = random(2, 20);
	    // vector for position
	    Vec pos = new Vec(random(rad, width - rad), random(rad, height - rad));
	    // create particle (Vec pos, mass, radius)
	    VParticle particle = new VParticle(pos, 4, rad);
	    // add Collision Behavior
	    particle.addBehavior(new BCollision(.1f));
	    // particle.addBehavior(new BAttractionLocal(particle.radius, -1));
	    // add particle to world
	    physics.addParticle(particle);
	  }

	  physics.update();

	  for (VParticle p : physics.particles) {
	    for (VParticle other : p.neighbors) {
	      float d = p.dist(other);
	      if (d < p.radius+other.radius+3 && d>1) {
	        VSpring s = new VSpring(p, other, d, 1);
	        physics.addSpring(s);
	      }
	    }
	  }
	}

	public void draw() {

	  background(255);

	  physics.update();

	  noFill();
	  stroke(200, 0, 0);
	  // set pos to mousePosition
	  //attr.setAttractor(new Vec(mouseX, mouseY));
	  ellipse(attr.getAttractor().x, attr.getAttractor().y, attr.getRadius(), attr.getRadius());

	  noStroke();
	  fill(0, 255);
	  for (VParticle p : physics.particles) {
	    ellipse(p.x, p.y, p.getRadius() * 2, p.getRadius() * 2);
	  }

	  stroke(200);
	  for (VSpring s : physics.springs) {
	    line(s.a.x, s.a.y, s.b.x, s.b.y);
	  }

	  for (VParticle p : physics.particles) {
	    if (physics.getnumConnected(p) < 5) {
	      for (VParticle other : p.neighbors) {
	        float d = p.dist(other);
	        if (d < p.radius+other.radius+10 && d>1) {
	          VSpring s = new VSpring(p, other, d, 1);
	          physics.addSpring(s);
	        }
	      }
	    }
	  }
	}

}
