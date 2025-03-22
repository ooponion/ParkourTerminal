package parkourterminal.gui.screens.intf;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import parkourterminal.shader.Shader;
import parkourterminal.shader.ShaderGroup;
import parkourterminal.shader.ShaderUniform;
import parkourterminal.util.AnimationUtils.impls.BeizerAnimation;
import parkourterminal.util.AnimationUtils.impls.ColorInterpolateAnimation;
import parkourterminal.util.AnimationUtils.impls.interpolatingData.InterpolatingColor;
import parkourterminal.util.AnimationUtils.impls.interpolatingData.Interpolatingfloat;
import parkourterminal.util.AnimationUtils.intf.AbstractAnimation;
import parkourterminal.util.AnimationUtils.intf.AnimationMode;
import parkourterminal.util.BlurRenderer;
import parkourterminal.util.ShaderHelper;

import java.io.IOException;
import java.util.List;

public class BlurGui extends GuiScreen {
    private ShaderGroup shaderGroup;
    private float targetBlurIntensity = 10.0f; // 固定模糊强度为 10

    // 关闭动画
    private boolean isClosing = false;
    private final AbstractAnimation<Interpolatingfloat> animation=new BeizerAnimation<Interpolatingfloat>(0.2f,new Interpolatingfloat(0f), AnimationMode.BLENDED,0,0,1,1);

    @Override
    public void initGui() {
        isClosing = false;
        if (OpenGlHelper.shadersSupported) {
            // 使用 ShaderHelper 加载模糊 Shader
            this.shaderGroup = ShaderHelper.loadBlurShader(targetBlurIntensity);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (this.shaderGroup != null) {
            if (isClosing) {
                // 计算淡出动画进度
                animation.RestartAnimation(new Interpolatingfloat(targetBlurIntensity));
                // 当动画完成，正式退出 GUI
                if (animation.getProgress() >= 1.0f) {
                    this.mc.displayGuiScreen(null);
                    return;
                }
            } else {
                // 计算淡入动画进度
                animation.RestartAnimation(new Interpolatingfloat(0f));
            }

            // 更新模糊强度
            List<Shader> shaders = shaderGroup.getListShaders();
            if (!shaders.isEmpty()) {
                ShaderUniform radiusUniform = shaders.get(0).getShaderManager().getShaderUniform("Radius");
                if (radiusUniform != null) {
                    radiusUniform.set(animation.Update().getValue());
                }
            }
            OnResizeBlurShader();
            // 应用模糊 Shader
            this.shaderGroup.loadShaderGroup(partialTicks);
            this.mc.getFramebuffer().bindFramebuffer(true);
        }
    }

    @Override
    public void onGuiClosed() {
        if (this.shaderGroup != null) {
            this.mc.entityRenderer.stopUseShader();
        }
    }

    @Override
    public void handleKeyboardInput() throws IOException {
        if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE && Keyboard.getEventKeyState()) {
            if (!isClosing) {
                isClosing = true;
            }
            return;
        }

        super.handleKeyboardInput();
    }

    private void OnResizeBlurShader() {
        if (shaderGroup != null) {
            int width = Minecraft.getMinecraft().displayWidth;
            int height = Minecraft.getMinecraft().displayHeight;
            shaderGroup.createBindFramebuffers(width, height); // 这里确保 Framebuffer 适应新窗口大小
        }
    }
}