package punktiert.test;

import edu.syr.pcpratts.rootbeer.runtime.Kernel;
import peasy.PeasyCam;
import processing.core.PApplet;
import punktiert.math.Vec;
import punktiert.GPUPhysics.*;
import punktiert.GPUPhysics.BAttraction;
import punktiert.GPUPhysics.BConstantForce;
import punktiert.GPUPhysics.BVortex;
import punktiert.physics.*;

@SuppressWarnings("serial")
public class GPUBehavior extends PApplet {

	VPhysicsSimple physics;
	
	Physics gpuPhysics;

	PeasyCam cam;
	
	boolean runOnGPU = true;

	public void setup() {
		size(800, 800, P3D);

		cam = new PeasyCam(this, 800);
		
		
		if(runOnGPU) {
			createGPUPhysics();
			// gpuPhysics.addBehavior(new BAttraction(new Vec(), 400, -1));
			 gpuPhysics.addBehavior(new BConstantForce(new Vec(0,.1f)));
		} else {
		createCPUPhysics();
		}
	}
	

	public void draw() {
		background(240);
		println(frameRate);

		strokeWeight(2);
		stroke(0);
			
		
		if(runOnGPU) {
			gpuPhysics.update();
			for (int i = 0; i < gpuPhysics.particles.size(); i++) {
				Particle p = (Particle) gpuPhysics.particles.get(i);
				point(p.x, p.y, p.z);
			}
		} else {
			physics.update();
			for (int i = 0; i < physics.particles.size(); i++) {
				VParticle p = (VParticle) physics.particles.get(i);
				point(p.x, p.y, p.z);
			}
		}
	}
	
	
	
	public void createCPUPhysics() {
		physics = new VPhysicsSimple();

		for (int i = 0; i < 10000; i++) {

			Vec pos = new Vec(0, 0, 0).jitter(10);
			Vec vel = new Vec(random(-1, 1), random(-1, 1), random(-1, 1));
			VParticle p = new VParticle(pos, vel);
			physics.addParticle(p);

			 VSpring anchor = new VSpring(new VParticle(), p, 400.0f, .05f);
			 physics.addSpring(anchor);
		}
	}
	
	public void createGPUPhysics() {
		gpuPhysics = new Physics();
		
		for (int i = 0; i < 10000; i++) {

			Vec pos = new Vec(0, 0, 0).jitter(10);
			Vec vel = new Vec(random(-1, 1), random(-1, 1), random(-1, 1));
			Particle p = new Particle(pos, vel);
			gpuPhysics.addParticle(p);

			 Spring anchor = new Spring(new Particle(), p, 400.0f, .05f);
			 gpuPhysics.addSpring(anchor);
		}
				
	}

}
