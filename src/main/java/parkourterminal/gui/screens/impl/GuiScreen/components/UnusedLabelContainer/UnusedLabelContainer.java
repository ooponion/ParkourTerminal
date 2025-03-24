package parkourterminal.gui.screens.impl.GuiScreen.components.UnusedLabelContainer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import parkourterminal.gui.component.scrollBar.impl.ScrollBarImpl;
import parkourterminal.gui.component.scrollBar.intf.ScrollDirection;
import parkourterminal.gui.layout.*;
import parkourterminal.gui.screens.impl.GuiScreen.components.DisableTip;
import parkourterminal.gui.screens.impl.GuiScreen.components.Label;
import parkourterminal.gui.screens.impl.GuiScreen.components.UsedLabelContainer.UsedLabelContainer;
import parkourterminal.gui.screens.impl.GuiScreen.components.manager.LabelManager;
import parkourterminal.util.ScissorHelper;
import parkourterminal.util.ShapeDrawer;

import java.util.HashMap;

public class UnusedLabelContainer extends Container {
    private final DisableTip disableTip;
    private UsedLabelContainer usedLabelContainer;
    private ScrollBarImpl scrollBar=new ScrollBarImpl(0,0,4,0, ScrollDirection.VERTICAL);
    public UnusedLabelContainer(DisableTip disableTip,UsedLabelContainer usedLabelContainer){
        this.disableTip=disableTip;
        this.usedLabelContainer=usedLabelContainer;
        this.setPadding(new Padding(7));
        this.setLayoutManager(new noWarpLinearLayout(LayoutDirection.VERTICAL,2));
        this.setWidth(40);
        scrollBar.setColor(0x00000000,0x40000000);
        HashMap<String, Label> hashMap = LabelManager.getLabelList();
        for(Label label:hashMap.values()){
            ListLabel listLabel=new ListLabel(label,disableTip,usedLabelContainer,this);
            addComponent(listLabel);
        }
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        this.setY((int) (0-scrollBar.getInterpolatingContentOffset()));
        GuiScreen guiScreen=Minecraft.getMinecraft().currentScreen;
        if(guiScreen==null){
            return;
        }
        ScissorHelper.EnableScissor(guiScreen.width-getWidth(),0,getWidth(), guiScreen.height);
        ShapeDrawer.drawRoundedRect(getX(),getY(),getWidth(),getHeight(),0xAA312e72,3);
        ShapeDrawer.drawRoundedRectBorder(getX(),getY(),getWidth(),getHeight(),0xFF5d58c8,3);
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
        scrollBar.setHeight(this.getComponentsTotalHeight());
        disableTip.setEnabled(false);
    };
    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton){
        scrollBar.mouseClicked(mouseX, mouseY, mouseButton);
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    @Override
    public void mouseReleased(int mouseX, int mouseY){
        scrollBar.onRelease();
        deleteComponent(getFocusedUI());
        super.mouseReleased(mouseX, mouseY);
    }
    @Override
    public boolean mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick){
        scrollBar.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        return super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);

    }
    public ScrollBarImpl getScrollBar(){
        return scrollBar;
    }
}
