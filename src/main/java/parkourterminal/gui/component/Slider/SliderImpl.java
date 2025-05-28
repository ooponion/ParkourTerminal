package parkourterminal.gui.component.Slider;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import parkourterminal.gui.component.scrollBar.intf.ScrollDirection;
import parkourterminal.gui.layout.UIComponent;
import parkourterminal.gui.layout.KeyTyped;
import parkourterminal.util.AnimationUtils.impls.BeizerAnimation;
import parkourterminal.util.AnimationUtils.impls.interpolatingData.InterpolatingColor;
import parkourterminal.util.AnimationUtils.impls.interpolatingData.Interpolatingfloat;
import parkourterminal.util.AnimationUtils.intf.AnimationMode;
import parkourterminal.util.AnimationUtils.intf.InterpolatingMath;
import parkourterminal.util.AnimationUtils.intf.interpolating;
import parkourterminal.util.NumberWrapper;
import parkourterminal.util.ParseHelper;
import parkourterminal.util.RenderTextHelper;
import parkourterminal.util.ShapeDrawer;

import java.awt.*;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;

public class SliderImpl<T extends interpolating<?,T>> extends UIComponent implements KeyTyped {
    private float thumbWidth=0;
    private float thumbHeight=0;
    private T min;
    private T max;
    private T value;
    private T step;
    private float progress=0;//[0,1]
    private final float trackHeight=4;
    private float trackWidth=0;
    private final int trackColor=0xdd8f999d;
    private final int trackProgressColor=0xdd7688ff;
    private final int trackProgressBorderColor=0xdd464855;
    private final int thumbColor=0xffc1d1d8;
    private final int thumbColorBorder=0xff6c6c6c;
    private final int thumbColorHovered=0xffa9d2e4;
    private final int thumbColorBorderHovered=0xffa4a4a4;
    private boolean showProgressTrack=true;
    private boolean showScales=true;
    private BeizerAnimation<Interpolatingfloat> animation = new BeizerAnimation<Interpolatingfloat>(0.5f,new Interpolatingfloat(0),AnimationMode.BLENDED);
    private BeizerAnimation<Interpolatingfloat> animationBorderRadius = new BeizerAnimation<Interpolatingfloat>(1.5f,new Interpolatingfloat(4),AnimationMode.BLENDED);
    private double oldPos =0;
    private boolean mouseOver=false;
    private final ScrollDirection direction=null;//之后加
    private Optional<SliderValueChangedListener<T>> listener=Optional.empty();
    private boolean displayValue=false;
    public static int displayDecimalPlace=2;

    public SliderImpl(T min, T max, T step,T initValue, float thumbWidth, int width, int height,int x,int y,SliderValueChangedListener<T> listener) {
        width=Math.max(width,8);
        height= (int) Math.max(height,trackHeight+2);

        thumbWidth= MathHelper.clamp_float(thumbWidth,6,width-2);

        setPosition(x, y);
        setSize(width, height);

        this.thumbWidth = thumbWidth;
        this.thumbHeight = height-2;
        this.trackWidth = width;
        this.min = min;
        this.max = max;
        this.step = step;
        this.value = initValue;
        setValue(initValue);
        if(listener != null){
            this.listener = Optional.of(listener);
        }
        activeListener(value);
        getAnimationColor().changeWithOutAnimation(new InterpolatingColor(thumbColor));
        setAnimationTime(1.5f);
    }
    public SliderImpl(T min, T max, T step, float thumbWidth, int width, int height,int x,int y,SliderValueChangedListener<T> listener){
        this(min,max,step,min,thumbWidth,width,height,x,y,listener);
    }
    public SliderImpl(T min, T max, T step, float thumbWidth, int width, int height,SliderValueChangedListener<T> listener){
        this(min,max,step,min,thumbWidth,width,height,0,0,listener);
    }
    public SliderImpl(T min, T max, T step,T initValue, float thumbWidth, int width, int height,SliderValueChangedListener<T> listener){
        this(min,max,step,initValue,thumbWidth,width,height,0,0,listener);
    }
    public SliderImpl(T min, T max, T step,T initValue, float thumbWidth, int width, int height){
        this(min,max,step,initValue,thumbWidth,width,height,0,0,null);
    }
    public void setMin(T min) {
        if(min.compare(max)>0){
            this.min = max;
            return;
        }
        this.min = min;
        validateValue(value);
    }
    public void setMax(T max) {
        if(max.compare(min)<0){
            this.max=min;
            return;
        }
        this.max = max;
        validateValue(value);
    }
    public void setListener( SliderValueChangedListener<T> listener) {
        this.listener = Optional.of(listener);
    }
    public void setStep(T step) {
        this.step = step;
    }

