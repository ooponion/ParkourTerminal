package parkourterminal.util.AnimationUtils.intf;

public interface interpolating<T extends interpolating<T>> {
    T interpolate(T p2, float progress);
    boolean equals(T p2);
}
