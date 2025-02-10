package parkourterminal.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import parkourterminal.gui.component.BlurGui;
import parkourterminal.gui.component.ConsolaFontRenderer;
import parkourterminal.util.AnimationHelper;
import parkourterminal.util.NumberWrapper;
import parkourterminal.util.ShapeDrawer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ShiftRightClickGui extends BlurGui {
    // 竖直滑动
    private int scrollOffset = 0;
    private int targetScrollOffset = 0;
    private int maxScroll = 0;

    // 水平滑动
    private Map<Integer, Integer> horizontalScrollOffsets = new HashMap<Integer, Integer>();
    private Map<Integer, Integer> targetHorizontalScrollOffsets = new HashMap<Integer, Integer>();
    private int draggingEntry = -1;
    private boolean draggingHoriz = false;
    private int dragStartMouseX = 0;
    private int dragStartHorizOffset = 0;

    // 悬停渐变
    private int hoveredEntry = -1;
    private Map<Integer, Float> hoverColorProgressMap = new HashMap<Integer, Float>();
    private int hoveredScrollBarEntry = -1;
    private Map<Integer, Float> scrollBarHoverProgress = new HashMap<Integer, Float>();

    @Override
    public void initGui() {
        super.initGui();

        fontRendererObj = new ConsolaFontRenderer(Minecraft.getMinecraft());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        int selectedIndex = -1;

        // 计算平滑滚动
        scrollOffset += (int)((targetScrollOffset - scrollOffset) * 0.4);

        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        ItemStack heldItem = player.getHeldItem();

        // NBT验证
        if (heldItem != null && heldItem.hasTagCompound()) {
            NBTTagCompound nbt = heldItem.getTagCompound();

            if (nbt.hasKey("selectedIndex"))
                selectedIndex = nbt.getInteger("selectedIndex");

            if (nbt.hasKey("savedLocations")) {
                NBTTagList savedLocations = nbt.getTagList("savedLocations", 10);

                // 调整垂直间距，减小条目之间的距离
                int padding = 4;
                int textHeight = fontRendererObj.FONT_HEIGHT;
                int entryExtraPadding = 5;
                int scrollBarHeight = 4; // 仅当需要横向滚动时的滚动条高度

                // 固定横向可见宽度（例如：窗口宽度减去50像素）
                int fixedEntryWidth = width - 50;

                // 计算所有条目的总高度
                int totalHeight = 0;
                for (int i = 0; i < savedLocations.tagCount(); i++) {
                    NBTTagCompound location = savedLocations.getCompoundTagAt(i);
                    String name = location.getString("name");
                    String posText = String.format(
                            "X: %s, Y: %s, Z: %s, Yaw: %s, Pitch: %s",
                            NumberWrapper.round(location.getDouble("posX")),
                            NumberWrapper.round(location.getDouble("posY")),
                            NumberWrapper.round(location.getDouble("posZ")),
                            NumberWrapper.round(location.getFloat("yaw")),
                            NumberWrapper.round(location.getFloat("pitch"))
                    );
                    String combinedText = EnumChatFormatting.WHITE + name + " " + EnumChatFormatting.AQUA + posText;
                    int fullTextWidth = fontRendererObj.getStringWidth(combinedText);
                    boolean hasScrollbar = fullTextWidth > fixedEntryWidth;
                    int boxHeight = hasScrollbar
                            ? (textHeight + entryExtraPadding + scrollBarHeight)
                            : (textHeight + entryExtraPadding);
                    totalHeight += boxHeight;
                    if (i < savedLocations.tagCount() - 1) {
                        totalHeight += padding;
                    }
                }

                // 可视区域高度（上下各预留20像素）
                int availableHeight = height - 40;
                maxScroll = Math.max(0, totalHeight - availableHeight);

                // 限制scrollOffset的范围
                if (scrollOffset > maxScroll)
                    scrollOffset = maxScroll;
                if (scrollOffset < 0)
                    scrollOffset = 0;

                // 可视区域起始Y坐标（垂直居中）
                int visibleStartY = (height - availableHeight) / 2;

                // 绘制背景矩形
                int backgroundWidth = fixedEntryWidth + 40; // 与各个条目的背景宽度一致
                int outerX = (width - backgroundWidth) / 2;
                int outerY = visibleStartY - scrollOffset - 10;  // 第一个条目的顶部
                int outerWidth = backgroundWidth;
                int outerHeight = totalHeight + 20;

                ShapeDrawer.drawRoundedRect(outerX, outerY, outerWidth, outerHeight, 0x40FFFFFF, 2);

                // 遍历每个条目进行绘制
                int currentY = visibleStartY;
                hoveredEntry = -1;
                hoveredScrollBarEntry = -1;

                for (int i = 0; i < savedLocations.tagCount(); i++) {
                    NBTTagCompound location = savedLocations.getCompoundTagAt(i);
                    String name = location.getString("name");
                    String posText = String.format(
                            "X: %s, Y: %s, Z: %s, Yaw: %s, Pitch: %s",
                            NumberWrapper.round(location.getDouble("posX")),
                            NumberWrapper.round(location.getDouble("posY")),
                            NumberWrapper.round(location.getDouble("posZ")),
                            NumberWrapper.round(location.getFloat("yaw")),
                            NumberWrapper.round(location.getFloat("pitch"))
                    );
                    String combinedText = EnumChatFormatting.GOLD + name + " " + EnumChatFormatting.AQUA + posText;
                    int fullTextWidth = fontRendererObj.getStringWidth(combinedText);
                    int visibleWidth = fixedEntryWidth;
                    boolean hasScrollbar = fullTextWidth > visibleWidth;

                    // 计算横向滚动偏移
                    int targetHorizOffset = targetHorizontalScrollOffsets.containsKey(i) ? targetHorizontalScrollOffsets.get(i) : 0;
                    int horizOffset = horizontalScrollOffsets.containsKey(i) ? horizontalScrollOffsets.get(i) : 0;
                    int maxHorizScroll = Math.max(0, fullTextWidth - visibleWidth);

                    // 平滑更新横向滚动偏移量
                    horizOffset += (int)((targetHorizOffset - horizOffset) * 0.4);
                    horizontalScrollOffsets.put(i, horizOffset);

                    if (horizOffset > maxHorizScroll)
                        horizOffset = maxHorizScroll;
                    if (horizOffset < 0)
                        horizOffset = 0;
                    if (hasScrollbar)
                        horizontalScrollOffsets.put(i, horizOffset);

                    // 计算条目背景尺寸
                    int boxWidth = visibleWidth + 20; // 左右各留10像素边距
                    int boxHeight = hasScrollbar
                            ? (textHeight + entryExtraPadding + scrollBarHeight)
                            : (textHeight + entryExtraPadding);

                    // 绘制背景（圆角矩形）
                    int boxX = (width - boxWidth) / 2;
                    // 当前条目的Y坐标 = 当前累计Y - scrollOffset
                    int boxY = currentY - scrollOffset;

                    boolean isHovered = mouseX >= boxX && mouseX <= boxX + boxWidth &&
                            mouseY >= boxY && mouseY <= boxY + boxHeight;

                    if (isHovered)
                        hoveredEntry = i;

                    // 在遍历条目时
                    float progress = hoverColorProgressMap.containsKey(i) ? hoverColorProgressMap.get(i) : 0.0f;
                    float targetProgress = (hoveredEntry == i) ? 1.0f : 0.0f;
                    progress += (targetProgress - progress) * 0.2f;
                    hoverColorProgressMap.put(i, progress);

                    // 定义起始颜色和目标颜色
                    int startColor = 0x80000000; // 半透明黑色
                    int endColor = 0x600099FF;   // 半透明淡蓝色

                    // 使用 progress 进行颜色插值
                    int currentColor = AnimationHelper.interpolateColor(startColor, endColor, progress);

                    // 绘制背景（应用插值颜色）
                    ShapeDrawer.drawRoundedRect(boxX, boxY, boxWidth, boxHeight, currentColor, 3);

                    if (i == selectedIndex) {
                        int borderColor = 0xFFFF0000; // 红色边框颜色
                        ShapeDrawer.drawRoundedRectBorder(
                                boxX, boxY, // 边框的起始位置
                                boxWidth, boxHeight, // 边框的宽度和高度
                                borderColor, 3// 边框颜色和圆角半径
                        );
                    }

                    // 裁剪区域
                    GL11.glEnable(GL11.GL_SCISSOR_TEST);
                    int scaleFactor = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
                    int scissorX = boxX * scaleFactor;
                    int scissorY = (height - (boxY + boxHeight)) * scaleFactor;
                    int scissorWidth = boxWidth * scaleFactor;
                    int scissorHeight = boxHeight * scaleFactor;
                    GL11.glScissor(scissorX, scissorY, scissorWidth, scissorHeight);

                    // 绘制文本，从boxX+10处开始，考虑横向滚动偏移
                    int textX = boxX + 10 - horizOffset;
                    int textY = boxY + 5; // 上边距固定为5像素
                    fontRendererObj.drawStringWithShadow(combinedText, textX, textY, 0xFFFFFF);

                    // 如果需要横向滚动，则绘制滚动条
                    if (hasScrollbar) {
                        int scrollBarX = boxX + 10;
                        int scrollBarY = boxY + textHeight + entryExtraPadding;
                        int scrollBarWidth = visibleWidth;

                        // 检测鼠标是否悬停在滚动条轨道上
                        boolean isScrollBarHovered =
                                mouseX >= scrollBarX && mouseX <= scrollBarX + scrollBarWidth &&
                                        mouseY >= scrollBarY && mouseY <= scrollBarY + scrollBarHeight;

                        if (isScrollBarHovered)
                            hoveredScrollBarEntry = i;

                        // 获取或初始化动画进度
                        float progress2 = scrollBarHoverProgress.containsKey(i) ?
                                scrollBarHoverProgress.get(i) : 0.0f;
                        float targetProgress2 = (hoveredScrollBarEntry == i) ? 1.0f : 0.0f;
                        progress2 += (targetProgress2 - progress2) * 0.2f; // 平滑过渡
                        scrollBarHoverProgress.put(i, progress2);

                        // 定义滚动条颜色插值
                        int thumbStartColor = 0xFFAAAAAA;   // 拇指默认颜色
                        int thumbEndColor = 0xFFCCCCCC;     // 悬停时拇指颜色

                        int currentThumbColor = AnimationHelper.interpolateColor(thumbStartColor, thumbEndColor, progress2);

                        // 计算滚动条拇指宽度，按比例确定，最小值20像素
                        int thumbWidth = Math.max(20, scrollBarWidth * visibleWidth / fullTextWidth);
                        int thumbMaxMovement = scrollBarWidth - thumbWidth;
                        int thumbX = scrollBarX + (maxHorizScroll == 0 ? 0 : (int) ((float) horizOffset / maxHorizScroll * thumbMaxMovement));

                        // 绘制滚动条拇指（应用插值颜色）
                        ShapeDrawer.drawRoundedRect(thumbX, scrollBarY, thumbWidth, scrollBarHeight, currentThumbColor, 2);
                    }

                    GL11.glDisable(GL11.GL_SCISSOR_TEST);

                    // 累加当前Y：本条目的高度加上条目间距（最后一个条目不加padding）
                    currentY += boxHeight;
                    if (i < savedLocations.tagCount() - 1) {
                        currentY += padding;
                    }
                }
            }
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        int dWheel = Mouse.getDWheel(); // 获取鼠标滚轮滚动值
        if (dWheel != 0) {
            int scrollAmount = 40; // 每次滚动的像素大小
            if (dWheel > 0)
                targetScrollOffset -= scrollAmount; // 向上
            else
                targetScrollOffset += scrollAmount; // 向下

            // 限制 targetScrollOffset 在合法范围内
            targetScrollOffset = Math.max(0, Math.min(targetScrollOffset, maxScroll));
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        ItemStack heldItem = player.getHeldItem();
        if (heldItem != null && heldItem.hasTagCompound()) {
            NBTTagCompound nbt = heldItem.getTagCompound();
            if (nbt.hasKey("savedLocations")) {
                NBTTagList savedLocations = nbt.getTagList("savedLocations", 10);
                int padding = 4;
                int textHeight = fontRendererObj.FONT_HEIGHT;
                int entryExtraPadding = 5;
                int scrollBarHeight = 4;
                int fixedEntryWidth = width - 50;

                // 使用与 drawScreen 中相同的 availableHeight 与 visibleStartY
                int availableHeight = height - 40;
                int visibleStartY = (height - availableHeight) / 2; // 例如 10
                int currentY = visibleStartY;

                for (int i = 0; i < savedLocations.tagCount(); i++) {
                    NBTTagCompound location = savedLocations.getCompoundTagAt(i);
                    String name = location.getString("name");
                    String posText = String.format(
                            "X: %s, Y: %s, Z: %s, Yaw: %s, Pitch: %s",
                            NumberWrapper.round(location.getDouble("posX")),
                            NumberWrapper.round(location.getDouble("posY")),
                            NumberWrapper.round(location.getDouble("posZ")),
                            NumberWrapper.round(location.getFloat("yaw")),
                            NumberWrapper.round(location.getFloat("pitch"))
                    );
                    String combinedText = EnumChatFormatting.WHITE + name + " " + EnumChatFormatting.AQUA + posText;
                    int fullTextWidth = fontRendererObj.getStringWidth(combinedText);
                    boolean hasScrollbar = fullTextWidth > fixedEntryWidth;
                    int boxHeight = hasScrollbar
                            ? (textHeight + entryExtraPadding + scrollBarHeight)
                            : (textHeight + entryExtraPadding);
                    int boxWidth = fixedEntryWidth + 20;
                    int boxX = (width - boxWidth) / 2;
                    int boxY = currentY - scrollOffset;

                    // 如果当前条目需要滚动条，则检查是否点击在滚动条区域
                    if (hasScrollbar) {
                        int scrollBarX = boxX + 10;
                        int scrollBarY = boxY + textHeight + entryExtraPadding;
                        int scrollBarWidth = fixedEntryWidth;
                        if (mouseX >= scrollBarX && mouseX <= scrollBarX + scrollBarWidth &&
                                mouseY >= scrollBarY && mouseY <= scrollBarY + scrollBarHeight) {
                            draggingEntry = i;
                            draggingHoriz = true;
                            dragStartMouseX = mouseX;
                            dragStartHorizOffset = horizontalScrollOffsets.containsKey(i) ? horizontalScrollOffsets.get(i) : 0;
                            break;
                        }
                    }

                    // 检测鼠标是否点击在条目的背景区域内
                    boolean isClicked = mouseX >= boxX && mouseX <= boxX + boxWidth &&
                            mouseY >= boxY && mouseY <= boxY + boxHeight;

                    // 如果点击了条目背景区域且不是滚动条部分
                    if (isClicked && mouseButton == 0) { // 0 表示左键
                        // 排除滚动条区域
                        if (hasScrollbar) {
                            int scrollBarX = boxX + 10;
                            int scrollBarY = boxY + textHeight + entryExtraPadding;
                            if (mouseX >= scrollBarX && mouseX <= scrollBarX + fixedEntryWidth &&
                                    mouseY >= scrollBarY && mouseY <= scrollBarY + scrollBarHeight) {
                                // 点击了滚动条区域，不处理
                                continue;
                            }
                        }

                        // 打开新的 CoordinateInfoGui，并传递当前条目的数据
                        NBTTagCompound clickedLocation = savedLocations.getCompoundTagAt(i);
                        Minecraft.getMinecraft().displayGuiScreen(new CoordinateInfoGui(clickedLocation, heldItem));
                        break; // 只处理一个条目
                    }

                    currentY += boxHeight + padding;
                }
            }
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        if (draggingHoriz && draggingEntry != -1) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            ItemStack heldItem = player.getHeldItem();
            if (heldItem != null && heldItem.hasTagCompound()) {
                NBTTagCompound nbt = heldItem.getTagCompound();
                if (nbt.hasKey("savedLocations")) {
                    NBTTagList savedLocations = nbt.getTagList("savedLocations", 10);
                    if (draggingEntry < savedLocations.tagCount()) {
                        NBTTagCompound location = savedLocations.getCompoundTagAt(draggingEntry);
                        String name = location.getString("name");
                        String posText = String.format(
                                "X: %s, Y: %s, Z: %s, Yaw: %s, Pitch: %s",
                                NumberWrapper.round(location.getDouble("posX")),
                                NumberWrapper.round(location.getDouble("posY")),
                                NumberWrapper.round(location.getDouble("posZ")),
                                NumberWrapper.round(location.getFloat("yaw")),
                                NumberWrapper.round(location.getFloat("pitch"))
                        );
                        String combinedText = EnumChatFormatting.WHITE + name + " " + EnumChatFormatting.AQUA + posText;
                        int fullTextWidth = fontRendererObj.getStringWidth(combinedText);
                        int fixedEntryWidth = width - 50;
                        int visibleWidth = fixedEntryWidth;
                        int maxHorizScroll = Math.max(0, fullTextWidth - visibleWidth);
                        int scrollBarWidth = visibleWidth;
                        int dx = mouseX - dragStartMouseX;
                        int thumbWidth = Math.max(20, scrollBarWidth * visibleWidth / fullTextWidth);
                        int thumbMaxMovement = scrollBarWidth - thumbWidth;
                        int newOffset = dragStartHorizOffset + (maxHorizScroll == 0 ? 0 : (int) ((float) dx / thumbMaxMovement * maxHorizScroll));
                        if (newOffset < 0)
                            newOffset = 0;
                        if (newOffset > maxHorizScroll)
                            newOffset = maxHorizScroll;
                        targetHorizontalScrollOffsets.put(draggingEntry, newOffset);
                    }
                }
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        draggingHoriz = false;
        draggingEntry = -1;
        hoveredScrollBarEntry = -1;
    }
}