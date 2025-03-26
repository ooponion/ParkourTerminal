package parkourterminal.gui.screens.impl.ShiftRightClickScreen.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import parkourterminal.gui.component.scrollBar.impl.ScrollBarImpl;
import parkourterminal.gui.component.scrollBar.intf.ScrollDirection;
import parkourterminal.gui.layout.Margin;
import parkourterminal.gui.layout.Padding;
import parkourterminal.gui.layout.UIComponent;
import parkourterminal.gui.screens.intf.instantiationScreen.manager.ScreenManager;
import parkourterminal.util.AnimationUtils.impls.ColorInterpolateAnimation;
import parkourterminal.util.AnimationUtils.impls.interpolatingData.InterpolatingColor;
import parkourterminal.util.AnimationUtils.intf.AnimationMode;
import parkourterminal.util.NumberWrapper;
import parkourterminal.util.ScissorHelper;
import parkourterminal.util.ShapeDrawer;

public class CoordLine extends UIComponent {
    private final NBTTagCompound location;
    private final ItemStack heldItem;
    private boolean selected=false;
    private final ScrollBarImpl scrollBar;
    private final int startColor = 0x80000000; // 半透明黑色
    private final int endColor = 0x600099FF;   // 半透明淡蓝色
    private final int borderColor = 0xFFFF0000; // 红色边框颜色
    private final int textHeight =  Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;
    private final int entryExtraPadding = 5;
    private final int scrollBarHeight = 4; // 仅当需要横向滚动时的滚动条高度
    private final FontRenderer fontRendererObj;
    public CoordLine(NBTTagCompound location, int width, FontRenderer fontRendererObj,ItemStack heldItem){
        this.heldItem=heldItem;
        this.location=location;
        this.fontRendererObj = fontRendererObj;
        this.setMargin(new Margin(10,0,10,0));
        this.setPadding(new Padding(0,5,0,0));
        scrollBar=new ScrollBarImpl(0,0,getWidth(),scrollBarHeight, ScrollDirection.HORIZONTAL);
        scrollBar.setColor(0x00000000,0xFFAAAAAA);
        setSize(width,0);
        this.setAnimationColor(new ColorInterpolateAnimation(0.4f,new InterpolatingColor(startColor), AnimationMode.BLENDED));
    }
    public void UpdateSelect(boolean selected){
            this.selected=selected;
    }
    @Override
    public void setSize(int width, int height){
        this.setWidth(width);
        int boxHeight = hasScrollbar()
                ? (textHeight + entryExtraPadding + scrollBarHeight)
                : (textHeight + entryExtraPadding);
        scrollBar.setSize(getEntryWidth(),scrollBarHeight);
        scrollBar.UpdateContentSize(fontRendererObj.getStringWidth(getDisplayText()));
        this.setHeight(boxHeight);
    }
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        boolean isHovered = isMouseOver(mouseX,mouseY);
        if (isHovered){
            getAnimationColor().RestartAnimation(new InterpolatingColor(endColor));
        }else{
            getAnimationColor().RestartAnimation(new InterpolatingColor(startColor));
        }
        // 绘制背景
        ShapeDrawer.drawRoundedRect(getOuterLeft(),getOuterTop(), getOuterWidth(), getOuterHeight(), getAnimationColor().Update().getColor(), 3);
        // 裁剪区域
        ScissorHelper.EnableScissor(getOuterLeft(),getOuterTop(), getOuterWidth(), getOuterHeight()+1);
        scrollBar.Update();
        fontRendererObj.drawStringWithShadow(getDisplayText(), getEntryLeft()-scrollBar.getInterpolatingContentOffset(), getEntryTop(), 0xFFFFFF);
        scrollBar.draw(mouseX, mouseY, partialTicks);
        //fontRendererObj.drawString(scrollBar.toString(),getOuterLeft(),getOuterTop(),0xFFFFFFFF);
        ScissorHelper.DisableScissor();
        if(selected) {
            ShapeDrawer.drawRoundedRectBorder(getOuterLeft(), getOuterTop(), getOuterWidth(), getOuterHeight(), borderColor, 3);
        }
    }
    @Override
    public void setX(int x) {
        super.setX(x);
        scrollBar.setPosition(x,getY()+textHeight + entryExtraPadding);
    }
    @Override
    public void setY(int y) {
        super.setY(y);
        scrollBar.setPosition(getX(),y+textHeight + entryExtraPadding);
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
    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton){
        if (isMouseOver(mouseX, mouseY)) {
            getScrollBar().onClick(mouseX, mouseY);
            if(!getScrollBar().isMouseOver(mouseX, mouseY)){
                ScreenManager.SwitchToScreen(new CoordinateInfoGui(getNbt(), getHeldItem()));
                return true;
            }
        }
        return false;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY,int state){
        getScrollBar().onRelease();
    }
    @Override
    public boolean mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick){
        if(isMouseOver(mouseX,mouseY)){
            getScrollBar().onDrag(mouseX);
            return true;
        }
        return false;
    }
    public ScrollBarImpl getScrollBar(){
        return scrollBar;
    }
    public NBTTagCompound getNbt(){
        return location;
    }
    public ItemStack getHeldItem(){
        return heldItem;
    }
}
