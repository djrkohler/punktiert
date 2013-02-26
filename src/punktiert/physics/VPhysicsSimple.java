package punktiert.physics;

import java.util.List;
import java.util.ArrayList;

import punktiert.math.Vec;
import punktiert.physics.VParticle;

/**
 * particle physics engine using Verlet integration </p> based on:
 * http://en.wikipedia.org/wiki/Verlet_integration
 * http://www.teknikus.dk/tj/gdc2001.htm </p> this class is more or less an
 * modification/ extension of Karsten Schmidt's toxi.physics.VerletPhysics
 * class, http://toxiclibs.org ; </p> for convenience combined physics for 2D /
 * 3D; some addons for speed; and extended behaviors </p> Written by Daniel
 * Koehler - 2012 www.lab-eds.org for feedback please contact me at:
 * daniel@lab-eds.org
 */
public class VPhysicsSimple {

	/**
	 * List of particles
	 */
	public ArrayList<VParticle> particles;
	/**
	 * List of spring
	 */
	public ArrayList<VSpring> springs;

	public ArrayList<VParticle> constraints;

	/**
	 * List of particle groups
	 */
	public ArrayList<VParticleGroup> groups;

	/**
	 * Default iterations for verlet solver = 50
	 */
	protected int numIterations;
	/**
	 * List of behaviors
	 */
	public List<BehaviorInterface> behaviors;

	/**
	 * Default friction = 0.0
	 */
	protected float friction;

	/**
	 * Initializes an Verlet engine instance
	 * 
	 */
	public VPhysicsSimple() {
		this.particles = new ArrayList<VParticle>();
		this.springs = new ArrayList<VSpring>();
		this.behaviors = new ArrayList<BehaviorInterface>(1);
		this.constraints = new ArrayList<VParticle>(10);
		setNumIterations(50);
		setfriction(0);
	}

	/**
	 * Adds a behavior to the list
	 * 
	 * @param behavior
	 */
	public void addBehavior(BehaviorInterface behavior) {
		behaviors.add(behavior);
	}

	/**
	 * Adds a particle to the list
	 * 
	 * @param p
	 * @return itself
	 */
	public VParticle addParticle(VParticle particle) {
		VParticle p = returnIfConstrained(particle);
		
		//particles.add(p);
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
			springs = new ArrayList<VSpring>();
		if (getSpring(s.a, s.b) == null) {
			springs.add(s);
		}
		return this;
	}

	public VParticle addConstraint(VParticle constraint) {
		constraints.add(constraint);
		return constraint;
	}

	/**
	 * Adds a group (or String) to the List without affecting the global
	 * particle calculation
	 * 
	 * @param g
	 *            VParticleGroup
	 */
	public void addGroup(VParticleGroup g) {
		if (this.groups == null)
			groups = new ArrayList<VParticleGroup>();
		groups.add(g);
	}

	public VPhysicsSimple clear() {
		particles.clear();
		springs.clear();
		return this;
	}

	public VParticle getParticle(int index) {
		VParticle p = returnIfConstrained(index);
		return p;
	}
	
	public VParticle getParticle(VParticle particle) {
		
		VParticle p = returnIfConstrained(particle);
		return p;
	}

	public float getfriction() {
		return friction;
	}

	public int getNumIterations() {
		return numIterations;
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
		for (VSpring s : springs) {
			if ((s.a == a && s.b == b) || (s.a == b && s.b == a)) {
				return s;
			}
		}
		return null;
	}

	/**
	 * get the count of how many springs are connected to A
	 * 
	 * @param a
	 *            particle 1
	 */
	public int getnumConnected(Vec a) {
		int count = 0;
		if (springs != null) {
			for (VSpring s : springs) {
				if ((s.a == a) || (s.b == a)) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * Removes a behavior from the simulation.
	 * 
	 * @param b
	 *            behavior to remove
	 * @return true, if removed successfully
	 */
	public boolean removeBehavior(BehaviorInterface b) {
		return behaviors.remove(b);
	}

	/**
	 * Removes a constraint from the simulation.
	 */
	public boolean removeConstraint(VParticle constraint) {
		return constraints.remove(constraint);
	}

	/**
	 * Removes a particle from the simulation.
	 * 
	 * @param p
	 *            particle to remove
	 * @return true, if removed successfully
	 */
	public boolean removeParticle(VParticle p) {
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
		return springs.remove(s);
	}

	/**
	 * Removes a spring connector and its both end point particles from the
	 * simulation
	 * 
	 * @param s
	 *            spring to remove
	 * @return true, only if spring AND particles have been removed successfully
	 */
	public boolean removeSpringElements(VSpring s) {
		if (removeSpring(s)) {
			return (removeParticle(s.a) && removeParticle(s.b));
		}
		return false;
	}

	/**
	 * Removes a particle group from the simulation instance.
	 * 
	 * @param g
	 *            to remove
	 * @return true, if the spring has been removed
	 */
	public boolean removeGroup(VParticleGroup g) {
		return groups.remove(g);
	}

	public void setfriction(float friction) {
		this.friction = friction;
	}

	/**
	 * @param numIterations
	 *            the numIterations to set
	 */
	public void setNumIterations(int numIterations) {
		this.numIterations = numIterations;
	}

	/**
	 * Updates all particle positions
	 */

	protected void updateParticles() {

		for (int i = 0; i < particles.size(); i++) {
			VParticle p = particles.get(i);
			if (p.neighbors == null) {
				p.neighbors = particles;
			}
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
	public void update() {
		updateParticles();
		updateSprings();

		if (groups != null) {
			for (VParticleGroup g : groups) {
				g.update();
			}
		}
	}

	protected VParticle returnIfConstrained(VParticle p) {
		for (int i = 0; i < constraints.size(); i++) {
			VParticle other = (VParticle) constraints.get(i);
			if (p.equalsWithTolerance(other, .1f)) {
				return other;
			}
		}
		VParticle particle = returnIfDouble(p);
		
		return particle;
	}

	protected VParticle returnIfConstrained(int index) {
		VParticle p = particles.get(index);
		for (int i = 0; i < constraints.size(); i++) {
			VParticle other = (VParticle) constraints.get(i);
			if (p.equalsWithTolerance(other, .1f)) {
				return other;
			}
		}
		return p;
	}

	protected VParticle returnIfDouble(VParticle p) {
		
		for (int i = 0; i < particles.size(); i++) {
			VParticle other = (VParticle) particles.get(i);
			if (p.equals(other)) {
				return other;
			}
		}
		//if (!particles.contains(p) && !constraints.contains(p)) particles.add(p);
		particles.add(p);
		return p;
	}

	// end class
}
