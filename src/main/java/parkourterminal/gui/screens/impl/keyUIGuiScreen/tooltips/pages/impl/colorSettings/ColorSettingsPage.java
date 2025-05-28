package parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.impl.colorSettings;

import parkourterminal.gui.Button.ButtonClickEventListener;
import parkourterminal.gui.Button.CustomButton;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.KeyUITooltips;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.AbstractToolTipPage;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.PageManager;
import parkourterminal.util.listener.EventBus;

public class ColorSettingsPage extends AbstractToolTipPage {
    public ColorSettingsPage(EventBus eventBus, KeyUITooltips container, PageManager pageManager) {
        super(eventBus, container, pageManager);
        CustomButton backgroundButton =new CustomButton(60,30,0xFFFF0000,0xFF00FF00,2,"Background");
        final PageManager pageManager1=pageManager;
        backgroundButton.setClickEventListener(new ButtonClickEventListener() {
            @Override
            public void onButtonClick() {
                pageManager1.switchTo("BackgroundColorPage");
            }
        });
        CustomButton keyButton =new CustomButton(60,30,0xFFFF0000,0xFF00FF00,2,"Key");
        keyButton.setClickEventListener(new ButtonClickEventListener() {
            @Override
            public void onButtonClick() {
                pageManager1.switchTo("KeyColorPage");
            }
        });
        CustomButton nameButton =new CustomButton(60,30,0xFFFF0000,0xFF00FF00,2,"Name");
        nameButton.setClickEventListener(new ButtonClickEventListener() {
            @Override
            public void onButtonClick() {
                pageManager1.switchTo("NameColorPage");
            }
        });
        CustomButton back=new CustomButton(60,30,0xFFFF0000,0xFF00FF00,2,"Back");
        back.setClickEventListener(new ButtonClickEventListener() {
            @Override
            public void onButtonClick() {
                pageManager1.switchTo("guiPage");
            }
        });
        addComponent(backgroundButton);
        addComponent(keyButton);
        addComponent(nameButton);
        addComponent(back);
    }

    @Override
    public void registerListeners() {

    }

    @Override
    public String getIdentifier() {
        return "ColorSettingsPage";
    }

    @Override
    public void onEvent(String eventType, Object data) {
    }
}