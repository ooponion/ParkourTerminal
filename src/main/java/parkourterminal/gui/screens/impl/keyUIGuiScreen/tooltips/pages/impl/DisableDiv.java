package parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.impl;

import net.minecraft.client.Minecraft;
import parkourterminal.gui.component.Div;
import parkourterminal.util.ShapeDrawer;

public class DisableDiv extends Div {
    private boolean disabled=false;
    private final int width=60;
    private final int height=15;
    private final int rectSize=5;
    private final int rectX=3;
    private final int rectY=5;
    private DisableListener  disableListener;
    public DisableDiv() {
        super(60,15);
        setSize(width,height);
    }
    public void setDisableListener(DisableListener disableListener) {
        this.disableListener = disableListener;
    }
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
    public boolean isDisabled() {
        return disabled;
    }
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        ShapeDrawer.drawRoundedRectBorder(getX()+rectX,getY()+rectY,rectSize,rectSize,0xFF4a6a71,1);
        Minecraft.getMinecraft().fontRendererObj.drawString("Disabled",getX()+rectX+rectSize+3,getY()+rectY-1,0xFF4a6a71);
        if(disabled){
            ShapeDrawer.drawLine(getX()+rectX+1,getY()+rectY+1,getX()+rectX+rectSize-2,getY()+rectY+rectSize-2,0xFF4a6a71,1f);
            ShapeDrawer.drawLine(getX()+rectX+rectSize-2,getY()+rectY+1,getX()+rectX+1,getY()+rectY+rectSize-2,0xFF4a6a71,1f);
        }
    }
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!isMouseOver(mouseX, mouseY)) {
            return false;
        }
        if (isEnabled()) {
            if (isInRect(mouseX, mouseY)) {
                disabled=!disabled;
                if(disabled){
                    disableListener.onDisable();
                }else{
                    disableListener.onEnable();
                }
                return true;
            }
        }
        return false;
    }
    private boolean isInRect(int mouseX, int mouseY) {
        return mouseX >= getX()+rectX && mouseX <= getX()+rectX+rectSize &&
                mouseY >= getY()+rectY && mouseY <= getY() + getY()+rectY+rectSize;
    }
    public interface DisableListener{
        public void onDisable();
        public void onEnable();
    }
}
