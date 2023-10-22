package org.codevillage;

import java.util.Random;

class JavaClass {
  private int nom;
  private int noa;

  public JavaClass(int nom, int noa) {
    this.nom = nom;
    this.noa = noa;
  }

  public int getNOA() {
    return noa;
  }

  public int getNOM() {
    return nom;
  }

  public static JavaClass createRandomJavaClass() {
    Random random = new Random();
    int attribute1 = random.nextInt(10) + 1; // Random number between 1 and 10
    int attribute2 = random.nextInt(10) + 1; // Random number between 1 and 10
    return new JavaClass(attribute1, attribute2);
  }
}
