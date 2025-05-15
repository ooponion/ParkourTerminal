package parkourterminal.gui.screens.impl.InGameMenuGui.Components.cardIntf;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import parkourterminal.gui.component.fontRenderer.ConsolaFontRenderer;
import parkourterminal.gui.layout.UIComponent;
import parkourterminal.util.AnimationUtils.impls.ColorInterpolateAnimation;
import parkourterminal.util.AnimationUtils.impls.interpolatingData.InterpolatingColor;
import parkourterminal.util.AnimationUtils.intf.AbstractAnimation;
import parkourterminal.util.AnimationUtils.intf.AnimationMode;
import parkourterminal.gui.screens.intf.ModDetailGui;
import parkourterminal.util.AnimationUtils.impls.interpolatingData.FloatPoint;
import parkourterminal.util.AnimationUtils.impls.BeizerAnimation;
import parkourterminal.util.ShapeDrawer;

import static java.lang.Math.abs;

public abstract class ModCard extends UIComponent {
    private String title;
    private int cornerRadius;
    private ResourceLocation icon;

    private int backgroundColor = 0x40000000;
    private int borderColor = 0x80000000;
    private int highlightColor = 0x40FFFFFF;

    private float hoverProgress = 0.0f;

    // 布局常量：图标固定为8x8，图标与文字间隔，以及按钮内边距
    private static final int ICON_SIZE = 16;
    private static final int ICON_TEXT_GAP = 4;
    private final float animation_time=4;

    private final AbstractAnimation<InterpolatingColor> animationColor;
    private final AbstractAnimation<FloatPoint> animation;


    // 使用自定义字体渲染器
    private static FontRenderer fontRendererObj = new ConsolaFontRenderer(Minecraft.getMinecraft());

    public ModCard(String title, ResourceLocation icon, int x, int y, int width, int height) {
        animation=new BeizerAnimation<FloatPoint>(animation_time,new FloatPoint(x,y), AnimationMode.BLENDED);
        animationColor=new ColorInterpolateAnimation(0.4f,new InterpolatingColor(backgroundColor),AnimationMode.BLENDED);

        this.title = title;
        this.setX(x);
        this.setY(y);
        this.setWidth(width);
        this.setHeight(height);
        this.cornerRadius = 3;
        this.icon = icon;
        }

    // 提供一个更新位置和尺寸的方法
    @Override
    public void setPosition(int x, int y){
        super.setPosition(x,y);
        animation.RestartAnimation(new FloatPoint(x,y));
    }
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        FloatPoint midpoint=animation.Update();
        this.setX((int) midpoint.getX());
        this.setY((int) midpoint.getY());
        // 计算悬停动画进度
        boolean hovering = isMouseOver(mouseX, mouseY);
        int currentHighlightColor;
        if (hovering) {
            animationColor.RestartAnimation(new InterpolatingColor(highlightColor));
        } else {
            animationColor.RestartAnimation(new InterpolatingColor(backgroundColor));
        }
        // 计算渐变颜色
        currentHighlightColor = animationColor.Update().getColor();

        // 绘制圆角背景
        ShapeDrawer.drawRoundedRect(getX(),getY(),getWidth(),getHeight(), currentHighlightColor, cornerRadius);
        // 绘制边框
        ShapeDrawer.drawRoundedRectBorder(getX(),getY(),getWidth(),getHeight(), borderColor, cornerRadius);

        // 绘制图标（如果设置了 icon，则在左侧绘制固定 8×8 的图标，并垂直居中）
        if (icon != null) {
            Minecraft mc = Minecraft.getMinecraft();
            mc.getTextureManager().bindTexture(icon);

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            float iconX = getX() + ICON_TEXT_GAP; // 预留间隔
            float iconY = getY() + (getHeight() - ICON_SIZE) / 2.0f;
            ShapeDrawer.drawScaledCustomSizeModalRect(iconX, iconY, 0, 0, 64, 64, ICON_SIZE, ICON_SIZE, 64, 64);
        }

        // 绘制标题文本
        int textColor = 0xFFFFFFFF;
        // 如果存在图标，则文本区域左边界向右偏移图标宽度和间隔
        int textOffset = (icon != null ? (ICON_SIZE + ICON_TEXT_GAP * 2) : 0);
        // 使文本在剩余区域内居中：剩余宽度为 (width - textOffset)
        float textX = getX() + textOffset;
        float textY = getY() + (getHeight() - fontRendererObj.FONT_HEIGHT) * 13.0f / 20;
        fontRendererObj.drawString(title, textX, textY, textColor,false);
    }
    @Override
    public boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= getEntryLeft() && mouseX <= getEntryRight()
                && mouseY >= getEntryTop() && mouseY <= getEntryBottom();
    }
    // 点击卡片后返回对应的详细设置界面
    public abstract ModDetailGui getModDetailGui();
}