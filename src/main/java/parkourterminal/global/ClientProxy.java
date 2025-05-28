package parkourterminal.global;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import parkourterminal.command.clientCommand.ClientCommandsHelper;
import parkourterminal.global.event.*;
import parkourterminal.gui.Overlay.OverlayHandler;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.manager.LabelManager;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.KeyBoard.KeyUIManager;
import parkourterminal.gui.screens.intf.instantiationScreen.manager.ScreenManager;
import parkourterminal.input.TerminalCommandHandler;
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        ClientCommandsHelper.RegisterClientSideCommands();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        // Only client would have extra code
        super.init(event);

        // Client-only logic

        MinecraftForge.EVENT_BUS.register(new MenuHandler());
        MinecraftForge.EVENT_BUS.register(new LandBlockRendererHandler());
        MinecraftForge.EVENT_BUS.register(new TickEventHandler());

        OverlayHandler.RegisterOverlayInit();
        TerminalCommandHandler.terminalCommandInit();
        GlobalConfig.configInit();//here configRead
        ScreenManager.registerScreens();
        LabelManager.InitContainer();//depends screens
        KeyUIManager.initContainer();
        KeyHandler.register();
    }
}
