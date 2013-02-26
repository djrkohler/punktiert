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
