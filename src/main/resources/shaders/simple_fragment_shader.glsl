#version 400 core

uniform sampler2D textureSampler;
uniform vec3 eyePosition;
uniform vec3 lightDirection;
uniform vec3 modelColor;

in vec3 worldPosition;
in vec2 texCoord;

flat in vec3 worldNormal;
flat in vec3 modelNormal;

out vec4 out_Color;

void main(void)
{
    // out_Color = vec4(texture(textureSampler, texCoord).xyz, 1);
    // vec3 modelColor = texture(textureSampler, texCoord).xyz;
    vec3 lightColor = vec3(1, 1, 1);
    float specularStrength = 0.6;
    float ambientStrength = 0.15;

    // start by calculating the specular light
    vec3 viewDir = normalize(eyePosition - worldPosition);
    vec3 reflectDir = reflect(lightDirection, worldNormal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32);
    vec3 specular = specularStrength * spec * lightColor;

    // then calculate the diffuse light
    float cosAngle = dot(-lightDirection, worldNormal);
    vec3 diffuse = max(0, cosAngle) * lightColor;

    // then calculate the ambient light
    vec3 ambient = ambientStrength * lightColor;

    vec3 totalLight = (ambient + diffuse) * modelColor + specular;
    out_Color = vec4(totalLight, 1);
}