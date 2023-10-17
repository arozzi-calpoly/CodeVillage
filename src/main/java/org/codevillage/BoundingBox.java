package org.codevillage;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.math.Matrix4f;
import com.jogamp.opengl.math.Vec3f;

import java.awt.*;
import java.util.List;

/**
 * represents a package that encompasses other packages and classes
 * in a graphical representation
 */
public class BoundingBox extends Box {
    private Vec3f position;
    private float y;
    private boolean isSelected;
    private Color color;
    private List<Box> subBoxes;

    public BoundingBox(Vec3f position, float y, Color color) {
        super(position, y, color);
        isSelected = false;
    }

    public void draw(GL4 gl, StaticMVPShader shader, Matrix4f viewMatrix, Matrix4f projectionMatrix,
                     Texture modelTexture, Vec3f lightDirection, Model3D cubeModel) {
        super.draw(gl, shader, viewMatrix, projectionMatrix, modelTexture, lightDirection, cubeModel);
    }

    public List<Box> getSubBoxes() {
        return this.subBoxes;
    }

    public void addSubBoxes(Box box) {
        this.subBoxes.add(box);
    }
}
