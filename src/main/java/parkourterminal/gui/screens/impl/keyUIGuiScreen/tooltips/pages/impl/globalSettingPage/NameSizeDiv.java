package parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.impl.globalSettingPage;

import net.minecraft.client.Minecraft;
import parkourterminal.gui.component.Div;

public class NameSizeDiv extends Div {
    public NameSizeDiv() {
        super(60,10);
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        Minecraft.getMinecraft().fontRendererObj.drawString("NameSize:",getX(),getY(),0xFFFFFFFF,true);
    }
}
