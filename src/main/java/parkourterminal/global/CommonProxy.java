package parkourterminal.global;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import parkourterminal.command.CommandLoader;
import parkourterminal.network.NetworkLoader;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        new NetworkLoader(event);
    }

    public void init(FMLInitializationEvent event) {
        new EventLoader();
    }

    public void serverStarting(FMLServerStartingEvent event) {
        new CommandLoader(event);
    }
}