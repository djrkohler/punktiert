import peasy.PeasyCam;
import punktiert.math.Vec;
import punktiert.physics.*;

VPhysics physics;
PeasyCam cam;

public void setup() {
  size(1280, 720, P3D);
  stroke(200, 0, 0);
  //fill(250,200,200);
  noFill();

  cam = new PeasyCam(this, 400);

  int maxrangeofVision = 80;
  physics = new VPhysics(maxrangeofVision);

  // import boids seperated per flower
  ArrayList importedGroups = importBoids("/data/fromRhino/boidGroups.txt");

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

      p.trail.setInPast(300);
      p.swarm.setSeperationScale(0.0f);
      p.swarm.setCohesionScale(0.4f);
      //p.swarm.setAlignScale(0.3f);

      //p.swarm.setSeperationRadius(10);
      p.swarm.setCohesionRadius(200);
      p.swarm.setAlignRadius(50);

      p.swarm.setMaxForce(.01);
      p.swarm.setMaxSpeed(.5);

      physics.addParticle(p);
      group.addParticle(p);
    }
  }
}

public void draw() {
  background(240);
  lights();
  hint(DISABLE_STROKE_PERSPECTIVE);

  physics.update();

  for (int i = 0; i < physics.particles.size(); i++) {
    VBoid boid = (VBoid) physics.particles.get(i);

    beginShape();
    for (int j = 0; j < boid.trail.particles.size(); j++) {
      VParticle t = boid.trail.particles.get(j);
      vertex(t.x, t.y, t.z);
    }
    endShape();
  }
}




