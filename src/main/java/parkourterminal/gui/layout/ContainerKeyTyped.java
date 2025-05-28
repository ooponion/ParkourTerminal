package parkourterminal.gui.layout;

import parkourterminal.gui.component.scrollBar.impl.ScrollBarImpl;
import parkourterminal.gui.component.scrollBar.intf.ScrollDirection;

import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContainerKeyTyped extends Container implements KeyTyped{
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        for (UIComponent component:new ArrayList<UIComponent>(getComponents())){
            if(component.isFocused()&&component instanceof KeyTyped){
                ((KeyTyped)component).keyTyped(typedChar,keyCode);
            }
        }
    }
}