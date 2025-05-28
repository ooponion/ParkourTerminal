package parkourterminal.gui.layout;

import java.io.IOException;
import java.util.ArrayList;

public class ContainerOverlapped extends Container{
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