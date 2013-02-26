package punktiert.physics;

import punktiert.math.Vec;


/**
 * An example how you can set up your own agent by combining/ implementing different beaviors. 
 * Here the BSwarm and the VTrail Class are simply added to an extended VParticle class 
 * 
 */
public class VBoid extends VParticle {

	public BSwarm swarm;
	public VTrail trail;

	/**
	 * Creates a VBoid at position xy, z set to 0.0
	 * 
	 * @param x
	 * @param y
	 */
	public VBoid(float x, float y) {
		this(x, y, 0, 1, 1);
	}

	/**
	 * Creates a VBoid at position xyz
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public VBoid(float x, float y, float z) {
		this(x, y, z, 1, 1);
	}

	/**
	 * Creates a VBoid at position xyz with weight 1, default radius 1.0
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 */
	public VBoid(float x, float y, float z, float w) {
		this(x, y, z, w, 1);
	}

	/**
	 * Creates a VBoid at position xyz with weight , radius , default flocking values as in the Processing example "Flocking" by Daniel Shiffman
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 */
	public VBoid(float x, float y, float z, float w, float r) {
		super(x, y, z, w, r);
		this.swarm = new BSwarm(25, 50, 50, 1.5f, 1, 1, 2, 0.03f);
		addBehavior(swarm);

		this.trail = new VTrail(this);
	}

	/**
	 * Creates a VBoid at the position of the passed in vector
	 * 
	 * @param v position
	 */
	public VBoid(Vec v) {
		this(v.x, v.y, v.z, 1, 1);
	}

	/**
	 * Creates a VBoid at the position of the passed in vector
	 * 
	 * @param pos position
	 * @param vel velocity / previous position
	 */
	public VBoid(Vec pos, Vec vel) {
		super(pos, vel);
		this.swarm = new BSwarm(25, 50, 50, 1.5f, 1, 1, 2, 0.03f);
		addBehavior(swarm);

		this.trail = new VTrail(this);
	}
	
	/**
	 * Creates a VBoid at the position of the passed in vector, belonging to a specific group
	 * 
	 * @param pos position
	 * @param vel velocity / previous position
	 */
	public VBoid(Vec pos, Vec vel, VParticleGroup group) {
		super(pos, vel);
		this.swarm = new BSwarm(group);
		addBehavior(swarm);

		this.trail = new VTrail(this);
	}

}
