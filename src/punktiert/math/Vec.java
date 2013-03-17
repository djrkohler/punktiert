package punktiert.math;

import java.util.Random;
import javax.xml.bind.annotation.XmlAttribute;

import processing.core.PVector;

/**
 * Vector Class based on Karsten Schmidt's toxi.geom.Vec3D class
 * some simplifications, no difference between 2D/3D; method names analogue to PVector; takes PVectors as input
 * whereas this class is not extending the PVector class, because of PVectors extensive use of static methods
 * @author Daniel
 */

public class Vec implements Cloneable {

	/** X coordinate. */
	@XmlAttribute(required = true)
	public float x;

	/** Y coordinate. */
	@XmlAttribute(required = true)
	public float y;

	/** Z coordinate. */
	@XmlAttribute(required = true)
	public float z;

	/**
	 * Creates a new zero vector.
	 */
	public Vec() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	/**
	 * Creates a new vector with the given coordinates.
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param z
	 *            the z
	 */
	public Vec(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Creates a new vector in XY-Plane
	 * 
	 * @param x
	 * @param y
	 */
	public Vec(float x, float y) {
		this.x = x;
		this.y = y;
		this.z = 0;
	}

	public Vec(float[] v) {
		this.x = v[0];
		this.y = v[1];
		this.z = v[2];
	}

	/**
	 * Creates a new vector with the coordinates of the given vector.
	 * 
	 * @param v
	 *            vector to be copied
	 */
	public Vec(Vec v) {
		this.x = v.x();
		this.y = v.y();
		this.z = v.z();
	}

	/**
	 * Creates a new vector with the coordinates of the given vector.
	 * 
	 * @param v
	 *            : vector to be copied
	 */
	public Vec(PVector v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}

	/**
	 * Absolute value
	 * 
	 * @return the vec3 d
	 */
	public final Vec abs() {
		x = Math.abs(x);
		y = Math.abs(y);
		z = Math.abs(z);
		return this;
	}

	/**
	 * returns the sum of this vector and the input
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	public final Vec add(float a, float b, float c) {
		return new Vec(x + a, y + b, z + c);
	}

	public final Vec add(Vec v) {
		return new Vec(x + v.x, y + v.y, z + v.z);
	}

	public final Vec add(PVector v) {
		return new Vec(x + v.x, y + v.y, z + v.z);
	}

	/**
	 * Adds vector {a,b,c} and overrides coordinates with result.
	 * 
	 * @param a
	 *            X coordinate
	 * @param b
	 *            Y coordinate
	 * @param c
	 *            Z coordinate
	 * 
	 * @return itself
	 */
	public final Vec addSelf(float a, float b, float c) {
		x += a;
		y += b;
		z += c;
		return this;
	}

	public final Vec addSelf(Vec v) {
		x += v.x;
		y += v.y;
		z += v.z;
		return this;
	}

	public final Vec addSelf(PVector v) {
		x += v.x;
		y += v.y;
		z += v.z;
		return this;
	}

	public final float angleBetween(Vec v) {
		return (float) Math.acos(dot(v));
	}

	public final float angleBetween(PVector v) {
		return (float) Math.acos(x * v.x + y * v.y + z * v.z);
	}

	public final float angleBetween(Vec v, boolean forceNormalize) {
		float theta;
		if (forceNormalize) {
			theta = getNormalized().dot(v.getNormalized());
		} else {
			theta = dot(v);
		}
		return (float) Math.acos(theta);
	}

	/**
	 * Sets all vector components to 0.
	 * 
	 * @return itself
	 */
	public final Vec clear() {
		x = y = z = 0;
		return this;
	}

	/**
	 * returns if the length of this vector is smaller (-1), equal (0), or
	 * bigger (+1) then the input vector
	 * 
	 * @param v
	 * 
	 * @return
	 */
	public int compareTo(Vec v) {
		if (x == v.x() && y == v.y() && z == v.z()) {
			return 0;
		}
		float a = magSq();
		float b = v.magSq();
		if (a < b) {
			return -1;
		}
		return +1;
	}

	public Vec copy() {
		try {
			Vec v = (Vec) this.clone();
			return v;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return new Vec();
		}

	}

	public final Vec cross(Vec v) {
		return new Vec(y * v.z - v.y * z, z * v.x - v.z * x, x * v.y - v.x * y);
	}

	public final Vec cross(PVector v) {
		return new Vec(y * v.z - v.y * z, z * v.x - v.z * x, x * v.y - v.x * y);
	}

	public final Vec crossInto(Vec v, Vec result) {
		final float vx = v.x();
		final float vy = v.y();
		final float vz = v.z();
		result.x = y * vz - vy * z;
		result.y = z * vx - vz * x;
		result.z = x * vy - vx * y;
		return result;
	}

	/**
	 * Calculates cross-product with vector v. The resulting vector is
	 * perpendicular to both the current and supplied vector and overrides the
	 * current.
	 * 
	 * @param v
	 *            the v
	 * 
	 * @return itself
	 */
	public final Vec crossSelf(Vec v) {
		final float cx = y * v.z - v.y * z;
		final float cy = z * v.x - v.z * x;
		z = x * v.y - v.x * y;
		y = cy;
		x = cx;
		return this;
	}

	public final float dist(Vec v) {
		if (v != null) {
			final float dx = x - v.x();
			final float dy = y - v.y();
			final float dz = z - v.z();
			return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
		} else {
			return Float.NaN;
		}
	}

	public final float dist(PVector v) {
		if (v != null) {
			final float dx = x - v.x;
			final float dy = y - v.y;
			final float dz = z - v.z;
			return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
		} else {
			return Float.NaN;
		}
	}

	public final float distSq(Vec v) {
		if (v != null) {
			final float dx = x - v.x();
			final float dy = y - v.y();
			final float dz = z - v.z();
			return dx * dx + dy * dy + dz * dz;
		} else {
			return Float.NaN;
		}
	}
	
	/**
	 * returns the Division of this vector with the input
	 * @param s
	 * @return
	 */
		public Vec div(float s) {
			return new Vec(x/ s, y / s, z / s);
		}

		public Vec div(float a, float b, float c) {
			return new Vec(x / a, y / b, z / c);
		}

		public Vec div(Vec s) {
			return new Vec(x / s.x, y / s.y, z / s.z);
		}

		public Vec div(PVector s) {
			return new Vec(x / s.x, y / s.y, z / s.z);
		}

		/**
		 * Divides vector and overrides coordinates with result.
		 * 
		 * @param s  scale factor
		 * @return itself
		 */
		public Vec divSelf(float s) {
			x /= s;
			y /= s;
			z /= s;
			return this;
		}

		public Vec divSelf(float a, float b, float c) {
			x /= a;
			y /= b;
			z /= c;
			return this;
		}

	public final float dot(Vec v) {
		return x * v.x + y * v.y + z * v.z;
	}

	/**
	 * Returns true if the Object v is of type Vec and all of the data members
	 * of v are equal to the corresponding data members in this vector.
	 * 
	 * @param v
	 *            the Object with which the comparison is made
	 * @return true or false
	 */
	public boolean equals(Object v) {
		try {
			Vec vv = (Vec) v;
			return (x == vv.x() && y == vv.y() && z == vv.z());
		} catch (NullPointerException e) {
			return false;
		} catch (ClassCastException e) {
			return false;
		}
	}

	/**
	 * Returns true if the Object v is of type Vec and all of the data members
	 * of v are equal to the corresponding data members in this vector.
	 * 
	 * @param v
	 *            the vector with which the comparison is made
	 * @return true or false
	 */
	public boolean equals(Vec v) {
		try {
			return (x == v.x() && y == v.y() && z == v.z());
		} catch (NullPointerException e) {
			return false;
		}
	}

	public boolean equalsWithTolerance(Vec v, float tolerance) {
		try {
			float diff = x - v.x;
			if (Float.isNaN(diff)) {
				return false;
			}
			if ((diff < 0 ? -diff : diff) > tolerance) {
				return false;
			}
			diff = y - v.y;
			if (Float.isNaN(diff)) {
				return false;
			}
			if ((diff < 0 ? -diff : diff) > tolerance) {
				return false;
			}
			diff = z - v.z;
			if (Float.isNaN(diff)) {
				return false;
			}
			if ((diff < 0 ? -diff : diff) > tolerance) {
				return false;
			}
			return true;
		} catch (NullPointerException e) {
			return false;
		}
	}

	/**
	 * Replaces the vector components with integer values of their current
	 * values.
	 * 
	 * @return itself
	 */
	public final Vec floor() {
		x = (float) Math.floor(x);
		y = (float) Math.floor(y);
		z = (float) Math.floor(z);
		return this;
	}

	public final Vec getNormalized() {
		return new Vec(this).normalize();
	}

	public final Vec getNormalizedTo(float len) {
		return new Vec(this).normalizeTo(len);
	}
	
	public final Vec getRotatedX(float theta) {
		return new Vec(this).rotateX(theta);
	}

	public final Vec getRotatedY(float theta) {
		return new Vec(this).rotateY(theta);
	}

	public final Vec getRotatedZ(float theta) {
		return new Vec(this).rotateZ(theta);
	}


	public final float heading() {
		return (float) Math.atan2(y, x);
	}

	public final float headingXZ() {
		return (float) Math.atan2(z, x);
	}

	public final float headingYZ() {
		return (float) Math.atan2(y, z);
	}

	/**
	 * returns the Interpolation of the vector towards the given target vector,
	 * using linear interpolation.
	 */
	public final Vec interpolateTo(Vec v, float f) {
		return new Vec(x + (v.x() - x) * f, y + (v.y() - y) * f, z + (v.z() - z) * f);
	}

	public final Vec interpolateTo(PVector v, float f) {
		return new Vec(x + (v.x - x) * f, y + (v.y - y) * f, z + (v.z - z) * f);
	}

	/**
	 * Interpolates the vector towards the given target vector, using linear
	 * interpolation.
	 * 
	 * @param v
	 *            target vector
	 * @param f
	 *            interpolation factor (should be in the range 0..1)
	 * 
	 * @return itself, result overrides current vector
	 */
	public final Vec interpolateToSelf(Vec v, float f) {
		x += (v.x() - x) * f;
		y += (v.y() - y) * f;
		z += (v.z() - z) * f;
		return this;
	}

	public final Vec interpolateToSelf(PVector v, float f) {
		x += (v.x - x) * f;
		y += (v.y - y) * f;
		z += (v.z - z) * f;
		return this;
	}

	/**
	 * Scales vector uniformly by factor -1 ( v = -v ), overrides coordinates
	 * with result.
	 * 
	 * @return itself
	 */
	public final Vec invert() {
		x *= -1;
		y *= -1;
		z *= -1;
		return this;
	}

	/**
	 * Add random jitter to the vector in the range -j ... +j using the default
	 * {@link Random} generator of {@link MathUtils}.
	 * 
	 * @param j
	 *            the j
	 * 
	 * @return the vec3 d
	 */
	public final Vec jitter(float j) {
		return jitter(j, j, j);
	}

	/**
	 * Adds random jitter to the vector in the range -j ... +j using the default
	 * {@link Random} generator of {@link MathUtils}.
	 * 
	 * @param jx
	 *            maximum x jitter
	 * @param jy
	 *            maximum y jitter
	 * @param jz
	 *            maximum z jitter
	 * 
	 * @return itself
	 */
	public final Vec jitter(float jx, float jy, float jz) {
		x += Math.random() * jx;
		y += Math.random() * jy;
		z += Math.random() * jz;
		return this;
	}

	/**
	 * Adds random jitter to the vector in the range defined by the given vector
	 * components and using the default {@link Random} generator of
	 * {@link MathUtils}.
	 * 
	 * @param jitterVec
	 *            the jitter vec
	 * 
	 * @return itself
	 */
	public final Vec jitter(Vec jitterVec) {
		return jitter(jitterVec.x, jitterVec.y, jitterVec.z);
	}

	public final Vec jitter(PVector jitterVec) {
		return jitter(jitterVec.x, jitterVec.y, jitterVec.z);
	}

	/**
	 * Limits the vector's magnitude to the length given.
	 * 
	 * @param lim
	 *            new maximum magnitude
	 * 
	 * @return itself
	 */
	public final Vec limit(float lim) {
		if (magSq() > lim * lim) {
			return normalize().multSelf(lim);
		}
		return this;
	}

	/**
	 * returns the length of the vector
	 * 
	 * @return
	 */
	public final float mag() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}

