package org.codevillage;

import java.awt.event.KeyEvent;
import java.util.concurrent.atomic.AtomicBoolean;

public class VerticalMovementController extends HorizontalMovementController
{
    protected AtomicBoolean spaceBarIsPressed = new AtomicBoolean(false);
    protected AtomicBoolean shiftIsPressed = new AtomicBoolean(false);
    public VerticalMovementController(float stepSize)
    {
        super(stepSize);
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        super.keyPressed(e);
        switch (e.getKeyCode())
        {
            case KeyEvent.VK_SPACE:
                spaceBarIsPressed.set(true);
                break;
            case KeyEvent.VK_SHIFT:
                shiftIsPressed.set(true);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        super.keyReleased(e);
        switch (e.getKeyCode())
        {
            case KeyEvent.VK_SPACE:
                spaceBarIsPressed.set(false);
                break;
            case KeyEvent.VK_SHIFT:
                shiftIsPressed.set(false);
                break;
        }
    }

    public boolean getSpaceBarIsPressed()
    {
        return this.spaceBarIsPressed.get();
    }

    public boolean getShiftIsPressed()
    {
        return this.shiftIsPressed.get();
    }
}
