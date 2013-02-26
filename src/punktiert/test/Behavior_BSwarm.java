package punktiert.test;

import processing.core.PApplet;
import peasy.*;
import punktiert.math.Vec;
import punktiert.physics.*;

@SuppressWarnings("serial")
public class Behavior_BSwarm extends PApplet {

	VPhysics physics;

	PeasyCam cam;

	int amount = 1000;

	public void setup() {

		size(640, 360);
		smooth();
		noStroke();
		fill(0,255);

		physics = new VPhysics();

		BWorldBox boxx = new BWorldBox(new Vec(), new Vec(width, height, 500));
		boxx.setBounceSpace(true);
		physics.addBehavior(boxx);
		

		for (int i = 0; i < amount; i++) {
			Vec pos = new Vec(random(10,width), random(10,height),0);
			float rad = random(3, 8);
			VBoid p = new VBoid(pos);
			p.swarm.setSeperationScale(rad*.7f);
			p.setRadius(rad);
			physics.addParticle(p);
			
		}
	}

	public void draw() {

		background(255);

		physics.update();

		for (VParticle p : physics.particles) {
			ellipse(p.x,p.y, p.getRadius()*2, p.getRadius()*2);
		}
	}
}
