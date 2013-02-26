package punktiert.test;

import java.util.ArrayList;

import peasy.PeasyCam;
import processing.core.PApplet;
import punktiert.math.Vec;
import punktiert.physics.*;

@SuppressWarnings("serial")
public class FlockGoth06 extends PApplet {

	VPhysics physics;

	PeasyCam cam;
	boolean pause = true;

	// time
	String fileName;

	public void setup() {
		size(1280, 720, P3D);

		setUI();

		physics = new VPhysics(50);

		// import boids seperated per flower
		ArrayList importedGroups = importBoids("D:/Eclipse/punktiert.public/data/boidGroups.txt");

		for (int j = 0; j < importedGroups.size(); j++) {
			ArrayList imports = (ArrayList) importedGroups.get(j);
			// create new ParticleGroup, and store each new Boid in it.
			// .swarm will ignore particles belonging to this group
			VParticleGroup group = new VParticleGroup();
			physics.addGroup(group);

			for (int i = 0; i < imports.size(); i += 2) {
				Vec pos = (Vec) imports.get(i);
				Vec vel = (Vec) imports.get(i + 1);
				VBoid p = new VBoid(pos, vel, group);

				p.trail.setInPast(500);
				p.swarm.setSeperationScale(1.0f);
				p.swarm.setCohesionScale(1.0f);
				p.swarm.setAlignScale(1.0f);

				
				p.swarm.setSeperationRadius(30);
				p.swarm.setCohesionRadius(30);
				p.swarm.setAlignRadius(30);

				physics.addParticle(p);
				group.addParticle(p);
			}
		}
	}

	public void draw() {
		background(240);

		if (pause)
			physics.update();

		for (int i = 0; i < physics.particles.size(); i++) {
			VBoid boid = (VBoid) physics.particles.get(i);

			strokeWeight(5);
			stroke(0);
			point(boid.x, boid.y, boid.z);
			strokeWeight(1);
			stroke(200, 0, 0);
			noFill();
			beginShape();
			for (int j = 0; j < boid.trail.particles.size(); j++) {
				VParticle t = boid.trail.particles.get(j);
				vertex(t.x, t.y, t.z);
			}
			endShape();
		}

	}
	
	/////////////////////////////////////////////////////////////////////////////

	public ArrayList<ArrayList> importBoids(String fileName) {

		ArrayList<ArrayList> importList = new ArrayList<ArrayList>();

		String lines[] = loadStrings(fileName);

		for (int i = 0; i < (lines.length); i++) {

			String[] txtBoids = split(lines[i], "/");
			ArrayList<Vec> group = new ArrayList<Vec>();

			for (int j = 0; j < txtBoids.length - 1; j++) {

				// Tokens position and velocity
				String[] txtVectors = split(txtBoids[j], ";");

				// Tokens each coordinate for position
				String[] txtCoord = split(txtVectors[0], ",");
				// convert from String to float
				float x = Float.parseFloat(txtCoord[0]);
				float y = Float.parseFloat(txtCoord[1]);
				float z = Float.parseFloat(txtCoord[2]);
				// create vector and add to the PositionList
				Vec pos = new Vec(x, -y, z);
				group.add(pos);

				// Tokens each coordinate for velocity
				txtCoord = split(txtVectors[1], ",");
				// convert from String to float
				x = Float.parseFloat(txtCoord[0]);
				y = Float.parseFloat(txtCoord[1]);
				z = Float.parseFloat(txtCoord[2]);
				// create vector and add to the VelocityList
				// notice: Rhino Coordinates: y is goes up: multiply y Coord by
				// -1
				Vec vel = new Vec(x, -y, z);
				group.add(vel);
			}
			importList.add(group);
		}
		return importList;
	}

	public void keyPressed() {

		// pause simulation
		if (key == ' ') {
			pause = !pause;
		}

		// exports current frame as jpg
		if (key == 'i') {
			saveFrame("frame_#####.png");
		}

		if (key == 's') {
			for (int i = 0; i < physics.particles.size(); i++) {
				VBoid p = (VBoid) physics.particles.get(i);
				p.swarm.setSeperationRadius(20);
				p.swarm.setCohesionRadius(50);
				p.swarm.setAlignRadius(50);
			}
		}
	}

	// ///////////////////////////////////////////////////////////////////

	void setUI() {

		// peasycam setup
		cam = new PeasyCam(this, 800);

		// fileName by current Date
		int mo = month();
		int d = day();
		int h = hour();
		int mi = minute();
		int se = second();

		String[] buff = new String[5];
		buff[0] = String.valueOf(mo);
		buff[1] = String.valueOf(d);
		buff[2] = String.valueOf(h);
		buff[3] = String.valueOf(mi);
		buff[4] = String.valueOf(se);
		fileName = join(buff, "_");
		println(fileName);

		// ///////////////////////////////////////////////////////////////
	}

}
