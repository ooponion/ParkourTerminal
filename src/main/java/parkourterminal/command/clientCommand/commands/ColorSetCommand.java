package parkourterminal.command.clientCommand.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import parkourterminal.command.clientCommand.TerminalCommandBase;
import parkourterminal.data.GlobalData;
import parkourterminal.util.ParseHelper;
import parkourterminal.util.SendMessageHelper;

import java.util.List;

public class ColorSetCommand extends TerminalCommandBase {
    @Override
    public String getCommandName() {
        return "color";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Changes the UI colors";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if(args.length!=2||!(args[0].equalsIgnoreCase("label")||args[0].equalsIgnoreCase("value"))){
            SendMessageHelper.addChatMessage(sender,"Invalid Command, try /tl help");
        }else{
            SendMessageHelper.addChatMessage(sender,"Successfully set the color");
            EnumChatFormatting color= ParseHelper.parseEnumChatFormatting(args[1]);
            if(args[0].equalsIgnoreCase("label")){//label
                GlobalData.getColorData().setLabelColor(color);
            }else{//value
                GlobalData.getColorData().setValueColor(color);
            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if(args.length==1){
            return CommandBase.getListOfStringsMatchingLastWord(args,"label","value");
        }
        if(args.length==2){
            return CommandBase.getListOfStringsMatchingLastWord(args,
                    "black",
                    "dark_blue",
                    "dark_green",
                    "dark_aqua",
                    "dark_red",
                    "dark_purple",
                    "gold",
                    "gray",
                    "dark_gray",
                    "blue",
                    "green",
                    "aqua",
                    "red",
                    "light_purple",
                    "yellow",
                    "white");
        }
        return null;
    }

}