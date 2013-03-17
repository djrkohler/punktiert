package punktiert.physics;

import punktiert.math.Vec;


/**
 * Local Particle Behavior: compares the passed in Particle to its own list of neighbors 
 * </p> addBehavior to the VParticle.behaviors;
 * </p> seperation algorythm based on: Craig Reynold's Boids program to simulate the flocking behavior of birds. Here just the rule of seperation. </p> Java
 * implementation Daniel Shiffman (www.shiffman.net); Jose Sanchez (www.plethora-project.com)
 */
public class BSeparate implements BehaviorInterface {
	
	float neighbordistance;
	float desSepSquared;
	float maxSpeed, maxForce;

	/**
	 * default Constructor input: rangeRadius for Seperation 25, default maxSpeed: 3.0, default maxForce .05
	 */
	public BSeparate() {
		this(25, 3.0f, .05f);
	}
	
	/**
	 * Constructor input: rangeRadius for Seperation, default maxSpeed: 3.0, default maxForce .05
	 * 
	 * @param neighbordistance 
	 */
	public BSeparate(float neighbordistance) {
		this(neighbordistance, 3.0f,.05f);
	}

	/**
	 * Constructor input: rangeRadius for Seperation, maximum speed, maximum force applied each time
	 * 
	 * @param neighbordistance 
	 * @param maxSpeed
	 * @param maxForce
	 */
	public BSeparate(float neighbordistance, float maxSpeed, float maxForce) {

		this.neighbordistance = neighbordistance;
		desSepSquared = neighbordistance*neighbordistance;
		this.maxSpeed = maxSpeed;
		this.maxForce = maxForce;
	}


	@Override
	public void apply(VParticle p) {
		Vec f = seperate(p);
		p.addForce(f);
	}

	public Vec seperate(VParticle p) {

		Vec sep = new Vec();

		int countSep = 0;

		for (VParticle neighbor : p.neighbors) {
			if (neighbor == p)
				continue;

			float d = p.distSq(neighbor);

			//SEPARATION:
			if (d < desSepSquared) {
				Vec diff = p.sub(neighbor);
				diff.normalizeTo(1.0f / d);
				sep.addSelf(diff);
				countSep++;
			}
			//SEPARATION:
			if (countSep > 0) {
				sep.multSelf(1.0f / countSep);
			}
			if (sep.magSq() > 0) {
				sep.normalizeTo(maxSpeed);
				sep.subSelf(p.getVelocity());
				sep.limit(maxForce);
			}
		}

		return sep;
	}


	public float getDesiredSeperation() {
		return neighbordistance;
	}

	public void setDesiredSeperation(float desiredSeperation) {
		this.neighbordistance = desiredSeperation;
		desSepSquared = neighbordistance*neighbordistance;
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
