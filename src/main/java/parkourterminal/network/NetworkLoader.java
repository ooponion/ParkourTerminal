package parkourterminal.network;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkLoader {
    public static final SimpleNetworkWrapper NETWORK_WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel("parkourterminal");

    public NetworkLoader(FMLPreInitializationEvent event) {
        int packetId = 0;

        NETWORK_WRAPPER.registerMessage(SaveCoordinatePacket.Handler.class, SaveCoordinatePacket.class, packetId++, Side.SERVER);
        NETWORK_WRAPPER.registerMessage(DeleteCoordinatePacket.Handler.class, DeleteCoordinatePacket.class, packetId++, Side.SERVER);
        NETWORK_WRAPPER.registerMessage(TeleportPacket.Handler.class, TeleportPacket.class, packetId++, Side.SERVER);
        NETWORK_WRAPPER.registerMessage(SelectCoordinatePacket.Handler.class, SelectCoordinatePacket.class, packetId++, Side.SERVER);
    }
}