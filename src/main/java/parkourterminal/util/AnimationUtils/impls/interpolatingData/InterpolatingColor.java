package parkourterminal.util.AnimationUtils.impls.interpolatingData;

import parkourterminal.util.AnimationUtils.intf.interpolating;

public class InterpolatingColor implements interpolating<InterpolatingColor>
{
    private int Color=0;
    public int getColor(){
        return Color;
    }
    public InterpolatingColor(int color){
        this.Color=color;
    }
    @Override
    public InterpolatingColor interpolate(InterpolatingColor endColor, float progress) {
        int startA = (Color >> 24) & 0xFF;
        int startR = (Color >> 16) & 0xFF;
        int startG = (Color >> 8) & 0xFF;
        int startB = Color & 0xFF;

        // 提取目标颜色的ARGB分量
        int endA = (endColor.getColor() >> 24) & 0xFF;
        int endR = (endColor.getColor() >> 16) & 0xFF;
        int endG = (endColor.getColor() >> 8) & 0xFF;
        int endB = endColor.getColor() & 0xFF;

        // 插值计算当前颜色分量
        int currentA = (int) (startA + (endA - startA) * progress);
        int currentR = (int) (startR + (endR - startR) * progress);
        int currentG = (int) (startG + (endG - startG) * progress);
        int currentB = (int) (startB + (endB - startB) * progress);
        return new InterpolatingColor((currentA << 24) | (currentR << 16) | (currentG << 8) | currentB);
    }

    @Override
    public boolean equals(InterpolatingColor p2) {
        return p2.getColor()==this.Color;
    }
}
