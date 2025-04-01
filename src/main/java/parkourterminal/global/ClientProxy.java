package parkourterminal.global;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import parkourterminal.command.clientCommand.ClientCommandsHelper;
import parkourterminal.global.event.LandBlockRendererHandler;
import parkourterminal.global.event.MenuHandler;
import parkourterminal.global.event.TickEventHandler;
import parkourterminal.gui.Overlay.OverlayHandler;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.manager.LabelManager;
import parkourterminal.gui.screens.intf.instantiationScreen.manager.ScreenManager;
import parkourterminal.global.event.KeyHandler;
import parkourterminal.input.TerminalCommandHandler;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        // Only client would have extra code
        super.init(event);

        // Client-only logic
        ClientCommandsHelper.RegisterClientSideCommands();
        MinecraftForge.EVENT_BUS.register(new MenuHandler());
        MinecraftForge.EVENT_BUS.register(new LandBlockRendererHandler());
        MinecraftForge.EVENT_BUS.register(new TickEventHandler());

        OverlayHandler.RegisterOverlayInit();
        TerminalCommandHandler.terminalCommandInit();
        GlobalConfig.configInit();//here configRead
        ScreenManager.registerScreens();
        LabelManager.InitLabels();
        LabelManager.TerminalGuiInitContainers();//depends screens
        KeyHandler.register();
    }
}
