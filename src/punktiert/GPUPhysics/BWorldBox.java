package punktiert.GPUPhysics;

import punktiert.math.Vec;

/**
 * can be used as a framing wrap- or bouncing box for your physics engine or
 * VParticle </p> for convinience use one of the VPhysics constructors
 */
public class BWorldBox implements BehaviorInterface {

	protected Vec min, max;
	boolean wrap = false;
	boolean bounce = false;
	boolean flatten = false;

	/**
	 * constructor input: minimal Vector/ Corner, max Vector/ Corner of your
	 * BoundingBox
	 * 
	 * @param _min
	 * @param _max
	 */
	public BWorldBox(Vec _min, Vec _max) {
		this.min = _min;
		this.max = _max;
		if (min.z == max.z)
			flatten = true;
	}

	public BWorldBox(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		this.min = new Vec(minX, minY, minZ);
		this.max = new Vec(maxX, maxY, maxZ);
		if (min.z == max.z)
			flatten = true;
	}

	public void apply(Particle p) {
		if (wrap) {

			if (p.x < min.x) {
				Vec vel = p.getVelocity();
				p.x = max.x;
				p.setPreviousPosition(p.sub(vel));
			}
			if (p.y < min.y) {
				Vec vel = p.getVelocity();
				p.y = max.y;
				p.setPreviousPosition(p.sub(vel));
			}
			if (p.z < min.z && !flatten) {
				Vec vel = p.getVelocity();
				p.z = max.z;
				p.setPreviousPosition(p.sub(vel));
			}
			if (p.x > max.x) {
				Vec vel = p.getVelocity();
				p.x = min.x;
				p.setPreviousPosition(p.sub(vel));
			}
			if (p.y > max.y) {
				Vec vel = p.getVelocity();
				p.y = min.y;
				p.setPreviousPosition(p.sub(vel));
			}
			if (p.z > max.z && !flatten) {
				Vec vel = p.getVelocity();
				p.z = min.z;
				p.setPreviousPosition(p.sub(vel));
			}

		} 
		if (bounce) {
			if (p.x <= min.x) {
				Vec vel = p.getVelocity();
				p.x = min.x+1;
				vel.x *= -1;
				//p.setPreviousPosition(vel);
				p.setPreviousPosition(p.sub(vel));
				p.setVelocity(vel);
			}
			if (p.y <= min.y) {
				Vec vel = p.getVelocity();
				p.y = min.y+1;
				//p.setPreviousPosition(vel);
				vel.y *= -1;
				p.setPreviousPosition(p.sub(vel));
				p.setVelocity(vel);

			}
			if (p.z <= min.z && !flatten) {
				Vec vel = p.getVelocity();
				p.z = min.z+1;
				//p.setPreviousPosition(vel);
				vel.z *= -1;
				p.setPreviousPosition(p.sub(vel));
				p.setVelocity(vel);
			}
			if (p.x >= max.x) {

				Vec vel = p.getVelocity();
				p.x = max.x-1;
				//p.setPreviousPosition(vel);
				vel.x *= -1;
				p.setPreviousPosition(p.sub(vel));
				p.setVelocity(vel);
			}
			if (p.y >= max.y) {
				Vec vel = p.getVelocity();
				p.y = max.y-1;
				//p.setPreviousPosition(vel);
				vel.y *= -1;
				p.setPreviousPosition(p.sub(vel));
				p.setVelocity(vel);
			}
			if (p.z >= max.z && !flatten) {
				Vec vel = p.getVelocity();
				p.z = max.z-1;
				//p.setPreviousPosition(vel);
				vel.z *= -1;
				p.setPreviousPosition(p.sub(vel));
				p.setVelocity(vel);
			}
		} else {

		}
	}

	/**
	 * @return the min Corner of the BoundingBox
	 */
	public Vec getMin() {
		return min;
	}

	/**
	 * @return the max Corner of the BoundingBox
	 */
	public Vec getMax() {
		return max;
	}

	/**
	 * set true, if the Particles should reappear on the other side of the
	 * BoundingBoxSpace
	 */
	public void setWrapSpace(boolean wrap) {
		this.wrap = wrap;
		this.bounce = !wrap;
	}

	/**
	 * set true, if the Particles should reappear bounce on the borders of the
	 * BoundingBoxSpace
	 */
	public void setBounceSpace(boolean bounce) {
		this.bounce = bounce;
		this.wrap = !bounce;
	}

	/**
	 * @param force
	 *            the min Corner to set
	 */
	public void setMin(Vec min) {
		this.min = min;
	}

	/**
	 * @param force
	 *            the max Corner to set
	 */
	public void setMax(Vec max) {
		this.max = max;
	}
}
