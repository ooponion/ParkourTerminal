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
            sender.addChatMessage(new ChatComponentText(GlobalData.getLabelColor()+"No such command"));
        }else{
            if(!isPositiveInteger(args[0])){
                sender.addChatMessage(new ChatComponentText(GlobalData.getLabelColor()+"No such command"));
                return;
            }
            GlobalConfig.updateConfig("precision",args[0]);
            sender.addChatMessage(new ChatComponentText(GlobalData.getLabelColor()+"Changed coords precision to "+args[0]+" decimals."));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }
    private static boolean isPositiveInteger(String str) {
        return str != null && str.matches("^[1-9]\\d*$"); // 正整数（不含0）
    }
}