package org.codevillage;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

public abstract class MovementController implements KeyListener, MouseListener
{
    /**
     * Gets the next position for a given point
     * @param currentPosition The point's current position
     * @return Where that point is moved to
     */
    public abstract Point3d getNextPosition(Point3d currentPosition);

    /**
     * Gets the next (yaw, pitch) angles given the current angles
     * @param currentRotation The current yaw and pitch angles, in radians,
     *                        where the yaw is in the x component and the pitch is in the y component
     * @return The next yaw and pitch angles, in the same format as provided
     */
    public abstract Point2d getNextRotation(Point2d currentRotation);
}
