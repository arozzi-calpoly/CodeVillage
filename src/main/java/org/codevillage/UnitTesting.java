package org.codevillage;

import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.math.*;
import com.jogamp.opengl.util.FPSAnimator;

import java.awt.*;
import java.nio.file.Path;

import java.util.List;
import java.util.ArrayList;

public class UnitTesting {
  /*
   * public static void testHorizontalMovementControllerKeyPresses()
   * {
   * JFrame frame = new JFrame();
   * frame.setSize(500, 500);
   * JPanel mainPanel = new JPanel(new GridLayout(4, 2));
   * JLabel wIsPressedLabel = new JLabel();
   * JLabel aIsPressedLabel = new JLabel();
   * JLabel sIsPressedLabel = new JLabel();
   * JLabel dIsPressedLabel = new JLabel();
   * 
   * mainPanel.setBorder(new CompoundBorder(
   * new EmptyBorder(15, 15, 15, 15),
   * new TitledBorder("Keys:")
   * ));
   * 
   * mainPanel.add(new JLabel("W:"));
   * mainPanel.add(wIsPressedLabel);
   * mainPanel.add(new JLabel("A:"));
   * mainPanel.add(aIsPressedLabel);
   * mainPanel.add(new JLabel("S:"));
   * mainPanel.add(sIsPressedLabel);
   * mainPanel.add(new JLabel("D:"));
   * mainPanel.add(dIsPressedLabel);
   * 
   * HorizontalMovementController horizontalMovementController = new
   * HorizontalMovementController(1);
   * frame.addKeyListener(horizontalMovementController);
   * 
   * Thread thread = new Thread(() -> {
   * while(true)
   * {
   * wIsPressedLabel.setText("" + horizontalMovementController.getWIsPressed());
   * aIsPressedLabel.setText("" + horizontalMovementController.getAIsPressed());
   * sIsPressedLabel.setText("" + horizontalMovementController.getSIsPressed());
   * dIsPressedLabel.setText("" + horizontalMovementController.getDIsPressed());
   * try{
   * Thread.sleep(50);
   * }
   * catch (InterruptedException e) {
   * break;
   * }
   * }
   * });
   * thread.setDaemon(true);
   * thread.start();
   * 
   * frame.getContentPane().add(mainPanel);
   * frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
   * frame.setLocationRelativeTo(null);
   * frame.pack();
   * frame.setVisible(true);
   * }
   * 
   * public static void testVerticalMovementControllerKeyPresses()
   * {
   * JFrame frame = new JFrame();
   * frame.setSize(500, 500);
   * JPanel mainPanel = new JPanel(new GridLayout(6, 2));
   * JLabel wIsPressedLabel = new JLabel();
   * JLabel aIsPressedLabel = new JLabel();
   * JLabel sIsPressedLabel = new JLabel();
   * JLabel dIsPressedLabel = new JLabel();
   * JLabel spaceIsPressedLabel = new JLabel();
   * JLabel shiftIsPressedLabel = new JLabel();
   * 
   * mainPanel.setBorder(new CompoundBorder(
   * new EmptyBorder(15, 15, 15, 15),
   * new TitledBorder("Keys:")
   * ));
   * 
   * mainPanel.add(new JLabel("W:"));
   * mainPanel.add(wIsPressedLabel);
   * mainPanel.add(new JLabel("A:"));
   * mainPanel.add(aIsPressedLabel);
   * mainPanel.add(new JLabel("S:"));
   * mainPanel.add(sIsPressedLabel);
   * mainPanel.add(new JLabel("D:"));
   * mainPanel.add(dIsPressedLabel);
   * mainPanel.add(new JLabel("Space:"));
   * mainPanel.add(spaceIsPressedLabel);
   * mainPanel.add(new JLabel("Shift:"));
   * mainPanel.add(shiftIsPressedLabel);
   * 
   * VerticalMovementController verticalMovementController = new
   * VerticalMovementController(1);
   * frame.addKeyListener(verticalMovementController);
   * 
   * Thread thread = new Thread(() -> {
   * while(true)
   * {
   * wIsPressedLabel.setText("" + verticalMovementController.getWIsPressed());
   * aIsPressedLabel.setText("" + verticalMovementController.getAIsPressed());
   * sIsPressedLabel.setText("" + verticalMovementController.getSIsPressed());
   * dIsPressedLabel.setText("" + verticalMovementController.getDIsPressed());
   * spaceIsPressedLabel.setText("" +
   * verticalMovementController.getSpaceBarIsPressed());
   * shiftIsPressedLabel.setText("" +
   * verticalMovementController.getShiftIsPressed());
   * try{
   * Thread.sleep(50);
   * }
   * catch (InterruptedException e) {
   * break;
   * }
   * }
   * });
   * thread.setDaemon(true);
   * thread.start();
   * 
   * frame.getContentPane().add(mainPanel);
   * frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
   * frame.setLocationRelativeTo(null);
   * frame.pack();
   * frame.setVisible(true);
   * }
   */
  public static void testSimpleShader(MovementController movementController) {
    GLProfile glProfile = GLProfile.get(GLProfile.GL4);

    GLCapabilities glCapabilities = new GLCapabilities(glProfile);
    glCapabilities.setDoubleBuffered(true);
    glCapabilities.setHardwareAccelerated(true);

    String windowTitle = "Simple Shader Test";
    Display display = NewtFactory.createDisplay(windowTitle);
    Screen screen = NewtFactory.createScreen(display, 0);

    int WINDOW_WIDTH = 800, WINDOW_HEIGHT = 600;
    GLWindow window = GLWindow.create(screen, glCapabilities);
    window.setTitle(windowTitle);
    window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    window.setVisible(true);

    FPSAnimator animator = new FPSAnimator(window, 30);
    GLTestSimpleShaderEventListener testSimpleShaderEventListener = new GLTestSimpleShaderEventListener(
        movementController);
    animator.start();
    window.addKeyListener(movementController);
    window.addMouseListener(movementController);
    window.addGLEventListener(testSimpleShaderEventListener);
  }

