package punktiert.physics;

import punktiert.math.Vec;

/**
 * Implements a string which will only enforce its rest length if the current distance is MORE than its rest length. 
 */
public class VSpringMaxDistance extends VSpring {

	public VSpringMaxDistance(VParticle a, VParticle b, float len, float str) {
		super(a, b, len, str);
	}

	public VSpringMaxDistance(VParticle a, VParticle b, float str) {
		super(a, b, str);
	}

	protected void update() {
		if (b.distSq(a) > restLengthSquared) {
			//super.update();
			Vec delta = b.sub(a);
			// add minute offset to avoid div-by-zero errors
			float dist = delta.mag() + EPS;
			float normDistStrength = (dist - restLength) / (dist * (a.invWeight + b.invWeight)) * strength;
			if (!a.isLocked) {
				a.addSelf(delta.mult(normDistStrength * a.invWeight));	
			}
			if (!b.isLocked) {
				b.addSelf(delta.mult(-normDistStrength * b.invWeight));
			}
		}
	}
}
