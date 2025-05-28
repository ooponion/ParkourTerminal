package parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.impl.createKeyPage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.opengl.GL11;
import parkourterminal.gui.Button.ButtonClickEventListener;
import parkourterminal.gui.Button.CustomButton;
import parkourterminal.gui.layout.KeyTyped;
import parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.ToolTipManager;
import parkourterminal.util.AnimationUtils.impls.ColorInterpolateAnimation;
import parkourterminal.util.AnimationUtils.impls.interpolatingData.InterpolatingColor;
import parkourterminal.util.AnimationUtils.intf.AbstractAnimation;
import parkourterminal.util.AnimationUtils.intf.AnimationMode;
import parkourterminal.util.RenderTextHelper;
import parkourterminal.util.ShapeDrawer;

import java.io.IOException;

public class StartBindingButton extends CustomButton implements KeyTyped {
    protected final int focusedColor;
    private boolean receiving=false;
    public StartBindingButton(int width, int height, int normalColor, int hoverColor,int focusedColor, int cornerRadius) {
        super(width, height, normalColor, hoverColor, cornerRadius, "Start binding");
        this.focusedColor = focusedColor;
        setClickEventListener(new ButtonClickEventListener() {
            @Override
            public void onButtonClick() {
                receiving=!receiving;
                setFocused(receiving);
                if(receiving){
                    ToolTipManager.getInstance().getEventBus().emit("startBindingEvent",null);
                }else{
                    ToolTipManager.getInstance().getEventBus().emit("bindingCancelEvent",null);
                }
            }
        });
    }
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        boolean isHovered = isMouseOver(mouseX, mouseY);
        if(isHovered){
            animationColor.RestartAnimation(new InterpolatingColor(hoverColor));
        }else if(isFocused()){
            animationColor.RestartAnimation(new InterpolatingColor(focusedColor));
        }else{
            animationColor.RestartAnimation(new InterpolatingColor(normalColor));
        }

        GL11.glEnable(GL11.GL_BLEND);
        ShapeDrawer.drawRoundedRect(getOuterLeft(),getOuterTop(),getOuterWidth(),getOuterHeight(), animationColor.Update().getColor(), cornerRadius);
        GL11.glDisable(GL11.GL_BLEND);

        RenderTextHelper.drawCenteredLinkBreakString(Minecraft.getMinecraft().fontRendererObj,text, getEntryLeft(),getEntryTop(),getEntryWidth(),getEntryHeight(),isEnabled()?0xFFFFFFFF:0xFFaaaaaa,true);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        ToolTipManager.getInstance().getEventBus().emit("bindingEvent",keyCode);
        receiving=!receiving;
        setFocused(false);
    }
    public void reset(){
        receiving=false;
    }
}
