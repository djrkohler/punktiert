package punktiert.physics;
/**
 * Implements a string which will only enforce its rest length if the current distance is LESS than its rest length. 
 * </p> by Karsten Schmidt: toxi.physics.VerletMinDistanceSpring
 */
public class VSpringMinDistance extends VSpring {
	
	public VSpringMinDistance(VParticle a, VParticle b, float len, float str) {
		super(a, b, len, str);
	}
	
	public VSpringMinDistance(VParticle a, VParticle b, float str) {
		super(a, b, str);
	}

	protected void update() {
		if (b.distSq(a) < restLengthSquared) {
			super.update();
		}
	}
}