  private static class GLTestSimpleShaderEventListener implements GLEventListener {
    private final MovementController movementController;

    public GLTestSimpleShaderEventListener(MovementController movementController) {
      this.movementController = movementController;
    }

    private StaticMVPShader shader;
    private Model3D cubeModel;
    private Model3D groundModel;
    private Texture modelTexture;
    private Texture groundTexture;
    private Vec3f lightDirection;
    private Vec3f eyePosition;
    private Vec2f eyeRotation;
    JavaPackage app;
    BoundingBox boundingBox;

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
      GL4 gl = glAutoDrawable.getGL().getGL4();
      String vertexShaderPath = "/Users/khoaly/Desktop/projects/CodeVillage/src/main/resources/shaders/simple_vertex_shader.glsl";
      String fragmentShaderPath = "/Users/khoaly/Desktop/projects/CodeVillage/src/main/resources/shaders/simple_fragment_shader.glsl";
      shader = new StaticMVPShader(gl, Path.of(vertexShaderPath), Path.of(fragmentShaderPath));

      // sphereModel = RenderingGeometryLib.generateSphereBySubdividingIcosahedron(gl,
      // 4);
      cubeModel = RenderingGeometryLib.generateCubeModel(gl);

      groundModel = RenderingGeometryLib.generateXZGrid(gl, -10, 10, -10, 10, 2, 2);

      modelTexture = ModelLoader.createSolidColorTexture(gl, 100, 100, Color.MAGENTA);

      groundTexture = ModelLoader.createSolidColorTexture(gl, 100, 100, new Color(128, 128, 128));

      lightDirection = new Vec3f(1, -1, -2);

      eyePosition = new Vec3f(0, 0.05f, 3);

      eyeRotation = new Vec2f(0, 0);

      app = createExampleOutput();

      boundingBox = calculateBounds(app);

    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
      // Free the VAOs, textures, whatever resources you were using with the given GL4
      // instance
      GL4 gl = glAutoDrawable.getGL().getGL4();
      System.out.println("Disposed");
      ModelLoader.cleanUp(gl);
      System.exit(0);
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
      GL4 gl = glAutoDrawable.getGL().getGL4();
      gl.glClearColor(0, 0, 0, 1);
      gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
      // configs alpha blending settings
      gl.glEnable(GL.GL_BLEND);
      gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
      // turns on depth testing
      gl.glEnable(GL.GL_DEPTH_TEST);
      // enables multi-sample antialiasing (if the GLCapabilities object set it up)
      gl.glEnable(GL.GL_MULTISAMPLE);

      eyePosition = movementController.getNextPosition(eyePosition, eyeRotation);
      eyeRotation = movementController.getNextRotation(eyePosition, eyeRotation);

      // Matrix4f projectionMatrix = new
      // Matrix4f().loadIdentity().setToPerspective((float)Math.toRadians(90.0f),
      // 1280.0f/720.0f, 0.1f, 1000.0f);

