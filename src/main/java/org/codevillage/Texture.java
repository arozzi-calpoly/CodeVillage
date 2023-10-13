package org.codevillage;

/**
 * A class representing an OpenGL texture
 */
public class Texture
{
    /**
     * Gets the texture ID
     */
    private final int textureID;

    /**
     * Creates a texture from a texture ID
     * @param textureID The texture ID
     */
    public Texture(int textureID)
    {
        this.textureID = textureID;
    }

    /**
     * Gets the texture ID
     * @return The texture ID
     */
    public int getTextureID()
    {
        return textureID;
    }
}
