package punktiert.physics;

import punktiert.math.Vec;


public class ConstraintNail implements BehaviorInterface {

	VParticle nail;
	
	public ConstraintNail(VParticle nail) {
		this.nail = nail;
		
	}
	
	@Override
	public void apply(VParticle p) {
		Vec vel = p.getVelocity();
		p.unlock();
		p.set(nail.x, nail.y, nail.z);
		p.setPreviousPosition(p.sub(vel));
		p.lock();
	}

	
	
}
