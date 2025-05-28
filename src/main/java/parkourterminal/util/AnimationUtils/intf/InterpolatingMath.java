package parkourterminal.util.AnimationUtils.intf;

import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import parkourterminal.util.AnimationUtils.impls.interpolatingData.Interpolatingfloat;

public class InterpolatingMath {
    public static <T extends interpolating<?,T>> T clamp(T value, T min, T max) {
        if(min.compare(max)>0){
            T temp=min;
            min=max;
            max=temp;
        }
        T result= value;
        if(value.compare(min)<0){
            result=min;
        }
        if(value.compare(max)>0){
            result=max;
        }
        return result;
    }
    public static <T extends interpolating<?,T>> float getProgress(T min, T max,T value) {
        float holistic=min.distance(max);
        if(holistic==0){
            return 0;
        }
        return min.distance(value)/holistic;
    }
    public static <T extends interpolating<?,T>> float getRoundedProgress(T min, T max,float progress,T step) {
        float holistic=min.distance(max);
        if(holistic==0){
            return 0;
        }
        float stepDistance=step.getSize();
        float progressDistance=holistic*progress;
        float stepNumber=Math.round(progressDistance/stepDistance);
        return MathHelper.clamp_float(stepNumber*stepDistance/holistic,0,1);
    }
    public static <T extends interpolating<?,T>> T getValue(T min, T max,float progress) {
        T vec=max.subtract(min);
        return min.add(vec.multiply(progress));
    }
}
