package parkourterminal.gui.layout;

import java.util.ArrayList;
import java.util.List;

public class Container extends UIComponent {
    private List<UIComponent> components = new ArrayList<UIComponent>();
    private LayoutManager layoutManager;

    public void setLayoutManager(LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public void addComponent(UIComponent component) {
        components.add(component);
    }

    public List<UIComponent> getComponents() {
        return components;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (layoutManager != null) {
            layoutManager.layoutComponents(this);
        }
        for (UIComponent component : components) {
            component.draw(mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public boolean isMouseOver(int mouseX, int mouseY) {
        return false; // 容器本身不处理点击
    }
}