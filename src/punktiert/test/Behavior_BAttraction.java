package punktiert.test;

import processing.core.PApplet;
import punktiert.math.Vec;
import punktiert.physics.*;

@SuppressWarnings("serial")
public class Behavior_BAttraction extends PApplet {

	// world object
	VPhysics physics;

	// attractor
	BAttraction attr;
	//BVortex attr;

	// number of particles in the scene
	int amount = 200;

	public void setup() {

		size(800, 600);
		smooth();

		physics = new VPhysics();
		physics.setfriction(.1f);

		// new AttractionForce: (Vec pos, radius, strength)
		attr = new BAttraction(new Vec(width * .5f, height * .5f),new Vec(1,0,0), 400, .1f);
		//attr = new BVortex(new Vec(width * .5f, height * .5f), 400, .1f);
		physics.addBehavior(attr);

		for (int i = 0; i < amount; i++) {
			// val for arbitrary radius
			float rad = random(2, 20);
			// vector for position
			Vec pos = new Vec(random(rad, width - rad), random(rad, height - rad));
			// create particle (Vec pos, mass, radius)
			VParticle particle = new VParticle(pos, 4, rad);
			// add Collision Behavior
			particle.addBehavior(new BCollision());
			// add particle to world
			physics.addParticle(particle);
			//particle.setWeight(0);
		}
	}

	public void draw() {

		background(255);

		physics.update();

		noFill();
		stroke(200, 0, 0);
		// set pos to mousePosition
		attr.setAttractor(new Vec(mouseX, mouseY));
		ellipse(attr.getAttractor().x, attr.getAttractor().y, attr.getRadius(), attr.getRadius());

		noStroke();
		fill(0, 255);
		for (VParticle p : physics.particles) {
			ellipse(p.x, p.y, p.getRadius() * 2, p.getRadius() * 2);
		}
	}

}