	public final float magSq() {
		return x * x + y * y + z * z;
	}

	/**
	 * Max self.
	 * 
	 * @param b
	 *            the b
	 * 
	 * @return the vec3 d
	 */
	public final Vec maxSelf(Vec b) {
		x = Math.max(x, b.x);
		y = Math.max(y, b.y);
		z = Math.max(z, b.z);
		return this;
	}

	/**
	 * Min self.
	 * 
	 * @param b
	 *            the b
	 * 
	 * @return the vec3 d
	 */
	public final Vec minSelf(Vec b) {
		x = Math.min(x, b.x);
		y = Math.min(y, b.y);
		z = Math.min(z, b.z);
		return this;
	}
/**
 * returns the Product, Scaled vector of this vector with the input
 * @param s
 * @return
 */
	public Vec mult(float s) {
		return new Vec(x * s, y * s, z * s);
	}

	public Vec mult(float a, float b, float c) {
		return new Vec(x * a, y * b, z * c);
	}

	public Vec mult(Vec s) {
		return new Vec(x * s.x, y * s.y, z * s.z);
	}

	public Vec mult(PVector s) {
		return new Vec(x * s.x, y * s.y, z * s.z);
	}

	/**
	 * Scales vector and overrides coordinates with result.
	 * 
	 * @param s  scale factor
	 * @return itself
	 */
	public Vec multSelf(float s) {
		x *= s;
		y *= s;
		z *= s;
		return this;
	}


