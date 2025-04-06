package parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.manager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import parkourterminal.data.GlobalData;
import parkourterminal.global.json.LabelJson;
import parkourterminal.global.json.TerminalJsonConfig;
import parkourterminal.gui.screens.impl.GuiScreen.TerminalGuiScreen;
import parkourterminal.gui.screens.impl.GuiScreen.components.Label;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.impl.*;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.intf.BlipLabel;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.intf.LabelValue;
import parkourterminal.gui.screens.intf.instantiationScreen.intf.ScreenID;
import parkourterminal.gui.screens.intf.instantiationScreen.manager.ScreenManager;

import javax.vecmath.Vector3d;
import java.time.LocalDateTime;
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

        //PB Part
        addLabel("X PB",new LabelValueDouble());
        addLabel("Z PB",new LabelValueDouble());
        addLabel("PB",new LabelValueDouble());

        //Strat Part
        addLabel("Last Timing",new LabelValueString());
        addLabel("Last Input",new LabelValueString());

        //Land Part
        addLabel("Hit X",new LabelValueDouble());
        addLabel("Hit Y",new LabelValueDouble());
        addLabel("Hit Z",new LabelValueDouble());
        addLabel("Landing X",new LabelValueDouble());
        addLabel("Landing Y",new LabelValueDouble());
        addLabel("Landing Z",new LabelValueDouble());

        //SMTH
        addLabel("Speed (b/t)",new LabelValue3DVector());
        addLabel("Speed Vector",new LabelValue2DDegreeVector());
        addLabel("Max Speed (b/t)",new LabelValue3DVector());
        addLabel("Max Speed Vector",new LabelValue2DDegreeVector());
        addLabel("Date",new LabelValueDate());
        addLabel("Time",new LabelValueTime());
        addLabel("FPS",new LabelValueInt());
        addLabel("Looking At",new LabelValueBlockPos());
        addLabel("Motion (b/t)",new LabelValue3DVector());
        addLabel("Tier",new LabelValueInt());
        addLabel("Ping",new LabelValuePing());

        //Jump
        addLabel("Jump X",new LabelValueDouble());
        addLabel("Jump Y",new LabelValueDouble());
        addLabel("Jump Z",new LabelValueDouble());

        //AngleRelated
        addLabel("Last turning",new LabelValueDegree());
        addLabel("Preturn",new LabelValueDegree());
        addLabel("Jump Angle",new LabelValueDegree());
        addLabel("Last 45",new LabelValueDegree());
        addLabel("First turning",new LabelValueDegree());
        addLabel("Second turning",new LabelValueDegree());
        addLabel("Third turning",new LabelValueDegree());
        addLabel("Airtime",new LabelValueInt());

        //Blip
        addLabel("Blip",new LabelValueBlip());
    }
    public static HashMap<String,Label> getDefaultLabelList(){
        return  defaultLabelList;
    }
    private static void addLabel(String name, LabelValue labelValue){
        Label label=new Label(name,labelValue);
        defaultLabelList.put(label.getLabel(),label);
    }
    public static void UpdateLabelValuesPerTick(float partialTicks){
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

        //Strat Part
        UpdateLabel("Last Timing",GlobalData.getInputData().getStrat());
        UpdateLabel("Last Input",GlobalData.getInputData().getOperation().getDirectionKeys());

        //Land Part
        UpdateLabel("Hit X",GlobalData.getLandData().getHitX());
        UpdateLabel("Hit Y",GlobalData.getLandData().getHitY());
        UpdateLabel("Hit Z",GlobalData.getLandData().getHitZ());
        UpdateLabel("Landing X",GlobalData.getLandData().getLandingX());
        UpdateLabel("Landing Y",GlobalData.getLandData().getLandingY());
        UpdateLabel("Landing Z",GlobalData.getLandData().getLandingZ());

        //SMTH
        UpdateLabel("Speed (b/t)",GlobalData.getSpeedData().getSpeed());
        UpdateLabel("Speed Vector",GlobalData.getSpeedData().getSpeedVector());
        UpdateLabel("Max Speed (b/t)",GlobalData.getSpeedData().getMaxSpeed());
        UpdateLabel("Max Speed Vector",GlobalData.getSpeedData().getMaxSpeedVector());
        UpdateLabel("Date",LocalDateTime.now());
        UpdateLabel("Time", LocalDateTime.now());
        UpdateLabel("FPS",Minecraft.getDebugFPS());
        MovingObjectPosition result = player.rayTrace(10, partialTicks);
        if(result!=null){
            UpdateLabel("Looking At",new BlockPos(result.getBlockPos()));
        }
        UpdateLabel("Motion (b/t)",new Vector3d(player.motionX,player.motionY,player.motionZ));
        UpdateLabel("Tier",GlobalData.getLandData().getTier());
        UpdateLabel("Ping",getPing());

        //Jump
        UpdateLabel("Jump X",GlobalData.getJumpData().getJumpX());
        UpdateLabel("Jump Y",GlobalData.getJumpData().getJumpY());
        UpdateLabel("Jump Z",GlobalData.getJumpData().getJumpZ());

        //AngleRelated
        UpdateLabel("Last turning",GlobalData.getJumpData().getLastTurning());
        UpdateLabel("Jump Angle",GlobalData.getJumpData().getJumpAngle());
        UpdateLabel("Preturn",GlobalData.getJumpData().getPreturn());
        UpdateLabel("Last 45",GlobalData.getJumpData().getLast45());
        UpdateLabel("First turning",GlobalData.getJumpData().getFirstTurning());
        UpdateLabel("Second turning",GlobalData.getJumpData().getSecondTurning());
        UpdateLabel("Third turning",GlobalData.getJumpData().getThirdTurning());
        UpdateLabel("Airtime",GlobalData.getJumpData().getAirTime());

        //Blip
        UpdateLabel("Blip",new BlipLabel(GlobalData.getLandData().getBlipY(),GlobalData.getLandData().getBlipTimes()));
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
        System.out.printf("???????TerminalGuiInitContainers\n");
        TerminalGuiScreen guiScreen=(TerminalGuiScreen) ScreenManager.getGuiScreen(new ScreenID("TerminalGuiScreen"));
        if(guiScreen!=null){
            guiScreen.InitContainers(initUsedLabelsFromJson(),initUnusedLabelsFromJson());
        }
    }
    public static void TerminalGuiResetContainers(){
        TerminalGuiScreen guiScreen=(TerminalGuiScreen) ScreenManager.getGuiScreen(new ScreenID("TerminalGuiScreen"));
        int y=0;
        int padding=1;
        if(guiScreen!=null){
            List<Label> list=new ArrayList<Label>();
            for(String  name:defaultLabelList.keySet()) {
                Label label=defaultLabelList.get(name);
                label.setPosition(0,y);
                label.setEnabled(true);
                list.add(label);
                y+=label.getHeight()+padding;
            }
            guiScreen.InitContainers(list,new ArrayList<Label>());
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
    public static int getPing() {
        Minecraft mc = Minecraft.getMinecraft();
        NetworkPlayerInfo info = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID());
        return (info != null) ? info.getResponseTime() : 0;
    }
}
