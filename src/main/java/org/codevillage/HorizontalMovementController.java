package org.codevillage;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.opengl.math.Vec2f;
import com.jogamp.opengl.math.Vec3f;

import java.util.concurrent.atomic.AtomicBoolean;

public class HorizontalMovementController implements MovementController
{
    Vec3f UP_VECTOR = new Vec3f(0, 1, 0);

    protected float translationStepSize;
    protected float rotationStepSize;

    protected AtomicBoolean wIsPressed = new AtomicBoolean(false);
    protected AtomicBoolean aIsPressed = new AtomicBoolean(false);
    protected AtomicBoolean sIsPressed = new AtomicBoolean(false);
    protected AtomicBoolean dIsPressed = new AtomicBoolean(false);



    protected AtomicBoolean upIsPressed = new AtomicBoolean(false);
    protected AtomicBoolean downIsPressed = new AtomicBoolean(false);
    protected AtomicBoolean leftIsPressed = new AtomicBoolean(false);
    protected AtomicBoolean rightIsPressed = new AtomicBoolean(false);


    // we eventually want to create a second constructor that accepts a bounding box argument too
    // once the Box class is implemented
    public HorizontalMovementController(float translationStepSize, float rotationStepSize)
    {
        this.translationStepSize = translationStepSize;
        this.rotationStepSize = rotationStepSize;
        // this.boundingBox = null;
    }

    /*
    public HorizontalMovementController(double stepSize, Box boundingBox)
    {
        this.stepSize = stepSize;
        this.boundingBox = boundingBox;
    }
    */

    @Override
    public Vec3f getNextPosition(Vec3f currentPosition, Vec2f currentRotationRads)
    {
        Vec3f nextPosition = new Vec3f(currentPosition);
        Vec3f forwardVector =
                MovementController.calculateOpenGLForwardVectorFromPitchAndYaw(currentRotationRads.x(), currentRotationRads.y());
                // MovementController.calculateOpenGLForwardVectorFromPitchAndYaw(0, currentRotationRads.y());

        Vec3f rightVector = new Vec3f();
        rightVector.cross(forwardVector, UP_VECTOR);
        rightVector.normalize();

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

        return nextPosition;
    }

    @Override
    public Vec2f getNextRotation(Vec3f currentPosition, Vec2f currentRotation)
    {
        float pitchRads = currentRotation.x();
        float yawRads = currentRotation.y();

        if(upIsPressed.get())
            pitchRads += rotationStepSize;
        if(downIsPressed.get())
            pitchRads -= rotationStepSize;

        if(leftIsPressed.get())
            yawRads += rotationStepSize;

        if(rightIsPressed.get())
            yawRads -= rotationStepSize;

        return new Vec2f(pitchRads, yawRads);
    }

    public boolean getWIsPressed()
    {
        return wIsPressed.get();
    }

    public boolean getAIsPressed()
    {
        return aIsPressed.get();
    }

    public boolean getSIsPressed()
    {
        return sIsPressed.get();
    }

    public boolean getDIsPressed()
    {
        return dIsPressed.get();
    }

    @Override
    public void keyPressed(KeyEvent keyEvent)
    {
        if(keyEvent.isAutoRepeat())
            return;

        switch (keyEvent.getKeyCode())
        {
            case KeyEvent.VK_W:
                wIsPressed.set(true);
                break;
            case KeyEvent.VK_A:
                aIsPressed.set(true);
                break;
            case KeyEvent.VK_S:
                sIsPressed.set(true);
                break;
            case KeyEvent.VK_D:
                dIsPressed.set(true);
                break;

            case KeyEvent.VK_UP:
                upIsPressed.set(true);
                break;
            case KeyEvent.VK_DOWN:
                downIsPressed.set(true);
                break;
            case KeyEvent.VK_LEFT:
                leftIsPressed.set(true);
                break;
            case KeyEvent.VK_RIGHT:
                rightIsPressed.set(true);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent)
    {
        if(keyEvent.isAutoRepeat())
            return;

        switch (keyEvent.getKeyCode())
        {
            case KeyEvent.VK_W:
                wIsPressed.set(false);
                break;
            case KeyEvent.VK_A:
                aIsPressed.set(false);
                break;
            case KeyEvent.VK_S:
                sIsPressed.set(false);
                break;
            case KeyEvent.VK_D:
                dIsPressed.set(false);
                break;

            case KeyEvent.VK_UP:
                upIsPressed.set(false);
                break;
            case KeyEvent.VK_DOWN:
                downIsPressed.set(false);
                break;
            case KeyEvent.VK_LEFT:
                leftIsPressed.set(false);
                break;
            case KeyEvent.VK_RIGHT:
                rightIsPressed.set(false);
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent)
    { }

    @Override
    public void mouseEntered(MouseEvent mouseEvent)
    { }

    @Override
    public void mouseExited(MouseEvent mouseEvent)
    { }

    @Override
    public void mousePressed(MouseEvent mouseEvent)
    { }

    @Override
    public void mouseReleased(MouseEvent mouseEvent)
    { }

    @Override
    public void mouseMoved(MouseEvent mouseEvent)
    { }

    @Override
    public void mouseDragged(MouseEvent mouseEvent)
    { }

    @Override
    public void mouseWheelMoved(MouseEvent mouseEvent)
    { }
}
