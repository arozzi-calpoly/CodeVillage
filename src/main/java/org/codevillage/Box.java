package org.codevillage;

import com.jogamp.opengl.math.Vec3f;

public class Box
{
    private Vec3f position;
    private Vec3f size;

    public Box(Vec3f position, Vec3f size)
    {
        this.position = position;
        this.size = size;
    }

    public Vec3f getPosition()
    {
        return position;
    }

    public Vec3f getSize()
    {
        return size;
    }

    public void setPosition(Vec3f position)
    {
        this.position = position;
    }

    public void setSize(Vec3f size)
    {
        this.size = size;
    }

}