	public Vec multSelf(float a, float b, float c) {
		x *= a;
		y *= b;
		z *= c;
		return this;
	}

	/**
	 * Normalizes the vector so that its magnitude = 1.
	 * 
	 * @return itself
	 */
	public final Vec normalize() {
		float mag = (float) Math.sqrt(x * x + y * y + z * z);
		if (mag > 0) {
			mag = 1f / mag;
			x *= mag;
			y *= mag;
			z *= mag;
		}
		return this;
	}

	/**
	 * Normalizes the vector to the given length.
	 * 
	 * @param len desired length
	 * @return itself
	 */
	public final Vec normalizeTo(float len) {
		float mag = (float) Math.sqrt(x * x + y * y + z * z);
		if (mag > 0) {
			mag = len / mag;
			x *= mag;
			y *= mag;
			z *= mag;
		}
		return this;
	}

	/**
	 * Replaces the vector components with their multiplicative inverse.
	 * 
	 * @return itself
	 */
	public final Vec reciprocal() {
		x = 1f / x;
		y = 1f / y;
		z = 1f / z;
		return this;
	}

	public final Vec reflect(Vec normal) {
		return set(normal.mult(this.dot(normal) * 2).subSelf(this));
	}

	/**
	 * Rotates the vector around the giving axis.
	 * 
	 * @param axis
	 *            rotation axis vector
	 * @param theta
	 *            rotation angle (in radians)
	 * 
	 * @return itself
	 */
	public final Vec rotateAroundAxis(Vec axis, float theta) {
		final float ax = axis.x();
		final float ay = axis.y();
		final float az = axis.z();
		final float ux = ax * x;
		final float uy = ax * y;
		final float uz = ax * z;
		final float vx = ay * x;
		final float vy = ay * y;
		final float vz = ay * z;
		final float wx = az * x;
		final float wy = az * y;
		final float wz = az * z;
		final double si = Math.sin(theta);
		final double co = Math.cos(theta);
		float xx = (float) (ax * (ux + vy + wz) + (x * (ay * ay + az * az) - ax * (vy + wz)) * co + (-wy + vz) * si);
		float yy = (float) (ay * (ux + vy + wz) + (y * (ax * ax + az * az) - ay * (ux + wz)) * co + (wx - uz) * si);
		float zz = (float) (az * (ux + vy + wz) + (z * (ax * ax + ay * ay) - az * (ux + vy)) * co + (-vx + uy) * si);
		x = xx;
		y = yy;
		z = zz;
		return this;
	}

