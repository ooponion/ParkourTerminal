package parkourterminal.gui.screens.impl.GuiScreen.components.UnusedLabelContainer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import parkourterminal.gui.component.scrollBar.impl.ScrollBarImpl;
import parkourterminal.gui.component.scrollBar.intf.ScrollDirection;
import parkourterminal.gui.layout.*;
import parkourterminal.gui.screens.impl.GuiScreen.components.DisableTip;
import parkourterminal.gui.screens.impl.GuiScreen.components.Label;
import parkourterminal.gui.screens.impl.GuiScreen.components.UsedLabelContainer.UsedLabelContainer;
import parkourterminal.util.ScissorHelper;
import parkourterminal.util.ShapeDrawer;

import java.util.Comparator;
import java.util.HashMap;

public class UnusedLabelContainer extends Container {
    private final DisableTip disableTip;
    private final ExpansionFrame expansionFrame ;
    private UsedLabelContainer usedLabelContainer;
    public UnusedLabelContainer(DisableTip disableTip,UsedLabelContainer usedLabelContainer){
        super(ScrollDirection.VERTICAL);
        this.disableTip=disableTip;
        this.usedLabelContainer=usedLabelContainer;
        this.expansionFrame =new ExpansionFrame(20,20,0xAA312e72,0xFF312e72,3,this);
        this.setEnabled(false);
        this.setPadding(new Padding(7));
        this.setLayoutManager(new noWarpLinearLayout(LayoutDirection.VERTICAL,2));
        this.setWidth(100);
        getScrollBar().setColor(0x00000000,0x40000000);
        displayScrollBar(false);
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        expansionFrame.draw(mouseX,mouseY,partialTicks);
        if(!isEnabled()){
            return;
        }
        GuiScreen guiScreen=Minecraft.getMinecraft().currentScreen;
        if(guiScreen==null){
            return;
        }
        ScissorHelper.EnableScissor(guiScreen.width-getWidth(),0,getWidth(), guiScreen.height);
        ShapeDrawer.drawRoundedRect(getX(),0,getWidth(),getHeight(),0xAA312e72,3);
        ShapeDrawer.drawRoundedRectBorder(getX(),0,getWidth(),getHeight(),0xFF5d58c8,3);
        super.draw(mouseX, mouseY, partialTicks);
        ScissorHelper.DisableScissor();

    }
    @Override
    public void Update(){
        GuiScreen guiScreen=Minecraft.getMinecraft().currentScreen;
        if(guiScreen==null){
            return;
        }
        this.setX(guiScreen.width-getWidth());
        this.setHeight(guiScreen.height);
        disableTip.setEnabled(false);
        expansionFrame.Update();
        super.Update();
    };
    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton){
        if(expansionFrame.mouseClicked(mouseX, mouseY, mouseButton)){
            return true;
        }
        if(!isEnabled()){
            return false;
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    @Override
    public void mouseReleased(int mouseX, int mouseY,int state){
        if(!isEnabled()||state!=0){
            return;
        }
        deleteComponent(getFocusedUI());
        super.mouseReleased(mouseX, mouseY,state);
    }
    @Override
    public boolean mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick){
        if(!isEnabled()){
            return false;
        }
        return super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);

    }
    @Override
    public boolean isMouseOver(int mouseX, int mouseY) {
        return isEnabled()&&super.isMouseOver(mouseX, mouseY);
    }
    @Override
    public void deleteComponent(UIComponent component){
        super.deleteComponent(component);
        super.Update();
    }
    @Override
    public void addComponent(UIComponent component) {
        super.addComponent(component);
        getComponents().sort(new Comparator<UIComponent>() {
            @Override
            public int compare(UIComponent o1, UIComponent o2) {
                String label1=((ListLabel)o1).getLabel().getLabel();
                String label2=((ListLabel)o2).getLabel().getLabel();
                return label1.compareTo(label2);
            }
        });
        super.Update();
    }
}
