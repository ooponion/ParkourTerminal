package parkourterminal.command.clientCommand.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import parkourterminal.command.clientCommand.TerminalCommandBase;
import parkourterminal.data.macroData.controller.MacroRunner;
import parkourterminal.global.json.TerminalJsonConfig;
import parkourterminal.gui.screens.intf.instantiationScreen.intf.ScreenID;
import parkourterminal.gui.screens.intf.instantiationScreen.manager.ScreenManager;
import parkourterminal.util.SendMessageHelper;
import scala.actors.threadpool.Arrays;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MacroCommand extends TerminalCommandBase {
    @Override
    public String getCommandName() {
        return "macro";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Opens macro screen";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if(args.length>=2){
            SendMessageHelper.addChatMessage(sender,"Invalid Command, try /tl help");
        }else if(args.length==0){
            Minecraft.getMinecraft().inGameHasFocus=true;
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                        @Override
                        public void run() {
                            if (Minecraft.getMinecraft().thePlayer != null) {
                                ScreenManager.SwitchToScreen(new ScreenID("MacroScreen"));
                            }
                        }
                    });
                }
            }, 100, TimeUnit.MILLISECONDS);

            SendMessageHelper.addChatMessage(sender,"Successfully opens macro screen");
        }else {
            MacroRunner.run();
            SendMessageHelper.addChatMessage(sender,"Run macro "+ TerminalJsonConfig.getMacroData().getCurrentMacroName()+".");
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return CommandBase.getListOfStringsMatchingLastWord(args,"run");
    }
}