package punktiert.GPUPhysics;

/**
 * abstract interface for implementing particle behaviors
 * </p> implement this interface for any new behavior
 * </p> by Karsten Schmidt: toxi.physics.behaviors.ParticleBehavior
 */
public interface BehaviorInterface{
	/**
	 * Applies the behavior to the passed in particle. The method is assumed to manipulate the given instance directly.
	 * 
	 * @param p particle
	 */	
	public void apply(Particle p);
}
