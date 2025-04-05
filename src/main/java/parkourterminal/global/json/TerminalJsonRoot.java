package parkourterminal.global.json;


import parkourterminal.data.ColorData.ColorData;
import parkourterminal.data.properties.ConfigProperties;

import java.util.HashMap;

public class TerminalJsonRoot {
    private HashMap<String, LabelJson> usedLabels =new HashMap<String, LabelJson>();
    private LandBlockJson landBlock=new LandBlockJson();
    private ColorData colorData =new ColorData();
    private ConfigProperties properties=new ConfigProperties();



    public HashMap<String, LabelJson> getUsedLabels() {
        return usedLabels;
    }

    public void setUsedLabels(HashMap<String, LabelJson> usedLabels) {
        this.usedLabels=new HashMap<String, LabelJson>(usedLabels);
    }

    public LandBlockJson getLandBlock() {
        return landBlock;
    }

    public ColorData getColorData() {
        return colorData;
    }

    public ConfigProperties getProperties(){return properties;}

}
