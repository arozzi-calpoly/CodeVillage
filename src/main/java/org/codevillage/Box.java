package org.codevillage;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.math.Matrix4f;
import com.jogamp.opengl.math.Vec3f;

public class Box {
    private Vec3f position;
    private float x;
    private float y;
    private float z;
    private boolean isSelected;

    public Box(Vec3f position, float x, float y, float z) {
        this.position = position;
        this.x = x;
        this.y = y;
        this.z = z;
        isSelected = false;
    }

    public void draw(GL4 gl, StaticMVPShader shader, Matrix4f viewMatrix, Matrix4f projectionMatrix, Texture modelTexture, Vec3f lightDirection, Model3D cubeModel) {
        Matrix4f modelMatrix = new Matrix4f()
                .loadIdentity()
                .translate(position, new Matrix4f())
                .scale(y, new Matrix4f());

        shader.start(gl);
        shader.loadModelViewProjectionMatrices(gl, modelMatrix, viewMatrix, projectionMatrix);
        shader.loadModelTexture(gl, modelTexture);
        shader.loadLightDirection(gl, lightDirection);

        gl.glBindVertexArray(cubeModel.getVaoID());
        cubeModel.enableAllVertexAttributeArrays(gl);
        shader.enableAllTextures(gl);
        gl.glDrawElements(GL4.GL_TRIANGLES, cubeModel.getVertexCount(), GL.GL_UNSIGNED_INT, 0);
        cubeModel.disableAllVertexAttributeArrays(gl);
        gl.glBindVertexArray(0);

        shader.stop(gl);
    }
}
