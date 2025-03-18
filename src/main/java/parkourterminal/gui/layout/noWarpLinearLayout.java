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
                layoutRow(row, container.getX(), container.getEntryTop()+(container.getEntryHeight()-rowHeight)/2, rowHeight,spacing);
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
                layoutColumn(column, container.getEntryRight()+(container.getEntryWidth()-columnWidth)/2, container.getY(), columnWidth,spacing);
            }
        }
    }
}
