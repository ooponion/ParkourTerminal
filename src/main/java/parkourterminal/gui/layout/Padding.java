package parkourterminal.gui.layout;

public class Padding {
    public int left;
    public int right;
    public int top;
    public int bottom;

    public Padding(int padding) {
        this.left = padding;
        this.right = padding;
        this.top = padding;
        this.bottom = padding;
    }

    public Padding(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }
    public int vertical() {
        return top + bottom;
    }
    public int horizontal() {
        return left + right;
    }
}
