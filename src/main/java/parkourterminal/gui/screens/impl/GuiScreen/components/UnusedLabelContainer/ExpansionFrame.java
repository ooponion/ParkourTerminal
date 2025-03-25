package parkourterminal.gui.screens.impl.GuiScreen.components.UnusedLabelContainer;

import net.minecraft.client.Minecraft;
import parkourterminal.gui.component.CustomButton;

public class ExpansionFrame extends CustomButton {
    private final UnusedLabelContainer container;
    public ExpansionFrame( int width, int height, int normalColor, int hoverColor, int cornerRadius, UnusedLabelContainer unusedLabelContainer) {
        super(0,0, width, height, normalColor, hoverColor, cornerRadius, "<");
        this.container=unusedLabelContainer;
    }
    @Override
    public void Update(){
        if(container.isEnabled()){
            this.setText(">");
            setPosition(container.getX()-getWidth(),container.getY());
        }else{
            this.setText("<");
            setPosition(container.getX()+container.getWidth()-getWidth(),container.getY());
        }

    };
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        super.drawButton(Minecraft.getMinecraft().fontRendererObj,mouseX,mouseY);
    }
    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton){
        if(!super.isMouseOver(mouseX, mouseY)){
            return false;
        }
        container.setEnabled(!container.isEnabled());
        Update();
        return true;
    }
}
