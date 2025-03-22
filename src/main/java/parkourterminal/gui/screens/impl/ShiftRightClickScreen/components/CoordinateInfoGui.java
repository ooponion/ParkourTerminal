package parkourterminal.gui.screens.impl.ShiftRightClickScreen.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;
import parkourterminal.gui.screens.impl.ShiftRightClickScreen.ShiftRightClickGui;
import parkourterminal.gui.screens.intf.BlurGui;
import parkourterminal.gui.component.ConsolaFontRenderer;
import parkourterminal.gui.component.CustomButton;
import parkourterminal.gui.component.CustomGuiTextField;
import parkourterminal.gui.screens.intf.instantiationScreen.intf.ScreenID;
import parkourterminal.gui.screens.intf.instantiationScreen.manager.ScreenManager;
import parkourterminal.network.DeleteCoordinatePacket;
import parkourterminal.network.NetworkLoader;
import parkourterminal.network.SaveCoordinatePacket;
import parkourterminal.network.SelectCoordinatePacket;
import parkourterminal.util.AnimationUtils.impls.ColorInterpolateAnimation;
import parkourterminal.util.AnimationUtils.impls.interpolatingData.InterpolatingColor;
import parkourterminal.util.AnimationUtils.intf.AbstractAnimation;
import parkourterminal.util.AnimationUtils.intf.AnimationMode;
import parkourterminal.util.NumberWrapper;
import parkourterminal.util.TeleporterHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoordinateInfoGui extends BlurGui {
    private final NBTTagCompound location;
    private final ItemStack heldItem;

    // 编辑框（行1～5）
    private CustomGuiTextField posXField;
    private CustomGuiTextField posYField;
    private CustomGuiTextField posZField;
    private CustomGuiTextField yawField;
    private CustomGuiTextField pitchField;

    // 布局参数
    private int verticalSpacing = 30;
    private int fieldWidth = 200;
    private int fieldHeight = 20;
    private int fieldX;
    private int totalRows = 6;  // 第0行用于显示名称，其余5行为可编辑文本框

    // 按钮参数
    private int buttonsY;           // 按钮区域Y坐标
    private int btnGroupWidth = 200;  // 与文本框宽度一致
    private int gap = 10;            // 按钮间隔
    private int btnWidth;
    private int btnHeight = 20;
    private int btnNormalColor = 0x40000000; // 深灰色
    private int btnHoverColor  = 0x600099FF; // 浅灰色
    private int btnCornerRadius = 5;

    // 自定义按钮组件
    private CustomButton saveButton;
    private CustomButton deleteButton;
    private CustomButton cancelButton;
    private CustomButton selectButton;

    // 输入框动画
    private List<CustomGuiTextField> fieldHoverProgress = new ArrayList<CustomGuiTextField>();
    private CustomGuiTextField focusedField = null;

    private final int startColor = 0x40000000; // 默认边框色（半透明黑）
    private final int endColor = 0x600099FF;   // 高亮边框色（半透明蓝）
    private final AbstractAnimation<InterpolatingColor> animation=new ColorInterpolateAnimation(0.25f,new InterpolatingColor(startColor), AnimationMode.BLENDED);

    public CoordinateInfoGui(NBTTagCompound location, ItemStack heldItem) {
        this.location = location;
        this.heldItem = heldItem;
    }

    @Override
    public void initGui() {
        super.initGui();
        fontRendererObj = new ConsolaFontRenderer(Minecraft.getMinecraft());

        // 计算整体组高度（6行）和起始Y坐标（垂直居中）
        int totalGroupHeight = totalRows * verticalSpacing;
        int startY = height / 2 - totalGroupHeight / 2;
        fieldX = width / 2 - fieldWidth / 2;

        // 第0行为名称显示，不用文本框；后续行（1～5）使用文本框
        posXField = new CustomGuiTextField(1, fontRendererObj, fieldX, startY + verticalSpacing, fieldWidth, fieldHeight);
        posYField = new CustomGuiTextField(2, fontRendererObj, fieldX, startY + 2 * verticalSpacing, fieldWidth, fieldHeight);
        posZField = new CustomGuiTextField(3, fontRendererObj, fieldX, startY + 3 * verticalSpacing, fieldWidth, fieldHeight);
        yawField   = new CustomGuiTextField(4, fontRendererObj, fieldX, startY + 4 * verticalSpacing, fieldWidth, fieldHeight);
        pitchField = new CustomGuiTextField(5, fontRendererObj, fieldX, startY + 5 * verticalSpacing, fieldWidth, fieldHeight);

        // 设置初始文本值（从 location 中读取）
        posXField.setText(String.valueOf(location.getDouble("posX")));
        posYField.setText(String.valueOf(location.getDouble("posY")));
        posZField.setText(String.valueOf(location.getDouble("posZ")));
        yawField.setText(String.valueOf(location.getFloat("yaw")));
        pitchField.setText(String.valueOf(location.getFloat("pitch")));

        // 初始化输入框动画变量
        fieldHoverProgress.add(posXField);
        fieldHoverProgress.add(posYField);
        fieldHoverProgress.add(posZField);
        fieldHoverProgress.add(yawField);
        fieldHoverProgress.add(pitchField);

        // 按钮区域位于整体组下方10像素处
        buttonsY = startY + totalGroupHeight + 10;
        btnGroupWidth = fieldWidth;
        btnWidth = (btnGroupWidth - 2 * gap) / 3;

        // 计算按钮 X 坐标并创建 CustomButton 实例
        int saveButtonX = fieldX;
        int deleteButtonX = fieldX + btnWidth + gap;
        int cancelButtonX = fieldX + 2 * (btnWidth + gap);
        int selectButtonX = fieldX + (fieldWidth - btnWidth) / 2;
        int selectButtonY = height - (buttonsY + btnHeight);
        saveButton   = new CustomButton(saveButtonX, buttonsY, btnWidth, btnHeight, btnNormalColor, btnHoverColor, btnCornerRadius, "Save");
        deleteButton = new CustomButton(deleteButtonX, buttonsY, btnWidth, btnHeight, btnNormalColor, btnHoverColor, btnCornerRadius, "Delete");
        cancelButton = new CustomButton(cancelButtonX, buttonsY, btnWidth, btnHeight, btnNormalColor, btnHoverColor, btnCornerRadius, "Cancel");
        selectButton = new CustomButton(selectButtonX, selectButtonY, btnWidth, btnHeight, btnNormalColor, btnHoverColor, btnCornerRadius, "Select");
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        // 计算起始Y坐标，与 initGui 保持一致
        int startY = height / 2 - (totalRows * verticalSpacing) / 2;

        // 绘制第0行的名称（非编辑）
        String nameLabel = "Name:";
        String nameValue = location.getString("name");
        int labelWidth = fontRendererObj.getStringWidth(nameLabel);
        int labelX = fieldX - labelWidth - 10;
        int row0Y = startY + (fieldHeight - fontRendererObj.FONT_HEIGHT) / 2;
        fontRendererObj.drawStringWithShadow(nameValue, fieldX, row0Y, 0xFFFFFF);

        // 绘制文本框
        posXField.draw(mouseX,mouseY,partialTicks);
        posYField.draw(mouseX,mouseY,partialTicks);
        posZField.draw(mouseX,mouseY,partialTicks);
        yawField.draw(mouseX,mouseY,partialTicks);
        pitchField.draw(mouseX,mouseY,partialTicks);

        // 绘制文本框标签（X, Y, Z, Yaw, Pitch）
        String[] labels = new String[] {"X:", "Y:", "Z:", "Yaw:", "Pitch:"};
        for (int i = 0; i < labels.length; i++) {
            int lw = fontRendererObj.getStringWidth(labels[i]);
            int lx = fieldX - lw - 10;
            int ly = startY + (i + 1) * verticalSpacing + (fieldHeight - fontRendererObj.FONT_HEIGHT) / 2;
            fontRendererObj.drawStringWithShadow(labels[i], lx, ly, 0xFFFFFF);
        }

        // 绘制自定义按钮
        saveButton.drawButton(fontRendererObj, mouseX, mouseY);
        deleteButton.drawButton(fontRendererObj, mouseX, mouseY);
        cancelButton.drawButton(fontRendererObj, mouseX, mouseY);
        selectButton.drawButton(fontRendererObj, mouseX, mouseY);

        // 输入框边框动画（悬停/聚焦效果）
        for (CustomGuiTextField field : fieldHoverProgress) {
            boolean isHovered = mouseX >= field.xPosition && mouseX <= field.xPosition + fieldWidth &&
                    mouseY >= field.yPosition && mouseY <= field.yPosition + fieldHeight;
            boolean isFocused = (focusedField == field);
            if(isHovered || isFocused){
                animation.RestartAnimation(new InterpolatingColor(endColor));
            }else{
                animation.RestartAnimation(new InterpolatingColor(startColor));
            }
            field.setCurrentBorderColor(animation.Update().getColor());
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        // 传递点击事件给文本框
        posXField.mouseClicked(mouseX, mouseY, mouseButton);
        posYField.mouseClicked(mouseX, mouseY, mouseButton);
        posZField.mouseClicked(mouseX, mouseY, mouseButton);
        yawField.mouseClicked(mouseX, mouseY, mouseButton);
        pitchField.mouseClicked(mouseX, mouseY, mouseButton);

        // 检查自定义按钮点击
        if (saveButton.isMouseOver(mouseX, mouseY)) {
            performSaveAction();
        } else if (deleteButton.isMouseOver(mouseX, mouseY)) {
            performDeleteAction();
        } else if (cancelButton.isMouseOver(mouseX, mouseY)) {
            performCancelAction();
        } else if (selectButton.isMouseOver(mouseX, mouseY)) {
            performSelectAction();
        }
    }

    private void performSelectAction() {
        if (heldItem != null && heldItem.hasTagCompound()) {
            NBTTagCompound nbt = heldItem.getTagCompound();
            if (nbt.hasKey("savedLocations")) {
                NBTTagList savedLocations = nbt.getTagList("savedLocations", 10);
                // 遍历保存的坐标列表，找到名称匹配的项
                for (int i = 0; i < savedLocations.tagCount(); i++) {
                    NBTTagCompound entry = savedLocations.getCompoundTagAt(i);
                    if (entry.getString("name").equals(location.getString("name"))) {
                        // 直接调用 TeleporterHelper.switchSelectedIndex，并传入当前玩家实例和目标索引
                        TeleporterHelper.switchSelectedIndex(Minecraft.getMinecraft().thePlayer, i);

                        // 发送保存网络包
                        SelectCoordinatePacket packet = new SelectCoordinatePacket(i);
                        NetworkLoader.NETWORK_WRAPPER.sendToServer(packet);
                        break;
                    }
                }
            }
        }

        ScreenManager.SwitchToScreen(new ScreenID("ShiftRightClickGui"));
    }

    private void performSaveAction() {
        // 更新 location 数据
        location.setDouble("posX", Double.parseDouble(posXField.getText()));
        location.setDouble("posY", Double.parseDouble(posYField.getText()));
        location.setDouble("posZ", Double.parseDouble(posZField.getText()));
        location.setFloat("yaw", Float.parseFloat(yawField.getText()));
        location.setFloat("pitch", Float.parseFloat(pitchField.getText()));

        // 同步到 heldItem 的 NBT
        if (heldItem != null && heldItem.hasTagCompound()) {
            NBTTagCompound nbt = heldItem.getTagCompound();
            if (nbt.hasKey("savedLocations")) {
                NBTTagList savedLocations = nbt.getTagList("savedLocations", 10);
                for (int i = 0; i < savedLocations.tagCount(); i++) {
                    NBTTagCompound entry = savedLocations.getCompoundTagAt(i);
                    if (entry.getString("name").equals(location.getString("name"))) {
                        savedLocations.set(i, location);
                        break;
                    }
                }
            }
        }

        // 发送保存网络包
        SaveCoordinatePacket packet = new SaveCoordinatePacket(location);
        NetworkLoader.NETWORK_WRAPPER.sendToServer(packet);
        ScreenManager.SwitchToScreen(new ScreenID("ShiftRightClickGui"));
    }

    private void performDeleteAction() {
        if (heldItem != null && heldItem.hasTagCompound()) {
            NBTTagCompound nbt = heldItem.getTagCompound();
            if (nbt.hasKey("savedLocations")) {
                NBTTagList savedLocations = nbt.getTagList("savedLocations", 10);
                int selectedIndex = nbt.hasKey("selectedIndex") ? nbt.getInteger("selectedIndex") : 0;
                String targetName = location.getString("name");
                boolean indexChanged = false;

                for (int i = 0; i < savedLocations.tagCount(); i++) {
                    NBTTagCompound entry = savedLocations.getCompoundTagAt(i);
                    if (entry.getString("name").equals(targetName)) {
                        savedLocations.removeTag(i);
                        if (i < selectedIndex) {
                            selectedIndex--;
                            indexChanged = true;
                        } else if (i == selectedIndex) {
                            selectedIndex = Math.min(selectedIndex, savedLocations.tagCount() - 1);
                            indexChanged = true;
                        } else {
                            indexChanged = true;
                        }
                        break;
                    }
                }

                for (int i = 0; i < savedLocations.tagCount(); i++) {
                    NBTTagCompound entry = savedLocations.getCompoundTagAt(i);
                    entry.setString("name", "Position #" + i);
                }

                if (savedLocations.tagCount() > 0) {
                    selectedIndex = Math.min(selectedIndex, savedLocations.tagCount() - 1);
                    nbt.setInteger("selectedIndex", selectedIndex);

                    NBTTagCompound selectedLoc = savedLocations.getCompoundTagAt(selectedIndex);
                    String teleporterAlias = nbt.getString("teleporterAlias");
                    String displayName = EnumChatFormatting.GOLD + "" + EnumChatFormatting.BOLD
                            + teleporterAlias + " " + selectedLoc.getString("name");
                    heldItem.setStackDisplayName(displayName);

                    NBTTagCompound displayTag = nbt.hasKey("display") ? nbt.getCompoundTag("display") : new NBTTagCompound();
                    NBTTagList loreList = new NBTTagList();

                    loreList.appendTag(new NBTTagString(EnumChatFormatting.YELLOW + "Stored Position:"));
                    loreList.appendTag(new NBTTagString(EnumChatFormatting.AQUA + "X: " + NumberWrapper.round(selectedLoc.getDouble("posX"))));
                    loreList.appendTag(new NBTTagString(EnumChatFormatting.AQUA + "Y: " + NumberWrapper.round(selectedLoc.getDouble("posY"))));
                    loreList.appendTag(new NBTTagString(EnumChatFormatting.AQUA + "Z: " + NumberWrapper.round(selectedLoc.getDouble("posZ"))));
                    loreList.appendTag(new NBTTagString(EnumChatFormatting.LIGHT_PURPLE + "Yaw: " + NumberWrapper.round(selectedLoc.getFloat("yaw"))));
                    loreList.appendTag(new NBTTagString(EnumChatFormatting.LIGHT_PURPLE + "Pitch: " + NumberWrapper.round(selectedLoc.getFloat("pitch"))));

                    displayTag.setTag("Lore", loreList);
                    nbt.setTag("display", displayTag);
                }

                boolean isLastEntry = false;
                if (savedLocations.tagCount() == 0) {
                    nbt.removeTag("savedLocations");
                    nbt.removeTag("selectedIndex");
                    isLastEntry = true;

                    if (nbt.hasKey("display")) {
                        NBTTagCompound displayTag = nbt.getCompoundTag("display");
                        if (displayTag.hasKey("Name")) {
                            displayTag.removeTag("Name");
                        }
                        if (displayTag.hasKey("Lore")) {
                            displayTag.removeTag("Lore");
                        }
                        if (displayTag.hasNoTags()) {
                            nbt.removeTag("display");
                        }
                    }
                    if (nbt.hasNoTags()) {
                        heldItem.setTagCompound(null);
                    } else {
                        heldItem.setTagCompound(nbt);
                    }
                } else {
                    nbt.setTag("savedLocations", savedLocations);
                    heldItem.setTagCompound(nbt);
                }

                DeleteCoordinatePacket packet = new DeleteCoordinatePacket(targetName, indexChanged, selectedIndex, isLastEntry);
                NetworkLoader.NETWORK_WRAPPER.sendToServer(packet);

                if (savedLocations.tagCount() == 0) {
                    ScreenManager.SwitchToGame();
                } else {
                    ScreenManager.SwitchToScreen(new ScreenID("ShiftRightClickGui"));
                }
                return;
            }
        }
        ScreenManager.SwitchToScreen(new ScreenID("ShiftRightClickGui"));
    }

    private void performCancelAction() {
        ScreenManager.SwitchToScreen(new ScreenID("ShiftRightClickGui"));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        posXField.textboxKeyTyped(typedChar, keyCode);
        posYField.textboxKeyTyped(typedChar, keyCode);
        posZField.textboxKeyTyped(typedChar, keyCode);
        yawField.textboxKeyTyped(typedChar, keyCode);
        pitchField.textboxKeyTyped(typedChar, keyCode);
        if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(null);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}