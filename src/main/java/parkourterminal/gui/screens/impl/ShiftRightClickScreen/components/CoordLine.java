//package parkourterminal.gui.screens.impl.ShiftRightClickScreen.components;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.FontRenderer;
//import net.minecraft.client.gui.ScaledResolution;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.util.EnumChatFormatting;
//import org.lwjgl.opengl.GL11;
//import parkourterminal.gui.layout.UIComponent;
//import parkourterminal.util.NumberWrapper;
//import parkourterminal.util.ShapeDrawer;
//
//public class CoordLine extends UIComponent {
//    private NBTTagCompound location;
//    public CoordLine(NBTTagCompound location){
//        this.location=location;
//    }
//    @Override
//    public void draw(int mouseX, int mouseY, float partialTicks) {
//        FontRenderer fontRendererObj=Minecraft.getMinecraft().fontRendererObj;
//        int textHeight = fontRendererObj.FONT_HEIGHT;
//        int entryExtraPadding = 5;
//        int scrollBarHeight = 4; // 仅当需要横向滚动时的滚动条高度
//
//        // 固定横向可见宽度（例如：窗口宽度减去50像素）
//        int fixedEntryWidth = width - 50;
//        String name = location.getString("name");
//        String posText = String.format(
//                "X: %s, Y: %s, Z: %s, Yaw: %s, Pitch: %s",
//                NumberWrapper.round(location.getDouble("posX")),
//                NumberWrapper.round(location.getDouble("posY")),
//                NumberWrapper.round(location.getDouble("posZ")),
//                NumberWrapper.round(location.getFloat("yaw")),
//                NumberWrapper.round(location.getFloat("pitch"))
//        );
//        String combinedText = EnumChatFormatting.GOLD + name + " " + EnumChatFormatting.AQUA + posText;
//
//        int fullTextWidth = fontRendererObj.getStringWidth(combinedText);
//        int visibleWidth = fixedEntryWidth;
//        boolean hasScrollbar = fullTextWidth > visibleWidth;
//
////        // 计算横向滚动偏移
////        int targetHorizOffset = targetHorizontalScrollOffsets.containsKey(i) ? targetHorizontalScrollOffsets.get(i) : 0;
////        int horizOffset = horizontalScrollOffsets.containsKey(i) ? horizontalScrollOffsets.get(i) : 0;
////        int maxHorizScroll = Math.max(0, fullTextWidth - visibleWidth);
////
////        // 平滑更新横向滚动偏移量
////        horizOffset += (int)((targetHorizOffset - horizOffset) * 0.4);
////        horizontalScrollOffsets.put(i, horizOffset);
////
////        if (horizOffset > maxHorizScroll)
////            horizOffset = maxHorizScroll;
////        if (horizOffset < 0)
////            horizOffset = 0;
////        if (hasScrollbar)
////            horizontalScrollOffsets.put(i, horizOffset);
//
//        // 计算条目背景尺寸
//        int boxWidth = visibleWidth + 20; // 左右各留10像素边距
//        int boxHeight = hasScrollbar
//                ? (textHeight + entryExtraPadding + scrollBarHeight)
//                : (textHeight + entryExtraPadding);
//
//        // 绘制背景（圆角矩形）
//        int boxX = (width - boxWidth) / 2;
//        // 当前条目的Y坐标 = 当前累计Y - scrollOffset
//        int boxY = this.y-scrollOffset;
//
//        boolean isHovered = mouseX >= boxX && mouseX <= boxX + boxWidth &&
//                mouseY >= boxY && mouseY <= boxY + boxHeight;
//
//        if (isHovered)
//            hoveredEntry = i;
//
//        // 在遍历条目时
//        float progress = hoverColorProgressMap.containsKey(i) ? hoverColorProgressMap.get(i) : 0.0f;
//        float targetProgress = (hoveredEntry == i) ? 1.0f : 0.0f;
//        progress += (targetProgress - progress) * 0.2f;
//        hoverColorProgressMap.put(i, progress);
//
//        // 定义起始颜色和目标颜色
//        int startColor = 0x80000000; // 半透明黑色
//        int endColor = 0x600099FF;   // 半透明淡蓝色
//
//        // 使用 progress 进行颜色插值
//        int currentColor = AnimationHelper.interpolateColor(startColor, endColor, progress);
//
//        // 绘制背景（应用插值颜色）
//        ShapeDrawer.drawRoundedRect(boxX, boxY, boxWidth, boxHeight, currentColor, 3);
//
//        // 裁剪区域
//        GL11.glEnable(GL11.GL_SCISSOR_TEST);
//        int scaleFactor = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
//        int scissorX = boxX * scaleFactor;
//        int scissorY = (height - (boxY + boxHeight)) * scaleFactor;
//        int scissorWidth = boxWidth * scaleFactor;
//        int scissorHeight = boxHeight * scaleFactor;
//        GL11.glScissor(scissorX, scissorY, scissorWidth, scissorHeight);
//
//        // 绘制文本，从boxX+10处开始，考虑横向滚动偏移
//        int textX = boxX + 10 - horizOffset;
//        int textY = boxY + 5; // 上边距固定为5像素
//        fontRendererObj.drawStringWithShadow(combinedText, textX, textY, 0xFFFFFF);
//
//        // 如果需要横向滚动，则绘制滚动条
//        if (hasScrollbar) {
//            int scrollBarX = boxX + 10;
//            int scrollBarY = boxY + textHeight + entryExtraPadding;
//            int scrollBarWidth = visibleWidth;
//
//            // 检测鼠标是否悬停在滚动条轨道上
//            boolean isScrollBarHovered =
//                    mouseX >= scrollBarX && mouseX <= scrollBarX + scrollBarWidth &&
//                            mouseY >= scrollBarY && mouseY <= scrollBarY + scrollBarHeight;
//
//            if (isScrollBarHovered)
//                hoveredScrollBarEntry = i;
//
//            // 获取或初始化动画进度
//            float progress2 = scrollBarHoverProgress.containsKey(i) ?
//                    scrollBarHoverProgress.get(i) : 0.0f;
//            float targetProgress2 = (hoveredScrollBarEntry == i) ? 1.0f : 0.0f;
//            progress2 += (targetProgress2 - progress2) * 0.2f; // 平滑过渡
//            scrollBarHoverProgress.put(i, progress2);
//
//            // 定义滚动条颜色插值
//            int thumbStartColor = 0xFFAAAAAA;   // 拇指默认颜色
//            int thumbEndColor = 0xFFCCCCCC;     // 悬停时拇指颜色
//
//            int currentThumbColor = AnimationHelper.interpolateColor(thumbStartColor, thumbEndColor, progress2);
//
//            // 计算滚动条拇指宽度，按比例确定，最小值20像素
//            int thumbWidth = Math.max(20, scrollBarWidth * visibleWidth / fullTextWidth);
//            int thumbMaxMovement = scrollBarWidth - thumbWidth;
//            int thumbX = scrollBarX + (maxHorizScroll == 0 ? 0 : (int) ((float) horizOffset / maxHorizScroll * thumbMaxMovement));
//
//            // 绘制滚动条拇指（应用插值颜色）
//            ShapeDrawer.drawRoundedRect(thumbX, scrollBarY, thumbWidth, scrollBarHeight, currentThumbColor, 2);
//        }
//
//        GL11.glDisable(GL11.GL_SCISSOR_TEST);
//        if (i == selectedIndex) {
//            int borderColor = 0xFFFF0000; // 红色边框颜色
//            ShapeDrawer.drawRoundedRectBorder(
//                    boxX, boxY, // 边框的起始位置
//                    boxWidth, boxHeight, // 边框的宽度和高度
//                    borderColor, 3// 边框颜色和圆角半径
//            );
//        }
//
//    }
//
//    @Override
//    public boolean isMouseOver(int mouseX, int mouseY) {
//        return false;
//    }
//}
