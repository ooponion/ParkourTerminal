package parkourterminal.gui.layout;

import parkourterminal.util.AnimationUtils.impls.BeizerAnimation;
import parkourterminal.util.AnimationUtils.impls.ColorInterpolateAnimation;
import parkourterminal.util.AnimationUtils.impls.interpolatingData.FloatPoint;
import parkourterminal.util.AnimationUtils.impls.interpolatingData.InterpolatingColor;
import parkourterminal.util.AnimationUtils.intf.AbstractAnimation;
import parkourterminal.util.AnimationUtils.intf.AnimationMode;

public abstract class UIComponent {
    private boolean isFocused=false;
    private boolean enabled=true;
    private int x=0, y=0, width=0, height=0;
    private Margin margin = new Margin(0);
    private Padding padding =new Padding(0);
    private AbstractAnimation<FloatPoint> animation=new BeizerAnimation<FloatPoint>(0.5f,new FloatPoint(x,y), AnimationMode.BLENDED);
    private AbstractAnimation<InterpolatingColor> animationColor=new ColorInterpolateAnimation(0.5f ,new InterpolatingColor(0),AnimationMode.BLENDED);

    public abstract void draw(int mouseX, int mouseY, float partialTicks);

    public boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= getX() && mouseX <= getX() + getWidth() &&
                mouseY >= getY() && mouseY <= getY() + getHeight();
    }

    public void setAnimationTime(float time){
        animation.SetAnimationTime(time);
        animationColor.SetAnimationTime(time);
    }
    public boolean isFocused(){
        return this.isFocused;
    }

    public void setFocused(boolean focused) {
        isFocused = focused;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled){
        this.enabled=enabled;
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    public int getEntryHeight(){
        return this.height-padding.bottom- padding.top;
    }
    public int getEntryWidth(){
        return this.width-padding.left- padding.right;
    }
    public int getEntryLeft(){
        return this.x+this.padding.left;
    }
    public int getEntryRight(){
        return this.x+this.width-padding.right;
    }
    public int getEntryTop(){
        return this.y+this.padding.top;
    }
    public int getEntryBottom(){
        return this.y+this.height-padding.bottom;
    }


    public int getOuterHeight(){
        return this.height+margin.bottom+ margin.top;
    }
    public int getOuterWidth(){
        return this.width+margin.left+ margin.right;
    }
    public int getOuterLeft(){
        return this.x-this.margin.left;
    }
    public int getOuterRight(){
        return this.x+this.width+margin.right;
    }
    public int getOuterTop(){
        return this.y-this.margin.top;
    }
    public int getOuterBottom(){
        return this.y+this.height+margin.bottom;
    }



    public Margin getMargin() {
        return margin;
    }

    public void setMargin(Margin margin) {
        this.margin = margin;
    }

    public void setPadding(Padding padding) {
        this.padding = padding;
    }

    public Padding getPadding() {
        return padding;
    }

    public void setPosition(int x, int y){
        this.x=x;
        this.y=y;
    }
    public void setSize(int width, int height){
        this.width=width;
        this.height=height;
    }
    public AbstractAnimation<FloatPoint> getAnimation() {
        return animation;
    }

    public AbstractAnimation<InterpolatingColor> getAnimationColor() {
        return animationColor;
    }
    public void setAnimationColor(AbstractAnimation<InterpolatingColor> animationColor){
        this.animationColor=animationColor;
    }
    public void setAnimation(AbstractAnimation<FloatPoint> animation) {
        this.animation = animation;
    }
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton){
        return false;
    }
    public void mouseReleased(int mouseX, int mouseY,int state){

    }
    public boolean mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick){
        return false;
    }
    public void Update(){};
}