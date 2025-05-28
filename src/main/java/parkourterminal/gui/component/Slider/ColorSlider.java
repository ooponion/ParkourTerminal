package parkourterminal.gui.component.Slider;

import parkourterminal.gui.layout.*;
import parkourterminal.gui.layout.Container;
import parkourterminal.util.AnimationUtils.impls.interpolatingData.InterpolatingColor;
import parkourterminal.util.AnimationUtils.impls.interpolatingData.Interpolatingfloat;
import parkourterminal.util.AnimationUtils.intf.interpolating;
import parkourterminal.util.SystemOutHelper;

import java.awt.*;
import java.util.Optional;

public class ColorSlider extends ContainerKeyTyped {
    private final SliderImpl<Interpolatingfloat> hueSlider;
    private final SliderImpl<Interpolatingfloat> saturationSlider;
    private final SliderImpl<Interpolatingfloat> brightnessSlider;
    private final ColorDisplayDiv colorDisplayDiv = new ColorDisplayDiv();
    private boolean inSetting=false;
    private SliderColorChangedListener listener;
    public ColorSlider(int initColor, int x,int y, float thumbWidth,int width, int height,SliderColorChangedListener listener) {
        setSize(width, height);
        setPosition(x,y);
        setLayoutManager(new FlexLayout(LayoutDirection.VERTICAL, Alignment.STRETCH,Alignment.CENTER,4));
        addComponent(colorDisplayDiv);
        colorDisplayDiv.setColor(initColor);
        this.listener=listener;
        hueSlider=new SliderImpl<Interpolatingfloat>(new Interpolatingfloat(0), new Interpolatingfloat(1), new Interpolatingfloat(0.005f), thumbWidth, width, 20,null);
        saturationSlider=new SliderImpl<Interpolatingfloat>(new Interpolatingfloat(0), new Interpolatingfloat(1), new Interpolatingfloat(0.005f), thumbWidth, width, 20,null);
        brightnessSlider=new SliderImpl<Interpolatingfloat>(new Interpolatingfloat(0), new Interpolatingfloat(1), new Interpolatingfloat(0.005f), thumbWidth, width, 20,null);
        hueSlider.setShowScales(false);
        saturationSlider.setShowScales(false);
        brightnessSlider.setShowScales(false);
        hueSlider.setShowProgressTrack(false);
        saturationSlider.setShowProgressTrack(false);
        brightnessSlider.setShowProgressTrack(false);
        selectRGB(initColor);
        hueSlider.setListener(getSliderValueChangedListener(listener));
        saturationSlider.setListener(getSliderValueChangedListener(listener));
        brightnessSlider.setListener(getSliderValueChangedListener(listener));
        hueSlider.setDisplayValue(true);
        saturationSlider.setDisplayValue(true);
        brightnessSlider.setDisplayValue(true);
        addComponent(hueSlider);
        addComponent(saturationSlider);
        addComponent(brightnessSlider);
    }
    private SliderValueChangedListener<Interpolatingfloat> getSliderValueChangedListener(final SliderColorChangedListener listener){
        return new SliderValueChangedListener<Interpolatingfloat>() {
            @Override
            public void onValueChanged(Interpolatingfloat newValue) {
                if(listener != null&&!isInSetting()) {
                    int color=getRGB();
                    colorDisplayDiv.setColor(color);
                    listener.onValueChanged(color);
                }
            }
        };
    }
    public void selectRGB(int r, int g, int b) {
        float[] hsb=Color.RGBtoHSB(r,g,b,null);
        colorDisplayDiv.setColor(r<<16|g<<8|b);
        inSetting=true;
        hueSlider.setValue(new Interpolatingfloat(hsb[0]));
        saturationSlider.setValue(new Interpolatingfloat(hsb[1]));
        brightnessSlider.setValue(new Interpolatingfloat(hsb[2]));
        inSetting=false;
        if(listener!=null) {
            listener.onValueChanged(getRGB());
        }
    }
    public void selectRGB(int color) {
        int red   = (color >> 16) & 0xFF;
        int green = (color >> 8)  & 0xFF;
        int blue  = color & 0xFF;
        selectRGB(red,green,blue);
    }
    public int getRGB() {
        return Color.HSBtoRGB(
                hueSlider.getValue().getValue(),
                saturationSlider.getValue().getValue(),
                brightnessSlider.getValue().getValue());
    }
    public interface SliderColorChangedListener {
        void onValueChanged(int color);
    }
    private boolean isInSetting(){
        return inSetting;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        boolean ret=super.mouseClicked(mouseX, mouseY, mouseButton);
        setFocused(ret);
        return ret;
    }
}

