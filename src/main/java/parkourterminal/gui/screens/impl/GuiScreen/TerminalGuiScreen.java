package parkourterminal.gui.screens.impl.GuiScreen;

import net.minecraft.client.gui.GuiScreen;
import parkourterminal.gui.layout.UIComponent;
import parkourterminal.gui.screens.impl.GuiScreen.components.Label;
import parkourterminal.gui.screens.impl.GuiScreen.components.UnusedLabelContainer;
import parkourterminal.gui.screens.impl.GuiScreen.components.UsedLabelContainer.UsedLabelContainer;
import parkourterminal.gui.screens.impl.GuiScreen.components.manager.LabelManager;
import parkourterminal.gui.screens.intf.instantiationScreen.intf.InstantiationScreen;
import parkourterminal.gui.screens.intf.instantiationScreen.intf.ScreenID;

import java.io.IOException;

public class TerminalGuiScreen extends GuiScreen implements InstantiationScreen {
    private final UsedLabelContainer usedLabelContainer=new UsedLabelContainer();
    private final UnusedLabelContainer unusedLabelContainer=new UnusedLabelContainer();
    @Override
    public GuiScreen getScreenInstantiation() {
        return this;
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        usedLabelContainer.setIsOverlay(false);
        LabelManager.UpdateLabelValuesPerTick();
        usedLabelContainer.draw(mouseX, mouseY, partialTicks);
    }
    public void drawOverlay(){
        usedLabelContainer.setIsOverlay(true);
        LabelManager.UpdateLabelValuesPerTick();
        usedLabelContainer.draw(0,0,0);
    }
    @Override
    public ScreenID getScreenID() {
        return new ScreenID("TerminalGuiScreen");
    }
    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
    {
        usedLabelContainer.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        usedLabelContainer.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        usedLabelContainer.mouseReleased(mouseX, mouseY);
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
}
