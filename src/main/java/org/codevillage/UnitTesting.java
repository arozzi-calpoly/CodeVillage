package org.codevillage;

import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class UnitTesting
{
    public static void testHorizontalMovementControllerKeyPresses()
    {
        JFrame frame = new JFrame();
        frame.setSize(500, 500);
        JPanel mainPanel = new JPanel(new GridLayout(4, 2));
        JLabel wIsPressedLabel = new JLabel();
        JLabel aIsPressedLabel = new JLabel();
        JLabel sIsPressedLabel = new JLabel();
        JLabel dIsPressedLabel = new JLabel();

        mainPanel.setBorder(new CompoundBorder(
                new EmptyBorder(15, 15, 15, 15),
                new TitledBorder("Keys:")
        ));

        mainPanel.add(new JLabel("W:"));
        mainPanel.add(wIsPressedLabel);
        mainPanel.add(new JLabel("A:"));
        mainPanel.add(aIsPressedLabel);
        mainPanel.add(new JLabel("S:"));
        mainPanel.add(sIsPressedLabel);
        mainPanel.add(new JLabel("D:"));
        mainPanel.add(dIsPressedLabel);

        HorizontalMovementController horizontalMovementController = new HorizontalMovementController(1);
        frame.addKeyListener(horizontalMovementController);

        Thread thread = new Thread(() -> {
            while(true)
            {
                wIsPressedLabel.setText("" + horizontalMovementController.getWIsPressed());
                aIsPressedLabel.setText("" + horizontalMovementController.getAIsPressed());
                sIsPressedLabel.setText("" + horizontalMovementController.getSIsPressed());
                dIsPressedLabel.setText("" + horizontalMovementController.getDIsPressed());
                try{
                    Thread.sleep(50);
                }
                catch (InterruptedException e) {
                    break;
                }
            }
        });
        thread.setDaemon(true);
        thread.start();

        frame.getContentPane().add(mainPanel);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

    public static void testVerticalMovementControllerKeyPresses()
    {
        JFrame frame = new JFrame();
        frame.setSize(500, 500);
        JPanel mainPanel = new JPanel(new GridLayout(6, 2));
        JLabel wIsPressedLabel = new JLabel();
        JLabel aIsPressedLabel = new JLabel();
        JLabel sIsPressedLabel = new JLabel();
        JLabel dIsPressedLabel = new JLabel();
        JLabel spaceIsPressedLabel = new JLabel();
        JLabel shiftIsPressedLabel = new JLabel();

        mainPanel.setBorder(new CompoundBorder(
                new EmptyBorder(15, 15, 15, 15),
                new TitledBorder("Keys:")
        ));

        mainPanel.add(new JLabel("W:"));
        mainPanel.add(wIsPressedLabel);
        mainPanel.add(new JLabel("A:"));
        mainPanel.add(aIsPressedLabel);
        mainPanel.add(new JLabel("S:"));
        mainPanel.add(sIsPressedLabel);
        mainPanel.add(new JLabel("D:"));
        mainPanel.add(dIsPressedLabel);
        mainPanel.add(new JLabel("Space:"));
        mainPanel.add(spaceIsPressedLabel);
        mainPanel.add(new JLabel("Shift:"));
        mainPanel.add(shiftIsPressedLabel);

        VerticalMovementController verticalMovementController = new VerticalMovementController(1);
        frame.addKeyListener(verticalMovementController);

        Thread thread = new Thread(() -> {
            while(true)
            {
                wIsPressedLabel.setText("" + verticalMovementController.getWIsPressed());
                aIsPressedLabel.setText("" + verticalMovementController.getAIsPressed());
                sIsPressedLabel.setText("" + verticalMovementController.getSIsPressed());
                dIsPressedLabel.setText("" + verticalMovementController.getDIsPressed());
                spaceIsPressedLabel.setText("" + verticalMovementController.getSpaceBarIsPressed());
                shiftIsPressedLabel.setText("" + verticalMovementController.getShiftIsPressed());
                try{
                    Thread.sleep(50);
                }
                catch (InterruptedException e) {
                    break;
                }
            }
        });
        thread.setDaemon(true);
        thread.start();

        frame.getContentPane().add(mainPanel);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
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
        { }

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
        { }
    }
}
