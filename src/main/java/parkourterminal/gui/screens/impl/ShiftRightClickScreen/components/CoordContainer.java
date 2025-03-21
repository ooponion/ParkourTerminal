package parkourterminal.gui.screens.impl.ShiftRightClickScreen.components;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import parkourterminal.gui.layout.*;
import parkourterminal.gui.screens.impl.CoordinateInfoGui;
import parkourterminal.util.ShapeDrawer;

public class CoordContainer extends Container {
    public CoordContainer(){
        super(new Margin(0,0,0,0),new Padding(25,20,25,20),new noWarpLinearLayout(LayoutDirection.VERTICAL,4));
    }
    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton){
        for (UIComponent component:getComponents()){
            CoordLine coordLine =(CoordLine) component;
            boolean openDetail=coordLine.mouseClicked(mouseX, mouseY, mouseButton);
            if(openDetail){
                Minecraft.getMinecraft().displayGuiScreen(new CoordinateInfoGui(coordLine.getNbt(), coordLine.getHeldItem()));
                return true;
            }
        }
        return false;
    }
    @Override
    public void mouseReleased(int mouseX, int mouseY){
        super.mouseReleased(mouseX, mouseY);
    }
    @Override
    public boolean mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick){
        if(clickedMouseButton!=0){
            return false;
        }

        return super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        // 绘制背景矩形
        float outerX = getEntryLeft()-20;
        float outerY = getEntryTop() - 10;  // 第一个条目的顶部
        float outerWidth = getEntryWidth()+40;
        float outerHeight = getEntryHeight()+10;

        ShapeDrawer.drawRoundedRect(outerX, outerY, outerWidth, outerHeight, 0x40FFFFFF, 2);
        super.draw(mouseX,mouseY,partialTicks);
    }
}
