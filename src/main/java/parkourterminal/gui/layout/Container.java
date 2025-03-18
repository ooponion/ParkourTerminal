package parkourterminal.gui.layout;

import java.util.ArrayList;
import java.util.List;

public class Container extends UIComponent {
    private List<UIComponent> components = new ArrayList<UIComponent>();
    private LayoutManager layoutManager=new LinearLayout(LayoutDirection.HORIZONTAL,0);
    public Container(Margin margin,Padding padding,LayoutManager layoutManager){
        this(margin,padding);
        setLayoutManager(layoutManager);
    }
    public Container(Margin margin,Padding padding){
        this.setMargin(margin);
        this.setPadding(padding);
    }
    public Container(){
    }

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
    public void SetPosition(int x,int y){
        super.SetPosition(x,y);
        layoutManager.layoutComponents(this);
    }
    @Override
    public void SetSize(int width, int height){
        super.SetSize(width,height);
        layoutManager.layoutComponents(this);
    }
    public void setMargin(Margin margin) {
        super.setMargin(margin);
        layoutManager.layoutComponents(this);
    }

    public void setPadding(Padding padding) {
        super.setPadding(padding);
        layoutManager.layoutComponents(this);
    }
    public void setX(int x) {
        super.setX(x);
        layoutManager.layoutComponents(this);
    }

    public void setY(int y) {
        super.setY(y);
        layoutManager.layoutComponents(this);
    }

    public void setHeight(int height) {
        super.setHeight(height);
        layoutManager.layoutComponents(this);
    }

    public void setWidth(int width) {
        super.setWidth(width);
        layoutManager.layoutComponents(this);
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