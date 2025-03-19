package parkourterminal.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

public class ScissorHelper {
    public static void EnableScissor(int x,int y,int width,int height){
        int scaleFactor = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();

        // 将逻辑坐标转换为物理坐标
        int physicalX = x * scaleFactor;
        int physicalY = y * scaleFactor;
        int physicalWidth = width * scaleFactor;
        int physicalHeight = height * scaleFactor;

        // 启用 OpenGL 裁剪测试
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        // 设置裁剪区域（注意 Y 坐标转换）
        GL11.glScissor(
                physicalX, // 物理 X 坐标
                Minecraft.getMinecraft().displayHeight - (physicalY + physicalHeight), // 物理 Y 坐标（转换）
                physicalWidth, // 物理宽度
                physicalHeight // 物理高度
        );

    }
    public static void DisableScissor(){
        // 关闭裁剪测试
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }
}
