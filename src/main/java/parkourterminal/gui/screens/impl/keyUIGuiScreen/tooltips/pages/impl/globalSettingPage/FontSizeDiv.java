package parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.impl.globalSettingPage;

import net.minecraft.client.Minecraft;
import parkourterminal.gui.component.Div;
import parkourterminal.util.RenderTextHelper;

public class FontSizeDiv extends Div {
    public FontSizeDiv() {
        super(60,10);
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        Minecraft.getMinecraft().fontRendererObj.drawString("FontSize:",getX(),getY(),0xFFFFFFFF,true);
    }
}
