package punktiert.physics;

import java.util.ArrayList;

import punktiert.math.*;
import punktiert.physics.VParticle;

/**
 * A particle physics engine using Verlet integration </p> based on:
 * http://en.wikipedia.org/wiki/Verlet_integration
 * http://www.teknikus.dk/tj/gdc2001.htm </p> this class is more or less an
 * modification/ extension of Karsten Schmidt's toxi.physics.VerletPhysics
 * class, http://toxiclibs.org ; </p> for convenience combined physics for 2D /
 * 3D; some addons for speed; and extended behaviors </p> </p> Written by Daniel
 * Koehler - 2012 www.lab-eds.org for feedback please contact me at:
 * daniel@lab-eds.org
 */
@SuppressWarnings("unchecked")
public class VPhysics extends VPhysicsSimple {

	protected HashGrid hashgrid;
	/**
	 * Boundig Box
	 */
	public BWorldBox box;

	protected HashMapSprings springMap;

	/**
	 * Initializes an Verlet engine instance
	 * 
	 */
	public VPhysics() {
		super();
		this.hashgrid = new HashGrid(80);
	}
/**
 * default neighborRange = 80; define this val, if your scene uses any bigger or extremely smaller influence radius for local neighbor sets
 * @param neighborRange
 */
	public VPhysics(float neighborRange) {
		super();
		this.hashgrid = new HashGrid(neighborRange);
	}

	/**
	 * Initializes an Verlet engine instance with bounding box, by default
	 * bouncing space
	 * 
	 */
	public VPhysics(Vec min, Vec max) {
		this(min, max, true);
	}

	/**
	 * Initializes an Verlet engine instance with bounding box, by default min
	 * Corner == zero; bouncing space
	 * 
	 */
	public VPhysics(float width, float height) {
		this(new Vec(), new Vec(width, height, 0), true);
	}

	/**
	 * Initializes an Verlet engine instance with bounding box
	 * 
	 */
	public VPhysics(Vec min, Vec max, boolean bounce) {
		super();
		this.box = new BWorldBox(min, max);
		box.setBounceSpace(bounce);
		box.setWrapSpace(!bounce);
		this.behaviors.add(box);
		this.hashgrid = new HashGrid(80);

	}

	/**
	 * Adds a particle to the list
	 * 
	 * @param p
	 * @return itself
	 */
	public VParticle addParticle(VParticle particle) {
		VParticle p = returnIfConstrained(particle);
		particles.add(p);
		hashgrid.add(p);
		return p;
	}

	/**
	 * Adds a spring to the list
	 * 
	 * @param s
	 * @return itself
	 */
	public VPhysicsSimple addSpring(VSpring s) {
		if (this.springs == null)
			this.springs = new ArrayList<VSpring>();
		this.springMap = new HashMapSprings(particles.size() * 4);
		if (getSpring(s.a, s.b) == null) {
			springs.add(s);
			this.springMap.insert(s);
		}
		return this;
	}

	/**
	 * Attempts to find the spring element between the 2 particles supplied
	 * 
	 * @param a
	 *            particle 1
	 * @param b
	 *            particle 2
	 * @return spring instance, or null if not found
	 */
	public VSpring getSpring(Vec a, Vec b) {

		if (springMap.containsKey(a)) {
			ArrayList<VSpring> aSprings = (ArrayList<VSpring>) springMap.get(a);
			for (VSpring s : aSprings) {
				if ((s.a == a && s.b == b) || (s.a == b && s.b == a)) {
					return s;
				}
			}
		}
		return null;
	}

	/**
	 * get the count of how many springs are connected to A
	 * 
	 * @param a
	 *            particle 1
	 * @return count
	 */
	public int getnumConnected(Vec a) {
		int count = 0;
		if (springMap.containsKey(a)) {
			ArrayList<VSpring> aSprings = (ArrayList<VSpring>) springMap.get(a);
			count = aSprings.size();
			return count;
		}
		return count;
	}

	public void setBox(Vec min, Vec max) {
		if (this.box == null) {
			this.box = new BWorldBox(min, max);
		} else {
			this.box.setMin(min);
			this.box.setMax(max);
		}
	}

	public VPhysics clear() {
		particles.clear();
		hashgrid.clear();
		if (springs != null) {
			springs.clear();
			springMap.clear();
		}
		return this;
	}

	/**
	 * Removes a particle from the simulation.
	 * 
	 * @param p
	 *            particle to remove
	 * @return true, if removed successfully
	 */
	public boolean removeParticle(VParticle p) {
		hashgrid.remove(p);
		return particles.remove(p);
	}

	/**
	 * Removes a spring connector from the simulation instance.
	 * 
	 * @param s
	 *            spring to remove
	 * @return true, if the spring has been removed
	 */
	public boolean removeSpring(VSpring s) {
		springMap.removeSpring(s);
		return springs.remove(s);
	}

	protected void updateParticles() {

		for (int i = 0; i < particles.size(); i++) {
			VParticle p = particles.get(i);
			p.setNeighbors(hashgrid.check(p));

			for (BehaviorInterface b : behaviors) {
				b.apply(p);
			}
		}
		for (int i = 0; i < particles.size(); i++) {
			VParticle p = particles.get(i);
			p.scaleVelocity(friction);
			p.update();
		}
	}

	protected void updateSprings() {
		// int spSize = springs.size();
		if (this.springs != null) {
			for (int i = numIterations; i > 0; i--) {

				for (int j = 0; j < springs.size(); j++) {

					VSpring s = springs.get(j);
					s.update();
				}
			}
		}
	}

	/**
	 * Progresses the physics simulation by 1 time step and updates all forces
	 * and particle positions accordingly
	 */
	public final void update() {
		hashgrid.updateAll();
		updateParticles();
		updateSprings();
	}

	// end class
}
