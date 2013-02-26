package punktiert.physics;

import java.util.ArrayList;

public class ConstraintGroup extends VParticleGroup {

	ConstraintGroup() {
		super();
	}

	public VParticle returnIfFixed(VParticle pos, ArrayList particles) {
		for (int i = 0; i < particles.size(); i++) {
			VParticle other = (VParticle) particles.get(i);
			if (pos.equalsWithTolerance(other, .1f)) {
				return other;
			}
		}
		VParticle a = new VParticle(pos);
		return a;
	}

}
