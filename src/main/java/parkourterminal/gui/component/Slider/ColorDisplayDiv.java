package parkourterminal.gui.component.Slider;

import parkourterminal.gui.component.Div;
import parkourterminal.util.RenderTextHelper;
import parkourterminal.util.ShapeDrawer;

public class ColorDisplayDiv extends Div {
    private int color;
    public ColorDisplayDiv() {
        super(60,15);
    }

    public void setColor(int color) {
        this.color = color|0xff000000;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        int min=Math.min(getWidth(),getHeight());
        ShapeDrawer.drawRoundedRectBorder(getX(),getY(),min,min, RenderTextHelper.dimColor(color,0.5),2);
        ShapeDrawer.drawRoundedRect(getX(),getY(),min,min, color,2);
    }
}