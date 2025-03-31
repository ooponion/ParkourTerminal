package parkourterminal.gui.screens.impl.GuiScreen;

import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;
import parkourterminal.gui.layout.UIComponent;
import parkourterminal.gui.screens.impl.GuiScreen.components.DisableTip;
import parkourterminal.gui.screens.impl.GuiScreen.components.Label;
import parkourterminal.gui.screens.impl.GuiScreen.components.UnusedLabelContainer.ListLabel;
import parkourterminal.gui.screens.impl.GuiScreen.components.UnusedLabelContainer.UnusedLabelContainer;
import parkourterminal.gui.screens.impl.GuiScreen.components.UsedLabelContainer.UsedLabelContainer;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.manager.LabelManager;
import parkourterminal.gui.screens.intf.instantiationScreen.intf.InstantiationScreen;
import parkourterminal.gui.screens.intf.instantiationScreen.intf.ScreenID;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TerminalGuiScreen extends GuiScreen implements InstantiationScreen {
    private final DisableTip disableTip=new DisableTip();
    private final UsedLabelContainer usedLabelContainer;
    private final UnusedLabelContainer unusedLabelContainer;

    public TerminalGuiScreen() {

        usedLabelContainer =new UsedLabelContainer(disableTip);
        unusedLabelContainer =new UnusedLabelContainer(disableTip,usedLabelContainer);
        usedLabelContainer.SetUnusedLabelContainer(unusedLabelContainer);
    }

    @Override
    public void initGui() {
        super.initGui();
        disableTip.setFocused(false);
        unusedLabelContainer.Update();
    }

    @Override
    public GuiScreen getScreenInstantiation() {
        return this;
    }

    public void drawOverlay(){
        usedLabelContainer.setIsOverlay(true);
        LabelManager.UpdateLabelValuesPerTick(0f);
        usedLabelContainer.draw(0,0,0);
    }
    @Override
    public ScreenID getScreenID() {
        return new ScreenID("TerminalGuiScreen");
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        usedLabelContainer.setIsOverlay(false);
        LabelManager.UpdateLabelValuesPerTick(partialTicks);
        usedLabelContainer.draw(mouseX, mouseY, partialTicks);
        unusedLabelContainer.draw(mouseX, mouseY, partialTicks);
        Label label=(Label) usedLabelContainer.getFocusedUI();
        if(label!=null){
            label.draw(mouseX, mouseY, partialTicks);
        }
        disableTip.draw(mouseX, mouseY, partialTicks);
    }
    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
    {
        usedLabelContainer.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if(disableTip.mouseClicked(mouseX,mouseY,mouseButton)){
            return;
        }
        if(!unusedLabelContainer.mouseClicked(mouseX, mouseY, mouseButton)){
            usedLabelContainer.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        disableTip.mouseReleased(mouseX,mouseY,state);
        usedLabelContainer.mouseReleased(mouseX, mouseY,state);
        unusedLabelContainer.mouseReleased(mouseX, mouseY,state);
    }
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (keyCode == 1){
            for(UIComponent component:usedLabelContainer.getComponents()){
                Label label=(Label) component;
                label.setFocused(false);
            }
        }
        super.keyTyped(typedChar,keyCode);
    }
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        // 处理鼠标滚轮事件
        int scrollAmount = Mouse.getEventDWheel();
        if (scrollAmount != 0) {
            scrollAmount = scrollAmount > 0 ? -20 : 20; // 反转滚动方向
            unusedLabelContainer.getScrollBar().scrollWheel(scrollAmount );// 每次滚动20像素
        }
    }
    public List<Label> getUsedLabels(){
        List<Label> list=new ArrayList<Label>();
        for(UIComponent component:usedLabelContainer.getComponents()){
            list.add((Label) component);
        }
        return list;
    }
    public void InitContainers(List<Label> used,List<Label> unused){
        for(Label label:used){
            usedLabelContainer.addComponent(label);
        }
        for(Label label:unused){
            unusedLabelContainer.addComponent(new ListLabel(label,disableTip,usedLabelContainer,unusedLabelContainer));
        }
    }
}
