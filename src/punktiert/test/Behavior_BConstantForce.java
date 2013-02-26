package punktiert.test;

import processing.core.PApplet;
import punktiert.math.Vec;
import punktiert.physics.*;

@SuppressWarnings("serial")
public class Behavior_BConstantForce extends PApplet {

	//world object
	VPhysics physics;
	
	BConstantForce force;

	//number of particles in the scene
	int amount = 100;

	public void setup() {
	  size(800, 600);
	  smooth();
	  fill(0, 255);

	  physics = new VPhysics();
	  physics.setfriction(.02f);
	  
	  BWorldBox box = new BWorldBox(new Vec(20,20), new Vec(width-20, height-20));
	  box.setBounceSpace(true);
	  physics.addBehavior(box);
	 
	  //something like gravity..
	  physics.addBehavior(new BConstantForce(new Vec(0,.04f)));
	  
	  force = new BConstantForce(new Vec());
	  physics.addBehavior(force);

	  for (int i = 0; i < amount; i++) {
	    //val for arbitrary radius
	    float rad = random(2, 20);
	    //vector for position
	    Vec pos = new Vec (random(rad, width-rad), random(rad, height-rad));
	    float weight = rad;
	    //create particle (Vec pos, mass, radius)
	    VParticle particle = new VParticle(pos, weight, rad);
	    //add Collision Behavior
	    particle.addBehavior(new BCollision().setLimit(.2f));
	    //particle.addBehavior(new BAttractionLocal(rad*6, -1000));
	    //add particle to world
	    physics.addParticle(particle);
	  }
	}

	public void draw() {
	  background(255);
	  
	  physics.update();

	  //set Force related to mousePos and limit the length to .05
	  force.setForce(new Vec(width*.5f-mouseX, height*.5f-mouseY).normalizeTo(.05f));
	  
	  for (VParticle p : physics.particles) {
	    ellipse(p.x, p.y, p.getRadius()*2, p.getRadius()*2);
	  }
	}
	

}
