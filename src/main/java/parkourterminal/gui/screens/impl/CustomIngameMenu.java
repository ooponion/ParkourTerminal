package parkourterminal.gui.screens.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import parkourterminal.gui.component.ConsolaFontRenderer;
import parkourterminal.gui.screens.impl.InGameMenuGui.IngameMenuGui;
import parkourterminal.util.BlurRenderer;
import parkourterminal.util.ScissorHelper;
import parkourterminal.util.ShapeDrawer;

import java.io.IOException;

public class CustomIngameMenu extends GuiIngameMenu {
    // 旋转动画
    private float rotationAngle = 0.0f;
    private long lastUpdateTime = System.currentTimeMillis();

    @Override
    public void initGui() {
        BlurRenderer.initSharedBlurShader(10.0f);
        fontRendererObj = new ConsolaFontRenderer(Minecraft.getMinecraft());

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // 绘制原版菜单界面
        super.drawScreen(mouseX, mouseY, partialTicks);

        // 定义图标和外圈矩形的位置与尺寸
        int iconSize = 16;           // 图标大小为 16x16 像素
        int iconX = 10;              // 图标绘制在屏幕左上角（x 坐标 10 像素）
        int iconY = 10;              // 图标绘制在屏幕顶部（y 坐标 10 像素）

        int blurColor = 0x40FFFFFF;  // 25% 透明的白色，防止背景变黑
        int rectRadius = 5;          // 圆角半径设为 5 像素
        float blurIntensity = 10.0f; // 与 initGui 中的模糊强度保持一致

        // 先绘制一个带毛玻璃效果的外圈矩形
        BlurRenderer.drawBlurredRoundedRect(iconX, iconY, iconSize, iconSize, blurColor, rectRadius, blurIntensity, partialTicks);

        // 计算动画
        boolean isHovered = mouseX >= iconX && mouseX <= iconX + iconSize &&
                mouseY >= iconY && mouseY <= iconY + iconSize;

        long currentTime = System.currentTimeMillis();
        float deltaTime = (currentTime - lastUpdateTime) / 500.0f; // 计算时间差（秒）
        lastUpdateTime = currentTime;

        if (isHovered) {
            if (rotationAngle < 360.0f)
                rotationAngle += 360.0f * deltaTime; // 让图标顺时针旋转
            else
                rotationAngle = 360.0f;
        } else {
            // 当鼠标移开时，逐渐恢复到 0°
            rotationAngle *= 0.9f;
        }

        // 计算矩形动画
        float expandProgress = rotationAngle / 360.0f; // 归一化到 0-1
        String fullText = "Settings";
        int maxTextWidth = fontRendererObj.getStringWidth(fullText) + 10; // 最大矩形宽度
        int textRectWidth = (int) (expandProgress * maxTextWidth);
        int textRectHeight = iconSize;
        int texticonX = iconX + iconSize + 3; // 齿轮右侧
        int textRectY = iconY;

        // 绘制展开的圆角矩形
        if(textRectWidth>6) {
            BlurRenderer.drawBlurredRoundedRect(texticonX, textRectY, textRectWidth, textRectHeight, blurColor, 3, blurIntensity, partialTicks);
        }

        // 绑定自定义图标纹理
        Minecraft mc = Minecraft.getMinecraft();
        mc.getTextureManager().bindTexture(new ResourceLocation("parkourterminal", "textures/gui/settings.png"));
        // 确保混合模式正确
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableDepth();  // 防止深度测试影响

        // 确保颜色状态正确
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        // 旋转图标
        GlStateManager.pushMatrix();
        GlStateManager.translate(iconX + iconSize / 2.0, iconY + iconSize / 2.0, 0); // 平移到中心
        GlStateManager.rotate(rotationAngle, 0, 0, 1); // 旋转
        GlStateManager.translate(-iconSize / 2.0, -iconSize / 2.0, 0); // 还原偏移

        // 绘制图标
        ShapeDrawer.drawScaledCustomSizeModalRect(0, 0, 0, 0, 64, 64, iconSize, iconSize, 64, 64);

        GlStateManager.popMatrix(); // 恢复 OpenGL 变换

        // 逐渐显示 "Settings"
        ScissorHelper.EnableScissor(texticonX,textRectY,textRectWidth,textRectHeight);

        fontRendererObj.drawString(fullText, texticonX + 5, textRectY + 4, 0xFFFFFFFF);
        ScissorHelper.DisableScissor();


        // 恢复 OpenGL 状态
        GlStateManager.enableDepth();
    }


    @Override
    public void onGuiClosed() {
        BlurRenderer.cleanupBlurResources();

        super.onGuiClosed();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        // 计算图标的区域
        int iconSize = 16;
        int iconX = 10;
        int iconY = 10;

        boolean isClicked = mouseX >= iconX && mouseX <= iconX + iconSize &&
                mouseY >= iconY && mouseY <= iconY + iconSize;

        if (isClicked && mouseButton == 0) { // 左键点击
            Minecraft mc = Minecraft.getMinecraft();
            mc.displayGuiScreen(null); // 关闭当前 GUI，回到游戏界面
            mc.displayGuiScreen(new IngameMenuGui()); // 打开自定义 IngameMenuGui
        }
    }
}