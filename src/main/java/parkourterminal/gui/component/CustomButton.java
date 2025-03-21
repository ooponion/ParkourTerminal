package parkourterminal.gui.component;

import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;
import parkourterminal.gui.layout.UIComponent;
import parkourterminal.util.AnimationUtils.impls.ColorInterpolateAnimation;
import parkourterminal.util.AnimationUtils.impls.interpolatingData.InterpolatingColor;
import parkourterminal.util.AnimationUtils.intf.AbstractAnimation;
import parkourterminal.util.AnimationUtils.intf.AnimationMode;
import parkourterminal.util.ShapeDrawer;

public class CustomButton extends UIComponent {
    private int normalColor, hoverColor;
    private int cornerRadius;
    private String text;

    private final AbstractAnimation<InterpolatingColor> animationColor;

    public CustomButton(int x, int y, int width, int height, int normalColor, int hoverColor, int cornerRadius, String text) {
        this.setSize(width, height);
        this.setPosition(x,y);
        this.normalColor = normalColor;
        this.hoverColor = hoverColor;
        this.cornerRadius = cornerRadius;
        this.text = text;
        this.animationColor= new ColorInterpolateAnimation(0.4f,new InterpolatingColor(normalColor),AnimationMode.BLENDED);
    }

    public void drawButton(FontRenderer fontRenderer, int mouseX, int mouseY) {
        // 计算悬停动画进度
        boolean isHovered = isMouseOver(mouseX, mouseY);
        if(isHovered){
            animationColor.RestartAnimation(new InterpolatingColor(hoverColor));
        }else{
            animationColor.RestartAnimation(new InterpolatingColor(normalColor));
        }


        // 绘制按钮背景
        GL11.glEnable(GL11.GL_BLEND);
        ShapeDrawer.drawRoundedRect(getOuterLeft(),getOuterTop(),getOuterWidth(),getOuterHeight(), animationColor.Update().getColor(), cornerRadius);
        GL11.glDisable(GL11.GL_BLEND);

        // 绘制按钮文本
        int textWidth = fontRenderer.getStringWidth(text);
        int textX = getX() + (getWidth() - textWidth) / 2;
        int textY = getY() + (getHeight() - fontRenderer.FONT_HEIGHT) / 2;
        fontRenderer.drawStringWithShadow(text, textX, textY, 0xFFFFFFFF);
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {

    }
    @Override
    public boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= getX() && mouseX < getX()+getWidth() && mouseY >= getY() && mouseY < getY() + getHeight();
    }
}
