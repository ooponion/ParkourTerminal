package parkourterminal.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import parkourterminal.shader.Shader;
import parkourterminal.shader.ShaderGroup;
import parkourterminal.shader.ShaderManager;
import parkourterminal.shader.ShaderUniform;

import java.io.IOException;
import java.util.List;

public class ShaderHelper {
    public static ShaderGroup loadBlurShader(float blurIntensity) {
        try {
            // 加载模糊 Shader
            ShaderGroup blurShader = new ShaderGroup(
                    Minecraft.getMinecraft().getTextureManager(),
                    Minecraft.getMinecraft().getResourceManager(),
                    Minecraft.getMinecraft().getFramebuffer(),
                    new ResourceLocation("parkourterminal", "shaders/post/blur.json")
            );
            blurShader.createBindFramebuffers(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);

            // 设置模糊强度
            List<Shader> shaders = blurShader.getListShaders();

            if (!shaders.isEmpty()) {
                ShaderUniform radiusUniform = shaders.get(0).getShaderManager().getShaderUniform("Radius");

                if (radiusUniform != null)
                    radiusUniform.set(blurIntensity);
            }

            return blurShader;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ShaderGroup loadRegionBlurShader(float blurIntensity, ResourceLocation maskTexture) {
        try {
            // 加载遮罩模糊 Shader
            ShaderGroup regionBlurShader = new ShaderGroup(
                    Minecraft.getMinecraft().getTextureManager(),
                    Minecraft.getMinecraft().getResourceManager(),
                    Minecraft.getMinecraft().getFramebuffer(),
                    new ResourceLocation("parkourterminal", "shaders/post/region_blur.json")
            );
            regionBlurShader.createBindFramebuffers(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);

            // 获取 Shader 列表
            List<Shader> shaders = regionBlurShader.getListShaders();
            if (!shaders.isEmpty()) {
                Shader shader = shaders.get(0);
                ShaderManager manager = shader.getShaderManager();

                // 设置模糊强度
                ShaderUniform radiusUniform = manager.getShaderUniform("Radius");
                if (radiusUniform != null) {
                    radiusUniform.set(blurIntensity);
                }

                // 绑定遮罩纹理到 MaskSampler
                ShaderUniform maskUniform = manager.getShaderUniform("MaskSampler");
                if (maskUniform != null) {
                    Minecraft.getMinecraft().getTextureManager().bindTexture(maskTexture);
                }
            }

            return regionBlurShader;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}