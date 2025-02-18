package parkourterminal.gui.layout;

public abstract class UIComponent {
    public int x, y, width, height;
    protected Margin margin = new Margin(0);
    protected Alignment alignment = Alignment.START;

    public abstract void draw(int mouseX, int mouseY, float partialTicks);
    public abstract boolean isMouseOver(int mouseX, int mouseY);
}