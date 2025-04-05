package parkourterminal.command.clientCommand.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import parkourterminal.command.clientCommand.TerminalCommandBase;
import parkourterminal.data.GlobalData;
import parkourterminal.util.SendMessageHelper;

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
            SendMessageHelper.addChatMessage(sender,"Invalid Command, try /tl help");
        }else{
            SendMessageHelper.addChatMessage(sender,"Successfully cleared landing block");
            GlobalData.getLandingBlock().setAABBs(new ArrayList<AxisAlignedBB>());
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }
}