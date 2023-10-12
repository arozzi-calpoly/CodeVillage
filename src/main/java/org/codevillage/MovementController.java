package org.codevillage;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

public abstract class MovementController implements KeyListener, MouseListener
{
    /**
     * Gets the next position for a given point
     * @param currentPosition The point's current position
     * @param currentRotationRads The current yaw and pitch angles, in radians,
     *      *                     where the pitch is in the x component and the yaw is in the y component
     * @return Where that point is moved to
     */
    public abstract Point3d getNextPosition(Point3d currentPosition, Point2d currentRotationRads);

    /**
     * Gets the next (yaw, pitch) angles given the current angles
     * @param currentPosition The point's current position
     * @param currentRotation The current yaw and pitch angles, in radians,
     *                        where the pitch is in the x component and the yaw is in the y component
     * @return The next yaw and pitch angles, in the same format as provided
     */
    public abstract Point2d getNextRotation(Point3d currentPosition, Point2d currentRotation);

    public static Vector3d calculateJava3DForwardVectorFromPitchAndYaw(double pitchRads, double yawRads)
    {
        double cosPitch = Math.cos(pitchRads);
        double x = cosPitch * Math.sin(yawRads);
        double y = Math.sin(pitchRads);
        double z = cosPitch * Math.cos(yawRads);

        // The extra multiplications with -1 is because OpenGL's default looking direction is along the -Z axis
        x *= -1;
        z *= -1;
        return new Vector3d(x, y, z);
    }
}
