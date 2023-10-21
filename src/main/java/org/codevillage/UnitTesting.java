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
import java.util.List;

public class UnitTesting
{
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
        GLTestSimpleShaderEventListener testSimpleShaderEventListener =
                new GLTestSimpleShaderEventListener(movementController);
        animator.start();
        window.addKeyListener(movementController);
        window.addMouseListener(movementController);
        window.addGLEventListener(testSimpleShaderEventListener);
    }

    private static class GLTestSimpleShaderEventListener implements GLEventListener
    {
        private final MovementController movementController;
        private List<Box> boxes;

        public GLTestSimpleShaderEventListener(MovementController movementController)
        {
            int NUM_CUBES_TO_ADD = 10000;
            int cubesPerDimension = (int) Math.pow(NUM_CUBES_TO_ADD, 1. / 3);
            this.movementController = movementController;
            this.boxes = new ArrayList<>();
            for (int i = -cubesPerDimension / 2; i <= cubesPerDimension / 2; i++) {
                for (int j = -cubesPerDimension / 2; j <= cubesPerDimension / 2; j++) {
                    for (int k = -cubesPerDimension / 2; k <= cubesPerDimension / 2; k++) {
                        boxes.add(new Box(
                                new Vec3f(i, j, k),
                                new Vec3f(0.05f, 0.05f, 0.05f),
                                Color.getHSBColor((float) Math.random(), 1, 1)));
                    }
                }
            }
        }

        private StaticMVPShader shader;
        private Vec3f lightDirection;
        private Vec3f eyePosition;
        private Vec2f eyeRotation;

        @Override
        public void init(GLAutoDrawable glAutoDrawable)
        {
            GL4 gl = glAutoDrawable.getGL().getGL4();
            InputStream vertexShaderStream =
                    ClassLoader.getSystemClassLoader().getResourceAsStream("shaders/simple_vertex_shader.glsl");
            InputStream fragmentShaderStream =
                    ClassLoader.getSystemClassLoader().getResourceAsStream("shaders/simple_fragment_shader.glsl");
            shader = new StaticMVPShader(gl, vertexShaderStream, fragmentShaderStream);
            lightDirection = new Vec3f(1, -1, -2);
            eyePosition = new Vec3f(0, 0.05f, 3);
            eyeRotation = new Vec2f(0, 0);
        }

        @Override
        public void dispose(GLAutoDrawable glAutoDrawable)
        {
            // Free the VAOs, textures, whatever resources you were using with the given GL4 instance
            GL4 gl = glAutoDrawable.getGL().getGL4();
            System.out.println("Disposed");
            ModelLoader.cleanUp(gl);
            System.exit(0);
        }

        @Override
        public void display(GLAutoDrawable glAutoDrawable)
        {
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

            Matrix4f projectionMatrix = new Matrix4f().loadIdentity();
            projectionMatrix.setToPerspective(FovHVHalves.byRadians((float) (Math.PI / 2), (float) (Math.PI / 2)),
                    0.1f, 100f);

            Matrix4f viewMatrix = createViewMatrixFromEye(eyePosition, eyeRotation);
            shader.start(gl);
            shader.loadEyePosition(gl, eyePosition);
            shader.loadLightDirection(gl, lightDirection);
            // There are the constants used in the original Lorenz Attractor
            double sigma = 10;
            double rho = 28;
            double beta = 8. / 3;
            for (Box box : this.boxes) {
                boolean isVisible = Math.sin((box.hashCode() + System.currentTimeMillis() / 1000.)) > 0;
                box.setVisible(isVisible);

                Vec3f position = box.getPosition();
                double xStep = sigma * (position.y() - position.x());
                double yStep = position.x() * (rho - position.z()) - position.y();
                double zStep = position.x() * position.y() - beta * position.z();
                Vec3f offset = new Vec3f((float) xStep, (float) yStep, (float) zStep);
                offset.scale(0.001f);
                position.add(offset);
                box.draw(gl, shader, viewMatrix, projectionMatrix);
                // position.sub(offset);
            }
            shader.stop(gl);
        }

        @Override
        public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3)
        {
        }

        public static Matrix4f createModelTransformationMatrix(Vec3f translation, float rx, float ry,
                                                               float rz, float scale)
        {
            Matrix4f matrix = new Matrix4f();
            matrix.loadIdentity();
            matrix.translate(translation, new Matrix4f());
            matrix.mul(new Matrix4f().setToRotationEuler(rx, 0, 0));
            matrix.mul(new Matrix4f().setToRotationEuler(0, ry, 0));
            matrix.mul(new Matrix4f().setToRotationEuler(0, 0, rz));
            matrix.scale(scale, new Matrix4f());
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

    public static void testGraphicsWindowCreation()
    {
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
        GLTestWindowCreationEventListener windowCreationEventListener =
                new GLTestWindowCreationEventListener();
        animator.start();
        // window.addKeyListener(windowCreationEventListener);
        window.addGLEventListener(windowCreationEventListener);
    }

    private static class GLTestWindowCreationEventListener implements GLEventListener
    {
        @Override
        public void init(GLAutoDrawable glAutoDrawable)
        {
        }

        @Override
        public void dispose(GLAutoDrawable glAutoDrawable)
        {
            System.out.println("Disposed");
            // Free the VAOs, textures, whatever resources you were using with the GL4 instance
            // given by 'glAutoDrawable.getGL().getGL4()'
            System.exit(0);
        }

        @Override
        public void display(GLAutoDrawable glAutoDrawable)
        {
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
        public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3)
        {
        }
    }
}
