package parkourterminal.command.clientCommand.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import parkourterminal.command.clientCommand.TerminalCommandBase;
import parkourterminal.data.globalData.GlobalData;
import parkourterminal.gui.screens.impl.GuiScreen.TerminalGuiScreen;
import parkourterminal.gui.screens.intf.instantiationScreen.intf.ScreenID;
import parkourterminal.gui.screens.intf.instantiationScreen.manager.ScreenManager;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GuiCommand extends TerminalCommandBase {
    @Override
    public String getCommandName() {
        return "gui";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Opens a GUI for positioning/disabling/removing the labels";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if(args.length>=1){
            sender.addChatMessage(new ChatComponentText(GlobalData.getLabelColor()+"No such command"));
        }else{
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    if (Minecraft.getMinecraft().thePlayer != null) {
                        ScreenManager.SwitchToScreen(new ScreenID("TerminalGuiScreen"));
                    }
                }
            }, 100, TimeUnit.MILLISECONDS);
            sender.addChatMessage(new ChatComponentText(GlobalData.getLabelColor()+"Opens gui"));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }
}