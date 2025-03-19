package parkourterminal.gui.component.scrollBar.impl;

import parkourterminal.gui.component.scrollBar.intf.ScrollDirection;
import parkourterminal.util.AnimationUtils.impls.interpolatingData.Interpolatingfloat;
import parkourterminal.util.AnimationUtils.intf.AnimationMode;
import parkourterminal.util.AnimationUtils.impls.BeizerAnimation;
import parkourterminal.util.AnimationUtils.intf.AbstractAnimation;
import parkourterminal.util.ShapeDrawer;

public class ScrollBarImpl {
    private int x,y,width,height;
    private final ScrollDirection direction;

    private int min_height;
    private float scrollSize;//当前滚动条大小(高度/宽度)
    private float contentSize =1.0f;//内容高度/宽度
    private float contentOffset=0.0f;
    private float scrollOffset = 0.0f; // 当前滚动偏移量
    private float scrollTime=3f; // 控制滚动平滑时间
    private final AbstractAnimation<Interpolatingfloat> animation=new BeizerAnimation<Interpolatingfloat>(scrollTime,new Interpolatingfloat(0), AnimationMode.BLENDED);;

    private final AbstractAnimation<Interpolatingfloat> animationContentOffset=new BeizerAnimation<Interpolatingfloat>(scrollTime,new Interpolatingfloat(0), AnimationMode.BLENDED);;

    private boolean mouseOver=false;

    private boolean displayable=true;
    private double oldPos =0;

    public ScrollBarImpl(int x,int y,int width,int height,ScrollDirection direction){
        if(x<0||y<0||width<=0||height<=0){
            System.out.print("Illegal args to setup this Scroll bar\n");
        }
        this.animation.changeWithOutAnimation(new Interpolatingfloat(direction==ScrollDirection.VERTICAL?x:y));
        ChangeSize(width, height);
        ChangePosition(x, y);
        this.min_height=10;
        this.direction=direction;
    }
    public ScrollBarImpl(int width,int height,ScrollDirection direction) {
        displayable = false;
        ChangeSize(width, height);
        ChangePosition(0, 0);
        this.min_height = 10;
        this.direction = direction;
    }
    public void ChangeSize( int width, int height){

        this.width=width;
        this.height=height;
        if(direction==ScrollDirection.VERTICAL){
            UpdateScrollVariables(contentSize,scrollOffset,height);
        }
        else{
            UpdateScrollVariables(contentSize,scrollOffset,width);
        }
    }
    public void ChangePosition( int x,int y){
        this.x=x;
        this.y=y;
        this.animation.changeWithOutAnimation(new Interpolatingfloat(direction==ScrollDirection.VERTICAL?x:y));
    }
    public void UpdateContentSize(int contentSize) {
        if(contentSize==0){
            contentSize=1;
        }
        this.contentSize = contentSize;
        if(direction==ScrollDirection.VERTICAL){
            UpdateScrollVariables(contentSize, scrollOffset, height);
        }
        else{
            UpdateScrollVariables(contentSize, scrollOffset, width);
        }

    }
    public void draw(int trackColor, int thumbColor) {
        // 仅当内容总高度大于可见区域时才绘制滑动条
        if (direction==ScrollDirection.VERTICAL&&contentSize > height&&displayable) {
            int cornerRadius = 2;

            // 绘制轨道（整个卡片区域高度）
            ShapeDrawer.drawRoundedRectBorder(x, y, width, height, trackColor, cornerRadius);

            // 绘制拇指
            ShapeDrawer.drawRoundedRect(x, animation.Update().getValue(), width, scrollSize, thumbColor, cornerRadius);
        }else if (contentSize > width&&displayable) {
            int cornerRadius = 2;

            // 绘制轨道（整个卡片区域高度）
            ShapeDrawer.drawRoundedRectBorder(x, y, width, height, trackColor, cornerRadius);

            // 绘制拇指
            ShapeDrawer.drawRoundedRect( animation.Update().getValue(),y, scrollSize, height, thumbColor, cornerRadius);
        }
    }

    private void CalculateScrollSize(float contentHeight, int size){

        scrollSize =  Math.max(Math.min(size /contentHeight,1.0f)*size,min_height);
    }
    private void CalculateContentOffset(float scrollOffset,float scrollSize,float contentSize,int size){//0,height-scrollHeight->0,contentHeight-height
        if(size<=scrollSize){
            contentOffset=0;
            return;
        }
        contentOffset=  scrollOffset/(size-scrollSize)*Math.max(contentSize-size,0);
        animationContentOffset.RestartAnimation(new Interpolatingfloat(contentOffset));

    }
    private void ValidateScrollOffset(float FakeOffset,float scrollSize,int size){//FakeOffset可能是错误的,所以重新计算位置//0,height-scrollHeight
        scrollOffset= Math.max(0,Math.min(size-scrollSize,FakeOffset));
    }
    private void UpdateScrollVariables(float contentSize,float FakeOffset,int size){
        CalculateScrollSize(contentSize,size);
        ValidateScrollOffset(FakeOffset, scrollSize,size);
        if(direction==ScrollDirection.VERTICAL){
            animation.RestartAnimation(new Interpolatingfloat(y+scrollOffset));
        }
        else{
            animation.RestartAnimation(new Interpolatingfloat(x+scrollOffset));
        }
        CalculateContentOffset(scrollOffset, scrollSize,contentSize,size);
    }
    public boolean ValidScrollClick(double mouseX, double mouseY){
        if(direction==ScrollDirection.VERTICAL){
            return (mouseX>x&&mouseX<x+width&&mouseY>y+scrollOffset&&mouseY<y+scrollOffset+ scrollSize);
        }
        else{
            return (mouseX>x+scrollOffset&&mouseX<x+scrollSize+scrollOffset&&mouseY>y&&mouseY<y+ height);
        }
    }
    public void onClick(double mouseX, double mouseY) {
        System.out.printf("onclick:%s\n",ValidScrollClick(mouseX, mouseY));
        if(ValidScrollClick(mouseX, mouseY)&&displayable) {
            mouseOver = true;

            if(direction==ScrollDirection.VERTICAL){
                oldPos =mouseY;
            }
            else {
                oldPos = mouseX;
            }
        }
    }
    public void onRelease() {
        mouseOver=false;
        oldPos =0;
    }
    public void scrollWheel(float amount){
        float fakeOffset=scrollOffset+ amount;
        if(direction==ScrollDirection.VERTICAL){
            UpdateScrollVariables(contentSize,fakeOffset,height);
        }
        else{
            UpdateScrollVariables(contentSize,fakeOffset,width);
        }
    }
    public void onDrag(float newPos) {
        if(mouseOver){
            float fakeOffset= (float) (scrollOffset+ newPos- oldPos);
            if(direction==ScrollDirection.VERTICAL){
                UpdateScrollVariables(contentSize,fakeOffset,height);
            }
            else{
                UpdateScrollVariables(contentSize,fakeOffset,width);
            }
            oldPos =newPos;
        }
    }
    public void Update(){
        animationContentOffset.Update();
    }
    public float getContentOffset() {
        return contentOffset;
    }
    public float getInterpolatingContentOffset() {
        return animationContentOffset.getInterpolatingValue().getValue();
    }
    public void setScrollTime(float scrollTime){
        this.scrollTime=scrollTime;
    }
    @Override
    public String toString(){

        return "height:"+this.height+",contentHeight:"+this.contentSize +",scrollHeight:"+this.scrollSize +",scrollOffset:"+this.scrollOffset+",contentOffset:"+this.contentOffset;
    }
}
