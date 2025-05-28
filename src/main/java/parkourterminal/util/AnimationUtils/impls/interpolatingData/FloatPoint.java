package parkourterminal.util.AnimationUtils.impls.interpolatingData;

import parkourterminal.util.AnimationUtils.intf.interpolating;

public class FloatPoint implements interpolating<FloatPoint,FloatPoint> {
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

    @Override
    public int compare(FloatPoint other) {
        int compare = Float.compare(this.getX(), other.getX());
        if(compare==0){
            compare = Float.compare(this.getY(), other.getY());
        }
        return compare;
    }

    @Override
    public float distance(FloatPoint p2) {
        return (float)Math.hypot(this.x-p2.x,this.y-p2.y);
    }

    @Override
    public FloatPoint add(FloatPoint other) {
        return new FloatPoint(this.getX()+other.getX(),this.getY()+other.getY());
    }

    @Override
    public FloatPoint subtract(FloatPoint other) {
        return new FloatPoint(this.getX()-other.getX(),this.getY()-other.getY());
    }

    @Override
    public FloatPoint multiply(float multiplier) {
        return new FloatPoint(this.getX()*multiplier,this.getY()*multiplier);
    }

    @Override
    public FloatPoint getValue() {
        return this;
    }

    @Override
    public float getSize() {
        return (float) Math.hypot(x,y);
    }
}
