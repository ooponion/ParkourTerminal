package parkourterminal.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import java.util.Stack;

public class ScissorHelper {
//    public static void EnableScissor(int x,int y,int width,int height){
//        int scaleFactor = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
//
//        // 将逻辑坐标转换为物理坐标
//        int physicalX = x * scaleFactor;
//        int physicalY = y * scaleFactor;
//        int physicalWidth = width * scaleFactor;
//        int physicalHeight = height * scaleFactor;
//
//        // 启用 OpenGL 裁剪测试
//        GL11.glEnable(GL11.GL_SCISSOR_TEST);
//        // 设置裁剪区域（注意 Y 坐标转换）
//        GL11.glScissor(
//                physicalX, // 物理 X 坐标
//                Minecraft.getMinecraft().displayHeight - (physicalY + physicalHeight), // 物理 Y 坐标（转换）
//                physicalWidth, // 物理宽度
//                physicalHeight // 物理高度
//        );
//
//    }
//    public static void DisableScissor(){
//        // 关闭裁剪测试
//        GL11.glDisable(GL11.GL_SCISSOR_TEST);
//    }
    private static final Stack<ScissorRect> scissorStack = new Stack<ScissorRect>();

    public static void EnableScissor(int x, int y, int width, int height) {
        // 将 Y 轴转换为 OpenGL 底部原点
        int scaleFactor = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();

        // 将逻辑坐标转换为物理坐标
        int physicalX = x * scaleFactor;
        int physicalY = y * scaleFactor;
        int physicalWidth = width * scaleFactor;
        int physicalHeight = height * scaleFactor;
        ScissorRect newRect = new ScissorRect(physicalX, //物理 X 坐标
                Minecraft.getMinecraft().displayHeight - (physicalY + physicalHeight), // 物理 Y 坐标（转换）
                physicalWidth, // 物理宽度
                physicalHeight // 物理高度
                );

        if (!scissorStack.isEmpty()) {
            ScissorRect top = scissorStack.peek();
            newRect = top.intersect(newRect);
        }

        scissorStack.push(newRect);
        applyScissor(newRect);
    }

    public static void DisableScissor() {
        if (!scissorStack.isEmpty()) {
            scissorStack.pop();
            if (scissorStack.isEmpty()) {
                GL11.glDisable(GL11.GL_SCISSOR_TEST);
            } else {
                applyScissor(scissorStack.peek());
            }
        }
    }

    private static void applyScissor(ScissorRect rect) {
        if (rect.width <= 0 || rect.height <= 0) {
            GL11.glScissor(0, 0, 0, 0); // 无绘制区域
        } else {
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            GL11.glScissor(rect.x, rect.y, rect.width, rect.height);
        }
    }

    private static class ScissorRect {
        int x, y, width, height;

        public ScissorRect(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public ScissorRect intersect(ScissorRect other) {
            int nx = Math.max(this.x, other.x);
            int ny = Math.max(this.y, other.y);
            int nw = Math.min(this.x + this.width, other.x + other.width) - nx;
            int nh = Math.min(this.y + this.height, other.y + other.height) - ny;
            return new ScissorRect(nx, ny, Math.max(0, nw), Math.max(0, nh));
        }
    }
}
