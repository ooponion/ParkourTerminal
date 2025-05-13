package parkourterminal.gui.screens.impl.macro_Screen.Components.SearchingScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import parkourterminal.data.GlobalData;
import parkourterminal.data.macroData.intf.Macro;
import parkourterminal.data.macroData.intf.Operation;
import parkourterminal.global.json.TerminalJsonConfig;
import parkourterminal.global.json.TerminalJsonRoot;
import parkourterminal.gui.component.scrollBar.impl.ScrollBarImpl;
import parkourterminal.gui.component.scrollBar.intf.ScrollDirection;
import parkourterminal.gui.layout.*;
import parkourterminal.gui.screens.impl.macro_Screen.Components.modificationScreen.MacroLineUI;
import parkourterminal.util.RenderTextHelper;
import parkourterminal.util.ScissorHelper;
import parkourterminal.util.StringSimilarityFinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchResultContainer extends Container {
    private int boxWidth=70;
    private int boxHeight=20;
    private int scrollWidth=4;
//    private ScrollBarImpl scrollBar=new ScrollBarImpl(0,0,scrollWidth,0, ScrollDirection.VERTICAL);
    private String macroName;
    public SearchResultContainer(int x,int y,int width,int height){
//        super(new Margin(0),new Padding(0),new noWarpLinearLayout(LayoutDirection.VERTICAL,6));
        super(new Margin(0),new Padding(0),new LinearLayout(LayoutDirection.HORIZONTAL,6),ScrollDirection.VERTICAL);
        setSize(width, height);
        setPosition(x,y);
//        scrollBar.setColor(0x00000000,0x40000000);
        SetQueryWord("");
    }
//    @Override
//    public void Update(){
//        scrollBar.UpdateContentSize(this.getComponentsTotalHeight()+ getPadding().bottom+ getPadding().top);
//    }
//    @Override
//    public void setX(int x) {
//        super.setX(x);
//        scrollBar.setPosition(x+getWidth()-scrollWidth,getY());
//    }
//    @Override
//    public void setY(int y) {
//        super.setY(y);
//        scrollBar.setPosition(getX()+getWidth()-scrollWidth,y);
//    }
//    @Override
//    public void setHeight(int height) {
//        super.setHeight(height);
//        scrollBar.setHeight(height);
//    }
//    @Override
//    public void setWidth(int width) {
//        super.setWidth(width);
//    }
//    @Override
//    public void setPosition(int x, int y){
//        super.setPosition(x,y);
//        scrollBar.setPosition(x+getWidth()-scrollWidth,y);
//    }
//    @Override
//    public void setSize(int width, int height){
//        super.setSize(width,height);
//        scrollBar.setHeight(height);
//    }
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        this.Update();
        ScissorHelper.EnableScissor(getX(),getY(),getWidth(),getHeight());
        super.draw(mouseX, mouseY, partialTicks);
        ScissorHelper.DisableScissor();
    }
    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton){
        getScrollBar().mouseClicked(mouseX, mouseY, mouseButton);
        for (UIComponent component:getComponents()){
            if(component.isMouseOver(mouseX, mouseY)){
                macroName=((SearchResultUI) component).getName();
                return true;
            };
        }
        return false;
    }
    @Override
    public boolean mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick){

        return super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }
    @Override
    public void mouseReleased(int mouseX, int mouseY,int state){
        super.mouseReleased(mouseX, mouseY, state);
    }

    public void keyTyped(char typedChar, int keyCode){

    }
    public void scrollWheel(int mouseX, int mouseY,int scrollAmount ){
        super.scrollWheel(mouseX,mouseY,scrollAmount);
    }
    public void SetQueryWord(String query){
        List<String> names=new ArrayList<String>(TerminalJsonConfig.getMacroData().getMacroNames());
        if(query.isEmpty()){
            this.deleteComponents();
            for(String name: names){
                addComponent(new SearchResultUI(boxWidth,boxHeight,name,false));
            }
            Update();
            return;
        }
        List<String> matchingNames=StringSimilarityFinder.findSimilar(query,names,2);
        this.deleteComponents();
        if(!names.contains(query)){
            addComponent(new SearchResultUI(boxWidth,boxHeight,query,true));
        }
        for(String name: matchingNames){
            addComponent(new SearchResultUI(boxWidth,boxHeight,name,false));
        }
        Update();
    }
    public String getSelectedMacroName() {
        return macroName;
    }
}
