package parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.impl.createKeyPage;

import net.minecraft.client.Minecraft;
import parkourterminal.gui.component.Div;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.ToolTipManager;
import parkourterminal.util.RenderTextHelper;
import parkourterminal.util.ShapeDrawer;
import parkourterminal.util.SystemOutHelper;

public class BindingMouseHereDiv extends Div {
    public BindingMouseHereDiv(int width, int height) {
        super(width, height);
    }
    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton){
        if(isMouseOver(mouseX, mouseY)){
            ToolTipManager.getInstance().getEventBus().emit("bindingEvent",mouseButton-100);
        }
        return isMouseOver(mouseX, mouseY);
    }
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        ShapeDrawer.drawRoundedRectBorder(getX(),getY(),getWidth(),getHeight(),0xFF0000FF,4);
        RenderTextHelper.drawCenteredLinkBreakString(Minecraft.getMinecraft().fontRendererObj, "Click with mouse you want to bind",getX(),getY(),getWidth(),getHeight(),0xFFFFFFFF,true);

    }
}
