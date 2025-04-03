package parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.intf;

public class BlipLabel {
    private Double blipY= Double.NaN;
    private int blipTimes = 0;
    public BlipLabel(Double blipY,int blipTimes){
        this.blipY=blipY;
        this.blipTimes=blipTimes;
    }
    public Double getBlipY() {
        return blipY;
    }
    public int getBlipTimes(){
        return blipTimes;
    }
}
