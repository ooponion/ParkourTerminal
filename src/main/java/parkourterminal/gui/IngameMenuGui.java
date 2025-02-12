package parkourterminal.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;
import parkourterminal.gui.component.BlurGui;
import parkourterminal.gui.component.ConsolaFontRenderer;
import parkourterminal.gui.component.ModCard;
import parkourterminal.gui.component.ModDetailGui;
import parkourterminal.util.ShapeDrawer;

public class IngameMenuGui extends BlurGui {
    public enum State {
        MAIN_MENU,
        MOD_DETAIL
    }

    private State currentState = State.MAIN_MENU;
    private List<ModCard> modCards = new ArrayList<ModCard>();
    private ModDetailGui currentModDetailGui;
    private int scrollOffset = 0; // 滚动偏移量

    private void registerCards() {
        for (int i = 0; i < 60; i++) { // 假设我们有20个卡片
            registerCard("Mod " + (i + 1));
        }
    }

    private void registerCard(String title) {
        modCards.add(new ModCard(title, width - 115, height - 35, 100, 20));
    }

    @Override
    public void initGui() {
        fontRendererObj = new ConsolaFontRenderer(mc);
        registerCards();

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        // 每次绘制前更新卡片位置，确保自适应屏幕变化
        updateCardPositions();

        // 绘制仪表盘背景（例如模糊背景由 BlurGui 实现）
        drawDashboardBackground();

        if (currentState == State.MAIN_MENU) {
            // 绘制所有 Mod 卡片，考虑滚动偏移量
            for (ModCard card : modCards) {
                card.draw(mouseX, mouseY - scrollOffset); // 调整绘制位置
            }
        } else if (currentState == State.MOD_DETAIL && currentModDetailGui != null) {
            // 绘制详细设置界面
            currentModDetailGui.drawScreen(mouseX, mouseY, partialTicks);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (currentState == State.MAIN_MENU) {
            // 检查是否点击了某个卡片，考虑滚动偏移量
            for (ModCard card : modCards) {
                if (card.isMouseOver(mouseX, mouseY + scrollOffset)) { // 调整点击检测
                    currentModDetailGui = card.getModDetailGui();
                    currentState = State.MOD_DETAIL;
                    return;
                }
            }
        } else if (currentState == State.MOD_DETAIL && currentModDetailGui != null) {
            currentModDetailGui.mouseClicked(mouseX, mouseY, mouseButton);
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (currentState == State.MOD_DETAIL && currentModDetailGui != null) {
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
            scrollAmount = scrollAmount > 0 ? -1 : 1; // 反转滚动方向
            scrollOffset += scrollAmount * 20; // 每次滚动20像素
            scrollOffset = Math.max(0, Math.min(getMaxScrollOffset(), scrollOffset)); // 限制滚动范围
        }
    }

    private int getMaxScrollOffset() {
        int panelMargin = 10;
        int panelHeight = height - panelMargin * 2;
        int cardAreaHeight = (int)(panelHeight * 0.90);
        int cardHeight = 20;
        int fixedMargin = 5;

        int rows = (modCards.size() + getColumns() - 1) / getColumns();
        int totalHeight = rows * (cardHeight + fixedMargin) + fixedMargin;

        return Math.max(0, totalHeight - cardAreaHeight);
    }

    private int getColumns() {
        int panelMargin = 10;
        int panelWidth = width - panelMargin * 2;
        int cardAreaWidth = (int)(panelWidth * 0.80);
        int cardWidth = 80;
        int fixedMargin = 5;

        int columns = (cardAreaWidth - fixedMargin) / (cardWidth + fixedMargin);
        return Math.max(1, columns);
    }

    private void drawDashboardBackground() {
        int panelMargin = 10; // 背景板与屏幕边界的间距
        int panelX = panelMargin;
        int panelY = panelMargin;
        int panelWidth = width - panelMargin * 2;
        int panelHeight = height - panelMargin * 2;

        int cornerRadius = 3; // 背景板圆角

        // 单层玻璃效果背景
        drawGlassPanel(panelX, panelY, panelWidth, panelHeight, cornerRadius);
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

    private void updateCardPositions() {
        int panelMargin = 10;
        int panelX = panelMargin;
        int panelY = panelMargin;
        int panelWidth = width - panelMargin * 2;
        int panelHeight = height - panelMargin * 2;

        // Card 显示区域：背景板的右 80% 与下 90% 的交叉部分
        int cardAreaX = panelX + (int)(panelWidth * 0.20);
        int cardAreaY = panelY + (int)(panelHeight * 0.10);
        int cardAreaWidth = (int)(panelWidth * 0.80);
        int cardAreaHeight = (int)(panelHeight * 0.90);

        int cardWidth = 80;
        int cardHeight = 20;
        int fixedMargin = 5; // 卡片与卡片以及卡片与边界之间的固定间距

        // 计算可放置的列数（确保至少 1 列）
        int columns = getColumns();
        // 计算需要的行数（向上取整）
        int rows = (modCards.size() + columns - 1) / columns;

        // 计算水平额外空间：每行的最小所需宽度与显示区域宽度的差值均摊到 (columns+1) 个间隙中
        int requiredWidth = columns * cardWidth + (columns + 1) * fixedMargin;
        int extraHoriz = cardAreaWidth - requiredWidth;
        float additionalHoriz = extraHoriz > 0 ? extraHoriz / (float)(columns + 1) : 0;
        float gapX = fixedMargin + additionalHoriz;
        float gapY = fixedMargin;

        // 对每个卡片，根据其索引计算所在的行和列，设置目标位置（setPosition 内部实现了动画移动）
        for (int i = 0; i < modCards.size(); i++) {
            int col = i % columns;
            int row = i / columns;
            int targetX = cardAreaX + Math.round(gapX * (col + 1) + col * cardWidth);
            int targetY = cardAreaY + Math.round(gapY * (row + 1) + row * cardHeight) - scrollOffset; // 考虑滚动偏移量
            modCards.get(i).setPosition(targetX, targetY, cardWidth, cardHeight);
        }
    }

    // 提供给详细界面调用，实现返回主菜单
    public void returnToMainMenu() {
        currentState = State.MAIN_MENU;
        currentModDetailGui = null;
    }
}