package org.codevillage;

import com.jogamp.newt.event.KeyEvent;

import java.util.concurrent.atomic.AtomicBoolean;

public class VerticalMovementController extends HorizontalMovementController
{
    protected AtomicBoolean spaceBarIsPressed = new AtomicBoolean(false);
    protected AtomicBoolean shiftIsPressed = new AtomicBoolean(false);
    public VerticalMovementController(float translationStepSize, float rotationStepSize)
    {
        super(translationStepSize, rotationStepSize);
    }

    public void keyPressed(KeyEvent keyEvent)
    {
        if(keyEvent.isAutoRepeat())
            return;

        super.keyPressed(keyEvent);
        switch (keyEvent.getKeyCode())
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
    public void keyReleased(KeyEvent keyEvent)
    {
        if(keyEvent.isAutoRepeat())
            return;

        super.keyReleased(keyEvent);
        switch (keyEvent.getKeyCode())
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
