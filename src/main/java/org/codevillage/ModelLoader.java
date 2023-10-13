package org.codevillage;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.*;

/**
 * Loads 3D models into OpenGL with attributes.
 */
public class ModelLoader
{
    /**
     * The list of all the IDs of the VAOs that have been created
     */
    private static final List<Integer> vaos = new ArrayList<>();

    /**
     * The list of all the IDs of the VBOs that have been created
     */
    private static final List<Integer> vbos = new ArrayList<>();

    /**
     * The list of the IDs of all the textures that have been created
     */
    private static final List<Integer> textureIDs = new ArrayList<>();

    /**
     * The number of vertex attributes that have been implemented.
     * Right now it is just position, normal, and texture coordinate
     */
    public static final int NUM_VERTEX_ATTRIBUTES = 3;

    /**
     * The position of the position attribute
     */
    public static final int POSITION_ATTRIBUTE = 0;

    /**
     * The position of the normal attribute
     */
    public static final int NORMAL_ATTRIBUTE = 1;

    /**
     * The position of the texture coordinate attribute
     */
    public static final int TEXTURE_COORDINATE_ATTRIBUTE = 2;

    /**
     * Creates a VAO and stores the position data of the vertices into attribute
     * 0, and the normal data into attribute 1 of the VAO.
     * The indices are stored in an index buffer and bound to the VAO.
     *
     * @param positions
     *            - The 3D positions of each vertex in the geometry (in this
     *            example a quad).
     * @param indices
     *            - The indices of the model that we want to store in the VAO.
     *            The indices indicate how the vertices should be connected
     *            together to form triangles.
     * @param normals
     *            - The normal vectors associated with each vertex
     * @param textureCoordinates
     *            - The texture coordinates for each vertex
     * @return The loaded model.
     */
    public static Model3D loadToVAO(GL4 gl, float[] positions, int[] indices, float[] normals, float[] textureCoordinates)
    {
        int vaoID = createVAO(gl);

        bindIndicesBuffer(gl, indices); // create the index buffer
        storeFloat3DataInAttributeList(gl, POSITION_ATTRIBUTE, positions); // create the position buffer
        storeFloat3DataInAttributeList(gl, NORMAL_ATTRIBUTE, normals); // create the normal buffer

        if(textureCoordinates == null || textureCoordinates.length == 0)
        {
            textureCoordinates = new float[positions.length / 3 * 2];
        }

        storeFloat2DataInAttributeList(gl, TEXTURE_COORDINATE_ATTRIBUTE, textureCoordinates);

        unbindVAO(gl);
        return new Model3D(vaoID, indices.length, NUM_VERTEX_ATTRIBUTES);
    }

    /**
     * Flips a buffered image vertically
     * @param image The image to flip
     * @return The vertically flipped image
     */
    public static BufferedImage flipImageVertically(BufferedImage image)
    {
        int x = 0;
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage flippedImage = new BufferedImage(width, height, image.getType());
        Graphics2D g = flippedImage.createGraphics();
        g.drawImage(image, x, height, width, -height, null);
        g.dispose();
        return flippedImage;
    }


    /**
     * Loads a texture and returns it, or null if the image failed to load
     * @param imageURL The image URL
     * @return The texture, or null if the texture failed to load
     */
    public static Texture loadTexture(GL4 gl, URL imageURL)
    {
        return loadTexture(gl, imageURL, gl.GL_LINEAR_MIPMAP_LINEAR, gl.GL_LINEAR);
    }

