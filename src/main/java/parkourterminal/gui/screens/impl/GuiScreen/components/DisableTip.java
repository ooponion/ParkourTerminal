package parkourterminal.gui.screens.impl.GuiScreen.components;

import net.minecraft.client.Minecraft;
import parkourterminal.gui.layout.UIComponent;
import parkourterminal.gui.screens.impl.GuiScreen.components.Label;
import parkourterminal.util.AnimationUtils.impls.BeizerAnimation;
import parkourterminal.util.AnimationUtils.impls.interpolatingData.Interpolatingfloat;
import parkourterminal.util.AnimationUtils.intf.AbstractAnimation;
import parkourterminal.util.AnimationUtils.intf.AnimationMode;
import parkourterminal.util.ShapeDrawer;

public class DisableTip extends UIComponent {
    private final AbstractAnimation<Interpolatingfloat> animation=new BeizerAnimation<Interpolatingfloat>(0.2f,new Interpolatingfloat(0), AnimationMode.BLENDED);
    private final int width=60;
    private final int height=15;
    private final int rectSize=5;
    private final int rectX=3;
    private final int rectY=5;
    private boolean ifEnabled =false;
    private Label label;
    public DisableTip(){
        setEnabled(false);
        setSize(width,height);
    }
    @Override
    public void setEnabled(boolean enabled){
        super.setEnabled(enabled);
        if(enabled){
            animation.RestartAnimation(new Interpolatingfloat(1));
        }else{
            animation.RestartAnimation(new Interpolatingfloat(0));
        }
    }
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if(label!=null){
            ifEnabled =label.isEnabled();
        }
        float progress=animation.Update().getValue();

        if(progress>=0.99f){
            ShapeDrawer.drawRoundedRect(getX(),getY(),width,height,0xEE384649,2);
            ShapeDrawer.drawRoundedRectBorder(getX()+rectX,getY()+rectY,rectSize,rectSize,0xFF4a6a71,1);
            Minecraft.getMinecraft().fontRendererObj.drawString("Disabled",getX()+rectX+rectSize+3,getY()+rectY-1,0xFF4a6a71);
            if(!ifEnabled){
                ShapeDrawer.drawLine(getX()+rectX+1,getY()+rectY+1,getX()+rectX+rectSize-2,getY()+rectY+rectSize-2,0xFF4a6a71);
                ShapeDrawer.drawLine(getX()+rectX+rectSize-2,getY()+rectY+1,getX()+rectX+1,getY()+rectY+rectSize-2,0xFF4a6a71);
            }
        }
    }
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton){
        if(!isMouseOver(mouseX,mouseY)){
            this.setEnabled(false);
            return false;
        }
        if(isEnabled()){
            if(isInRect(mouseX,mouseY)){
                this.setEnabled(false);
                if(label!=null){
                    label.setEnabled(!ifEnabled);
                }
            }
            return true;
        }
        return false;
    }
    public void setChosenLabel(Label label){
        this.label=label;
    }
    private boolean isInRect(int mouseX, int mouseY) {
        return mouseX >= getX()+rectX && mouseX <= getX()+rectX+rectSize &&
                mouseY >= getY()+rectY && mouseY <= getY() + getY()+rectY+rectSize;
    }

}