      // left=-width/2, right=+width/2, bottom=-height/2 and top=+height/2;
      // projectionMatrix.setToOrtho(-width/2, +width/2, -height/2, +height/2, 0.1f,
      // 100f);
      Matrix4f projectionMatrix = new Matrix4f().loadIdentity();
      projectionMatrix.setToPerspective(FovHVHalves.byRadians((float) (Math.PI / 2), (float) (Math.PI / 2)),
          0.1f, 100f);

      // Matrix4f viewMatrix = new Matrix4f().loadIdentity().setToTranslation(new
      // Vec3f(eyePosition).scale(-1));
      Matrix4f viewMatrix = createViewMatrixFromEye(eyePosition, eyeRotation);
      // System.out.println(eyePosition);
      // System.out.println(eyeRotation);
      // System.out.println();

      // double angle = (System.currentTimeMillis() / 1000.0 * 0.15) % (2 * Math.PI);
      // double angleX = (Math.sin(5.*angle + 11) + Math.sin(2.*angle + 17) +
      // Math.sin(7.*angle + 13)) / 3 * 2 * Math.PI;
      // double angleY = (Math.sin(5.*angle + 17) + Math.sin(2.*angle + 13) +
      // Math.sin(7.*angle + 11)) / 3 * 2 * Math.PI;
      // double angleZ = (Math.sin(5.*angle + 13) + Math.sin(2.*angle + 11) +
      // Math.sin(7.*angle + 17)) / 3 * 2 * Math.PI;

      // double cubeScaleAngle = (System.currentTimeMillis() / 1000.0) % (2 *
      // Math.PI);
      // double cubeScale = Math.sin(cubeScaleAngle) * 0.5 + 0.55;

      double angleX = 0;
      double angleY = 0;
      double angleZ = 0;

      double scaleX = boundingBox.getSize().x();
      double scaleY = boundingBox.getSize().y();
      double scaleZ = boundingBox.getSize().z();

      Matrix4f modelMatrix = createModelTransformationMatrix(
          new Vec3f(0, 1, 0),
          (float) angleX, (float) angleY, (float) angleZ,
          (float) scaleX, (float) scaleY, (float) scaleZ);

      shader.start(gl);
      shader.loadEyePosition(gl, eyePosition);
      shader.loadLightDirection(gl, lightDirection);

      // draw the cube
      shader.loadModelViewProjectionMatrices(gl, modelMatrix, viewMatrix, projectionMatrix);
      shader.loadModelTexture(gl, modelTexture);
      // bind the VAO
      gl.glBindVertexArray(cubeModel.getVaoID());
      // enable all the vertex attributes
      cubeModel.enableAllVertexAttributeArrays(gl);
      // activate and bind the textures for the model
      shader.enableAllTextures(gl);
      // finally, draw all the triangles
      gl.glDrawElements(GL4.GL_TRIANGLES, cubeModel.getVertexCount(), GL.GL_UNSIGNED_INT, 0);
      cubeModel.disableAllVertexAttributeArrays(gl);
      gl.glBindVertexArray(0);

      Matrix4f groundModelMatrix = new Matrix4f().loadIdentity().setToTranslation(0, -1f, 0);
      // draw the ground
      shader.loadModelViewProjectionMatrices(gl, groundModelMatrix, viewMatrix, projectionMatrix);
      shader.loadModelTexture(gl, groundTexture);
      // bind the VAO
      gl.glBindVertexArray(groundModel.getVaoID());
      // enable all the vertex attributes
      groundModel.enableAllVertexAttributeArrays(gl);
      // activate and bind the textures for the model
      shader.enableAllTextures(gl);
      // finally, draw all the triangles
      gl.glDrawElements(GL4.GL_TRIANGLES, groundModel.getVertexCount(), GL.GL_UNSIGNED_INT, 0);
      groundModel.disableAllVertexAttributeArrays(gl);
      gl.glBindVertexArray(0);

      shader.stop(gl);
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {
    }

    public static Matrix4f createModelTransformationMatrix(Vec3f translation, float rx, float ry,
        float rz, float sx, float sy, float sz) {
      Matrix4f matrix = new Matrix4f();
      matrix.loadIdentity();
      matrix.translate(translation, new Matrix4f());
      matrix.mul(new Matrix4f().setToRotationEuler(rx, 0, 0));
      matrix.mul(new Matrix4f().setToRotationEuler(0, ry, 0));
      matrix.mul(new Matrix4f().setToRotationEuler(0, 0, rz));
      matrix.scale(sx, sy, sz, new Matrix4f());
      return matrix;
    }

