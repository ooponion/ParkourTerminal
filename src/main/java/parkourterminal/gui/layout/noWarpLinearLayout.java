package parkourterminal.gui.layout;

import java.util.ArrayList;
import java.util.List;

public class noWarpLinearLayout  implements LayoutManager{
    private LayoutDirection direction;
    private int spacing;

    public noWarpLinearLayout(LayoutDirection direction, int spacing) {
        this.direction = direction;
        this.spacing = spacing;
    }

    @Override
    public void layoutComponents(Container container) {
        if (direction == LayoutDirection.HORIZONTAL) {
            int currentX = container.getX();
            int rowHeight = 0;
            List<UIComponent> row = new ArrayList<UIComponent>();

            for (UIComponent comp : container.getComponents()) {
                if (!row.isEmpty()) {
                    currentX += spacing;
                }
                row.add(comp);
                int compTotalHeight = comp.getOuterHeight();
                if (compTotalHeight > rowHeight) {
                    rowHeight = compTotalHeight;
                }
                int compTotalWidth = comp.getOuterWidth();
                currentX += compTotalWidth;
            }
            // 布局
            if (!row.isEmpty()) {
                layoutRow(row, container.getEntryLeft(), container.getEntryTop()+(container.getEntryHeight()-rowHeight)/2, rowHeight,spacing);
            }
        } else { // VERTICAL 布局
            int currentY = container.getY();
            int columnWidth = 0;
            List<UIComponent> column = new ArrayList<UIComponent>();

            for (UIComponent comp : container.getComponents()) {
                if (!column.isEmpty()) {
                    currentY += spacing;
                }
                column.add(comp);
                int compTotalWidth = comp.getOuterWidth();
                if (compTotalWidth > columnWidth) {
                    columnWidth = compTotalWidth;
                }
                int compTotalHeight = comp.getOuterHeight();
                currentY += compTotalHeight;
            }
            // 布局
            if (!column.isEmpty()) {
                layoutColumn(column, container.getEntryLeft()+(container.getEntryWidth()-columnWidth)/2, container.getEntryTop(), columnWidth,spacing);
            }
        }
    }

    @Override
    public int getComponentsTotalHeight(Container container) {
        if (direction == LayoutDirection.HORIZONTAL) {
            int currentX = 0;
            int rowHeight = 0;
            List<UIComponent> row = new ArrayList<UIComponent>();

            for (UIComponent comp : container.getComponents()) {
                if (!row.isEmpty()) {
                    currentX += spacing;
                }
                row.add(comp);
                int compTotalHeight = comp.getOuterHeight();
                if (compTotalHeight > rowHeight) {
                    rowHeight = compTotalHeight;
                }
                int compTotalWidth = comp.getOuterWidth();
                currentX += compTotalWidth;
            }
            return rowHeight;
        } else { // VERTICAL 布局
            int currentY = 0;
            int columnWidth = 0;
            List<UIComponent> column = new ArrayList<UIComponent>();

            for (UIComponent comp : container.getComponents()) {
                if (!column.isEmpty()) {
                    currentY += spacing;
                }
                column.add(comp);
                int compTotalWidth = comp.getOuterWidth();
                if (compTotalWidth > columnWidth) {
                    columnWidth = compTotalWidth;
                }
                int compTotalHeight = comp.getOuterHeight();

                currentY += compTotalHeight;
            }
            return currentY;
        }
    }

    @Override
    public int getComponentsTotalWidth(Container container) {
        if (direction == LayoutDirection.HORIZONTAL) {
            int currentX = container.getX();
            int rowHeight = 0;
            List<UIComponent> row = new ArrayList<UIComponent>();

            for (UIComponent comp : container.getComponents()) {
                if (!row.isEmpty()) {
                    currentX += spacing;
                }
                row.add(comp);
                int compTotalHeight = comp.getOuterHeight();
                if (compTotalHeight > rowHeight) {
                    rowHeight = compTotalHeight;
                }
                int compTotalWidth = comp.getOuterWidth();
                currentX += compTotalWidth;
            }
            return currentX;
        } else { // VERTICAL 布局
            int currentY = container.getY();
            int columnWidth = 0;
            List<UIComponent> column = new ArrayList<UIComponent>();

            for (UIComponent comp : container.getComponents()) {
                if (!column.isEmpty()) {
                    currentY += spacing;
                }
                column.add(comp);
                int compTotalWidth = comp.getOuterWidth();
                if (compTotalWidth > columnWidth) {
                    columnWidth = compTotalWidth;
                }
                int compTotalHeight = comp.getOuterHeight();
                currentY += compTotalHeight;
            }
            return columnWidth;
        }
    }
    /**
     * 布局一行中的组件：
     * 从 startX 开始，按顺序排列组件，
     * 并使每个组件在行内垂直居中（考虑其上下外边距）。
     */
    private void layoutRow(List<UIComponent> row, int startX, int y, int rowHeight,int spacing) {
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
    private void layoutColumn(List<UIComponent> column, int x, int startY, int columnWidth,int spacing) {
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
