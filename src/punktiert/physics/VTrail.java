package punktiert.physics;

import java.util.ArrayList;

/**
 * adds the passed in VParticle to the List: Trail.particles; records a "Trail"
 * of the particle (head) movement </p> connected to the VParticle instance;
 */
public class VTrail implements BehaviorInterface {

	public VParticle myHead;
	public ArrayList<VParticle> particles;

	boolean reduce = false;

	/**
	 * a modular for adding at each (%) frame a particle to the trail
	 */
	float reductionFactor;
	int counter;
	int inPast;

	public VTrail(VParticle myHead) {
		this(myHead, 1, 100);
	}

	public VTrail(VParticle myHead, float reduct) {
		this(myHead, reduct, 100);
	}

	public VTrail(VParticle myHead, float reduct, int inPast) {

		this.myHead = myHead;
		this.reductionFactor = reduct;
		this.inPast = inPast;

		this.counter = 0;
		this.particles = new ArrayList<VParticle>();

		this.myHead.addBehavior(this);
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void apply(VParticle part) {
		counter++;
		if (counter % reductionFactor == 0) {
			particles.add(new VParticle(part));
		}
		if (inPast <= particles.size()) {
			particles.remove(0);
		}
	}

	public void remove(VParticle part) {
		particles.remove(part);
	}

	protected void reduceCvs() {
		reduce = true;

		@SuppressWarnings("unchecked")
		ArrayList<VParticle> tempList = (ArrayList<VParticle>) particles.clone();
		particles.clear();

		for (int i = 0; i < tempList.size(); i += reductionFactor) {
			VParticle part = (VParticle) tempList.get(i);
			particles.add(part);
		}
	}

	/**
	 * a modular for adding at each (%) frame a particle to the trail
	 */
	public float getReductionFactor() {
		return reductionFactor;
	}

	public int getInPast() {
		return inPast;
	}

	/**
	 * how many Particles in Past will be stored
	 * 
	 * @param num
	 */
	public void setInPast(int num) {
		inPast = num;
	}

	/**
	 * a modular for adding at each (%) frame a particle to the trail
	 */
	public void setreductionFactor(float red) {
		reductionFactor = red;
		reduceCvs();
	}
}
