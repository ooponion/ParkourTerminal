package parkourterminal.input.commands;

import java.util.List;

public class CommandClear extends CommandTemplate {
    @Override
    public String commandHandler(List<String> args) {
        if (args.isEmpty())
            return "";
        else
            return "Usage: clear";
    }

    @Override
    public String description() {
        return "Command 'clear' is used to clear your terminal.";
    }
}