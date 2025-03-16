package parkourterminal.gui.component.scrollBar.impl;

import parkourterminal.util.ShapeDrawer;

public class ScrollBarImpl {
    private int x,y,width,height;
    private int min_height;
    private float scrollHeight;//当前滚动条大小(高度)
    private float contentHeight=0.0f;//内容高度
    private float contentOffset=0.0f;
    private float scrollOffset = 0.0f; // 当前滚动偏移量
    private float scrollSpeed; // 控制滚动平滑的速度（越小越平滑，越大越快）
    private boolean mouseOver=false;
    private void ChangeSize(int x, int y, int width, int height){
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
        UpdateScrollVariables(contentHeight,scrollOffset,height);
    }
    public ScrollBarImpl(int x,int y,int width,int height){
        if(x<=0||y<=0||width<=0||height<=0){
            throw new IllegalArgumentException("Illegal args to setup this Scroll bar");
        }
        ChangeSize(x, y, width, height);
        this.scrollSpeed=0.4f;//default
        this.min_height=10;
    }
    public ScrollBarImpl(int x,int y,int width,int height,int min_height,float scrollSpeed){
        this(x, y, width, height);
        this.scrollSpeed=scrollSpeed;
        this.min_height=min_height;
    }
    public void SetScrollScreenHeight(int height){
        if(height<=0){
            throw new IllegalArgumentException("Illegal height to setup this Scroll bar");
        }
        this.height=height;
        UpdateScrollVariables(contentHeight,scrollOffset,height);
    }
    public void UpdateContentHeight(int contentHeight){
        this.contentHeight=contentHeight;
        UpdateScrollVariables(contentHeight,scrollOffset,height);
    }
    private void drawScrollBar() {
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
            ShapeDrawer.drawRoundedRect(x, scrollOffset, width, scrollHeight, thumbColor, cornerRadius);
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
        scrollOffset= Math.max(0,Math.min(height-scrollHeight,FakeOffset));
    }
    private void UpdateScrollVariables(float contentHeight,float FakeOffset,int height){
        CalculateScrollHeight(contentHeight,height);
        ValidateScrollOffset(FakeOffset,scrollHeight,height);
        CalculateContentOffset(scrollOffset,scrollHeight,contentHeight,height);
    }
    private boolean ValidScrollClick(double mouseX, double mouseY){
        return (mouseX>x&&mouseX<x+width&&mouseY>y+height&&mouseY<y+height+scrollHeight);
    }
    public void onClick(double mouseX, double mouseY) {
        if(ValidScrollClick(mouseX, mouseY)) {
            mouseOver = true;
        }
    }
    public void onRelease() {
        mouseOver=false;
    }
    public void onDrag(double deltaY) {
        if(mouseOver){
            float fakeOffset=scrollOffset+ (float) deltaY;
            UpdateScrollVariables(contentHeight,fakeOffset,height);
        }
    }
}
