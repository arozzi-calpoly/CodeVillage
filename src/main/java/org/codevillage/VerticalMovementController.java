package org.codevillage;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.opengl.math.Vec2f;
import com.jogamp.opengl.math.Vec3f;

import java.util.concurrent.atomic.AtomicBoolean;

public class VerticalMovementController extends HorizontalMovementController
{
    protected AtomicBoolean spaceBarIsPressed = new AtomicBoolean(false);
    protected AtomicBoolean shiftIsPressed = new AtomicBoolean(false);
    public VerticalMovementController(float translationStepSize, float keyboardRotationStepSize, float mouseRotationStepSize)
    {
        super(translationStepSize, keyboardRotationStepSize, mouseRotationStepSize);
    }

    @Override
    public synchronized Vec3f getNextPosition(Vec3f currentPosition, Vec2f currentRotationRads)
    {
        Vec3f nextPosition = new Vec3f(currentPosition);
        Vec3f forwardVector =
                // MovementController.calculateOpenGLForwardVectorFromPitchAndYaw(currentRotationRads.x(), currentRotationRads.y());
                MovementController.calculateOpenGLForwardVectorFromPitchAndYaw(0, currentRotationRads.y());

        Vec3f rightVector = new Vec3f();
        rightVector.cross(forwardVector, UP_VECTOR);
        rightVector.normalize();

        Vec3f up = new Vec3f(UP_VECTOR);
        up.scale(translationStepSize);

        forwardVector.scale(translationStepSize);
        rightVector.scale(translationStepSize);

        if(wIsPressed.get())
            nextPosition.add(forwardVector);
        if(sIsPressed.get())
            nextPosition.sub(forwardVector);
        if(dIsPressed.get())
            nextPosition.add(rightVector);
        if(aIsPressed.get())
            nextPosition.sub(rightVector);
        if(spaceBarIsPressed.get())
            nextPosition.add(up);
        if(shiftIsPressed.get())
            nextPosition.sub(up);

        return nextPosition;
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
