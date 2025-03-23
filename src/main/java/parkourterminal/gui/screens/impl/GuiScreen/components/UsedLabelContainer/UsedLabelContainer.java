package parkourterminal.gui.screens.impl.GuiScreen.components.UsedLabelContainer;

import parkourterminal.gui.layout.Container;
import parkourterminal.gui.layout.UIComponent;
import parkourterminal.gui.screens.impl.GuiScreen.components.Label;
import parkourterminal.gui.screens.impl.GuiScreen.components.manager.LabelManager;

import java.util.HashMap;

public class UsedLabelContainer extends Container {
    DisableTip disableTip=new DisableTip();
    public UsedLabelContainer(){
        disableTip.setFocused(false);
        this.setLayoutManagerEnabled(false);
        HashMap<String, Label> hashMap = LabelManager.getLabelList();
        for(Label label:hashMap.values()){
            addComponent(label);
        }
    }
    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton){

        if(disableTip.mouseClicked(mouseX,mouseY,mouseButton)){
            return true;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (UIComponent component:getComponents()){
            if(component.mouseClicked(mouseX, mouseY, mouseButton)){
                ((Label)component).SetIsPressing(true);
                component.setFocused(true);
                if(mouseButton==1){
                    disableTip.setEnabled(true);
                    disableTip.setPosition(mouseX,mouseY);
                    disableTip.setChosenLabel((Label) component);
                }
                return true;
            }
        }
        return false;
    }
    @Override
    public void mouseReleased(int mouseX, int mouseY){
        disableTip.mouseReleased(mouseX,mouseY);
        super.mouseReleased(mouseX,mouseY);
    }
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        super.draw(mouseX, mouseY, partialTicks);
        disableTip.draw(mouseX, mouseY, partialTicks);
    }
    public void setIsOverlay(boolean isOverlay){
        for (UIComponent component:getComponents()){
            ((Label)component).setIsOverlay(isOverlay);
        }
    }
}
