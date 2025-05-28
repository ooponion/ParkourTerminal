package parkourterminal.gui.screens.impl.keyUIGuiScreen.KeyBoard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import parkourterminal.global.json.KeyUIJson;
import parkourterminal.global.json.TerminalJsonConfig;
import parkourterminal.gui.component.fontRenderer.DDFontRenderer;
import parkourterminal.gui.layout.UIComponent;

import java.util.*;

public class KeyUIManager {
    private static final KeyUIManager instance=new KeyUIManager();
    private final KeyboardUIContainer container =new KeyboardUIContainer();
    private final HashMap<Integer,KeyUI> keyUIHashMap=new HashMap<Integer,KeyUI>();
    private HashMap<Integer,String> actionNames=new HashMap<Integer,String>();
    private DDFontRenderer keyRenderer;
    private DDFontRenderer nameRenderer;

    public KeyUIManager(){

    }
    public static KeyUIManager getInstance(){
        return instance;
    }
    public KeyboardUIContainer getContainer() {
        return container;
    }
    public DDFontRenderer getKeyRenderer() {
        if(keyRenderer==null){
            keyRenderer=DDFontRenderer.newInstance();
        }
        return keyRenderer;
    }
    public DDFontRenderer getNameRenderer() {
        if(nameRenderer==null){
            nameRenderer=DDFontRenderer.newInstance();
        }
        return nameRenderer;
    }
    public String getAbbreviationName(KeyBinding keyBinding){
        if (actionNames .isEmpty()) {
            actionNames=createDefaultNames();
        }
        return actionNames.getOrDefault(keyBinding.getKeyCode(),"");
    }
    private boolean hasRepeatName(final KeyBinding keyBinding,final String name){
        for(Map.Entry<Integer,String> set:actionNames.entrySet()){
            if(set.getKey()==keyBinding.getKeyCode())continue;
            if(set.getValue().equals(name)){
                return true;
            }
        }
        return false;
    }
    public boolean putName(KeyBinding keyBinding, String name){
        if(hasRepeatName(keyBinding,name)){
            return false;
        }
        actionNames.put(keyBinding.getKeyCode(),name);
        return true;
    }
    public static KeyBinding getModKeyBindingByName(String keyName) {
        for (KeyBinding key : Minecraft.getMinecraft().gameSettings.keyBindings) {
            String displayName = GameSettings.getKeyDisplayString(key.getKeyCode());
            if (displayName.equalsIgnoreCase(keyName)) {
                return key;
            }
        }
        return null;
    }
    public void removeKeyUI(KeyBinding keyBinding){
        container.deleteComponent(keyUIHashMap.get(keyBinding.getKeyCode()));
        actionNames.remove(keyBinding.getKeyCode());
        keyUIHashMap.remove(keyBinding.getKeyCode());
    }
    public void removeKeyUI(KeyUI keyUI){
        removeKeyUI(keyUI.getKeyBinding());
    }
    public KeyUI getKeyUI(KeyBinding keyBinding){
        return keyUIHashMap.get(keyBinding.getKeyCode());
    }
    public void addKeyUI(KeyBinding keyBinding, int x, int y){
        addKeyUI(keyBinding, x, y, KeyUI.DEFAULT_SIZE, KeyUI.DEFAULT_SIZE);
    }
    public boolean addKeyUI(KeyUI keyUI){
        if(keyUIHashMap.containsKey(keyUI.getKeyBinding().getKeyCode())){
            return false;
        }
        keyUIHashMap.put(keyUI.getKeyBinding().getKeyCode(),keyUI);
        actionNames.put(keyUI.getKeyBinding().getKeyCode(),keyUI.getActionName());
        container.addComponent(keyUI);
        return true;
    }
    public boolean addKeyUI(KeyBinding keyBinding,int x,int y,int width,int height){
        if(keyUIHashMap.containsKey(keyBinding.getKeyCode())){
            return false;
        }
        KeyUI keyUI=new KeyUI(keyBinding,x,y,width,height);
        keyUIHashMap.put(keyBinding.getKeyCode(),keyUI);
        actionNames.put(keyBinding.getKeyCode(),keyUI.getActionName());
        container.addComponent(keyUI);
        return true;
    }
    private HashMap<Integer,String> createDefaultNames(){
        //Fw,Bw,Lf,Rt,Jp,Sp,Sk
        HashMap<Integer,String> defaultNames=new HashMap<Integer,String>();
        GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;
        defaultNames.put(gameSettings.keyBindBack.getKeyCode(),"Bw");
        defaultNames.put(gameSettings.keyBindForward.getKeyCode(),"Fw");
        defaultNames.put(gameSettings.keyBindLeft.getKeyCode(),"Lf");
        defaultNames.put(gameSettings.keyBindRight.getKeyCode(),"Rt");
        defaultNames.put(gameSettings.keyBindSneak.getKeyCode(),"Sk");
        defaultNames.put(gameSettings.keyBindSprint.getKeyCode(),"Sp");
        defaultNames.put(gameSettings.keyBindJump.getKeyCode(),"Jp");
        return defaultNames;
    }
    public KeyUI getRightClickedKeyUI(){
        return KeyUIManager.getInstance().getContainer().getRightClickedKeyUI();
    };
    public KeyUI topKeyUIUnderMouse(){
        return KeyUIManager.getInstance().getContainer().topKeyUIUnderMouse();
    }
    public static void saveConfigUsedKeyUIs(){
        HashMap<String, KeyUIJson> usedKeyUI = new HashMap<String, KeyUIJson>();
        for (UIComponent uiComponent : KeyUIManager.getInstance().getContainer().getComponents()) {
            KeyUI keyUI=(KeyUI) uiComponent;
            String keyName = GameSettings.getKeyDisplayString(keyUI.getKeyBinding().getKeyCode()).toLowerCase();
            KeyUIJson keyUIJson=new KeyUIJson(keyUI.getActionName(),keyName,keyUI.getX(),keyUI.getY(),keyUI.getWidth(),keyUI.getHeight(),keyUI.isEnabled(),keyUI.getBackgroundColor(),keyUI.getKeyColor(),keyUI.getNameColor());
            usedKeyUI.put(keyUI.getKeyName(),keyUIJson);
        }
        TerminalJsonConfig.setKeyUIList(usedKeyUI);
        TerminalJsonConfig.setNameFontSize(KeyUIManager.getInstance().getNameRenderer().getFontScale());
        TerminalJsonConfig.setKeyFontSize(KeyUIManager.getInstance().getKeyRenderer().getFontScale());
    };
    public static void initContainer(){
        KeyUIManager.getInstance().getNameRenderer().setFontScale(TerminalJsonConfig.getNameFontSize());
        KeyUIManager.getInstance().getKeyRenderer().setFontScale(TerminalJsonConfig.getKeyFontSize());
        for (KeyUIJson uiJson:TerminalJsonConfig.getKeyUIJsons().values()) {
            KeyBinding keyBinding=getModKeyBindingByName(uiJson.getKeyName());
            KeyUI ui=new KeyUI(keyBinding,uiJson.getX(),uiJson.getY(),uiJson.getWidth(),uiJson.getHeight());
            ui.setEnabled(uiJson.isEnabled());
            ui.setBackgroundColor(uiJson.getBackgroundColor());
            ui.setKeyColor(uiJson.getKeyColor());
            ui.setNameColor(uiJson.getNameColor());
            getInstance().addKeyUI(ui);
        }
    };
}
