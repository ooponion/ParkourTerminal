package parkourterminal.gui.Overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import parkourterminal.global.GlobalConfig;
import parkourterminal.global.json.TerminalJsonConfig;
import parkourterminal.util.NumberWrapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
@SideOnly(Side.CLIENT)
public class CoordinatesOverlay {
    private int boxX;
    private int boxY;
    private final int textInterval=10;
    private static final Minecraft mc = Minecraft.getMinecraft();
    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        // 仅在游戏 HUD 渲染时执行
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) return;

        ScaledResolution scaled = new ScaledResolution(mc);
        int screenWidth = scaled.getScaledWidth();
        int screenHeight = scaled.getScaledHeight();
        EntityPlayer player = mc.thePlayer;
        ItemStack heldItem = player.getHeldItem();

        if (heldItem == null || !heldItem.hasTagCompound())
            return;

        NBTTagCompound nbt = heldItem.getTagCompound();
        if (!nbt.hasKey("savedLocations"))
            return;

        NBTTagList savedLocations = nbt.getTagList("savedLocations", 10);
        int currentIndex = nbt.getInteger("selectedIndex");
        NBTTagCompound location = savedLocations.getCompoundTagAt(currentIndex);
        double x = location.getDouble("posX");
        double y = location.getDouble("posY");
        double z = location.getDouble("posZ");
        float yaw = location.getFloat("yaw");
        float pitch = location.getFloat("pitch");
        String name = location.getString("name");
        int precision= TerminalJsonConfig.getProperties().getPrecision();
        String dx = "posX: "+ new BigDecimal(x).setScale(precision, RoundingMode.HALF_UP);
        String dy ="posY: "+ new BigDecimal(y).setScale(precision, RoundingMode.HALF_UP);
        String dz ="posZ: "+ new BigDecimal(z).setScale(precision, RoundingMode.HALF_UP);
        String dYaw ="yaw: "+ new BigDecimal(yaw).setScale(precision, RoundingMode.HALF_UP);
        String dPitch ="pitch: "+ new BigDecimal(pitch).setScale(precision, RoundingMode.HALF_UP);

        int textX = boxX + 10;// 绘制文本，从boxX+10处开始，考虑横向滚动偏移
        int textY = boxY + 5; // 上边距固定为5像素
        mc.ingameGUI.drawString(mc.fontRendererObj, name, textX, textY, 0xFFAA00);
        mc.ingameGUI.drawString(mc.fontRendererObj, dx, textX, textY+textInterval, 0x55FFFF);
        mc.ingameGUI.drawString(mc.fontRendererObj, dy, textX, textY+textInterval*2, 0x55FFFF);
        mc.ingameGUI.drawString(mc.fontRendererObj, dz, textX, textY+textInterval*3, 0x55FFFF);
        mc.ingameGUI.drawString(mc.fontRendererObj, dYaw, textX, textY+textInterval*4, 0x55FFFF);
        mc.ingameGUI.drawString(mc.fontRendererObj, dPitch, textX, textY+textInterval*5, 0x55FFFF);
    }
}