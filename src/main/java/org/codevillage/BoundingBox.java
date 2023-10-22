package org.codevillage;

import com.jogamp.opengl.math.Vec3f;

import java.util.List;

public class BoundingBox extends Box {
  public final List<Box> boundedBoxes;

  BoundingBox(Vec3f position, Vec3f size, List<Box> boundedBoxes) {
    super(position, size);
    this.boundedBoxes = boundedBoxes;
  }

  public static BoundingBox createBoundingBoxFor(List<Box> boxes) {
    if (boxes == null)
      throw new IllegalArgumentException("Boxes cannot be null");

    if (boxes.isEmpty())
      throw new IllegalArgumentException("Boxes cannot be empty");

    float minX = Float.POSITIVE_INFINITY, maxX = Float.NEGATIVE_INFINITY;
    float minY = Float.POSITIVE_INFINITY, maxY = Float.NEGATIVE_INFINITY;
    float minZ = Float.POSITIVE_INFINITY, maxZ = Float.NEGATIVE_INFINITY;

    for (Box box : boxes) {
      Vec3f boxPosition = box.getCenter();
      Vec3f boxSide = box.getSize();
      minX = Math.min(minX, boxPosition.x() - boxSide.x() / 2);
      maxX = Math.max(maxX, boxPosition.x() + boxSide.x() / 2);
      minY = Math.min(minY, boxPosition.y() - boxSide.y() / 2);
      maxY = Math.max(maxY, boxPosition.y() + boxSide.y() / 2);
      minZ = Math.min(minZ, boxPosition.z() - boxSide.z() / 2);
      maxZ = Math.max(maxZ, boxPosition.z() + boxSide.z() / 2);
    }

    Vec3f center = new Vec3f((minX + maxX) / 2f, (minY + maxY) / 2f, (minZ + maxZ) / 2f);
    Vec3f size = new Vec3f(maxX - minX, maxY - minY, maxZ - minZ);
    return new BoundingBox(center, size, boxes);
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
    return "Bounding Box: [center=" + getCenter() + ", size=" + getSize() + ", boxes=" + boundedBoxes.toString() + "]";
  }

  public List<Box> getBoundedBoxes() {
    return this.boundedBoxes;
  }
}
