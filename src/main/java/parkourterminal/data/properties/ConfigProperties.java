package parkourterminal.data.properties;

public class ConfigProperties {
    private boolean animationOn = false;
    private int precision = 5;
    private String prefix="terminal";
    private boolean showLabels=true;

    public boolean isAnimationOn() {
        return animationOn;
    }

    public int getPrecision() {
        return precision;
    }

    public void setAnimationOn(boolean animationOn) {
        this.animationOn = animationOn;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public boolean isShowLabels() {
        return showLabels;
    }

    public void setShowLabels(boolean showLabels) {
        this.showLabels = showLabels;
    }
}
