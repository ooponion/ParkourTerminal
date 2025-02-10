package parkourterminal.util;

public class AnimationHelper {
    public static int interpolateColor(int startColor, int endColor, float progress) {
        // 提取起始颜色的ARGB分量
        int startA = (startColor >> 24) & 0xFF;
        int startR = (startColor >> 16) & 0xFF;
        int startG = (startColor >> 8) & 0xFF;
        int startB = startColor & 0xFF;

        // 提取目标颜色的ARGB分量
        int endA = (endColor >> 24) & 0xFF;
        int endR = (endColor >> 16) & 0xFF;
        int endG = (endColor >> 8) & 0xFF;
        int endB = endColor & 0xFF;

        // 插值计算当前颜色分量
        int currentA = (int) (startA + (endA - startA) * progress);
        int currentR = (int) (startR + (endR - startR) * progress);
        int currentG = (int) (startG + (endG - startG) * progress);
        int currentB = (int) (startB + (endB - startB) * progress);

        // 合成当前颜色
        return (currentA << 24) | (currentR << 16) | (currentG << 8) | currentB;
    }
}
