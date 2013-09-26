package punktiert.test;

import edu.syr.pcpratts.rootbeer.runtime.Kernel;
import peasy.PeasyCam;
import processing.core.PApplet;
import punktiert.math.Vec;
import punktiert.physics.*;

@SuppressWarnings("serial")
public class SpringBall extends PApplet {

	VPhysics physics;

	PeasyCam cam;

	public void setup() {

		size(800, 600, P3D);
		smooth();

		cam = new PeasyCam(this, 800);

		physics = new VPhysics();

		for (int i = 0; i < 10000; i++) {

			Vec pos = new Vec(0, 0, 0).jitter(1);
			Vec vel = new Vec(random(-1, 1), random(-1, 1), random(-1, 1));
			VParticle p = new VParticle(pos, vel);
			physics.addParticle(p);

			 //GPUSpring anchor = new GPUSpring(new GPUParticle(), p, 400.0f, .05f);
			 //physics.addSpring(anchor);
		}
		
		for (VParticle p : physics.particles) {
			VParticle part0 = (VParticle) p;
			for (int i = 0; i < 10; i++) {
				int randi = (int) random(physics.particles.size());
				VParticle part1 = (VParticle) physics.particles.get(randi);
				VSpring anchor = new VSpring(part0, part1, 100.0f, .05f);
				physics.addSpring(anchor);
			}
		}
	}

	public void draw() {
		background(240);

		physics.update();

		strokeWeight(2);
		stroke(0);
		for (int i = 0; i < physics.particles.size(); i++) {
			VParticle p = (VParticle) physics.particles.get(i);
			point(p.x, p.y, p.z);
		}

		strokeWeight(.5f);
		stroke(100);
		for (int i = 0; i < physics.springs.size(); i++) {
			VSpring s = (VSpring) physics.springs.get(i);
			line(s.a.x, s.a.y, s.a.z, s.b.x, s.b.y, s.b.z);
		}
	}

}
