package parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.impl.homePage;

import parkourterminal.gui.Button.ButtonClickEventListener;
import parkourterminal.gui.Button.CustomButton;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.KeyBoard.KeyUIManager;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.KeyUITooltips;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.ToolTipManager;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.AbstractToolTipPage;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.PageManager;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.impl.DisableDiv;
import parkourterminal.gui.screens.intf.instantiationScreen.intf.ScreenID;
import parkourterminal.gui.screens.intf.instantiationScreen.manager.ScreenManager;
import parkourterminal.util.listener.EventBus;

public class Homepage extends AbstractToolTipPage {
    public Homepage(EventBus eventBus, KeyUITooltips container, PageManager pageManager1) {
        super(eventBus, container, pageManager1);

        DisableDiv disableDiv=new DisableDiv();
        disableDiv.setDisableListener(new DisableDiv.DisableListener() {
            @Override
            public void onDisable() {
                KeyUIManager.getInstance().getContainer().setComponentsEnabled(false);
            }

            @Override
            public void onEnable() {
                KeyUIManager.getInstance().getContainer().setComponentsEnabled(true);
            }
        });
        addComponent(disableDiv);
        addComponent(getGUIButton());
        addComponent(getGlobalSettingButton());
    }

    private CustomButton getGUIButton() {
        CustomButton button=new CustomButton(60,60,0xFFFF0000,0xFF00FF00,2,"GUI");
        button.setClickEventListener(new ButtonClickEventListener() {
            @Override
            public void onButtonClick() {
                pageManager.switchTo("guiPage");
                ScreenManager.SwitchToScreen(new ScreenID("KeyUIGuiScreen"));
                ToolTipManager.getInstance().getKeyUITooltips().setEnabled(false);
            }
        });
        return button;
    }
    private CustomButton getGlobalSettingButton() {
        CustomButton button=new CustomButton(60,60,0xFFFF0000,0xFF00FF00,2,"Global Settings");
        button.setClickEventListener(new ButtonClickEventListener() {
            @Override
            public void onButtonClick() {
                pageManager.switchTo("GlobalSettingPage");
            }
        });
        return button;
    }

    @Override
    public void registerListeners() {

    }

    @Override
    public String getIdentifier() {
        return "homepage";
    }
    public void onEvent(String eventType, Object data){
    }
}
