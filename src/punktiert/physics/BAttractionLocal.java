package punktiert.physics;

import punktiert.math.Vec;

/**
 * Local Particle Behavior: compares the passed in Particle to its own list of
 * neighbors </p> add the Behavior to the VParticle.behaviors; </p> applies a
 * force based on distance to the passed in VParticle;
 * 
 */
public class BAttractionLocal implements BehaviorInterface {

	protected float radius, radiusSquared;
	protected float strength;

	/**
	 * constructor input: influence radius, strength (+ == attraction; - ==
	 * repulsion)
	 * 
	 * @param attractor
	 * @param radius
	 * @param strength
	 */
	public BAttractionLocal(float radius, float strength) {
		this.strength = strength;
		setRadius(radius);
	}

	@Override
	public void apply(VParticle p) {
		Vec sum = new Vec();
		int count = 0;
		for (VParticle neighbor : p.neighbors) {
			if (neighbor == p)
				continue;
			Vec delta = neighbor.sub(p);
			float dist = delta.magSq();
			if (dist < radiusSquared) {
				Vec f = delta.normalizeTo((1.0f - dist / radiusSquared)).multSelf(strength);
				sum.addSelf(f);
				count++;
			}
		}
		if (count > 0)
			sum.multSelf(1.0f / count);
		sum.limit(.01f);
		p.addForce(sum);
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

	public void setRadius(float r) {
		this.radius = r;
		this.radiusSquared = r * r;
	}

	/**
	 * @param strength
	 *            the strength to set
	 */
	public void setStrength(float strength) {
		this.strength = strength;
	}

}
