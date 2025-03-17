package parkourterminal.gui.component;

import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;
import parkourterminal.util.AnimationUtils.AnimationHelper;
import parkourterminal.util.ShapeDrawer;

public class CustomButton {
    private int x, y;
    private int width, height;
    private int normalColor, hoverColor;
    private int cornerRadius;
    private String text;
    private float hoverProgress = 0.0f;

    public CustomButton(int x, int y, int width, int height, int normalColor, int hoverColor, int cornerRadius, String text) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.normalColor = normalColor;
        this.hoverColor = hoverColor;
        this.cornerRadius = cornerRadius;
        this.text = text;
    }

    public void drawButton(FontRenderer fontRenderer, int mouseX, int mouseY) {
        // 计算悬停动画进度
        boolean isHovered = isMouseOver(mouseX, mouseY);
        float targetProgress = isHovered ? 1.0f : 0.0f;
        hoverProgress += (targetProgress - hoverProgress) * 0.2f;

        // 计算当前颜色
        int currentColor = AnimationHelper.interpolateColor(normalColor, hoverColor, hoverProgress);

        // 绘制按钮背景
        GL11.glEnable(GL11.GL_BLEND);
        ShapeDrawer.drawRoundedRect(x, y, width, height, currentColor, cornerRadius);
        GL11.glDisable(GL11.GL_BLEND);

        // 绘制按钮文本
        int textWidth = fontRenderer.getStringWidth(text);
        int textX = x + (width - textWidth) / 2;
        int textY = y + (height - fontRenderer.FONT_HEIGHT) / 2;
        fontRenderer.drawStringWithShadow(text, textX, textY, 0xFFFFFFFF);
    }

    public boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }
}
