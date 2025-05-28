package parkourterminal.gui.screens.impl.GuiScreen.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import parkourterminal.data.GlobalData;
import parkourterminal.gui.layout.KeyTyped;
import parkourterminal.gui.layout.UIComponent;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.intf.LabelValue;
import parkourterminal.gui.screens.impl.GuiScreen.manager.TerminalGuiManager;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.KeyBoard.intf.SideMode;
import parkourterminal.util.ShapeDrawer;

import java.io.IOException;

public class Label extends UIComponent implements KeyTyped {
//    private boolean isPressing=false;
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
        return GlobalData.getLabelColor() +label+": "+value.getValue();
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
        if(this.isFocused()&&!isOverlay){
            ShapeDrawer.drawRect(getX(),getY(),getWidth()+getX(),getHeight()+getY(),0x88ffc83f);
        }
        GlStateManager.disableBlend();
        GlStateManager.disableDepth();
        if(this.isEnabled()){
            renderer.drawStringWithShadow(getFormattedString(),getX(),getY(),0xFFFFFFFF);
        }
        else if(!isOverlay){
            renderer.drawStringWithShadow(EnumChatFormatting.getTextWithoutFormattingCodes(getFormattedString()),getX(),getY(),0xFF999999);
            ShapeDrawer.drawLine(getX(),getY()+getHeight()/2.0f,getX()+getWidth(),getY()+getHeight()/2.0f,0xFF999999,1f);
        }
        GlStateManager.enableBlend();
        GlStateManager.enableDepth();
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
        this.setFocused(true);
        if(mouseButton==1){
            DisableTip disableTip= TerminalGuiManager.getInstance().getDisableTip();
            disableTip.setEnabled(true);
            disableTip.setPosition(mouseX,mouseY);
            disableTip.setChosenLabel(this);
        }
        return true;
    }
    @Override
    public void mouseReleased(int mouseX, int mouseY,int state){

    }
    @Override
    public boolean mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick){
        GuiScreen guiScreen= Minecraft.getMinecraft().currentScreen;
        if(guiScreen==null){
            return false;
        }
        if(isFocused()&&clickedMouseButton==0){
            int validX= MathHelper.clamp_int(mouseX+OffsetX,0,guiScreen.width-getWidth());
            int validY= MathHelper.clamp_int(mouseY+OffsetY,0,guiScreen.height-getHeight());
            this.setPosition(validX,validY);
            return true;
        }
        return false;
    }
    public void setIsOverlay(boolean isOverlay){
        this.isOverlay=isOverlay;
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        GuiScreen guiScreen= Minecraft.getMinecraft().currentScreen;
        if(guiScreen==null){
            return ;
        }
        if(isFocused()){
            int dx=0;
            int dy=0;
            if (keyCode == Keyboard.KEY_UP) {
                dy=-1;
            } else if (keyCode == Keyboard.KEY_DOWN) {
                dy=1;
            } else if (keyCode == Keyboard.KEY_LEFT) {
                dx=-1;
            } else if (keyCode == Keyboard.KEY_RIGHT) {
                dx=1;
            }
            int validX= MathHelper.clamp_int(getX()+dx,0,guiScreen.width-getWidth());
            int validY= MathHelper.clamp_int(getY()+dy,0,guiScreen.height-getHeight());
            this.setPosition(validX,validY);
        }
    }
}
