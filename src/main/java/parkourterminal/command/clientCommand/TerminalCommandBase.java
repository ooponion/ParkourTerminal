package parkourterminal.command.clientCommand;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

import java.util.List;

public abstract class TerminalCommandBase implements ICommand {
    public abstract String getCommandName() ;

    public abstract String getCommandUsage(ICommandSender sender) ;
    public abstract void processCommand(ICommandSender sender, String[] args) throws CommandException;
    public abstract List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos);




    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }


    @Override
    public List<String> getCommandAliases() {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand p_compareTo_1_)
    {
        return this.getCommandName().compareTo(p_compareTo_1_.getCommandName());
    }

}
