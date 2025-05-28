package parkourterminal.gui.layout;

import parkourterminal.gui.component.scrollBar.intf.ScrollDirection;

import java.util.ArrayList;
import java.util.List;

public class FlexLayout implements LayoutManager{
    private LayoutDirection flexDirection;
    private Alignment justifyContent;
    private Alignment alignItems;
    private List<Integer> flexGrows;
    private int spacing;

    public FlexLayout(LayoutDirection flexDirection, Alignment justifyContent,Alignment alignItems,List<Integer> flexGrows, int spacing) {
        this.flexDirection = flexDirection;
        this.justifyContent = justifyContent;
        this.alignItems = alignItems;
        this.flexGrows = flexGrows;
        this.spacing = spacing;
    }
    public FlexLayout(LayoutDirection flexDirection, Alignment justifyContent,Alignment alignItems, int spacing) {
        this(flexDirection, justifyContent,alignItems, new ArrayList<Integer>(), spacing);
    }
    public FlexLayout(LayoutDirection flexDirection) {
        this(flexDirection, Alignment.CENTER, Alignment.CENTER, new ArrayList<Integer>(), 0);
    }
    public int getSpacing() {
        return spacing;
    }
    @Override
    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }

    public LayoutDirection getFlexDirection() {
        return flexDirection;
    }
    public void setFlexDirection(LayoutDirection flexDirection) {
        this.flexDirection = flexDirection;
    }
    public Alignment getJustifyContent() {
        return justifyContent;
    }
    public void setJustifyContent(Alignment justifyContent) {
        this.justifyContent = justifyContent;
    }
    public Alignment getAlignItems() {
        return alignItems;
    }
    public void setAlignItems(Alignment alignItems) {
        this.alignItems = alignItems;
    }
    public List<Integer> getFlexGrows() {
        return flexGrows;
    }
    public void setFlexGrows(List<Integer> flexGrows) {
        this.flexGrows = flexGrows;
    }


    @Override
    public void layoutComponents(Container container) {
        if (flexDirection == LayoutDirection.HORIZONTAL) {
            List<UIComponent> row = new ArrayList<UIComponent>(container.getComponents());
            if (!row.isEmpty()) {
                layoutRow(container,row, container.getEntryLeft(), container.getEntryTop(),spacing);
            }
        } else { // VERTICAL 布局
            List<UIComponent> column = new ArrayList<UIComponent>(container.getComponents());
            if (!column.isEmpty()) {
                layoutColumn(container,column, container.getEntryLeft(), container.getEntryTop(),spacing);
            }
        }
    }

    @Override
    public int getComponentsTotalHeight(Container container) {
        if(flexDirection == LayoutDirection.HORIZONTAL) {
            return container.getEntryHeight();
        }
        List<UIComponent> comps = new ArrayList<UIComponent>(container.getComponents());
        int totalHeight = 0;
        for (UIComponent comp : comps) {
            totalHeight += comp.getOuterHeight();
        }
        totalHeight += spacing * (comps.size() - 1);
        return totalHeight;
    }

    @Override
    public int getComponentsTotalWidth(Container container) {
        if(flexDirection == LayoutDirection.VERTICAL) {
            return container.getEntryWidth();
        }
        List<UIComponent> comps = new ArrayList<UIComponent>(container.getComponents());
        int totalWidth = 0;
        for (UIComponent comp : comps) {
            totalWidth += comp.getOuterWidth();
        }
        totalWidth += spacing * (comps.size() - 1);
        return totalWidth;
    }
    /**
     * flex布局一行中的组件：
     */
    private void layoutRow(Container container, List<UIComponent> row, int baseX, int y, int spacing) {
        int rowHeight = container.getEntryHeight();
        int totalWidth = 0;
        for (UIComponent comp : row) {
            totalWidth += comp.getOuterWidth();
        }
        totalWidth += spacing * (row.size() - 1);

        int availableWidth = container.getEntryWidth() - totalWidth;

        // 先根据 justifyContent 计算起始 X 和调整 spacing
        int startX = baseX;
        switch (justifyContent) {
            case START:
                startX = baseX;
                break;
            case CENTER:
                startX = baseX + availableWidth / 2;
                break;
            case END:
                startX = baseX + availableWidth;
                break;
            case SPACE_BETWEEN:
                spacing = row.size() > 1 ? availableWidth / (row.size() - 1) : 0;
                startX = baseX;
                break;
            case SPACE_AROUND:
                spacing = row.size() > 0 ? availableWidth / (row.size() * 2) : 0;
                startX = baseX + spacing;
                break;
            case STRETCH:
                // 这里不处理宽度，后面由flex-grow来处理，先默认startX
                startX = baseX;
                break;
            case FLEX_STRETCH:
                startX = baseX;
                break;
        }

        // === 计算 flex-grow 总值 ===

        int totalFlexGrow = 0;
        for (int i = 0; i < row.size(); i++) {
            int grow = (i < flexGrows.size()) ? flexGrows.get(i) : 0;
            totalFlexGrow += grow;
        }

        // === flex-grow 宽度调整 ===
        if(justifyContent==Alignment.FLEX_STRETCH){
            int totalMinusSpacing=container.getEntryWidth()-spacing * (row.size() - 1);
                for (int i = 0; i < row.size(); i++) {
                UIComponent comp = row.get(i);
                int grow = (i < flexGrows.size()) ? flexGrows.get(i) : 0;
                if (grow > 0 && totalFlexGrow > 0) {
                    comp.setHeight(totalMinusSpacing * grow / totalFlexGrow);
                }
            }
        }else{
            for (int i = 0; i < row.size(); i++) {
                UIComponent comp = row.get(i);
                int grow = (i < flexGrows.size()) ? flexGrows.get(i) : 0;
                if (grow > 0 && totalFlexGrow > 0 && availableWidth > 0) {
                    int extraWidth = availableWidth * grow / totalFlexGrow;
                    comp.setWidth(comp.getWidth() + extraWidth);
                } else if (justifyContent == Alignment.STRETCH) {
                    // 如果是stretch且没有flex-grow，默认均分剩余空间
                    int extraPerComponent = row.size() > 0 ? availableWidth / row.size() : 0;
                    comp.setWidth(comp.getOuterWidth() + extraPerComponent - comp.getMargin().horizontal());
                }
            }
        }


        int currentX = startX;
        if (container.getScrollDirection() == ScrollDirection.HORIZONTAL) {
            currentX -= (int) container.getScrollBar().getInterpolatingContentOffset();
        } else {
            y -= (int) container.getScrollBar().getInterpolatingContentOffset();
        }

        for (UIComponent comp : row) {
            int compHeight = comp.getOuterHeight();
            int extraSpace = rowHeight - compHeight;

            // alignItems 控制纵向位置
            int offsetY = 0;
            switch (alignItems) {
                case START:
                    offsetY = 0;
                    break;
                case CENTER:
                    offsetY = extraSpace / 2;
                    break;
                case END:
                    offsetY = extraSpace;
                    break;
                case STRETCH:
                    comp.setHeight(rowHeight - comp.getMargin().vertical());
                    break;
                default:
                    break;
            }

            comp.setX(currentX + comp.getMargin().left);
            comp.setY(y + comp.getMargin().top + offsetY);

            currentX += comp.getOuterWidth() + spacing;
        }
    }