    public static Matrix4f createViewMatrixFromEye(Vec3f eyePosition, Vec2f eyeRotation) {
      Matrix4f matrix = new Matrix4f();
      matrix.loadIdentity();
      matrix.mul(new Matrix4f().setToRotationEuler(-eyeRotation.x(), 0, 0));
      matrix.mul(new Matrix4f().setToRotationEuler(0, -eyeRotation.y(), 0));
      matrix.translate(new Vec3f(eyePosition).scale(-1), new Matrix4f());
      return matrix;
    }
  }

  public static void testGraphicsWindowCreation() {
    GLProfile glProfile = GLProfile.get(GLProfile.GL4);

    GLCapabilities glCapabilities = new GLCapabilities(glProfile);
    glCapabilities.setDoubleBuffered(true);
    glCapabilities.setHardwareAccelerated(true);

    String windowTitle = "Window Creation Test";
    Display display = NewtFactory.createDisplay(windowTitle);
    Screen screen = NewtFactory.createScreen(display, 0);

    int WINDOW_WIDTH = 800, WINDOW_HEIGHT = 600;
    GLWindow window = GLWindow.create(screen, glCapabilities);
    window.setTitle(windowTitle);
    window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    window.setVisible(true);

    FPSAnimator animator = new FPSAnimator(window, 30);
    GLTestWindowCreationEventListener windowCreationEventListener = new GLTestWindowCreationEventListener();
    animator.start();
    // window.addKeyListener(windowCreationEventListener);
    window.addGLEventListener(windowCreationEventListener);
  }

  private static class GLTestWindowCreationEventListener implements GLEventListener {
    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
      System.out.println("Disposed");
      // Free the VAOs, textures, whatever resources you were using with the GL4
      // instance
      // given by 'glAutoDrawable.getGL().getGL4()'
      System.exit(0);
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
      GL4 gl = glAutoDrawable.getGL().getGL4();
      double redComponent = Math.sin(System.currentTimeMillis() / 1000.0 * Math.PI) * 0.5 + 0.5;
      gl.glClearColor((float) redComponent, 0, 0, 0);
      gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
      // configs alpha blending settings
      gl.glEnable(GL.GL_BLEND);
      gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
      // turns on depth testing
      gl.glEnable(GL.GL_DEPTH_TEST);
      // enables multi-sample antialiasing (if the GLCapabilities object set it up)
      gl.glEnable(GL.GL_MULTISAMPLE);
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {
    }
  }

  public static JavaPackage createExampleOutput() {
    JavaPackage app = new JavaPackage();
    JavaClass appClass = new JavaClass(1, 1);
    JavaClass appClass2 = new JavaClass(1, 2);

    app.addClass(appClass);
    app.addClass(appClass2);

    JavaPackage subApp = new JavaPackage();
    JavaClass subAppClass = new JavaClass(2, 1);
    JavaClass subAppClass2 = new JavaClass(2, 2);
    JavaClass subAppClass3 = new JavaClass(1, 3);

    subApp.addClass(subAppClass);
    subApp.addClass(subAppClass2);
    subApp.addClass(subAppClass3);

    // JavaPackage subApp2 = new JavaPackage();
    // JavaClass subApp2Class = new JavaClass(1, 1);
    // JavaClass subApp2Class2 = new JavaClass(2, 2);
    // JavaClass subApp2Class3 = new JavaClass(3, 4);
    //
    // subApp2.addClass(subApp2Class);
    // subApp2.addClass(subApp2Class2);
    // subApp2.addClass(subApp2Class3);
    //
    app.addSubpackage(subApp);
    // app.addSubpackage(subApp2);

    return app;
  }

  public static BoundingBox calculateBounds(JavaPackage app) {
    List<JavaPackage> subpackages = app.getSubpackages();
    List<JavaClass> classes = app.getClasses();

    BoundingBox boundingBox = new BoundingBox(new Vec3f(0, 0, 0), new Vec3f(1f, 1f, 1f));
    double totalWidth = 0;
    double spacing = 0.5;

    for (JavaClass javaClass : classes) {
      // length and width determined by NOA, height determined by NOM
      Box box = new Box(new Vec3f(0, 0, 0),
          new Vec3f(1f * javaClass.getNOA(), 1f * javaClass.getNOM(), 1f * javaClass.getNOA()));
      totalWidth += box.getSize().x() + (spacing * 2);
      boundingBox.addBoundBox(box);
    }

    for (JavaPackage javaPackage : subpackages) {

      BoundingBox subBoundingBox = calculateBounds(javaPackage);
      boundingBox.addBoundBox(subBoundingBox);
      totalWidth += subBoundingBox.getSize().x() + (spacing * 2);
    }

    boundingBox.setSize(new Vec3f((float) totalWidth, 0, (float) totalWidth));
    return boundingBox;
  }

}
