package parkourterminal.util.AnimationUtils.intf;

public interface interpolating<S,T extends interpolating<S,T>> {
    T interpolate(T p2, float progress);
    boolean equals(T p2);
    /**the value 0 if this == other; a value less than 0 if this < other; and a value greater than 0 if this > other**/
    int compare(T other);
    float distance(T other);
    T add(T other);
    T subtract(T other);
    T multiply(float multiplier);
    S getValue();
    float getSize();
}
