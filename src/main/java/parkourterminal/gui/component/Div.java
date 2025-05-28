package parkourterminal.gui.component;

import parkourterminal.gui.layout.UIComponent;

public abstract class Div extends UIComponent {
    public Div(int width, int height) {
        setSize(width, height);
    }
    public Div(int x, int y, int width, int height) {
        this(width, height);
        setPosition(x,y);
    }
}
