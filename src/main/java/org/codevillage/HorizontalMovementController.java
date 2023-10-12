package org.codevillage;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicBoolean;

public class HorizontalMovementController extends MovementController
{
    Vector3d UP_VECTOR = new Vector3d(0, 1, 0);

    protected double stepSize;
    protected AtomicBoolean wIsPressed = new AtomicBoolean(false);
    protected AtomicBoolean aIsPressed = new AtomicBoolean(false);
    protected AtomicBoolean sIsPressed = new AtomicBoolean(false);
    protected AtomicBoolean dIsPressed = new AtomicBoolean(false);

    // we eventually want to create a second constructor that accepts a bounding box argument too
    // once the Box class is implemented
    public HorizontalMovementController(double stepSize)
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
    public Point3d getNextPosition(Point3d currentPosition, Point2d currentRotationRads)
    {
        Point3d nextPosition = new Point3d(currentPosition);
        Vector3d forwardVector = calculateJava3DForwardVectorFromPitchAndYaw(currentRotationRads.x, currentRotationRads.y);
        Vector3d rightVector = new Vector3d();
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
    public Point2d getNextRotation(Point3d currentPosition, Point2d currentRotation)
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
}