//    private void layoutRow(Container container,List<UIComponent> row, int baseX, int y,int spacing) {
//        int rowHeight=container.getEntryHeight();
//        int totalWidth = 0;
//        for (UIComponent comp : row) {
//            totalWidth += comp.getOuterWidth();
//        }
//        totalWidth += spacing * (row.size() - 1);
//
//        // 计算起始 X 根据 justifyContent
//        int startX = baseX;
//        int availableWidth = container.getEntryWidth() - totalWidth;
//
//        switch (justifyContent) {
//            case START: // 左对齐
//                startX = baseX;
//                break;
//            case CENTER: // 居中
//                startX = baseX + availableWidth / 2;
//                break;
//            case END: // 右对齐
//                startX = baseX + availableWidth;
//                break;
//            case SPACE_BETWEEN:
//                // spacing 会在下面每步中动态分配
//                spacing = row.size() > 1 ? availableWidth / (row.size() - 1) : 0;
//                startX = baseX;
//                break;
//            case SPACE_AROUND:
//                spacing = row.size() > 0 ? availableWidth / (row.size() * 2) : 0;
//                startX = baseX + spacing;
//                break;
//            case STRETCH:
//                // 将剩余高度均分给所有组件
//                int remaining = Math.max(0, availableWidth);
//                int extraPerComponent = row.size() > 0 ? remaining / row.size() : 0;
//                for (UIComponent comp : row) {
//                    int newWidth = comp.getOuterWidth() + extraPerComponent;
//                    comp.setWidth(newWidth - comp.getMargin().horizontal());
//                }
//                startX = baseX;
//                break;
//        }
//
//        int currentX = startX;
//        if (container.getScrollDirection() == ScrollDirection.HORIZONTAL) {
//            currentX -= (int) container.getScrollBar().getInterpolatingContentOffset();
//        } else {
//            y -= (int) container.getScrollBar().getInterpolatingContentOffset();
//        }
//
//        for (UIComponent comp : row) {
//            int compHeight = comp.getOuterHeight();
//            int extraSpace = rowHeight - compHeight;
//
//            // alignItems 控制纵向位置
//            int offsetY = 0;
//            switch (alignItems) {
//                case START:
//                    offsetY = 0;
//                    break;
//                case CENTER:
//                    offsetY = extraSpace / 2;
//                    break;
//                case END:
//                    offsetY = extraSpace;
//                    break;
//                case STRETCH:
//                    // 不修改 offsetY，只设置高度
//                    comp.setHeight(rowHeight - comp.getMargin().vertical());
//                    break;
//                default:break;//其他alignment都默认为start;
//            }
//
//            comp.setX(currentX + comp.getMargin().left);
//            comp.setY(y + comp.getMargin().top + offsetY);
//
//            currentX += comp.getOuterWidth() + spacing;
//        }
//    }

    /**
     * flex布局一列中的组件：
     */
    private void layoutColumn(Container container, List<UIComponent> column, int x, int baseY, int spacing) {
        int columnWidth = container.getEntryWidth();
        int totalHeight = 0;
        for (UIComponent comp : column) {
            totalHeight += comp.getOuterHeight();
        }
        totalHeight += spacing * (column.size() - 1);

        int availableHeight = container.getEntryHeight() - totalHeight;

        int startY = baseY;
        switch (justifyContent) {
            case START:
                startY = baseY;
                break;
            case CENTER:
                startY = baseY + availableHeight / 2;
                break;
            case END:
                startY = baseY + availableHeight;
                break;
            case SPACE_BETWEEN:
                spacing = column.size() > 1 ? availableHeight / (column.size() - 1) : 0;
                startY = baseY;
                break;
            case SPACE_AROUND:
                spacing = column.size() > 0 ? availableHeight / (column.size() * 2) : 0;
                startY = baseY + spacing;
                break;
            case STRETCH:
                // 先默认从 baseY 开始，剩余空间交给 flex-grow 处理
                startY = baseY;
                break;
            case FLEX_STRETCH:
                startY = baseY;
                break;
        }

        // 计算 flex-grow 总和和剩余空间

        int totalFlexGrow = 0;
        for (int i = 0; i < column.size(); i++) {
            int grow = (i < flexGrows.size()) ? flexGrows.get(i) : 0;
            totalFlexGrow += grow;
        }

        // 根据 flex-grow 分配剩余空间
        if(justifyContent==Alignment.FLEX_STRETCH){
            int totalMinusSpacing=container.getEntryHeight()-spacing * (column.size() - 1);
            for (int i = 0; i < column.size(); i++) {
                UIComponent comp = column.get(i);
                int grow = (i < flexGrows.size()) ? flexGrows.get(i) : 0;
                if (grow > 0 && totalFlexGrow > 0) {
                    comp.setHeight(totalMinusSpacing * grow / totalFlexGrow);
                }
            }
        }else{
            for (int i = 0; i < column.size(); i++) {
                UIComponent comp = column.get(i);
                int grow = (i < flexGrows.size()) ? flexGrows.get(i) : 0;
                if (grow > 0 && totalFlexGrow > 0 && availableHeight > 0) {
                    int extraHeight = availableHeight * grow / totalFlexGrow;
                    comp.setHeight(comp.getHeight() + extraHeight);
                } else if (justifyContent == Alignment.STRETCH) {
                    // 没有 flex-grow，均分剩余空间
                    int extraPerComponent = column.size() > 0 ? availableHeight / column.size() : 0;
                    comp.setHeight(comp.getOuterHeight() + extraPerComponent - comp.getMargin().vertical());
                }
            }
        }


        int currentY = startY;
        if (container.getScrollDirection() == ScrollDirection.HORIZONTAL) {
            x -= (int) container.getScrollBar().getInterpolatingContentOffset();
        } else {
            currentY -= (int) container.getScrollBar().getInterpolatingContentOffset();
        }

        for (UIComponent comp : column) {
            int compWidth = comp.getOuterWidth();
            int extraSpace = columnWidth - compWidth;

            // alignItems 控制横向位置
            int offsetX = 0;
            switch (alignItems) {
                case START:
                    offsetX = 0;
                    break;
                case CENTER:
                    offsetX = extraSpace / 2;
                    break;
                case END:
                    offsetX = extraSpace;
                    break;
                case STRETCH:
                    comp.setWidth(columnWidth - comp.getMargin().horizontal());
                    break;
                default:
                    break;
            }

            comp.setX(x + comp.getMargin().left + offsetX);
            comp.setY(currentY + comp.getMargin().top);

            currentY += comp.getOuterHeight() + spacing;
        }
    }

