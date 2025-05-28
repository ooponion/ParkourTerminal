package parkourterminal.gui.screens.impl.macro_Screen.Components.modificationScreen;

import net.minecraft.client.Minecraft;
import parkourterminal.gui.Button.CustomButton;
import parkourterminal.gui.component.Div;
import parkourterminal.gui.component.scrollBar.intf.ScrollDirection;
import parkourterminal.gui.layout.*;
import parkourterminal.util.RenderTextHelper;
import parkourterminal.util.ShapeDrawer;

import java.util.ArrayList;
import java.util.Arrays;

public class ConfirmContainer extends Container{
    private boolean releaseDisable=false;
    private final CustomButton cancelButton;
    private final CustomButton deleteButton;
    private final Div title;

    private final FlexLayout flex2=new FlexLayout(LayoutDirection.HORIZONTAL,Alignment.STRETCH,Alignment.STRETCH,40);
    private final Container container2=new Container(flex2);
    public ConfirmContainer(int x,int y,int width,int height){
        super(new Margin(0),
                new Padding(0,0,0,0),
                new FlexLayout(LayoutDirection.VERTICAL,Alignment.FLEX_STRETCH,Alignment.STRETCH, new ArrayList<Integer>(Arrays.asList(2, 1)),0));
        setSize(width, height);
        setPosition(x,y);
        cancelButton=new CustomButton(width/3, height/3,0xee7f9ba7,0xee2e96c3,4,"cancel");
        deleteButton=new CustomButton(width/3, height/3,0xee7f9ba7,0xee2e96c3,4,"delete");
        container2.addComponent( cancelButton );
        container2.addComponent( deleteButton );
        container2.setPadding( new Padding(40,0,40,20));
        title=new Div(width, height/3) {
            @Override
            public void draw(int mouseX, int mouseY, float partialTicks) {
                Minecraft mc = Minecraft.getMinecraft();
                String text="Are you sure you want to delete this macro?";
                RenderTextHelper.drawCenteredString(mc.fontRendererObj,text,getX(),getY(),getWidth(), getHeight(),0xFFf0b921,false);
            }
        };
        addComponent(title);
        addComponent(container2);
        setEnabled(false);
    }
    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton){
        releaseDisable=false;
        boolean state =super.mouseClicked(mouseX, mouseY, mouseButton);
        if(state){
           releaseDisable=true;
        }
        return state;
    }

    public int ClickedButton(){
        if(cancelButton.isClicked()){
            return 0;
        }
        if(deleteButton.isClicked()){
            return 1;
        }
        return -1;
    }
    @Override
    public void mouseReleased(int mouseX, int mouseY,int state){
        super.mouseReleased(mouseX, mouseY, state);
        if(releaseDisable){
            setEnabled(false);
            releaseDisable=false;
        }
    }
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if(isEnabled()){
            ShapeDrawer.drawRoundedRect(getX(),getY(),getWidth(),getHeight(),0xee7a766d,6);
            ShapeDrawer.drawRoundedRectBorder(getX(),getY(),getWidth(),getHeight(),0xeef0a10c,6);
            super.draw(mouseX, mouseY, partialTicks);
        }
    }
//    @Override
//    public void setHeight(int height) {
//        super.setHeight(height);
//        for(UIComponent component : getComponents()){
//            component.setHeight(height/3);
//        }
//        getPadding().top= height*2/3;
//    }
//    @Override
//    public void setWidth(int width) {
//        super.setWidth(width);
//        for(UIComponent component : getComponents()){
//            component.setWidth(width/3);
//        }
//        setSpacing(width/9);
//        getPadding().left = width/9;
//
//    }
//    @Override
//    public void setSize(int width, int height) {
//        super.setSize(width,height);
//        for(UIComponent component : getComponents()){
//            component.setSize(width/3,height/3);
//        }
//    }
//    @Override
//    public void setPosition(int x, int y){
//        super.setPosition(x,y);
//        for(UIComponent component : getComponents()){
//            component.setPosition(x,y);
//        }
//    }
//    @Override
//    public void setX(int x) {
//        super.setX(x);
//        for(UIComponent component : getComponents()){
//            component.setX(x);
//        }
//    }
//    @Override
//    public void setY(int y) {
//        super.setY(y);
//        for(UIComponent component : getComponents()){
//            component.setY(y);
//        }
//    }
}
