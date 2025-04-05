package parkourterminal.command.clientCommand.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import parkourterminal.command.clientCommand.TerminalCommandBase;
import parkourterminal.data.globalData.GlobalData;
import parkourterminal.global.GlobalConfig;
import parkourterminal.gui.screens.intf.instantiationScreen.intf.ScreenID;
import parkourterminal.gui.screens.intf.instantiationScreen.manager.ScreenManager;
import parkourterminal.util.AnimationUtils.impls.interpolatingData.Interpolatingfloat;
import parkourterminal.util.ParseHelper;
import parkourterminal.util.SendMessageHelper;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class dfCommand extends TerminalCommandBase {
    @Override
    public String getCommandName() {
        return "df";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Changes the number of decimal numbers";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if(args.length!=1){
            SendMessageHelper.addChatMessage(sender,"Invalid Command, try /tl help");
        }else{
            if(!ParseHelper.isPositiveInteger(args[0])){
                SendMessageHelper.addChatMessage(sender,"Invalid Command, try /tl help");
                return;
            }
            if(Integer.parseInt(args[0])<0||Integer.parseInt(args[0])>16){
                SendMessageHelper.addChatMessage(sender,"precision is between 0 and 16");
                return;
            }
            GlobalConfig.updateConfig("precision",args[0]);
            SendMessageHelper.addChatMessage(sender,"Changed coords precision to "+args[0]+" decimals.");
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }

}