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
  public static void testSimpleShader(MovementController movementController)
  {
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
    private List<Box> cubePositions;

    private List<Box> generateCubePositions(int columns, int rows, float spacing) {
      List<Box> cubePositions = new ArrayList<>();

      float startX = -((columns - 1) * spacing) / 2.0f;
      float startZ = -((rows - 1) * spacing) / 2.0f;

      for (int i = 0; i < rows; i++) {
        for (int j = 0; j < columns; j++) {
          float x = startX + j * spacing;
          float z = startZ + i * spacing;
          float y = 0.0f;

          Box box = new Box(new Vec3f(x, y, z), 1.0f, Color.RED);

          cubePositions.add(box);
        }
      }

      return cubePositions;
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
      GL4 gl = glAutoDrawable.getGL().getGL4();
      String vertexShaderPath = "/Users/abrahamdev/Desktop/csc509/csc509-visuals/src/main/resources/shaders/simple_vertex_shader.glsl";
      String fragmentShaderPath = "/Users/abrahamdev/Desktop/csc509/csc509-visuals/src/main/resources/shaders/simple_fragment_shader.glsl";
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

      cubePositions = generateCubePositions(5, 5, 5);
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
      gl.glEnable(GL.GL_BLEND);
      gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
      gl.glEnable(GL.GL_DEPTH_TEST);
      gl.glEnable(GL.GL_MULTISAMPLE);

      eyePosition = movementController.getNextPosition(eyePosition, eyeRotation);
      eyeRotation = movementController.getNextRotation(eyePosition, eyeRotation);

      Matrix4f projectionMatrix = new Matrix4f().loadIdentity();
      projectionMatrix.setToPerspective(FovHVHalves.byRadians((float) (Math.PI / 2), (float) (Math.PI / 2)), 0.1f,
          100f);

      Matrix4f viewMatrix = createTransformationMatrix(new Vec3f(eyePosition).scale(-1), -eyeRotation.x(),
          -eyeRotation.y(), 0, 1);

      // Render multiple cubes
      for (Box cubePosition : cubePositions) {
        cubePosition.draw(gl, shader, viewMatrix, projectionMatrix, modelTexture, lightDirection, cubeModel);
      }

      Matrix4f modelMatrix = new Matrix4f().loadIdentity();

      shader.start(gl);
      shader.loadModelViewProjectionMatrices(gl, modelMatrix, viewMatrix, projectionMatrix);
      shader.loadModelTexture(gl, modelTexture);
      shader.loadLightDirection(gl, lightDirection);

      gl.glBindVertexArray(cubeModel.getVaoID());
      groundModel.enableAllVertexAttributeArrays(gl);
      shader.enableAllTextures(gl);
      gl.glDrawElements(GL4.GL_TRIANGLES, groundModel.getVertexCount(), GL.GL_UNSIGNED_INT, 0);
      groundModel.disableAllVertexAttributeArrays(gl);
      gl.glBindVertexArray(0);

      shader.stop(gl);

    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {
    }

    public static Matrix4f createTransformationMatrix(Vec3f translation, float rx, float ry,
        float rz, float scale) {
      Matrix4f matrix = new Matrix4f();
      matrix.loadIdentity();
      matrix.scale(scale, new Matrix4f());
      matrix.mul(new Matrix4f().setToRotationEuler(rx, 0, 0));
      matrix.mul(new Matrix4f().setToRotationEuler(0, ry, 0));
      matrix.mul(new Matrix4f().setToRotationEuler(0, 0, rz));
      matrix.translate(translation, new Matrix4f());
      return matrix;
    }
  }
        public static Matrix4f createModelTransformationMatrix(Vec3f translation, float rx, float ry,
                                                               float rz, float scaleX, float scaleY, float scaleZ)
        {
            Matrix4f matrix = new Matrix4f();
            matrix.loadIdentity();
            matrix.translate(translation, new Matrix4f());
            matrix.mul(new Matrix4f().setToRotationEuler(rx, 0, 0));
            matrix.mul(new Matrix4f().setToRotationEuler(0, ry, 0));
            matrix.mul(new Matrix4f().setToRotationEuler(0, 0, rz));
            matrix.scale(scaleX, scaleY, scaleZ, new Matrix4f());
            return matrix;
        }

        public static Matrix4f createViewMatrixFromEye(Vec3f eyePosition, Vec2f eyeRotation)
        {
            Matrix4f matrix = new Matrix4f();
            matrix.loadIdentity();
            matrix.mul(new Matrix4f().setToRotationEuler(-eyeRotation.x(), 0, 0));
            matrix.mul(new Matrix4f().setToRotationEuler(0, -eyeRotation.y(), 0));
            matrix.translate(new Vec3f(eyePosition).scale(-1), new Matrix4f());
            return matrix;
        }
    }

  // public static void testGraphicsWindowCreation() {
  // GLProfile glProfile = GLProfile.get(GLProfile.GL4);
  //
  // GLCapabilities glCapabilities = new GLCapabilities(glProfile);
  // glCapabilities.setDoubleBuffered(true);
  // glCapabilities.setHardwareAccelerated(true);
  //
  // String windowTitle = "Window Creation Test";
  // Display display = NewtFactory.createDisplay(windowTitle);
  // Screen screen = NewtFactory.createScreen(display, 0);
  //
  // int WINDOW_WIDTH = 800, WINDOW_HEIGHT = 600;
  // GLWindow window = GLWindow.create(screen, glCapabilities);
  // window.setTitle(windowTitle);
  // window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
  // window.setVisible(true);
  //
  // FPSAnimator animator = new FPSAnimator(window, 30);
  // GLTestWindowCreationEventListener windowCreationEventListener = new
  // GLTestWindowCreationEventListener();
  // animator.start();
  // // window.addKeyListener(windowCreationEventListener);
  // window.addGLEventListener(windowCreationEventListener);
  // }
  //
  // private static class GLTestWindowCreationEventListener implements
  // GLEventListener {
  // @Override
  // public void init(GLAutoDrawable glAutoDrawable) {
  // }
  //
  // @Override
  // public void dispose(GLAutoDrawable glAutoDrawable) {
  // System.out.println("Disposed");
  // // Free the VAOs, textures, whatever resources you were using with the GL4
  // // instance
  // // given by 'glAutoDrawable.getGL().getGL4()'
  // System.exit(0);
  // }
  //
  // @Override
  // public void display(GLAutoDrawable glAutoDrawable) {
  // GL4 gl = glAutoDrawable.getGL().getGL4();
  // double redComponent = Math.sin(System.currentTimeMillis() / 1000.0 * Math.PI)
  // * 0.5 + 0.5;
  // gl.glClearColor((float) redComponent, 0, 0, 0);
  // gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
  // // configs alpha blending settings
  // gl.glEnable(GL.GL_BLEND);
  // gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
  // // turns on depth testing
  // gl.glEnable(GL.GL_DEPTH_TEST);
  // // enables multi-sample antialiasing (if the GLCapabilities object set it up)
  // gl.glEnable(GL.GL_MULTISAMPLE);
  // }
  //
  // @Override
  // public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int
  // i3) {
  // }
  // }
}
