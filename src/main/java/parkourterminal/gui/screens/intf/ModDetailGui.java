package parkourterminal.gui.screens.intf;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import parkourterminal.gui.layout.Container;
import parkourterminal.util.ShapeDrawer;

import java.io.IOException;

public class ModDetailGui {
    private String modName;

    // 定义详细区域的布局参数
    protected int detailX, detailY, detailWidth, detailHeight;

    // 退出按钮参数（位于详细区域左上角，也就是 detailX, detailY 处）
    protected int exitButtonWidth = 10;
    protected int exitButtonHeight = 10;

    // Container
    protected Container detailContainer;

    // 按钮边框颜色和圆角
    protected int exitButtonColor = 0x00FFFFFF;
    protected int exitButtonTargetColor = 0x80FFFFFF;
    protected int exitButtonCornerRadius = 5;
    private float hoverProgress = 0.0f;
    private static final float ANIMATION_SPEED = 0.1f;

    public ModDetailGui(String modName) {
        this.modName = modName;
        // 初始化 Container
        detailContainer = new Container();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks, int width, int height) {
        int panelMargin = 10;
        int panelX, panelY;
        panelX = panelY = panelMargin;
        int panelWidth = width - panelMargin * 2;
        int panelHeight = height - panelMargin * 2;
        int detailMargin = 5;

        detailX = panelX + (int) (panelWidth * 0.20) + detailMargin;
        detailY = panelY + (int) (panelHeight * 0.10) + detailMargin;
        detailWidth = (int) (panelWidth * 0.80) - 2 * detailMargin;
        detailHeight = (int) (panelHeight * 0.90) - 2 * detailMargin;

        // 更新 detailContainer 的位置和尺寸，使其与详细区域保持一致
        detailContainer.setX( detailX);
        detailContainer.setY( detailY + 20);
        detailContainer.setWidth( detailWidth);
        detailContainer.setHeight( detailHeight - 20);

        // 更新悬停动画进度
        boolean isHovered = isMouseOverExitButton(mouseX, mouseY);
        hoverProgress += (isHovered ? ANIMATION_SPEED : -ANIMATION_SPEED);
        hoverProgress = Math.max(0, Math.min(1, hoverProgress)); // 限制在 0~1 之间

        // 使用插值后的颜色绘制按钮背景
//        int currentColor = AnimationHelper.interpolateColor(
//                exitButtonColor,
//                exitButtonTargetColor,
//                hoverProgress
//        );
        int currentColor = exitButtonColor;

        ShapeDrawer.drawRoundedRect(
                detailX, detailY,
                exitButtonWidth, exitButtonHeight,
                currentColor, exitButtonCornerRadius
        );

        ShapeDrawer.drawRoundedRectBorder(
                detailX, detailY,
                exitButtonWidth, exitButtonHeight,
                exitButtonTargetColor, exitButtonCornerRadius
        );

        ShapeDrawer.drawLine(
                panelX + (int) (panelWidth * 0.20) + 1, detailY + 15,
                panelX + panelWidth, detailY + 15,
                0xFFFFFFFF,1f
        );

        // 计算图标在按钮内居中位置
        ResourceLocation iconQuit2 = new ResourceLocation("parkourterminal", "textures/gui/quit2.png");
        int iconDrawWidth = 8;
        int iconDrawHeight = 8;
        int iconX = detailX + (exitButtonWidth - iconDrawWidth) / 2;
        int iconY = detailY + (exitButtonHeight - iconDrawHeight) / 2;

        // 启用混合模式，确保透明效果正确
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        // 绑定图标纹理
        Minecraft.getMinecraft().getTextureManager().bindTexture(iconQuit2);
        // 重置颜色，避免纹理被其他颜色影响
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        // 绘制图标，将64x64的原图缩放绘制为8x8
        ShapeDrawer.drawScaledCustomSizeModalRect(
                iconX, iconY,
                0, 0,                  // u, v起始坐标
                64, 64,
                8, 8,
                64, 64
        );

        // 恢复 OpenGL 状态
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0 && isMouseOverExitButton(mouseX, mouseY)) {
            return true;
        }
        return false;
    }

    // 检查鼠标是否在退出按钮上
    private boolean isMouseOverExitButton(int mouseX, int mouseY) {
        return mouseX >= detailX && mouseX <= detailX + exitButtonWidth &&
                mouseY >= detailY && mouseY <= detailY + exitButtonHeight;
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {

    }
}