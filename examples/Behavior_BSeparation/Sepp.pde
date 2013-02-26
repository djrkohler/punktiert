class Sepp extends VParticle{
  
  BSeparate separation;
  
  Sepp(Vec pos, float weight,float  rad) {
    super(pos,weight, rad);
    
    separation = new BSeparate(0, 3.5f, .1f);
    this.addBehavior(separation);
    
  }
  
  void setSeparation(float radius) {
  
  separation.setDesiredSeperation(radius);
  }
}