    public void setValue(T value){
        validateValue(value);
    }
    private void validateValue(T value1){
        T newValue=InterpolatingMath.clamp(value1,min,max);
        T oldValue=this.value;
        this.value=newValue;
        progress=InterpolatingMath.getProgress(min,max,value);
        animation.RestartAnimation(new Interpolatingfloat(this.progress));
        if(!newValue.equals(oldValue)){
            activeListener(newValue);
        }
    }
    private void activeListener(final T newValue){
        this.listener.ifPresent(new Consumer<SliderValueChangedListener<T>>() {
            @Override
            public void accept(SliderValueChangedListener<T> tSliderValueChangedListener) {
                tSliderValueChangedListener.onValueChanged(newValue);
            }
        });
    }
    public void setProgress(float progress){
        validateProgress(progress);
    }
    private void validateProgress(float progress1){
        this.progress=InterpolatingMath.getRoundedProgress(min,max,progress1,step);
        animation.RestartAnimation(new Interpolatingfloat(this.progress));
        T newValue=InterpolatingMath.getValue(min,max,this.progress);
        T oldValue=this.value;
        this.value=newValue;
        if(!newValue.equals(oldValue)){
            activeListener(newValue);
        }
    }
    public T getValue(){
        return this.value;
    }
    public boolean isShowProgressTrack() {
        return showProgressTrack;
    }
    public void setShowProgressTrack(boolean showProgressTrack) {
        this.showProgressTrack = showProgressTrack;
    }

    public boolean isShowScales() {
        return  showScales;
    }
    public void setShowScales(boolean showScales) {
        this.showScales = showScales;
    }
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if(!isEnabled()){
            return;
        }
        float progress=animation.Update().getValue();
        ShapeDrawer.drawRoundedRect(getX(),getY()+(getHeight()-trackHeight)/2.0f,trackWidth,trackHeight,trackColor,trackHeight/2f);
        if(showProgressTrack){
            ShapeDrawer.drawRoundedRect(getX(),getY()+(getHeight()-trackHeight-2)/2.0f,getThumbX(progress)-getThumbX(0)+5,trackHeight+2,trackProgressColor,(trackHeight+2)/2f);
            ShapeDrawer.drawRoundedRectBorder(getX(),getY()+(getHeight()-trackHeight-2)/2.0f,getThumbX(progress)-getThumbX(0)+5,trackHeight+2,trackProgressBorderColor,(trackHeight+2)/2f);
        }
        float thumbX=getThumbX(progress);
        boolean hovered=isMouseOver(mouseX,mouseY);
        if(mouseOver){
            getAnimationColor().RestartAnimation(new InterpolatingColor(thumbColorHovered));
        }else{
            getAnimationColor().RestartAnimation(new InterpolatingColor(thumbColor));
        }
        if(hovered){
            animationBorderRadius.RestartAnimation(new Interpolatingfloat(Math.min(thumbHeight,thumbWidth)/3f));
        }else{
            animationBorderRadius.RestartAnimation(new Interpolatingfloat(Math.min(thumbHeight,thumbWidth)/4f));
        }

        if(showScales){
            drawScale();
        }

