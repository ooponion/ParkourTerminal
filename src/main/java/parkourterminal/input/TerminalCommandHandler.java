package parkourterminal.input;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import parkourterminal.input.commands.*;

import java.util.*;

// Total handler for all the commands in terminal
// clear should not have an individual command name, since it is "special"
public class TerminalCommandHandler {
    public static final Map<String, CommandTemplate> commandTable = new HashMap<String, CommandTemplate>();

    public static void terminalCommandInit() {
        registerCommand("clear", new CommandClear());
        registerCommand("help", new CommandHelp());
        registerCommand("animation", new CommandAnimation());
        registerCommand("precision", new CommandPrecision());
    }

    public static void registerCommand(String command, CommandTemplate handler) {
        commandTable.put(command.toLowerCase(), handler);
    }

    public static String handleCommand(String input) {
        input = input.trim();

        if (input.isEmpty())
            return "No command entered.";

        String[] parts = input.split("\\s+");
        String command = parts[0].toLowerCase();
        List<String> args = new ArrayList<String>(Arrays.asList(parts).subList(1, parts.length));

        if (commandTable.containsKey(command)) {
            CommandTemplate handler = commandTable.get(command);
            return handler.commandHandler(args);
        }

        return "Unknown command: " + command;
    }
}
