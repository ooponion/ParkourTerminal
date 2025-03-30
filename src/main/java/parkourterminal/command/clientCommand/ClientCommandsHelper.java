package parkourterminal.command.clientCommand;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientCommandsHelper {
    public static void RegisterClientSideCommands(){
        ClientCommandHandler.instance.registerCommand(new TerminalClientCommandsHandler());
    }
    public static List<TerminalCommandBase> parseToList(@Nonnull TerminalCommandBase... commands){
        return Arrays.asList(commands);
    }
    public static List<String> getListOfStringsMatchingLastWord(ICommandSender sender, String[] args, BlockPos pos, List<TerminalCommandBase> commandBases){
        if(args.length == 0){
            return null;
        }
        if(args.length==1){
            List<String> tabStings=new ArrayList<String>();
            for (TerminalCommandBase commandBase:commandBases){
                System.out.printf("args1:%s\n",Arrays.asList(args));
                tabStings.add(commandBase.getCommandName());
            }
            return CommandBase.getListOfStringsMatchingLastWord(args,tabStings);
        }
        for (TerminalCommandBase commandBase:commandBases){
            System.out.printf("args2:%s\n",Arrays.asList(args));
            if (commandBase.getCommandName().equalsIgnoreCase(args[0])){
                String[] newArray = new String[args.length - 1];
                System.arraycopy(args, 1, newArray, 0, args.length - 1);
                return commandBase.addTabCompletionOptions(sender,newArray,pos);
            }
        }
        return null;
    }
    public static void processCommand(ICommandSender sender, String[] args,List<TerminalCommandBase> commandBases) throws CommandException {
        if (args.length == 0) {
            sender.addChatMessage(new ChatComponentText("wrong format"));
            return;
        }
        for (TerminalCommandBase commandBase:commandBases){
            if (commandBase.getCommandName().equalsIgnoreCase(args[0])){
                String[] newArray = new String[args.length - 1];
                System.arraycopy(args, 1, newArray, 0, args.length - 1);
                commandBase.processCommand(sender,newArray);
                return;
            }
        }
        sender.addChatMessage(new ChatComponentText("unknown command: " + args[0]));
    }
}
