package parkourterminal.util;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class ShapeDrawer {
    public static void drawRect(float left, float top, float right, float bottom, int color) {
        float a = (float)(color >> 24 & 255) / 255.0F;
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GlStateManager.color(r, g, b, a);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer wr = tessellator.getWorldRenderer();

        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        wr.pos(left, bottom, 0).endVertex();
        wr.pos(right, bottom, 0).endVertex();
        wr.pos(right, top, 0).endVertex();
        wr.pos(left, top, 0).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRoundedRect(float x, float y, float width, float height, int color, float radius) {
        float a = (float)(color >> 24 & 255) / 255.0F;
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GlStateManager.color(r, g, b, a);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer wr = tessellator.getWorldRenderer();

        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);

        // 绘制中间矩形主体
        ShapeDrawer.drawRect(x + radius, y + radius, x + width - radius, y + height - radius, wr);

        // 绘制四个边矩形
        // 上边
        drawRect(x + radius, y, x + width - radius, y + radius, wr);
        // 下边
        drawRect(x + radius, y + height - radius, x + width - radius, y + height, wr);
        // 左边
        drawRect(x, y + radius, x + radius, y + height - radius, wr);
        // 右边
        drawRect(x + width - radius, y + radius, x + width, y + height - radius, wr);

        tessellator.draw();

        // 绘制四个圆角（每个圆角分成4个三角形）
        wr.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION);
        GlStateManager.disableCull();

        drawCorner(x + radius, y + radius, radius, 180, 270, wr);  // 左上
        drawCorner(x + width - radius, y + radius, radius, 270, 360, wr);  // 右上
        drawCorner(x + radius, y + height - radius, radius, 90, 180, wr);  // 左下
        drawCorner(x + width - radius, y + height - radius, radius, 0, 90, wr);  // 右下

        tessellator.draw();
        GlStateManager.enableCull();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRoundedRectBorder(float x, float y, float width, float height, int color, float radius) {
        // 提取颜色分量
        float a = (float)(color >> 24 & 255) / 255.0F;
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;

        // 设置 OpenGL 状态
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GlStateManager.color(r, g, b, a);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer wr = tessellator.getWorldRenderer();

        // 使用 GL_LINE_STRIP 来连续绘制边框轮廓
        wr.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);

        // 1. 从上边缘的左侧开始：顶点 (x+radius, y)
        wr.pos(x + radius, y, 0).endVertex();

        // 2. 上边直线：到 (x+width - radius, y)
        wr.pos(x + width - radius, y, 0).endVertex();

        // 3. 右上角弧线：中心 (x+width - radius, y+radius)，角度 270° -> 360°
        addCornerVertices(wr, x + width - radius, y + radius, radius, 270, 360);

        // 4. 右边直线：从 (x+width, y+radius) 到 (x+width, y+height - radius)
        wr.pos(x + width, y + height - radius, 0).endVertex();

        // 5. 右下角弧线：中心 (x+width - radius, y+height - radius)，角度 0° -> 90°
        addCornerVertices(wr, x + width - radius, y + height - radius, radius, 0, 90);

        // 6. 下边直线：到 (x+radius, y+height)
        wr.pos(x + radius, y + height, 0).endVertex();

        // 7. 左下角弧线：中心 (x+radius, y+height - radius)，角度 90° -> 180°
        addCornerVertices(wr, x + radius, y + height - radius, radius, 90, 180);

        // 8. 左边直线：到 (x, y+radius)
        wr.pos(x, y + radius, 0).endVertex();

        // 9. 左上角弧线：中心 (x+radius, y+radius)，角度 180° -> 270°
        addCornerVertices(wr, x + radius, y + radius, radius, 180, 270);

        // 闭合轮廓：返回起点 (x+radius, y)
        wr.pos(x + radius, y, 0).endVertex();

        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    private static void addCornerVertices(WorldRenderer wr, float centerX, float centerY, float radius, int startAngle, int endAngle) {
        int segments = 10;
        float angleStep = (float)(endAngle - startAngle) / segments;
        for (int i = 1; i <= segments; i++) {
            float angle = (float)Math.toRadians(startAngle + i * angleStep);
            float posX = centerX + (float)Math.cos(angle) * radius;
            float posY = centerY + (float)Math.sin(angle) * radius;
            wr.pos(posX, posY, 0).endVertex();
        }
    }

    private static void drawRect(float left, float top, float right, float bottom, WorldRenderer wr) {
        wr.pos(left, bottom, 0).endVertex();
        wr.pos(right, bottom, 0).endVertex();
        wr.pos(right, top, 0).endVertex();
        wr.pos(left, top, 0).endVertex();
    }

    private static void drawCorner(float centerX, float centerY, float radius, int startAngle, int endAngle, WorldRenderer wr) {
        // 圆心的坐标
        double centerXPos = centerX;
        double centerYPos = centerY;

        // 增加细分段数使圆弧更平滑
        for (int i = startAngle; i < endAngle; i += 1) {
            // 当前角度
            double angle1 = Math.toRadians(i);
            double x1 = centerX + Math.cos(angle1) * radius;
            double y1 = centerY + Math.sin(angle1) * radius;

            // 下一个角度
            double angle2 = Math.toRadians(i + 1);
            double x2 = centerX + Math.cos(angle2) * radius;
            double y2 = centerY + Math.sin(angle2) * radius;

            // 绘制三角形（圆心 + 两个圆弧上的点）
            wr.pos(centerXPos, centerYPos, 0).endVertex(); // 圆心
            wr.pos(x1, y1, 0).endVertex(); // 当前点
            wr.pos(x2, y2, 0).endVertex(); // 下一个点
        }
    }

    public static void drawLine(float x1, float y1, float x2, float y2, int color) {
        // 提取颜色分量
        float a = (float)((color >> 24) & 255) / 255.0F;
        float r = (float)((color >> 16) & 255) / 255.0F;
        float g = (float)((color >> 8) & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;

        // 设置 OpenGL 状态
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GlStateManager.color(r, g, b, a);

        // 设置线宽为 1.0f
        GL11.glLineWidth(1.0f);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer wr = tessellator.getWorldRenderer();

        // 使用 GL_LINES 绘制一条直线
        wr.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
        wr.pos(x1, y1, 0).endVertex();
        wr.pos(x2, y2, 0).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    public static void drawScaledCustomSizeModalRect(float x, float y, float u, float v, float uWidth, float vHeight, float width, float height, float tileWidth, float tileHeight)
    {
        float f = 1.0F / tileWidth;
        float f1 = 1.0F / tileHeight;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + height, 0.0D).tex(u * f, (v + vHeight) * f1).endVertex();
        worldrenderer.pos(x + width, y + height, 0.0D).tex((u + uWidth) * f, (v + vHeight) * f1).endVertex();
        worldrenderer.pos(x + width, y, 0.0D).tex((u + uWidth) * f, v * f1).endVertex();
        worldrenderer.pos(x, y, 0.0D).tex(u * f, v * f1).endVertex();
        tessellator.draw();
    }
}