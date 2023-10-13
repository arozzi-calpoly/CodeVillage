package org.codevillage;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.math.Matrix4f;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

/**
 * An abstract class to load, compile, and store a generic
 * OpenGL vertex and fragment shader.
 */
public abstract class Shader {
    /**
     * The ID of the program used by OpenGL
     */
    private final int programID;
    /**
     * The ID of the vertex shader used by OpenGL
     */
    private final int vertexShaderID;

    /**
     * The ID of the fragment shader used by OpenGL
     */
    private final int fragmentShaderID;

    /**
     * A buffer used for setting uniform matrices in a
     * shader.
     */
    private static final FloatBuffer matrixBuffer = FloatBuffer.allocate(16);

    /**
     * Creates a shader from a vertex shader file and fragment shader file.
     * @param gl The OpenGL context
     * @param vertexShaderFilepath The path to the vertex shader
     * @param fragmentShaderFilepath The path to the fragment shader
     */
    public Shader(GL4 gl, Path vertexShaderFilepath, Path fragmentShaderFilepath)
    {
        this(gl, getFileContent(vertexShaderFilepath), getFileContent(fragmentShaderFilepath));
    }

    /**
     * Creates a shader from vertex and fragment shader source code.
     * @param gl The OpenGL context
     * @param vertexShaderStream The input stream of the vertex shader source code
     * @param fragmentShaderStream The input stream of the fragment shader source code
     */
    public Shader(GL4 gl, InputStream vertexShaderStream, InputStream fragmentShaderStream)
    {
        this(gl, getFileContent(vertexShaderStream), getFileContent(fragmentShaderStream));
    }

    /**
     * Creates a shader from vertex and fragment shader source code
     * @param gl The OpenGL context
     * @param vertexShaderContent The vertex shader source code
     * @param fragmentShaderContent The fragment shader source code
     */
    public Shader(GL4 gl, String vertexShaderContent, String fragmentShaderContent)
    {
        this.vertexShaderID = loadShader(gl, vertexShaderContent, ShaderType.VERTEX);
        this.fragmentShaderID = loadShader(gl, fragmentShaderContent, ShaderType.FRAGMENT);
        this.programID = gl.glCreateProgram();
        gl.glAttachShader(programID, fragmentShaderID);
        gl.glAttachShader(programID, vertexShaderID);
        bindAttributes(gl);
        gl.glLinkProgram(programID);
        gl.glValidateProgram(programID);
        getAllUniformLocations(gl);
    }

    /**
     * Gets the file content of a filepath
     * @param filepath The path of the file
     * @return The content of the file
     */
    private static String getFileContent(Path filepath)
    {
        try{
            return new String(Files.readAllBytes(filepath));
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("There was an error reading the shader file '" + filepath.toString() + "'" + e.getMessage());
        }
    }

    /**
     * Gets the entire content of an input stream
     * @param fileInputStream The input stream to read the content from
     * @return The entire content of the input stream
     */
    private static String getFileContent(InputStream fileInputStream)
    {
        return new BufferedReader(new InputStreamReader(fileInputStream))
                .lines().collect(Collectors.joining("\n"));
    }

    /**
     * Gets all the uniform locations associated with the shader
     * @param gl The OpenGL context
     */
    protected abstract void getAllUniformLocations(GL4 gl);

    /**
     * Gets the location of a uniform by name
     * @param gl The gl context
     * @param uniformName The name of the uniform
     * @return The location ID of the uniform
     */
    protected int getUniformLocation(GL4 gl, String uniformName)
    {
        return getUniformLocation(gl, uniformName, true);
    }

    /**
     * Gets the location of a uniform by name
     * @param gl The gl context
     * @param uniformName The name of the uniform
     * @return The location ID of the uniform
     */
    protected int getUniformLocation(GL4 gl, String uniformName, boolean errorOnFail)
    {
        int location = gl.glGetUniformLocation(programID, uniformName);
        if(location == -1 && errorOnFail)
        {
            throw new IllegalArgumentException("There is no uniform with name '" + uniformName + "'");
        }
        return location;
    }

    /**
     * Loads a matrix as a uniform
     * @param gl The gl context
     * @param location The location of the matrix uniform
     * @param matrix The matrix to load
     */
    protected synchronized void loadMatrix(GL4 gl, int location, Matrix4f matrix)
    {
        matrix.get(matrixBuffer);
        matrixBuffer.flip();
        gl.glUniformMatrix4fv(location, 1, false, matrixBuffer);
    }

    /**
     * Loads an int as a uniform
     * @param gl The gl context
     * @param location The location of the int uniform
     * @param value The value of the int to load
     */
    protected void loadInt(GL4 gl, int location, int value)
    {
        gl.glUniform1i(location, value);
    }

