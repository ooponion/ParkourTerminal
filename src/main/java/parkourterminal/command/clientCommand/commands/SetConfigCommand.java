package parkourterminal.command.clientCommand.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import parkourterminal.command.clientCommand.TerminalCommandBase;
import parkourterminal.gui.screens.intf.instantiationScreen.intf.ScreenID;
import parkourterminal.gui.screens.intf.instantiationScreen.manager.ScreenManager;
import parkourterminal.util.SendMessageHelper;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SetConfigCommand extends TerminalCommandBase {
    @Override
    public String getCommandName() {
        return "config";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Opens a GUI for setting configs";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if(args.length>=1){
            SendMessageHelper.addChatMessage(sender,"Invalid Command, try /tl help");
        }else{
            Minecraft.getMinecraft().inGameHasFocus=true;
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                        @Override
                        public void run() {
                            if (Minecraft.getMinecraft().thePlayer != null) {
                                ScreenManager.SwitchToScreen(new ScreenID("ConfigSettingScreen"));
                            }
                        }
                    });
                }
            }, 100, TimeUnit.MILLISECONDS);
            SendMessageHelper.addChatMessage(sender,"Opens config");
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }
}