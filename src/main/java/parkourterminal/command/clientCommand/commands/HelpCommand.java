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
import parkourterminal.util.SendMessageHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelpCommand extends TerminalCommandBase {
    @Override
    public String getCommandName() {
        return "help";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Showing terminal client commands in chat";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if(args.length>1){
            SendMessageHelper.addChatMessage(sender,"Invalid Command, try /tl help");
            return;
        }
        sender.addChatMessage(new ChatComponentText(GlobalData.getLabelColor()+""+EnumChatFormatting.BOLD+"TERMINAL Help:"));
        if(args.length==0){
            for (TerminalCommandBase commandBase:new TerminalClientCommandsHandler().getCommands()){
                sender.addChatMessage(new ChatComponentText(
                        GlobalData.getLabelColor()
                                +commandBase.getCommandName()+": "
                                +GlobalData.getValueColor()
                                +commandBase.getCommandUsage(sender)
                ));
            }
            return;
        }
        for (TerminalCommandBase commandBase:new TerminalClientCommandsHandler().getCommands()){
            if (commandBase.getCommandName().equalsIgnoreCase(args[0])){
                sender.addChatMessage(new ChatComponentText(
                        GlobalData.getLabelColor()
                                +commandBase.getCommandName()+": "
                                +GlobalData.getValueColor()
                                +commandBase.getCommandUsage(sender)
                ));
                return;
            }
        }
        SendMessageHelper.addChatMessage(sender,"Invalid Command, try /tl help");
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if(args.length!=1){
            return null;
        }
        List<String> tabStings=new ArrayList<String>();
        for (TerminalCommandBase commandBase:(new TerminalClientCommandsHandler().getCommands())){
            tabStings.add(commandBase.getCommandName());
        }
        return CommandBase.getListOfStringsMatchingLastWord(args,tabStings);

    }
}