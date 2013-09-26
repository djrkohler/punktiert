package punktiert.GPUPhysics;

import punktiert.math.Vec;


/**
 * basic Attractor class, based on toxi.physics.behaviors.AttractionBehavior by Karsten Schmidt 
 * </p> applies a force based on distance to the passed in VParticle
 * </p> add this behavior to the VPhysics instance
 */

public class BVortex implements BehaviorInterface {

	protected Vec pos;
	protected float radius, radiusSquared;
	protected float strength;

	/**
	 * constructor input: position, influence radius, strength (+ == attraction; - == repulsion)
	 * 
	 * @param attractor
	 * @param radius
	 * @param strength
	 */
	public BVortex(Vec attractor, float radius, float strength) {
		this.pos = attractor;
		this.strength = strength;
		setRadius(radius);
	}

	public void apply(Particle p) {
		Vec delta = pos.sub(p);
		float dist = delta.magSq();
		if (dist < radiusSquared) {
			Vec f = delta.normalizeTo((1.0f - dist / radiusSquared)).multSelf(strength);
			p.addForce(f);
			
			Vec vel = p.getVelocity();
			vel.rotateZ(f.mag());
			p.setVelocity(vel);
			p.setPreviousPosition(p.sub(vel));
		}
	}


	/**
	 * @return the attractor
	 */
	public Vec getAttractor() {
		return pos;
	}

	/**
	 * @return the radius
	 */
	public float getRadius() {
		return radius;
	}

	/**
	 * @return the strength
	 */
	public float getStrength() {
		return strength;
	}

	/**
	 * @param attractor the attractor to set
	 */
	public void setAttractor(Vec attractor) {
		pos.x = attractor.x;
		pos.y = attractor.y;
		pos.z = attractor.z;
	}

	public void setRadius(float r) {
		this.radius = r;
		this.radiusSquared = r * r;
	}

	/**
	 * @param strength the strength to set
	 */
	public void setStrength(float strength) {
		this.strength = strength;
	}

}
