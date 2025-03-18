package parkourterminal.gui.screens.impl.ShiftRightClickScreen.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import parkourterminal.gui.component.scrollBar.impl.ScrollBarImpl;
import parkourterminal.gui.component.scrollBar.intf.ScrollDirection;
import parkourterminal.gui.layout.Margin;
import parkourterminal.gui.layout.Padding;
import parkourterminal.gui.layout.UIComponent;
import parkourterminal.util.AnimationUtils.impls.ColorInterpolateAnimation;
import parkourterminal.util.AnimationUtils.impls.interpolatingData.FloatPoint;
import parkourterminal.util.AnimationUtils.impls.interpolatingData.InterpolatingColor;
import parkourterminal.util.AnimationUtils.intf.AnimationMode;
import parkourterminal.util.NumberWrapper;
import parkourterminal.util.ScissorHelper;
import parkourterminal.util.ShapeDrawer;

public class CoordLine extends UIComponent {
    private NBTTagCompound location;
    private final ScrollBarImpl scrollBar;
    int startColor = 0x80000000; // 半透明黑色
    int endColor = 0x600099FF;   // 半透明淡蓝色
    private final int textHeight =  Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;
    private final int entryExtraPadding = 5;
    private final int scrollBarHeight = 4; // 仅当需要横向滚动时的滚动条高度
    private final FontRenderer fontRendererObj;
    public CoordLine(NBTTagCompound location, int width, FontRenderer fontRendererObj){
        this.location=location;
        this.fontRendererObj = fontRendererObj;
        this.setMargin(new Margin(10,0,10,0));
        this.setPadding(new Padding(0,5,0,0));
        SetSize(width,0);
        scrollBar=new ScrollBarImpl(0,0,getWidth(),scrollBarHeight, ScrollDirection.HORIZONTAL);
        scrollBar.UpdateContentSize(fontRendererObj.getStringWidth(getDisplayText()));
        this.setAnimationColor(new ColorInterpolateAnimation(0.4f,new InterpolatingColor(startColor), AnimationMode.BLENDED));
    }
    @Override
    public void SetSize(int width, int _){
        this.setWidth(width);
        int boxHeight = hasScrollbar()
                ? (textHeight + entryExtraPadding + scrollBarHeight)
                : (textHeight + entryExtraPadding);
        this.setHeight(boxHeight);
    }
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {


                    // 绘制背景（圆角矩形）

                    // 当前条目的Y坐标 = 当前累计Y - scrollOffset


                    boolean isHovered = isMouseOver(mouseX,mouseY);

                    if (isHovered){
                        getAnimationColor().RestartAnimation(new InterpolatingColor(endColor));
                    }else{
                        getAnimationColor().RestartAnimation(new InterpolatingColor(startColor));
                    }


                    // 绘制背景（应用插值颜色）

                    ShapeDrawer.drawRoundedRect(getOuterLeft(),getOuterTop(), getOuterWidth(), getOuterHeight(), getAnimationColor().Update().getColor(), 3);

                    // 裁剪区域
                    ScissorHelper.EnableScissor(getOuterLeft(),getOuterTop(), getOuterWidth(), getOuterHeight()+1);

                    // 绘制文本，从boxX+10处开始，考虑横向滚动偏移
                    //float textX = boxX + 10 - horizOffset;
                    fontRendererObj.drawStringWithShadow(getDisplayText(), getEntryLeft(), getEntryTop(), 0xFFFFFF);

                    // 如果需要横向滚动，则绘制滚动条
                    scrollBar.drawScrollBar();
//                    if (hasScrollbar) {
//                        float scrollBarX = boxX + 10;
//                        float scrollBarY = boxY + textHeight + entryExtraPadding;
//                        float scrollBarWidth = visibleWidth;
//
//                        // 检测鼠标是否悬停在滚动条轨道上
//                        boolean isScrollBarHovered =
//                                mouseX >= scrollBarX && mouseX <= scrollBarX + scrollBarWidth &&
//                                        mouseY >= scrollBarY && mouseY <= scrollBarY + scrollBarHeight;
//
//                        if (isScrollBarHovered)
//                            hoveredScrollBarEntry = i;
//
//                        // 获取或初始化动画进度
//                        float progress2 = scrollBarHoverProgress.containsKey(i) ?
//                                scrollBarHoverProgress.get(i) : 0.0f;
//                        float targetProgress2 = (hoveredScrollBarEntry == i) ? 1.0f : 0.0f;
//                        progress2 += (targetProgress2 - progress2) * 0.2f; // 平滑过渡
//                        scrollBarHoverProgress.put(i, progress2);
//
//                        // 定义滚动条颜色插值
//                        int thumbStartColor = 0xFFAAAAAA;   // 拇指默认颜色
//                        int thumbEndColor = 0xFFCCCCCC;     // 悬停时拇指颜色
//
//                        //int currentThumbColor = AnimationHelper.interpolateColor(thumbStartColor, thumbEndColor, progress2);
//                        int currentThumbColor =thumbStartColor;
//
//                        // 计算滚动条拇指宽度，按比例确定，最小值20像素
//                        float thumbWidth = Math.max(20, scrollBarWidth * visibleWidth / fullTextWidth);
//                        float thumbMaxMovement = scrollBarWidth - thumbWidth;
//                        float thumbX = scrollBarX + (maxHorizScroll == 0 ? 0 : (int) ((float) horizOffset / maxHorizScroll * thumbMaxMovement));
//
//                        // 绘制滚动条拇指（应用插值颜色）
//                        ShapeDrawer.drawRoundedRect(thumbX, scrollBarY, thumbWidth, scrollBarHeight, currentThumbColor, 2);
//                    }

                    ScissorHelper.DisableScissor();
//                    if (i == selectedIndex) {
//                        int borderColor = 0xFFFF0000; // 红色边框颜色
//                        ShapeDrawer.drawRoundedRectBorder(
//                                boxX, boxY, // 边框的起始位置
//                                boxWidth, boxHeight, // 边框的宽度和高度
//                                borderColor, 3// 边框颜色和圆角半径
//                        );
//                    }










                    // 累加当前Y：本条目的高度加上条目间距（最后一个条目不加padding）
//                    currentY += boxHeight;
//                    if (i < savedLocations.tagCount() - 1) {
//                        currentY += padding;
//                    }

    }
    @Override
    public void setX(int x) {
        super.setX(x);
        scrollBar.ChangePosition(x,getY()+textHeight + entryExtraPadding);
    }
    @Override
    public void setY(int y) {
        super.setY(y);
        scrollBar.ChangePosition(getX(),y+textHeight + entryExtraPadding);
    }

    public boolean hasScrollbar(){
        String text =getDisplayText();
        int fullTextWidth = fontRendererObj.getStringWidth(text);
        return fullTextWidth > this.getWidth();
    }
    public String getDisplayText(){
        String name = location.getString("name");
        String posText = String.format(
                "X: %s, Y: %s, Z: %s, Yaw: %s, Pitch: %s",
                NumberWrapper.round(location.getDouble("posX")),
                NumberWrapper.round(location.getDouble("posY")),
                NumberWrapper.round(location.getDouble("posZ")),
                NumberWrapper.round(location.getFloat("yaw")),
                NumberWrapper.round(location.getFloat("pitch"))
        );
        return EnumChatFormatting.GOLD + name + " " + EnumChatFormatting.AQUA + posText;
    }
    @Override
    public boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= getOuterLeft() && mouseX <= getOuterRight() &&
                mouseY >= getOuterTop() && mouseY <= getOuterBottom();
    }
    public ScrollBarImpl getScrollBar(){
        return scrollBar;
    }
}
