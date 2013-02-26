package punktiert.test;

import peasy.PeasyCam;
import processing.core.PApplet;
import punktiert.math.Vec;
import punktiert.physics.*;

@SuppressWarnings("serial")
public class Spring_Anchor extends PApplet {

	VPhysics physics;

	PeasyCam cam;

	public void setup() {

		size(800, 600, P3D);
		smooth();

		cam = new PeasyCam(this, 800);

		physics = new VPhysics();

		for (int i = 0; i < 1000; i++) {
			
			Vec pos = new Vec(0,0,0).jitter(1);
			Vec vel = new Vec(random(-1, 1), random(-1, 1), random(-1, 1));
			VBoid p = new VBoid(pos, vel);
			
			p.swarm.setSeperationScale(3.0f);
			p.swarm.setSeperationRadius(50);
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

			strokeWeight(1);
			stroke(200, 0, 0);
			noFill();
			beginShape();
			for(int j=0;j<boid.trail.particles.size();j++){
				VParticle t = boid.trail.particles.get(j);
		      vertex(t.x, t.y, t.z);
		    }
		    endShape();
		}
	}


}
