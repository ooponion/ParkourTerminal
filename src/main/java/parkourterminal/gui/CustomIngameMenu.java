package parkourterminal.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class CustomIngameMenu extends GuiIngameMenu {
    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button); // 处理原版按钮点击
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks); // 绘制原版菜单

        // 重置颜色为白色，确保纹理正常显示
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        // 绑定自定义齿轮图标纹理（请确保资源路径正确）
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("parkourterminal", "textures/gui/settings.png"));

        int iconSize = 16; // 目标显示大小
        int iconX = this.width - iconSize - 10; // 距离屏幕右边 10 像素
        int iconY = 10; // 距离屏幕顶部 10 像素

        // 使用 drawScaledCustomSizeModalRect 将 64x64 的图片缩放到 16x16 显示
        drawScaledCustomSizeModalRect(iconX, iconY, 0, 0, 64, 64, iconSize, iconSize, 64, 64);
    }
}