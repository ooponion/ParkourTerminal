package parkourterminal.command.clientCommand.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import parkourterminal.command.clientCommand.TerminalCommandBase;
import parkourterminal.data.globalData.GlobalData;
import parkourterminal.gui.screens.intf.instantiationScreen.intf.ScreenID;
import parkourterminal.gui.screens.intf.instantiationScreen.manager.ScreenManager;

import java.util.Arrays;
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
            sender.addChatMessage(new ChatComponentText(GlobalData.getLabelColor()+"No such command"));
        }else{
            EnumChatFormatting color=getChatFormattingByName(args[1]);
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
    private static EnumChatFormatting getChatFormattingByName(String name) {
        if (name == null) {
            return null;
        }
        name = name.toUpperCase();

        if (name.equals("BLACK")) return EnumChatFormatting.BLACK;
        else if (name.equals("DARK_BLUE")) return EnumChatFormatting.DARK_BLUE;
        else if (name.equals("DARK_GREEN")) return EnumChatFormatting.DARK_GREEN;
        else if (name.equals("DARK_AQUA")) return EnumChatFormatting.DARK_AQUA;
        else if (name.equals("DARK_RED")) return EnumChatFormatting.DARK_RED;
        else if (name.equals("DARK_PURPLE")) return EnumChatFormatting.DARK_PURPLE;
        else if (name.equals("GOLD")) return EnumChatFormatting.GOLD;
        else if (name.equals("GRAY")) return EnumChatFormatting.GRAY;
        else if (name.equals("DARK_GRAY")) return EnumChatFormatting.DARK_GRAY;
        else if (name.equals("BLUE")) return EnumChatFormatting.BLUE;
        else if (name.equals("GREEN")) return EnumChatFormatting.GREEN;
        else if (name.equals("AQUA")) return EnumChatFormatting.AQUA;
        else if (name.equals("RED")) return EnumChatFormatting.RED;
        else if (name.equals("LIGHT_PURPLE")) return EnumChatFormatting.LIGHT_PURPLE;
        else if (name.equals("YELLOW")) return EnumChatFormatting.YELLOW;
        else if (name.equals("WHITE")) return EnumChatFormatting.WHITE;
        else return null; // 未找到
    }
}