package parkourterminal.gui.card;

import net.minecraft.util.ResourceLocation;
import parkourterminal.gui.component.ModCard;
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
