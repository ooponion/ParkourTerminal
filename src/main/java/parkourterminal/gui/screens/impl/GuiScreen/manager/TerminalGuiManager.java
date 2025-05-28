package parkourterminal.gui.screens.impl.GuiScreen.manager;

import parkourterminal.gui.screens.impl.GuiScreen.components.DisableTip;
import parkourterminal.gui.screens.impl.GuiScreen.components.UnusedLabelContainer.UnusedLabelContainer;
import parkourterminal.gui.screens.impl.GuiScreen.components.UsedLabelContainer.UsedLabelContainer;

public class TerminalGuiManager {
    private static TerminalGuiManager instance;
    private final DisableTip disableTip=new DisableTip();
    private final UsedLabelContainer usedLabelContainer;
    private final UnusedLabelContainer unusedLabelContainer;

    public TerminalGuiManager() {

        usedLabelContainer =new UsedLabelContainer(disableTip);
        unusedLabelContainer =new UnusedLabelContainer(disableTip,usedLabelContainer);
        usedLabelContainer.SetUnusedLabelContainer(unusedLabelContainer);

    }
    public static TerminalGuiManager getInstance() {
        if (instance == null) {
            instance = new TerminalGuiManager();
        }
        return instance;
    }
    public DisableTip getDisableTip() {
        return disableTip;
    }
    public UsedLabelContainer getUsedLabelContainer() {
        return usedLabelContainer;
    }
    public UnusedLabelContainer getUnusedLabelContainer() {
        return unusedLabelContainer;
    }
}
