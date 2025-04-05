package parkourterminal.data.properties;

public class ConfigProperties {
    private boolean animationOn = false;
    private int precision = 5;
    private String prefix = "terminal";
    private boolean showLabels = true;
    private boolean trimZeros=true;
    private int landBlockColor = 0xf55a44;

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

    public int getLandBlockColor() {
        return landBlockColor;
    }

    public void setLandBlockColor(int landBlockColor) {
        landBlockColor=Math.max(0x0,Math.min(0xFFFFFF,landBlockColor));
        this.landBlockColor = landBlockColor;
    }

    public boolean isTrimZeros() {
        return trimZeros;
    }

    public void setTrimZeros(boolean trimZeros) {
        this.trimZeros = trimZeros;
    }
}
