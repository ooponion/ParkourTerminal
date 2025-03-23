package parkourterminal.gui.layout;

import java.util.ArrayList;
import java.util.List;

public class Container extends UIComponent {
    private boolean layoutManagerEnabled=true;
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
    public void setPosition(int x, int y){
        super.setPosition(x,y);
    }
    @Override
    public void setSize(int width, int height){
        super.setSize(width,height);
    }
    public void setMargin(Margin margin) {
        super.setMargin(margin);
    }

    public void setPadding(Padding padding) {
        super.setPadding(padding);
    }
    public void setX(int x) {
        super.setX(x);
    }

    public void setY(int y) {
        super.setY(y);
    }

    public void setHeight(int height) {
        super.setHeight(height);
    }

    public void setWidth(int width) {
        super.setWidth(width);
    }

    public boolean isLayoutManagerEnabled() {
        return layoutManagerEnabled;
    }

    public void setLayoutManagerEnabled(boolean layoutManagerEnabled) {
        this.layoutManagerEnabled = layoutManagerEnabled;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (layoutManager != null&&layoutManagerEnabled) {
            layoutManager.layoutComponents(this);
        }
        for (UIComponent component : components) {
            component.draw(mouseX, mouseY, partialTicks);
        }
    }
    public int getComponentsTotalHeight(){
        return this.layoutManager.getComponentsTotalHeight(this);
    };
    public int getComponentsTotalWidth(){
        return this.layoutManager.getComponentsTotalWidth(this);
    };
    public void Clear(){
        this.components.clear();
    }
    @Override
    public boolean isMouseOver(int mouseX, int mouseY) {
        return false; // 容器本身不处理点击
    }
    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton){
        boolean state=false;
        for (UIComponent component:getComponents()){
            state|=component.mouseClicked(mouseX, mouseY, mouseButton);
        }
        return state;
    }
    @Override
    public void mouseReleased(int mouseX, int mouseY){
        for (UIComponent component:getComponents()){
            component.mouseReleased(mouseX, mouseY);
        }
    }
    @Override
    public boolean mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick){
        boolean state=false;
        for (UIComponent component:getComponents()){
            state|=component.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        }
        return state;
    }
    public UIComponent getFocusedUI(){
        for (UIComponent component:getComponents()){
            if(component.isFocused()){
                return component;
            };
        }
        return null;
    }
}