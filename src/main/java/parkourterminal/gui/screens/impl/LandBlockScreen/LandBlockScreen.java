package parkourterminal.gui.screens.impl.LandBlockScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;
import parkourterminal.data.GlobalData;
import parkourterminal.global.json.TerminalJsonConfig;
import parkourterminal.gui.component.ConsolaFontRenderer;
import parkourterminal.gui.component.CustomButton;
import parkourterminal.gui.screens.intf.BlurGui;
import parkourterminal.gui.screens.intf.instantiationScreen.intf.InstantiationScreen;
import parkourterminal.gui.screens.intf.instantiationScreen.intf.ScreenID;
import parkourterminal.gui.screens.intf.instantiationScreen.manager.ScreenManager;
import parkourterminal.util.renderhelper.LandBlockRendererHelper;

import javax.xml.crypto.Data;
import java.io.IOException;

public class LandBlockScreen extends BlurGui implements InstantiationScreen {
    private int buttonWidth =70;
    private int buttonHeight =20;
    private int spacing =5;
    private int normalColor = 0x40000000;
    private int hoverColor  = 0x600099FF;
    private CustomButton LandingModeButton =new CustomButton(0,0,buttonWidth,buttonHeight,normalColor,hoverColor,4,"Landing Mode");
    private CustomButton AxisButton =new CustomButton(0,0,buttonWidth,buttonHeight,normalColor,hoverColor,4,"Axis");
    private CustomButton BBVisibleButton =new CustomButton(0,0,buttonWidth,buttonHeight,normalColor,hoverColor,4,"BB visible");
    private CustomButton CondVisibleButton =new CustomButton(0,0,buttonWidth,buttonHeight,normalColor,hoverColor,4,"Cond visible");
    private CustomButton CalculateWallButton =new CustomButton(0,0,buttonWidth,buttonHeight,normalColor,hoverColor,4,"Calculate Walls");

    private CustomButton ResetWallButton =new CustomButton(0,0,buttonWidth,buttonHeight,normalColor,hoverColor,4,"Reset Walls");

    private CustomButton CloseButton =new CustomButton(0,0,buttonWidth,buttonHeight,normalColor,hoverColor,4,"Close");


    @Override
    public void initGui() {
        super.initGui();
        fontRendererObj = new ConsolaFontRenderer(Minecraft.getMinecraft());
        LandingModeButton.setPosition(this.width-buttonWidth-spacing,spacing);
        AxisButton.setPosition(this.width-buttonWidth-spacing,spacing*2+buttonHeight);
        BBVisibleButton.setPosition(this.width-buttonWidth-spacing,spacing*4+buttonHeight*2);
        CondVisibleButton.setPosition(this.width-buttonWidth-spacing,spacing*5+buttonHeight*3);
        CalculateWallButton.setPosition(this.width-buttonWidth-spacing,spacing*7+buttonHeight*4);
        ResetWallButton.setPosition(this.width-buttonWidth-spacing,spacing*8+buttonHeight*5);
        CloseButton.setPosition((this.width-buttonWidth)/2,this.height-buttonHeight-spacing);

    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        LandingModeButton.setText("Landing Mode: "+GlobalData.getLandingBlock().getlBmod().getDisplayName());
        AxisButton.setText("Axis: "+GlobalData.getLandingBlock().getlBaxis().getDisplayName());
        BBVisibleButton.setText("BB visible: "+ TerminalJsonConfig.getProperties().isBbVisible());
        CondVisibleButton.setText("Cond visible: "+ TerminalJsonConfig.getProperties().isCondVisible());


        LandingModeButton.draw(mouseX, mouseY, partialTicks);
        AxisButton.draw(mouseX, mouseY, partialTicks);
        BBVisibleButton.draw(mouseX, mouseY, partialTicks);
        CondVisibleButton.draw(mouseX, mouseY, partialTicks);
        CalculateWallButton.draw(mouseX, mouseY, partialTicks);
        ResetWallButton.draw(mouseX, mouseY, partialTicks);
        CloseButton.draw(mouseX, mouseY, partialTicks);
    }
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        int dWheel = Mouse.getDWheel(); // 获取鼠标滚轮滚动值
        if (dWheel != 0) {

        }
    }
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if(LandingModeButton.mouseClicked(mouseX, mouseY, mouseButton)){
            GlobalData.getLandingBlock().toggleLbmod();
        }else if(AxisButton.mouseClicked(mouseX, mouseY, mouseButton)){
            GlobalData.getLandingBlock().toggleLbAxis();
        }else if(BBVisibleButton.mouseClicked(mouseX, mouseY, mouseButton)){
            TerminalJsonConfig.getProperties().toggleBbVisible();
        }else if(CondVisibleButton.mouseClicked(mouseX, mouseY, mouseButton)){
            TerminalJsonConfig.getProperties().toggleCondVisible();
        }else if(CalculateWallButton.mouseClicked(mouseX, mouseY, mouseButton)){
            GlobalData.getLandingBlock().getWholeCollisionBox().setClipAgainstWall(true);
            GlobalData.getLandingBlock().getWholeCollisionBox().subtractWalls(Minecraft.getMinecraft().theWorld);
        }else if(ResetWallButton.mouseClicked(mouseX, mouseY, mouseButton)){
            GlobalData.getLandingBlock().getWholeCollisionBox().setClipAgainstWall(false);
        }else if(CloseButton.mouseClicked(mouseX, mouseY, mouseButton)){
            ScreenManager.SwitchToGame();
        }
    }
    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public GuiScreen getScreenInstantiation() {
        return this;
    }

    @Override
    public ScreenID getScreenID() {
        return new ScreenID("LandBlockScreen");
    }
}
