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
            int containerLeft = container.getEntryLeft();
            int containerRight = container.getEntryRight();
            int currentX = containerLeft;
            int currentY = container.getEntryTop();
            int rowHeight = 0;
            List<UIComponent> row = new ArrayList<UIComponent>();

            for (UIComponent comp : container.getComponents()) {
                int compTotalWidth = comp.getOuterWidth();
                int additionalSpacing = row.isEmpty() ? 0 : spacing;

                // 如果加上当前组件后超出容器右边界，则先布局当前行，再换行
                if (currentX + additionalSpacing + compTotalWidth > containerRight) {
                    layoutRow(row, containerLeft, currentY, rowHeight,spacing);
                    currentY += rowHeight + spacing;
                    currentX = containerLeft;
                    row.clear();
                    rowHeight = 0;
                }
                if (!row.isEmpty()) {
                    currentX += spacing;
                }
                row.add(comp);
                int compTotalHeight = comp.getOuterHeight();
                if (compTotalHeight > rowHeight) {
                    rowHeight = compTotalHeight;
                }
                currentX += compTotalWidth;
            }
            // 布局最后一行
            if (!row.isEmpty()) {
                layoutRow(row, containerLeft, currentY, rowHeight,spacing);
            }
        } else { // VERTICAL 布局
            int containerTop = container.getEntryTop();
            int containerBottom = container.getEntryBottom();
            int currentY = containerTop;
            int currentX = container.getEntryLeft();
            int columnWidth = 0;
            List<UIComponent> column = new ArrayList<UIComponent>();

            for (UIComponent comp : container.getComponents()) {
                int compTotalHeight = comp.getOuterHeight();
                int additionalSpacing = column.isEmpty() ? 0 : spacing;

                if (currentY + additionalSpacing + compTotalHeight > containerBottom) {
                    layoutColumn(column, currentX, containerTop, columnWidth,spacing);
                    currentX += columnWidth + spacing;
                    currentY = containerTop;
                    column.clear();
                    columnWidth = 0;
                }
                if (!column.isEmpty()) {
                    currentY += spacing;
                }
                column.add(comp);
                int compTotalWidth = comp.getOuterWidth();
                if (compTotalWidth > columnWidth) {
                    columnWidth = compTotalWidth;
                }
                currentY += compTotalHeight;
            }
            if (!column.isEmpty()) {
                layoutColumn(column, currentX, containerTop, columnWidth,spacing);
            }
        }
    }

    @Override
    public int getComponentsTotalHeight(Container container) {
        if (direction == LayoutDirection.HORIZONTAL) {
            int containerRight = container.getEntryRight()-container.getEntryLeft();
            int currentX = 0;
            int currentY = 0;
            int rowHeight = 0;
            List<UIComponent> row = new ArrayList<UIComponent>();

            for (UIComponent comp : container.getComponents()) {
                int compTotalWidth = comp.getOuterWidth();
                int additionalSpacing = row.isEmpty() ? 0 : spacing;
                // 如果加上当前组件后超出容器右边界，则先布局当前行，再换行
                if (currentX + additionalSpacing + compTotalWidth > containerRight) {
                    currentX += comp.getOuterWidth();
                    //return currentX
                    currentY += rowHeight + spacing;
                    currentX = 0;
                    row.clear();
                    rowHeight = 0;
                }
                if (!row.isEmpty()) {
                    currentX += spacing;
                }
                row.add(comp);
                int compTotalHeight = comp.getOuterHeight();
                if (compTotalHeight > rowHeight) {
                    rowHeight = compTotalHeight;
                }
                currentX += compTotalWidth;
            }
            return currentY+rowHeight;
        } else { // VERTICAL 布局
            int containerBottom = container.getEntryBottom()-container.getEntryTop();
            int currentY = 0;
            int currentX = container.getEntryLeft();
            int columnWidth = 0;
            List<UIComponent> column = new ArrayList<UIComponent>();

            for (UIComponent comp : container.getComponents()) {
                int compTotalHeight = comp.getOuterHeight();
                int additionalSpacing = column.isEmpty() ? 0 : spacing;

                if (currentY + additionalSpacing + compTotalHeight > containerBottom) {
                    currentY+=comp.getOuterHeight();
                    return currentY;
                }
                if (!column.isEmpty()) {
                    currentY += spacing;
                }
                column.add(comp);
                int compTotalWidth = comp.getOuterWidth();
                if (compTotalWidth > columnWidth) {
                    columnWidth = compTotalWidth;
                }
                currentY += compTotalHeight;
            }
            //return currentX+=columnWidth;
        }
        return 0;
    }

    @Override
    public int getComponentsTotalWidth(Container container) {
        if (direction == LayoutDirection.HORIZONTAL) {
            int containerRight = container.getEntryRight()-container.getEntryLeft();
            int currentX = 0;
            int currentY = 0;
            int rowHeight = 0;
            List<UIComponent> row = new ArrayList<UIComponent>();

            for (UIComponent comp : container.getComponents()) {
                int compTotalWidth = comp.getOuterWidth();
                int additionalSpacing = row.isEmpty() ? 0 : spacing;
                // 如果加上当前组件后超出容器右边界，则先布局当前行，再换行
                if (currentX + additionalSpacing + compTotalWidth > containerRight) {
                    currentX += comp.getOuterWidth();
                    return currentX;
                }
                if (!row.isEmpty()) {
                    currentX += spacing;
                }
                row.add(comp);
                int compTotalHeight = comp.getOuterHeight();
                if (compTotalHeight > rowHeight) {
                    rowHeight = compTotalHeight;
                }
                currentX += compTotalWidth;
            }
            //return currentY+=rowHeight;
        } else { // VERTICAL 布局
            int containerBottom = container.getEntryBottom()-container.getEntryTop();
            int currentY = 0;
            int currentX = container.getEntryLeft();
            int columnWidth = 0;
            List<UIComponent> column = new ArrayList<UIComponent>();

            for (UIComponent comp : container.getComponents()) {
                int compTotalHeight = comp.getOuterHeight();
                int additionalSpacing = column.isEmpty() ? 0 : spacing;

                if (currentY + additionalSpacing + compTotalHeight > containerBottom) {
                    currentY+=comp.getOuterHeight();
                    //return currentY;
                    currentX += columnWidth + spacing;
                    currentY = 0;
                    column.clear();
                    columnWidth = 0;
                }
                if (!column.isEmpty()) {
                    currentY += spacing;
                }
                column.add(comp);
                int compTotalWidth = comp.getOuterWidth();
                if (compTotalWidth > columnWidth) {
                    columnWidth = compTotalWidth;
                }
                currentY += compTotalHeight;
            }
            return currentX+columnWidth;
        }
        return 0;
    }
    private void layoutRow(List<UIComponent> row, int startX, int y, int rowHeight,int spacing) {
        int currentX = startX;
        for (UIComponent comp : row) {
            int compTotalHeight = comp.getOuterHeight();
            int extraSpace = rowHeight - compTotalHeight;
            // 垂直居中：在上边距基础上加上额外空白的一半
            comp.setPosition(currentX + comp.getMargin().left,y + comp.getMargin().top + extraSpace / 2);
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

            int compTotalWidth = comp.getOuterWidth();
            int extraSpace = columnWidth - compTotalWidth;
            // 水平居中：在左边距基础上加上额外空白的一半
            comp.setPosition(x + comp.getMargin().left + extraSpace / 2,currentY + comp.getMargin().top);
            currentY += comp.getOuterHeight() + spacing;
        }
    }
}
