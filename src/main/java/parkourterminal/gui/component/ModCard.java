package parkourterminal.gui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import parkourterminal.util.AnimationHelper;
import parkourterminal.util.ShapeDrawer;

public class ModCard {
    private String title;
    protected int x, y, width, height;
    private int targetX, targetY;
    private int cornerRadius;

    private int backgroundColor = 0x40000000;
    private int borderColor = 0x80000000;
    private int highlightColor = 0x40FFFFFF;

    private float hoverProgress = 0.0f;

    // 使用自定义字体渲染器
    private static FontRenderer fontRendererObj = new ConsolaFontRenderer(Minecraft.getMinecraft());

    public ModCard(String title, int x, int y, int width, int height) {
        this.title = title;
        this.x = this.targetX = x;
        this.y = this.targetY = y;
        this.width = width;
        this.height = height;
        this.cornerRadius = 3;
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

        // 居中绘制标题文本
        int textColor = 0xFFFFFFFF;
        int textWidth = fontRendererObj.getStringWidth(title);
        int textX = x + (width - textWidth) / 2;
        int textY = y + (height - 8) / 2;  // 假设字体高度约为 8
        fontRendererObj.drawString(title, textX, textY, textColor);
    }

    public boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= (x + width)
                && mouseY >= y && mouseY <= (y + height);
    }

    // 点击卡片后返回对应的详细设置界面
    public ModDetailGui getModDetailGui() {
        return new ModDetailGui(title);
    }
}