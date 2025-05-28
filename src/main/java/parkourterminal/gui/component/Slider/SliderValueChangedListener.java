package parkourterminal.gui.component.Slider;

import parkourterminal.util.AnimationUtils.intf.interpolating;

public interface SliderValueChangedListener<T extends interpolating<?,T>> {
    void onValueChanged(T newValue);
}
