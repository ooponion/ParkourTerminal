package parkourterminal.data.properties;

public class ConfigProperties {
    private boolean bbVisible =true;
    private boolean condVisible =true;
    private boolean animationOn = false;
    private int precision = 5;
    private String prefix = "terminal";
    private boolean showLabels = true;
    private boolean trimZeros=true;
    private int landBlockColor = 0xf55a44;
    private int condColor = 0xf1c008;
    private boolean toggleSprint=false;
    private boolean sendChatPb=true;
    private boolean sendChatOffset=false;
    private boolean sendHitInertia=false;


    public boolean isSendChatPb() {
        return sendChatPb;
    }
    public boolean isSendHitInertia() {
        return sendHitInertia;
    }

    public void setSendHitInertia(boolean sendHitInertia) {
        this.sendHitInertia = sendHitInertia;
    }

    public boolean isSendChatOffset() {
        return sendChatOffset;
    }

    public void setSendChatPb(boolean sendChatPb) {
        this.sendChatPb = sendChatPb;
    }
    public void setSendChatOffset(boolean sendChatOffset) {
        this.sendChatOffset = sendChatOffset;
    }

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

    public boolean isToggleSprint() {
        return toggleSprint;
    }

    public void setToggleSprint(boolean toggleSprint) {
        this.toggleSprint = toggleSprint;
    }
    public void toggleSprint() {
        this.toggleSprint = !this.toggleSprint;
    }

    public int getCondColor() {
        return condColor;
    }

    public void setCondColor(int condColor) {
        condColor=Math.max(0x0,Math.min(0xFFFFFF,condColor));
        this.condColor = condColor;
    }
    public boolean isBbVisible() {
        return bbVisible;
    }

    public void setBbVisible(boolean bbVisible) {
        this.bbVisible = bbVisible;
    }
    public void toggleBbVisible() {
        this.bbVisible = !this.bbVisible;
    }

    public boolean isCondVisible() {
        return condVisible;
    }

    public void setCondVisible(boolean condVisible) {
        this.condVisible = condVisible;
    }
    public void toggleCondVisible(){
        this.condVisible=!this.condVisible;
    }
}
