package parkourterminal.gui.screens.intf.instantiationScreen.manager;

import net.minecraft.client.Minecraft;

import net.minecraft.client.gui.GuiScreen;
import parkourterminal.gui.screens.impl.CustomIngameMenu;

import parkourterminal.gui.screens.impl.GuiScreen.TerminalGuiScreen;
import parkourterminal.gui.screens.impl.ShiftRightClickScreen.ShiftRightClickGui;
import parkourterminal.gui.screens.intf.instantiationScreen.intf.InstantiationScreen;
import parkourterminal.gui.screens.intf.instantiationScreen.intf.ScreenID;

import java.util.HashMap;

public class ScreenManager {

    private static final HashMap<String,InstantiationScreen> screenMap=new HashMap<String,InstantiationScreen>();
    public static void registerScreens(){
        ScreenManager.addScreen(new CustomIngameMenu());
        ScreenManager.addScreen(new ShiftRightClickGui());
        ScreenManager.addScreen(new TerminalGuiScreen());

    }

    public static void SwitchToScreen(ScreenID id){
        if(!screenMap.containsKey(id.getID())){
            return;
        }
        InstantiationScreen screen=screenMap.get(id.getID());
        System.out.printf("switchTo?%s\n",screen.getScreenInstantiation());
        Minecraft.getMinecraft().displayGuiScreen(screen.getScreenInstantiation());
    }
    public static void SwitchToScreen(GuiScreen screen){
        Minecraft.getMinecraft().displayGuiScreen(screen);
    }
    public static void SwitchToGame(){
        Minecraft.getMinecraft().displayGuiScreen(null);
    }
    public static void addScreen(InstantiationScreen screen){
        screenMap.put(screen.getScreenID().getID(),screen);
    }
    public static GuiScreen getGuiScreen(ScreenID id){
        if(!screenMap.containsKey(id.getID())){
            return null;
        }
        return screenMap.get(id.getID()).getScreenInstantiation();
    }

}
