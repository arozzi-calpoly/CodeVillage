package org.codevillage;

import com.jogamp.opengl.math.Vec3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class BoundingBox extends Box
{
    private List<Box> boundBoxes;

    public BoundingBox(Vec3f position, Vec3f size)
    {
        super(position, size, new Color(0, 0, 0, 0));
        boundBoxes = new ArrayList<>();
    }

    public void addBoundBox(Box box)
    {
        boundBoxes.add(box);
    }

    public List<Box> getBoundBoxes()
    {
        return boundBoxes;
    }

}