//    private void layoutColumn(Container container, List<UIComponent> column, int x, int baseY, int spacing) {
//        int columnWidth=container.getEntryWidth();
//        int totalHeight = 0;
//        for (UIComponent comp : column) {
//            totalHeight += comp.getOuterHeight();
//        }
//        totalHeight += spacing * (column.size() - 1);
//
//        // 计算起始 Y，根据 justifyContent
//        int startY = baseY;
//        int availableHeight = container.getEntryHeight() - totalHeight;
//
//        switch (justifyContent) {
//            case START:
//                startY = baseY;
//                break;
//            case CENTER:
//                startY = baseY + availableHeight / 2;
//                break;
//            case END:
//                startY = baseY + availableHeight;
//                break;
//            case SPACE_BETWEEN:
//                spacing = column.size() > 1 ? availableHeight / (column.size() - 1) : 0;
//                startY = baseY;
//                break;
//            case SPACE_AROUND:
//                spacing = column.size() > 0 ? availableHeight / (column.size() * 2) : 0;
//                startY = baseY + spacing;
//                break;
//            case STRETCH:
//                // 将剩余高度均分给所有组件
//                int remaining = Math.max(0, availableHeight);
//                int extraPerComponent = column.size() > 0 ? remaining / column.size() : 0;
//                for (UIComponent comp : column) {
//                    int newHeight = comp.getOuterHeight() + extraPerComponent;
//                    comp.setHeight(newHeight - comp.getMargin().vertical());
//                }
//                startY = baseY;
//                break;
//        }
//
//        int currentY = startY;
//        if (container.getScrollDirection() == ScrollDirection.HORIZONTAL) {
//            x -= (int) container.getScrollBar().getInterpolatingContentOffset();
//        } else {
//            currentY -= (int) container.getScrollBar().getInterpolatingContentOffset();
//        }
//
//        for (UIComponent comp : column) {
//            int compWidth = comp.getOuterWidth();
//            int extraSpace = columnWidth - compWidth;
//
//            int offsetX = 0;
//            switch (alignItems) {
//                case START:
//                    offsetX = 0;
//                    break;
//                case CENTER:
//                    offsetX = extraSpace / 2;
//                    break;
//                case END:
//                    offsetX = extraSpace;
//                    break;
//                case STRETCH:
//                    comp.setWidth(columnWidth - comp.getMargin().horizontal());
//                    break;
//                default:break;//其他alignment都默认为start;
//            }
//
//            comp.setX(x + comp.getMargin().left + offsetX);
//            comp.setY(currentY + comp.getMargin().top);
//
//            currentY += comp.getOuterHeight() + spacing;
//        }
//    }
}
