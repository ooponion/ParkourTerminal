package parkourterminal.util;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;

public class RenderTextHelper {
    public static void drawCenteredString(FontRenderer fontRendererObj,String string,float x, float y,float width,float height,int color,boolean dropShadow){
        float textY = y + (height -fontRendererObj.FONT_HEIGHT) / 2;
        float textX = x+ (width-fontRendererObj.getStringWidth(string))/2;
        fontRendererObj.drawString(string,textX, textY, color,dropShadow);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
