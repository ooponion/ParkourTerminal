package parkourterminal.util.AnimationUtils.impls;

import parkourterminal.util.AnimationUtils.impls.interpolatingData.FloatPoint;
import parkourterminal.util.AnimationUtils.impls.interpolatingData.InterpolatingColor;
import parkourterminal.util.AnimationUtils.intf.AbstractAnimation;
import parkourterminal.util.AnimationUtils.intf.AnimationMode;

public class ColorInterpolateAnimation extends AbstractAnimation<InterpolatingColor> {
    public ColorInterpolateAnimation(float totalTime, InterpolatingColor startColor, AnimationMode animationMode) {
        super(totalTime, startColor, animationMode);
    }

    @Override
    public float ProgressFunction(float t) {
        return t;
    }
}
