package org.codevillage;

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
}
