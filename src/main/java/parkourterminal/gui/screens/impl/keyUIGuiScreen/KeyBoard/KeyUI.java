package parkourterminal.gui.screens.impl.keyUIGuiScreen.KeyBoard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import parkourterminal.gui.component.fontRenderer.DDFontRenderer;
import parkourterminal.gui.layout.UIComponent;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.KeyBoard.intf.MoveMode;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.KeyBoard.intf.SideMode;
import parkourterminal.util.AnimationUtils.impls.interpolatingData.InterpolatingColor;
import parkourterminal.util.RenderTextHelper;
import parkourterminal.util.ShapeDrawer;
import parkourterminal.util.SystemOutHelper;

import javax.vecmath.Vector2f;
import java.io.IOException;

public class KeyUI extends UIComponent {
    public static int MAX_NAME_LENGTH=13;
    public static int DEFAULT_SIZE=40;
    private String actionName;
    private KeyBinding keyBinding;
    private String keyName="";
    private int backgroundColor =0x66afafaf;
    private int keyColor =0xFFFFFFFF;
    private int nameColor=0xFFFFFFFF;
    private int sideHoveredColor=0xFFFF0000;
    private int sideSelectedColor=0xFF00FF00;
    private int sideWidth =3;

    private final int minSize=10;

    private Vector2f oldMouseMovePos =new Vector2f(0,0);
    private MoveMode moveMode = MoveMode.MOVE;
    private SideMode sideMode= SideMode.NONE;

    private final DDFontRenderer keyRenderer;
    private final DDFontRenderer nameRenderer;

    public KeyUI(KeyBinding keyBinding,int x,int y,int width,int height) {
        setSize(width, height);
        setPosition(x,y);
        setKeyBinding(keyBinding);
        getAnimationColor().changeWithOutAnimation(new InterpolatingColor(backgroundColor));
        setAnimationTime(0.2f);
        keyRenderer=KeyUIManager.getInstance().getKeyRenderer();
        nameRenderer=KeyUIManager.getInstance().getNameRenderer();
    }
    public KeyUI(KeyBinding keyBinding) {
        this(keyBinding,0,0,DEFAULT_SIZE,DEFAULT_SIZE);
    }
    public KeyBinding getKeyBinding() {
        return keyBinding;
    }
    public String getKeyName() {
        return keyName;
    }
    public String getActionName() {
        return actionName;
    }

