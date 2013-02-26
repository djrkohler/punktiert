package punktiert.physics;

import java.util.ArrayList;
import punktiert.math.Vec;

/**
 * Utility builder/grouping/management class to connect a set of particles into a physical string/thread.
 * </p> based on Karsten Schmidt's: toxi.physics.ParticleString </p>
 * 
 */

public class VParticleString extends VParticleGroup {

	/**
	 * Takes a list of already created particles and connects them into a continuous string using springs.
	 * 
	 * @param physics existing physics engine instance
	 * @param plist particle list
	 * @param strength spring strength
	 */
	public VParticleString(VPhysics physics, ArrayList<VParticle> plist, float strength) {
		super();
		this.physics = physics;
		particles.addAll(plist);
		VParticle prev = null;
		for (VParticle p : particles) {
			//if the particleList was not already added to the engine
			physics.addParticle(p);
			if (prev != null) {
				VSpring s = new VSpring(prev, p, prev.dist(p), strength);
				springs.add(s);
				physics.addSpring(s);
			}
			prev = p;
		}
	}

	/**
	 * Creates a number of particles along a line and connects them into a string using springs.
	 * 
	 * @param physics existing physics engine
	 * @param pos start position
	 * @param step step direction & distance between successive particles
	 * @param num number of particles
	 * @param mass particle mass
	 * @param strength spring strength
	 */
	public VParticleString(VPhysics physics, Vec pos, Vec step, int num, float mass, float strength) {
		super();
		this.physics = physics;
		float len = step.mag();
		VParticle prev = null;
		pos = pos.copy();
		for (int i = 0; i < num; i++) {
			VParticle p = new VParticle(pos.copy(), mass);
			particles.add(p);
			physics.particles.add(p);
			if (prev != null) {
				VSpring s = new VSpring(prev, p, len, strength);
				springs.add(s);
				physics.addSpring(s);
			}
			prev = p;
			pos.addSelf(step);
		}
		//physics.addGroup(this);
	}

	/**
	 * Creates a number of particles between a start and an end VParticle and connects them into a string using springs.
	 * 
	 * @param physics physics engine
	 * @param a start position
	 * @param b end position
	 * @param resolution subdivision resolution
	 * @param strength spring strength
	 */
	public VParticleString(VPhysics physics, VParticle a, VParticle b, float resolution, float strength) {
		super();
		this.physics = physics;
		subdivide(a, b, resolution, strength);
	}

	protected void subdivide(VParticle a, VParticle b, float res, float strength) {

		float length = a.dist(b);
		float resolution = (int) (length * res);
		float divLength = length / (resolution - 1);

		for (int i = 0; i <= resolution; i++) {
			VParticle p = null;
			if (i == resolution) {
				p = b;
			} else {
				p = new VParticle(a.interpolateTo(b, (1.0f / resolution) * i));
			}
			if (i == 0) {
				physics.addParticle(a);
				particles.add(a);
			} else if (i == resolution) {
				physics.addParticle(b);
				particles.add(b);
			} else {
				physics.addParticle(p);
				particles.add(p);
			}
			// connect each particle to its previous neighbour
			if (i > 0) {
				VParticle q = (VParticle) particles.get(i - 1);
				VSpring s = new VSpring(p, q, divLength, strength);
				physics.addSpring(s);
				springs.add(s);
			}
		}
	}
	
	

	/**
	 * Removes the entire string from the physics simulation, incl. all of its particles & springs.
	 */
	public void clear() {
		for (VSpring s : springs) {
			physics.removeSpringElements(s);
		}
		particles.clear();
		springs.clear();
	}

	/**
	 * Returns the first particle of the string.
	 * 
	 * @return first particle
	 */
	public VParticle getHead() {
		return particles.get(0);
	}

	/**
	 * Returns last particle of the string.
	 * 
	 * @return last particle
	 */
	public VParticle getTail() {
		return particles.get(particles.size() - 1);
	}
}
