package parkourterminal.gui.Button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;
import parkourterminal.gui.layout.UIComponent;
import parkourterminal.util.AnimationUtils.impls.ColorInterpolateAnimation;
import parkourterminal.util.AnimationUtils.impls.interpolatingData.InterpolatingColor;
import parkourterminal.util.AnimationUtils.intf.AbstractAnimation;
import parkourterminal.util.AnimationUtils.intf.AnimationMode;
import parkourterminal.util.RenderTextHelper;
import parkourterminal.util.ShapeDrawer;

public class CustomButton extends UIComponent {
    protected int normalColor, hoverColor;
    protected int cornerRadius;
    protected String text;
    protected boolean clicked;
    protected final AbstractAnimation<InterpolatingColor> animationColor;
    protected ButtonClickEventListener clickEventListener;

    public CustomButton(int x, int y, int width, int height, int normalColor, int hoverColor, int cornerRadius, String text) {
        this.setSize(width, height);
        this.setPosition(x,y);
        this.normalColor = normalColor;
        this.hoverColor = hoverColor;
        this.cornerRadius = cornerRadius;
        this.text = text;
        this.animationColor= new ColorInterpolateAnimation(0.4f,new InterpolatingColor(normalColor),AnimationMode.BLENDED);
    }
    public CustomButton( int width, int height, int normalColor, int hoverColor, int cornerRadius, String text) {
        this(0,0,width,height,normalColor,hoverColor,cornerRadius,text);
    }

    public void drawButton(FontRenderer fontRenderer, int mouseX, int mouseY) {
        // 计算悬停动画进度
        boolean isHovered = isMouseOver(mouseX, mouseY);
        if(isHovered){
            animationColor.RestartAnimation(new InterpolatingColor(hoverColor));
        }else{
            animationColor.RestartAnimation(new InterpolatingColor(normalColor));
        }
        
        GL11.glEnable(GL11.GL_BLEND);
        ShapeDrawer.drawRoundedRect(getOuterLeft(),getOuterTop(),getOuterWidth(),getOuterHeight(), animationColor.Update().getColor(), cornerRadius);
        GL11.glDisable(GL11.GL_BLEND);

        RenderTextHelper.drawCenteredLinkBreakString(fontRenderer,text, getEntryLeft(),getEntryTop(),getEntryWidth(),getEntryHeight(),isEnabled()?0xFFFFFFFF:0xFFaaaaaa,true);
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        this.drawButton(Minecraft.getMinecraft().fontRendererObj,mouseX,mouseY);
    }
    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton){
        if(isMouseOver(mouseX, mouseY)&&isEnabled()){
            setFocused(true);
            clicked=true;
            if(clickEventListener!=null){
                clickEventListener.onButtonClick();
            }
            return true;
        }
        setFocused(false);
        clicked=false;
        return false;
    }
    @Override
    public void mouseReleased(int mouseX, int mouseY,int state){
        clicked=false;
    }
    public boolean isClicked(){return clicked;}
    public void setClickEventListener(ButtonClickEventListener clickEventListener){
        this.clickEventListener=clickEventListener;
    }
}