    public int getKeyColor() {
        return keyColor;
    }
    public int getNameColor() {
        return nameColor;
    }
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        if(isFocused()){
            if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
                if (keyCode == Keyboard.KEY_UP) {
                    resize(0,-1,SideMode.BOTTOM);
                } else if (keyCode == Keyboard.KEY_DOWN) {
                    resize(0,1,SideMode.BOTTOM);
                } else if (keyCode == Keyboard.KEY_LEFT) {
                    resize(-1,0,SideMode.RIGHT);
                } else if (keyCode == Keyboard.KEY_RIGHT) {
                    resize(1,0,SideMode.RIGHT);
                }
                return;
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
                if (keyCode == Keyboard.KEY_UP) {
                    resize(0,-1,SideMode.TOP);
                } else if (keyCode == Keyboard.KEY_DOWN) {
                    resize(0,1,SideMode.TOP);
                } else if (keyCode == Keyboard.KEY_LEFT) {
                    resize(-1,0,SideMode.LEFT);
                } else if (keyCode == Keyboard.KEY_RIGHT) {
                    resize(1,0,SideMode.LEFT);
                }
                return;
            }
            if (keyCode == Keyboard.KEY_UP) {
                move(0,-1,0);
            } else if (keyCode == Keyboard.KEY_DOWN) {
                move(0,1,0);
            } else if (keyCode == Keyboard.KEY_LEFT) {
                move(-1,0,0);
            } else if (keyCode == Keyboard.KEY_RIGHT) {
                move(1,0,0);
            }
        }
    }
    //移动,尺寸更改,监听按键,颜色更改,显示按键功能(只显示初始7个默认按键绑定情况(Fw,Bw,Lf,Rt,Jp,Sp,Sk),其他可以自定义但不能和已知重复)
    public boolean move(int deltaX, int deltaY, int clickedMouseButton) {
        GuiScreen guiScreen= Minecraft.getMinecraft().currentScreen;
        if(guiScreen==null){
            return false;
        }
        if(clickedMouseButton==1){
            return false;
        }
        Vector2f newPos =new Vector2f(getX()+deltaX,getY()+deltaY);
        if(clickedMouseButton==0){
            int validX= (int) MathHelper.clamp_float(newPos.x,0,guiScreen.width-getWidth());
            int validY= (int) MathHelper.clamp_float(newPos.y,0,guiScreen.height-getHeight());
            this.setPosition(validX,validY);
            return true;
        }
        return false;
    }
    public SideMode getHoveredSide(int mouseX, int mouseY) {

        int side=0;
        if(mouseY<=getY()+getHeight() &&mouseY>=getY()){
            if(mouseX<=getX()+ sideWidth &&mouseX>=getX()){//left
                side|=1;
            }else if(mouseX<=getX()+getWidth() &&mouseX>=getX()+getWidth()- sideWidth){//right
                side|=2;
            }
        }
        if(mouseX<=getX()+getWidth() &&mouseX>=getX()){
            if(mouseY<=getY()+ sideWidth &&mouseY>=getY()){//Top
                side|=4;
            }else if(mouseY<=getY()+getHeight() &&mouseY>=getY()+getHeight()- sideWidth){//bottom
                side|=8;
            }
        }
        switch (side) {
            case 1:  return SideMode.LEFT;
            case 2:  return SideMode.RIGHT;
            case 4:  return SideMode.TOP;
            case 5:  return SideMode.LEFT_TOP;
            case 6:  return SideMode.RIGHT_TOP;
            case 8:  return SideMode.BOTTOM;
            case 9:  return SideMode.LEFT_BOTTOM;
            case 10: return SideMode.RIGHT_BOTTOM;
            default: return SideMode.NONE;
        }
    }
    public void resetSize(){
        setSize(DEFAULT_SIZE,DEFAULT_SIZE);
    }
    public void resetColors(){
        setBackgroundColor(0xafafaf);
        keyColor =0xFFFFFFFF;
        nameColor=0xFFFFFFFF;
    }
    public boolean resize(int deltaX, int deltaY,SideMode sideMode){//直接选中的边高亮
        if(sideMode==SideMode.NONE){
            return false;
        }
        if(sideMode.isLeft()){
            setWidth(getWidth()-deltaX);
            setX(getX()+deltaX);
        }else if(sideMode.isRight()){
            setWidth(getWidth()+deltaX);
        }
        if(sideMode.isTop()){
            setHeight(getHeight()-deltaY);
            setY(getY()+deltaY);
        }else if (sideMode.isBottom()){
            setHeight(getHeight()+deltaY);
        }
        return true;
    }
    public void setNameColor(int color){
        nameColor =(color&0xFFFFFF)|0xFF000000;
    }
    public void setKeyColor(int color){
        keyColor =(color&0xFFFFFF)|0xFF000000;
    }
    public void setBackgroundColor(int color){
        backgroundColor =(color&0xFFFFFF)|0x66000000;
        getAnimationColor().RestartAnimation(new InterpolatingColor(backgroundColor));
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setActionName(String name){
        String validName="-";
        if (name.length() >= MAX_NAME_LENGTH) {
            validName = name.substring(0, MAX_NAME_LENGTH);
        } else {
            validName = name;
        }
        if(KeyUIManager.getInstance().putName(this.keyBinding,validName)){
            this.actionName=validName;
        }
    }
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton){
        if(mouseButton!=0){
            return false;
        }
        boolean contains = isMouseOver(mouseX,mouseY);
        if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)&&isEnabled()&&contains){
            setFocused(!isFocused());
            return true;
        }
        if(contains){
            setFocused(true);
            oldMouseMovePos.x=mouseX;
            oldMouseMovePos.y=mouseY;
            moveMode=MoveMode.MOVE;
            sideMode=getHoveredSide(mouseX,mouseY);
            if(sideMode!=SideMode.NONE){
                moveMode=MoveMode.RESIZE;
            }
        }
        setFocused(contains);
        return contains;
    }
    public void mouseReleased(int mouseX, int mouseY,int state){
        sideMode=SideMode.NONE;
        if(!Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)){
            setFocused(false);
        }
    }
    public boolean mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick){
        if(clickedMouseButton!=0){
            return false;
        }
        if(!isFocused()){
            return false;
        }
        GuiScreen guiScreen= Minecraft.getMinecraft().currentScreen;
        if(guiScreen==null){
            return false;
        }
        int deltaX= (int) (mouseX- oldMouseMovePos.x);
        int deltaY= (int) (mouseY- oldMouseMovePos.y);
        oldMouseMovePos.x=mouseX;
        oldMouseMovePos.y=mouseY;

        if(moveMode == MoveMode.MOVE&&move(deltaX,deltaY,clickedMouseButton)){
            return true;
        }
        if(moveMode == MoveMode.RESIZE&&resize(deltaX,deltaY,sideMode)){
            return true;
        }
        return false;
    }
    /**NotNull**/
    private void setKeyBinding( KeyBinding keyBinding){
        this.keyBinding=keyBinding;
        int keyCode = keyBinding.getKeyCode();
        if (keyCode < 0) {
            // 是鼠标按键
            int mouseIndex = keyCode + 100;
            if (mouseIndex >= 0 && mouseIndex < Mouse.getButtonCount()) {
                switch (mouseIndex) {
                    case 0: keyName ="LMB";break;
                    case 1: keyName ="RMB";break;
                    case 2: keyName ="MMB";break;
                    default: keyName = Mouse.getButtonName(mouseIndex);break;
                }
            } else {
                keyName = "Unknown";
            }
        } else {
            // 是键盘按键
            if (keyCode < Keyboard.KEYBOARD_SIZE) {
                keyName = Keyboard.getKeyName(keyCode);
            } else {
                keyName = "Unknown";
            }
        }
        actionName=KeyUIManager.getInstance().getAbbreviationName(keyBinding);
    }
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        raw_draw(mouseX, mouseY, partialTicks,false,false);
    }
    public void inGuiDraw() {
        raw_draw(-1,-1,0,false,true);
    }
    public void overlayDraw(){
        raw_draw(-1,-1,0,true,false);
    }
    public void raw_draw(int mouseX, int mouseY, float partialTicks, boolean overlay,boolean inGui){
        if(overlay&&!isEnabled()){
            return;
        }
        drawKeyBackGround(mouseX,mouseY,overlay);
        drawActionName();
        drawKeyName();
        drawSideHoveredAndInMoveSide(mouseX,mouseY,overlay,inGui);
    }
    private void drawKeyBackGround(int mouseX, int mouseY,boolean overlay){
        if(overlay){
            //dont use mouseX,mouseY if overlay
        }
        if(keyBinding.isKeyDown()||(isFocused()&&!overlay)){
            int keyPressedColor=(backgroundColor&0xFFFFFF)|0xee000000;
            getAnimationColor().RestartAnimation(new InterpolatingColor(keyPressedColor));
        }else{
            getAnimationColor().RestartAnimation(new InterpolatingColor(backgroundColor|0x66000000));
        }
        int keyColor=getAnimationColor().Update().getColor();
        ShapeDrawer.drawRoundedRect(getX(),getY(),getWidth(),getHeight(),keyColor,1);
    }
    private void drawKeyName(){
        if(isEnabled()){
            RenderTextHelper.drawCenteredString(keyRenderer,keyName,
                    getX(),getY(),getWidth(),getHeight(), keyColor,false);
        }else{
            RenderTextHelper.drawCenteredString(keyRenderer,keyName,
                    getX(),getY(),getWidth(),getHeight(),0xFF999999,false);
            int width=keyRenderer.getStringWidth(keyName);
            ShapeDrawer.drawLine(getX()+(getWidth()-width)/2f-2,getY()+getHeight()/2.0f-4,getX()+(getWidth()+width)/2f+2,getY()+getHeight()/2.0f-4,0xFF999999,1.2f);
        }
    }
    private void drawActionName(){
        nameRenderer.drawString(actionName,getX(),getY(),nameColor,true);
    }
    private void drawSideHoveredAndInMoveSide(int mouseX, int mouseY, boolean overlay,boolean inGui){
        if(overlay||inGui){
            return;
        }
        if(this.equals(KeyUIManager.getInstance().topKeyUIUnderMouse())){
            drawSides(getHoveredSide(mouseX,mouseY),sideHoveredColor);
        }
        if(isFocused()){
            drawSides(sideMode,sideSelectedColor);
        }
    }
    private void drawSides(SideMode sideMode,int color){
        if(sideMode==SideMode.NONE){
            return;
        }
        if(sideMode.isLeft()){
            ShapeDrawer.drawLine(getX(),getY(),getX(),getY()+getHeight(),color, sideWidth);
        }else if(sideMode.isRight()){
            ShapeDrawer.drawLine(getX()+getWidth(),getY(),getX()+getWidth(),getY()+getHeight(),color, sideWidth);
        }
        if(sideMode.isTop()){
            ShapeDrawer.drawLine(getX(),getY(),getX()+getWidth(),getY(),color, sideWidth);
        }else if(sideMode.isBottom()){
            ShapeDrawer.drawLine(getX(),getY()+getHeight(),getX()+getWidth(),getY()+getHeight(),color, sideWidth);
        }
    }
    public void setHeight(int height) {
        super.setHeight(Math.max(height,minSize));
    }

    public void setWidth(int width) {
        super.setWidth(Math.max(width,minSize));
    }
    public void setSize(int width, int height){
        super.setSize(Math.max(width,minSize),Math.max(height,minSize));
    }
}
