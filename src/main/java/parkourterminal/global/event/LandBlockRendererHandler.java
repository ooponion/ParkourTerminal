package parkourterminal.global.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import parkourterminal.data.GlobalData;
import parkourterminal.global.json.TerminalJsonConfig;
import parkourterminal.util.BlockUtils;

import javax.vecmath.Vector3d;

public class LandBlockRendererHandler {


    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        float partialTicks = event.partialTicks;

        double x = mc.getRenderViewEntity().prevPosX + (mc.getRenderViewEntity().posX - mc.getRenderViewEntity().prevPosX) * partialTicks;
        double y = mc.getRenderViewEntity().prevPosY + (mc.getRenderViewEntity().posY - mc.getRenderViewEntity().prevPosY) * partialTicks;
        double z = mc.getRenderViewEntity().prevPosZ + (mc.getRenderViewEntity().posZ - mc.getRenderViewEntity().prevPosZ) * partialTicks;

        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        if(TerminalJsonConfig.getLandBlockJson().isDisplayable()){
            for (AxisAlignedBB bb: GlobalData.getLandingBlock().getAABBs()) {
                renderCube(x, y, z, BlockUtils.getMinPos(bb), BlockUtils.getMaxPos(bb), 0xf55a44);
            }
        }
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();

    }
    private void renderCube(double viewerX, double viewerY, double viewerZ, Vec3 ps1, Vec3 ps2, int color) {
        renderCube(viewerX,viewerY,viewerZ,new Vector3d(ps1.xCoord,ps1.yCoord,ps1.zCoord),new Vector3d(ps2.xCoord,ps2.yCoord,ps2.zCoord),color);
    }
    private void renderCube(double viewerX, double viewerY, double viewerZ, Vector3d ps1, Vector3d ps2, int color) {
        double x1 = ps1.getX() - viewerX;
        double y1 = ps1.getY() - viewerY;
        double z1 = ps1.getZ() - viewerZ;
        double x2 = ps2.getX() - viewerX;
        double y2 = ps2.getY() - viewerY;
        double z2 = ps2.getZ() - viewerZ;

        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        double distance = player.getDistance(x1, y1, z1); // 计算与线条的距离
        float lineWidth = (float) Math.max(3.0, 3.0 * (1.0 / distance)); // 远离时缩小
        double thickness=0.02d;
        GL11.glLineWidth(lineWidth);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(GL11.GL_QUADS, net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION_COLOR);
        addCube(worldRenderer, x1-thickness, y1-thickness, z1-thickness, x2+thickness, y2+thickness, z2+thickness,0x66000000+color);
        tessellator.draw();
        GL11.glLineWidth(lineWidth);
        worldRenderer.begin(GL11.GL_LINES, net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION_COLOR);
        renderCubeOutline(worldRenderer, x1-thickness, y1-thickness, z1-thickness, x2+thickness, y2+thickness, z2+thickness, 0xFF000000+color);
        tessellator.draw();

    }

    // 添加立方体的 6 个面
    private void addCube(WorldRenderer renderer, double x1, double y1, double z1, double x2, double y2, double z2, int color) {
        float a = (float)(color >> 24 & 255) / 255.0F;
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;

        renderer.pos(x1, y1, z1).color(r, g, b, a).endVertex();
        renderer.pos(x2, y1, z1).color(r, g, b, a).endVertex();
        renderer.pos(x2, y2, z1).color(r, g, b, a).endVertex();
        renderer.pos(x1, y2, z1).color(r, g, b, a).endVertex();

        // 绘制后面
        renderer.pos(x1, y1, z2).color(r, g, b, a).endVertex();
        renderer.pos(x2, y1, z2).color(r, g, b, a).endVertex();
        renderer.pos(x2, y2, z2).color(r, g, b, a).endVertex();
        renderer.pos(x1, y2, z2).color(r, g, b, a).endVertex();

        // 绘制左面
        renderer.pos(x1, y1, z1).color(r, g, b, a).endVertex();
        renderer.pos(x1, y1, z2).color(r, g, b, a).endVertex();
        renderer.pos(x1, y2, z2).color(r, g, b, a).endVertex();
        renderer.pos(x1, y2, z1).color(r, g, b, a).endVertex();

        // 绘制右面
        renderer.pos(x2, y1, z1).color(r, g, b, a).endVertex();
        renderer.pos(x2, y1, z2).color(r, g, b, a).endVertex();
        renderer.pos(x2, y2, z2).color(r, g, b, a).endVertex();
        renderer.pos(x2, y2, z1).color(r, g, b, a).endVertex();

        // 绘制顶部
        renderer.pos(x1, y2, z1).color(r, g, b, a).endVertex();
        renderer.pos(x2, y2, z1).color(r, g, b, a).endVertex();
        renderer.pos(x2, y2, z2).color(r, g, b, a).endVertex();
        renderer.pos(x1, y2, z2).color(r, g, b, a).endVertex();

        // 绘制底部
        renderer.pos(x1, y1, z1).color(r, g, b, a).endVertex();
        renderer.pos(x2, y1, z1).color(r, g, b, a).endVertex();
        renderer.pos(x2, y1, z2).color(r, g, b, a).endVertex();
        renderer.pos(x1, y1, z2).color(r, g, b, a).endVertex();


        renderer.pos(x1, y2, z1).color(r, g, b, a).endVertex();
        renderer.pos(x2, y2, z1).color(r, g, b, a).endVertex();
        renderer.pos(x2, y1, z1).color(r, g, b, a).endVertex();
        renderer.pos(x1, y1, z1).color(r, g, b, a).endVertex();

        // 绘制后面
        renderer.pos(x1, y2, z2).color(r, g, b, a).endVertex();
        renderer.pos(x2, y2, z2).color(r, g, b, a).endVertex();
        renderer.pos(x2, y1, z2).color(r, g, b, a).endVertex();
        renderer.pos(x1, y1, z2).color(r, g, b, a).endVertex();

        // 绘制左面
        renderer.pos(x1, y2, z1).color(r, g, b, a).endVertex();
        renderer.pos(x1, y2, z2).color(r, g, b, a).endVertex();
        renderer.pos(x1, y1, z2).color(r, g, b, a).endVertex();
        renderer.pos(x1, y1, z1).color(r, g, b, a).endVertex();

        // 绘制右面
        renderer.pos(x2, y2, z1).color(r, g, b, a).endVertex();
        renderer.pos(x2, y2, z2).color(r, g, b, a).endVertex();
        renderer.pos(x2, y1, z2).color(r, g, b, a).endVertex();
        renderer.pos(x2, y1, z1).color(r, g, b, a).endVertex();

        // 绘制顶部
        renderer.pos(x1, y2, z2).color(r, g, b, a).endVertex();
        renderer.pos(x2, y2, z2).color(r, g, b, a).endVertex();
        renderer.pos(x2, y2, z1).color(r, g, b, a).endVertex();
        renderer.pos(x1, y2, z1).color(r, g, b, a).endVertex();

        // 绘制底部
        renderer.pos(x1, y1, z2).color(r, g, b, a).endVertex();
        renderer.pos(x2, y1, z2).color(r, g, b, a).endVertex();
        renderer.pos(x2, y1, z1).color(r, g, b, a).endVertex();
        renderer.pos(x1, y1, z1).color(r, g, b, a).endVertex();
    }
    private void renderCubeOutline(WorldRenderer renderer, double x1, double y1, double z1, double x2, double y2, double z2, int color) {
        // 绘制 12 条边，每条边需要两个顶点
        float a = (float)(color >> 24 & 255) / 255.0F;
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;
        // 前面四条边
        addLine(renderer, x1, y1, z1, x2, y1, z1, r, g, b,a); // 底边
        addLine(renderer, x2, y1, z1, x2, y2, z1, r, g, b,a); // 右边
        addLine(renderer, x2, y2, z1, x1, y2, z1, r, g, b,a); // 顶边
        addLine(renderer, x1, y2, z1, x1, y1, z1, r, g, b,a); // 左边

        // 后面四条边
        addLine(renderer, x1, y1, z2, x2, y1, z2, r, g, b,a); // 底边
        addLine(renderer, x2, y1, z2, x2, y2, z2, r, g, b,a); // 右边
        addLine(renderer, x2, y2, z2, x1, y2, z2, r, g, b,a); // 顶边
        addLine(renderer, x1, y2, z2, x1, y1, z2, r, g, b,a); // 左边

        // 连接前后两面的四条边
        addLine(renderer, x1, y1, z1, x1, y1, z2, r, g, b,a); // 左前到左后
        addLine(renderer, x2, y1, z1, x2, y1, z2, r, g, b,a); // 右前到右后
        addLine(renderer, x2, y2, z1, x2, y2, z2, r, g, b,a); // 右上到右下
        addLine(renderer, x1, y2, z1, x1, y2, z2, r, g, b,a); // 左上到左下
    }

    // 绘制每条边
    private void addLine(WorldRenderer renderer, double x1, double y1, double z1, double x2, double y2, double z2, float r, float g, float b,float a) {
        renderer.pos(x1, y1, z1).color(r, g, b, a).endVertex();
        renderer.pos(x2, y2, z2).color(r, g, b, a).endVertex();
    }

}
