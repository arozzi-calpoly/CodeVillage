package org.codevillage;

import com.jogamp.opengl.math.Vec3f;
import com.jogamp.opengl.math.*;
import java.util.List;
import java.util.Arrays;

class Box {
  private Vec3f center;
  private Vec3f size;
  private Vec3f upperLeft;
  private Vec3f upperRight;
  private Vec3f lowerLeft;
  private Vec3f lowerRight;

  public Box(Vec3f center, Vec3f size) {
    this.center = center;
    this.size = size;
    this.upperLeft = new Vec3f(center.x() - size.x() / 2, center.y(), center.z() + size.z() / 2);
    this.upperRight = new Vec3f(center.x() + size.x() / 2, center.y(), center.z() + size.z() / 2);
    this.lowerRight = new Vec3f(center.x() + size.x() / 2, center.y(), center.z() - size.z() / 2);
    this.lowerLeft = new Vec3f(center.x() - size.x() / 2, center.y(), center.z() - size.z() / 2);

  }

  public Vec3f getCenter() {
    return this.center;
  }

  public Vec3f getSize() {
    return this.size;
  }

  public void setCenter(Vec3f center) {
    this.center = center;
    this.upperLeft = new Vec3f(center.x() - size.x() / 2, center.y(), center.z() + size.z() / 2);
    this.upperRight = new Vec3f(center.x() + size.x() / 2, center.y(), center.z() + size.z() / 2);
    this.lowerRight = new Vec3f(center.x() + size.x() / 2, center.y(), center.z() - size.z() / 2);
    this.lowerLeft = new Vec3f(center.x() - size.x() / 2, center.y(), center.z() - size.z() / 2);

  }

  public void setSize(Vec3f size) {
    this.size = size;
  }

  public Vec3f getUpperLeft() {
    return this.upperLeft;
  }

  public Vec3f getUpperRight() {
    return this.upperRight;
  }

  public Vec3f getLowerRight() {
    return this.lowerRight;
  }

  public Vec3f getLowerLeft() {
    return this.lowerLeft;
  }

  public List<Vec3f> getVertices() {

    return Arrays.asList(this.upperLeft, this.upperRight, this.lowerRight, this.lowerLeft);
  }

  @Override
  public String toString() {
    return "Box [center=" + this.center + ", size=" + this.size + "]";
  }
}
