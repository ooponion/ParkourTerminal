package parkourterminal.command.clientCommand.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import parkourterminal.command.clientCommand.TerminalCommandBase;
import parkourterminal.data.GlobalData;
import parkourterminal.data.landingblock.intf.LBmod;
import parkourterminal.global.json.TerminalJsonConfig;
import parkourterminal.util.ParseHelper;
import parkourterminal.util.SendMessageHelper;

import java.util.List;

public class SetLBmodCommand extends TerminalCommandBase {
    @Override
    public String getCommandName() {
        return "landmod";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Changes the Land block mod";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if(args.length!=1||!(args[0].equalsIgnoreCase("land")||
                args[0].equalsIgnoreCase("hit")||
                args[0].equalsIgnoreCase("zneo")||
                args[0].equalsIgnoreCase("enter"))){
            SendMessageHelper.addChatMessage(sender,"Invalid Command, try /tl help");
        }else{
            SendMessageHelper.addChatMessage(sender,"Successfully set the land block mod to "+args[0].toLowerCase());
            if(args[0].equalsIgnoreCase("land")){
                GlobalData.getLandingBlock().setlBmod(LBmod.Land);
            }else if(args[0].equalsIgnoreCase("hit")){
                GlobalData.getLandingBlock().setlBmod(LBmod.Hit);
            }else if(args[0].equalsIgnoreCase("zneo")){
                GlobalData.getLandingBlock().setlBmod(LBmod.Z_neo);
            }else if(args[0].equalsIgnoreCase("enter")){
                GlobalData.getLandingBlock().setlBmod(LBmod.Enter);
            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if(args.length==1){
            return CommandBase.getListOfStringsMatchingLastWord(args,"land","hit","zneo","enter");
        }
        return null;
    }

}