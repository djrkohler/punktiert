package punktiert.physics;

import punktiert.math.Vec;

/**
 * Local Particle Behavior: compares the passed in Particle to its own list of
 * neighbors </p> addBehavior to the VParticle.behaviors; </p> swarm / flock
 * algorythm based on: Craig Reynold's Boids program to simulate the flocking
 * behavior of birds. Each boid steers itself based on rules of avoidance,
 * alignment, and coherence. </p> Java implementation Daniel Shiffman
 * (www.shiffman.net); Jose Sanchez (www.plethora-project.com)
 */
public class BSwarm implements BehaviorInterface {

	VParticleGroup group;
	float cohesionRadius, alignmentRadius, seperationRadius, cohesionScale, alignScale, seperationScale;
	float cohRadSquared, alRadSquared, sepRadSquared;
	float maxSpeed, maxForce;

	/**
	 * default Constructor input: rangeRadius for Seperation 25, rangeRadius for
	 * Alignment 25, rangeRadius for Cohesion 25, defaultScale: 1.5;1.0;1.0,
	 * default maxSpeed: 3.0, default maxForce .05
	 */
	public BSwarm() {
		this(25, 25, 20, 1.5f, 1.0f, 1.0f, 3.0f, .05f);
	}

	public BSwarm(VParticleGroup group) {
		this(group, 25, 25, 20, 1.5f, 1.0f, 1.0f, 3.0f, .05f);
	}

	public BSwarm(float seperationRadius, float alignmentRadius, float cohesionRadius, float seperationScale, float alignScale,
			float cohesionScale, float maxSpeed, float maxForce) {

		this.cohesionRadius = cohesionRadius;
		this.alignmentRadius = alignmentRadius;
		this.seperationRadius = seperationRadius;
		cohRadSquared = cohesionRadius * cohesionRadius;
		alRadSquared = alignmentRadius * alignmentRadius;
		sepRadSquared = seperationRadius * seperationRadius;
		this.cohesionScale = cohesionScale;
		this.alignScale = alignScale;
		this.seperationScale = seperationScale;
		this.maxSpeed = maxSpeed;
		this.maxForce = maxForce;
	}

	public BSwarm(VParticleGroup group, float seperationRadius, float alignmentRadius, float cohesionRadius, float seperationScale,
			float alignScale, float cohesionScale, float maxSpeed, float maxForce) {

		this.group = group;
		this.cohesionRadius = cohesionRadius;
		this.alignmentRadius = alignmentRadius;
		this.seperationRadius = seperationRadius;
		cohRadSquared = cohesionRadius * cohesionRadius;
		alRadSquared = alignmentRadius * alignmentRadius;
		sepRadSquared = seperationRadius * seperationRadius;
		this.cohesionScale = cohesionScale;
		this.alignScale = alignScale;
		this.seperationScale = seperationScale;
		this.maxSpeed = maxSpeed;
		this.maxForce = maxForce;
	}

	@Override
	public void apply(VParticle p) {
		Vec f = swarm(p);
		p.addForce(f);
	}

	protected Vec swarm(VParticle p) {

		// empty vectors to accumulate all locations
		Vec coh = new Vec();
		Vec sep = new Vec();
		Vec ali = new Vec();

		// counters for average division
		int countCoh = 0;
		int countAli = 0;
		int countSep = 0;

		// p.neighbors.removeAll(group.particles);

		for (VParticle neighbor : p.neighbors) {
			if (neighbor == p)
				continue;

			if (group != null) {
				if (group.particles.contains(neighbor))
					continue;
			}

			float d = p.distSq(neighbor);

			// COHESION:
			if (d < cohRadSquared) {
				coh.addSelf(neighbor);
				countCoh++;
			}
			// SEPARATION:
			if (d < sepRadSquared) {
				Vec diff = p.sub(neighbor);
				diff.normalizeTo(1.0f / d);
				sep.addSelf(diff);
				countSep++;
			}
			// ALIGNMENT:
			if (d < alRadSquared) {
				ali.addSelf(neighbor.getVelocity());
				countAli++;
			}
		}

		// COHESION:
		if (countCoh > 0) {
			coh.multSelf(1.0f / countCoh);
			coh = seek(coh, p);
			coh.limit(maxForce);

		}

		// SEPARATION:
		if (countSep > 0) {
			sep.multSelf(1.0f / countSep);
		}

		if (sep.magSq() > 0) {
			sep.normalizeTo(maxSpeed);
			sep.subSelf(p.getVelocity());
			sep.limit(maxForce);
		}

		// ALIGNMENT:
		if (countAli > 0) {
			ali.multSelf(1.0f / countAli);
		}
		if (ali.magSq() > 0) {
			ali.normalizeTo(maxSpeed);
			ali.subSelf(p.getVelocity());
			ali.limit(maxForce);
		}

		// ---------------------------------------------------

		sep.multSelf(seperationScale);
		coh.multSelf(cohesionScale);
		ali.multSelf(alignScale);

		Vec acc = new Vec();
		acc.addSelf(sep);
		acc.addSelf(ali);
		acc.addSelf(coh);

		return acc;
	}

	private Vec seek(Vec target, VParticle p) {
		Vec steer;
		Vec desired = target.sub(p);
		float d = desired.magSq();
		if (d > 0) {
			desired.normalize();
			desired.multSelf(maxSpeed);
			steer = desired.sub(p.getVelocity()).limit(maxForce);
		} else {
			steer = new Vec();
		}
		return steer;
	}

	public float getCohesionRadius() {
		return cohesionRadius;
	}

	public void setCohesionRadius(float cohesionRadius) {
		this.cohesionRadius = cohesionRadius;
		this.cohRadSquared = cohesionRadius * cohesionRadius;
	}

	public float getAlignRadius() {
		return alignmentRadius;
	}

	public void setAlignRadius(float alignmentRadius) {
		this.alignmentRadius = alignmentRadius;
		this.alRadSquared = alignmentRadius * alignmentRadius;
	}

	public float getSeperationRadius() {
		return seperationRadius;
	}

	public void setSeperationRadius(float seperationRadius) {
		this.seperationRadius = seperationRadius;
		this.sepRadSquared = seperationRadius * seperationRadius;
	}

	public float getCohesionScale() {
		return cohesionScale;
	}

	public void setCohesionScale(float cohesionScale) {
		this.cohesionScale = cohesionScale;
	}

	public float getAlignScale() {
		return alignScale;
	}

	public void setAlignScale(float alignScale) {
		this.alignScale = alignScale;
	}

	public float getSeperationScale() {
		return seperationScale;
	}

	public void setSeperationScale(float seperationScale) {
		this.seperationScale = seperationScale;
	}

	public float getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public float getMaxForce() {
		return maxForce;
	}

	public void setMaxForce(float maxForce) {
		this.maxForce = maxForce;
	}

}
