package parkourterminal.input.commands;

import parkourterminal.global.GlobalConfig;
import parkourterminal.global.json.TerminalJsonConfig;

import java.util.List;

public class CommandPrecision extends CommandTemplate {
    @Override
    public String commandHandler(List<String> args) {
        if (args.isEmpty())
            return "Current precision: " + TerminalJsonConfig.getProperties().getPrecision();
        else if (args.size() == 1) {
            try {
                int newPrecision = Integer.parseInt(args.get(0));

                if (newPrecision >= 0 && newPrecision <= 16) {
                    TerminalJsonConfig.getProperties().setPrecision(Integer.parseInt(args.get(0)));
                    return "Update precision successfully!";
                }

                return "Precision must be between 0 and 16.";
            } catch (NumberFormatException e) {
                return "The precision should be a valid integer.";
            }
        }

        return "Usage: precision <number>";
    }

    @Override
    public String description() {
        return "Command 'precision' is used to modify global precision of number.";
    }
}
