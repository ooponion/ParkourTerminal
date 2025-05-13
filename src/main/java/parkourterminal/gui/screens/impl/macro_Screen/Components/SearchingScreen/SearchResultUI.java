package parkourterminal.gui.screens.impl.macro_Screen.Components.SearchingScreen;

import net.minecraft.client.Minecraft;
import parkourterminal.gui.layout.UIComponent;
import parkourterminal.util.AnimationUtils.impls.ColorInterpolateAnimation;
import parkourterminal.util.AnimationUtils.impls.interpolatingData.InterpolatingColor;
import parkourterminal.util.AnimationUtils.intf.AnimationMode;
import parkourterminal.util.RenderTextHelper;
import parkourterminal.util.ShapeDrawer;

public class SearchResultUI extends UIComponent {
    private String name;
    private boolean newMacro=false;
    private int backgroundColor = 0x00000000;
    private int borderColor = 0x80FFFFFF;
    private int highlightColor = 0x402196f3;
    private ColorInterpolateAnimation animationColor=new ColorInterpolateAnimation(0.4f,new InterpolatingColor(backgroundColor), AnimationMode.BLENDED);

    public String getName() {
        return name;
    }
    public SearchResultUI(int width,int height,String name, boolean newMacro){
        setSize(width, height);
        this.name=name;
        this.newMacro=newMacro;
    }

    public boolean isNewMacro() {
        return newMacro;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (isMouseOver(mouseX, mouseY)) {
            animationColor.RestartAnimation(new InterpolatingColor(highlightColor));
        } else {
            animationColor.RestartAnimation(new InterpolatingColor(backgroundColor));
        }
        ShapeDrawer.drawRoundedRect(getX(),getY(),getWidth(),getHeight(), animationColor.Update().getColor(), 6);
        ShapeDrawer.drawRoundedRectBorder(
                getX(),getY(),getWidth(),getHeight(),isNewMacro()?0x660caff0:0x66c1c1c1,6
        );
        String text = isNewMacro()?("+ "+name):name;
        RenderTextHelper.drawCenteredString(Minecraft.getMinecraft().fontRendererObj,text,getX(),getY(),getWidth(),getHeight(),isNewMacro()?0xFF0caff0:0xFFFFFFFF,false);
    }
}
