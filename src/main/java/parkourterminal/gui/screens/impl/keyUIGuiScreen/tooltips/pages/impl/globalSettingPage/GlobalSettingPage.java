package parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.impl.globalSettingPage;

import parkourterminal.gui.Button.ButtonClickEventListener;
import parkourterminal.gui.Button.CustomButton;
import parkourterminal.gui.component.Slider.SliderImpl;
import parkourterminal.gui.component.Slider.SliderValueChangedListener;
import parkourterminal.gui.layout.UIComponent;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.KeyBoard.KeyUI;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.KeyBoard.KeyUIManager;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.KeyUITooltips;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.AbstractToolTipPage;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.PageManager;
import parkourterminal.util.AnimationUtils.impls.interpolatingData.Interpolatingfloat;
import parkourterminal.util.listener.EventBus;

public class GlobalSettingPage extends AbstractToolTipPage {
    final SliderImpl<Interpolatingfloat> keySizeSlider;
    final SliderImpl<Interpolatingfloat> nameSizeSlider;
    public GlobalSettingPage(EventBus eventBus,KeyUITooltips container, PageManager pageManager) {
        super(eventBus, container, pageManager);
        SliderValueChangedListener<Interpolatingfloat> listener=new SliderValueChangedListener<Interpolatingfloat>() {
            @Override
            public void onValueChanged(Interpolatingfloat newValue) {
                KeyUIManager.getInstance().getKeyRenderer().setFontScale(newValue.getValue());
            }
        };
        keySizeSlider = new SliderImpl<Interpolatingfloat>(
                new Interpolatingfloat(0.7f),
                new Interpolatingfloat(3.0f),
                new Interpolatingfloat(0.1f),
                new Interpolatingfloat(1f),
                10f,
                60,
                10,
                0,0,
                listener);
        keySizeSlider.setShowScales(false);
        SliderValueChangedListener<Interpolatingfloat> listener2=new SliderValueChangedListener<Interpolatingfloat>() {
            @Override
            public void onValueChanged(Interpolatingfloat newValue) {
                KeyUIManager.getInstance().getNameRenderer().setFontScale(newValue.getValue());
            }
        };
        nameSizeSlider= new SliderImpl<Interpolatingfloat>(
                new Interpolatingfloat(0.7f),
                new Interpolatingfloat(3.0f),
                new Interpolatingfloat(0.1f),
                new Interpolatingfloat(1f),
                10f,
                60,
                10,
                0,0,
                listener2);
        nameSizeSlider.setShowScales(false);
        CustomButton button=new CustomButton(60,30,0xFFFF0000,0xFF00FF00,2,"Back");
        final PageManager pageManager1=pageManager;
        button.setClickEventListener(new ButtonClickEventListener() {
            @Override
            public void onButtonClick() {
                pageManager1.switchTo("homepage");
            }
        });
        CustomButton resetFontSize =new CustomButton(60,30,0xFFFF0000,0xFF00FF00,2,"Reset FontSize");
        resetFontSize.setClickEventListener(new ButtonClickEventListener() {
            @Override
            public void onButtonClick() {
                KeyUIManager.getInstance().getNameRenderer().setFontScale(1);
                KeyUIManager.getInstance().getKeyRenderer().setFontScale(1);
                nameSizeSlider.setValue(new Interpolatingfloat(1f));
                keySizeSlider.setValue(new Interpolatingfloat(1f));
            }
        });
        CustomButton resetSize =new CustomButton(60,30,0xFFFF0000,0xFF00FF00,2,"Reset Size");
        resetSize.setClickEventListener(new ButtonClickEventListener() {
            @Override
            public void onButtonClick() {
                for(UIComponent component:KeyUIManager.getInstance().getContainer().getComponents()){
                    ((KeyUI)component).resetSize();
                }
            }
        });
        CustomButton resetColors =new CustomButton(60,30,0xFFFF0000,0xFF00FF00,2,"Reset Colors");
        resetColors.setClickEventListener(new ButtonClickEventListener() {
            @Override
            public void onButtonClick() {
                for(UIComponent component:KeyUIManager.getInstance().getContainer().getComponents()){
                    ((KeyUI)component).resetColors();
                }
            }
        });
        addComponent(new FontSizeDiv());
        addComponent(keySizeSlider);
        addComponent(new NameSizeDiv());
        addComponent(nameSizeSlider);
        addComponent(resetFontSize);
        addComponent(resetSize);
        addComponent(resetColors);
        addComponent(button);
    }

    @Override
    public void registerListeners() {

    }

    @Override
    public String getIdentifier() {
        return "GlobalSettingPage";
    }

    @Override
    public void onEvent(String eventType, Object data) {

    }
    @Override
    public void onEnter(){
        super.onEnter();
        keySizeSlider.setValue(new Interpolatingfloat( KeyUIManager.getInstance().getKeyRenderer().getFontScale()));
        nameSizeSlider.setValue(new Interpolatingfloat( KeyUIManager.getInstance().getNameRenderer().getFontScale()));
    }
}
