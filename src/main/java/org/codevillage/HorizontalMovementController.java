package org.codevillage;

import com.jogamp.opengl.math.Vec2f;
import com.jogamp.opengl.math.Vec3f;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicBoolean;

public class HorizontalMovementController implements MovementController
{
    Vec3f UP_VECTOR = new Vec3f(0, 1, 0);

    protected float stepSize;
    protected AtomicBoolean wIsPressed = new AtomicBoolean(false);
    protected AtomicBoolean aIsPressed = new AtomicBoolean(false);
    protected AtomicBoolean sIsPressed = new AtomicBoolean(false);
    protected AtomicBoolean dIsPressed = new AtomicBoolean(false);

    // we eventually want to create a second constructor that accepts a bounding box argument too
    // once the Box class is implemented
    public HorizontalMovementController(float stepSize)
    {
        this.stepSize = stepSize;
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
        Vec3f forwardVector = MovementController.calculateOpenGLForwardVectorFromPitchAndYaw(currentRotationRads.x(), currentRotationRads.y());
        Vec3f rightVector = new Vec3f();
        rightVector.cross(UP_VECTOR, forwardVector);
        rightVector.normalize();

        forwardVector.scale(stepSize);
        rightVector.scale(stepSize);

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
        return null;
    }

    @Override
    public void keyTyped(KeyEvent e)
    { }

    @Override
    public void keyPressed(KeyEvent e)
    {
        switch (e.getKeyCode())
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
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        switch (e.getKeyCode())
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
        }
    }

    @Override
    public void mouseClicked(MouseEvent e)
    { }

    @Override
    public void mousePressed(MouseEvent e)
    { }

    @Override
    public void mouseReleased(MouseEvent e)
    { }

    @Override
    public void mouseEntered(MouseEvent e)
    { }

    @Override
    public void mouseExited(MouseEvent e)
    { }

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
}
