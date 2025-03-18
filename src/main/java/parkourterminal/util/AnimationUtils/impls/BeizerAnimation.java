package parkourterminal.util.AnimationUtils.impls;

import parkourterminal.util.AnimationUtils.intf.AnimationMode;
import parkourterminal.util.AnimationUtils.intf.AbstractAnimation;
import parkourterminal.util.AnimationUtils.intf.interpolating;

public class BeizerAnimation<T extends interpolating<T>> extends AbstractAnimation<T> {
    // 三阶贝塞尔曲线控制点
    final float P0X = 0, P0Y = 0;
    final float P3X = 1, P3Y = 1;
    float P1X , P1Y;
    float P2X , P2Y;


    // 计算贝塞尔曲线的值
    public float bezier(float p, float p0, float p1, float p2, float p3) {
        return (float) (Math.pow(1 - p, 3) * p0
                        + 3f * Math.pow(1 - p, 2) * p * p1
                        + 3f * (1 - p) * Math.pow(p, 2) * p2
                        + Math.pow(p, 3) * p3);
    }

    // 通过二分法找到 t，使得 B_x(p) ≈ x
    public float findT(float x, float epsilon) {
        float low = 0, high = 1, mid;
        while (high - low > epsilon) {
            mid = (low + high) / 2;
            if (bezier(mid, P0X, P1X, P2X, P3X) < x) {
                low = mid;
            } else {
                high = mid;
            }
        }
        return (low + high) / 2;
    }

    public BeizerAnimation(float totalTime, T startPoint, AnimationMode animationMode) {
        super(totalTime, startPoint, animationMode);
        P1X=0.00f; P1Y=0.36f; P2X=0.17f; P2Y=1.0f;
    }
    public BeizerAnimation(float totalTime, T startPoint, AnimationMode animationMode,float P1X, float P1Y, float P2X, float P2Y) {
        super(totalTime, startPoint, animationMode);
        SetBeizerPoints(P1X, P1Y, P2X, P2Y);
    }
    public void SetBeizerPoints(float P1X, float P1Y, float P2X, float P2Y){
        this.P1X=P1X;
        this.P1Y=P1Y;
        this.P2X=P2X;
        this.P2Y=P2Y;
    }

    @Override
    public float ProgressFunction(float t) {
        float p = findT(t, 1e-2f); // 找到 p
        return bezier(p, P0Y, P1Y, P2Y, P3Y); // 计算 y
    }
}
