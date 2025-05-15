package parkourterminal.gui.component.fontRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import parkourterminal.util.ShapeDrawer;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DDFontRenderer extends FontRenderer {
    private final Minecraft mc;
    private final TextureManager textureManager;
    private final Map<Integer, ResourceLocation> charPageMap = new HashMap<Integer, ResourceLocation>();
    private final float fontHeight = 6.0f;
    private float fontScale = 3.0f;

    public DDFontRenderer(Minecraft mc) {
        super(mc.gameSettings, new ResourceLocation("parkourterminal", "fonts/alternate.png"), mc.getTextureManager(), false);
        this.mc = mc;
        this.textureManager = mc.getTextureManager();
        loadAllGlyphPages();
    }

    public void setFontScale(float scale) {
        this.fontScale = scale;
    }


    private void loadAllGlyphPages() {
        loadGlyphPage("fonts/ascii.png","fonts/ascii.properties", 0);
//        for (int i = 0; i < 256; i++) {
//            String name = String.format("glyph_%02x", i);
//            loadGlyphPage("fonts/" + name + ".png", i * 256);
//        }
//        for (int i = 0; i < 256; i++) {
//            String name = String.format("unicode_page_%02x", i);
//            loadGlyphPage("fonts/" + name + ".png", i * 256);
//        }
    }

    private void loadGlyphPage(String pngPath, int startChar) {
        ResourceLocation tex = new ResourceLocation("parkourterminal", pngPath);
        for (int i = 0; i < 256; i++) {
            int charCode = startChar + i;
            charPageMap.put(charCode, tex);
            charWidth[charCode] = 4;
        }
    }
    private void loadGlyphPage(String pngPath, String propPath, int startChar) {
        ResourceLocation tex = new ResourceLocation("parkourterminal", pngPath);
        ResourceLocation prop = new ResourceLocation("parkourterminal", propPath);

        InputStream input = null;
        try {
            // 尝试加载 .properties 文件
            try {
                input = mc.getResourceManager().getResource(prop).getInputStream();
                Properties props = new Properties();
                props.load(input);
                for (String key : props.stringPropertyNames()) {
                    if (key.startsWith("width.")) {
                        int index = Integer.parseInt(key.substring(6));
                        int charCode = startChar + index;
                        int width = Integer.parseInt(props.getProperty(key));
                        charWidth[charCode] = width;
                        charPageMap.put(charCode, tex);
                    }
                }
            } catch (IOException ignored) {
            }
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
    public float getCharWidthFloat(int charCode) {
        return (charWidth[charCode]==0?6:charWidth[charCode])* fontScale;
    }
    @Override
    public int getStringWidth(String text) {
        float width = 0;
        for (int i = 0; i < text.length(); i++) {
            width += charWidth[text.charAt(i)]*fontScale;
        }
        return (int) width;
    }
    @Override
    public int getCharWidth(char character){
        int width = charWidth[character];
        return (int)( (width==0?4:width) * fontScale);
    }
    @Override
    public int drawString(String text, int x, int y, int color) {
        return drawString(text, x, y, color, false);
    }
    @Override
    public int drawString(String text, float x, float y, int color, boolean dropShadow){
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        float posX = x+1;
        float width=0;
        if(dropShadow) {
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                drawChar(c, posX, y+1,(color & 16579836) >> 2 | color & -16777216);
                posX += getCharWidthFloat(c);
            }
        }
        posX = x;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            drawChar(c, posX, y, color);
            float j= getCharWidthFloat(c);
            posX += j;
            width += j;
        }
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        return (int) width;
    }
    @Override
    public int drawStringWithShadow(String text, float x, float y, int color) {
        return drawString(text, x, y, color, true);
    }

    private int drawChar(int codePoint, float x, float y, int color) {

        ResourceLocation tex = charPageMap.get(codePoint);
        int charWidth_ = charWidth[codePoint];
        if(charWidth_==0){
            return 4;
        }
        if (tex == null) return 0;


        float u1 = (codePoint % 16) * 8 / 128.0f;
        float v1 = (codePoint / 16 ) * 8 / 128.0f;
        float u2 = u1 + (charWidth_ / 128.0f);
        float v2 = v1 + (8 / 128.0f);
        float a = ((color >> 24) & 0xFF) / 255.0f;
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;

        float w = charWidth_ * fontScale;
        float h = fontHeight * fontScale;


        textureManager.bindTexture(tex);
        Tessellator tess = Tessellator.getInstance();
        WorldRenderer wr = tess.getWorldRenderer();
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        wr.pos(x, y + h, 0).tex(u1, v2).color(r,g,b,a).endVertex();
        wr.pos(x + w, y + h, 0).tex(u2, v2).color(r,g,b,a).endVertex();
        wr.pos(x + w, y, 0).tex(u2, v1).color(r,g,b,a).endVertex();
        wr.pos(x, y, 0).tex(u1, v1).color(r,g,b,a).endVertex();
        tess.draw();

        return (int )w;
    }
}
