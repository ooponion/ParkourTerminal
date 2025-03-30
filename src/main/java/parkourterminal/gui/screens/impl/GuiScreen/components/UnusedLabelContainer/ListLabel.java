package parkourterminal.gui.screens.impl.GuiScreen.components.UnusedLabelContainer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import parkourterminal.data.globalData.GlobalData;
import parkourterminal.global.GlobalConfig;
import parkourterminal.gui.layout.Padding;
import parkourterminal.gui.layout.UIComponent;
import parkourterminal.gui.screens.impl.GuiScreen.components.DisableTip;
import parkourterminal.gui.screens.impl.GuiScreen.components.Label;
import parkourterminal.gui.screens.impl.GuiScreen.components.UsedLabelContainer.UsedLabelContainer;
import parkourterminal.util.ShapeDrawer;

public class ListLabel extends UIComponent {
    private final Label label;
    private final DisableTip disableTip;
    private final UsedLabelContainer usedLabelContainer;
    private final UnusedLabelContainer unusedLabelContainer;
    private final int height=60;
    public ListLabel(Label label, DisableTip disableTip, UsedLabelContainer usedLabelContainer,UnusedLabelContainer unusedLabelContainer){
        this.label = label;
        this.disableTip=disableTip;
        this.usedLabelContainer=usedLabelContainer;
        this.unusedLabelContainer=unusedLabelContainer;
        this.setFocused(false);
        setPadding(new Padding(5));
        Update();
    }
    public String getFormattedString(){
        return GlobalData.getLabelColor()+getLabel().getLabel();
    }

    public Label getLabel(){
        return label;
    }
    @Override
    public void Update(){
        FontRenderer renderer=Minecraft.getMinecraft().fontRendererObj;
        int width=renderer.getStringWidth(getFormattedString());
        setSize(width+getPadding().left+getPadding().right,renderer.FONT_HEIGHT+getPadding().top+getPadding().bottom);

    };
    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton){
        if(mouseButton==2){
            return false;
        }
        if(!isMouseOver(mouseX,mouseY)){
            return false;
        }


        if(mouseButton==1){
            disableTip.setEnabled(true);
            disableTip.setPosition(mouseX,mouseY);
            disableTip.setChosenLabel(label);
            return true;
        }
        label.SetIsPressing(true);
        this.setFocused(true);
        label.setPosition(mouseX-label.getEntryHeight()/2,mouseY-label.getEntryHeight()/2);
        label.mouseClicked(mouseX,mouseY,mouseButton);
        usedLabelContainer.addComponent(label);
        for(UIComponent component: usedLabelContainer.getComponents()){
            component.setFocused(false);
        }
        label.setFocused(true);
        return true;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY,int state){
        this.setFocused(false);
    }
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        Update();
        FontRenderer renderer= Minecraft.getMinecraft().fontRendererObj;
        ShapeDrawer.drawRoundedRectBorder(getX(),getY(),getWidth(),getHeight(),0xFF258eeb,2);
        renderer.drawString(getFormattedString(),getEntryLeft(),getEntryTop(),0xFFFFFFFF);

    }
}