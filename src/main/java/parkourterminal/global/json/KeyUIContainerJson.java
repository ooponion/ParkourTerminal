package parkourterminal.global.json;

import java.util.HashMap;

public class KeyUIContainerJson {
    private HashMap<String, KeyUIJson> usedKeyUIs =new HashMap<String, KeyUIJson>();
    private float keyFontSize=1;
    private float nameFontSize=1;
    public HashMap<String,KeyUIJson> getUsedKeyUIs() {
        return usedKeyUIs;
    }
    public void setUsedKeyUIs(HashMap<String, KeyUIJson> usedKeyUIs) {
        this.usedKeyUIs=new HashMap<String, KeyUIJson>(usedKeyUIs);
    }
    public float getKeyFontSize() {
        return keyFontSize;
    }
    public void setKeyFontSize(float keyFontSize) {
        this.keyFontSize=keyFontSize;
    }
    public float getNameFontSize() {
        return nameFontSize;
    }
    public void setNameFontSize(float nameFontSize) {
        this.nameFontSize=nameFontSize;
    }
}
