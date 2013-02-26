package punktiert.test;

import java.util.ArrayList;

import peasy.PeasyCam;
import processing.core.PApplet;
import punktiert.math.Vec;
import punktiert.physics.*;

@SuppressWarnings("serial")
public class ConstraintMesh_01 extends PApplet {

	// physics system for the mesh
	VPhysicsSimple physics;
	// physics for the constraints
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
		physicsConstraints.addBehavior(new BAttraction(new Vec(), 1000, .2f));

		// lock all the Constraints (otherwise the springforce will alter the
		// position
		VParticle a = new VParticle(-width * .5f, -height * .5f).lock();
		VParticle b = new VParticle(width * .5f, -height * .5f).lock();
		VParticle c = new VParticle(width * .5f, height * .5f).lock();
		VParticle d = new VParticle(-width * .5f, height * .5f).lock();

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
		float amountY = 50;

		float strength = 1f;
		ArrayList<VParticle> particles = new ArrayList<VParticle>();

		for (int i = 0; i <= amountY; i++) {

			Vec a0 = a.interpolateTo(d, i / amountY);
			Vec b0 = b.interpolateTo(c, i / amountY);

			for (int j = 0; j <= amountX; j++) {

				Vec pos = a0.interpolateTo(b0, j / amountX);
				VParticle p = null;
				if (random(1) < .01f) {
					p = new VParticle(pos,1,random(10,50)).lock();
					p.addBehavior(new BCollision());
					physics.addConstraint(p);
					physicsConstraints.addParticle(p);
				} else {
					p = physics.addParticle(new VParticle(pos));
				}
				particles.add(p);

				if (j > 0) {
					// getParticle gives you the equal particle or constraint
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

		if (!pause) {
			physics.update();

			// before you update you have to unlock the constarints
			for (VParticle c : physicsConstraints.particles) {
				c.unlock();
				// if you have just local forces
				// c.update();
				// c.lock();
			}
			// if you have forces attached to the physics class update so
			physicsConstraints.update();
			for (VParticle c : physicsConstraints.particles) {
				c.lock();
			}

		}

		
		stroke(0,0,200);
		noFill();
		for (VParticle p : physics.constraints) {
			ellipse(p.x, p.y, p.radius*2, p.radius*2);
		}

		stroke(100);
		for (VSpring s : physics.springs){
			line(s.a.x, s.a.y, s.a.z, s.b.x, s.b.y, s.b.z);
		}
	}

	public void keyPressed() {
		if (key == ' ')
			pause = !pause;

	}

}
