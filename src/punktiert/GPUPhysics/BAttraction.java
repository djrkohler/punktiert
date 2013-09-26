package punktiert.GPUPhysics;

import punktiert.math.Vec;


/**
 * basic Attractor class, based on toxi.physics.behaviors.AttractionBehavior by Karsten Schmidt 
 * </p> applies a force based on distance to the passed in VParticle
 * </p> add this behavior to the VPhysics instance
 */

public class BAttraction extends Particle implements BehaviorInterface {

	//protected Vec pos;
	protected Vec dir;
	protected float radius, radiusSquared;
	protected float strength;

	/**
	 * constructor input: position, influence radius, strength (+ == attraction; - == repulsion)
	 * 
	 * @param attractor
	 * @param radius
	 * @param strength
	 */
	public BAttraction(Vec attractor, float radius, float strength) {
		super(attractor);
		this.strength = strength;
		setRadius(radius);
	}
	
	/**
	 * constructor input: position, direction of force, influence radius, strength (+ == attraction; - == repulsion)
	 * 
	 * @param attractor
	 * @param radius
	 * @param strength
	 */
	public BAttraction(Vec attractor, Vec dir, float radius, float strength) {
		super(attractor);
		//this.pos = attractor;
		this.dir = dir;
		this.strength = strength;
		setRadius(radius);
	}

	public void apply(Particle p) {
		Vec delta = this.sub(p);
		float dist = delta.magSq();
		if (dist < radiusSquared) {
			Vec f = delta.normalizeTo((1.0f - dist / radiusSquared)).multSelf(strength);
			if(dir != null) {
				f.multSelf(dir.x, dir.y, dir.z);
			}
			p.addForce(f);
		}
	}


	/**
	 * @return the attractor
	 */
	public Vec getAttractor() {
		return this;
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
		this.x = attractor.x;
		this.y = attractor.y;
		this.z = attractor.z;
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
