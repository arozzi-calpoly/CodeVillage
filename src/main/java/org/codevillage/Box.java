package org.codevillage;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.math.Matrix4f;
import com.jogamp.opengl.math.Vec3f;

import java.awt.*;

public class Box
{
    private Vec3f position;
    private float y;
    private boolean isSelected;
    private Color color;

    public Box(Vec3f position, float y, Color color)
    {
        this.position = position;
        this.y = y;
        this.color = color;
        isSelected = false;
    }

    public void draw(GL4 gl, StaticMVPShader shader, Matrix4f viewMatrix, Matrix4f projectionMatrix,
                     Texture modelTexture, Vec3f lightDirection, Model3D cubeModel)
    {
        Matrix4f modelMatrix = new Matrix4f()
                .loadIdentity()
                .translate(position, new Matrix4f())
                .scale(y, new Matrix4f());

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
