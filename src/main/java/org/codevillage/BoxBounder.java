package org.codevillage;

import java.util.List;
import com.jogamp.opengl.math.*;

class BoxBounder {

  JavaPackage app;

  public BoxBounder(JavaPackage app) {
    this.app = app;
  }

  public BoundingBox calculateBounds() {
    List<JavaPackage> subpackages = app.getSubpackages();
    List<JavaClass> classes = app.getClasses();

    BoundingBox boundingBox = new BoundingBox(new Vec3f(0, 0, 0), new Vec3f(1f, 1f, 1f));
    double totalArea = 0;
    double spacing = 0;

    for (JavaClass javaClass : classes) {
      // length and width determined by NOA, height determined by NOM
      Box box = new Box(new Vec3f(0, 0, 0),
          new Vec3f(1f + javaClass.getNOA(), 1f + javaClass.getNOM(), 1f + javaClass.getNOA()));
      totalArea += Math.pow(box.getSize().x() + (spacing * 2), 2);
      boundingBox.addBoundBox(box);
    }

    for (JavaPackage javaPackage : subpackages) {
      BoxBounder subBounder = new BoxBounder(javaPackage);
      BoundingBox subBoundingBox = subBounder.calculateBounds();
      boundingBox.addBoundBox(subBoundingBox);
      totalArea += Math.pow(subBoundingBox.getSize().x() + (spacing * 2), 2);
    }

    double rootLength = Math.sqrt(totalArea);

    int sideLength = (int) Math.ceil(rootLength);

    boundingBox.setSize(new Vec3f((float) sideLength, 0.25f, (float) sideLength));
    return boundingBox;
  }

}
