package parkourterminal.command.clientCommand.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import parkourterminal.command.clientCommand.TerminalCommandBase;
import parkourterminal.data.GlobalData;
import parkourterminal.util.SendMessageHelper;

import java.util.List;

public class ClearPbCommand extends TerminalCommandBase {
    @Override
    public String getCommandName() {
        return "clearpb";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Clears the PB value";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if(args.length>=1){
            SendMessageHelper.addChatMessage(sender,"Invalid Command, try /tl help");
        }else{
            SendMessageHelper.addChatMessage(sender,"Successfully cleared PB value");
            GlobalData.getLandingBlock().setOffsets(new Double[]{Double.NaN,Double.NaN,Double.NaN});
            GlobalData.getLandingBlock().setPb(new Double[]{Double.NaN,Double.NaN,Double.NaN});
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }
}