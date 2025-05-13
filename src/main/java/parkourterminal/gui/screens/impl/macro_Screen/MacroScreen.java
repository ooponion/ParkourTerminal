package parkourterminal.gui.screens.impl.macro_Screen;

import com.google.common.base.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import parkourterminal.data.GlobalData;
import parkourterminal.data.macroData.MacroData;
import parkourterminal.data.macroData.intf.Macro;
import parkourterminal.data.macroData.intf.Operation;
import parkourterminal.global.json.TerminalJsonConfig;
import parkourterminal.gui.component.CustomGuiTextField;
import parkourterminal.gui.screens.impl.macro_Screen.Components.SearchingScreen.SearchResultContainer;
import parkourterminal.gui.screens.impl.macro_Screen.Components.SearchingScreen.SearchResultUI;
import parkourterminal.gui.screens.impl.macro_Screen.Components.modificationScreen.MacroContainer;
import parkourterminal.gui.screens.impl.macro_Screen.intf.MacroScreenState;
import parkourterminal.gui.screens.intf.BlurGui;
import parkourterminal.gui.screens.intf.instantiationScreen.intf.InstantiationScreen;
import parkourterminal.gui.screens.intf.instantiationScreen.intf.ScreenID;

import javax.annotation.Nullable;
import java.io.IOException;

public class MacroScreen extends BlurGui implements InstantiationScreen {
    private boolean firstOpen=true;
    private final CustomGuiTextField textField =new CustomGuiTextField(0,Minecraft.getMinecraft().fontRendererObj,800,20,90,50);
    private MacroScreenState macroScreenState=MacroScreenState.MACRO_MODIFICATION;
    private final MacroContainer macroContainer =new MacroContainer(230,20,500,300);
    private final SearchResultContainer searchResultContainer =new SearchResultContainer(180,20,600,300);
    @Override
    public void initGui() {
        super.initGui();
        if( firstOpen){
            textField.setMaxStringLength(20);
            textField.setValidator(new Predicate<String>() {
                @Override
                public boolean apply(@Nullable String input) {
                    if (input == null) return false;
                    return input.length() < 30;
                }
            });
            ReturnToModificationScreen(TerminalJsonConfig.getMacroData().getCurrentMacroName());
        }
        GuiScreen guiScreen= Minecraft.getMinecraft().currentScreen;
        if(guiScreen==null){
            return;
        }
        int width=Math.max(guiScreen.width,200);
        int height=Math.max(guiScreen.height,100);
        int textFieldWidth=Math.max(width/7,60);
        macroContainer.setWidth(width-75-12-textFieldWidth-20);
        macroContainer.setHeight(height-40);
        macroContainer.setX(20);
        macroContainer.Update();

        searchResultContainer.setWidth(width-textFieldWidth-20);
        searchResultContainer.setHeight(height-40);
        searchResultContainer.setX(20);

        textField.setWidth(textFieldWidth-20);
        textField.setX(width-textFieldWidth+10);
        firstOpen=false;
//        ApplyButton.setPosition((guiScreen.width-ConfirmButtonWidth)/2, guiScreen.height-ConfirmButtonHeight);
//        WithDrawAllButton.setPosition(guiScreen.width/2, guiScreen.height-ConfirmButtonHeight);
    }
    @Override
    public GuiScreen getScreenInstantiation() {
        return this;
    }

    @Override
    public ScreenID getScreenID() {
        return new ScreenID("MacroScreen");
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        super.drawScreen(mouseX, mouseY, partialTicks);
        textField.draw(mouseX, mouseY, partialTicks);
        if(macroScreenState==MacroScreenState.MACRO_MODIFICATION){
            macroContainer.draw(mouseX, mouseY, partialTicks);
        }else if (macroScreenState==MacroScreenState.QUERYING_RESULT){
            searchResultContainer.draw(mouseX, mouseY, partialTicks);
        }

//        ApplyButton.draw(mouseX, mouseY, partialTicks);
//        WithDrawAllButton.draw(mouseX, mouseY, partialTicks);
    }
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if(textField.isFocused()){
            textField.textboxKeyTyped(typedChar,keyCode);
            searchResultContainer.SetQueryWord(textField.getText());
            macroScreenState=MacroScreenState.QUERYING_RESULT;
            return;
        }
        if(macroScreenState==MacroScreenState.MACRO_MODIFICATION){
            macroContainer.keyTyped(typedChar, keyCode);
        }else if (macroScreenState==MacroScreenState.QUERYING_RESULT){
            if(!searchResultContainer.getComponents().isEmpty() &&keyCode == Keyboard.KEY_RETURN){
                ReturnToModificationScreen(((SearchResultUI)searchResultContainer.getComponents().get(0)).getName());
            }
        }

    }
    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick){
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        if(macroScreenState==MacroScreenState.MACRO_MODIFICATION){
            macroContainer.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        }else if (macroScreenState==MacroScreenState.QUERYING_RESULT){
            searchResultContainer.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        }

    }
    @Override
    public void mouseReleased(int mouseX, int mouseY,int state){
        super.mouseReleased(mouseX, mouseY, state);
        macroContainer.mouseReleased(mouseX, mouseY, state);
        searchResultContainer.mouseReleased(mouseX, mouseY, state);

    }
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if(textField.mouseClicked(mouseX, mouseY, mouseButton)){
            return;
        }
        if(macroScreenState==MacroScreenState.MACRO_MODIFICATION){
            macroContainer.mouseClicked(mouseX, mouseY, mouseButton);
        }else if (macroScreenState==MacroScreenState.QUERYING_RESULT){
            if(searchResultContainer.mouseClicked(mouseX, mouseY, mouseButton)){
                ReturnToModificationScreen(searchResultContainer.getSelectedMacroName());
            }
        }

//        if (ApplyButton.mouseClicked(mouseX, mouseY, mouseButton)) {
//            container.SaveConfig();
//            ScreenManager.SwitchToGame();
//        }
//        if (WithDrawAllButton.mouseClicked(mouseX, mouseY, mouseButton)) {
//            container.WithDraw();
//        }
    }
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int guiMouseX = Mouse.getX() * this.width / this.mc.displayWidth;
        int guiMouseY = this.height - Mouse.getY() * this.height / this.mc.displayHeight - 1;
        int scrollAmount = Mouse.getEventDWheel();

        if (scrollAmount != 0) {
            scrollAmount = scrollAmount > 0 ? -20 : 20;
            textField.scrollWheel(guiMouseX,guiMouseY,scrollAmount/5);
            if(macroScreenState==MacroScreenState.MACRO_MODIFICATION){
                macroContainer.scrollWheel(guiMouseX,guiMouseY,scrollAmount/5);
            }else if (macroScreenState==MacroScreenState.QUERYING_RESULT){
                searchResultContainer.scrollWheel(guiMouseX,guiMouseY,scrollAmount );
            }

        }
    }
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        firstOpen=true;
    }
    private void ReturnToModificationScreen(String macroName){// set macro to macroContainer
        macroScreenState=MacroScreenState.MACRO_MODIFICATION;
        macroContainer.setMacro(macroName);
        textField.setText(macroName);
        TerminalJsonConfig.getMacroData().setCurrentMacro(macroName);
    }
}