    public static Texture loadTexture(GL4 gl, URL imageURL, int textureMinFilter, int textureMagFilter)
    {
        try {
            BufferedImage image = ImageIO.read(imageURL);

            image = flipImageVertically(image); // opengl texture coordinates y-axis is flipped

            // copy the image to an RGBA byte buffer
            int[] pixels = new int[image.getWidth() * image.getHeight()];
            image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
            ByteBuffer imageBytesBuffer = ByteBuffer.allocateDirect(image.getWidth() * image.getHeight() * 4);

            for(int h = 0; h < image.getHeight(); h++)
            {
                for(int w = 0; w < image.getWidth(); w++)
                {
                    int pixel = pixels[h * image.getWidth() + w];
                    imageBytesBuffer.put((byte) ((pixel >> 16) & 0xFF));
                    imageBytesBuffer.put((byte) ((pixel >> 8) & 0xFF));
                    imageBytesBuffer.put((byte) (pixel & 0xFF));
                    imageBytesBuffer.put((byte) ((pixel >> 24) & 0xFF));
                }
            }
            imageBytesBuffer.flip();

            IntBuffer textureIDPointer = IntBuffer.allocate(1);
            gl.glGenTextures(1, textureIDPointer);
            int textureID = textureIDPointer.get(0);

            gl.glBindTexture(gl.GL_TEXTURE_2D, textureID);

            gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_WRAP_S, gl.GL_REPEAT);
            gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_WRAP_T, gl.GL_REPEAT);
            gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MIN_FILTER, textureMinFilter);
            gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MAG_FILTER, textureMagFilter);

            gl.glTexImage2D(
                    // The first argument specifies the texture target; setting this to GL_TEXTURE_2D means
                    // this operation will generate a texture on the currently bound texture object at the
                    // same target (so any textures bound to targets GL_TEXTURE_1D or GL_TEXTURE_3D
                    // will not be affected).
                    gl.GL_TEXTURE_2D,
                    // The second argument specifies the mipmap level for which we want to create a texture
                    // for if you want to set each mipmap level manually, but we'll leave it at the base level
                    // which is 0.
                    0,
                    // The third argument tells OpenGL in what kind of format we want to store the texture.
                    GL.GL_RGBA,
                    // The 4th and 5th argument sets the width and height of the resulting texture.
                    // We stored those earlier when loading the image, so we'll use the corresponding variables.
                    image.getWidth(), image.getHeight(),
                    // The next argument should always be 0 (some legacy stuff).
                    0,
                    // The 7th and 8th argument specify the format and datatype of the source image.
                    // We loaded the image with RGB values and stored them as chars (bytes)
                    // so we'll pass in the corresponding values.
                    GL.GL_RGBA,
                    gl.GL_UNSIGNED_BYTE,
                    // The last argument is the actual image data.
                    imageBytesBuffer
            );

            // Automatically generates all the required mipmaps for the currently bound texture.
            gl.glGenerateMipmap(gl.GL_TEXTURE_2D);

            // create anisotropic mipmap (if hardware supports it)
            if (gl.isExtensionAvailable("GL_EXT_texture_filter_anisotropic"))
            {
                float[] maxAnisotropyPtr = new float[1];
                gl.glGetFloatv(GL.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT, maxAnisotropyPtr, 0);
                gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAX_ANISOTROPY_EXT, maxAnisotropyPtr[0]);
            }

            textureIDs.add(textureID);

            return new Texture(textureID);
        }
        catch (IOException e)
        {
            return null;
        }

    }

    /**
     * This method takes in a grid of size NxMx2 and creates a NxM texture with 4 color channels.
     * The elements at grid[n][m][0] are put in the red channel of the texture, and the elements
     * at grid[n][m][1] are put in the green channel.
     * @param grid The grid of values
     * @return A texture with the red and green channels populated with the grid's values.
     * @throws IllegalArgumentException if the length of the third dimensions is not 2.
     */
    public static Texture createRGBATextureFromFloatGrid(GL4 gl, float[][][] grid)
    {
        int rows = grid.length; // height
        int cols = grid[0].length; // width
        int numChannels = grid[0][0].length; // color channels

        if(numChannels != 4)
            throw new IllegalArgumentException("The length of the third axis of the grid must be 4");

        FloatBuffer floatBuffer = FloatBuffer.allocate(grid.length * grid[0].length * grid[0][0].length);
        for(int r = 0; r < rows; r++)
        {
            for(int c = 0; c < cols; c++)
            {
                for(int channel = 0; channel < numChannels; channel++)
                {
                    floatBuffer.put(grid[r][c][channel]);
                }
            }
        }

        IntBuffer textureIDPointer = IntBuffer.allocate(1);
        gl.glGenTextures(1, textureIDPointer);
        int textureID = textureIDPointer.get(0);

        gl.glBindTexture(gl.GL_TEXTURE_2D, textureID);

        gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_WRAP_S, gl.GL_REPEAT);
        gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_WRAP_T, gl.GL_REPEAT);
        gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MIN_FILTER, gl.GL_LINEAR_MIPMAP_LINEAR);
        gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MAG_FILTER, gl.GL_LINEAR);
        floatBuffer.rewind();
        gl.glTexImage2D(
                // The first argument specifies the texture target; setting this to GL_TEXTURE_2D means
                // this operation will generate a texture on the currently bound texture object at the
                // same target (so any textures bound to targets GL_TEXTURE_1D or GL_TEXTURE_3D
                // will not be affected).
                gl.GL_TEXTURE_2D,
                // The second argument specifies the mipmap level for which we want to create a texture
                // for if you want to set each mipmap level manually, but we'll leave it at the base level
                // which is 0.
                0,
                // The third argument tells OpenGL in what kind of format we want to store the texture.
                GL.GL_RGBA32F,
                // The 4th and 5th argument sets the width and height of the resulting texture.
                // We stored those earlier when loading the image, so we'll use the corresponding variables.
                cols, rows,
                // The next argument should always be 0 (some legacy stuff).
                0,
                // The 7th and 8th argument specify the format and datatype of the source image.
                // We loaded the image with RGB values and stored them as chars (bytes)
                // so we'll pass in the corresponding values.
                GL.GL_RGBA,
                gl.GL_FLOAT,
                // The last argument is the actual image data.
                floatBuffer
        );

        // glTexImage2D(GL_TEXTURE_2D, 0, GL_R32F, texDim, texDim, 0, GL_RED, GL_FLOAT, texData);

        // Automatically generates all the required mipmaps for the currently bound texture.
        gl.glGenerateMipmap(gl.GL_TEXTURE_2D);

        // create anisotropic mipmap (if hardware supports it)
        if (gl.isExtensionAvailable("GL_EXT_texture_filter_anisotropic"))
        {
            float[] maxAnisotropyPtr = new float[1];
            gl.glGetFloatv(GL.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT, maxAnisotropyPtr, 0);
            gl.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAX_ANISOTROPY_EXT, maxAnisotropyPtr[0]);
        }

        textureIDs.add(textureID);
        return new Texture(textureID);
    }

    public static Texture createSolidColorTexture(GL4 gl, int width, int height, Color color)
    {
        float red = color.getRed() / 255.f;
        float green = color.getGreen() / 255.f;
        float blue = color.getBlue() / 255.f;
        float alpha = color.getAlpha() / 255.f;
        float[][][] data = new float[height][width][4];
        for(int r = 0; r < height; r++)
        {
            for(int c = 0; c < width; c++)
            {
                data[r][c][0] = red;
                data[r][c][1] = green;
                data[r][c][2] = blue;
                data[r][c][3] = alpha;
            }
        }
        return ModelLoader.createRGBATextureFromFloatGrid(gl, data);
    }

    /**
     * Deletes all the VAOs, VBOs, and textures. They are located in video memory.
     */
    public static void cleanUp(GL4 gl)
    {
        IntBuffer vaoIDs = toIntBuffer(vaos);
        gl.glDeleteVertexArrays(vaoIDs.capacity(), vaoIDs);
        IntBuffer vboIDs = toIntBuffer(vbos);
        gl.glDeleteBuffers(vboIDs.capacity(), vboIDs);
        IntBuffer textureIDsBuffer = toIntBuffer(textureIDs);
        gl.glDeleteTextures(textureIDsBuffer.capacity(), textureIDsBuffer);
    }

    /**
     * Creates a new VAO and returns its ID. A VAO holds geometry data that we
     * can render and is physically stored in memory on the GPU, so that it can
     * be accessed very quickly during rendering.
     * Like most objects in OpenGL, the new VAO is created using a "gen" method
     * which returns the ID of the new VAO. In order to use the VAO it needs to
     * be made the active VAO. Only one VAO can be active at a time. To make
     * this VAO the active VAO (so that we can store stuff in it) we have to
     * bind it.
     *
     * @return The ID of the newly created VAO.
     */
    private static int createVAO(GL4 gl)
    {
        IntBuffer vaoIDBuf = IntBuffer.allocate(1);
        gl.glGenVertexArrays(1, vaoIDBuf);
        int vaoID = vaoIDBuf.get(0);
        vaos.add(vaoID);
        gl.glBindVertexArray(vaoID);
        return vaoID;
    }

    /**
     * Stores the position data of the vertices into attribute 0 of the VAO. To
     * do this the positions must first be stored in a VBO. You can simply think
     * of a VBO as an array of data that is stored in memory on the GPU for easy
     * access during rendering.
     * Just like with the VAO, we create a new VBO using a "gen" method, and
     * make it the active VBO (so that we do stuff to it) by binding it.
     * We then store the positions data in the active VBO by using the
     * glBufferData method. We also indicate using GL_STATIC_DRAW that this data
     * won't need to be changed. If we wanted to edit the positions every frame
     * (perhaps to animate the quad) then we would use GL_DYNAMIC_DRAW instead.
     * We connect the VBO to the VAO using the glVertexAttribPointer()
     * method. This needs to know the attribute number of the VAO where we want
     * to put the data, the number of floats used for each vertex (3 floats in
     * this case, because each vertex has a 3D position, an x, y, and z value),
     * the type of data (in this case we used floats) and then some other more
     * complicated stuff for storing the data in more fancy ways. Don't worry
     * about the last 3 parameters for now, we don't need them here.
     * Now that we've finished using the VBO we can unbind it. This isn't
     * totally necessary, but I think it's good practice to unbind the VBO when
     * you're done using it.
     *
     * @param attributeNumber
     *            - The number of the attribute of the VAO where the data is to
     *            be stored.
     * @param data
     *            - The geometry data to be stored in the VAO, in this case the
     *            positions of the vertices.
     */
    private static void storeFloat3DataInAttributeList(GL4 gl, int attributeNumber, float[] data)
    {
        IntBuffer vboIDBuf = IntBuffer.allocate(1);
        gl.glGenBuffers(1, vboIDBuf);
        int vboID = vboIDBuf.get(0);
        vbos.add(vboID);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = toFloatBuffer(data);
        int FLOAT_SIZE = Float.SIZE / Byte.SIZE;
        gl.glBufferData(GL.GL_ARRAY_BUFFER, (long) FLOAT_SIZE * buffer.capacity(), buffer, GL.GL_STATIC_DRAW);
        gl.glVertexAttribPointer(attributeNumber, 3, GL.GL_FLOAT, false, 0, 0);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    }

    /**
     * Stores an unravelled array of floats into an attribute list, specified by the attribute number.
     * @param gl The OpenGL context
     * @param attributeNumber The attribute number of this new buffer of float pairs
     * @param data The actual float data. Adjacent floats constitute pairs.
     */
    private static void storeFloat2DataInAttributeList(GL4 gl, int attributeNumber, float[] data)
    {
        IntBuffer vboIDBuf = IntBuffer.allocate(1);
        gl.glGenBuffers(1, vboIDBuf);
        int vboID = vboIDBuf.get(0);
        vbos.add(vboID);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = toFloatBuffer(data);
        int FLOAT_SIZE = Float.SIZE / Byte.SIZE;
        gl.glBufferData(GL.GL_ARRAY_BUFFER, (long) FLOAT_SIZE * buffer.capacity(), buffer, GL.GL_STATIC_DRAW);
        gl.glVertexAttribPointer(attributeNumber, 2, GL.GL_FLOAT, false, 0, 0);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    }

    /**
     * Unbinds the VAO after we're finished using it. If we want to edit or use
     * the VAO we would have to bind it again first.
     */
    private static void unbindVAO(GL4 gl)
    {
        gl.glBindVertexArray(0);
    }

    /**
     * Creates an index buffer, binds the index buffer to the currently active
     * VAO, and then fills it with our indices.
     * The index buffer is different from other data that we might store in the
     * attributes of the VAO. When we stored the positions we were storing data
     * about each vertex. The positions were "attributes" of each vertex. Data
     * like that is stored in an attribute list of the VAO.
     * The index buffer however does not contain data about each vertex. Instead,
     * it tells OpenGL how the vertices should be connected. Each VAO can only
     * have one index buffer associated with it. This is why we don't store the
     * index buffer in a certain attribute of the VAO; each VAO has one special
     * "slot" for an index buffer and simply binding the index buffer binds it
     * to the currently active VAO. When the VAO is rendered it will use the
     * index buffer that is bound to it.
     * This is also why we don't unbind the index buffer, as that would unbind
     * it from the VAO.
     * Note that we tell OpenGL that this is an index buffer by using
     * "GL_ELEMENT_ARRAY_BUFFER" instead of "GL_ARRAY_BUFFER". This is how
     * OpenGL knows to bind it as the index buffer for the current VAO.
     *
     * @param indices The indices
     */
    private static void bindIndicesBuffer(GL4 gl, int[] indices)
    {
        IntBuffer vboIDBuf = IntBuffer.allocate(1);
        gl.glGenBuffers(1, vboIDBuf);
        int vboId = vboIDBuf.get(0);
        vbos.add(vboId);
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, vboId);
        IntBuffer buffer = toIntBuffer(indices);
        int INT_SIZE = Integer.SIZE / Byte.SIZE;
        gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, (long) INT_SIZE * buffer.capacity(), buffer, GL.GL_STATIC_DRAW);
    }

    /**
     * Converts the indices from an int array to an IntBuffer so that they can
     * be stored in a VBO. Very similar to the storeDataInFloatBuffer() method
     * below.
     *
     * @param data
     *            - The indices in an int[].
     * @return The indices in a buffer.
     */
    private static IntBuffer toIntBuffer(int[] data)
    {
        IntBuffer buffer = IntBuffer.allocate(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    /**
     * Converts a list of Integer objects to a IntBuffer
     * @param data The list of Integers
     * @return The IntBuffer containing the same data
     */
    private static IntBuffer toIntBuffer(List<Integer> data)
    {
        int[] dataArray = data.stream().mapToInt(i->i).toArray();
        return toIntBuffer(dataArray);
    }

    /**
     * Before we can store data in a VBO it needs to be in a certain format: in
     * a buffer. In this case we will use a float buffer because the data we
     * want to store is float data. If we were storing int data we would use an
     * IntBuffer.
     * First and empty buffer of the correct size is created. You can think of a
     * buffer as basically an array with a pointer. After putting the necessary
     * data into the buffer the pointer will have increased so that it points at
     * the first empty element of the array. This is so that we could add more
     * data to the buffer if we wanted, and it wouldn't overwrite the data we've
     * already put in. However, we're done with storing data, and we want to make
     * the buffer ready for reading. To do this we need to make the pointer
     * point to the start of the data, so that OpenGL knows where in the buffer
     * to start reading. The "flip()" method does just that, putting the pointer
     * back to the start of the buffer.
     *
     * @param data
     *            - The float data that is going to be stored in the buffer.
     * @return The FloatBuffer containing the data. This float buffer is ready
     *         to be loaded into a VBO.
     */
    private static FloatBuffer toFloatBuffer(float[] data) {
        FloatBuffer buffer = FloatBuffer.allocate(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

}