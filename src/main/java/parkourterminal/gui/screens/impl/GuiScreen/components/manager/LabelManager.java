package parkourterminal.gui.screens.impl.GuiScreen.components.manager;

import ibxm.Player;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import parkourterminal.gui.screens.impl.GuiScreen.components.Label;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.impl.LabelValueDegree;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.impl.LabelValueDouble;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LabelManager {
    private static final HashMap<String,Label> labelList=new HashMap<String,Label>();
    static {
        addLabel(new Label("X",new LabelValueDouble()));
        addLabel(new Label("Y",new LabelValueDouble()));
        addLabel(new Label("Z",new LabelValueDouble()));
        addLabel(new Label("F",new LabelValueDegree()));
        addLabel(new Label("Pitch",new LabelValueDegree()));
    }
    public static HashMap<String,Label> getLabelList(){
        return labelList;
    }
    private static void addLabel(Label label){
        labelList.put(label.getLabel(),label);
    }
    public static void UpdateLabelValuesPerTick(){
        EntityPlayerSP player =Minecraft.getMinecraft().thePlayer;
        labelList.get("X").getValue().Update(player.posX);
        labelList.get("Y").getValue().Update(player.posY);
        labelList.get("Z").getValue().Update(player.posZ);
//        labelList.get("F").getValue().Update(player.cameraYaw);
//        labelList.get("Pitch").getValue().Update(player.cameraPitch);
        labelList.get("F").getValue().Update(player.rotationYaw);
        labelList.get("Pitch").getValue().Update(player.rotationPitch);
    }
}
