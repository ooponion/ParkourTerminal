package parkourterminal.command.clientCommand.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import parkourterminal.command.clientCommand.TerminalCommandBase;
import parkourterminal.data.GlobalData;
import parkourterminal.gui.screens.impl.GuiScreen.components.labelValueType.manager.LabelManager;
import parkourterminal.util.SendMessageHelper;

import java.util.List;

public class ResetGuiCommand extends TerminalCommandBase {
    @Override
    public String getCommandName() {
        return "resetgui";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Resets all labels and colors";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if(args.length>=1){
            SendMessageHelper.addChatMessage(sender,"Invalid Command, try /tl help");
        }else{
            GlobalData.getColorData().reset();
            LabelManager.TerminalGuiResetContainers();
            SendMessageHelper.addChatMessage(sender,"Successfully reset gui");
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }
}