package punktiert.math;

import java.util.ArrayList;
import java.util.HashMap;

import punktiert.physics.VParticle;
import punktiert.physics.VSpring;

/**
 * used for internal speed optimization
 */
@SuppressWarnings("serial")
public class HashMapSprings extends HashMap {

	public HashMapSprings(int capacity) {
		super(capacity);
	}

	public HashMapSprings() {
		super(1000000);
	}

	@SuppressWarnings("unchecked")
	public void insert(VSpring s) {

		VParticle hashKeyA = s.a;
		VParticle hashKeyB = s.b;
		if (get(hashKeyA) == null) {
			ArrayList<VSpring> springs = new ArrayList<VSpring>();
			springs.add(s);
			put(hashKeyA, springs);
		} else {
			ArrayList<VSpring> springs = (ArrayList<VSpring>) get(hashKeyA);
			springs.add(s);
		}
		if (get(hashKeyB) == null) {
			ArrayList<VSpring> springs = new ArrayList<VSpring>();
			springs.add(s);
			put(hashKeyB, springs);
		} else {
			ArrayList<VSpring> springs = (ArrayList<VSpring>) get(hashKeyB);
			springs.add(s);
		}
	}

	public void removeSpring(VSpring s) {
		VParticle hashKeyA = s.a;
		VParticle hashKeyB = s.b;
		this.remove(hashKeyA);
		this.remove(hashKeyB);
	}

}
