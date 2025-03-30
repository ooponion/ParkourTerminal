package parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.manager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import parkourterminal.data.globalData.GlobalData;
import parkourterminal.global.json.LabelJson;
import parkourterminal.global.json.TerminalJsonConfig;
import parkourterminal.gui.screens.impl.GuiScreen.TerminalGuiScreen;
import parkourterminal.gui.screens.impl.GuiScreen.components.Label;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.impl.LabelValueDegree;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.impl.LabelValueDouble;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.intf.LabelValue;
import parkourterminal.gui.screens.intf.instantiationScreen.intf.ScreenID;
import parkourterminal.gui.screens.intf.instantiationScreen.manager.ScreenManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LabelManager {
    private static final HashMap<String,Label> defaultLabelList=new HashMap<String,Label>();
    public static void InitLabels() {
        addLabel("X",new LabelValueDouble());
        addLabel("Y",new LabelValueDouble());
        addLabel("Z",new LabelValueDouble());
        addLabel("F",new LabelValueDegree());
        addLabel("Pitch",new LabelValueDegree());

        //Offset Part
        addLabel("X Offset",new LabelValueDouble());
        addLabel("Z Offset",new LabelValueDouble());
        addLabel("Total Offset",new LabelValueDouble());
        addLabel("X PB",new LabelValueDouble());
        addLabel("Z PB",new LabelValueDouble());
        addLabel("PB",new LabelValueDouble());
    }
    public static HashMap<String,Label> getDefaultLabelList(){
        return  defaultLabelList;
    }
    private static void addLabel(String name, LabelValue labelValue){
        Label label=new Label(name,labelValue);
        defaultLabelList.put(label.getLabel(),label);
    }
    public static void UpdateLabelValuesPerTick(){
        EntityPlayerSP player =Minecraft.getMinecraft().thePlayer;
        UpdateLabel("X",player.posX);
        UpdateLabel("Y",player.posY);
        UpdateLabel("Z",player.posZ);
        UpdateLabel("F",player.rotationYaw);
        UpdateLabel("Pitch",player.rotationPitch);

        //Offset Part
        Double[] offsets = GlobalData.getLandingBlock().getOffsets();
        UpdateLabel("X Offset",offsets[0]);
        UpdateLabel("Z Offset",offsets[1]);
        UpdateLabel("Total Offset",offsets[2]);

        //PB Part
        Double[] pb =GlobalData.getLandingBlock().getPb();
        UpdateLabel("X PB",pb[0]);
        UpdateLabel("Z PB",pb[1]);
        UpdateLabel("PB",pb[2]);
    }
    public static <T> void UpdateLabel(String name,T value){
        if(defaultLabelList.containsKey(name)){
            defaultLabelList.get(name).getValue().Update(value);
        }
    };

    public static void saveConfigUsedLabels() {
        TerminalGuiScreen guiScreen=(TerminalGuiScreen)ScreenManager.getGuiScreen(new ScreenID("TerminalGuiScreen"));
        if(guiScreen==null){
            return;
        }
        HashMap<String, LabelJson> usedLabel = new HashMap<String, LabelJson>();
        for (Label label : guiScreen.getUsedLabels()) {
            LabelJson labelJson = new LabelJson();
            labelJson.setX(label.getX());
            labelJson.setY(label.getY());
            labelJson.setEnabled(label.isEnabled());
            usedLabel.put(label.getLabel(), labelJson);
        }
        TerminalJsonConfig.setLabelList(usedLabel);
    }
    public static void TerminalGuiInitContainers(){
        TerminalGuiScreen guiScreen=(TerminalGuiScreen) ScreenManager.getGuiScreen(new ScreenID("TerminalGuiScreen"));
        if(guiScreen!=null){
            guiScreen.InitContainers(initUsedLabelsFromJson(),initUnusedLabelsFromJson());
        }
    }
    private static List<Label> initUsedLabelsFromJson(){
        List<Label> usedLabelList=new ArrayList<Label>();
        for(Map.Entry<String, LabelJson> entry : TerminalJsonConfig.getUsedLabelJsons().entrySet()) {
            if(defaultLabelList.containsKey(entry.getKey())){
                String name=entry.getKey();
                LabelJson labelJson =entry.getValue();
                defaultLabelList.get(name).setPosition(labelJson.getX(), labelJson.getY());
                defaultLabelList.get(name).setEnabled(labelJson.isEnabled());
                usedLabelList.add(defaultLabelList.get(name));
            }

        }
        return usedLabelList;
    }
    private static List<Label> initUnusedLabelsFromJson(){
        List<Label> list =initUsedLabelsFromJson();
        List<Label> unusedLabelList=new ArrayList<Label>();
        for (Label label : getDefaultLabelList().values()) {
            if (!list.contains(label)) {
                unusedLabelList.add(label);
            }
        }
        return unusedLabelList;
    }
}
