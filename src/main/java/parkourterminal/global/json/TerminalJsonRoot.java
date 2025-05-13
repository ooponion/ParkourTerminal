package parkourterminal.global.json;


import parkourterminal.data.ColorData.ColorData;
import parkourterminal.data.macroData.MacroData;
import parkourterminal.data.properties.ConfigProperties;

import java.util.HashMap;

public class TerminalJsonRoot {
    private HashMap<String, LabelJson> usedLabels =new HashMap<String, LabelJson>();
    private ColorData colorData =new ColorData();
    private ConfigProperties properties=new ConfigProperties();
    private MacroData macroData =new MacroData();


    public HashMap<String, LabelJson> getUsedLabels() {
        return usedLabels;
    }

    public void setUsedLabels(HashMap<String, LabelJson> usedLabels) {
        this.usedLabels=new HashMap<String, LabelJson>(usedLabels);
    }


    public ColorData getColorData() {
        return colorData;
    }
    public MacroData getMacroData(){
        return macroData;
    }

    public ConfigProperties getProperties(){return properties;}

}
