package parkourterminal.util;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import parkourterminal.data.GlobalData;
import parkourterminal.global.json.TerminalJsonConfig;

public class SendMessageHelper {
    public static void addChatMessage(EntityPlayerSP player, String text){
        player.addChatMessage(new ChatComponentText(GlobalData.getLabelColor()+ TerminalJsonConfig.getPrefixWithAngleBrackets() +
                GlobalData.getValueColor()+text));
    };
    public static void addChatMessage(ICommandSender sender, String text){
        sender.addChatMessage(new ChatComponentText(GlobalData.getLabelColor()+ TerminalJsonConfig.getPrefixWithAngleBrackets() +
                GlobalData.getValueColor()+text));
    };
}
