package parkourterminal.gui;

import net.minecraft.client.gui.GuiScreen;
import parkourterminal.util.BlurRenderer;
import parkourterminal.util.ShapeDrawer;

public class TestGui extends GuiScreen {
    @Override
    public void initGui() {
        BlurRenderer.initSharedBlurShader(10.0f);

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        BlurRenderer.drawBlurredRoundedRect(0, 100, 50, 100, 0x96B4C8FF, 5, 10.0f, partialTicks);
        BlurRenderer.drawBlurredRoundedRect(200, 100, 50, 100, 0x96B4C8FF, 5, 10.0f, partialTicks);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void onGuiClosed() {
        BlurRenderer.cleanupBlurResources();

        super.onGuiClosed();
    }
}