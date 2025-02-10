#version 120

// Variables
uniform sampler2D DiffuseSampler;
varying vec2 texCoord;
varying vec2 oneTexel;
uniform vec2 InSize;
uniform vec2 BlurDir;
uniform float Radius;

void main() {
    // Local variables
    vec4 blurred = vec4(0.0);
    float totalWeight = 0.0;
    float sigma = Radius / 2.0;

    // Blurring
    for (float r = -Radius; r <= Radius; r += 1.0) {
        float weight = exp(-(r*r) / (2.0 * sigma * sigma));
        vec4 sampleColor = texture2D(DiffuseSampler, texCoord + oneTexel * r * BlurDir);
        blurred += sampleColor * weight;
        totalWeight += weight;
    }

    // Post-processing
    blurred /= totalWeight;
    gl_FragColor = blurred;
}