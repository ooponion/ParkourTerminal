package parkourterminal.gui.screens.impl.InGameMenuGui.Components;

import net.minecraft.util.ResourceLocation;
import parkourterminal.gui.cardIntf.ModCard;
import parkourterminal.gui.layout.Padding;
import parkourterminal.gui.screens.intf.ModDetailGui;
import parkourterminal.gui.module.TestModule;

public class TestCard extends ModCard {
    public TestCard(int x, int y, int width, int height) {
        super("Test", new ResourceLocation("parkourterminal", "textures/gui/terminal.png"), x, y, width, height);
    }

    @Override
    public ModDetailGui getModDetailGui() {
        return new TestModule();
    }
}
