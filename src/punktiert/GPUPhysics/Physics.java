package punktiert.GPUPhysics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.syr.pcpratts.rootbeer.runtime.Kernel;
import edu.syr.pcpratts.rootbeer.runtime.Rootbeer;
import punktiert.math.*;
import punktiert.GPUPhysics.BehaviorInterface;
import punktiert.GPUPhysics.Particle;

/**
 * A particle physics engine using Verlet integration </p> based on:
 * http://en.wikipedia.org/wiki/Verlet_integration
 * http://www.teknikus.dk/tj/gdc2001.htm </p> this class builds on Karsten
 * Schmidt's toxi.physics.VerletPhysics class, http://toxiclibs.org ; </p>
 * OpenCL integration with the help of rotbeer (c) 2012 Phil Pratt-Szeliga, Syracuse University
 * Rootbeer is licensed under the MIT license,  https://github.com/pcpratts/rootbeer1
 * </p> punktiert written by Daniel Koehler - 2013 www.lab-eds.org for feedback please contact me at:
 * daniel@lab-eds.org
 */
@SuppressWarnings("unchecked")
public class Physics  {

	/**
	 * Boundig Box
	 */
	public BWorldBox box;

	private Rootbeer rootbeer;
	
	/**
	 * List of particles
	 */
	public List<Kernel> particles;
	/**
	 * List of spring
	 */
	public List<Kernel> springs;

	public ArrayList<Particle> constraints;

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
	public Physics() {
		this(80);
	}

	/**
	 * default neighborRange = 80; define this val, if your scene uses any
	 * bigger or extremely smaller influence radius for local neighbor sets
	 * 
	 * @param neighborRange
	 */
	public Physics(float neighborRange) {
		this.rootbeer = new Rootbeer();
		this.particles = new ArrayList<Kernel>(1000000);
		this.springs = new ArrayList<Kernel>(1000000);
		this.behaviors = new ArrayList<BehaviorInterface>(100);
		setNumIterations(10);
		setfriction(0);
	}

	/**
	 * Initializes an Verlet engine instance with bounding box, by default
	 * bouncing space
	 * 
	 */
	public Physics(Vec min, Vec max) {
		this(min, max, true);
	}

	/**
	 * Initializes an Verlet engine instance with bounding box, by default min
	 * Corner == zero; bouncing space
	 * 
	 */
	public Physics(float width, float height) {
		this(new Vec(), new Vec(width, height, 0), true);
	}

	/**
	 * Initializes an Verlet engine instance with bounding box
	 * 
	 */
	public Physics(Vec min, Vec max, boolean bounce) {
		this.rootbeer = new Rootbeer();
		this.particles = new ArrayList<Kernel>(1000000);
		this.springs = new ArrayList<Kernel>(1000000);
		this.behaviors = new ArrayList<BehaviorInterface>(100);
		setNumIterations(10);
		setfriction(0);
		this.box = new BWorldBox(min, max);
		box.setBounceSpace(bounce);
		this.behaviors.add((BehaviorInterface) box);

	}

	/**
	 * Initializes an Verlet engine instance
	 * 
	 */
	public Physics(int numParticles, int numSprings, int numBehaviors, int numIterations, float hashRadius) {
		this.rootbeer = new Rootbeer();
		this.particles = new ArrayList<Kernel>(numParticles);
		this.springs = new ArrayList<Kernel>(numSprings);
		this.behaviors = new ArrayList<BehaviorInterface>(numBehaviors);
		setNumIterations(numIterations);
		setfriction(0);

	}

	/**
	 * Adds a particle to the list
	 * 
	 * @param p
	 * @return itself
	 */
	public Particle addParticle(Particle p2) {
		Particle p = returnIfConstrained(p2);
		return p;
	}

	/**
	 * Adds a spring to the list
	 * 
	 * @param s
	 * @return itself
	 */
	public Spring addSpring(Spring s) {
		if (this.springs == null)
			this.springs = new ArrayList<Kernel>();

		if (getSpring(s.a, s.b) == null) {
			springs.add(s);
		}
		return s;
	}
	
