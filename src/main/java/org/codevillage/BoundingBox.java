package org.codevillage;

import com.jogamp.opengl.math.*;
import java.util.List;
import java.util.ArrayList;

class BoundingBox extends Box {
  private List<Box> boundBoxes;

  BoundingBox(Vec3f position, Vec3f size) {
    super(position, size);
    boundBoxes = new ArrayList<>();
  }

  public void addBoundBox(Box box) {
    boundBoxes.add(box);
  }

  public List<Box> getBoundBoxes() {
    return boundBoxes;
  }

}
