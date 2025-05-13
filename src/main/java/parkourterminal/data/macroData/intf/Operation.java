package parkourterminal.data.macroData.intf;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;

import static parkourterminal.data.macroData.controller.MacroRunner.*;

public class Operation {
    private int operates=0;
    private float dPitch=0;
    private float dYaw=0;
    public Operation(){};
    public Operation(int operates, float dYaw, float dPitch){
        this.operates=operates;
        this.dYaw=dYaw;
        this.dPitch=dPitch;
    }
    public Operation(Operation operation){
        this.operates=operation.operates;
        this.dPitch=operation.dPitch;
        this.dYaw= operation.dYaw;
    }
    public boolean GetBool(int operates,int position){
        return ((operates >> position) & 1) == 1;
    };
    public void toggleBool(int position) {
        operates =operates ^ (1 << position);
    }
    public int getOperates(){
        return operates;
    }

    public float getdPitch() {
        return dPitch;
    }

    public void setdPitch(float dPitch) {
        this.dPitch = dPitch;
    }

    public void setOperates(int operates){
        this.operates=operates;
    }
    public float getdYaw(){
        return dYaw;
    }
    public void setdYaw(float dYaw){
        this.dYaw=dYaw;
    }
    public boolean getW(){
        return GetBool(operates,0);
    }
    public boolean getS(){
        return GetBool(operates,1);
    }
    public boolean getA(){
        return GetBool(operates,2);
    }
    public boolean getD(){
        return GetBool(operates,3);
    }
    public boolean getSpace(){
        return GetBool(operates,4);
    }
    public boolean getSneak(){
        return GetBool(operates,5);
    }
    public boolean getSprint(){
        return GetBool(operates,6);
    }
    public void execute(EntityPlayerSP player){
        KeyBinding.setKeyBindState(jumpKey, getSpace());
        KeyBinding.setKeyBindState(sneakKey, getSneak());
        KeyBinding.setKeyBindState(sprintKey, getSprint());
        KeyBinding.setKeyBindState(leftKey, getA());
        KeyBinding.setKeyBindState(rightKey, getD());
        KeyBinding.setKeyBindState(forwardKey, getW());
        KeyBinding.setKeyBindState(backKey, getS());
        player.rotationYaw+=dYaw;
        player.rotationPitch+=dPitch;
    }
}
