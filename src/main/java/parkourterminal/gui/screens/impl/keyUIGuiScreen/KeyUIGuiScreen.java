package parkourterminal.gui.screens.impl.keyUIGuiScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import parkourterminal.gui.layout.Container;
import parkourterminal.gui.layout.ContainerKeyTyped;
import parkourterminal.gui.layout.UIComponent;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.KeyBoard.KeyUI;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.KeyBoard.KeyUIManager;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.manager.LabelManager;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.KeyUITooltips;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.ToolTipManager;
import parkourterminal.gui.screens.intf.BlurGui;
import parkourterminal.gui.screens.intf.instantiationScreen.intf.InstantiationScreen;
import parkourterminal.gui.screens.intf.instantiationScreen.intf.ScreenID;
import parkourterminal.gui.screens.intf.instantiationScreen.manager.ScreenManager;
import parkourterminal.util.ShapeDrawer;
import parkourterminal.util.SystemOutHelper;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;
import java.io.IOException;
import java.util.ArrayList;

public class KeyUIGuiScreen extends BlurGui implements InstantiationScreen {
    private ContainerKeyTyped container=new ContainerKeyTyped(){
        @Override
        public boolean mouseClicked(int mouseX, int mouseY, int mouseButton){
            if(isEnabled()){
                boolean state=false;
                boolean setFocusedFalse=false;
                for (UIComponent component:new ArrayList<UIComponent>(getComponents())){
                    if(!state){
                        state = component.mouseClicked(mouseX, mouseY, mouseButton);
                    }
                    if(setFocusedFalse){
                        component.setFocused(false);
                    }
                    if(component.isFocused()){
                        setFocusedFalse=true;
                    }
                }
                return state;
            }
            return false;
        }
    };
    public KeyUIGuiScreen(){
        container.setLayoutManager(null);
        container.addComponent(ToolTipManager.getInstance().getKeyUITooltips());
        container.addComponent(KeyUIManager.getInstance().getContainer());
    }
    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public GuiScreen getScreenInstantiation() {
        return this;
    }

    public void overlayDraw(){
    }
    @Override
    public ScreenID getScreenID() {
        return new ScreenID("KeyUIGuiScreen");
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        super.drawScreen(mouseX, mouseY, partialTicks);
        ShapeDrawer.drawRect(0, 0, this.width, this.height, 0x80101010);
        container.draw(mouseX,mouseY,partialTicks);
    }
    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
    {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        container.mouseClickMove(mouseX, mouseY, clickedMouseButton,timeSinceLastClick);
//        KeyUIManager.getInstance().getContainer().mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
//        ToolTipManager.getInstance().getKeyUITooltips().mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        SystemOutHelper.printf("mouseClickMove");
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        KeyUIManager.getInstance().getContainer().mouseClickedToGetKeyUI(mouseX, mouseY, mouseButton);
//        ToolTipManager.getInstance().getKeyUITooltips().mouseClicked(mouseX, mouseY, mouseButton);
//        if(!ToolTipManager.getInstance().getKeyUITooltips().isEnabled()||!ToolTipManager.getInstance().getKeyUITooltips().isMouseOver(mouseX, mouseY)){
//            KeyUIManager.getInstance().getContainer().mouseClicked(mouseX, mouseY, mouseButton);
//        }
        container.mouseClicked(mouseX, mouseY, mouseButton);
        SystemOutHelper.printf("mouseClicked");
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        container.mouseReleased(mouseX, mouseY, state);
//        KeyUIManager.getInstance().getContainer().mouseReleased(mouseX, mouseY, state);
//        ToolTipManager.getInstance().getKeyUITooltips().mouseReleased(mouseX, mouseY, state);
        SystemOutHelper.printf("mouseReleased");
    }
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        super.keyTyped(typedChar,keyCode);
        container.keyTyped(typedChar,keyCode);
//        KeyUIManager.getInstance().getContainer().keyTyped(typedChar, keyCode);
//        ToolTipManager.getInstance().getKeyUITooltips().keyTyped(typedChar, keyCode);
        SystemOutHelper.printf("keyTyped");
    }
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int guiMouseX = Mouse.getX() * this.width / this.mc.displayWidth;
        int guiMouseY = this.height - Mouse.getY() * this.height / this.mc.displayHeight - 1;
        // 处理鼠标滚轮事件
        int scrollAmount = Mouse.getEventDWheel();
        if (scrollAmount != 0) {
            scrollAmount = scrollAmount > 0 ? -20 : 20; // 反转滚动方向
            ToolTipManager.getInstance().getKeyUITooltips().scrollWheel(guiMouseX, guiMouseY, scrollAmount);
        }
    }
    @Override
    public void handleKeyboardInput() throws IOException {
        if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE && Keyboard.getEventKeyState()) {
            ScreenManager.SwitchToScreen(new ScreenID("TerminalGuiScreen"));
            ToolTipManager.getInstance().getKeyUITooltips().setEnabled(false);
            return;
        }

        super.handleKeyboardInput();
    }

}
