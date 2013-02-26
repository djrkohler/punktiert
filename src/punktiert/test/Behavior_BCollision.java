package punktiert.test;

import processing.core.PApplet;
import punktiert.math.Vec;
import punktiert.physics.*;

@SuppressWarnings("serial")
public class Behavior_BCollision extends PApplet {

	//world object
	VPhysics physics;

	//number of particles in the scene
	int amount = 200;

	public void setup() {
	  size(800, 600);
	  noStroke();
	  fill(0, 255);

	  physics = new VPhysics(width, height);
	  physics.setfriction(.01f);

	  for (int i = 0; i < amount; i++) {
	    //val for arbitrary radius
	    float rad = 16;
	    //vector for position
	    Vec pos = new Vec (random(rad, width-rad), random(rad, height-rad));
	    //create particle (Vec pos, mass, radius)
	    VParticle particle = new VParticle(pos, 1, rad);
	    //add Collision Behavior
	    particle.addBehavior(new BCollision(.3f));
	    //add particle to world
	    physics.addParticle(particle);
	  }
	}

	public void draw() {
	  background(255);
	  
	  physics.update();

	  for (VParticle p : physics.particles) {
	    ellipse(p.x, p.y, p.getRadius()*2, p.getRadius()*2);
	  }
	}
	

}
