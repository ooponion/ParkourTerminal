package parkourterminal.util;

public class AngleUtils {
    public static float normalizeAngle(float angle) {
        angle = (( angle + 180) % 360 + 360) % 360 - 180;
        return angle;
    }
}
