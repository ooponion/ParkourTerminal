package parkourterminal.gui.screens.impl.GuiScreen.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.EnumChatFormatting;
import parkourterminal.global.GlobalConfig;
import parkourterminal.gui.layout.UIComponent;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.intf.LabelValue;
import parkourterminal.util.ShapeDrawer;

public class Label extends UIComponent {
    private boolean isPressing=false;
    private final String label;
    private final LabelValue value;
    private int OffsetX;
    private int OffsetY;
    private boolean isOverlay=false;
    public Label(String label, LabelValue value){
        this.label = label;
        this.value = value;
        Update();
    }
    public String getFormattedString(){
        return GlobalConfig.getLabelColor() +label+": "+value.getValue();
    }

    public String getLabel(){
        return label;
    }
    public LabelValue getValue(){
        return value;
    }
    @Override
    public void Update(){
        FontRenderer renderer=Minecraft.getMinecraft().fontRendererObj;
        int width=renderer.getStringWidth(getFormattedString());
        setSize(width,renderer.FONT_HEIGHT);
    };
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        Update();
        FontRenderer renderer=Minecraft.getMinecraft().fontRendererObj;
        if(isPressing){
            ShapeDrawer.drawRect(getX(),getY(),getWidth()+getX(),getHeight()+getY(),0x88ffc83f);
        }
        else if(this.isFocused()){
            ShapeDrawer.drawRect(getX(),getY(),getWidth()+getX(),getHeight()+getY(),0x66ffd849);
        }
        if(this.isEnabled()){
            renderer.drawString(getFormattedString(),getX(),getY(),0xFFFFFFFF);
        }
        else if(!isOverlay){
            renderer.drawString(EnumChatFormatting.getTextWithoutFormattingCodes(getFormattedString()),getX(),getY(),0xFF999999);
            ShapeDrawer.drawLine(getX(),getY()+getHeight()/2.0f,getX()+getWidth(),getY()+getHeight()/2.0f,0xFF999999);
        }
    }
    public void SetIsPressing(boolean isPressing){
        this.isPressing=isPressing;
    }
    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton){
        if(mouseButton==2){
            return false;
        }
        if(!isMouseOver(mouseX,mouseY)){
            this.setFocused(false);
            return false;
        }
        OffsetX=getX()-mouseX;
        OffsetY=getY()-mouseY;

        return true;
    }
    @Override
    public void mouseReleased(int mouseX, int mouseY,int state){
        isPressing=false;

        //判断是否落入unusedContainer
    }
    @Override
    public boolean mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick){
        if(isPressing&&clickedMouseButton==0){
            this.setPosition(mouseX+OffsetX,mouseY+OffsetY);
            return true;
        }
        return false;
    }
    public void setIsOverlay(boolean isOverlay){
        this.isOverlay=isOverlay;
    }
    public boolean isPressing(){
        return isPressing;
    }
}
