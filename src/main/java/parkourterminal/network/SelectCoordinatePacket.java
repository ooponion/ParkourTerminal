package parkourterminal.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import parkourterminal.util.TeleporterHelper;

public class SelectCoordinatePacket implements IMessage {
    private int selectedIndex;

    public SelectCoordinatePacket() { }

    public SelectCoordinatePacket(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        // 将目标索引写入 ByteBuf
        buf.writeInt(selectedIndex);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        // 从 ByteBuf 中读取目标索引
        selectedIndex = buf.readInt();
    }

    public static class Handler implements IMessageHandler<SelectCoordinatePacket, IMessage> {
        @Override
        public IMessage onMessage(final SelectCoordinatePacket message, final MessageContext ctx) {
            ctx.getServerHandler().playerEntity.mcServer.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    TeleporterHelper.switchSelectedIndex(ctx.getServerHandler().playerEntity, message.selectedIndex);
                }
            });
            return null;
        }
    }
}