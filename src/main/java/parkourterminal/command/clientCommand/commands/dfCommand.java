package parkourterminal.command.clientCommand.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import parkourterminal.command.clientCommand.TerminalCommandBase;
import parkourterminal.global.GlobalConfig;
import parkourterminal.global.json.TerminalJsonConfig;
import parkourterminal.util.ParseHelper;
import parkourterminal.util.SendMessageHelper;

import java.util.List;

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
            if(!ParseHelper.isNunNegativeInteger(args[0])){
                SendMessageHelper.addChatMessage(sender,"Invalid Command, try /tl help");
                return;
            }
            if(Integer.parseInt(args[0])<0||Integer.parseInt(args[0])>16){
                SendMessageHelper.addChatMessage(sender,"precision is between 0 and 16");
                return;
            }
            TerminalJsonConfig.getProperties().setPrecision(Integer.parseInt(args[0]));
            SendMessageHelper.addChatMessage(sender,"Changed coords precision to "+args[0]+" decimals.");
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }

}