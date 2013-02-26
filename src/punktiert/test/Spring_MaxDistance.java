package punktiert.test;

import processing.core.PApplet;
import punktiert.math.Vec;
import punktiert.physics.*;

@SuppressWarnings("serial")
public class Spring_MaxDistance extends PApplet {

	// world object
	VPhysics physics;

	// number of particles in the scene
	int amount = 500;

	public void setup() {

		size(800, 600);
		smooth();
		noStroke();
		fill(0, 255);

		physics = new VPhysics(width, height);
		physics.setfriction(.1f);

		for (int i = 0; i < amount; i++) {
			// val for arbitrary radius
			float rad = random(2, 15);
			// vector for position
			Vec pos = new Vec(random(width*.2f, width*.8f), random(height*.2f, height*.8f));
			// create particle (Vec pos, mass, radius)
			VParticle particle = new VParticle(pos, 5, rad);
			// add Collision Behavior
			particle.addBehavior(new BCollision());
			// add particle to world
			physics.addParticle(particle);
		}
		
		// Connect all the nodes with a Spring
	    for (int i = 0; i < physics.particles.size()-1; i++) {
	      VParticle pi = physics.particles.get(i);
	      for (int j = i+1; j < physics.particles.size(); j++) {
	        VParticle pj = physics.particles.get(j);
	        // A Spring needs two particles, a resting length, and a strength
	        physics.addSpring(new VSpringMaxDistance(pi, pj, height-150, 0.0001f));
	      }
	    }
	}

	public void draw() {

		background(255);

		physics.update();
		
		for (VParticle p : physics.particles) {
			ellipse(p.x, p.y, p.getRadius() * 2, p.getRadius() * 2);
		}
	}

}
