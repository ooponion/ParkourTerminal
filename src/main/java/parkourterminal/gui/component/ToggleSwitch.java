package parkourterminal.gui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;
import parkourterminal.gui.layout.UIComponent;
import parkourterminal.util.AnimationUtils.impls.BeizerAnimation;
import parkourterminal.util.AnimationUtils.impls.ColorInterpolateAnimation;
import parkourterminal.util.AnimationUtils.impls.interpolatingData.FloatPoint;
import parkourterminal.util.AnimationUtils.impls.interpolatingData.InterpolatingColor;
import parkourterminal.util.AnimationUtils.intf.AbstractAnimation;
import parkourterminal.util.AnimationUtils.intf.AnimationMode;
import parkourterminal.util.ShapeDrawer;

public class ToggleSwitch extends UIComponent {
    private boolean isOn = false;
    private int onColor = 0xFF00FF00;   // 绿色
    private int offColor = 0xFFFF0000;  // 红色
    private int thumbWidth = 6;
    // 用于绘制文字的字体渲染器，此处假设 ConsolaFontRenderer 提供 getStringWidth 方法
    private final FontRenderer fontRenderer = new ConsolaFontRenderer(Minecraft.getMinecraft());
    private String labelText; // 右侧显示的标签文字
    private static final int TEXT_PADDING = 5; // 文字与开关的间距

    // 按钮部分的宽度（不包括标签部分）
    // 文本区域假定固定宽度 40 像素



    /**
     * 构造函数中传入的 buttonWidth 表示仅按钮的宽度，
     * 整个组件的宽度 = buttonWidth + TEXT_PADDING + FIXED_TEXT_WIDTH(discard hehe)
     */
    public ToggleSwitch(int width, int height, String labelText) {
        this.labelText = labelText;
        this.setHeight(height);
        // 组件总宽度按按钮宽度 + 间距 + 假定的文本区域宽度计算
        this.setWidth(width);
        // 根据按钮宽度初始化 thumbX
        this.getAnimation().changeWithOutAnimation(new FloatPoint(this.getX(),this.getY()));
        this.getAnimationColor().changeWithOutAnimation(new InterpolatingColor(offColor));
    }
    @Override
    public void setPosition(int x, int y){
        super.setPosition(x,y);
        this.getAnimation().changeWithOutAnimation(new FloatPoint(this.getX(),this.getY()));
    }
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        // 更新平滑动画
        FloatPoint point=getAnimation().Update();

        // 绘制按钮区域的边框（仅针对按钮区域，不包含标签部分）
        ShapeDrawer.drawRoundedRectBorder(getX(), getY(), getWidth(), getHeight(), 0x40000000, (float) thumbWidth /2);

        // 根据 thumbX 计算进度（0 表示开启状态，1 表示关闭状态），基于按钮区域宽度
        //float progress = (float) thumbX / (float) (buttonWidth - thumbWidth);
        // 使用插值函数计算当前滑块颜色
        //int interpolatedColor = AnimationHelper.interpolateColor(onColor, offColor, progress);
        if(isOn){
            getAnimation().RestartAnimation(new FloatPoint(getX()+getWidth() - thumbWidth,getY()));
            getAnimationColor().RestartAnimation(new InterpolatingColor(onColor));
        }else{
            getAnimation().RestartAnimation(new FloatPoint(getX(),getY()));
            getAnimationColor().RestartAnimation(new InterpolatingColor(offColor));
        }

        // 绘制滑块
        ShapeDrawer.drawRoundedRect(point.getX(), getY(), thumbWidth, getHeight(), getAnimationColor().Update().getColor(), (float) thumbWidth /2);

        // 绘制右侧的标签文字（直接左对齐显示，假定宽度 100）
        int labelTextX = getX() + getWidth() + TEXT_PADDING;
        int labelTextY = getY() + (getHeight() - fontRenderer.FONT_HEIGHT) / 2;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        fontRenderer.drawString(labelText, labelTextX, labelTextY, 0xFFFFFF);

        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    public boolean isMouseOver(int mouseX, int mouseY) {
        // 整个组件范围包括按钮区域和标签区域
        return mouseX >= getX() && mouseX <= getX() + getWidth() &&
                mouseY >= getY() && mouseY <= getY() + getHeight();
    }

    public void toggle() {
        isOn = !isOn;
    }
    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton){
        if (isMouseOver(mouseX, mouseY) && mouseButton == 0) {
            toggle();
            return true;
        }
        return false;
    }
    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        thumbWidth=Math.min(getWidth(),getHeight());
    }
    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        thumbWidth=Math.min(getWidth(),getHeight());
    }
    @Override
    public void setSize(int width,int height) {
        super.setSize(width,height);
        thumbWidth=Math.min(getWidth(),getHeight());
    }
    public boolean isOn() {
        return isOn;
    }
    public void setOn(boolean on){
        isOn=on;
    }

}
