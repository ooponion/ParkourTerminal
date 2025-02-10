package parkourterminal.input.commands;

import parkourterminal.global.GlobalConfig;

import java.util.List;

public class CommandAnimation extends CommandTemplate {
    @Override
    public String commandHandler(List<String> args) {
        if (args.isEmpty())
            return "Animation state: " + (GlobalConfig.animation ? "on" : "off");
        else if (args.size() == 1) {
            if (args.get(0).equals("on")) {
                GlobalConfig.updateConfig("animation", String.valueOf(true));
                GlobalConfig.saveConfig();
                return "Successfully enable output animation.";
            }
            else if (args.get(0).equals("off")) {
                GlobalConfig.updateConfig("animation", String.valueOf(false));
                GlobalConfig.saveConfig();
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