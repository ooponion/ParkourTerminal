package parkourterminal.gui.screens.impl.macro_Screen.Components.modificationScreen;

import com.google.common.base.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import parkourterminal.gui.component.CustomGuiTextField;
import parkourterminal.gui.component.TextField;
import parkourterminal.gui.layout.UIComponent;
import parkourterminal.data.macroData.intf.Operation;
import parkourterminal.util.RenderTextHelper;
import parkourterminal.util.ShapeDrawer;

import javax.annotation.Nullable;

public class MacroLineUI extends UIComponent {
    private boolean selected=false;
    private final Operation operation;
    private final TextField yawText;
    private final TextField pitchText;
    private double partial;
    private static final ResourceLocation X_TEXTURE = new ResourceLocation("parkourterminal", "textures/overlay/x_slot.png");
    private static final ResourceLocation S_TEXTURE = new ResourceLocation("parkourterminal", "textures/overlay/s_slot.png");
    public MacroLineUI(int width, Operation operation){
        partial=this.getWidth()/16.5;
        this.operation=operation;
        Minecraft mc = Minecraft.getMinecraft();
        yawText=new TextField(0,mc.fontRendererObj,0,0, (int)(partial*2), (int) (partial+3));
        pitchText=new TextField(1,mc.fontRendererObj,0,0,(int)(partial*2), (int) (partial+3));
        setSize(width, 0);
        Predicate<String> predicate=new Predicate<String>() {
            @Override
            public boolean apply(@Nullable String input) {
                if (input == null) return false;
                if(input.isEmpty()||input.equals("-")||input.equals("+")){
                    return true;
                }
                try {
                    Float.parseFloat(input);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }

        };
        yawText.setValidator(predicate);
        pitchText.setValidator(predicate);
        yawText.setText(String.valueOf(operation.getdYaw()));
        pitchText.setText(String.valueOf(operation.getdPitch()));

    }
    public void setWidth(int width){
        super.setWidth(width);
        partial=this.getEntryWidth()/16.5;
        yawText.setWidth((int) (partial*2.5));
        pitchText.setWidth((int) (partial*2.5));
        setHeight((int) (partial+3));
        yawText.setHeight( (int) (partial+3));
        pitchText.setHeight( (int) (partial+3));
    }
    public void setSize(int width,int height){
        super.setWidth(width);
        partial=this.getEntryWidth()/16.5;
        super.setHeight((int) (partial+3));
        yawText.setWidth((int) (partial*2.5));
        pitchText.setWidth((int) (partial*2.5));
        yawText.setHeight( (int) (partial+3));
        pitchText.setHeight( (int) (partial+3));
    }
    public void setPosition(int x, int y){
        super.setPosition(x,y);
        yawText.setPosition((int) (partial*11.5)+x,y);
        pitchText.setPosition((int) (partial*14)+x,y);
    }
    public void setX(int x) {
        super.setX(x);
        yawText.setX((int) (partial*11.5)+x);
        pitchText.setX((int) (partial*14)+x);
    }

    public void setY(int y) {
        super.setY(y);
        yawText.setY(y);
        pitchText.setY(y);
    }
    @Override
    public void setFocused(boolean focused) {
        super.setFocused(focused);
        if(!focused){
            yawText.setFocused(false);
            pitchText.setFocused(false);
        }
    }
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {

        int height=this.getHeight();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableDepth();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft mc = Minecraft.getMinecraft();
        TextureManager textureManager=mc.getTextureManager();
        DrawOneSlot(textureManager,false,getX(),  (float)(partial*1.5),height);
        RenderTextHelper.drawCenteredString(mc.fontRendererObj,""+MacroContainer.getTickIndex(), getX(),getY(), (float) (partial*1.5),height,0x33FFFFFF,false);
        MacroContainer.incrementTickIndex();
        DrawOneSlot(textureManager,operation.getW(),(float) (partial*1.5)+getX(),  (float)(partial),height);
        DrawOneSlot(textureManager,operation.getS(), (float) (partial*2.5)+getX(),   (float)partial,height);
        DrawOneSlot(textureManager,operation.getA(), (float)(partial*3.5)+getX(),  (float)partial,height);
        DrawOneSlot(textureManager,operation.getD(), (float) (partial*4.5)+getX(),  (float) partial,height);
        DrawOneSlot(textureManager,operation.getSpace(), (float) (partial*5.5)+getX(),  (float)(partial*2),height);
        DrawOneSlot(textureManager,operation.getSneak(),  (float) (partial*7.5)+getX(),  (float) (partial*2),height);
        DrawOneSlot(textureManager,operation.getSprint(), (float) (partial*9.5)+getX(),  (float)(partial*2),height);
        DrawOneSlot(textureManager,false,  (float) (partial*11.5)+getX(),  (float) (partial*2.5),height);
        DrawOneSlot(textureManager,false, (float) (partial*14)+getX(),  (float)(partial*2.5),height);
        RenderTextHelper.drawCenteredString(mc.fontRendererObj,"yaw", (float) (partial*11.5)+getX(),getY(), (float) (partial*2.5),height,0x33FFFFFF,false);
        RenderTextHelper.drawCenteredString(mc.fontRendererObj,"pitch", (float) (partial*14)+getX(),getY(), (float) (partial*2.5),height,0x33FFFFFF,false);
        yawText.draw(mouseX, mouseY, partialTicks);
        pitchText.draw(mouseX, mouseY, partialTicks);
        if(selected){
            ShapeDrawer.drawRoundedRectBorder(getX(),getY(),getWidth(),getHeight(),0xFFFF0000,4);
        }
    }
    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton){
        boolean state=false;
        if(mouseY>=getEntryTop()&&mouseY<getEntryBottom()&&mouseX>=getEntryLeft()+partial*1.5&&mouseX<getEntryRight()){
            if(mouseX-getEntryLeft()<partial*2.5&&mouseX-getEntryLeft()>=partial*1.5){
                operation.toggleBool(0);
                state= true;
            }else if(mouseX-getEntryLeft()<partial*3.5){
                operation.toggleBool(1);
                state= true;
            }else if(mouseX-getEntryLeft()<partial*4.5){
                operation.toggleBool(2);
                state= true;
            }else if(mouseX-getEntryLeft()<partial*5.5){
                operation.toggleBool(3);
                state= true;
            }else if(mouseX-getEntryLeft()<partial*7.5){
                operation.toggleBool(4);
                state= true;
            }else if(mouseX-getEntryLeft()<partial*9.5){
                operation.toggleBool(5);
                state= true;
            }else if(mouseX-getEntryLeft()<partial*11.5){
                operation.toggleBool(6);
                state= true;
            }
        }
        if(isMouseOver(mouseX, mouseY)){
            if(yawText.mouseClicked(mouseX, mouseY, mouseButton)){
                pitchText.setFocused(false);
                state= true;
            }else if(pitchText.mouseClicked(mouseX, mouseY, mouseButton)){
                yawText.setFocused(false);
                state= true;
            }
        }
        return state;
    }
    @Override
    public void mouseReleased(int mouseX, int mouseY,int state){
        super.mouseReleased(mouseX, mouseY, state);
    }
    private void DrawOneSlot(TextureManager textureManager, boolean active, float x, float width, int height) {

        if (active) {
            textureManager.bindTexture(X_TEXTURE);
        } else {
            textureManager.bindTexture(S_TEXTURE);
        }
        ShapeDrawer.drawScaledCustomSizeModalRect(x, getY(), 0, 0, 64, 64, width, height, 64, 64);

    }
    public void keyTyped(char typedChar, int keyCode){
        yawText.textboxKeyTyped(typedChar, keyCode);
        pitchText.textboxKeyTyped(typedChar, keyCode);
        try{
            operation.setdPitch((float) Double.parseDouble(pitchText.getText()));
            operation.setdYaw((float) Double.parseDouble(yawText.getText()));
        }
        catch(Exception ignored){
        }
    }
    public void setSelected(boolean selected){
        this.selected=selected;
    }
    public boolean scrollWheel(int mouseX, int mouseY,int scrollAmount ){
        return yawText.scrollWheel(mouseX,mouseY,scrollAmount)||pitchText.scrollWheel(mouseX,mouseY,scrollAmount);

    }
}
