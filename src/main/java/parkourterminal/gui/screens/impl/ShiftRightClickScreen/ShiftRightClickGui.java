package parkourterminal.gui.screens.impl.ShiftRightClickScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Mouse;
import parkourterminal.gui.component.scrollBar.impl.ScrollBarImpl;
import parkourterminal.gui.component.scrollBar.intf.ScrollDirection;
import parkourterminal.gui.layout.*;
import parkourterminal.gui.screens.impl.CoordinateInfoGui;
import parkourterminal.gui.screens.impl.ShiftRightClickScreen.components.CoordContainer;
import parkourterminal.gui.screens.impl.ShiftRightClickScreen.components.CoordLine;
import parkourterminal.gui.screens.intf.BlurGui;
import parkourterminal.gui.component.ConsolaFontRenderer;
import parkourterminal.util.AnimationUtils.impls.BeizerAnimation;
import parkourterminal.util.AnimationUtils.impls.interpolatingData.Interpolatingfloat;
import parkourterminal.util.AnimationUtils.intf.AbstractAnimation;
import parkourterminal.util.AnimationUtils.intf.AnimationMode;
import parkourterminal.util.NumberWrapper;
import parkourterminal.util.ShapeDrawer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
public class ShiftRightClickGui extends BlurGui {
    private ScrollBarImpl scrollBar;
    private ItemStack LastHeldItem;
    private CoordContainer coordLineContainer = new CoordContainer();
    @Override
    public void initGui() {
        super.initGui();
        coordLineContainer.SetSize(width,height);
        scrollBar=new ScrollBarImpl(0,height - 40, ScrollDirection.VERTICAL);
        fontRendererObj = new ConsolaFontRenderer(Minecraft.getMinecraft());
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        int selectedIndex = -1;
        scrollBar.Update();
        // 计算平滑滚动

        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        ItemStack heldItem = player.getHeldItem();
        // NBT验证
        if (heldItem != null && heldItem.hasTagCompound()) {
            NBTTagCompound nbt = heldItem.getTagCompound();
            if (nbt.hasKey("selectedIndex"))
                selectedIndex = nbt.getInteger("selectedIndex");
            if (nbt.hasKey("savedLocations")) {
                NBTTagList savedLocations = nbt.getTagList("savedLocations", 10);
                // 添加到container
                if(!heldItem.equals( LastHeldItem)){
                    coordLineContainer.Clear();
                    for (int i = 0; i < savedLocations.tagCount(); i++) {
                        NBTTagCompound location = savedLocations.getCompoundTagAt(i);
                        coordLineContainer.addComponent(new CoordLine(location,width-50,fontRendererObj,heldItem));
                    }
                }
                //更新属性
                for (int i = 0; i < savedLocations.tagCount(); i++) {
                    CoordLine coordLine=(CoordLine) coordLineContainer.getComponents().get(i);
                    coordLine.SetSize(width-50,0);
                    coordLine.UpdateSelect(i == selectedIndex);
                }
                // 计算所有条目的总高度
                int totalHeight = coordLineContainer.getComponentsTotalHeight();
                scrollBar.UpdateContentSize(totalHeight);

                // 可视区域起始Y坐标
                coordLineContainer.setY((int) -scrollBar.getInterpolatingContentOffset());
                coordLineContainer.setHeight(totalHeight+coordLineContainer.getPadding().bottom+coordLineContainer.getPadding().top+10);
                coordLineContainer.draw(mouseX,mouseY,partialTicks);
            }
            LastHeldItem=heldItem;
        }else{
            coordLineContainer.Clear();
        }
    }
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        int dWheel = Mouse.getDWheel(); // 获取鼠标滚轮滚动值
        if (dWheel != 0) {
            scrollBar.scrollWheel(dWheel > 0 ? -40 : 40);
        }
    }
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if(!coordLineContainer.getComponents().isEmpty()){
            coordLineContainer.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }
    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        coordLineContainer.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        coordLineContainer.mouseReleased(mouseX, mouseY);
    }
}