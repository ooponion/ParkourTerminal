package parkourterminal.command.clientCommand;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import parkourterminal.command.clientCommand.commands.*;

import java.util.Arrays;
import java.util.List;

public class TerminalClientCommandsHandler extends CommandBase {
    private final List<String> aliases;
    private final List<TerminalCommandBase> commands;

    public TerminalClientCommandsHandler() {
        this.aliases = Arrays.asList("tl", "zhengli");
        commands=ClientCommandsHelper.parseToList(
                new HelpCommand(),
                new SetlbCommand(),
                new ClearLbCommand(),
                new ColorSetCommand(),
                new GuiCommand(),
                new ResetGuiCommand(),
                new dfCommand(),
                new ClearPbCommand(),
                new SetConfigCommand(),
                new ToggleSprintCommand(),
                new ClearMaxSpeedCommand(),
                new SetLBmodCommand(),
                new LBCommand());
    }
    @Override
    public List<String> getCommandAliases(){
        return aliases;
    };
    @Override
    public String getCommandName() {
        return "terminal";
    }
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return ClientCommandsHelper.getListOfStringsMatchingLastWord(sender,args,pos,commands);
    }
    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "terminal commands";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        ClientCommandsHelper.processCommand(sender,args,commands);
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    public List<TerminalCommandBase> getCommands() {
        return commands;
    }
}