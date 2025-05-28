package parkourterminal.util.AnimationUtils.impls.interpolatingData;

import parkourterminal.util.AnimationUtils.intf.interpolating;

public class Interpolatingfloat implements interpolating<Float,Interpolatingfloat>
{
    private float value;
    public Interpolatingfloat(float value){
        this.value=value;
    }
    public Float getValue() {
        return value;
    }

    @Override
    public float getSize() {
        return value;
    }

    @Override
    public Interpolatingfloat interpolate(Interpolatingfloat p2, float progress) {
        return new Interpolatingfloat(this.value+(p2.getValue()-this.value)*progress);
    }

    @Override
    public boolean equals(Interpolatingfloat p2) {
        return this.value==p2.getValue();
    }

    @Override
    public int compare(Interpolatingfloat other) {
        return Float.compare(this.getValue(), other.getValue());
    }

    @Override
    public float distance(Interpolatingfloat p2) {
        return Math.abs(this.getValue()-p2.getValue());
    }

    @Override
    public Interpolatingfloat add(Interpolatingfloat other) {
        return new  Interpolatingfloat(this.getValue()+other.getValue());
    }

    @Override
    public Interpolatingfloat subtract(Interpolatingfloat other) {
        return new  Interpolatingfloat(this.getValue()-other.getValue());
    }

    @Override
    public Interpolatingfloat multiply(float multiplier) {
        return new  Interpolatingfloat(this.getValue()*multiplier);
    }

}
