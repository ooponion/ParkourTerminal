package parkourterminal.global.json;


import java.util.HashMap;

public class TerminalJsonRoot {
    private HashMap<String, LabelJson> usedLabels =new HashMap<String, LabelJson>();
    private LandBlockJson landBlock=new LandBlockJson();

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
}
