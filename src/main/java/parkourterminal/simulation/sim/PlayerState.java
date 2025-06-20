package parkourterminal.simulation.sim;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import java.lang.reflect.Field;

public class PlayerState {
    public float width;
    public float height;

    public float moveStrafing;
    public float moveForward;
    public float rotationYaw;
    public float rotationPitch;
    public float prevRotationYaw;
    public float prevRotationPitch;
    public float jumpMovementFactor;
    public float fallDistance;
    public float stepHeight = 0.6F;

    public int jumpTicks=0;

    public World worldIn;

    public AxisAlignedBB boundingBox;

    public double posX, posY, posZ;
    public double motionX, motionY, motionZ;
    public double prevPosX,prevPosY,prevPosZ;


    public boolean onGround;
    public boolean noClip;
    public boolean isCollided;
    public boolean isCollidedHorizontally;
    public boolean isCollidedVertically;
    private boolean isInWeb=false;
    public boolean isOutsideBorder;
    public boolean isJumping=false;
    public boolean isInWater;
    public boolean isFlying;
    public boolean isSprinting;

    public float renderArmYaw;
    public float renderArmPitch;
    public float prevRenderArmYaw;
    public float prevRenderArmPitch;

    public float testMotion=0;

    public MovementInput movementInput;
    public PlayerCapabilities capabilities = new PlayerCapabilities();

    //display
    private float prevFrameTime=0L;
    private float eyeHeight=1.62F;
    private float smoothCamYaw=0;
    /** Smooth cam pitch */
    private float smoothCamPitch=0;
    /** Smooth cam filter X */
    private float smoothCamFilterX=0;
    /** Smooth cam filter Y */
    private float smoothCamFilterY=0;
    /** Smooth cam partial ticks */
    private float smoothCamPartialTicks=0;
    /** FOV modifier hand */
    private float fovModifierHand=0;
    /** FOV modifier hand prev */
    private float fovModifierHandPrev=0;
    private static float yaw=0;
    private static float pitch =0;
    public PlayerState(){
//        posX = 0;
//        posY = 0;
//        posZ = 0;
//        motionX = 0;
//        motionY = 0;
//        motionZ = 0;
//        onGround =true;
//        noClip = false;
//        isCollided = true;
//        isCollidedHorizontally = false;
//        isCollidedVertically = false;
//        isInWeb = false;
//        isOutsideBorder = player.isOutsideBorder();
//        jumpTicks=jumpTicks(player);
//        isJumping=isJumping(player);
//        isInWater=player.isInWater();
//        moveStrafing = player.moveStrafing;
//        moveForward = player.moveForward;
//        isFlying=player.capabilities.isFlying;
//        isSprinting=player.isSprinting();
//        rotationYaw=player.rotationYaw;
//        jumpMovementFactor=player.jumpMovementFactor;
//        fallDistance=player.fallDistance;
//        renderArmPitch=player.renderArmPitch;
//        rotationPitch=player.rotationPitch;
//        movementInput=new MovementInputFromOptions(Minecraft.getMinecraft().gameSettings);
//        movementInput.jump=player.movementInput.jump;
//        movementInput.moveForward=player.movementInput.moveForward;
//        movementInput.moveStrafe=player.movementInput.moveStrafe;
//        movementInput.sneak=player.movementInput.sneak;
    }
    public PlayerState(EntityPlayerSP player) {
        width=player.width;
        height=player.height;
        worldIn = player.worldObj;
        posX = player.posX;
        posY = player.posY;
        posZ = player.posZ;
        motionX = player.motionX;
        motionY = player.motionY;
        motionZ = player.motionZ;
        onGround = player.onGround;
        noClip = player.noClip;
        isCollided = player.isCollided;
        isCollidedHorizontally = player.isCollidedHorizontally;
        isCollidedVertically = player.isCollidedVertically;
        boundingBox = player.getEntityBoundingBox();
        stepHeight = player.stepHeight;
        isOutsideBorder = player.isOutsideBorder();
        isInWeb = isInWeb(player);
        jumpTicks=jumpTicks(player);
        isJumping=isJumping(player);
        isInWater=player.isInWater();
        moveStrafing = player.moveStrafing;
        moveForward = player.moveForward;
        isFlying=player.capabilities.isFlying;
        isSprinting=player.isSprinting();
        rotationYaw=player.rotationYaw;
        jumpMovementFactor=player.jumpMovementFactor;
        fallDistance=player.fallDistance;
        renderArmPitch=player.renderArmPitch;
        rotationPitch=player.rotationPitch;
        movementInput=new MovementInputFromOptions(Minecraft.getMinecraft().gameSettings);
        movementInput.jump=player.movementInput.jump;
        movementInput.moveForward=player.movementInput.moveForward;
        movementInput.moveStrafe=player.movementInput.moveStrafe;
        movementInput.sneak=player.movementInput.sneak;

    }
    public static void setFromPlayerState(Entity player) {
        PlayerState state = new PlayerState();
        player.posX = state.posX;
        player.posY = state.posY;
        player.posZ = state.posZ;
        player. motionX = state.motionX;
        player.motionY = state.motionY;
        player.motionZ = state.motionZ;
        player.rotationPitch = state.rotationPitch;
        player.rotationYaw = state.rotationYaw;
        player.prevRotationYaw = state.prevRotationYaw;
        player.prevRotationPitch = state.prevRotationPitch;
        player.lastTickPosX=player.prevPosX=state.prevPosX;
        player.lastTickPosY=player.prevPosY=state.prevPosY;
        player.lastTickPosZ=player.prevPosZ=state.prevPosZ;
    }

