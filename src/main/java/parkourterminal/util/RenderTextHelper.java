package parkourterminal.util;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class RenderTextHelper {
    public static void drawCenteredString(FontRenderer fontRendererObj,String string,float x, float y,float width,float height,int color,boolean dropShadow){
        GlStateManager.enableBlend(); // 开启混合
        GlStateManager.enableAlpha();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        float textY = y + (height -fontRendererObj.FONT_HEIGHT) / 2;
        float textX = x+ (width-fontRendererObj.getStringWidth(string))/2;
        fontRendererObj.drawString(string,textX, textY, color,dropShadow);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableBlend(); // 开启混合
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }
    public static int dimColor(int color, double factor) {
        int a = color & 0xFF000000;
        int r = (int)(((color >> 16) & 0xFF) * factor);
        int g = (int)(((color >> 8) & 0xFF) * factor);
        int b = (int)((color & 0xFF) * factor);
        return a | (r << 16) | (g << 8) | b;
    }
    public static void drawCenteredLinkBreakString(FontRenderer fontRendererObj,String string,float x, float y,float width,float height,int color,boolean dropShadow){
        GlStateManager.enableBlend(); // 开启混合
        GlStateManager.enableAlpha();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        List<String> lines = fontRendererObj.listFormattedStringToWidth(string, (int) width);
        int textY = (int) (y+(height-fontRendererObj.FONT_HEIGHT*lines.size())/2);
        for (String line : lines) {
            drawCenteredString(fontRendererObj,line, x, textY, width,fontRendererObj.FONT_HEIGHT , color, dropShadow);
            textY += fontRendererObj.FONT_HEIGHT;
        }
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableBlend(); // 开启混合
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }
}
