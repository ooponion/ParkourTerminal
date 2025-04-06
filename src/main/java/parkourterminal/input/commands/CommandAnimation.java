package parkourterminal.input.commands;

import parkourterminal.global.GlobalConfig;
import parkourterminal.global.json.TerminalJsonConfig;

import java.util.List;

public class CommandAnimation extends CommandTemplate {
    @Override
    public String commandHandler(List<String> args) {
        if (args.isEmpty())
            return "Animation state: " + (TerminalJsonConfig.getProperties().isAnimationOn() ? "on" : "off");
        else if (args.size() == 1) {
            if (args.get(0).equals("on")) {
                TerminalJsonConfig.getProperties().setAnimationOn(true);
                return "Successfully enable output animation.";
            }
            else if (args.get(0).equals("off")) {
                TerminalJsonConfig.getProperties().setAnimationOn(false);
                return "Successfully disable output animation.";
            }

            return "Usage: animation | animation <on/off>";
        }

        return "Usage: animation | animation <on/off>";
    }

    @Override
    public String description() {
        return "Command 'animation' is used to enable/disable output animation.";
    }
}