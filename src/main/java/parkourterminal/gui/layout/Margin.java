package parkourterminal.gui.layout;

public class Margin {
    public int left;
    public int right;
    public int top;
    public int bottom;

    public Margin(int margin) {
        this.left = margin;
        this.right = margin;
        this.top = margin;
        this.bottom = margin;
    }

    public Margin(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }
}