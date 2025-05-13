package parkourterminal.gui.screens.impl.InGameMenuGui;

import java.io.IOException;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import parkourterminal.gui.layout.UIComponent;
import parkourterminal.gui.screens.impl.InGameMenuGui.Components.TestCard;
import parkourterminal.gui.component.*;
import parkourterminal.gui.component.scrollBar.impl.ScrollBarImpl;
import parkourterminal.gui.component.scrollBar.intf.ScrollDirection;
import parkourterminal.gui.screens.impl.InGameMenuGui.Components.TestCardContainer;
import parkourterminal.gui.screens.intf.BlurGui;
import parkourterminal.gui.screens.intf.ModDetailGui;
import parkourterminal.util.ScissorHelper;
import parkourterminal.util.ShapeDrawer;
@SideOnly(Side.CLIENT)
public class IngameMenuGui extends BlurGui {
    public enum State {
        MAIN_MENU,
        MOD_DETAIL
    }
    private int panelMargin ;
    private int panelX;
    private int panelY;
    private int panelWidth;
    private int panelHeight ;
    private int cardAreaX ;
    private int cardAreaY;
    private int cardAreaWidth;
    private int cardAreaHeight;
    private ScrollBarImpl scrollBar=new ScrollBarImpl(0,0,0,0, ScrollDirection.VERTICAL);
    private boolean isFirstInit = true;  // 记录是否是首次打开 GUI
    private State currentState = State.MAIN_MENU;
//    private List<ModCard> modCards = new ArrayList<ModCard>();
    private ModDetailGui currentModDetailGui;
    private final TestCardContainer testCardContainer=new TestCardContainer();

    // 动画
    private float exitIconScale = 1.0f;  // 退出图标的当前缩放比例



    int cardWidth = 80;
    int fixedMargin = 5;
    int cardHeight = 20;

