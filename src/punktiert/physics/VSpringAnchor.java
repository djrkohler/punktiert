package punktiert.physics;

import punktiert.math.Vec;


/**
 * Implements a string which will only enforce its rest length, if the current distance from the original position is MORE than its rest length. Behaves like an
 * Anchor.
 */
public class VSpringAnchor extends VSpring {

	public VSpringAnchor(VParticle a, float len, float str) {
		super(new VParticle(a), a, len, str);
	}
	
	public VSpringAnchor(VParticle a, VParticle anchor, float len, float str) {
		super(anchor, a, len, str);
	}

	public void setAnchor(Vec anchor) {
		this.a = new VParticle(anchor.copy());
	}

	public VParticle getAnchor() {
		return this.a;
	}

	public VParticle getParticle() {
		return this.b;
	}

	public void setParticle(VParticle b) {
		this.b = b;
	}

}
