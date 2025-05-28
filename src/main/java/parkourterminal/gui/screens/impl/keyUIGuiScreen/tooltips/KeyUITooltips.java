package parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import parkourterminal.gui.component.Slider.SliderImpl;
import parkourterminal.gui.component.scrollBar.intf.ScrollDirection;
import parkourterminal.gui.layout.*;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.KeyBoard.KeyUI;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.KeyBoard.KeyUIManager;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.PageManager;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.impl.createKeyPage.CreateKeyStruc;
import parkourterminal.util.ShapeDrawer;
import parkourterminal.util.SystemOutHelper;
import parkourterminal.util.listener.EventBus;
import parkourterminal.util.listener.EventListener;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;
import java.io.IOException;
import java.util.List;

public class KeyUITooltips extends ContainerKeyTyped implements EventListener {
    private final EventBus eventBus;
    private final PageManager pageManager;
    private final int TooltipColor=0xaa39aee9;
    private final int TooltipColorBorder=0xee0b4d6d;

    public KeyUITooltips(EventBus eventBus, PageManager pageManager) {
        this.eventBus = eventBus;
        this.pageManager = pageManager;
        setPadding(new Padding(5,10,5,10));
        setLayoutManager(new FlexLayout(LayoutDirection.VERTICAL,Alignment.START,Alignment.CENTER,4));
        setWidth(70);
        displayScrollBar(false);
        setEnabled(false);
        eventBus.register("createKeyEvent",this);
    }
    @Override
    public void Update(){
        setHeight(getComponentsTotalHeight()+getPadding().vertical());
        setWidth(getComponentsTotalWidth()+getPadding().horizontal());
    }
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if(!isEnabled()){
            return;
        }
        ShapeDrawer.drawRoundedRect(getX(),getY(),getWidth(),getHeight(), TooltipColor,4);
        ShapeDrawer.drawRoundedRectBorder(getX(),getY(),getWidth(),getHeight(), TooltipColorBorder,4);
        super.draw(mouseX, mouseY, partialTicks);
    }
    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton){
        if(isEnabled()&&isMouseOver(mouseX, mouseY)){
            if(super.mouseClicked(mouseX, mouseY, mouseButton)){
                setFocused(true);
                return true;
            };
            setFocused(false);
            setEnabled(false);
            return false;
        }
        if(!KeyUIManager.getInstance().getContainer().isOverAnyComponent(mouseX, mouseY)&& Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
            setPosition(mouseX,mouseY);
            pageManager.switchTo("createKeyPage");
            setEnabled(true);
            setFocused(true);
            return true;
        }
        KeyUI keyUI=KeyUIManager.getInstance().getRightClickedKeyUI();
        if(keyUI==null||mouseButton!=1){
            setFocused(false);
            setEnabled(false);
            return false;
        }

        setFocused(true);
        setEnabled(true);
        eventBus.emit("rightClickKeyUI", keyUI.isEnabled());
        pageManager.switchTo("guiPage");
        ToolTipManager.getInstance().getKeyUITooltips().setPosition(mouseX, mouseY);
        return true;
    }
    @Override
    public boolean mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick){
        if(isEnabled()) {
            for (UIComponent component : getComponents()) {
                if(component.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick)){
                    return true;
                };
            }
        }
        return false;
    }
    @Override
    public void setPosition(int x, int y){
        GuiScreen screen= Minecraft.getMinecraft().currentScreen;
        if(screen==null){
            return;
        }
        int newX= MathHelper.clamp_int(x,0,screen.width-getOuterWidth());
        int newY= MathHelper.clamp_int(y,0,screen.height-getOuterHeight());
        super.setPosition(newX,newY);
    }
    @Override
    public void setSize(int width, int height){
        super.setSize(width,height);
        setPosition(getX(),getY());
    }
    @Override
    public void setHeight(int height) {
        setSize(getWidth(),height);
    }
    @Override
    public void setWidth(int width) {
        setSize(width,getHeight());
    }

    @Override
    public void onEvent(String eventType, Object data) {
        if(eventType.equals("createKeyEvent")&&data instanceof CreateKeyStruc){
            CreateKeyStruc struct=(CreateKeyStruc)data;
            KeyUIManager.getInstance().addKeyUI(struct.keyBinding,struct.x,struct.y);
            pageManager.switchTo("guiPage");
            setEnabled(false);
            setFocused(false);
        }
    }
    public void mouseClickedInGui(int mouseX, int mouseY, int mouseButton){
//        if(!(isMouseOver(mouseX, mouseY)&&isEnabled())&&
//                mouseButton==1&&
//                KeyUIManager.getInstance().getContainer().isOverAnyComponent(mouseX, mouseY)){
//            setPosition(mouseX,mouseY);
//            pageManager.switchTo("homepage");
//            setEnabled(true);
//        }else{
//            if(isMouseOver(mouseX, mouseY)&&isEnabled()){
//                mouseClicked(mouseX, mouseY, mouseButton);
//            }else{
//                setEnabled(false);
//            }
//        }

        if(isMouseOver(mouseX, mouseY)&&isEnabled()){
            mouseClicked(mouseX, mouseY, mouseButton);
        }else{
            if((mouseButton==1&&KeyUIManager.getInstance().getContainer().isOverAnyComponent(mouseX, mouseY)||
                    (mouseButton==1&&KeyUIManager.getInstance().getContainer().isEmpty()))){
                setPosition(mouseX,mouseY);
                pageManager.switchTo("homepage");
                setEnabled(true);
            }
            else{
                setEnabled(false);
            }
        }
    }
    public void scrollWheel(int mouseX, int mouseY,int scrollAmount) {
        for(UIComponent component:getComponents()){
            if(component instanceof Scrollable&&component.isFocused()){
                if(((Scrollable) component).scrollWheel(mouseX,mouseY,scrollAmount)){
                    break;
                };
            }
        }
    }
//    public void deleteComponent(UIComponent component){
//        if (component==null){
//            return;
//        }
//        components.remove(component);
//        Update();
//    }
//    public void deleteComponents(List<UIComponent> comps){
//        for(UIComponent component:comps){
//            components.remove(component);
//        }
//        Update();
//    }
//    public void deleteComponents(){
//        components.clear();
//        Update();
//    }
//    public void addComponent(UIComponent component) {
//        if(component==null) return;
//        components.add(component);
//        Update();
//    }
//    public void addComponents(List<UIComponent> comps) {
//        for(UIComponent component : comps){
//            if(component==null) continue;
//            components.add(component);
//        }
//        Update();
//    }

}
