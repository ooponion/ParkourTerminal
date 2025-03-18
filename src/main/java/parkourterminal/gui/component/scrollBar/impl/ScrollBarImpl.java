package parkourterminal.gui.component.scrollBar.impl;

import parkourterminal.util.AnimationUtils.intf.AnimationMode;
import parkourterminal.util.AnimationUtils.impls.interpolatingData.FloatPoint;
import parkourterminal.util.AnimationUtils.impls.BeizerAnimation;
import parkourterminal.util.AnimationUtils.intf.AbstractAnimation;
import parkourterminal.util.ShapeDrawer;

public class ScrollBarImpl {
    private int x,y,width,height;
    private float oldScrollOffset=0f;
    private int min_height;
    private float scrollHeight;//当前滚动条大小(高度)
    private float contentHeight=0.0f;//内容高度
    private float contentOffset=0.0f;
    private float scrollOffset = 0.0f; // 当前滚动偏移量
    private float scrollTime=3f; // 控制滚动平滑时间
    private final AbstractAnimation<FloatPoint> animation=new BeizerAnimation<FloatPoint>(scrollTime,new FloatPoint(x,y), AnimationMode.BLENDED);;
    private boolean mouseOver=false;

    private boolean displayable=true;
    private double oldY=0;

    public ScrollBarImpl(int x,int y,int width,int height){
        if(x<0||y<0||width<=0||height<=0){
            System.out.print("Illegal args to setup this Scroll bar\n");
        }
        this.animation.changeWithOutAnimation(new FloatPoint(x,y));
        ChangeSize(x, y, width, height);
        this.min_height=10;
    }
    public ScrollBarImpl(int height){
        displayable=false;
        ChangeSize(0,0, 0, height);
        this.min_height=10;
    }
    public ScrollBarImpl(int x,int y,int width,int height,int min_height,float scrollTime){
        this(x, y, width, height);
        this.scrollTime=scrollTime;
        this.min_height=min_height;
    }
    public void ChangeSize(int x, int y, int width, int height){
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
        UpdateScrollVariables(contentHeight,scrollOffset,height);
    }
    public void SetScrollScreenHeight(int height){
        if(height<=0){
            throw new IllegalArgumentException("Illegal height to setup this Scroll bar");
        }
        this.height=height;
        UpdateScrollVariables(contentHeight,scrollOffset,height);
    }
    public void UpdateContentHeight(int contentHeight) {
        this.contentHeight = contentHeight;
        UpdateScrollVariables(contentHeight, scrollOffset, height);
    }
    public void drawScrollBar() {
        // 仅当内容总高度大于可见区域时才绘制滑动条
        if (contentHeight > height) {
            // 滑动条宽度固定为 4 像素，绘制在卡片区域右侧

            // 轨道使用透明颜色（本身只绘制边框，内部透明）
            int trackColor = 0x00000000; // 完全透明
            int cornerRadius = 2;

            // 绘制轨道（整个卡片区域高度）
            ShapeDrawer.drawRoundedRectBorder(x, y, width, height, trackColor, cornerRadius);

            // 定义拇指颜色为半透明白色
            int thumbColor = 0x40000000;

            // 绘制拇指
            ShapeDrawer.drawRoundedRect(x, animation.Update().getY(), width, scrollHeight, thumbColor, cornerRadius);
        }
    }

    private void CalculateScrollHeight(float contentHeight,int height){
        scrollHeight=  Math.max(Math.min(height /contentHeight,1.0f)*height,min_height);
    }
    private void CalculateContentOffset(float scrollOffset,float scrollHeight,float contentHeight,int height){//0,height-scrollHeight->0,contentHeight-height
        if(height<=scrollHeight){
            contentOffset=0;
            return;
        }
        contentOffset=  scrollOffset/(height-scrollHeight)*Math.max(contentHeight-height,0);
    }
    private void ValidateScrollOffset(float FakeOffset,float scrollHeight,int height){//FakeOffset可能是错误的,所以重新计算位置//0,height-scrollHeight
        oldScrollOffset=scrollOffset;
        scrollOffset= Math.max(0,Math.min(height-scrollHeight,FakeOffset));
    }
    private void UpdateScrollVariables(float contentHeight,float FakeOffset,int height){
        CalculateScrollHeight(contentHeight,height);
        ValidateScrollOffset(FakeOffset,scrollHeight,height);
        animation.RestartAnimation(new FloatPoint(x,y+scrollOffset));
        CalculateContentOffset(scrollOffset,scrollHeight,contentHeight,height);
    }
    public boolean ValidScrollClick(double mouseX, double mouseY){
        return (mouseX>x&&mouseX<x+width&&mouseY>y+scrollOffset&&mouseY<y+scrollOffset+scrollHeight);
    }
    public void onClick(double mouseX, double mouseY) {
        if(ValidScrollClick(mouseX, mouseY)) {
            mouseOver = true;
            oldY=mouseY;
        }
    }
    public void onRelease() {
        mouseOver=false;
        oldY=0;
    }
    public void scrollWheel(float amount){
        float fakeOffset=scrollOffset+ amount;
        UpdateScrollVariables(contentHeight,fakeOffset,height);
    }
    public void onDrag(float newY) {
        if(mouseOver){
            float fakeOffset= (float) (scrollOffset+ newY-oldY);
            UpdateScrollVariables(contentHeight,fakeOffset,height);
            oldY=newY;
        }
    }

    public float getContentOffset() {
        return contentOffset;
    }
    @Override
    public String toString(){

        return "height:"+this.height+",contentHeight:"+this.contentHeight+",scrollHeight:"+this.scrollHeight+",scrollOffset:"+this.scrollOffset+",contentOffset:"+this.contentOffset;
    }
}
