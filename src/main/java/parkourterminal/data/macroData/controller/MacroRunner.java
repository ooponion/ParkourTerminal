package parkourterminal.data.macroData.controller;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import parkourterminal.data.macroData.intf.Macro;
import parkourterminal.data.macroData.intf.Operation;
import parkourterminal.global.json.TerminalJsonConfig;

public class MacroRunner {
    public static int jumpKey = Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode();
    public static int sneakKey = Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode();
    public static int sprintKey = Minecraft.getMinecraft().gameSettings.keyBindSprint.getKeyCode();
    public static int leftKey = Minecraft.getMinecraft().gameSettings.keyBindLeft.getKeyCode();
    public static int rightKey = Minecraft.getMinecraft().gameSettings.keyBindRight.getKeyCode();
    public static int forwardKey = Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode();
    public static int backKey = Minecraft.getMinecraft().gameSettings.keyBindBack.getKeyCode();
    private static boolean inProgress = false;
    private static int tick=0;
    private static Macro macro=null;
    public static void reset(){
        inProgress = false;
        tick=0;
        KeyBinding.setKeyBindState(jumpKey, false);
        KeyBinding.setKeyBindState(sneakKey, false);
        KeyBinding.setKeyBindState(sprintKey, false);
        KeyBinding.setKeyBindState(leftKey, false);
        KeyBinding.setKeyBindState(rightKey, false);
        KeyBinding.setKeyBindState(forwardKey, false);
        KeyBinding.setKeyBindState(backKey, false);
    }
    public static void run(){
        macro = TerminalJsonConfig.getMacroData().getCurrentMacro();
        reset();
        inProgress=true;
    }
    public static void onTick(EntityPlayerSP player){
        if(macro==null){
            return;
        }
        if(inProgress&&tick<macro.getSize()){
            Operation operation=macro.getMacro().get(tick);
            operation.execute(player);
            tick++;
        }else if(tick>=macro.getSize()){
            reset();
        }
    }
}
