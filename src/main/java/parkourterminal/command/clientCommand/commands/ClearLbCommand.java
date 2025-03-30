package parkourterminal.command.clientCommand.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import parkourterminal.command.clientCommand.TerminalClientCommandsHandler;
import parkourterminal.command.clientCommand.TerminalCommandBase;
import parkourterminal.data.globalData.GlobalData;
import parkourterminal.global.GlobalConfig;

import java.util.ArrayList;
import java.util.List;

public class ClearLbCommand extends TerminalCommandBase {
    @Override
    public String getCommandName() {
        return "clearlb";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Clears the saved landing block";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if(args.length>=1){
            sender.addChatMessage(new ChatComponentText(GlobalData.getLabelColor()+"No such command"));
        }else{
            sender.addChatMessage(new ChatComponentText(GlobalData.getLabelColor()+"Successfully cleared landing block"));
            GlobalData.getLandingBlock().setOffsets(new Double[]{Double.NaN,Double.NaN,Double.NaN});
            GlobalData.getLandingBlock().setPb(new Double[]{Double.NaN,Double.NaN,Double.NaN});
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }
}