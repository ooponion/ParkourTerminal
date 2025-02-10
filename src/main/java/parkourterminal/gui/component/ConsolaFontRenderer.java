package parkourterminal.gui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ConsolaFontRenderer extends FontRenderer {
    private static final ResourceLocation CONSOLA_TEXTURE = new ResourceLocation("parkourterminal", "fonts/consola.png");

    public ConsolaFontRenderer(Minecraft mc) {
        super(mc.gameSettings, CONSOLA_TEXTURE, mc.getTextureManager(), false);

        for (int i = 0; i < 256; i++)
            charWidth[i] = 4;
    }

    @Override
    public void bindTexture(ResourceLocation location) {
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        textureManager.bindTexture(CONSOLA_TEXTURE);
    }

    @Override
    public int drawStringWithShadow(String text, float x, float y, int color) {
        return drawString(text, x, y, color, true);
    }

//    @Override
//    public int getStringWidth(String text) {
//        return Math.round(super.getStringWidth(text) * 0.5f);
//    }

    @Override
    protected float renderDefaultChar(int ch, boolean italic) {
        // 获取字符宽度
        int charWidth = this.charWidth[ch];
        if (charWidth == 0) {
            return 0;
        }

        // 获取字符的 UV 坐标
        int texX = ch % 16 * 8;
        int texY = ch / 16 * 8;

        // 绘制字符
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        this.bindTexture(this.locationFontTexture);

        // 调整 UV 坐标和顶点坐标
        float startX = this.posX;
        float startY = this.posY;
        float endX = startX + 4;
        float endY = startY + 8;

        float u1 = (float) texX / 128.0f;
        float u2 = (float) (texX + charWidth * 2) / 128.0f;
        float v1 = (float) texY / 128.0f;
        float v2 = (float) (texY + 8) / 128.0f;

        worldRenderer.pos(startX, endY, 0.0).tex(u1, v2).endVertex();
        worldRenderer.pos(endX, endY, 0.0).tex(u2, v2).endVertex();
        worldRenderer.pos(endX, startY, 0.0).tex(u2, v1).endVertex();
        worldRenderer.pos(startX, startY, 0.0).tex(u1, v1).endVertex();

        tessellator.draw();

        // 返回压缩后的宽度
        return charWidth;
    }
}