package parkourterminal.input.commands;

import java.util.List;

public abstract class CommandTemplate {
    public abstract String commandHandler(List<String> args);
    public abstract String description();
}