package parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips;

import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.PageManager;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.impl.colorSettings.BackgroundColor;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.impl.colorSettings.ColorSettingsPage;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.impl.colorSettings.KeyColor;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.impl.colorSettings.NameColor;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.impl.createKeyPage.CreateKeyPage;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.impl.globalSettingPage.GlobalSettingPage;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.impl.guiPage.GuiPage;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.impl.homePage.Homepage;
import parkourterminal.util.listener.EventBus;

public class ToolTipManager {
    private static ToolTipManager tooltipManager=new ToolTipManager();
    private final KeyUITooltips tooltips;
    private final EventBus eventBus;
    private final PageManager pageManager;
//    private final ToolTipContext toolTipContext=new ToolTipContext(new EventBus(),new PageManager());
    public ToolTipManager(){
        eventBus = new EventBus();
        pageManager = new PageManager();
        tooltips=new KeyUITooltips(eventBus,pageManager);
        registerPages();
        pageManager.switchTo("homepage");
    }

    public EventBus getEventBus() {
        return eventBus;
    }
    public KeyUITooltips getKeyUITooltips() {
        return tooltips;
    }
    public static ToolTipManager getInstance(){
        return tooltipManager;
    }
    public PageManager getPageManager() {
        return pageManager;
    }
    private void registerPages(){
        pageManager.registerPage(new Homepage(eventBus,tooltips,pageManager));
        pageManager.registerPage(new GuiPage(eventBus,tooltips,pageManager));
        pageManager.registerPage(new GlobalSettingPage(eventBus,tooltips,pageManager));
        pageManager.registerPage(new ColorSettingsPage(eventBus,tooltips,pageManager));
        pageManager.registerPage(new BackgroundColor(eventBus,tooltips,pageManager));
        pageManager.registerPage(new KeyColor(eventBus,tooltips,pageManager));
        pageManager.registerPage(new NameColor(eventBus,tooltips,pageManager));
        pageManager.registerPage(new CreateKeyPage(eventBus,tooltips,pageManager));
    }
}
