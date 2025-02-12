package parkourterminal.gui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import parkourterminal.gui.IngameMenuGui;
import parkourterminal.util.ShapeDrawer;

import java.io.IOException;

public class ModDetailGui extends GuiScreen {
    private String modName;

    // “返回”按钮参数
    private int backButtonX = 20;
    private int backButtonY = 20;
    private int backButtonWidth = 60;
    private int backButtonHeight = 20;
    // 配色：按钮背景为半透明暗灰色，边框为浅灰色
    private int buttonColor = 0xCC555555;
    private int buttonBorderColor = 0xFF777777;

    public ModDetailGui(String modName) {
        this.modName = modName;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // 绘制详细设置面板背景（位于屏幕中央留有边距）
        int panelX = 50;
        int panelY = 50;
        int panelWidth = this.width - 100;
        int panelHeight = this.height - 100;
        int panelBackgroundColor = 0xCC222222;
        int panelBorderColor = 0xFF444444;
        int panelCornerRadius = 15;

        ShapeDrawer.drawRoundedRect(panelX, panelY, panelWidth, panelHeight, panelBackgroundColor, panelCornerRadius);
        ShapeDrawer.drawRoundedRectBorder(panelX, panelY, panelWidth, panelHeight, panelBorderColor, panelCornerRadius);

        // 绘制 Mod 标题（居中显示）
        int titleColor = 0xFFFFFFFF;
        int titleWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(modName);
        int titleX = panelX + (panelWidth - titleWidth) / 2;
        int titleY = panelY + 15;
        Minecraft.getMinecraft().fontRendererObj.drawString(modName, titleX, titleY, titleColor);

        // 绘制“返回”按钮
        ShapeDrawer.drawRoundedRect(backButtonX, backButtonY, backButtonWidth, backButtonHeight, buttonColor, 5);
        ShapeDrawer.drawRoundedRectBorder(backButtonX, backButtonY, backButtonWidth, backButtonHeight, buttonBorderColor, 5);
        String backText = "返回";
        int backTextWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(backText);
        int backTextX = backButtonX + (backButtonWidth - backTextWidth) / 2;
        int backTextY = backButtonY + (backButtonHeight - 8) / 2;
        Minecraft.getMinecraft().fontRendererObj.drawString(backText, backTextX, backTextY, titleColor);

        // 此处你可以继续绘制其他自定义设置组件（如按钮、滑条、文本框等）
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        // 检测是否点击“返回”按钮
        if (mouseX >= backButtonX && mouseX <= backButtonX + backButtonWidth
                && mouseY >= backButtonY && mouseY <= backButtonY + backButtonHeight) {
            if (Minecraft.getMinecraft().currentScreen instanceof IngameMenuGui) {
                ((IngameMenuGui) Minecraft.getMinecraft().currentScreen).returnToMainMenu();
            }
            return;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }
}
