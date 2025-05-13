package parkourterminal.util.renderhelper;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import parkourterminal.data.landingblock.intf.Segment;
import parkourterminal.data.landingblock.intf.WholeCollisionBox;
import parkourterminal.global.json.TerminalJsonConfig;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;
import java.util.ArrayList;
import java.util.List;

public class LandBlockRendererHelper {
    public static void RenderWholeCollisionBox(double viewerX, double viewerY, double viewerZ, WholeCollisionBox wholeCollisionBox){
        List<Segment> segments=wholeCollisionBox.segments(false);
        int color=TerminalJsonConfig.getProperties().getLandBlockColor()+0x44000000;
        for(AxisAlignedBB box: wholeCollisionBox.getBoxes()){
            RenderTopAndBottom(viewerX,viewerY,viewerZ,box,color);
        }
        for(Segment segment:segments){
            RenderLineSides(viewerX,viewerY,viewerZ,segment,color);
            RenderLine(viewerX,viewerY,viewerZ,segment,color);
        }
    }
    public static void RenderTopAndBottom(double viewerX, double viewerY, double viewerZ,AxisAlignedBB box, int color){
        Vector3d negView =new Vector3d(-viewerX,-viewerY,-viewerZ);
        Vector3d p1=new Vector3d(box.minX, box.minY,box.minZ);
        Vector3d p2=new Vector3d(box.minX, box.minY,box.maxZ);
        Vector3d p3=new Vector3d(box.maxX, box.minY,box.maxZ);
        Vector3d p4=new Vector3d(box.maxX, box.minY,box.minZ);
        Vector3d p5=new Vector3d(box.minX, box.maxY,box.minZ);
        Vector3d p6=new Vector3d(box.minX, box.maxY,box.maxZ);
        Vector3d p7=new Vector3d(box.maxX, box.maxY,box.maxZ);
        Vector3d p8=new Vector3d(box.maxX, box.maxY,box.minZ);
        p1.add(negView);
        p2.add(negView);
        p3.add(negView);
        p4.add(negView);
        p5.add(negView);
        p6.add(negView);
        p7.add(negView);
        p8.add(negView);
        float a = (float)(color >> 24 & 255) / 255.0F;
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer renderer = tessellator.getWorldRenderer();
        renderer.begin(GL11.GL_QUADS, net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION_COLOR);
        renderer.pos(p1.x,p1.y,p1.z).color(r, g, b, a).endVertex();
        renderer.pos(p2.x,p2.y,p2.z).color(r, g, b, a).endVertex();
        renderer.pos(p3.x,p3.y,p3.z).color(r, g, b, a).endVertex();
        renderer.pos(p4.x,p4.y,p4.z).color(r, g, b, a).endVertex();

        renderer.pos(p4.x,p4.y,p4.z).color(r, g, b, a).endVertex();
        renderer.pos(p3.x,p3.y,p3.z).color(r, g, b, a).endVertex();
        renderer.pos(p2.x,p2.y,p2.z).color(r, g, b, a).endVertex();
        renderer.pos(p1.x,p1.y,p1.z).color(r, g, b, a).endVertex();

        renderer.pos(p5.x,p5.y,p5.z).color(r, g, b, a).endVertex();
        renderer.pos(p6.x,p6.y,p6.z).color(r, g, b, a).endVertex();
        renderer.pos(p7.x,p7.y,p7.z).color(r, g, b, a).endVertex();
        renderer.pos(p8.x,p8.y,p8.z).color(r, g, b, a).endVertex();

        renderer.pos(p8.x,p8.y,p8.z).color(r, g, b, a).endVertex();
        renderer.pos(p7.x,p7.y,p7.z).color(r, g, b, a).endVertex();
        renderer.pos(p6.x,p6.y,p6.z).color(r, g, b, a).endVertex();
        renderer.pos(p5.x,p5.y,p5.z).color(r, g, b, a).endVertex();
        tessellator.draw();
    }
    public static void RenderLine(double viewerX, double viewerY, double viewerZ,Segment line, int color){
        Vector3d negView =new Vector3d(-viewerX,-viewerY,-viewerZ);
        Vector2d pos1=line.getPos1().getPos();
        Vector2d pos2=line.getPos2().getPos();
        Vector3d p1=new Vector3d(pos1.x, line.getPos1().getMaxY(),pos1.y);
        Vector3d p2=new Vector3d(pos2.x,line.getPos2().getMaxY(),pos2.y);
        Vector3d p3=new Vector3d(pos1.x, line.getPos1().getMinY(),pos1.y);
        Vector3d p4=new Vector3d(pos2.x,line.getPos2().getMinY(),pos2.y);
        Vector3d p5=new Vector3d(pos1.x, line.getPos1().getMinY(),pos1.y);
        Vector3d p6=new Vector3d(pos1.x, line.getPos1().getMaxY(),pos1.y);
        Vector3d p7=new Vector3d(pos2.x,line.getPos2().getMinY(),pos2.y);
        Vector3d p8=new Vector3d(pos2.x,line.getPos2().getMaxY(),pos2.y);

        p1.add(negView);
        p2.add(negView);
        p3.add(negView);
        p4.add(negView);
        p5.add(negView);
        p6.add(negView);
        p7.add(negView);
        p8.add(negView);
        float a = (float)(color >> 24 & 255) / 255.0F;
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;

        double distance = p1.length();
        float lineWidth = (float) Math.max(3,Math.min(6, 3.0 * (1.0 / (distance))));


        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer renderer = tessellator.getWorldRenderer();
        renderer.begin(GL11.GL_LINES, net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION_COLOR);
        GL11.glLineWidth(lineWidth);
        renderer.pos(p1.x,p1.y,p1.z).color(r, g, b, a).endVertex();
        renderer.pos(p2.x,p2.y,p2.z).color(r, g, b, a).endVertex();
        renderer.pos(p3.x,p3.y,p3.z).color(r, g, b, a).endVertex();
        renderer.pos(p4.x,p4.y,p4.z).color(r, g, b, a).endVertex();
        renderer.pos(p5.x,p5.y,p5.z).color(r, g, b, a).endVertex();
        renderer.pos(p6.x,p6.y,p6.z).color(r, g, b, a).endVertex();
        renderer.pos(p7.x,p7.y,p7.z).color(r, g, b, a).endVertex();
        renderer.pos(p8.x,p8.y,p8.z).color(r, g, b, a).endVertex();
        tessellator.draw();
    }
    public static void RenderLineSides(double viewerX, double viewerY, double viewerZ,Segment line, int color){
        Vector3d negView =new Vector3d(-viewerX,-viewerY,-viewerZ);
        Vector2d pos1=line.getPos1().getPos();
        Vector2d pos2=line.getPos2().getPos();
        Vector3d p1 =new Vector3d(pos1.x, line.getPos1().getMinY(),pos1.y);
        Vector3d p2 =new Vector3d(pos1.x, line.getPos1().getMaxY(),pos1.y);
        Vector3d p3 =new Vector3d(pos2.x,line.getPos2().getMaxY(),pos2.y);
        Vector3d p4 =new Vector3d(pos2.x,line.getPos2().getMinY(),pos2.y);

        p1.add(negView);
        p2.add(negView);
        p3.add(negView);
        p4.add(negView);
        float a = (float)(color >> 24 & 255) / 255.0F;
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer renderer = tessellator.getWorldRenderer();
        renderer.begin(GL11.GL_QUADS, net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION_COLOR);
        renderer.pos(p1.x,p1.y,p1.z).color(r, g, b, a).endVertex();
        renderer.pos(p2.x,p2.y,p2.z).color(r, g, b, a).endVertex();
        renderer.pos(p3.x,p3.y,p3.z).color(r, g, b, a).endVertex();
        renderer.pos(p4.x,p4.y,p4.z).color(r, g, b, a).endVertex();

        renderer.pos(p4.x,p4.y,p4.z).color(r, g, b, a).endVertex();
        renderer.pos(p3.x,p3.y,p3.z).color(r, g, b, a).endVertex();
        renderer.pos(p2.x,p2.y,p2.z).color(r, g, b, a).endVertex();
        renderer.pos(p1.x,p1.y,p1.z).color(r, g, b, a).endVertex();
        tessellator.draw();
    }
}
