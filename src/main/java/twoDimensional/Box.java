package twoDimensional;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 2-dimensional box representation
 */

public class Box extends JPanel {
    private Color color;

    public Box(Color defaultColor, int size) {
        // box size will be based on # methods & attribute
        int dim = 100*size;
        this.setPreferredSize(new Dimension(dim, dim));
        this.color = defaultColor;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                color = Color.YELLOW;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK); // Border color
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        g.setColor(color);
        g.fillRect(1, 1, getWidth() - 2, getHeight() - 2);
    }
}
