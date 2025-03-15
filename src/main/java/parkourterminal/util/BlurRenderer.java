package parkourterminal.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import parkourterminal.shader.Shader;
import parkourterminal.shader.ShaderGroup;

import java.io.IOException;

public class BlurRenderer {
    // 全局共享的 ShaderGroup 和当前使用的模糊强度
    private static ShaderGroup sharedBlurShader = null;
    private static float sharedBlurIntensity = -1.0f;
    // 伪装的 ResourceLocation，这里仅用于传入 loadRegionBlurShader（实际纹理绑定由 Framebuffer 纹理ID负责）
    private static final ResourceLocation MASK_TEXTURE = new ResourceLocation("minecraft", "textures/gui/mask");

    // 根据当前的 blurIntensity 初始化或更新全局共享的 ShaderGroup
    public static void initSharedBlurShader(float blurIntensity) {
        // 当第一次加载或模糊强度发生变化时重新创建 ShaderGroup
        if (sharedBlurShader == null || sharedBlurIntensity != blurIntensity) {
            // 如果已经存在，则先删除释放资源（请确保 deleteShaderGroup() 内部释放了所有 GPU 资源）
            if (sharedBlurShader != null) {
                sharedBlurShader.deleteShaderGroup();
            }
            sharedBlurShader = ShaderHelper.loadRegionBlurShader(blurIntensity, MASK_TEXTURE);
            sharedBlurIntensity = blurIntensity;
        }
    }
    private static void OnResizeSharedBlurShader() {
        if (sharedBlurShader != null) {
            int width = Minecraft.getMinecraft().displayWidth;
            int height = Minecraft.getMinecraft().displayHeight;
            sharedBlurShader.createBindFramebuffers(width, height); // 这里确保 Framebuffer 适应新窗口大小
        }
    }

    // 获取共享的 ShaderGroup
    public static ShaderGroup getSharedBlurShader() {
        return sharedBlurShader;
    }

    // 绘制一个带模糊背景的圆角矩形
    public static void drawBlurredRoundedRect(int x, int y, int width, int height, int color, int radius, float blurIntensity, float partialTicks) {
        Minecraft mc = Minecraft.getMinecraft();
        Framebuffer framebuffer = mc.getFramebuffer();
        // 初始化或更新共享的 ShaderGroup（如果模糊强度未改变则复用）
        initSharedBlurShader(blurIntensity);
        OnResizeSharedBlurShader();
        // 绑定 Framebuffer，并只清除深度缓冲（不清除颜色数据，保留背景）
        framebuffer.bindFramebuffer(true);
        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);

        // 绘制遮罩：在需要模糊的区域内绘制一个遮罩（例如半透明的颜色），这里用 0x40000000 表示遮罩区域
        GlStateManager.color(1, 1, 1, 1);
        ShapeDrawer.drawRoundedRect(x, y, width, height, 0x40000000, radius);

        // 绑定遮罩纹理。这里使用 framebuffer.framebufferTexture 得到当前帧缓冲的纹理ID，
        // 并通过 OpenGL 绑定该纹理，使 Shader 在 MaskSampler 中采样到正确的数据
        int maskTextureId = framebuffer.framebufferTexture;
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.bindTexture(maskTextureId);

        // 使用共享的 ShaderGroup 应用模糊效果
        ShaderGroup blurShader = getSharedBlurShader();
        if (blurShader != null) {
            blurShader.loadShaderGroup(partialTicks);

        }

        // 恢复正常渲染，解除 Framebuffer 绑定
        mc.getFramebuffer().bindFramebuffer(true);

        // 绘制前景的圆角矩形，使其覆盖在模糊背景上
        ShapeDrawer.drawRoundedRect(x, y, width, height, color, radius);
    }

    // 回收 GPU 资源
    public static void cleanupBlurResources() {
        if (sharedBlurShader != null) {
            sharedBlurShader.deleteShaderGroup();  // 确保释放 ShaderGroup 内部所有的 GPU 资源
            sharedBlurShader = null;
            sharedBlurIntensity = -1.0f;
        }
    }
}
