package parkourterminal.command.clientCommand.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import parkourterminal.command.clientCommand.TerminalCommandBase;
import parkourterminal.data.GlobalData;
import parkourterminal.util.ParseHelper;
import parkourterminal.util.SendMessageHelper;
import scala.Int;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LBCondCommand extends TerminalCommandBase {
    private final  String regex = "\\s+(?:(radius)\\s)?([-+]?[0-9]*\\.?[0-9]+(?:\\s+[-+]?[0-9]*\\.?[0-9]+)*)";

    @Override
    public String getCommandName() {
        return "cond";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Sets the cond zone";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (!GlobalData.getLandingBlock().hasBox()) {
            SendMessageHelper.addChatMessage(sender, "no lb was set");
            return;
        }
        StringBuilder input = new StringBuilder();
        for (String arg : args) {
            input.append(" ");
            input.append(arg);
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input.toString());
        if (matcher.matches()) {
            String hasRadius = matcher.group(1);
            String numberPart = matcher.group(2);
            String[] numbers = numberPart.split("\\s+");
            if(hasRadius!=null&&numbers.length==1){
                GlobalData.getLandingBlock().getWholeCollisionBox().setRadius(Double.parseDouble(numbers[0]));
                SendMessageHelper.addChatMessage(sender, "Successfully set the cond radius");
                return;
            } else if(numbers.length==4){
                double minX=Double.parseDouble(numbers[0]);
                double minZ=Double.parseDouble(numbers[1]);
                double maxX=Double.parseDouble(numbers[2]);
                double maxZ=Double.parseDouble(numbers[3]);
                GlobalData.getLandingBlock().getWholeCollisionBox().setCondZone(minX,maxX,minZ,maxZ);
                SendMessageHelper.addChatMessage(sender, "Successfully set the cond zone");
                return;
            }
        }
        SendMessageHelper.addChatMessage(sender, "/tl cond radius <radius>");
        SendMessageHelper.addChatMessage(sender, "/tl cond <minX> <minZ> <maxX> <maxZ>");
    }


    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        double x=sender.getPositionVector().xCoord;
        double z=sender.getPositionVector().zCoord;
        int xi= MathHelper.floor_double(x);
        int zi= MathHelper.floor_double(z);
        if(args.length==1){

            return CommandBase.getListOfStringsMatchingLastWord(args,Double.toString(xi-1),"radius");
        }
        if(args.length==2){
            return CommandBase.getListOfStringsMatchingLastWord(args, Double.toString(zi-1));
        }
        if(args.length==3){
            return CommandBase.getListOfStringsMatchingLastWord(args, Double.toString(xi+2));
        }
        if(args.length==4){
            return CommandBase.getListOfStringsMatchingLastWord(args, Double.toString(zi+2));
        }
        return null;
    }

}