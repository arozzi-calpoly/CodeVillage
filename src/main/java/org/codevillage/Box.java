package org.codevillage;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.math.Matrix4f;
import com.jogamp.opengl.math.Vec3f;

import java.awt.*;

public class Box
{
    private static Model3D cubeMesh;
    private Vec3f position;
    private Vec3f size;
    private boolean isSelected;
    private Color color;

    private boolean isVisible;

    public Box(Vec3f position, Vec3f size, Color color)
    {
        this.position = position;
        this.size = size;
        this.color = color;
        this.isSelected = false;
        this.isVisible = true;
    }

    public boolean isVisible()
    {
        return isVisible;
    }

    public void setVisible(boolean visible)
    {
        isVisible = visible;
    }

    public boolean isSelected()
    {
        return isSelected;
    }

    public void setSelected(boolean selected)
    {
        isSelected = selected;
    }

    public Color getColor()
    {
        return color;
    }

    public void setColor(Color color)
    {
        this.color = color;
    }

    private static synchronized Model3D getBoxMesh(GL4 gl)
    {
        if (cubeMesh == null) {
            cubeMesh = RenderingGeometryLib.generateCubeModel(gl);
        }
        return cubeMesh;
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

    public void draw(GL4 gl, StaticMVPShader shader, Matrix4f viewMatrix, Matrix4f projectionMatrix)
    {
        if (!isVisible)
            return;

        Matrix4f modelMatrix = new Matrix4f()
                .loadIdentity()
                .translate(position, new Matrix4f())
                .scale(size.x(), size.y(), size.z(), new Matrix4f());

        Model3D cubeModel = getBoxMesh(gl);
        shader.loadModelViewProjectionMatrices(gl, modelMatrix, viewMatrix, projectionMatrix);
        shader.loadModelColor(gl, this.color);
        gl.glBindVertexArray(cubeModel.getVaoID());
        cubeModel.enableAllVertexAttributeArrays(gl);
        shader.enableAllTextures(gl);
        gl.glDrawElements(GL4.GL_TRIANGLES, cubeModel.getVertexCount(), GL.GL_UNSIGNED_INT, 0);
        cubeModel.disableAllVertexAttributeArrays(gl);
        gl.glBindVertexArray(0);
    }
}