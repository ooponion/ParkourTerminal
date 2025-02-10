package parkourterminal;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import parkourterminal.global.CommonProxy;

@Mod(modid = ParkourTerminal.MODID, name = ParkourTerminal.NAME, version = ParkourTerminal.VERSION, acceptedMinecraftVersions = "1.8.9")
public class ParkourTerminal {
    // Basic information about mod (see also build.gradle)
    public static final String MODID = "parkourterminal";
    public static final String NAME = "Parkour Terminal";
    public static final String VERSION = "1.0.0";

    // Instance of mod
    @Mod.Instance(ParkourTerminal.MODID)
    public static ParkourTerminal instance;

    // Proxy and initialization
    @SidedProxy(clientSide = "parkourterminal.global.ClientProxy", serverSide = "parkourterminal.global.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }
}
