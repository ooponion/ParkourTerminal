#version 120

uniform sampler2D DiffuseSampler;  // 主纹理
uniform sampler2D MaskSampler;     // 遮罩纹理
varying vec2 texCoord;             // 纹理坐标
varying vec2 oneTexel;             // 单个像素大小
uniform vec2 InSize;               // 输入纹理大小
uniform vec2 BlurDir;              // 模糊方向
uniform float Radius;              // 模糊强度

void main() {
    // 检查遮罩纹理的 alpha 值（0=模糊，1=不模糊）
    float mask = texture2D(MaskSampler, texCoord).a;
    if (mask > 0.5) {
        gl_FragColor = texture2D(DiffuseSampler, texCoord);
        return;
    }

    // 模糊逻辑：采用对称采样来减少循环迭代次数
    vec4 blurred = vec4(0.0);
    float totalWeight = 0.0;
    float sigma = Radius / 2.0;

    // 先采样中心像素
    float centerWeight = exp(-0.0 / (2.0 * sigma * sigma)); // exp(0)=1.0
    blurred += texture2D(DiffuseSampler, texCoord) * centerWeight;
    totalWeight += centerWeight;

    // 对称采样：正负方向上的偏移量是对称的，所以只循环正方向
    for (float r = 1.0; r <= Radius; r += 1.0) {
        float weight = exp(-(r * r) / (2.0 * sigma * sigma));
        vec4 samplePos = texture2D(DiffuseSampler, texCoord + oneTexel * r * BlurDir);
        vec4 sampleNeg = texture2D(DiffuseSampler, texCoord - oneTexel * r * BlurDir);
        blurred += (samplePos + sampleNeg) * weight;
        totalWeight += 2.0 * weight;
    }

    blurred /= totalWeight;
    gl_FragColor = blurred;
}