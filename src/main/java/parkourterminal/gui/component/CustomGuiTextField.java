package parkourterminal.gui.component;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import parkourterminal.util.ShapeDrawer;

import java.lang.reflect.Field;

public class CustomGuiTextField extends GuiTextField {
    // 颜色优化
    private int backgroundColor = 0x60000000;   // 背景颜色（半透明深灰色）
    private int textColor = 0x80EEEEEE;         // 文本颜色（柔和白）
    private int disabledTextColor = 0x80999999; // 禁用时文本颜色（浅灰色）
    private int currentBorderColor = 0x60FFFFFF;

    private FontRenderer fontRendererObj;

    // 动画变量
    private int animatedCursorX = 0;

    public CustomGuiTextField(int id, FontRenderer fontRenderer, int x, int y, int width, int height) {
        super(id, fontRenderer, x, y, width, height);
        this.fontRendererObj = fontRenderer;
    }

    @Override
    public void drawTextBox() {
        // 禁用纹理和启用混合
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

        // 绘制圆角背景
        int borderWidth = 2;
        int radius = 10; // 圆角半径增大，优化视觉效果

        // 绘制边框
        ShapeDrawer.drawRoundedRect(this.xPosition - borderWidth, this.yPosition - borderWidth,
                this.width + 2 * borderWidth, this.height + 2 * borderWidth,
                currentBorderColor, radius);

        // 绘制背景
        ShapeDrawer.drawRoundedRect(this.xPosition, this.yPosition, this.width, this.height, backgroundColor, radius);

        // 恢复纹理和混合状态
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();

        // 计算文本颜色
        int currentTextColor = this.isEnabled() ? this.textColor : this.disabledTextColor;

        // 让文本居中
        int textX = this.xPosition + 6;
        int textY = this.yPosition + (this.height - fontRendererObj.FONT_HEIGHT) / 2;

        this.drawString(this.fontRendererObj, this.getText(), textX, textY, currentTextColor);

        // 绘制光标（带渐变效果）
        if (this.isFocused()) {
            // 计算目标光标位置：利用 cursorPosition (或 getSelectionEnd()) 计算文本前部分的宽度
            int targetCursorX = textX + this.fontRendererObj.getStringWidth(this.getText().substring(0, this.getSelectionEnd())) - 1;

            // 平滑插值：更新 animatedCursorX（使用 int 类型）
            int dx = (int)((targetCursorX - animatedCursorX) * 0.2);
            animatedCursorX += dx;

            if (dx == 0 && Math.abs(targetCursorX - animatedCursorX) > 0) {
                animatedCursorX = targetCursorX;
            }

            // 计算光标位置
            int cursorX = animatedCursorX;
            int cursorY = textY;
            int cursorWidth = 1; // 光标宽度
            int cursorHeight = fontRendererObj.FONT_HEIGHT - 1; // 光标高度

            // 计算光标闪烁的渐变效果
            long currentTime = System.currentTimeMillis();
            float blinkProgress = (float) Math.sin(currentTime / 200.0); // 200ms 一个周期
            int alpha = (int) (128 + 127 * blinkProgress); // 透明度在 128~255 之间变化
            int cursorColor = (alpha << 24) | 0xFFFFFF; // 白色光标，透明度渐变

            // 绘制渐变光标
            GlStateManager.disableTexture2D();
            GlStateManager.enableBlend();
            ShapeDrawer.drawRect(cursorX, cursorY - 1, cursorX + cursorWidth, cursorY + cursorHeight, cursorColor);
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
        }
    }

    private boolean isEnabled() {
        try {
            Field field = GuiTextField.class.getDeclaredField("isEnabled");
            field.setAccessible(true);
            return field.getBoolean(this);
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public void setCurrentBorderColor(int color) {
        this.currentBorderColor = color;
    }

    public void setTextColor(int color) {
        this.textColor = color;
    }
}
