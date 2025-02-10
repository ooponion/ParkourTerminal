package parkourterminal.gui.component;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import parkourterminal.shader.Shader;
import parkourterminal.shader.ShaderGroup;
import parkourterminal.shader.ShaderUniform;
import parkourterminal.util.ShaderHelper;

import java.io.IOException;
import java.util.List;

public class BlurGui extends GuiScreen {
    private ShaderGroup shaderGroup;
    private float targetBlurIntensity = 10.0f; // 固定模糊强度为 10

    // 关闭动画
    private boolean isClosing = false;
    private long closeStartTime = -1;

    // 动画变量
    private long animationStartTime = -1;
    private final float animationDuration = 100.0f;

    @Override
    public void initGui() {
        this.animationStartTime = System.currentTimeMillis();

        if (OpenGlHelper.shadersSupported) {
            // 使用 ShaderHelper 加载模糊 Shader
            this.shaderGroup = ShaderHelper.loadBlurShader(targetBlurIntensity);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (this.shaderGroup != null) {
            long elapsed;
            float progress;
            float easedProgress;
            float currentBlur;

            if (isClosing) {
                // 计算淡出动画进度
                elapsed = System.currentTimeMillis() - closeStartTime;
                progress = Math.min(elapsed / animationDuration, 1.0f);
                easedProgress = progress * progress;
                currentBlur = (1 - easedProgress) * targetBlurIntensity;

                // 当动画完成，正式退出 GUI
                if (progress >= 1.0f) {
                    this.mc.displayGuiScreen(null);
                    return;
                }
            } else {
                // 计算淡入动画进度
                elapsed = System.currentTimeMillis() - animationStartTime;
                progress = Math.min(elapsed / animationDuration, 1.0f);
                easedProgress = progress * progress;
                currentBlur = easedProgress * targetBlurIntensity;
            }

            // 更新模糊强度
            List<Shader> shaders = shaderGroup.getListShaders();
            if (!shaders.isEmpty()) {
                ShaderUniform radiusUniform = shaders.get(0).getShaderManager().getShaderUniform("Radius");
                if (radiusUniform != null) {
                    radiusUniform.set(currentBlur);
                }
            }

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
                closeStartTime = System.currentTimeMillis();
            }
            return;
        }

        super.handleKeyboardInput();
    }
}