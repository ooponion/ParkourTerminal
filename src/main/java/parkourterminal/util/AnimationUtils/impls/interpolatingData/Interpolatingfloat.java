package parkourterminal.util.AnimationUtils.impls.interpolatingData;

import parkourterminal.util.AnimationUtils.intf.interpolating;

public class Interpolatingfloat implements interpolating<Interpolatingfloat>
{
    private float value;
    public Interpolatingfloat(float value){
        this.value=value;
    }
    public float getValue() {
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
}
