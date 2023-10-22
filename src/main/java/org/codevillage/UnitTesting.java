package org.codevillage;

import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.math.FovHVHalves;
import com.jogamp.opengl.math.Matrix4f;
import com.jogamp.opengl.math.Vec2f;
import com.jogamp.opengl.math.Vec3f;
import com.jogamp.opengl.util.FPSAnimator;

import java.awt.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

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
    private Texture[] colors;
    JavaPackage app;
    List<Box> boxes;

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
      GL4 gl = glAutoDrawable.getGL().getGL4();
      // String vertexShaderPath =
      // "/Users/khoaly/Desktop/projects/CodeVillage/src/main/resources/shaders/simple_vertex_shader.glsl";
      // String fragmentShaderPath =
      // "/Users/khoaly/Desktop/projects/CodeVillage/src/main/resources/shaders/simple_fragment_shader.glsl";
      // shader = new StaticMVPShader(gl, Path.of(vertexShaderPath),
      // Path.of(fragmentShaderPath));

      InputStream vertexShaderStream = ClassLoader.getSystemClassLoader()
          .getResourceAsStream("shaders/simple_vertex_shader.glsl");
      InputStream fragmentShaderStream = ClassLoader.getSystemClassLoader()
          .getResourceAsStream("shaders/simple_fragment_shader.glsl");
      shader = new StaticMVPShader(gl, vertexShaderStream, fragmentShaderStream);

      // sphereModel = RenderingGeometryLib.generateSphereBySubdividingIcosahedron(gl,
      // 4);
      cubeModel = RenderingGeometryLib.generateCubeModel(gl);

      groundModel = RenderingGeometryLib.generateXZGrid(gl, -10, 10, -10, 10, 2, 2);

      modelTexture = ModelLoader.createSolidColorTexture(gl, 100, 100, Color.MAGENTA);

      groundTexture = ModelLoader.createSolidColorTexture(gl, 100, 100, new Color(128, 128, 128));

      lightDirection = new Vec3f(1, -1, -2);

      eyePosition = new Vec3f(0, 0.05f, 3);

      eyeRotation = new Vec2f(0, 0);

      app = new JavaPackage("Root Package");
      app.createSubpackagesAndClasses(3, 10, 10);
      app.printPackageStructure("");

      // boundingBox = createBoxSceneFromPackage(app);
      boxes = createBoxSceneFromPackage(app);
      colors = new Texture[boxes.size()];
      for (int i = 0; i < colors.length; i++)
        colors[i] = ModelLoader.createSolidColorTexture(gl, 1, 1,
            Color.getHSBColor((float) i / colors.length, 1, 1));
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

      for (int i = 0; i < boxes.size(); i++) {
        Box box = boxes.get(i);
        Texture texture = colors[i];
        double scaleX = box.getSize().x();
        double scaleY = box.getSize().y();
        double scaleZ = box.getSize().z();

        Matrix4f modelMatrix = createModelTransformationMatrix(
            box.getCenter(),
            (float) angleX, (float) angleY, (float) angleZ,
            (float) scaleX, (float) scaleY, (float) scaleZ);

        shader.start(gl);
        shader.loadEyePosition(gl, eyePosition);
        shader.loadLightDirection(gl, lightDirection);

        // draw the cube
        shader.loadModelViewProjectionMatrices(gl, modelMatrix, viewMatrix, projectionMatrix);
        shader.loadModelTexture(gl, texture);
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
      }

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

  public static List<Box> createBoxSceneFromPackage(JavaPackage app) {
    BoundingBox rootBox = createBoxSceneFromPackageRecur(app);
    Vec3f topLeftOfRoot = new Vec3f(-rootBox.getSize().x() / 2,
        0,
        -rootBox.getSize().z() / 2);

    List<Box> spacedOutBoxes = new ArrayList<>();
    linearizeBoxTreeRecur(rootBox, topLeftOfRoot, 0, spacedOutBoxes);
    return spacedOutBoxes;
  }

  private static final float PACKAGE_BOX_HEIGHT = 0.3f;

  private static void linearizeBoxTreeRecur(Box box, Vec3f topLeftOfParentBox, int depth, List<Box> spacedOutBoxes) {
    float heightFromXZPlane = depth * PACKAGE_BOX_HEIGHT;

    Vec3f newPosition = new Vec3f(
        box.getCenter().x() + topLeftOfParentBox.x(),
        box.getSize().y() / 2f + heightFromXZPlane,
        box.getCenter().z() + topLeftOfParentBox.z());

    box.setCenter(newPosition);

    Vec3f newTopLeft = new Vec3f(newPosition).sub(new Vec3f(box.getSize().x() / 2, 0, box.getSize().z() / 2));

    spacedOutBoxes.add(box);
    if (!(box instanceof BoundingBox boundingBox))
      return;

    for (Box childBox : boundingBox.boundedBoxes) {
      linearizeBoxTreeRecur(childBox, newTopLeft, depth + 1, spacedOutBoxes);
    }
  }

  public static BoundingBox createBoxSceneFromPackageRecur(JavaPackage app) {
    List<Box> boxes = new ArrayList<>();
    for (JavaClass javaClass : app.getClasses()) {
      boxes.add(new Box(
          new Vec3f(0, 0, 0),
          new Vec3f(1f * javaClass.getNOA(),
              1f * javaClass.getNOM(),
              1f * javaClass.getNOA())));
    }

    for (JavaPackage javaPackage : app.getSubpackages()) {
      BoundingBox packageBox = createBoxSceneFromPackageRecur(javaPackage);
      boxes.add(packageBox);
    }

    BoundingBox boundingBox = packBoxesIntoBoundingBoxInXZPlane(boxes);
    boundingBox.getSize().setY(PACKAGE_BOX_HEIGHT);
    return boundingBox;
  }

  public static BoundingBox packBoxesIntoBoundingBoxInXZPlane(List<Box> boxes) {
    // We first need to estimate the size of the box we need to fit everything
    // inside
    // We need to make sure that the bounding box at least has enough room for
    // each individual box (i.e. the side lengths must be > the maximum X and Z
    // side length of every contained box)
    // We also need to make sure that the total area of the XZ face of the bounding
    // box
    // must be > the total area of the XZ faces of the contained boxes
    double maxXSize = 0, maxZSize = 0;
    double totalArea = 0;
    for (Box box : boxes) {
      maxXSize = Math.max(maxXSize, box.getSize().x());
      maxZSize = Math.max(maxZSize, box.getSize().z());
      totalArea += box.getSize().x() * box.getSize().z();
    }

    // margin of error we should give ourselves for the total area of the bounding
    // box
    double arealMargin = 2;

    totalArea *= arealMargin;
    double xSideLength = Math.max(maxXSize + 0.0001, Math.sqrt(totalArea));
    double zSideLength = Math.max(maxZSize + 0.0001, Math.sqrt(totalArea));

    // keep increasing the side length until they can all fit
    while (!pack(boxes, xSideLength, zSideLength)) {
      xSideLength *= 1.5;
      zSideLength *= 1.5;
    }
    return BoundingBox.createBoundingBoxFor(boxes);
  }

  public static boolean pack(List<Box> boxes, double containerWidth, double containerHeight) {
    PriorityQueue<Box> rectanglesLeft = new PriorityQueue<>(
        (o1, o2) -> Double.compare(o2.getSize().x(), o1.getSize().x()));

    rectanglesLeft.addAll(boxes);

    List<Box> placedRectangles = new ArrayList<>(boxes.size());
    PriorityQueue<Vec2f> candidatePositions = new PriorityQueue<>(Comparator.comparingDouble(Vec2f::y));
    candidatePositions.add(new Vec2f(0f, 0f));
    return packRecur(rectanglesLeft, placedRectangles, candidatePositions,
        containerWidth, containerHeight);
  }

  public static boolean packRecur(PriorityQueue<Box> rectanglesLeft, List<Box> placedRectangles,
      PriorityQueue<Vec2f> candidatePositions,
      double containerWidth, double containerHeight) {
    // if they've all been placed, we're done!
    if (rectanglesLeft.isEmpty())
      return true;

    for (Vec2f position : new ArrayList<>(candidatePositions)) {
      for (Box box : new ArrayList<>(rectanglesLeft)) {
        // need to translate the top-left corner of the box
        // to the given position
        Vec2f newCenter = new Vec2f(position.x() + box.getSize().x() / 2, position.y() + box.getSize().z() / 2);
        box.setCenter(new Vec3f(newCenter.x(), 0, newCenter.y()));
        // calculated AFTER translation, they are relative to new_rect
        Vec2f topRight = new Vec2f(position.x() + box.getSize().x(), position.y());
        Vec2f bottomLeft = new Vec2f(position.x(), position.y() + box.getSize().z());
        if (rectangleCanBePlaced(box, placedRectangles, containerWidth, containerHeight)) {
          rectanglesLeft.remove(box);
          placedRectangles.add(box);
          candidatePositions.remove(position);
          candidatePositions.add(topRight);
          candidatePositions.add(bottomLeft);

          boolean success = packRecur(rectanglesLeft, placedRectangles, candidatePositions, containerWidth,
              containerHeight);
          if (success) {
            return true;
          } else {
            rectanglesLeft.add(box);
            placedRectangles.remove(placedRectangles.size() - 1);
            candidatePositions.add(position);
            candidatePositions.remove(topRight);
            candidatePositions.remove(bottomLeft);
          }
        }
      }
    }

    return false;
  }

  private static boolean rectangleCanBePlaced(Box candidateRect, List<Box> placedRectangles,
      double containerWidth, double containerHeight) {
    if (candidateRect.getCenter().x() + candidateRect.getSize().x() / 2 > containerWidth)
      return false;

    if (candidateRect.getCenter().z() + candidateRect.getSize().z() / 2 > containerHeight)
      return false;

    for (Box placedRect : placedRectangles) {
      if (boxesOverlapInXZPlane(candidateRect, placedRect))
        return false;
    }
    return true;
  }

  private static boolean boxesOverlapInXZPlane(Box box1, Box box2) {
    double box1Left = box1.getCenter().x() - box1.getSize().x() / 2;
    double box1Right = box1.getCenter().x() + box1.getSize().x() / 2;
    double box1Top = box1.getCenter().z() - box1.getSize().z() / 2;
    double box1Bottom = box1.getCenter().z() + box1.getSize().z() / 2;

    double box2Left = box2.getCenter().x() - box2.getSize().x() / 2;
    double box2Right = box2.getCenter().x() + box2.getSize().x() / 2;
    double box2Top = box2.getCenter().z() - box2.getSize().z() / 2;
    double box2Bottom = box2.getCenter().z() + box2.getSize().z() / 2;

    return box1Left < box2Right && box1Right > box2Left &&
        box1Top > box2Bottom && box1Bottom < box2Top;
  }
}
