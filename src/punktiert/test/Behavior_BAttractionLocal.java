package punktiert.test;

import processing.core.PApplet;
import punktiert.math.Vec;
import punktiert.physics.*;

@SuppressWarnings("serial")
public class Behavior_BAttractionLocal extends PApplet {

	// world object
	VPhysics physics;

	// number of particles in the scene
	int amount = 300;

	public void setup() {

		size(800, 600);
		smooth();
		noStroke();
		fill(0, 255);

		physics = new VPhysics();
		physics.setfriction(.4f);

		for (int i = 0; i < amount; i++) {
			// val for arbitrary radius
			float rad = random(2, 20);
			// vector for position
			Vec pos = new Vec(random(rad, width - rad), random(rad, height - rad));
			// create particle (Vec pos, mass, radius)
			VParticle particle = new VParticle(pos, 5, rad);
			// add Collision Behavior
			particle.addBehavior(new BCollision(.4f));
			// add Local Attractor on each Particle (radius, strength)
			particle.addBehavior(new BAttractionLocal(rad*2, 1));
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

}
