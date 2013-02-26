package punktiert.physics;

import punktiert.math.Vec;


/**
 * applies a force to the passed in VParticle
 * </p> add this behavior to the VPhysics instance
 */
public class BConstantForce implements BehaviorInterface {

	protected Vec force;

	public BConstantForce(Vec force) {
		this.force = force;
	}
	
	public BConstantForce(float x, float y, float z) {
		this.force = new Vec(x,y,z);
	}

	public void apply(VParticle p) {
		p.addForce(force);
	}
	/**
	 * @return the force
	 */
	public Vec getForce() {
		return force;
	}
	/**
	 * @param force the force to set
	 */
	public void setForce(Vec force) {
		this.force = force;
	}
}
