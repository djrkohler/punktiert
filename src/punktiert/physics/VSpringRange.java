package punktiert.physics;

import punktiert.math.Vec;

/**
 * Implements a string which will only enforce its rest length if the current distance is not between the MIN and MAX range. 
 */
public class VSpringRange extends VSpring {

	float restLengthMin, restLengthMinSquared;
	float restLengthMax, restLengthMaxSquared;
	
	public VSpringRange(VParticle a, VParticle b, float lenMin, float lenMax, float str) {
		super(a, b, str);
		setMinLength(lenMin);
		setMaxLength(lenMax);
		
	}

	public VSpringRange(VParticle a, VParticle b, float str) {
		super(a, b, str);
	}

	protected void update() {
		if (b.distSq(a) > restLengthMaxSquared) {
			//super.update();
			Vec delta = b.sub(a);
			// add minute offset to avoid div-by-zero errors
			float dist = delta.mag() + EPS;
			float normDistStrength = (dist - restLengthMax) / (dist * (a.invWeight + b.invWeight)) * strength;
			if (!a.isLocked) {
				a.addSelf(delta.mult(normDistStrength * a.invWeight));	
			}
			if (!b.isLocked) {
				b.addSelf(delta.mult(-normDistStrength * b.invWeight));
			}
		}
		
		if (b.distSq(a) < restLengthMinSquared) {
			//super.update();
			Vec delta = b.sub(a);
			// add minute offset to avoid div-by-zero errors
			float dist = delta.mag() + EPS;
			float normDistStrength = (dist - restLengthMin) / (dist * (a.invWeight + b.invWeight)) * strength;
			if (!a.isLocked) {
				a.addSelf(delta.mult(normDistStrength * a.invWeight));	
			}
			if (!b.isLocked) {
				b.addSelf(delta.mult(-normDistStrength * b.invWeight));
			}
		}
	}
	
	public float getRestLengthMin() {
		return restLengthMin;
	}

	public float getRestLengthMax() {
		return restLengthMax;
	}

	public VSpring setMinLength(float lenMin) {
		restLengthMin = lenMin;
		restLengthMinSquared = lenMin * lenMin;
		return this;
	}
	
	public VSpring setMaxLength(float lenMax) {
		restLengthMax = lenMax;
		restLengthMaxSquared = lenMax * lenMax;
		return this;
	}
	
}
