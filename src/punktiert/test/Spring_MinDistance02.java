package punktiert.test;

import peasy.PeasyCam;
import processing.core.PApplet;
import punktiert.math.Vec;
import punktiert.physics.*;

@SuppressWarnings("serial")
public class Spring_MinDistance02 extends PApplet {

	VPhysics physics;

	PeasyCam cam;

	public void setup() {

		size(800, 600, P3D);
		smooth();

		cam = new PeasyCam(this, 800);

		physics = new VPhysics();

		for (int i = 0; i < 1000; i++) {

			Vec pos = new Vec(0, 0, 0).jitter(1);
			Vec vel = new Vec(random(-1, 1), random(-1, 1), random(-1, 1));
			VBoid p = new VBoid(pos, vel);
			p.setRadius(20);

			p.swarm.setCohesionRadius(80);
			p.trail.setInPast(3);
			p.trail.setreductionFactor(2);
			physics.addParticle(p);
			
			p.addBehavior(new BCollision());

			physics.addSpring(new VSpringRange(p, new VParticle(), 300, 400, 0.0005f));
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

			if (frameCount > 400) {
				boid.trail.setInPast(100);
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

}
