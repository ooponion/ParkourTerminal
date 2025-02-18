package parkourterminal.gui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;
import parkourterminal.gui.layout.UIComponent;
import parkourterminal.util.AnimationHelper;
import parkourterminal.util.ShapeDrawer;

public class ToggleSwitch extends UIComponent {
    private boolean isOn = false;
    private int onColor = 0xFF00FF00;   // 绿色
    private int offColor = 0xFFFF0000;  // 红色
    private int thumbX;
    private final int thumbWidth = 6;
    // 用于绘制文字的字体渲染器，此处假设 ConsolaFontRenderer 提供 getStringWidth 方法
    private final FontRenderer fontRenderer = new ConsolaFontRenderer(Minecraft.getMinecraft());
    private String labelText; // 右侧显示的标签文字
    private static final int TEXT_PADDING = 5; // 文字与开关的间距

    // 按钮部分的宽度（不包括标签部分）
    private int buttonWidth;
    // 文本区域假定固定宽度 40 像素
    private static final int FIXED_TEXT_WIDTH = 60;

    /**
     * 构造函数中传入的 buttonWidth 表示仅按钮的宽度，
     * 整个组件的宽度 = buttonWidth + TEXT_PADDING + FIXED_TEXT_WIDTH
     */
    public ToggleSwitch(int buttonWidth, int height, String labelText) {
        this.buttonWidth = buttonWidth;
        this.labelText = labelText;
        this.height = height;
        // 组件总宽度按按钮宽度 + 间距 + 假定的文本区域宽度计算
        this.width = buttonWidth + TEXT_PADDING + FIXED_TEXT_WIDTH;
        // 根据按钮宽度初始化 thumbX
        this.thumbX = isOn ? 0 : buttonWidth - thumbWidth;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        // 更新平滑动画
        updateAnimation();

        // 绘制按钮区域的边框（仅针对按钮区域，不包含标签部分）
        ShapeDrawer.drawRoundedRectBorder(x, y, buttonWidth, height, 0x40000000, 3);

        // 根据 thumbX 计算进度（0 表示开启状态，1 表示关闭状态），基于按钮区域宽度
        float progress = (float) thumbX / (float) (buttonWidth - thumbWidth);
        // 使用插值函数计算当前滑块颜色
        int interpolatedColor = AnimationHelper.interpolateColor(onColor, offColor, progress);

        // 绘制滑块
        ShapeDrawer.drawRoundedRect(x + thumbX, y, thumbWidth, height, interpolatedColor, 3);

        // 绘制右侧的标签文字（直接左对齐显示，假定宽度 100）
        int labelTextX = x + buttonWidth + TEXT_PADDING;
        int labelTextY = y + (height - fontRenderer.FONT_HEIGHT) / 2;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        fontRenderer.drawString(labelText, labelTextX, labelTextY, 0xFFFFFF);

        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    public boolean isMouseOver(int mouseX, int mouseY) {
        // 整个组件范围包括按钮区域和标签区域
        return mouseX >= x && mouseX <= x + width &&
                mouseY >= y && mouseY <= y + height;
    }

    public void toggle() {
        isOn = !isOn;
    }

    // 平滑更新 thumbX，以实现滑块动画（使用按钮部分的宽度计算目标位置）
    public void updateAnimation() {
        int targetX = isOn ? 0 : buttonWidth - thumbWidth;
        int distance = (int)((targetX - thumbX) * 0.4f);
        thumbX += distance;
        if (distance == 0 && targetX != thumbX) {
            thumbX = targetX;
        }
    }
}
