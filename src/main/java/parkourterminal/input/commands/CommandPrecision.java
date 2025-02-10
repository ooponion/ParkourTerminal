package parkourterminal.input.commands;

import parkourterminal.global.GlobalConfig;

import java.util.List;

public class CommandPrecision extends CommandTemplate {
    @Override
    public String commandHandler(List<String> args) {
        if (args.isEmpty())
            return "Current precision: " + GlobalConfig.precision;
        else if (args.size() == 1) {
            try {
                int newPrecision = Integer.parseInt(args.get(0));

                if (newPrecision >= 3 && newPrecision <= 20) {
                    GlobalConfig.updateConfig("precision", args.get(0));
                    GlobalConfig.saveConfig();
                    return "Update precision successfully!";
                }

                return "Precision must be between 3 and 20.";
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
