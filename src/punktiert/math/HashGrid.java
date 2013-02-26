package punktiert.math;

import java.util.ArrayList;
import java.util.HashMap;

//import javolution.util.FastList;
//import javolution.util.FastMap;
import punktiert.physics.*;

/**
 * used for internal speed optimization
 */
public class HashGrid {
	@SuppressWarnings("rawtypes")
	HashMap<String, ArrayList> H;
	ArrayList<VParticle> particles;
	private float bucketSize;

	@SuppressWarnings("rawtypes")
	public HashGrid(float bucketSize) {
		this.bucketSize = bucketSize;
		H = new HashMap<String, ArrayList>(100000);
		particles = new ArrayList<VParticle>();
	}

	int getXr(Vec pos) {
		return (int) (Math.floor(pos.x / bucketSize));
	}

	int getYr(Vec pos) {
		return (int) (Math.floor(pos.y / bucketSize));
	}

	String getKey(Vec pos) {
		return Float.toString(getXr(pos)) + Float.toString(getYr(pos));
	}
	
	public void add(VParticle p) {
		particles.add(p);
		insert(p);
	}

	public void clear() {
		H.clear();
	}

	public void insert(VParticle p) {

		String hashKey = getKey(p);
		if (H.get(hashKey) == null) {
			ArrayList<VParticle> vl = new ArrayList<VParticle>();
			vl.add(p);
			H.put(hashKey, vl);
		} else {
			ArrayList<VParticle> vl = H.get(hashKey);
			vl.add(p);
		}
	}

	public void updateAll() {
		H.clear();
		for (int i = 0; i < particles.size(); i++) {
			VParticle p = particles.get(i);
			insert(p);
		}
	}

	public ArrayList<VParticle> check(Vec pos) {

		int Xr = getXr(pos);
		int Yr = getYr(pos);
		String[] keys = new String[9];
		int in = 0;
		for (int xr = Xr - 1; xr <= Xr + 1; xr++) {
			for (int yr = Yr - 1; yr <= Yr + 1; yr++) {
				//Vec2D c = new Vec2D(xr, yr);
				keys[in++] = Float.toString(xr) + Float.toString(yr);
			}
		}
		ArrayList<VParticle> checked = new ArrayList<VParticle>();
		for (int i = 0; i < keys.length; i++) {
			if (H.get(keys[i]) != null)
				checked.addAll(H.get(keys[i]));
		}
		return checked;
	}
	
	public void remove(VParticle p) {
		String hashKey = getKey(p);
		H.remove(hashKey);
		particles.remove(p);
	}

	public int size() {
		return H.size();
	}
	
	public void setBucketSize(float rad) {
		this.bucketSize = rad;
	}

}
