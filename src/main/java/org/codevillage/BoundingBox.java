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

  public boolean fits(Box box) {
    // check the upper left and lower right corners of the box to see if they are
    // within the bounding box
    Vec3f upperLeft = box.getUpperLeft();
    Vec3f lowerRight = box.getLowerRight();
    Vec3f bboxUpperLeft = getUpperLeft();
    Vec3f bboxLowerRight = getLowerRight();
    return (upperLeft.x() >= bboxUpperLeft.x() && upperLeft.z() <= bboxUpperLeft.z()
        && lowerRight.x() <= bboxLowerRight.x() && lowerRight.z() >= bboxLowerRight.z());
  }

  @Override
  public String toString() {
    return "Bounding Box: [center=" + getCenter() + ", size=" + getSize() + ", boxes=" + boundBoxes.toString() + "]";
  }

}
