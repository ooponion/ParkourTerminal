package parkourterminal.global.json;


import parkourterminal.data.ColorData.ColorData;

import java.util.HashMap;

public class TerminalJsonRoot {
    private HashMap<String, LabelJson> usedLabels =new HashMap<String, LabelJson>();
    private LandBlockJson landBlock=new LandBlockJson();
    private ColorData colorData =new ColorData();
    private String prefix="terminal";

    public HashMap<String, LabelJson> getUsedLabels() {
        return usedLabels;
    }

    public void setUsedLabels(HashMap<String, LabelJson> usedLabels) {
        this.usedLabels=new HashMap<String, LabelJson>(usedLabels);
    }

    public LandBlockJson getLandBlock() {
        return landBlock;
    }

    public void setLandBlock(LandBlockJson landBlock) {
        this.landBlock = landBlock;
    }

    public ColorData getColorData() {
        return colorData;
    }

    public void setColorData(ColorData colorData) {
        this.colorData = colorData;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