        float radius=animationBorderRadius.Update().getValue();
        ShapeDrawer.drawRoundedRect(thumbX,getY()+1,thumbWidth,thumbHeight,getAnimationColor().Update().getColor(), radius);
        ShapeDrawer.drawRoundedRectBorder(thumbX,getY()+1,thumbWidth,thumbHeight,isMouseOverThumb(mouseX,mouseY)?thumbColorBorderHovered:thumbColorBorder, radius);
        ShapeDrawer.drawLine(thumbX+3,getY()+1+thumbHeight/2f,thumbX+thumbWidth-3,getY()+1+thumbHeight/2f,hovered?thumbColorBorderHovered:thumbColorBorder,1f);
        if(displayValue){
            RenderTextHelper.drawCenteredString(Minecraft.getMinecraft().fontRendererObj, NumberWrapper.toFormattedFloat(value.getSize(),"%."+displayDecimalPlace+"f"),thumbX,getY(),thumbWidth,thumbHeight,0xFFFFFFFF,false);
        }
    }
    private float getThumbX(float progress){
        return getX()+1+progress*(getWidth()-2-thumbWidth);
    }

    public boolean isMouseOverThumb(int mouseX, int mouseY){
        Rectangle rect = new Rectangle((int) getThumbX(progress),getY()+1, (int) thumbWidth, (int) thumbHeight);
        return rect.contains(mouseX,mouseY);
    }
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton){
        boolean contains=isMouseOver(mouseX,mouseY);
        boolean overThumb = isMouseOverThumb(mouseX,mouseY);
        if(overThumb&&isEnabled()){
            setFocused(true);
            mouseOver=true;
            oldPos =mouseX-(getThumbX(progress)-getThumbX(0));
        }else if(contains&&!overThumb&&isEnabled()&&Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
                float fakeProgress= calculateProgress(mouseX- getThumbX(0)-thumbWidth/2f);
                setProgress(fakeProgress);
                setFocused(true);
                return true;
        }
        setFocused(contains);
        return contains;
    }
    public void mouseReleased(int mouseX, int mouseY,int state){
        mouseOver=false;
        oldPos =0;
    }
    public boolean mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick){
        if(mouseOver&&isEnabled()){
            float fakeProgress= calculateProgress((float) (mouseX- oldPos));
            setProgress(fakeProgress);
        }else{
            mouseReleased(mouseX,mouseY,clickedMouseButton);
        }
        return false;
    }
    private float calculateProgress(float offset){
        float trackWidth=Math.max(getThumbX(1)-getThumbX(0),1);
        return MathHelper.clamp_float(offset/trackWidth,0,1);
    }
    private void drawScale(){
        float scaleWholeWidth=getThumbX(1)-getThumbX(0);
        float stepDistance=step.getSize();
        float holistic=Math.max(min.distance(max),1);
        float progressDistance=0;
        while (progressDistance<holistic){
            drawOneScale(progressDistance/holistic*scaleWholeWidth);
            progressDistance+=stepDistance;
        }
        drawOneScale(scaleWholeWidth);
    }
    private void drawOneScale(float progressDistance){
        float scaleX=progressDistance+getThumbX(0)+thumbWidth/2f;
        ShapeDrawer.drawLine(scaleX,getY()+getHeight()/2f-5,scaleX,getY()+getHeight()/2f+1,thumbColorBorder,1f);
    }
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        if(!isFocused()){
            return;
        }
        boolean shiftDown=Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
        if (keyCode == Keyboard.KEY_A||keyCode == Keyboard.KEY_LEFT) {
            if(shiftDown){
                setValue(min);
            }else{
                if(value.equals(max)){
                    float stepDistance=step.getSize();

                    int stepNumber= (int) Math.ceil(value.subtract(step).distance(min)/stepDistance);
                    setValue(min.add(step.multiply(stepNumber)));
                }else{
                    setValue(value.subtract(step));
                }

            }
        }else if (keyCode == Keyboard.KEY_D||keyCode == Keyboard.KEY_RIGHT) {
            if(shiftDown){
                setValue(max);
            }else{
                setValue(value.add(step));
            }
        }
    }
    public void setDisplayValue(boolean displayValue){
        this.displayValue=displayValue;
    }
    public boolean isDisplayValue(){
        return displayValue;
    }
}
