package twoDimensional;

import javax.swing.*;
import java.awt.*;

/**
 * 2-dimensional graphics representation
 */

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Square Box App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        mainPanel.setPreferredSize(new Dimension(500, 500));

        Box box1 = new Box(Color.BLUE, 1);
        Box box2 = new Box(Color.BLUE, 1);
        mainPanel.add(box1);
        mainPanel.add(box2);

        // testing a bounding box (package w/ classes)
        Box boundingBox = new Box(Color.GREEN, 3);
        boundingBox.setLayout(new FlowLayout(FlowLayout.CENTER)); // Divide the large box into 3 columns

        Box inner1 = new Box(Color.RED, 1);
        Box inner2 = new Box(Color.ORANGE, 1);

        boundingBox.add(inner1);
        boundingBox.add(inner2);
        mainPanel.add(boundingBox, BorderLayout.CENTER);

        frame.add(mainPanel);
        frame.setVisible(true);
    }
}