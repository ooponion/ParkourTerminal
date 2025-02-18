package parkourterminal.gui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import parkourterminal.util.AnimationHelper;
import parkourterminal.util.ShapeDrawer;

public abstract class ModCard {
    private String title;
    protected int x, y, width, height;
    private int targetX, targetY;
    private int cornerRadius;
    private ResourceLocation icon;

    private int backgroundColor = 0x40000000;
    private int borderColor = 0x80000000;
    private int highlightColor = 0x40FFFFFF;

    private float hoverProgress = 0.0f;

    // 布局常量：图标固定为8x8，图标与文字间隔，以及按钮内边距
    private static final int ICON_SIZE = 16;
    private static final int ICON_TEXT_GAP = 4;

    // 使用自定义字体渲染器
    private static FontRenderer fontRendererObj = new ConsolaFontRenderer(Minecraft.getMinecraft());

    public ModCard(String title, ResourceLocation icon, int x, int y, int width, int height) {
        this.title = title;
        this.x = this.targetX = x;
        this.y = this.targetY = y;
        this.width = width;
        this.height = height;
        this.cornerRadius = 3;
        this.icon = icon;
    }

    // 提供一个更新位置和尺寸的方法
    public void setPosition(int x, int y, int width, int height) {
        this.targetX = x;
        this.targetY = y;
        this.width = width;
        this.height = height;
    }

    public void draw(int mouseX, int mouseY) {
        if (targetX != x) {
            int xDistance = (int)((targetX - x) * 0.15f);

            if (xDistance == 0)
                x = targetX;
            else
                x += xDistance;
        }

        if (targetY != y) {
            int yDistance = (int)((targetY - y) * 0.15f);

            if (yDistance == 0)
                y = targetY;
            else
                y += yDistance;
        }

        // 计算悬停动画进度
        boolean hovering = isMouseOver(mouseX, mouseY);
        if (hovering) {
            hoverProgress = Math.min(1.0f, hoverProgress + 0.1f); // 平滑进入
        } else {
            hoverProgress = Math.max(0.0f, hoverProgress - 0.1f); // 平滑退出
        }

        // 计算渐变颜色
        int currentHighlightColor = AnimationHelper.interpolateColor(backgroundColor, highlightColor, hoverProgress);

        // 绘制圆角背景
        ShapeDrawer.drawRoundedRect(x, y, width, height, currentHighlightColor, cornerRadius);
        // 绘制边框
        ShapeDrawer.drawRoundedRectBorder(x, y, width, height, borderColor, cornerRadius);

        // 绘制图标（如果设置了 icon，则在左侧绘制固定 8×8 的图标，并垂直居中）
        if (icon != null) {
            Minecraft mc = Minecraft.getMinecraft();
            mc.getTextureManager().bindTexture(icon);

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            int iconX = x + ICON_TEXT_GAP; // 预留间隔
            int iconY = y + (height - ICON_SIZE) / 2;
            mc.currentScreen.drawScaledCustomSizeModalRect(iconX, iconY, 0, 0, 64, 64, ICON_SIZE, ICON_SIZE, 64, 64);
        }

        // 绘制标题文本
        int textColor = 0xFFFFFFFF;
        // 如果存在图标，则文本区域左边界向右偏移图标宽度和间隔
        int textOffset = (icon != null ? (ICON_SIZE + ICON_TEXT_GAP * 2) : 0);
        // 使文本在剩余区域内居中：剩余宽度为 (width - textOffset)
        int textX = x + textOffset;
        int textY = y + (height - fontRendererObj.FONT_HEIGHT) * 13 / 20;
        fontRendererObj.drawString(title, textX, textY, textColor);
    }

    public boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= (x + width)
                && mouseY >= y && mouseY <= (y + height);
    }

    // 点击卡片后返回对应的详细设置界面
    public abstract ModDetailGui getModDetailGui();
}