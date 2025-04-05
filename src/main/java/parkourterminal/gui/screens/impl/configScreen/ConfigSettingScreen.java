package parkourterminal.gui.screens.impl.configScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;
import parkourterminal.gui.component.CustomButton;
import parkourterminal.gui.component.scrollBar.impl.ScrollBarImpl;
import parkourterminal.gui.component.scrollBar.intf.ScrollDirection;
import parkourterminal.gui.screens.impl.configScreen.intf.ConfigContainer;
import parkourterminal.gui.screens.intf.BlurGui;
import parkourterminal.gui.screens.intf.instantiationScreen.intf.InstantiationScreen;
import parkourterminal.gui.screens.intf.instantiationScreen.intf.ScreenID;
import parkourterminal.gui.screens.intf.instantiationScreen.manager.ScreenManager;

import java.io.IOException;

public class ConfigSettingScreen extends BlurGui implements InstantiationScreen {
    private final static int ConfirmButtonWidth=160;
    private final static int ConfirmButtonHeight=20;
    private final static int scrollBarWidth=4;
    private boolean initialized = false;
    private CustomButton ApplyButton =new CustomButton(0,0,ConfirmButtonWidth/2,ConfirmButtonHeight,0xee7f9ba7,0xee2e96c3,4,"Apply");
    private CustomButton WithDrawAllButton =new CustomButton(0,0,ConfirmButtonWidth/2,ConfirmButtonHeight,0xee7f9ba7,0xee2e96c3,4,"Withdraw");

    private final ConfigContainer container=new ConfigContainer();
    private final ScrollBarImpl scrollBar=new ScrollBarImpl(scrollBarWidth,0, ScrollDirection.VERTICAL);
    @Override
    public void initGui() {
        super.initGui();
        GuiScreen guiScreen= Minecraft.getMinecraft().currentScreen;
        if(guiScreen==null){
            return;
        }
        scrollBar.setColor(0x00000000,0x40000000);
        scrollBar.setHeight(guiScreen.height-ConfirmButtonHeight);
        scrollBar.setX(guiScreen.width-scrollBarWidth);
        scrollBar.UpdateContentSize(container.getComponentsTotalHeight());
        container.setSize(guiScreen.width,guiScreen.height-ConfirmButtonHeight);
        container.Update();
        ApplyButton.setPosition((guiScreen.width-ConfirmButtonWidth)/2, guiScreen.height-ConfirmButtonHeight);
        WithDrawAllButton.setPosition(guiScreen.width/2, guiScreen.height-ConfirmButtonHeight);
        if(!initialized){
            container.SyncFromConfig();
        }
        initialized=true;
    }
    @Override
    public GuiScreen getScreenInstantiation() {
        return this;
    }

    @Override
    public ScreenID getScreenID() {
        return new ScreenID("ConfigSettingScreen");
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        super.drawScreen(mouseX, mouseY, partialTicks);
        scrollBar.Update();
        container.setY((int) -scrollBar.getInterpolatingContentOffset());
        container.draw(mouseX, mouseY, partialTicks);
        ApplyButton.draw(mouseX, mouseY, partialTicks);
        WithDrawAllButton.draw(mouseX, mouseY, partialTicks);
    }
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        container.keyTyped(typedChar, keyCode);
    }
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        container.mouseClicked(mouseX, mouseY, mouseButton);
        if (ApplyButton.mouseClicked(mouseX, mouseY, mouseButton)) {
            container.SaveConfig();
            ScreenManager.SwitchToGame();
        }
        if (WithDrawAllButton.mouseClicked(mouseX, mouseY, mouseButton)) {
            container.WithDraw();
        }
    }
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        // 处理鼠标滚轮事件
        int scrollAmount = Mouse.getEventDWheel();
        if (scrollAmount != 0) {
            scrollAmount = scrollAmount > 0 ? -20 : 20; // 反转滚动方向
            scrollBar.scrollWheel(scrollAmount );// 每次滚动20像素
        }
    }
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        initialized = false;
    }
}