	/**
	 * Adds a behavior to the list
	 * 
	 * @param behavior
	 */
	public void addBehavior(BehaviorInterface behavior) {
		behaviors.add(behavior);
	}

	public Particle addConstraint(Particle constraint) {
		if(constraints == null) constraints = new ArrayList<Particle>();
		constraints.add(constraint);
		return constraint;
	}
	
	public float getfriction() {
		return friction;
	}

	public int getNumIterations() {
		return numIterations;
	}
	
	public Particle getParticle(int index) {
		Particle p = returnIfConstrained(index);
		return p;
	}
	
public Particle getParticle(Particle particle) {
		Particle p = returnIfConstrained(particle);
		return p;
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
	public Spring getSpring(Vec a, Vec b) {
		for (Kernel k : springs) {
			Spring s = (Spring) k;
			if ((s.a == a && s.b == b) || (s.a == b && s.b == a)) {
				return s;
			}
		}

		return null;
	}

	/*
	public List<Particle> getParticles() {
		List<Particle> parts = new ArrayList<Particle>();
		parts.addAll((Collection<? extends Particle>) particles);
		return parts;
	}*/

	/*
	public List<Spring> getSprings() {
		List<Spring> sp = new ArrayList<Spring>();
		sp.addAll((Collection<? extends Spring>) springs);
		return sp;
	}*/

	/**
	 * get the count of how many springs are connected to A
	 * 
	 * @param a
	 *            particle 1
	 * @return count
	 */
	public int getnumConnected(Vec a) {
		int count = springs.size();
		return count;
	}

	public void setBox(Vec min, Vec max) {
		if (this.box == null) {
			this.box = new BWorldBox(min, max);
			this.behaviors.add((BehaviorInterface) box);
		} else {
			this.box.setMin(min);
			this.box.setMax(max);
		}
	}

	public void setBounceSpace(boolean bounce) {
		if (this.box != null) {
			this.box.setBounceSpace(bounce);
		}
	}

	public void setWrappedSpace(boolean wrap) {
		if (this.box != null) {
			this.box.setWrapSpace(wrap);
		}
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

	public Physics clear() {
		particles.clear();
		if (springs != null) {
			springs.clear();
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
	public boolean removeParticle(Particle p) {
		return particles.remove(p);
	}

	/**
	 * Removes a spring connector from the simulation instance.
	 * 
	 * @param s
	 *            spring to remove
	 * @return true, if the spring has been removed
	 */
	public boolean removeSpring(Spring s) {
		return springs.remove(s);
	}

	protected void updateParticles() {
		for (int i = 0; i < particles.size(); i++) {
			Kernel k = (Kernel) particles.get(i);
			Particle p = (Particle) k;
			for (BehaviorInterface b : behaviors) {
				b.apply(p);
			}
		}
		rootbeer.runAll(particles);
	}

	protected void updateSprings() {
		if (this.springs != null) {
			for (int i = numIterations; i > 0; i--) {
				rootbeer.runAll(springs);
			}
		}

	}

	/**
	 * Progresses the physics simulation by 1 time step and updates all forces
	 * and particle positions accordingly
	 */
	public final void update() {
		updateParticles();
		updateSprings();
	}

	protected Particle returnIfConstrained(Particle p) {
		if (this.constraints != null) {
			for (int i = 0; i < constraints.size(); i++) {
				Particle other = (Particle) constraints.get(i);
				if (p.equalsWithTolerance(other, .1f)) {
					return other;
				}
			}
		}
		Particle particle = returnIfDouble(p);
		return particle;
	}

	protected Particle returnIfConstrained(int index) {
		Particle p = (Particle) particles.get(index);
		for (int i = 0; i < constraints.size(); i++) {
			Particle other = (Particle) constraints.get(i);
			if (p.equalsWithTolerance(other, .1f)) {
				return other;
			}
		}
		return p;
	}

	protected Particle returnIfDouble(Particle p) {
		for (int i = 0; i < particles.size(); i++) {
			Particle other = (Particle) particles.get(i);
			if (p.equals(other)) {
				return other;
			}
		}
		particles.add(p);
		return p;
	}

	// end class
}
