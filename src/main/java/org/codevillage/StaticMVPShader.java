package org.codevillage;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.math.Matrix4f;
import com.jogamp.opengl.math.Vec3f;

import java.awt.*;
import java.io.InputStream;
import java.nio.file.Path;

public class StaticMVPShader extends Shader
{
    private int modelMatrixLocation;
    private int transInvModelMatrixLocation;
    private int viewMatrixLocation;
    private int projectionMatrixLocation;
    private int mvpMatrixLocation;
    private int eyePositionLocation;
    private int lightDirectionLocation;
    private int modelTextureID;
    private int modelTextureLocation;
    private int modelColorLocation;

    public StaticMVPShader(GL4 gl, Path vertexShaderFilepath, Path fragmentShaderFilepath)
    {
        super(gl, vertexShaderFilepath, fragmentShaderFilepath);
    }

    public StaticMVPShader(GL4 gl, InputStream vertexShaderStream, InputStream fragmentShaderStream)
    {
        super(gl, vertexShaderStream, fragmentShaderStream);
    }

    public void loadModelViewProjectionMatrices(GL4 gl, Matrix4f modelMatrix, Matrix4f viewMatrix, Matrix4f projectionMatrix)
    {
        Matrix4f transInvModelMatrix = new Matrix4f(modelMatrix);
        transInvModelMatrix.invert();
        transInvModelMatrix.transpose();
        Matrix4f modelViewProjectionMatrix = new Matrix4f(projectionMatrix).mul(viewMatrix).mul(modelMatrix);
        super.loadMatrix(gl, this.modelMatrixLocation, modelMatrix);
        super.loadMatrix(gl, this.viewMatrixLocation, viewMatrix);
        super.loadMatrix(gl, this.projectionMatrixLocation, projectionMatrix);
        super.loadMatrix(gl, this.transInvModelMatrixLocation, transInvModelMatrix);
        super.loadMatrix(gl, this.mvpMatrixLocation, modelViewProjectionMatrix);
    }

    public void loadEyePosition(GL4 gl, Vec3f eyePosition)
    {
        super.loadVec3(gl, this.eyePositionLocation, eyePosition.x(), eyePosition.y(), eyePosition.z());
    }


    public void loadLightDirection(GL4 gl, Vec3f lightDirection)
    {
        Vec3f lightDirectionUnit = new Vec3f(lightDirection).normalize();
        super.loadVec3(gl, this.lightDirectionLocation,
                lightDirectionUnit.x(), lightDirectionUnit.y(),
                lightDirectionUnit.z());
    }

    public void loadModelTexture(GL4 gl, Texture texture)
    {
        this.modelTextureID = texture.getTextureID();
        // we're only using one texture, we just load the constant 0
        super.loadInt(gl, this.modelTextureLocation, 0);
    }

    public void loadModelColor(GL4 gl, Color color)
    {
        super.loadVec3(gl, modelColorLocation, color.getRed() / 255.0f, color.getGreen() / 255.0f,
                color.getBlue() / 255.0f);
    }

    @Override
    protected void getAllUniformLocations(GL4 gl)
    {
        this.modelMatrixLocation = super.getUniformLocation(gl, "modelMatrix", false);
        this.viewMatrixLocation = super.getUniformLocation(gl, "viewMatrix", false);
        this.projectionMatrixLocation = super.getUniformLocation(gl, "projectionMatrix", false);
        this.transInvModelMatrixLocation = super.getUniformLocation(gl, "transInvModelMatrix", false);
        this.mvpMatrixLocation = super.getUniformLocation(gl, "mvpMatrix", false);
        this.eyePositionLocation = super.getUniformLocation(gl, "eyePosition", false);
        this.lightDirectionLocation = super.getUniformLocation(gl, "lightDirection", false);
        this.modelTextureLocation = super.getUniformLocation(gl, "textureSampler", false);
        this.modelColorLocation = super.getUniformLocation(gl, "modelColor", false);
    }

    @Override
    protected void bindAttributes(GL4 gl)
    {
        super.bindAttributes(gl, ModelLoader.POSITION_ATTRIBUTE, "position");
        super.bindAttributes(gl, ModelLoader.NORMAL_ATTRIBUTE, "normal");
        super.bindAttributes(gl, ModelLoader.TEXTURE_COORDINATE_ATTRIBUTE, "textureCoord");
    }

    public void enableAllTextures(GL4 gl)
    {
        gl.glActiveTexture(GL4.GL_TEXTURE0);
        gl.glBindTexture(GL4.GL_TEXTURE_2D, this.modelTextureID);
    }
}
