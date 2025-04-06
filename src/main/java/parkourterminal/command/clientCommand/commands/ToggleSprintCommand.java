package parkourterminal.command.clientCommand.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import parkourterminal.command.clientCommand.TerminalCommandBase;
import parkourterminal.global.json.TerminalJsonConfig;
import parkourterminal.gui.screens.intf.instantiationScreen.intf.ScreenID;
import parkourterminal.gui.screens.intf.instantiationScreen.manager.ScreenManager;
import parkourterminal.util.SendMessageHelper;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ToggleSprintCommand extends TerminalCommandBase {
    @Override
    public String getCommandName() {
        return "togglesprint";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Toggles sprint";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if(args.length>=1){
            SendMessageHelper.addChatMessage(sender,"Invalid Command, try /tl help");
        }else{
            TerminalJsonConfig.getProperties().toggleSprint();
            SendMessageHelper.addChatMessage(sender,"ToggleSprint: " + (TerminalJsonConfig.getProperties().isToggleSprint()? "ON" : "OFF"));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }
}