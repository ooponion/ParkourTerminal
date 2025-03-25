package parkourterminal.global;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import parkourterminal.command.CommandLoader;
import parkourterminal.global.json.TerminalJsonConfig;
import parkourterminal.gui.screens.impl.GuiScreen.TerminalGuiScreen;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.manager.LabelManager;
import parkourterminal.gui.screens.intf.instantiationScreen.intf.ScreenID;
import parkourterminal.gui.screens.intf.instantiationScreen.manager.ScreenManager;
import parkourterminal.network.NetworkLoader;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        new NetworkLoader(event);
    }

    public void init(FMLInitializationEvent event) {
        new EventLoader();
    }

    public void serverStarting(FMLServerStartingEvent event) {
        ScreenManager.registerScreens();
        new CommandLoader(event);
    }
    public void serverClosing(FMLServerStoppingEvent event) {
        TerminalGuiScreen guiScreen=(TerminalGuiScreen)ScreenManager.getGuiScreen(new ScreenID("TerminalGuiScreen"));
        if(guiScreen!=null){
            LabelManager.saveConfigUsedLabels(guiScreen.getUsedLabels());
        }
    }
}