    /**
     * Loads a vec3 as a uniform
     * @param gl The gl context
     * @param location The location of the vec3 uniform
     * @param x The x coordinate of the vector
     * @param y The y coordinate of the vector
     * @param z The z coordinate of the vector
     */
    protected void loadVec3(GL4 gl, int location, float x, float y, float z)
    {
        gl.glUniform3f(location, x, y, z);
    }

    /**
     * Loads a float as a uniform
     * @param gl The gl context
     * @param location The location of the float uniform
     * @param value The value of the float to load
     */
    protected void loadFloat(GL4 gl, int location, float value)
    {
        gl.glUniform1f(location, value);
    }

    /**
     * Starts using the shader
     * @param gl The gl context
     */
    public void start(GL4 gl)
    {
        gl.glUseProgram(this.programID);
    }

    /**
     * Stops using the shader
     * @param gl The gl context
     */
    public void stop(GL4 gl) {
        gl.glUseProgram(0);
    }

    /**
     * Detaches and frees the shaders and program associated with this
     * object.
     * @param gl The gl context
     */
    public void cleanUp(GL4 gl)
    {
        stop(gl);
        gl.glDetachShader(programID, vertexShaderID);
        gl.glDetachShader(programID, fragmentShaderID);
        gl.glDeleteShader(vertexShaderID);
        gl.glDeleteShader(fragmentShaderID);
        gl.glDeleteProgram(programID);
    }

    /**
     * Binds all the attributes used in this shader
     * @param gl The OpenGL context
     */
    protected abstract void bindAttributes(GL4 gl);

    /**
     * Binds a specific attribute to the given attribute number and attribute name
     * @param gl The OpenGL attribute
     * @param attribute The attribute number
     * @param variableName The attribute name
     */
    protected void bindAttributes(GL4 gl, int attribute, String variableName)
    {
        gl.glBindAttribLocation(programID, attribute, variableName);
    }

    /**
     * Loads a shader in the given OpenGL context
     * @param gl The OpenGL context
     * @param fileContent The content of the shader file
     * @param shaderType The type of shader it is
     * @return The ID of the new shader
     */
    private static int loadShader(GL4 gl, String fileContent, ShaderType shaderType)
    {
        int shaderID = gl.glCreateShader(shaderType.getGlShaderType());
        gl.glShaderSource(shaderID, 1, new String[]{fileContent}, makeIntPointer(fileContent.length()));
        gl.glCompileShader(shaderID);
        IntBuffer errorPtr = makeIntPointer();
        gl.glGetShaderiv(shaderID, GL4.GL_COMPILE_STATUS, errorPtr);
        if(errorPtr.get(0) == GL4.GL_FALSE)
        {
            String compilationError = getShaderCompilationLog(gl, shaderID);
            throw new IllegalArgumentException("There was an error parsing the shader \n'" +  fileContent + "'\n" + compilationError);
        }

        return shaderID;
    }

    /**
     * Extracts the output of the OpenGL compilation log.
     * This is used for debugging when a shader fails to compile.
     * @param gl The gl context
     * @param shaderID The ID of the shader
     * @return The compilation log, as a String
     */
    private static String getShaderCompilationLog(GL4 gl, int shaderID)
    {
        int maxLength = 10_000;
        ByteBuffer outputBuffer = ByteBuffer.allocate(maxLength);
        IntBuffer actualOutputLength = makeIntPointer();
        gl.glGetShaderInfoLog(shaderID, maxLength, actualOutputLength, outputBuffer);
        // ByteBuffer conciseOutput = outputBuffer.slice();//(0, actualOutputLength.get(0));
        return StandardCharsets.UTF_8.decode(outputBuffer).toString();
    }

    /**
     * Creates an IntBuffer of size 1
     * @return An IntBuffer of size 1
     */
    public static IntBuffer makeIntPointer()
    {
        return IntBuffer.allocate(1);
    }

    /**
     * Creates an int buffer of size one and sets its value
     * @param value The value to set it to
     */
    public static IntBuffer makeIntPointer(int value)
    {
        IntBuffer buffer =  IntBuffer.allocate(1);
        buffer.put(value);
        buffer.rewind();
        return buffer;
    }

    /**
     * An enum representing the type of shader
     */
    private enum ShaderType {VERTEX(GL4.GL_VERTEX_SHADER), FRAGMENT(GL4.GL_FRAGMENT_SHADER);
        private final int glShaderType;
        ShaderType(int glShaderType) {
            this.glShaderType = glShaderType;
        }
        public int getGlShaderType()
        {
            return glShaderType;
        }
    }
}
