package parkourterminal.gui.screens.impl.ShiftRightClickScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.lwjgl.input.Mouse;
import parkourterminal.gui.component.scrollBar.impl.ScrollBarImpl;
import parkourterminal.gui.component.scrollBar.intf.ScrollDirection;
import parkourterminal.gui.screens.impl.ShiftRightClickScreen.components.CoordContainer;
import parkourterminal.gui.screens.impl.ShiftRightClickScreen.components.CoordLine;
import parkourterminal.gui.screens.intf.BlurGui;
import parkourterminal.gui.component.ConsolaFontRenderer;
import parkourterminal.gui.screens.intf.instantiationScreen.intf.InstantiationScreen;
import parkourterminal.gui.screens.intf.instantiationScreen.intf.ScreenID;

import java.io.IOException;

public class ShiftRightClickGui extends BlurGui implements InstantiationScreen {
    private final ScrollBarImpl scrollBar=new ScrollBarImpl(1,1, ScrollDirection.VERTICAL);
    private int LastHeldItemID=-2;
    private CoordContainer coordLineContainer = new CoordContainer();
    @Override
    public void initGui() {
        super.initGui();
        coordLineContainer.setSize(width,height);
        scrollBar.setHeight(height - 40);
        fontRendererObj = new ConsolaFontRenderer(Minecraft.getMinecraft());
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        int id=-1;
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
            if(nbt.hasKey("ItemStackId")){
                id=nbt.getInteger("ItemStackId");
            }
            if (nbt.hasKey("savedLocations")) {
                NBTTagList savedLocations = nbt.getTagList("savedLocations", 10);
                // 添加到container

                if(id!=LastHeldItemID){
                    coordLineContainer.Clear();
                    for (int i = 0; i < savedLocations.tagCount(); i++) {
                        NBTTagCompound location = savedLocations.getCompoundTagAt(i);
                        coordLineContainer.addComponent(new CoordLine(location,width-50,fontRendererObj,heldItem));
                    }
                    scrollBar.ResetOffset();
                }
                //更新属性
                for (int i = 0; i < savedLocations.tagCount(); i++) {
                    CoordLine coordLine=(CoordLine) coordLineContainer.getComponents().get(i);
                    coordLine.setSize(width-50,0);
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
            LastHeldItemID=id;
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
        coordLineContainer.mouseReleased(mouseX, mouseY,state);
    }

    @Override
    public GuiScreen getScreenInstantiation() {
        return this;
    }

    @Override
    public ScreenID getScreenID() {
        return new ScreenID("ShiftRightClickGui");
    }
}