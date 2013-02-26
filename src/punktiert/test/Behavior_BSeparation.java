package punktiert.test;

import processing.core.PApplet;
import punktiert.math.Vec;
import punktiert.physics.*;

@SuppressWarnings("serial")
public class Behavior_BSeparation extends PApplet {

	// world object
	VPhysics physics;

	// attractor
	BAttraction attr;

	// number of particles in the scene
	int amount = 400;

	public void setup() {

		size(800, 600);
		smooth();
		noStroke();
		fill(0, 255);

		physics = new VPhysics();
		physics.setfriction(.001f);
		
		BWorldBox box = new BWorldBox(new Vec(20,20), new Vec(width-20, height-20));
		  box.setBounceSpace(true);
		  physics.addBehavior(box);

		// new AttractionForce: (Vec pos, radius, strength)
		//attr = new BAttraction(new Vec(width * .5f, height * .5f), 400, .1f);
		//physics.addBehavior(attr);

		for (int i = 0; i < amount; i++) {
			// val for arbitrary radius
			float rad = random(2, 10);
			// vector for position
			Vec pos = new Vec(random(rad, width - rad), random(rad, height - rad));
			// create particle (Vec pos, mass, radius)
			VParticle particle = new VParticle(pos, 5, rad);
			// add Collision Behavior
			//particle.addBehavior(new BCollision());
			//add Cohesion Behavior to each Particle (radius, maxSpeed, maxForce)
			
			particle.addBehavior(new BSeparate(rad*.5f, 3.5f, .1f));
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
