package parkourterminal.gui.layout;

import java.util.List;

public interface LayoutManager {
    void layoutComponents(Container container);
    int getComponentsTotalHeight(Container container);
    int getComponentsTotalWidth(Container container);
    void setSpacing(int spacing);
}
