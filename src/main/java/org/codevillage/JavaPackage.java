package org.codevillage;

import java.util.ArrayList;
import java.util.List;

class JavaPackage {
  private List<JavaClass> classes;
  private List<JavaPackage> subpackages;

  public JavaPackage() {
    classes = new ArrayList<>();
    subpackages = new ArrayList<>();
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
}
