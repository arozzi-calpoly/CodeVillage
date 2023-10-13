package org.codevillage;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.math.Vec3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class RenderingGeometryLib {

    public static Model3D getSingleTriangleModel(GL4 gl)
    {
        return ModelLoader.loadToVAO(gl,
                getSingleTriangleVerticesPositions(),
                getIcosahedronFacesIndices(),
                getSingleTriangleVerticesNormals(),
                getSingleTriangleTextureCoordinates());
    }

    public static float[] getSingleTriangleVerticesPositions()
    {
        return new float[] {
          -1, 0, 0,
          1, 0, 0,
          0, 1, 0
        };
    }

    public static float[] getSingleTriangleVerticesNormals()
    {
        return new float[] {
                0, 0, 1,
                0, 0, 1,
                0, 0, 1
        };
    }

    public static float[] getSingleTriangleFaceIndices()
    {
        return new float[] {
                0, 0, 1,
                0, 0, 1,
                0, 0, 1
        };
    }

    public static float[] getSingleTriangleTextureCoordinates()
    {
        return new float[] {
                0, 1, 2
        };
    }

    /**
     * Returns the vertices' positions of a unit cube centered at the origin
     * @return The positions of the unit cube
     */
    public static float[] getUnitCubeVerticesPositions()
    {
        return new float[]{
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, 0.5f,
                -0.5f, 0.5f, -0.5f,
                -0.5f, 0.5f, 0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, 0.5f
        };
    }

    /**
     * Returns a list of integers that represent
     * the indices of the vertices that make up the
     * triangular faces of a cube. (Each square face
     * is split into two triangles, and each triangle
     * has 3 vertices so there is a total of 36 indices)
     * They are grouped in contiguous sets of 3.
     */
    public static int[] getUnitCubeTriangleFacesIndices()
    {
        return new int[] {
                0, 1, 2,
                1, 3, 2,
                2, 3, 6,
                3, 7, 6,
                6, 7, 4,
                7, 5, 4,
                4, 5, 0,
                5, 1, 0,
                3, 1, 5,
                3, 5, 7,
                0, 2, 6,
                0, 6, 4
        };
    }

    /**
     * Gets the normal vectors to the vertices of the unit cube
     * @return The components of the normal vectors of the unit cube,
     * grouped into contiguous sets of 3
     */
    public static float[] getUnitCubeTriangleFacesNormals()
    {
        return new float[]{
                -1, 0, 0,
                -1, 0, 0,
                0, 1, 0,
                0, 1, 0,
                1, 0, 0,
                1, 0, 0,
                0, -1, 0,
                0, -1, 0,
                0, 0, 1,
                0, 0, 1,
                0, 0, -1,
                0, 0, -1
        };
    }

    /**
     * Creates a 3D model of the cube from its positions, normal vectors, and face indices.
     * It doesn't have any texture coordinates.
     * @param gl The OpenGL context
     * @return The cube 3D model
     */
    public static Model3D generateCubeModel(GL4 gl)
    {
        return ModelLoader.loadToVAO(gl,
                getUnitCubeVerticesPositions(),
                getUnitCubeTriangleFacesIndices(),
                getUnitCubeTriangleFacesNormals(),
                new float[0]);
    }

    /**
     * Returns the vertices' positions of an icosahedron.
     * @return Returns the vertices' positions of an icosahedron.
     */
    public static float[] getIcosahedronVertices()
    {
        return new float[] {
                -0.7236068f, -1.17082039f,  0.f,
                -1.17082039f,  0.f,  0.7236068f,
                -0.7236068f,  1.17082039f,  0.f,
                0.7236068f, 1.17082039f, 0.f,
                1.17082039f, 0.f, 0.7236068f,
                0.7236068f, -1.17082039f,  0.f,
                0.f, -0.7236068f, -1.17082039f,
                1.17082039f,  0.f, -0.7236068f,
                0.f,  0.7236068f, -1.17082039f,
                0.f, -0.7236068f,  1.17082039f,
                0.f, 0.7236068f, 1.17082039f,
                -1.17082039f,  0.f, -0.7236068f};
    }


    /**
     * Returns the indices of the triangular faces of an icosahedron.
     * @return Returns the indices of the triangular faces of an icosahedron.
     */
    public static int[] getIcosahedronFacesIndices()
    {
        return new int[] {
                0, 11, 6,
                0, 9, 1,
                8, 11, 2,
                1, 10, 2,
                6, 7, 5,
                4, 9, 5,
                7, 8, 3,
                3, 10, 4,
                0, 6, 5,
                6, 11, 8,
                1, 11, 0,
                5, 9, 0,
                6, 8, 7,
                2, 11, 1,
                3, 8, 2,
                9, 10, 1,
                5, 7, 4,
                2, 10, 3,
                4, 10, 9,
                4, 7, 3
        };
    }

    public static Model3D generateXZGrid(GL4 gl, double minX, double maxX, double minZ, double maxZ, int xResolution, int zResolution)
    {
        int numPoints = zResolution * xResolution;
        int numTriangleFaces = (zResolution - 1) * (xResolution - 1) * 2;

        float[] positions = new float[numPoints * 3];
        float[] normals = new float[numPoints * 3];
        float[] textureCoordinates = new float[numPoints * 2];
        int[] faceIndices = new int[numTriangleFaces * 3];

        int positionIdx = 0;
        int normalIdx = 0;
        int textureIdx = 0;
        int faceIndicesIdx = 0;

        // Create all the positions, normals, and texture coordinates
        for(int x = 0; x < xResolution; x++)
        {
            float xCoordinate = (float) linearInterpolation(x, 0, xResolution - 1, minX, maxX);
            for(int z = 0; z < zResolution; z++)
            {
                float zCoordinate = (float) linearInterpolation(z, 0, zResolution - 1, minZ, maxZ);

                positions[positionIdx++] = xCoordinate;
                positions[positionIdx++] = 0;
                positions[positionIdx++] = zCoordinate;

                normals[normalIdx++] = 0;
                normals[normalIdx++] = 1;
                normals[normalIdx++] = 0;

                textureCoordinates[textureIdx++] = x / (float) (xResolution - 1);
                textureCoordinates[textureIdx++] = z / (float) (zResolution - 1);
            }
        }

        // Create all the face indices
        for(int x = 0; x < xResolution - 1; x++)
        {
            for(int z = 0; z < zResolution - 1; z++)
            {
                // These variables are named relative to look at them from the top down
                int topLeftIndex = x * zResolution + z;
                int bottomLeftIndex = topLeftIndex + 1;
                int topRightIndex = topLeftIndex + zResolution;
                int bottomRightIndex = topRightIndex + 1;

                faceIndices[faceIndicesIdx++] = topLeftIndex;
                faceIndices[faceIndicesIdx++] = bottomLeftIndex;
                faceIndices[faceIndicesIdx++] = topRightIndex;

                faceIndices[faceIndicesIdx++] = topRightIndex;
                faceIndices[faceIndicesIdx++] = bottomLeftIndex;
                faceIndices[faceIndicesIdx++] = bottomRightIndex;
            }
        }


        return ModelLoader.loadToVAO(gl, positions, faceIndices, normals, textureCoordinates);
    }

    private static double linearInterpolation(double value, double oldMin, double oldMax, double newMin, double newMax)
    {
        return (value - oldMin) / (oldMax - oldMin) * (newMax - newMin) + newMin;
    }

    /**
     * Creates a sphere approximation by subdividing an
     * icosahedron.
     * @param gl The OpenGL context
     * @param subdivisions The number of times to subdivide the icosahedron.
     * @return A 3D model of an icosahedron.
     */
    public static Model3D generateSphereBySubdividingIcosahedron(GL4 gl, int subdivisions)
    {
        if(subdivisions < 0)
            throw new IllegalArgumentException("The number of subdivisions cannot be negative, but was given " + subdivisions);

        ArrayList<Vec3f> positions = asVectors(getIcosahedronVertices());
        int[] indices = getIcosahedronFacesIndices();

        /*
        This method starts with a normal icosahedron and repeatedly splits
        its triangles at the midpoints:

                        v1
                        /\
                       /  \
                      /    \
                     /      \
                    /        \
                   /          \
                  /            \
                 /              \
                /                \
               /                  \
              /                    \
             /                      \
            /                        \
        v2 +--------------------------+ v3

                        v1
                        /\
                       /  \
                      /    \
                     /      \
                    /        \
                   /          \
              m12 /----------- \ m31
                 /\           / \
                /  \         /   \
               /    \       /     \
              /      \     /       \
             /        \   /         \
            /          \ /           \
        v2 +------------+-------------+ v3
                       m23

        It then projects all the vertices onto the surface of a unit sphere.
        */

        for(int d = 0; d < subdivisions; d++)
        {
            // Every triangle gets split into 4 more triangles
            // which means that the indices buffer grows by a factor of 4.
            // The number of vertices only doubles though, because
            // the old ones are reused.
            int[] nextIndices = Arrays.copyOf(indices, indices.length * 4);
            ArrayList<Vec3f> nextPositions = new ArrayList<>(positions);

            HashMap<IntPair, Integer> midpointMap = new HashMap<>();
            for(int i = 0; i < indices.length; i += 3)
            {
                int v1Index = indices[i];
                int v2Index = indices[i + 1];
                int v3Index = indices[i + 2];

                int min12Index = Math.min(v1Index, v2Index);
                int min23Index = Math.min(v2Index, v3Index);
                int min31Index = Math.min(v3Index, v1Index);

                int max12Index = Math.max(v1Index, v2Index);
                int max23Index = Math.max(v2Index, v3Index);
                int max31Index = Math.max(v3Index, v1Index);

                IntPair mid12Lookup = new IntPair(min12Index, max12Index);
                IntPair mid23Lookup = new IntPair(min23Index, max23Index);
                IntPair mid31Lookup = new IntPair(min31Index, max31Index);

                Vec3f v1 = positions.get(v1Index);
                Vec3f v2 = positions.get(v2Index);
                Vec3f v3 = positions.get(v3Index);

                if(!midpointMap.containsKey(mid12Lookup))
                {
                    Vec3f midpoint12 = new Vec3f(v1).add(v2).scale(.5f);
                    nextPositions.add(midpoint12);
                    midpointMap.put(mid12Lookup, nextPositions.size() - 1);
                }

                if(!midpointMap.containsKey(mid23Lookup))
                {
                    Vec3f midpoint23 = new Vec3f(v2).add(v3).scale(.5f);
                    nextPositions.add(midpoint23);
                    midpointMap.put(mid23Lookup, nextPositions.size() - 1);
                }

                if(!midpointMap.containsKey(mid31Lookup))
                {
                    Vec3f midpoint31 = new Vec3f(v3).add(v1).scale(.5f);
                    nextPositions.add(midpoint31);
                    midpointMap.put(mid31Lookup, nextPositions.size() - 1);
                }
            }

            int numIndicesAddedSoFar = 0;
            for(int i = 0; i < indices.length; i += 3)
            {
                int v1Index = indices[i];
                int v2Index = indices[i + 1];
                int v3Index = indices[i + 2];
                int min12Index = Math.min(v1Index, v2Index);
                int min23Index = Math.min(v2Index, v3Index);
                int min31Index = Math.min(v3Index, v1Index);

                int max12Index = Math.max(v1Index, v2Index);
                int max23Index = Math.max(v2Index, v3Index);
                int max31Index = Math.max(v3Index, v1Index);

                int mid12Index = midpointMap.get(new IntPair(min12Index, max12Index));
                int mid23Index = midpointMap.get(new IntPair(min23Index, max23Index));
                int mid31Index = midpointMap.get(new IntPair(min31Index, max31Index));

                // add the new top triangle
                nextIndices[numIndicesAddedSoFar++] = v1Index;
                nextIndices[numIndicesAddedSoFar++] = mid12Index;
                nextIndices[numIndicesAddedSoFar++] = mid31Index;

                // add the new middle triangle
                nextIndices[numIndicesAddedSoFar++] = mid12Index;
                nextIndices[numIndicesAddedSoFar++] = mid23Index;
                nextIndices[numIndicesAddedSoFar++] = mid31Index;

                // add the new bottom left triangle
                nextIndices[numIndicesAddedSoFar++] = mid12Index;
                nextIndices[numIndicesAddedSoFar++] = v2Index;
                nextIndices[numIndicesAddedSoFar++] = mid23Index;

                // add the new bottom right triangle
                nextIndices[numIndicesAddedSoFar++] = mid23Index;
                nextIndices[numIndicesAddedSoFar++] = v3Index;
                nextIndices[numIndicesAddedSoFar++] = mid31Index;
            }
            positions = nextPositions;
            indices = nextIndices;
        }

        // we can now go through and normalize them
        for(int i = 0; i < positions.size(); i++)
            positions.set(i, positions.get(i).normalize());

        float[] vertices = asFloats(positions);
        // because they all lie on a unit sphere centered at the origin,
        // the vertices are also the normal vectors!
        return ModelLoader.loadToVAO(gl, vertices, indices, vertices, new float[0]);
    }


    /**
     * Converts an array of 3D coordinates (stored contiguously) into a list of
     * Vec4 (treated as 3D vectors).
     * @param coordinates The unravelled array of position coordinates
     * @return The list of vectors
     */
    private static ArrayList<Vec3f> asVectors(float[] coordinates)
    {
        if(coordinates.length % 3 != 0)
        {
            throw new IllegalArgumentException("The number of coordinates is not divisible by 3!");
        }
        ArrayList<Vec3f> positions = new ArrayList<>(coordinates.length / 3);
        for(int i = 0; i < coordinates.length; i += 3)
        {
            positions.add(new Vec3f(coordinates[i], coordinates[i + 1], coordinates[i+2]));
        }
        return positions;
    }

    /**
     * Converts a list of Vec4 (treated as 3D vectors) back into an array of contiguous coordinate points
     * @param points A list of vectors
     * @return The unravelled array of 3D components
     */
    private static float[] asFloats(ArrayList<Vec3f> points)
    {
        float[] coordinates = new float[points.size() * 3];
        for(int i = 0; i < coordinates.length; i += 3)
        {
            int j = i / 3;
            Vec3f point = points.get(j);
            coordinates[i] = point.x();
            coordinates[i + 1] = point.y();
            coordinates[i + 2] = point.z();
        }
        return coordinates;
    }
}
