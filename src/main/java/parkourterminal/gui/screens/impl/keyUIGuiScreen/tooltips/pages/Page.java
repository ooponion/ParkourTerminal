package parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages;

public interface Page {
    void onEnter();
    void onExit();
    void registerListeners();
    String getIdentifier();
}
