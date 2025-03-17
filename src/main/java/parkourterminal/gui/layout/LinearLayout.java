package parkourterminal.gui.layout;

import java.util.ArrayList;
import java.util.List;

public class LinearLayout implements LayoutManager {
    private LayoutDirection direction;
    private int spacing;

    public LinearLayout(LayoutDirection direction, int spacing) {
        this.direction = direction;
        this.spacing = spacing;
    }

    @Override
    public void layoutComponents(Container container) {
        if (direction == LayoutDirection.HORIZONTAL) {
            int containerLeft = container.getX();
            int containerRight = container.getX() + container.getWidth();
            int currentX = containerLeft;
            int currentY = container.getY();
            int rowHeight = 0;
            List<UIComponent> row = new ArrayList<UIComponent>();

            for (UIComponent comp : container.getComponents()) {
                int compTotalWidth = comp.margin.left + comp.getWidth() + comp.margin.right;
                int additionalSpacing = row.isEmpty() ? 0 : spacing;

                // 如果加上当前组件后超出容器右边界，则先布局当前行，再换行
                if (currentX + additionalSpacing + compTotalWidth > containerRight) {
                    layoutRow(row, containerLeft, currentY, rowHeight);
                    currentY += rowHeight + spacing;
                    currentX = containerLeft;
                    row.clear();
                    rowHeight = 0;
                }
                if (!row.isEmpty()) {
                    currentX += spacing;
                }
                row.add(comp);
                int compTotalHeight = comp.margin.top + comp.getHeight() + comp.margin.bottom;
                if (compTotalHeight > rowHeight) {
                    rowHeight = compTotalHeight;
                }
                currentX += compTotalWidth;
            }
            // 布局最后一行
            if (!row.isEmpty()) {
                layoutRow(row, containerLeft, currentY, rowHeight);
            }
        } else { // VERTICAL 布局
            int containerTop = container.getY();
            int containerBottom = container.getY() + container.getHeight();
            int currentY = containerTop;
            int currentX = container.getX();
            int columnWidth = 0;
            List<UIComponent> column = new ArrayList<UIComponent>();

            for (UIComponent comp : container.getComponents()) {
                int compTotalHeight = comp.margin.top + comp.getHeight() + comp.margin.bottom;
                int additionalSpacing = column.isEmpty() ? 0 : spacing;

                if (currentY + additionalSpacing + compTotalHeight > containerBottom) {
                    layoutColumn(column, container.getX(), currentX, containerTop, columnWidth);
                    currentX += columnWidth + spacing;
                    currentY = containerTop;
                    column.clear();
                    columnWidth = 0;
                }
                if (!column.isEmpty()) {
                    currentY += spacing;
                }
                column.add(comp);
                int compTotalWidth = comp.margin.left + comp.getWidth() + comp.margin.right;
                if (compTotalWidth > columnWidth) {
                    columnWidth = compTotalWidth;
                }
                currentY += compTotalHeight;
            }
            if (!column.isEmpty()) {
                layoutColumn(column, container.getX(), currentX, containerTop, columnWidth);
            }
        }
    }

    /**
     * 布局一行中的组件：
     * 从 startX 开始，按顺序排列组件，
     * 并使每个组件在行内垂直居中（考虑其上下外边距）。
     */
    private void layoutRow(List<UIComponent> row, int startX, int y, int rowHeight) {
        int currentX = startX;
        int n = row.size();
        for (int i = 0; i < n; i++) {
            UIComponent comp = row.get(i);
            comp.setX( currentX + comp.margin.left);
            int compTotalHeight = comp.margin.top + comp.getHeight() + comp.margin.bottom;
            int extraSpace = rowHeight - compTotalHeight;
            // 垂直居中：在上边距基础上加上额外空白的一半
            comp.setY( y + comp.margin.top + extraSpace / 2);
            currentX += comp.margin.left + comp.getWidth() + comp.margin.right;
            // 仅在非最后一个组件后增加间距
            if (i < n - 1) {
                currentX += spacing;
            }
        }
    }

    /**
     * 布局一列中的组件：
     * 从 startY 开始，按顺序排列组件，
     * 并使每个组件在列内水平居中（考虑其左右外边距）。
     */
    private void layoutColumn(List<UIComponent> column, int startX, int x, int startY, int columnWidth) {
        int currentY = startY;
        for (UIComponent comp : column) {
            comp.setY( currentY + comp.margin.top);
            int compTotalWidth = comp.margin.left + comp.getWidth() + comp.margin.right;
            int extraSpace = columnWidth - compTotalWidth;
            // 水平居中：在左边距基础上加上额外空白的一半
            comp.setX(x + comp.margin.left + extraSpace / 2);
            currentY += comp.margin.top + comp.getHeight() + comp.margin.bottom + spacing;
        }
    }
}
