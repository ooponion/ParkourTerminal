package parkourterminal.gui.layout;

import parkourterminal.util.AnimationUtils.impls.BeizerAnimation;
import parkourterminal.util.AnimationUtils.impls.ColorInterpolateAnimation;
import parkourterminal.util.AnimationUtils.impls.interpolatingData.FloatPoint;
import parkourterminal.util.AnimationUtils.impls.interpolatingData.InterpolatingColor;
import parkourterminal.util.AnimationUtils.intf.AbstractAnimation;
import parkourterminal.util.AnimationUtils.intf.AnimationMode;

public abstract class UIComponent {
    private int x, y, width, height;
    protected Margin margin = new Margin(0);
    protected Alignment alignment = Alignment.START;
    private final AbstractAnimation<FloatPoint> animation=new BeizerAnimation<FloatPoint>(0.5f,new FloatPoint(x,y), AnimationMode.BLENDED);
    private final AbstractAnimation<InterpolatingColor> animationColor=new ColorInterpolateAnimation(0.5f ,new InterpolatingColor(0),AnimationMode.BLENDED);

    public abstract void draw(int mouseX, int mouseY, float partialTicks);
    public abstract boolean isMouseOver(int mouseX, int mouseY);

    public void setAnimationTime(float time){
        animation.SetAnimationTime(time);
        animationColor.SetAnimationTime(time);
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setX(int x) {
        animation.changeWithOutAnimation(new FloatPoint(x,y));
        this.x = x;
    }

    public void setY(int y) {
        animation.changeWithOutAnimation(new FloatPoint(x,y));
        this.y = y;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public AbstractAnimation<FloatPoint> getAnimation() {
        return animation;
    }

    public AbstractAnimation<InterpolatingColor> getAnimationColor() {
        return animationColor;
    }
}