	/**
	 * Rotates the vector by the given angle around the X axis.
	 * 
	 * @param theta
	 *            the theta
	 * 
	 * @return itself
	 */
	public final Vec rotateX(float theta) {
		final float co = (float) Math.cos(theta);
		final float si = (float) Math.sin(theta);
		final float zz = co * z - si * y;
		y = si * z + co * y;
		z = zz;
		return this;
	}

	/**
	 * Rotates the vector by the given angle around the Y axis.
	 * 
	 * @param theta
	 *            the theta
	 * 
	 * @return itself
	 */
	public final Vec rotateY(float theta) {
		final float co = (float) Math.cos(theta);
		final float si = (float) Math.sin(theta);
		final float xx = co * x - si * z;
		z = si * x + co * z;
		x = xx;
		return this;
	}

	/**
	 * Rotates the vector by the given angle around the Z axis.
	 * 
	 * @param theta
	 *            the theta
	 * 
	 * @return itself
	 */
	public final Vec rotateZ(float theta) {
		final float co = (float) Math.cos(theta);
		final float si = (float) Math.sin(theta);
		final float xx = co * x - si * y;
		y = si * x + co * y;
		x = xx;
		return this;
	}


	/**
	 * Overrides coordinates with the given values.
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param z
	 *            the z
	 * 
	 * @return itself
	 */
	public Vec set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public Vec set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}

	/**
	 * Overrides coordinates with the ones of the given vector.
	 * 
	 * @param v
	 *            vector to be copied
	 * 
	 * @return itself
	 */
	public Vec set(Vec v) {
		x = v.x;
		y = v.y;
		z = v.z;
		return this;
	}

	public Vec setX(float x) {
		this.x = x;
		return this;
	}

	/**
	 * Overrides XY coordinates with the ones of the given vector.
	 * 
	 * @param v
	 *            2D vector
	 * 
	 * @return itself
	 */
	public Vec setXY(Vec v) {
		x = v.x;
		y = v.y;
		return this;
	}

	public Vec setY(float y) {
		this.y = y;
		return this;
	}

	public Vec setZ(float z) {
		this.z = z;
		return this;
	}

	/**
	 * Replaces all vector components with the signum of their original values.
	 * In other words if a components value was negative its new value will be
	 * -1, if zero => 0, if positive => +1
	 * 
	 * @return itself
	 */
	public Vec signum() {
		x = (x < 0 ? -1 : x == 0 ? 0 : 1);
		y = (y < 0 ? -1 : y == 0 ? 0 : 1);
		z = (z < 0 ? -1 : z == 0 ? 0 : 1);
		return this;
	}

	public final Vec sub(float a, float b, float c) {
		return new Vec(x - a, y - b, z - c);
	}

	public final Vec sub(Vec v) {
		return new Vec(x - v.x, y - v.y, z - v.z);
	}

	public final Vec sub(PVector v) {
		return new Vec(x - v.x, y - v.y, z - v.z);
	}

	/**
	 * Subtracts vector {a,b,c} and overrides coordinates with result.
	 * 
	 * @param a
	 *            X coordinate
	 * @param b
	 *            Y coordinate
	 * @param c
	 *            Z coordinate
	 * 
	 * @return itself
	 */
	public final Vec subSelf(float a, float b, float c) {
		x -= a;
		y -= b;
		z -= c;
		return this;
	}

	public final Vec subSelf(Vec v) {
		x -= v.x;
		y -= v.y;
		z -= v.z;
		return this;
	}

	public final Vec subSelf(PVector v) {
		x -= v.x;
		y -= v.y;
		z -= v.z;
		return this;
	}

	public final Vec to2DXY() {
		return new Vec(x, y, 0);
	}

	public final Vec to2DXZ() {
		return new Vec(x, 0, z);
	}

	public final Vec to2DYZ() {
		return new Vec(0, y, z);
	}

	public float[] toArray() {
		return new float[] { x, y, z };
	}

	public String toString() {
		final StringBuffer sb = new StringBuffer(48);
		sb.append("{x:").append(x).append(", y:").append(y).append(", z:").append(z).append("}");
		return sb.toString();
	}

	public final float x() {
		return x;
	}

	public final float y() {
		return y;
	}

	public final float z() {
		return z;
	}

}
