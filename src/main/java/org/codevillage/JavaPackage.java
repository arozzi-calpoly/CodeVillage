package org.codevillage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class JavaPackage {
  private String name;
  private List<JavaClass> classes;
  private List<JavaPackage> subpackages;

  public JavaPackage(String name) {
    this.name = name;
    this.classes = new ArrayList<>();
    this.subpackages = new ArrayList<>();
  }

  public void addClass(JavaClass javaClass) {
    classes.add(javaClass);
  }

  public void addSubpackage(JavaPackage subpackage) {
    subpackages.add(subpackage);
  }

  public List<JavaClass> getClasses() {
    return classes;
  }

  public List<JavaPackage> getSubpackages() {
    return subpackages;
  }

  public void createSubpackagesAndClasses(int depth, int maxSubpackages, int maxClasses) {
    if (depth <= 0) {
      return;
    }

    Random random = new Random();
    int numSubpackages = random.nextInt(maxSubpackages) + 1;
    int numClasses = random.nextInt(maxClasses) + 1;

    for (int i = 0; i < numSubpackages; i++) {
      JavaPackage subpackage = new JavaPackage("Package " + i + " of " + name);
      subpackage.createSubpackagesAndClasses(depth - 1, maxSubpackages, maxClasses);
      subpackage.addClass(JavaClass.createRandomJavaClass());
      addSubpackage(subpackage);
    }

    // Ensure at least one class in the current package
    if (numClasses == 0) {
      addClass(JavaClass.createRandomJavaClass());
      numClasses = 1;
    }

    for (int i = 1; i < numClasses; i++) {
      addClass(JavaClass.createRandomJavaClass());
    }
  }

  public void printPackageStructure(String indent) {
    System.out.println(indent + "Package: " + name);
    for (JavaClass javaClass : classes) {
      System.out.println(indent + "  Class: " + javaClass);
    }
    for (JavaPackage subpackage : subpackages) {
      subpackage.printPackageStructure(indent + "  ");
    }
  }
}
