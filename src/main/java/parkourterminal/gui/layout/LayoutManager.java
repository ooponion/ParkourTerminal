package parkourterminal.gui.layout;

import java.util.List;

public interface LayoutManager {
    void layoutComponents(Container container);
    /**
     * 布局一行中的组件：
     * 从 startX 开始，按顺序排列组件，
     * 并使每个组件在行内垂直居中（考虑其上下外边距）。
     */
    default void layoutRow(List<UIComponent> row, int startX, int y, int rowHeight,int spacing) {
        int currentX = startX;
        for (UIComponent comp : row) {
            comp.setX( currentX + comp.getMargin().left);
            int compTotalHeight = comp.getOuterHeight();
            int extraSpace = rowHeight - compTotalHeight;
            // 垂直居中：在上边距基础上加上额外空白的一半
            comp.setY( y + comp.getMargin().top + extraSpace / 2);
            currentX += comp.getOuterWidth()+spacing;
        }
    }

    /**
     * 布局一列中的组件：
     * 从 startY 开始，按顺序排列组件，
     * 并使每个组件在列内水平居中（考虑其左右外边距）。
     */
    default void layoutColumn(List<UIComponent> column, int x, int startY, int columnWidth,int spacing) {
        int currentY = startY;
        for (UIComponent comp : column) {
            comp.setY( currentY + comp.getMargin().top);
            int compTotalWidth = comp.getOuterWidth();
            int extraSpace = columnWidth - compTotalWidth;
            // 水平居中：在左边距基础上加上额外空白的一半
            comp.setX(x + comp.getMargin().left + extraSpace / 2);
            currentY += comp.getOuterHeight() + spacing;
        }
    }
}
