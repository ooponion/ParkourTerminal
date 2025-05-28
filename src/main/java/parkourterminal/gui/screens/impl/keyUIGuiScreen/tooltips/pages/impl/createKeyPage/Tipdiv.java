package parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.impl.createKeyPage;

import net.minecraft.client.Minecraft;
import parkourterminal.gui.component.Div;
import parkourterminal.util.RenderTextHelper;

public class Tipdiv extends Div {
    public Tipdiv(int width, int height) {
        super(width, height);
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        RenderTextHelper.drawCenteredLinkBreakString(Minecraft.getMinecraft().fontRendererObj, "Press key to bind",getX(),getY(),getWidth(),getHeight(),0xFFFFFFFF,true);

    }
}
