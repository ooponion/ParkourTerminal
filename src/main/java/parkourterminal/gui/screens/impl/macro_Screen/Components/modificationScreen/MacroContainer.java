package parkourterminal.gui.screens.impl.macro_Screen.Components.modificationScreen;

import net.minecraft.client.Minecraft;
import parkourterminal.data.macroData.intf.Macro;
import parkourterminal.data.macroData.intf.Operation;
import parkourterminal.global.json.TerminalJsonConfig;
import parkourterminal.gui.component.scrollBar.intf.ScrollDirection;
import parkourterminal.gui.layout.*;
import parkourterminal.util.RenderTextHelper;
import parkourterminal.util.ScissorHelper;

public class MacroContainer extends Container {
    private static int tickIndex=0;
    private static final Padding padding=new Padding(6,50,12,6);
    private int lineHeight=18;
    private int scrollWidth=4;
    private Macro macro;
    private int focusedIndex=-1;
    private final ButtonsContainer buttonsContainer;
    private final ConfirmContainer confirmContainer;
    public MacroContainer(int x,int y,int width,int height){
        super(new Margin(0,0,0,0),padding,new noWarpLinearLayout(LayoutDirection.VERTICAL,0),ScrollDirection.VERTICAL);
        buttonsContainer=new ButtonsContainer(x+width+12,y+50,75,210);
        confirmContainer=new ConfirmContainer(x,y,width+87,height);
        setSize(width, height);
        setPosition(x,y);
        displayScrollBar(false);
    }
    @Override
    public void setX(int x) {
        super.setX(x);
        buttonsContainer.setX(x+getWidth()+12);
        confirmContainer.setX(x);
    }
    @Override
    public void setY(int y) {
        super.setY(y);
        buttonsContainer.setY(y);
        confirmContainer.setY(y);
    }
    @Override
    public void setPosition(int x, int y){
        super.setPosition(x,y);
        buttonsContainer.setPosition(x+getWidth()+12,y);
        confirmContainer.setPosition(x,y);
    }
    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        confirmContainer.setHeight(height);
    }
    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        for(UIComponent component : getComponents()){
            component.setWidth(getEntryWidth());
        }
        confirmContainer.setWidth(width+5);
    }
    @Override
    public void setSize(int width, int height) {
        setWidth(width);
        setHeight(height);
        confirmContainer.setSize(width+5,height);
    }
    public void setMacro(String macroName){
        if(this.macro!=null&&macroName.equals(macro.getName()) ){
            return;
        }
        this.macro=TerminalJsonConfig.getMacroData().getMacroByName(macroName);
        if(this.macro==null){
            TerminalJsonConfig.getMacroData().addMacro(new Macro(macroName));
            this.macro=new Macro(macroName);
        }
        deleteComponents();
        for(Operation operation:macro.getMacro()){
            getComponents().add(new MacroLineUI(getEntryWidth(),operation));
        }
        focusedIndex=-1;
        getScrollBar().ResetScrollOffset();
        Update();
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if(macro==null){
            Minecraft mc=Minecraft.getMinecraft();
            RenderTextHelper.drawCenteredString(mc.fontRendererObj,"None", getX(),getY(), getWidth(),getHeight(), 0x33FFFFFF,false);
            return;
        }
        buttonsContainer.draw(mouseX,mouseY,partialTicks);
        if(macro.getMacro().isEmpty()){
            Minecraft mc=Minecraft.getMinecraft();
            RenderTextHelper.drawCenteredString(mc.fontRendererObj,"empty", getX(),getY(), getWidth(),getHeight(), 0x33FFFFFF,false);
            return;
        }
        DrawHeaders();
        tickIndex=0;
        ScissorHelper.EnableScissor(getX(),getEntryTop(),getWidth(),getEntryHeight());
        super.draw(mouseX, mouseY, partialTicks);
        ScissorHelper.DisableScissor();
        getScrollBar().draw(mouseX,mouseY,partialTicks);
        confirmContainer.draw(mouseX,mouseY,partialTicks);
    }
    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton){
        boolean state =buttonsContainer.mouseClicked(mouseX,mouseY,mouseButton);
        int buttonIndex=buttonsContainer.ClickedButton();
        switch(buttonIndex){
            case 0:duplicateLine(); break;
            case 1:deleteLine();break;
            case 2:newLine();break;
            case 3:deleteMacro();break;
            default:break;
        }
        boolean cancelClicked=confirmContainer.mouseClicked(mouseX,mouseY,mouseButton);
        buttonIndex=confirmContainer.ClickedButton();
        if(buttonIndex==1&&macro!=null){
            TerminalJsonConfig.getMacroData().deleteMacro(macro.getName());
            macro=null;
        }
        if(cancelClicked){
            return state;
        }
        state=false;
        int index=0;
        for (UIComponent component:getComponents()){
            boolean succ=component.mouseClicked(mouseX, mouseY, mouseButton);
            if(succ){
                focusedIndex=index;
                replaceMacro();
            }
            state|=succ;
            component.setFocused(succ&&mouseOverPadding(mouseX,mouseY));
            ((MacroLineUI)component).setSelected(false);
            index++;
        }
        System.out.printf("macroContainer.click3");
        if(focusedIndex>=0&&focusedIndex<getComponents().size()) {
            ((MacroLineUI) getComponents().get(focusedIndex)).setSelected(true);
        }
        getScrollBar().mouseClicked(mouseX, mouseY, mouseButton);
        return state||getScrollBar().isMouseOver(mouseX,mouseY);
    }
    @Override
    public boolean mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick){
        if(!isMouseOver(mouseX,mouseY)){
            return false;
        }
        getScrollBar().mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        return super.mouseClickMove(mouseX,mouseY,clickedMouseButton,timeSinceLastClick);
    }
    @Override
    public void mouseReleased(int mouseX, int mouseY,int state){
        buttonsContainer.mouseReleased(mouseX,mouseY,state);
        confirmContainer.mouseReleased(mouseX,mouseY,state);
        getScrollBar().mouseReleased(mouseX,mouseY,state);
        super.mouseReleased(mouseX, mouseY, state);
    }
    public void deleteMacro(){
        confirmContainer.setEnabled(true);
    }
    public void duplicateLine(){
        if(macro==null||focusedIndex<0||focusedIndex>= macro.getSize()){
            return;
        }
        Operation operation=macro.duplicateLine(focusedIndex);
        if(operation==null){
            return;
        }
        getComponents().add(focusedIndex+1,new MacroLineUI(getEntryWidth(),operation));
        Update();
        replaceMacro();
    }
    public void newLine(){
        System.out.printf("newLine.macroIsNull?:::%s\n",macro==null);
        if(macro==null){
            return;
        }
        System.out.printf("newLine.macroIsEmpty?:::%s\n",macro.getMacro().isEmpty());
        Operation operation=macro.newLine(focusedIndex);
        System.out.printf("newLine.operationIsNull?:::%s\n",operation==null);
        if(operation==null){
            return;
        }
        System.out.printf("newLine.operation.operates?:::%s\n",operation.getOperates());
        getComponents().add(focusedIndex+1,new MacroLineUI(getEntryWidth(),operation));
        Update();
        replaceMacro();
    }
    public void deleteLine(){
        if(macro==null||focusedIndex<0||focusedIndex>= macro.getSize()){
            return;
        }
        macro.deleteLine(focusedIndex);
        getComponents().remove(focusedIndex);
        if(focusedIndex>= macro.getSize()){
            focusedIndex=macro.getSize()-1;
        }
        Update();
        replaceMacro();
    }
    private void replaceMacro(){
        if(macro==null){
            return;
        }
        TerminalJsonConfig.getMacroData().addMacro(new Macro(macro));
    }
    public void keyTyped(char typedChar, int keyCode){
        UIComponent lineUI=getFocusedUI();
        if(lineUI==null){
            return;
        }
        ((MacroLineUI) lineUI).keyTyped(typedChar, keyCode);
        replaceMacro();
    }
    public void scrollWheel(int mouseX, int mouseY,int scrollAmount ){
        scrollAmount=(int) (scrollAmount*getEntryWidth()/214.5);
        boolean state=false;
        for(UIComponent ui:getComponents()){
            MacroLineUI macroLineUI=(MacroLineUI) ui;
            state|=macroLineUI.scrollWheel(mouseX,mouseY,scrollAmount);
        }
        if(state){
            return;
        }
        super.scrollWheel(mouseX,mouseY,scrollAmount);
    }
    private void DrawHeaders(){
        float partial= (float) (this.getEntryWidth()/16.5);
        Minecraft mc=Minecraft.getMinecraft();
        RenderTextHelper.drawCenteredString(mc.fontRendererObj,"W", (partial*1.5f) +getEntryLeft(),getY(), partial, padding.top, 0x33FFFFFF,false);
        RenderTextHelper.drawCenteredString(mc.fontRendererObj,"S", (partial*2.5f) +getEntryLeft(),getY(), partial, padding.top, 0x33FFFFFF,false);
        RenderTextHelper.drawCenteredString(mc.fontRendererObj,"A", (partial*3.5f) +getEntryLeft(),getY(), partial, padding.top, 0x33FFFFFF,false);
        RenderTextHelper.drawCenteredString(mc.fontRendererObj,"D", (partial*4.5f) +getEntryLeft(),getY(), partial, padding.top, 0x33FFFFFF,false);
        RenderTextHelper.drawCenteredString(mc.fontRendererObj,"SPACE", (partial*5.5f) +getEntryLeft(),getY(), partial*2f, padding.top, 0x33FFFFFF,false);
        RenderTextHelper.drawCenteredString(mc.fontRendererObj,"SNEAK", (partial*7.5f) +getEntryLeft(),getY(), partial*2f, padding.top, 0x33FFFFFF,false);
        RenderTextHelper.drawCenteredString(mc.fontRendererObj,"SPRINT", (partial*9.5f) +getEntryLeft(),getY(), partial*2f, padding.top, 0x33FFFFFF,false);
        RenderTextHelper.drawCenteredString(mc.fontRendererObj,"yaw", (partial*11.5f) +getEntryLeft(),getY(), partial*2.5f, padding.top, 0x33FFFFFF,false);
        RenderTextHelper.drawCenteredString(mc.fontRendererObj,"pitch", (partial*14) +getEntryLeft(),getY(), partial*2.5f, padding.top, 0x33FFFFFF,false);
    }
    public static int  getTickIndex(){
        return tickIndex;
    }
    public static void incrementTickIndex(){
        tickIndex++;

    }
}
