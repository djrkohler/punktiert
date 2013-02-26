package punktiert.test;

import peasy.PeasyCam;
import processing.core.PApplet;
import processing.core.PShape;
import punktiert.math.Vec;
import punktiert.physics.*;
import processing.core.PConstants.*;

@SuppressWarnings("serial")
public class FlockWithTrails extends PApplet {

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
			
			PShape s =  new PShape(0);
			shape(s);
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
			/*
			for (VParticle t : boid.trail.particles) {
				point(t.x, t.y, t.z);
			}*/
			noFill();
			beginShape();
			for(int j=0;j<boid.trail.particles.size();j++){
				VParticle t = boid.trail.particles.get(j);
		      vertex(t.x, t.y, t.z);
		    }
		    endShape();
		}

		stroke(10);
		strokeWeight(.3f);
		

	}

	public void render(VParticle p) {

		float theta = p.getVelocity().heading() + radians(90);
		float r = p.getRadius();

		fill(200);
		
		noStroke();
		pushMatrix();
		translate(p.x, p.y, p.z);
		rotate(theta);
		beginShape(TRIANGLES);
		vertex(0, -r * 2);
		vertex(-r, r * 2);
		vertex(r, r * 2);
		endShape();
		popMatrix();
	}

	

}
