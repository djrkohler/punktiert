package punktiert.physics;


import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

import punktiert.math.Vec;

/**
 * An individual 3D particle for use by the VPhysics and VSpring classes. the functionality can be extended by applying different behaviors </p> this class is
 * more or less an modification/ extension of Karsten Schmidt's toxi.physics.VerletPartcle class 
 */
public class VParticle extends Vec {

	protected Vec prev, temp;
	protected boolean isLocked;

	public List<BehaviorInterface> behaviors;
	
	public Collection<VParticle> neighbors;

	/**
	 * Particle weight, default = 1
	 */
	protected float weight, invWeight;
	/**
	 * Particle radius, default = 1
	 */
	public float radius;
	/**
	 * Particle friction, default = 0;
	 */
	protected float friction;

	protected Vec force = new Vec();
	
	/**
	 * Creates particle at position zero
	 * 
	 * @param x
	 * @param y
	 */
	public VParticle() {
		this(0,0, 0.0f, 1, 1);
	}

	/**
	 * Creates particle at position xy, z set to 0.0
	 * 
	 * @param x
	 * @param y
	 */
	public VParticle(float x, float y) {
		this(x, y, 0.0f, 1, 1);
	}

	/**
	 * Creates particle at position xyz
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public VParticle(float x, float y, float z) {
		this(x, y, z, 1, 1);
	}

	/**
	 * Creates particle at position xyz with weight w
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 */
	public VParticle(float x, float y, float z, float w) {
		this(x, y, z, w, 1);
	}

	/**
	 * Creates particle at position xyz with weight w and radius r
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 */
	public VParticle(float x, float y, float z, float w, float r) {
		super(x, y, z);
		prev = new Vec(x, y, z);
		temp = new Vec();
		setWeight(w);
		setRadius(r);
	}

	/**
	 * Creates particle at the position of the passed in vector
	 * 
	 * @param v position
	 */
	public VParticle(Vec v) {
		this(v.x, v.y, v.z, 1, 1);
	}
	/**
	 * Creates particle at the position of the passed in vector
	 * 
	 * @param pos position
	 * @param vel velocity / previous position
	 */
	public VParticle(Vec pos, Vec vel) {
		super(pos.x, pos.y, pos.z);
		prev = new Vec();
		temp = new Vec();
		this.setVelocity(vel);
		setWeight(1);
		setRadius(1);
		setFriction(0);
	}
	

	/**
	 * Creates particle with weight w at the position of the passed in vector
	 * 
	 * @param v position
	 * @param w weight
	 */
	public VParticle(Vec v, float w) {
		this(v.x, v.y, v.z, w, 1);
	}

	/**
	 * Creates particle with weight w and radius r at the position of the passed in vector
	 * 
	 * @param v position
	 * @param w weight
	 */
	public VParticle(Vec v, float w, float r) {
		this(v.x, v.y, v.z, w, r);
	}
	/**
	 * Creates particle with weight w and radius r at the position of the passed in vector
	 * 
	 * @param v position
	 * @param w weight
	 */
	public VParticle(Vec pos, Vec v, float w, float r) {
		super(pos.x, pos.y, pos.z);
		prev = new Vec();
		temp = new Vec();
		this.setVelocity(v);
		setWeight(w);
		setRadius(r);
		setFriction(0);
	}

	/**
	 * Creates a copy of the passed in particle
	 * 
	 * @param p
	 */
	public VParticle(VParticle p) {
		this(p.x, p.y, p.z, p.weight, p.radius);
	}

	/**
	 * Adds the given behavior to the list of behaviors applied to this particle at each step.
	 */
	public VParticle addBehavior(BehaviorInterface behavior) {
		if (behaviors == null) {
			behaviors = new ArrayList<BehaviorInterface>(1);
		}
		behaviors.add(behavior);
		return this;
	}
	
	/**
	 * Adds the given constraint to the list of constraints applied to this particle at each step.
	 * A constraint is applied always wether the particle is locked or not
	 */
	public VParticle addConstraint(BehaviorInterface constraint) {
		if (behaviors == null) {
			behaviors = new ArrayList<BehaviorInterface>(1);
		}
		behaviors.add(constraint);
		return this;
	}
	

	public VParticle addForce(Vec f) {
		force.addSelf(f);
		return this;
	}

	public VParticle addVelocity(Vec v) {
		prev.subSelf(v);
		return this;
	}

	public void applyBehaviors() {
		if (behaviors != null) {
			for (BehaviorInterface b : behaviors) {
				b.apply(this);
			}
		}
	}

	protected void applyForce() {
		temp.set(this);
		
		addSelf(sub(prev).addSelf(force.mult(weight)));
		prev.set(temp.copy());
		
		force.clear();
	}

	
	public VParticle clearForce() {
		force.clear();
		return this;
	}

	public VParticle clearVelocity() {
		prev.set(this.x(), this.y(), this.z());
		return this;
	}

	/**
	 * @return the inverse weight (1/weight)
	 */
	public final float getInvWeight() {
		return invWeight;
	}

	/**
	 * Returns the particle's position at the most recent time step.
	 * 
	 * @return previous position
	 */
	public Vec getPreviousPosition() {
		return prev;
	}

	public float getRadius() {
		return radius;
	}
	
	public float getFriction() {
		return friction;
	}

	public Vec getVelocity() {
		return this.sub(prev);
	}

	/**
	 * @return the weight
	 */
	public final float getWeight() {
		return weight;
	}

	/**
	 * @return true, if particle is locked
	 */
	public final boolean isLocked() {
		return isLocked;
	}

	/**
	 * Locks/immobilizes particle in space
	 * 
	 * @return itself
	 */
	public VParticle lock() {
		isLocked = true;
		return this;
	}

	public boolean removeBehavior(BehaviorInterface b) {
		return behaviors.remove(b);
	}

	public VParticle scaleVelocity(float scl) {
		prev.interpolateToSelf(this, 0f+ scl);
		
		return this;
	}

	public void setNeighbors(Collection<VParticle> col) {
		if (neighbors == null) {
			neighbors = new ArrayList<VParticle>();
		}
		neighbors.clear();
		neighbors.addAll(col);
	}

	public VParticle setPreviousPosition(Vec p) {
		prev.set(p.x(), p.y(), p.z());
		return this;
	}
	
	public void setFriction(float friction) {
		this.friction = friction;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public void setVelocity(Vec vel) {
		prev.set(this.sub(vel));
	}

	public void setWeight(float w) {
		weight = w;
		invWeight = 1f / w;
	}

	/**
	 * Unlocks particle again
	 * 
	 * @return itself
	 */
	public VParticle unlock() {
		clearVelocity();
		isLocked = false;
		return this;
	}

	/**
	 * applies Behaviors and Force on the particles position. called automatically inherent from the Vphysics class
	 */
	public void update() {
		
		if (!isLocked) {
			applyBehaviors();
			applyForce();
		}
		scaleVelocity(friction);
	}

	//end class
}
