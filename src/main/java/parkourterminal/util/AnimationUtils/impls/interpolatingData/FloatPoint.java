package parkourterminal.util.AnimationUtils.impls.interpolatingData;

import parkourterminal.util.AnimationUtils.intf.interpolating;

public class FloatPoint implements interpolating<FloatPoint> {
    private float x,y;
    public FloatPoint(float x,float y){
        this.x=x;
        this.y=y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    @Override
    public FloatPoint interpolate(FloatPoint p2, float progress) {
        float newX = x + (p2.getX() - x) * progress;
        float newY =  y + (p2.getY() - y) * progress;
        return new FloatPoint(newX,newY);
    }

    @Override
    public boolean equals(FloatPoint p2) {
        return  x==p2.getX()&&y==p2.getY();
    }
}
