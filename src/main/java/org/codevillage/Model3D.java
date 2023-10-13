package org.codevillage;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;

/**
 * Represents a 3D model to be used by OpenGL
 */
public class Model3D
{
    /**
     * The ID of the vertex array object associated with this
     * 3D model
     */
    private final int vaoID;
    /**
     * The number of vertices in the model
     */
    private final int vertexCount;
    /**
     * The number of attributes associates with each vertex
     * (such as position, normal vectors, etc.)
     */
    private final int numAttributes;

    /**
     * Creates a new 3D model
     * @param vaoID The ID of the vertex attribute array
     * @param vertexCount The number of vertices in the model
     * @param numAttributes The number of attributes in the model
     */
    public Model3D(int vaoID, int vertexCount, int numAttributes)
    {
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
        this.numAttributes = numAttributes;
    }

    /**
     * Enables all the attributes associated with each vertex
     * @param gl The GL context
     */
    public void enableAllVertexAttributeArrays(GL4 gl)
    {
        for(int i = 0; i < this.numAttributes; i++)
            gl.glEnableVertexAttribArray(i);
    }

    /**
     * Disables all the attributes associated with each vertex
     * @param gl The OpenGL context
     */
    public void disableAllVertexAttributeArrays(GL4 gl)
    {
        for(int i = 0; i < this.numAttributes; i++)
            gl.glDisableVertexAttribArray(i);
    }

    /**
     * @return The ID of the VAO which contains the data about all the geometry
     *         of this model.
     */
    public int getVaoID() {
        return vaoID;
    }

    /**
     * @return The number of vertices in the model.
     */
    public int getVertexCount() {
        return vertexCount;
    }

    public void draw(GL4 gl)
    {
        // bind to this model's vao
        gl.glBindVertexArray(this.getVaoID());
        // enable all the vertex attributes
        this.enableAllVertexAttributeArrays(gl);
        // finally, draw all the triangles
        gl.glDrawElements(GL4.GL_TRIANGLES, this.getVertexCount(), GL.GL_UNSIGNED_INT, 0);
        // clean-up
        this.disableAllVertexAttributeArrays(gl);
        gl.glBindVertexArray(0);
    }
}