package punktiert.physics;

import punktiert.math.Vec;

/**
 * Local Particle Behavior: compares the passed in Particle to its own list of neighbors 
 * </p> addBehavior to the VParticle.behaviors; call each timeStep
 * </p> Seek algorythm based on: Craig Reynold's Boids program to simulate the flocking behavior of birds. Here just the rule of Steering. </p> Java
 * implementation Daniel Shiffman (www.shiffman.net)
 */
public class BSeek implements BehaviorInterface {

	Vec target;
	float maxSpeed;

	public BSeek(Vec target) {
		this(target, 3.5f);
	}

	public BSeek(Vec target, float maxSpeed) {
		this.target = target;
		this.maxSpeed = maxSpeed;
	}

	@Override
	public void apply(VParticle p) {
		Vec f = seek(p);
		p.addForce(f);
	}

	public Vec seek(VParticle p) {
		Vec desired = target.sub(p);
		desired.normalizeTo(maxSpeed);
		if ((desired.mag() < 100.0f)) {
			desired.multSelf(maxSpeed * (desired.mag() / 100.0f)); 
		} else {
			desired.multSelf(maxSpeed);
		}
		Vec steer = desired.sub(p.getVelocity());
		steer.limit(0.5f);

		return steer;
	}

}
