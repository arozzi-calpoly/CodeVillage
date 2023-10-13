package org.codevillage;

public class Main {
    public static void main(String[] args)
    {
        System.out.println("Hello world!");
        // UnitTesting.testVerticalMovementControllerKeyPresses();
        UnitTesting.testSimpleShader(new HorizontalMovementController(0.1f, 0.05f));
    }
}