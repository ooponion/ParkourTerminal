package parkourterminal.global.json;

import net.minecraft.client.settings.KeyBinding;

public class KeyUIJson {
    private String keyName="";
    private String actionName="";
    private int x=0;
    private int y=0;
    private int width=0;
    private int height=0;
    private boolean enabled=true;
    private int backgroundColor =0x66afafaf;
    private int keyColor =0xFFFFFFFF;
    private int nameColor=0xFFFFFFFF;
    public  KeyUIJson(String actionName,String keyName, int x, int y, int width, int height, boolean enabled,int  backgroundColor,int keyColor,int nameColor) {
        this.actionName=actionName;
        this.keyName=keyName;
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
        this.enabled=enabled;
        this.backgroundColor=backgroundColor;
        this.keyColor=keyColor;
        this.nameColor=nameColor;
    }
    public KeyUIJson() {

    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public String getKeyName() {
        return keyName;
    }
    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public String getActionName() {
        return actionName;
    }
    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
    public int getBackgroundColor() {
        return backgroundColor;
    }
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    public int getKeyColor() {
        return keyColor;
    }
    public void setKeyColor(int keyColor) {
        this.keyColor = keyColor;
    }
    public int getNameColor() {
        return nameColor;
    }
    public void setNameColor(int nameColor) {
        this.nameColor = nameColor;
    }
}