    public void setYawAndPitch(float yaw, float pitch) {
        setAngles(yaw,pitch);
    }
    public boolean isSneaking() {
        return movementInput != null && movementInput.sneak;
    }
    public boolean isUsingItem(){
        return false;
    }
    public void setInWeb(boolean isInWeb)
    {
        if(isInWeb){
            this.isInWeb = true;
            fallDistance = 0.0F;
        }else{
            this.isInWeb = false;
        }
    }
    private static boolean isInWeb(EntityPlayer player) {

        try {
            Field field = ReflectionHelper.findField(Entity.class, "isInWeb", "field_70134_J");
            field.setAccessible(true);
            return (boolean) field.get(player);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public boolean isInWeb() {
        return isInWeb;
    }
    private static int jumpTicks(EntityPlayer player) {
        try {
            Field field = ReflectionHelper.findField(EntityLivingBase.class, "jumpTicks", "field_70773_bE");
            field.setAccessible(true);
            return (int) field.get(player);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static boolean isJumping(EntityPlayer player) {
        try {
            Field field = ReflectionHelper.findField(EntityLivingBase.class, "isJumping", "field_70703_bu");
            field.setAccessible(true);
            return (boolean) field.get(player);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public boolean isInLava()
    {
        return worldIn.isMaterialInBB(boundingBox.expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.lava);
    }
    public boolean isOnLadder()
    {
        int i = MathHelper.floor_double(posX);
        int j = MathHelper.floor_double(boundingBox.minY);
        int k = MathHelper.floor_double(posZ);
        Block block = worldIn.getBlockState(new BlockPos(i, j, k)).getBlock();
        return block != null && block.isLadder(worldIn, new BlockPos(i, j, k), null);
    }
    public void updateCameraAndRender(float partialTicks, long nanoTime)
    {
        Minecraft mc=Minecraft.getMinecraft();
        boolean flag = Display.isActive();

        if (!flag && mc.gameSettings.pauseOnLostFocus && (!mc.gameSettings.touchscreen || !Mouse.isButtonDown(1)))
        {
            if (Minecraft.getSystemTime() - prevFrameTime > 500L)
            {
                //mc.displayInGameMenu();
                //DisplayText "You lose focus"
            }
        }
        else
        {
            prevFrameTime = Minecraft.getSystemTime();
        }

        if (flag && Minecraft.isRunningOnMac && mc.inGameHasFocus && !Mouse.isInsideWindow())
        {
            Mouse.setGrabbed(false);
            Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
            Mouse.setGrabbed(true);
        }

        if (mc.inGameHasFocus && flag)
        {
            float f = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
            float f1 = f * f * f * 8.0F;
            float f2 = (float)mc.mouseHelper.deltaX * f1;
            float f3 = (float)mc.mouseHelper.deltaY * f1;
            int i = 1;

            if (mc.gameSettings.invertMouse)
            {
                i = -1;
            }

            if (mc.gameSettings.smoothCamera)
            {
                smoothCamYaw += f2;
                smoothCamPitch += f3;
                float f4 = partialTicks - smoothCamPartialTicks;
                smoothCamPartialTicks = partialTicks;
                f2 = smoothCamFilterX * f4;
                f3 = smoothCamFilterY * f4;
                setAngles(f2, f3 * (float)i);
            }
            else
            {
                smoothCamYaw = 0.0F;
                smoothCamPitch = 0.0F;
                setAngles(f2, f3 * (float)i);
            }
        }


//        if (!mc.skipRenderWorld)
//        {
//            anaglyphEnable = mc.gameSettings.anaglyph;
//            final ScaledResolution scaledresolution = new ScaledResolution(mc);
//            int i1 = scaledresolution.getScaledWidth();
//            int j1 = scaledresolution.getScaledHeight();
//            final int k1 = Mouse.getX() * i1 / mc.displayWidth;
//            final int l1 = j1 - Mouse.getY() * j1 / mc.displayHeight - 1;
//            int i2 = mc.gameSettings.limitFramerate;
//
//            if (mc.theWorld != null)
//            {
//                mc.mcProfiler.startSection("level");
//                int j = Math.min(Minecraft.getDebugFPS(), i2);
//                j = Math.max(j, 60);
//                long k = System.nanoTime() - nanoTime;
//                long l = Math.max((long)(1000000000 / j / 4) - k, 0L);
//                renderWorld(partialTicks, System.nanoTime() + l);
//
//                if (OpenGlHelper.shadersSupported)
//                {
//                    mc.renderGlobal.renderEntityOutlineFramebuffer();
//
//                    if (theShaderGroup != null && useShader)
//                    {
//                        GlStateManager.matrixMode(5890);
//                        GlStateManager.pushMatrix();
//                        GlStateManager.loadIdentity();
//                        theShaderGroup.loadShaderGroup(partialTicks);
//                        GlStateManager.popMatrix();
//                    }
//
//                    mc.getFramebuffer().bindFramebuffer(true);
//                }
//
//                renderEndNanoTime = System.nanoTime();
//                mc.mcProfiler.endStartSection("gui");
//
//                if (!mc.gameSettings.hideGUI || mc.currentScreen != null)
//                {
//                    GlStateManager.alphaFunc(516, 0.1F);
//                    mc.ingameGUI.renderGameOverlay(partialTicks);
//                }
//
//                mc.mcProfiler.endSection();
//            }
//            else
//            {
//                GlStateManager.viewport(0, 0, mc.displayWidth, mc.displayHeight);
//                GlStateManager.matrixMode(5889);
//                GlStateManager.loadIdentity();
//                GlStateManager.matrixMode(5888);
//                GlStateManager.loadIdentity();
//                setupOverlayRendering();
//                renderEndNanoTime = System.nanoTime();
//            }
//
//            if (mc.currentScreen != null)
//            {
//                GlStateManager.clear(256);
//
//                try
//                {
//                    net.minecraftforge.client.ForgeHooksClient.drawScreen(mc.currentScreen, k1, l1, partialTicks);
//                }
//                catch (Throwable throwable)
//                {
//                    CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering screen");
//                    CrashReportCategory crashreportcategory = crashreport.makeCategory("Screen render details");
//                    crashreportcategory.addCrashSectionCallable("Screen name", new Callable<String>()
//                    {
//                        public String call() throws Exception
//                        {
//                            return EntityRenderer.mc.currentScreen.getClass().getCanonicalName();
//                        }
//                    });
//                    crashreportcategory.addCrashSectionCallable("Mouse location", new Callable<String>()
//                    {
//                        public String call() throws Exception
//                        {
//                            return String.format("Scaled: (%d, %d). Absolute: (%d, %d)", new Object[] {Integer.valueOf(k1), Integer.valueOf(l1), Integer.valueOf(Mouse.getX()), Integer.valueOf(Mouse.getY())});
//                        }
//                    });
//                    crashreportcategory.addCrashSectionCallable("Screen size", new Callable<String>()
//                    {
//                        public String call() throws Exception
//                        {
//                            return String.format("Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d", new Object[] {Integer.valueOf(scaledresolution.getScaledWidth()), Integer.valueOf(scaledresolution.getScaledHeight()), Integer.valueOf(EntityRenderer.mc.displayWidth), Integer.valueOf(EntityRenderer.mc.displayHeight), Integer.valueOf(scaledresolution.getScaleFactor())});
//                        }
//                    });
//                    throw new ReportedException(crashreport);
//                }
//            }
//        }
    }
    public void setAngles(float yaw, float pitch)
    {
        float f = this.rotationPitch;
        float f1 = this.rotationYaw;
        this.rotationYaw = (float)((double)this.rotationYaw + (double)yaw * 0.15D);
        this.rotationPitch = (float)((double)this.rotationPitch - (double)pitch * 0.15D);
        this.rotationPitch = MathHelper.clamp_float(this.rotationPitch, -90.0F, 90.0F);
        this.prevRotationPitch += this.rotationPitch - f;
        this.prevRotationYaw += this.rotationYaw - f1;
    }
    public void setPositionAndRotation(double x, double y, double z, float yaw, float pitch)
    {
        this.prevPosX = this.posX = x;
        this.prevPosY = this.posY = y;
        this.prevPosZ = this.posZ = z;
        this.prevRotationYaw = this.rotationYaw = yaw;
        this.prevRotationPitch = this.rotationPitch = pitch;
        double d0 = (double)(this.prevRotationYaw - yaw);

        if (d0 < -180.0D)
        {
            this.prevRotationYaw += 360.0F;
        }

        if (d0 >= 180.0D)
        {
            this.prevRotationYaw -= 360.0F;
        }

        this.setPosition(this.posX, this.posY, this.posZ);
        this.setRotation(yaw, pitch);
    }
    protected void setRotation(float yaw, float pitch)
    {
        this.rotationYaw = yaw % 360.0F;
        this.rotationPitch = pitch % 360.0F;
    }
    public void setPosition(double x, double y, double z)
    {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        float f = this.width / 2.0F;
        float f1 = this.height;
        this.boundingBox=new AxisAlignedBB(x - (double)f, y, z - (double)f, x + (double)f, y + (double)f1, z + (double)f);
    }
}
