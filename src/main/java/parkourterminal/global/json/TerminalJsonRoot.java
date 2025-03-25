package parkourterminal.global.json;


import java.util.HashMap;

public class TerminalJsonRoot {
    private HashMap<String, LabelJson> usedLabelJsons =new HashMap<String, LabelJson>();

    public HashMap<String, LabelJson> getUsedLabelJsons() {
        return usedLabelJsons;
    }

    public void setUsedLabelJsons(HashMap<String, LabelJson> usedLabelJsons) {
        this.usedLabelJsons=usedLabelJsons;
    }
}
