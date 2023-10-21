package org.codevillage;

import java.util.List;
import java.util.ArrayList;
import com.jogamp.opengl.math.*;

class BoxPacker {
  BoundingBox bbox;
  List<Vec3f> corners = new ArrayList<>();

  BoxPacker(
      BoundingBox bbox) {
    this.bbox = bbox;
    this.corners.add(bbox.getLowerLeft());
  }

  public void pack() {
    // sort list of bound boxes by size in descending order
    List<Box> boxes = bbox.getBoundBoxes();
    boxes.sort((a, b) -> (int) b.getSize().x() - (int) a.getSize().x());

    // for every box, try the list of bottom left corners. place box at corner
    // if the box fits, split the bounding box along the right edge of the placed
    // box
    // update the list of bottom left corners, remove the corner that was used and
    // add the new corners
    // repeat until all boxes are placed
    for (Box box : boxes) {
      for (Vec3f corner : corners) {
        box.setCenter(corner);
        if (bbox.fits(box)) {
          corners.add(box.getUpperLeft());
          corners.add(box.getLowerRight());
          corners.remove(corner);
          break;
        }
      }

      if (box instanceof BoundingBox) {
        BoundingBox subBox = (BoundingBox) box;
        BoxPacker subPacker = new BoxPacker(subBox);
        subPacker.pack();
      }
    }

  }

}
