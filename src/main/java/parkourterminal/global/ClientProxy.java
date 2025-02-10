package parkourterminal.global;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import parkourterminal.input.KeyHandler;
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
        TerminalCommandHandler.terminalCommandInit();
        GlobalConfig.configInit();
        KeyHandler.register();
    }
}
