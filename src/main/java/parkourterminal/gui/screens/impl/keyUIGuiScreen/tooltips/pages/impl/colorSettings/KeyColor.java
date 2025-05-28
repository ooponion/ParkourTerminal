package parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.impl.colorSettings;

import parkourterminal.gui.Button.ButtonClickEventListener;
import parkourterminal.gui.Button.CustomButton;
import parkourterminal.gui.component.Slider.ColorSlider;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.KeyBoard.KeyUI;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.KeyBoard.KeyUIManager;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.KeyUITooltips;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.AbstractToolTipPage;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.PageManager;
import parkourterminal.util.SystemOutHelper;
import parkourterminal.util.listener.EventBus;

public class KeyColor extends AbstractToolTipPage {
    private final ColorSlider keySlider;
    public KeyColor(EventBus eventBus, KeyUITooltips container, PageManager pageManager) {
        super(eventBus, container, pageManager);
        CustomButton button=new CustomButton(60,30,0xFFFF0000,0xFF00FF00,2,"Back");
        final PageManager pageManager1=pageManager;
        button.setClickEventListener(new ButtonClickEventListener() {
            @Override
            public void onButtonClick() {
                pageManager1.switchTo("ColorSettingsPage");
            }
        });
        keySlider =new ColorSlider(0x00FF00, 0, 0,
                10f,
                60,
                70,
                new ColorSlider.SliderColorChangedListener() {
                    @Override
                    public void onValueChanged(int color) {
                        KeyUI keyUI= KeyUIManager.getInstance().getRightClickedKeyUI();
                        if(keyUI==null){
                            return;
                        }
                        keyUI.setKeyColor(color);
                    }
                });
        addComponent(keySlider);
        addComponent(button);
    }
    @Override
    public void registerListeners() {
        eventBus.register("rightClickKeyUI",this);
    }

    @Override
    public String getIdentifier() {
        return "KeyColorPage";
    }

    @Override
    public void onEvent(String eventType, Object data) {
        if(eventType.equals("rightClickKeyUI")){
            KeyUI keyUI=KeyUIManager.getInstance().getRightClickedKeyUI();
            if(keyUI==null){
                return;
            }
            keySlider.selectRGB(keyUI.getKeyColor());
        }
    }
}