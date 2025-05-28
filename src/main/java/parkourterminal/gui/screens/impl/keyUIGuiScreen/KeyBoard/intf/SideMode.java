package parkourterminal.gui.screens.impl.keyUIGuiScreen.KeyBoard.intf;

public enum SideMode {
    LEFT_TOP,
    LEFT_BOTTOM,
    RIGHT_TOP,
    RIGHT_BOTTOM,
    RIGHT,
    LEFT,
    TOP,
    BOTTOM,
    NONE;
    public boolean isLeft() {
        return name().contains("LEFT");
    }

    public boolean isRight() {
        return name().contains("RIGHT");
    }

    public boolean isTop() {
        return name().contains("TOP");
    }

    public boolean isBottom() {
        return name().contains("BOTTOM");
    }
}
