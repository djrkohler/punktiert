package punktiert.GPUPhysics;

import punktiert.math.Vec;


/**
 * basic wander-algorythm
 *
 */

public class BWander implements BehaviorInterface {

	float wanderR, wanderD, change;

	public BWander(float wanderR, float wanderD, float change) {
		this.wanderR = wanderR;
		this.wanderD = wanderD;
		this.change = wanderR;
	}

	@Override
	public void apply(Particle p) {
		float wandertheta = (float) (Math.random() * 2 - 1) * change;
		Vec circleloc = p.getVelocity();
		circleloc.normalize();
		circleloc.multSelf(wanderD);
		circleloc.addSelf(p);
		Vec circleOffSet = new Vec(wanderR * (float) Math.cos(wandertheta), wanderR * (float) Math.sin(wandertheta), wanderR
				* (float) Math.sin(wandertheta / 5));
		circleOffSet.addSelf(circleloc);
		Vec f = (steer(circleOffSet, p));
		p.addForce(f);
	}

	public float getWanderR() {
		return wanderR;
	}

	public void setWanderR(float wanderR) {
		this.wanderR = wanderR;
	}

	public float getWanderD() {
		return wanderD;
	}

	public void setWanderD(float wanderD) {
		this.wanderD = wanderD;
	}

	public float getChange() {
		return change;
	}

	public void setChange(float change) {
		this.change = change;
	}

	private Vec steer(Vec target, Particle p) {
		Vec steer;
		Vec desired = target.sub(p);
		float d = desired.magSq();
		if (d > 0) {
			desired.normalize();
			desired.multSelf(3.0f);
			steer = desired.sub(p.getVelocity()).limit(.05f);
		} else {
			steer = new Vec();
		}
		return steer;
	}

}
