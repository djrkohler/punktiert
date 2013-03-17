package punktiert.physics;

import punktiert.math.Vec;


/**
 * Local Particle Behavior: compares the passed in Particle to its own list of neighbors
 * 
 * </p> cohesion algorythm based on: Craig Reynold's Boids program to simulate the flocking behavior of birds. Here just the rule of coherence. </p> Java
 * implementation Daniel Shiffman (www.shiffman.net); Jose Sanchez (www.plethora-project.com)
 * 
 */
public class BCohesion implements BehaviorInterface {

	float neighbordistance, neighbordistSquared;
	float maxSpeed;
	float maxForce;

	/**
	 * default Constructor input: rangeRadius for Cohesion 30, maxSpeed: 3.0, maxForce .05
	 */
	public BCohesion() {
		this(30, 3.0f, .05f);
	}

	/**
	 * Constructor input: rangeRadius for Cohesion
	 * 
	 * @param desiredSeperation
	 * @param maxSpeed
	 * @param maxForce
	 */
	public BCohesion(float neighbordistance) {
		this(neighbordistance, 3.5f, 0.5f);
	}

	/**
	 * Constructor input: rangeRadius for Cohesion, maximum speed, maximum force applied each time
	 * 
	 * @param desiredSeperation
	 * @param maxSpeed
	 * @param maxForce
	 */
	public BCohesion(float neighbordistance, float maxSpeed, float maxForce) {
		this.neighbordistance = neighbordistance;
		this.neighbordistSquared = neighbordistance * neighbordistance;
		this.maxSpeed = maxSpeed;
		this.maxForce = maxForce;
	}

	@Override
	public void apply(VParticle p) {
		Vec f = cohesion(p);
		p.addForce(f);
	}

	public Vec cohesion(VParticle p) {
		Vec sum = new Vec();
		int count = 0;
		for (VParticle neighbor : p.neighbors) {
			if (neighbor == p)
				continue;

			float d = p.distSq(neighbor);
			if ((d > 0) && (d < neighbordistSquared)) {
				sum.addSelf(neighbor);
				count++;
			}
		}
		if (count > 0) {
			sum.multSelf(1.0f / ((float) count));
			return seek(sum, p);
		}
		return sum;
	}

	private Vec seek(Vec target, VParticle p) {
		Vec desired = target.sub(p);
		desired.normalizeTo(maxSpeed);
		if ((desired.mag() < 10.0f)) {
			desired.multSelf(maxSpeed * (desired.mag() / 10.0f));
		} else {
			desired.multSelf(maxSpeed);
		}
		Vec steer = desired.sub(p.getVelocity());
		steer.limit(.2f);

		return steer;
	}

	public float getDistance() {
		return neighbordistance;
	}

	public void setDistance(float neighbordistance) {
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