    private void registerCards() {

        testCardContainer.addComponent(new TestCard(width - 115, height - 35, 100, 20));
    }
    private void UpdateSize(){
        panelMargin = 10; // 背景板与屏幕边界的间距
        panelX = panelMargin;
        panelY = panelMargin;
        panelWidth = width - panelMargin * 2;
        panelHeight = height - panelMargin * 2;
        cardAreaX = panelMargin + (int) (panelWidth * 0.20);
        cardAreaY = panelMargin + (int) (panelHeight * 0.10);
        cardAreaWidth = (int) (panelWidth * 0.80);
        cardAreaHeight = (int) (panelHeight * 0.90);
        testCardContainer.setPosition(cardAreaX,cardAreaY);
        testCardContainer.setWidth(cardAreaWidth);
        scrollBar.setSize(4, cardAreaHeight);
        scrollBar.setPosition(cardAreaX+cardAreaWidth-4, cardAreaY);
        scrollBar.UpdateContentSize(testCardContainer.getComponentsTotalHeight()+testCardContainer.getPadding().bottom+testCardContainer.getPadding().top);
        testCardContainer.setHeight(testCardContainer.getComponentsTotalHeight()+testCardContainer.getPadding().bottom+testCardContainer.getPadding().top);
    }
    @Override
    public void initGui() {
        fontRendererObj = new ConsolaFontRenderer(mc);
        if(isFirstInit){
            scrollBar.setColor(0x00000000,0x40000000);
            testCardContainer.Clear();
            for(int i=0;i<100;i++){
                registerCards();
            }
        }
        UpdateSize();
        isFirstInit=false;
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        testCardContainer.setPosition(cardAreaX,cardAreaY-(int)scrollBar.getContentOffset());
        // 绘制仪表盘背景（例如模糊背景由 BlurGui 实现）
        drawDashboardBackground(mouseX, mouseY);

        if (currentState == State.MAIN_MENU) {
            // 计算卡片显示区域的坐标和尺寸

            ScissorHelper.EnableScissor(cardAreaX,cardAreaY,cardAreaWidth,cardAreaHeight);

            // 绘制卡片（仅在此区域内可见）
            testCardContainer.draw(mouseX,mouseY,partialTicks);

            ScissorHelper.DisableScissor();

            scrollBar.draw(mouseX,mouseY,partialTicks);


        } else if (currentState == State.MOD_DETAIL && currentModDetailGui != null) {
            // 绘制详细设置界面
            currentModDetailGui.drawScreen(mouseX, mouseY, partialTicks, width, height);
        }
    }
    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
    {
        if (currentState == State.MAIN_MENU) {
            scrollBar.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        // 退出图标参数（右上角）
        int exitIconSize = 16;      // 图标尺寸
        int exitIconMargin = 5;       // 边距
        int exitIconX = panelX + panelWidth - exitIconSize - exitIconMargin;
        int exitIconY = panelY + exitIconMargin;

        // 如果左键点击在退出图标区域，则退出当前Gui
        if (mouseButton == 0 &&
                mouseX >= exitIconX && mouseX <= exitIconX + exitIconSize &&
                mouseY >= exitIconY && mouseY <= exitIconY + exitIconSize) {
            mc.displayGuiScreen(null);
            return;
        }

        if (currentState == State.MAIN_MENU) {

            // 检查是否点击了滑动条
            scrollBar.mouseClicked(mouseX, mouseY, mouseButton);
            // 确保鼠标点击位置在卡片显示区域内
            for (UIComponent card : testCardContainer.getComponents()) {
                if (card.isMouseOver(mouseX, mouseY)) { // 考虑滚动偏移量
                    currentModDetailGui = ((TestCard)card).getModDetailGui();
                    currentState = State.MOD_DETAIL;
                    return;
                }
            }
        } else if (currentState == State.MOD_DETAIL && currentModDetailGui != null) {
            if (currentModDetailGui.mouseClicked(mouseX, mouseY, mouseButton))
                returnToMainMenu();
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (currentState == State.MAIN_MENU) {
            // 停止拖动
            scrollBar.mouseReleased(mouseX,mouseY,state);
        } else if (currentState == State.MOD_DETAIL && currentModDetailGui != null) {
            currentModDetailGui.mouseReleased(mouseX, mouseY, state);
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        // 处理鼠标滚轮事件
        int scrollAmount = Mouse.getEventDWheel();
        if (scrollAmount != 0) {
            scrollAmount = scrollAmount > 0 ? -20 : 20; // 反转滚动方向
            scrollBar.scrollWheel(scrollAmount );// 每次滚动20像素
        }
    }

//    private int getContentHeight() {
//
//
//        int rows = (modCards.size() + getColumns() - 1) / getColumns();
//        int totalHeight = rows * (cardHeight + fixedMargin) + fixedMargin;
//
//        return totalHeight;
//    }
//
//    private int getColumns() {
//
//
//        int columns = (cardAreaWidth - fixedMargin) / (cardWidth + fixedMargin);
//        return Math.max(1, columns);
//    }

    private void drawDashboardBackground(int mouseX, int mouseY) {

        int cornerRadius = 3; // 背景板圆角

        // 单层玻璃效果背景
        drawGlassPanel(panelX, panelY, panelWidth, panelHeight, cornerRadius);

        // 在背景左上角绘制图标
        int iconSize = 16;     // 图标尺寸
        int iconMargin = 5;   // 图标与背景边界的间距（可根据需求调整）
        int iconX = panelX + iconMargin;
        int iconY = panelY + iconMargin;

        // 绑定自定义图标纹理
        Minecraft mc = Minecraft.getMinecraft();
        mc.getTextureManager().bindTexture(new ResourceLocation("parkourterminal", "textures/gui/terminal.png"));

        // 启用混合模式，确保透明效果正确
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        // 恢复颜色状态
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        // 直接绘制图标，不使用任何变换（旋转等）
        ShapeDrawer.drawScaledCustomSizeModalRect(iconX, iconY, 0, 0, 64, 64, iconSize, iconSize, 64, 64);

        // 在图标右侧绘制文字 "Parkour Terminal"
        String text = "Parkour Terminal";
        int textColor = 0xFFFFFF; // 白色
        int textX = iconX + iconSize + 5; // 文字 X 坐标：图标右侧 + 5 像素间距
        int textY = iconY + (iconSize - fontRendererObj.FONT_HEIGHT) / 2; // 文字 Y 坐标：垂直居中

        // 渲染文字
        ((ConsolaFontRenderer)fontRendererObj).setFontScale(1.5f);
        fontRendererObj.drawString(text, textX, textY, textColor);
        ((ConsolaFontRenderer)fontRendererObj).setFontScale(1.0f);

        // --- 下面处理右上角退出图标的动画效果 ---
        int exitIconSize = 12;         // 基础退出图标尺寸
        int exitIconMargin = 5;        // 与背景边界的间距
        int exitIconX = panelX + panelWidth - exitIconSize - exitIconMargin;
        int exitIconY = panelY + exitIconMargin;
        mc.getTextureManager().bindTexture(new ResourceLocation("parkourterminal", "textures/gui/quit.png"));

        // 判断鼠标是否悬停在退出图标区域（使用 lastMouseX、lastMouseY）
        if (mouseX >= exitIconX && mouseX <= exitIconX + exitIconSize &&
                mouseY >= exitIconY && mouseY <= exitIconY + exitIconSize) {
            // 平滑增大缩放比例至1.2
            exitIconScale += (1.2f - exitIconScale) * 0.2f;
        } else {
            // 平滑回落至1.0
            exitIconScale += (1.0f - exitIconScale) * 0.2f;
        }

        // 计算退出图标原来的中心位置
        float centerX = exitIconX + exitIconSize / 2.0f;
        float centerY = exitIconY + exitIconSize / 2.0f;

        // 缩放后的尺寸
        int scaledExitIconSize = (int) (exitIconSize * exitIconScale);

        // 为保持中心位置不变，重新计算绘制的左上角
        float drawX = centerX - scaledExitIconSize / 2.0f;
        float drawY = centerY - scaledExitIconSize / 2.0f;

        // 绘制退出图标（无需任何矩阵变换）
        ShapeDrawer.drawScaledCustomSizeModalRect(drawX, drawY, 0, 0, 64, 64, scaledExitIconSize, scaledExitIconSize, 64, 64);

        ResourceLocation skin = mc.thePlayer.getLocationSkin();
        mc.getTextureManager().bindTexture(skin);
        int leftAreaWidth = (int)(panelWidth * 0.20);

        // 设定头像绘制尺寸，例如 32×32 像素
        int avatarSize = 64;

        // 计算绘制位置：在左20%区域水平居中，并且垂直居中整个面板（或仪表盘内部）
        drawX = panelX + (leftAreaWidth - avatarSize) / 2.0f;
        drawY = panelY + (panelHeight - avatarSize) / 2.0f;

        ShapeDrawer.drawScaledCustomSizeModalRect(drawX, drawY, 8, 8, 8, 8, avatarSize, avatarSize, 64, 64);

        // 绘制玩家 ID
        String playerID = mc.thePlayer.getName();
        List<String> lines = fontRendererObj.listFormattedStringToWidth(playerID, leftAreaWidth - 10);

        // 设置一个适当的垂直边距
        int textMargin = 4;
        int textStartY = (int) (drawY + avatarSize + textMargin);
        for (String line : lines) {
            int lineWidth = fontRendererObj.getStringWidth(line);
            // 使文本水平居中
            int lineX = panelX + (leftAreaWidth - lineWidth) / 2;
            fontRendererObj.drawString(line, lineX, textStartY, 0xFFD700);
            textStartY += fontRendererObj.FONT_HEIGHT;
        }

        // 绘制水平线（仪表盘上 10% 位置）
        int horizontalLineY = panelY + (int) (panelHeight * 0.10); // 水平线 Y 坐标
        int horizontalLineColor = 0xA0FFFFFF; // 白色（ARGB 格式）
        ShapeDrawer.drawLine(panelX, horizontalLineY, panelX + panelWidth, horizontalLineY, horizontalLineColor);

        // 绘制垂直线（水平线下方的左 20% 位置）
        int verticalLineX = panelX + (int) (panelWidth * 0.20) + 1; // 垂直线 X 坐标
        int verticalLineColor = 0xA0FFFFFF; // 白色（ARGB 格式）
        ShapeDrawer.drawLine(verticalLineX, horizontalLineY, verticalLineX, panelY + panelHeight, verticalLineColor);

        // 恢复 OpenGL 状态
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    private void drawGlassPanel(int x, int y, int width, int height, int cornerRadius) {
        // 玻璃效果背景（半透明白色）
        ShapeDrawer.drawRoundedRect(
                x, y, width, height,
                0x40000000, // 半透明白色
                cornerRadius
        );
        // 内边框
        ShapeDrawer.drawRoundedRectBorder(
                x, y, width, height,
                0x20000000, // 半透明边框
                cornerRadius
        );
    }

//    private void updateCardPositions() {
//
//        // 计算可放置的列数（确保至少 1 列）
//        int columns = getColumns();
//
//        // 计算水平额外空间：每行的最小所需宽度与显示区域宽度的差值均摊到 (columns+1) 个间隙中
//        int requiredWidth = columns * cardWidth + (columns + 1) * fixedMargin;
//        int extraHoriz = cardAreaWidth - requiredWidth;
//        float additionalHoriz = extraHoriz > 0 ? extraHoriz / (float)(columns + 1) : 0;
//        float gapX = fixedMargin + additionalHoriz;
//        float gapY = fixedMargin;
//
//        // 对每个卡片，根据其索引计算所在的行和列，设置目标位置（setPosition 内部实现了动画移动）
//        for (int i = 0; i < modCards.size(); i++) {
//            int col = i % columns;
//            int row = i / columns;
//            int targetX = cardAreaX + Math.round(gapX * (col + 1) + col * cardWidth);
//            int targetY = (int) (cardAreaY + Math.round(gapY * (row + 1) + row * cardHeight) - scrollBar.getContentOffset()); // 考虑滚动偏移量
//            modCards.get(i).setPosition(targetX, targetY, cardWidth, cardHeight);
//        }
//    }

    // 提供给详细界面调用，实现返回主菜单
    public void returnToMainMenu() {
        currentState = State.MAIN_MENU;
        currentModDetailGui = null;
    }


    @Override
    public void onResize(Minecraft mcIn, int w, int h)
    {
        super.onResize(mcIn, w, h);
        scrollBar.setSize( 4, cardAreaHeight);
        scrollBar.setPosition(cardAreaX+cardAreaWidth-4,cardAreaY);
    }

}