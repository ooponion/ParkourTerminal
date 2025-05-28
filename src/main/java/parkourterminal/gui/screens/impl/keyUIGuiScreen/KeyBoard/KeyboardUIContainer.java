package parkourterminal.gui.screens.impl.keyUIGuiScreen.KeyBoard;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import parkourterminal.gui.layout.*;
import parkourterminal.gui.layout.Container;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.ToolTipManager;

import javax.vecmath.Vector2f;
import java.awt.*;
import java.io.IOException;

public class KeyboardUIContainer extends ContainerKeyTypedAndOverlapped {
    private KeyUI rightClickedKeyUI;
    private KeyUI topKeyUIUnderMouse;
    public boolean boxSelection=false;
    public Vector2f clickPos=new Vector2f();
    public KeyboardUIContainer() {
        super();
        setLayoutManagerEnabled(false);
        displayScrollBar(false);
    }
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        if ((Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))
                && keyCode == Keyboard.KEY_A) {
            for (UIComponent component:getComponents()){
                component.setFocused(true);
            }
            return;
        }
        for (UIComponent component:getComponents()){
            ((KeyUI)component).keyTyped(typedChar,keyCode);
        }
    }
    public void mouseClickedToGetKeyUI(int mouseX, int mouseY, int mouseButton){
        if(mouseButton==1){
            for(UIComponent component:getComponents()){
                if(component.isMouseOver(mouseX,mouseY)){
                    rightClickedKeyUI= (KeyUI) component;
                    return;
                }
            }
            rightClickedKeyUI= null;
        }
    }
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton){
        boolean clickOnComponents=super.mouseClicked(mouseX,mouseY,mouseButton);
        boxSelection=!clickOnComponents;
        clickPos=new Vector2f(mouseX,mouseY);
        setFocused(true);
        return clickOnComponents;
    }

    public void mouseReleased(int mouseX, int mouseY,int state){
        super.mouseReleased(mouseX, mouseY, state);
    }
    public boolean mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick){
        if(boxSelection){
            for (UIComponent component:getComponents()){
                component.setFocused(isIntersecting((int) clickPos.x, (int) clickPos.y,mouseX,mouseY,component));
            }
            return true;
        }else{
            boolean state = false;
            for (UIComponent component:getComponents()){
                if(component.isFocused()){
                    state|=component.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
                }
            }
            return state;
        }
    }
    public static boolean isIntersecting(int x1,int y1,int x2,int y2, UIComponent ui) {
        if(x1>x2){
            int temp=x1;
            x1=x2;
            x2=temp;
        }
        if(y1>y2){
            int temp=y1;
            y1=y2;
            y2=temp;
        }
        Rectangle selectionBox=new Rectangle(x1,y1,x2-x1,y2-y1);
        return selectionBox.getX() < ui.getX() + ui.getWidth() &&
                selectionBox.getX() + selectionBox.getWidth() > ui.getX() &&
                selectionBox.getY() < ui.getY() + ui.getHeight() &&
                selectionBox.getY() + selectionBox.getHeight() > ui.getY();
    }
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        topKeyUIUnderMouse=null;
        if(isEnabled()){
            for (UIComponent component :getComponents()) {
                if(component.isMouseOver(mouseX,mouseY)){
                    topKeyUIUnderMouse= (KeyUI) component;
                    break;
                }
            }
            for (UIComponent component :getComponents()) {
                component.draw(mouseX, mouseY, partialTicks);
            }
        }
    }
    public void overlayDraw(){
        if(isEnabled()){
            for (UIComponent component :getComponents()) {
                ((KeyUI)component).overlayDraw();
            }
        }
    }
    public void inGuiDraw(){
        if(isEnabled()){
            for (UIComponent component :getComponents()) {
                ((KeyUI)component).inGuiDraw();
            }
        }
    }
    public boolean isOverAnyComponent(int mouseX, int mouseY){
        boolean over=false;
        for(UIComponent component:getComponents()){
            if(component.isMouseOver(mouseX,mouseY)){
                over=true;
                break;
            }
        }
        return over;
    }
    public void setComponentsEnabled(boolean enabled){
        for(UIComponent component:getComponents()){
            component.setEnabled(enabled);
        }
    }
    public KeyUI getRightClickedKeyUI() {
        return rightClickedKeyUI;
    }
    public KeyUI topKeyUIUnderMouse(){
        return topKeyUIUnderMouse;
    }

}
