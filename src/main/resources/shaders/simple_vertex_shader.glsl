#version 400 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 normal;
layout (location = 2) in vec2 textureCoord;

uniform mat4 modelMatrix;
uniform mat4 transInvModelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
uniform mat4 mvpMatrix;

out vec3 worldPosition;
out vec2 texCoord;

out vec3 worldNormal;
out vec3 modelNormal;

void main(void)
{
    gl_Position = mvpMatrix * vec4(position, 1);

    worldPosition = (modelMatrix * vec4(position, 1)).xyz;

    worldNormal = normalize(mat3(transInvModelMatrix) * normal);
    modelNormal = normal;

    texCoord = textureCoord;
}