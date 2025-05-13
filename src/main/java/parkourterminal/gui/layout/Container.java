package parkourterminal.gui.layout;

import parkourterminal.gui.component.scrollBar.impl.ScrollBarImpl;
import parkourterminal.gui.component.scrollBar.intf.ScrollDirection;
import parkourterminal.util.ShapeDrawer;

import java.util.ArrayList;
import java.util.List;

public class Container extends UIComponent {
    private boolean layoutManagerEnabled=true;
    private List<UIComponent> components = new ArrayList<UIComponent>();
    private LayoutManager layoutManager=new LinearLayout(LayoutDirection.HORIZONTAL,0);
    private boolean displayScrollBar=true;

    private final ScrollBarImpl scrollBar;
    private final ScrollDirection scrollDirection;
    public Container(Margin margin,Padding padding,LayoutManager layoutManager){
        this(margin,padding,layoutManager,ScrollDirection.VERTICAL);

    }
    public Container(Margin margin,Padding padding,LayoutManager layoutManager,ScrollDirection scrollDirection){
        this(margin,padding,scrollDirection);
        setLayoutManager(layoutManager);
    }
    public Container(Margin margin,Padding padding,ScrollDirection scrollDirection){
        this(scrollDirection);
        this.setMargin(margin);
        this.setPadding(padding);
    }
    public Container(Margin margin,Padding padding){
        this(margin,padding,ScrollDirection.VERTICAL);
    }
    public Container(ScrollDirection scrollDirection){
        this.scrollDirection=scrollDirection;
        scrollBar=new ScrollBarImpl(0,0,4,0, scrollDirection);
        scrollBar.setColor(0x00000000,0x40000000);
    }
    public Container(){
        this(ScrollDirection.VERTICAL);
    }
    public ScrollDirection getScrollDirection(){
        return scrollDirection;
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

    public void setMargin(Margin margin) {
        super.setMargin(margin);
    }

    public void setPadding(Padding padding) {
        super.setPadding(padding);
    }

    @Override
    public void Update(){
        if(scrollDirection==ScrollDirection.VERTICAL){
            scrollBar.UpdateContentSize(this.getComponentsTotalHeight()+ getPadding().bottom+ getPadding().top);
        }else{
            scrollBar.UpdateContentSize(this.getComponentsTotalWidth()+ getPadding().left+ getPadding().right);
        }
        System.out.printf("Container.scrollBar<%s\n",scrollBar);
    }
    @Override
    public void setX(int x) {
        setPosition(x, getY());
    }
    @Override
    public void setY(int y) {
        setPosition(getX(), y);
    }
    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        if(scrollDirection==ScrollDirection.VERTICAL){
            scrollBar.setHeight(height);
        }
    }
    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        if(scrollDirection==ScrollDirection.HORIZONTAL){
            scrollBar.setWidth(width);
        }
    }
    @Override
    public void setPosition(int x, int y){
        super.setPosition(x,y);
        if(scrollDirection==ScrollDirection.VERTICAL){
            scrollBar.setPosition(x+getWidth()-scrollBar.getWidth(),y);
        }
        else{
            scrollBar.setPosition(x,y+getHeight()-scrollBar.getHeight());
        }
    }
    @Override
    public void setSize(int width, int height){
        super.setSize(width,height);
        if(scrollDirection==ScrollDirection.VERTICAL){
            scrollBar.setHeight(height);
        }
        else{
            scrollBar.setWidth(width);
        }
    }
    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton){
        if(isEnabled()){
            boolean state=false;
            for (UIComponent component:getComponents()){
                state|=component.mouseClicked(mouseX, mouseY, mouseButton);
            }
            if(displayScrollBar){
                return state||scrollBar.mouseClicked(mouseX, mouseY, mouseButton);
            }
            return state;
        }
        return false;
    }
    @Override
    public boolean mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick){
        if(isEnabled()) {
            boolean state = false;
            for (UIComponent component : getComponents()) {
                state |= component.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
            }
            if (displayScrollBar) {
                scrollBar.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
                return state || scrollBar.isMouseOver(mouseX, mouseY);
            }
            return state;
        }
        return false;
    }
    @Override
    public void mouseReleased(int mouseX, int mouseY,int state){
        scrollBar.mouseReleased(mouseX,mouseY,state);
        for (UIComponent component:getComponents()){
            component.mouseReleased(mouseX, mouseY,state);
        }
    }
    public boolean isLayoutManagerEnabled() {
        return layoutManagerEnabled;
    }

    public void setLayoutManagerEnabled(boolean layoutManagerEnabled) {
        this.layoutManagerEnabled = layoutManagerEnabled;
    }
    public void displayScrollBar(boolean state){
        displayScrollBar=state;
    }
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if(isEnabled()){
            if (layoutManager != null&&layoutManagerEnabled) {
                layoutManager.layoutComponents(this);
            }
            for (UIComponent component : components) {
                component.draw(mouseX, mouseY, partialTicks);
            }

            if(displayScrollBar){
                scrollBar.draw(mouseX, mouseY, partialTicks);
            }else{
                scrollBar.Update();
            }
        }
    }
    public ScrollBarImpl getScrollBar(){
        return scrollBar;
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

    public void scrollWheel(int mouseX, int mouseY,int scrollAmount ){
        if(isMouseOver(mouseX, mouseY)){
            scrollBar.scrollWheel(scrollAmount);
        }
    }
    public UIComponent getFocusedUI(){
        for (UIComponent component:getComponents()){
            if(component.isFocused()){
                return component;
            };
        }
        return null;
    }
    public void deleteComponent(UIComponent component){
        if (component==null){
            return;
        }
        components.remove(component);
    }
    public void deleteComponents(){

        components.clear();
    }
    public boolean mouseOverPadding(int mouseX, int mouseY){
        return mouseX >= getEntryLeft() && mouseX <= getEntryRight()&&
                mouseY >= getEntryTop() && mouseY <= getEntryBottom();
    }
    public void setSpacing(int spacing){
        layoutManager.setSpacing(spacing);
    }
}