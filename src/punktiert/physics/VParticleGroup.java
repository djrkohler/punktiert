package punktiert.physics;

import java.util.ArrayList;
import java.util.Collection;

import punktiert.math.HashGrid;

/**
 * container for VParticle Groups
 * 
 */
public class VParticleGroup {

	public VPhysics physics;
	public ArrayList<VParticle> particles;
	public ArrayList<VSpring> springs;

	private HashGrid hashgrid;

	/**
	 * empty particle group
	 */
	public VParticleGroup() {
		this.springs = new ArrayList<VSpring>();
		this.particles = new ArrayList<VParticle>();
	}

	/**
	 * particle group with sorted particles according to their position
	 * 
	 * @param neighborRange
	 */
	public VParticleGroup(float neighborRange) {
		this.springs = new ArrayList<VSpring>();
		this.particles = new ArrayList<VParticle>();
		this.hashgrid = new HashGrid(neighborRange);
	}

	public VParticleGroup(ArrayList<VParticle> plist) {
		this.physics = new VPhysics();
		for (VParticle p : plist) {
			physics.addParticle(p);
		}
	}

	public VParticleGroup(VPhysics physics) {
		this.physics = physics;
	}

	public VParticleGroup(VPhysics physics, ArrayList<VParticle> plist) {
		this.physics = physics;
		for (VParticle p : plist) {
			physics.addParticle(p);
		}
	}

	public VParticleGroup(VPhysics physics, ArrayList<VParticle> plist, ArrayList<VSpring> slist) {
		this.physics = physics;
		for (VParticle p : plist) {
			physics.addParticle(p);
		}
		for (VSpring s : slist) {
			physics.addSpring(s);
		}
	}

	public void update() {
		if (hashgrid != null)
			hashgrid.updateAll();
		if (physics != null)
			physics.update();
		
	}

	public void addParticle(VParticle p) {
		if (physics != null)
			this.physics.addParticle(p);
		if (particles != null)
			this.particles.add(p);
	}

	public void addSpring(VSpring s) {
		if (physics != null)
			this.physics.addSpring(s);
		if (springs != null)
			this.springs.add(s);
	}

	public Collection<VParticle> getNeighbors(VParticle particle) {
		if (hashgrid == null && physics != null) {
			return physics.hashgrid.check(particle);
		}
		return hashgrid.check(particle);
	}

	public void removeParticle(VParticle p) {
		this.physics.removeParticle(p);
	}

	public void removeSpring(VSpring s) {
		this.physics.removeSpring(s);
	}
	
	public VParticle getParticle(int index) {
		VParticle p = null;
		if (physics != null)
			p = (VParticle) this.physics.particles.get(index);
		if (particles != null)
			p = (VParticle) this.particles.get(index);
		
		return p;
	}
	
	public VSpring getSpring(int index) {
		VSpring s = null;
		if (physics != null)
			s = this.physics.springs.get(index);
		if (springs != null)
			 s = this.springs.get(index);
		return s;
	}
	
	

}
