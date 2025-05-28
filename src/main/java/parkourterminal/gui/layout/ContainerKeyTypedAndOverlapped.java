package parkourterminal.gui.layout;

import java.io.IOException;

public class ContainerKeyTypedAndOverlapped  extends Container implements KeyTyped{
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        for (UIComponent component:getComponents()){
            if(component.isFocused()&&component instanceof KeyTyped){
                ((KeyTyped)component).keyTyped(typedChar,keyCode);
            }
        }
    }
    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        boolean ret=super.mouseClicked(mouseX, mouseY, mouseButton);
        UIComponent component=getFocusedUI();
        if(component!=null){
            components.remove(component);
            components.add(0, component);
        }
        return ret;
    }
}