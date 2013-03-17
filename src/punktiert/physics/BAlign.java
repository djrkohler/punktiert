package punktiert.physics;

import punktiert.math.Vec;


/**
 * Local Particle Behavior: compares the passed in Particle to its own list of neighbors 
 * </p> addBehavior to the VParticle.behaviors;  
 * </p> Alignment algorythm based on: Craig Reynold's Boids program to simulate the flocking behavior of birds. Here just the rule of Alignment. </p> Java
 * implementation Daniel Shiffman (www.shiffman.net); Jose Sanchez (www.plethora-project.com)
 */
public class BAlign implements BehaviorInterface {

	float neighbordistance, neighbordistSquared;
	float maxSpeed;
	float maxForce;

	/**
	 * default Constructor input: rangeRadius for Seperation 25, default maxSpeed: 3.0, default maxForce .05
	 */
	public BAlign() {
		this(30, 3.0f, .05f);
	}

	/**
	 * Constructor input: rangeRadius for Seperation, default maxSpeed: 3.0, default maxForce .05
	 * 
	 * @param neighbordistance
	 */
	public BAlign(float neighbordistance) {
		this(neighbordistance, 3.0f, .05f);
	}

	/**
	 * Constructor input: rangeRadius for Seperation, maximum speed, maximum force
	 * 
	 * @param neighbordistance
	 * @param maxSpeed
	 * @param maxForce
	 */
	public BAlign(float neighbordistance, float maxSpeed, float maxForce) {

		this.neighbordistance = neighbordistance;
		neighbordistSquared = neighbordistance * neighbordistance;
		this.maxSpeed = maxSpeed;
		this.maxForce = maxForce;
	}

	@Override
	public void apply(VParticle p) {
		Vec f = align(p);
		p.addForce(f);
	}

	public Vec align(VParticle p) {

		Vec ali = new Vec();
		int countAli = 0;

		for (VParticle neighbor : p.neighbors) {
			if (neighbor == p)
				continue;

			float d = p.distSq(neighbor);

			if (d < neighbordistSquared) {
				ali.addSelf(neighbor.getVelocity());
				countAli++;
			}
		}

		if (countAli > 0) {
			ali.multSelf(1.0f / countAli);
		}
		if (ali.magSq() > 0) {
			ali.normalizeTo(maxSpeed);
			ali.subSelf(p.getVelocity());
			ali.limit(maxForce);
		}
		return ali;
	}

	public float getNeighbordistance() {
		return neighbordistance;
	}

	public void setNeighbordistance(float neighbordistance) {
		this.neighbordistance = neighbordistance;
		neighbordistSquared = neighbordistance*neighbordistance;
		
	}

	public float getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public float getMaxForce() {
		return maxForce;
	}

	public void setMaxForce(float maxForce) {
		this.maxForce = maxForce;
	}
	
	

}
