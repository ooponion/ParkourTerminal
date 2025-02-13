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

    public float fontScale;

    public ConsolaFontRenderer(Minecraft mc) {
        this(mc, 1.0f);
    }

    public ConsolaFontRenderer(Minecraft mc, float fontScale) {
        super(mc.gameSettings, CONSOLA_TEXTURE, mc.getTextureManager(), false);

        for (int i = 0; i < 256; i++)
            charWidth[i] = 4;

        this.fontScale = fontScale;
    }

    public void setFontScale(float fontScale) {
        this.fontScale = fontScale;
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

    @Override
    protected float renderDefaultChar(int ch, boolean italic) {
        // 获取字符宽度（原始宽度）
        int baseCharWidth = this.charWidth[ch];
        if (baseCharWidth == 0) {
            return 0;
        }

        // 获取字符在纹理中的坐标（保持不变）
        int texX = ch % 16 * 8;
        int texY = ch / 16 * 8;

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        this.bindTexture(this.locationFontTexture);

        // 根据缩放因子计算绘制时的宽度和高度
        float scaledWidth = 4 * fontScale;
        float scaledHeight = 8 * fontScale;
        float startX = this.posX;
        float startY = this.posY;
        float endX = startX + scaledWidth;
        float endY = startY + scaledHeight;

        // 纹理坐标仍然按照原始单元格大小计算
        float u1 = (float) texX / 128.0f;
        float u2 = (float) (texX + baseCharWidth * 2) / 128.0f;
        float v1 = (float) texY / 128.0f;
        float v2 = (float) (texY + 8) / 128.0f;

        // 绘制字符四边形
        worldRenderer.pos(startX, endY, 0.0).tex(u1, v2).endVertex();
        worldRenderer.pos(endX, endY, 0.0).tex(u2, v2).endVertex();
        worldRenderer.pos(endX, startY, 0.0).tex(u2, v1).endVertex();
        worldRenderer.pos(startX, startY, 0.0).tex(u1, v1).endVertex();

        tessellator.draw();

        // 返回绘制的宽度，用于计算下一个字符的位置
        return baseCharWidth * fontScale;
    }
}