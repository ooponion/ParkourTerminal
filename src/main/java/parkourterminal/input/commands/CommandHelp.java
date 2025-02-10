package parkourterminal.input.commands;

import java.util.List;
import parkourterminal.input.TerminalCommandHandler;

public class CommandHelp extends CommandTemplate {
    @Override
    public String commandHandler(List<String> args) {
        if (args.isEmpty()) {
            StringBuilder helpMessage = new StringBuilder("Available commands: ");

            for (String command : TerminalCommandHandler.commandTable.keySet())
                helpMessage.append(command).append(", ");

            if (helpMessage.length() > 2)
                helpMessage.setLength(helpMessage.length() - 2);

            return helpMessage.toString();
        }
        else if (args.size() == 1 && TerminalCommandHandler.commandTable.containsKey(args.get(0))) {
            return TerminalCommandHandler.commandTable.get(args.get(0)).description();
        }
        else
            return "Usage: help | help <command-name>";
    }

    @Override
    public String description() {
        return "Command 'help' is used to see all the functions already exists if there's no parameter, otherwise it will show the description of certain command just as this.";
    }
}
