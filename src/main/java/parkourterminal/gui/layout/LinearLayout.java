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


}
