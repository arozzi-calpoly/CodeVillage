package org.codevillage;

import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.opengl.math.Vec2f;
import com.jogamp.opengl.math.Vec3f;


public interface MovementController extends KeyListener, MouseListener
{
    /**
     * Gets the next position for a given point
     * @param currentPosition The point's current position
     * @param currentRotationRads The current yaw and pitch angles, in radians,
     *      *                     where the pitch is in the x component and the yaw is in the y component
     * @return Where that point is moved to
     */
    Vec3f getNextPosition(Vec3f currentPosition, Vec2f currentRotationRads);

    /**
     * Gets the next (pitch, yaw) angles given the current angles
     * @param currentPosition The point's current position
     * @param currentRotation The current pitch and yaw angles, in radians,
     *                        where the pitch is in the x component and the yaw is in the y component
     * @return The next pitch and yaw angles, in the same format as provided
     */
    Vec2f getNextRotation(Vec3f currentPosition, Vec2f currentRotation);

    static Vec3f calculateOpenGLForwardVectorFromPitchAndYaw(double pitchRads, double yawRads)
    {
        double cosPitch = Math.cos(pitchRads);
        float x = (float) (cosPitch * Math.sin(yawRads));
        float y = (float) Math.sin(pitchRads);
        float z = (float) (cosPitch * Math.cos(yawRads));

        // The extra multiplications with -1 is because OpenGL's default looking direction is along the -Z axis
        x *= -1;
        z *= -1;
        return new Vec3f(x, y, z);
    }
}
