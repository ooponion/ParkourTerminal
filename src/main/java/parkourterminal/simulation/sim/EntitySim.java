package parkourterminal.simulation.sim;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import parkourterminal.simulation.manager.BlockInteractionManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EntitySim {//testPerfect
    public static void onUpdate(PlayerState state)
    {
        onEntityUpdate(state);
    }
    public static void onEntityUpdate(PlayerState state) {
        state.prevPosX = state.posX;
        state.prevPosY = state.posY;
        state.prevPosZ = state.posZ;
        state.prevRotationPitch = state.rotationPitch;
        state.prevRotationYaw = state.rotationYaw;
        handleWaterMovement(state);
    }
    public static boolean handleMaterialAcceleration(AxisAlignedBB bb, Material materialIn, PlayerState state)
    {
        //SystemOutHelper.printf("beforeWaterMovement1:%s:%s",state.motionX,state.motionZ);
        if(materialIn != Material.water){
            return false;
        }
        int i = MathHelper.floor_double(bb.minX);
        int j = MathHelper.floor_double(bb.maxX + 1.0D);
        int k = MathHelper.floor_double(bb.minY);
        int l = MathHelper.floor_double(bb.maxY + 1.0D);
        int i1 = MathHelper.floor_double(bb.minZ);
        int j1 = MathHelper.floor_double(bb.maxZ + 1.0D);

        boolean flag = false;
        Vec3 vec3 = new Vec3(0.0D, 0.0D, 0.0D);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (int k1 = i; k1 < j; ++k1)
        {
            for (int l1 = k; l1 < l; ++l1)
            {
                for (int i2 = i1; i2 < j1; ++i2)
                {
                    blockpos$mutableblockpos.set(k1, l1, i2);
                    IBlockState iblockstate = state.worldIn.getBlockState(blockpos$mutableblockpos);
                    Block block = iblockstate.getBlock();
                    //Boolean result = block.isEntityInsideMaterial(state.worldIn, blockpos$mutableblockpos, iblockstate, entityIn, (double)l, materialIn, false);
                    Boolean result=null;
                    if (result != null && result&&block instanceof BlockLiquid)
                    {
                        // Forge: When requested call blocks modifyAcceleration method, and more importantly cause this method to return true, which results in an entity being "inWater"
                        flag = true;
                        vec3 = modifyAcceleration(state.worldIn, (BlockLiquid) block,blockpos$mutableblockpos, vec3);
                        continue;
                    }
                    else if (result != null) continue;

                    if (block.getMaterial() == materialIn)
                    {
                        double d0 = (double)((float)(l1 + 1) - BlockLiquid.getLiquidHeightPercent(((Integer)iblockstate.getValue(BlockLiquid.LEVEL)).intValue()));

                        if ((double)l >= d0)
                        {
                            flag = true;
                            vec3 = modifyAcceleration(state.worldIn,(BlockLiquid)block, blockpos$mutableblockpos, vec3);
                        }
                    }
                }
            }
        }

        if (vec3.lengthVector() > 0.0D && true)
        {
            vec3 = vec3.normalize();
            double d1 = 0.014D;
            state.motionX += vec3.xCoord * d1;
            state.motionY += vec3.yCoord * d1;
            state.motionZ += vec3.zCoord * d1;
        }
        //SystemOutHelper.printf("afterWaterMovement1:%s:%s",state.motionX,state.motionZ);
        return flag;

    }
    public static Vec3 modifyAcceleration(World worldIn,BlockLiquid block, BlockPos pos, Vec3 motion)
    {
        try {
            Method method=BlockLiquid.class.getDeclaredMethod("getFlowVector",IBlockAccess.class,BlockPos.class);
            method.setAccessible(true);
            return motion.add((Vec3) method.invoke(block,worldIn,pos));
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean handleWaterMovement(PlayerState state)
    {

        if (handleMaterialAcceleration(state.boundingBox.expand(0.0D, -0.4000000059604645D, 0.0D).contract(0.001D, 0.001D, 0.001D), Material.water, state))
        {
            state.isInWater = true;
        }
        else
        {
            state.isInWater = false;
        }
        return state.isInWater;
    }

    public static void moveEntity(PlayerState state, double x, double y, double z) {//testPerfect
        if (state.noClip) {
            state.boundingBox = state.boundingBox.offset(x, y, z);
            updatePositionFromBB(state);
            return;
        }
        if (state.isInWeb()) {
            x *= 0.25D;
            y *= 0.05000000074505806D;
            z *= 0.25D;
            state.motionX = 0;
            state.motionY = 0;
            state.motionZ = 0;
            state.setInWeb(false);
        }
        double originalX = x;
        double originalY = y;
        double originalZ = z;

        if (state.onGround && state.isSneaking()) {//handleSneakMovement //testPerfect
            double d6 = 0.05D;
            while (x != 0.0D && getCollidingBoundingBoxes(state, state.boundingBox.offset(x, -1.0D, 0.0D)).isEmpty()) {
                if (x < d6 && x >= -d6)
                {
                    x = 0.0D;
                }
                else if (x > 0.0D)
                {
                    x -= d6;
                }
                else
                {
                    x += d6;
                }
                originalX = x;
            }

            while (z != 0.0D && getCollidingBoundingBoxes(state, state.boundingBox.offset(0.0D, -1.0D, z)).isEmpty()) {
                if (z < d6 && z >= -d6)
                {
                    z = 0.0D;
                }
                else if (z > 0.0D)
                {
                    z -= d6;
                }
                else
                {
                    z += d6;
                }
                originalZ = z;
            }

            while (x != 0.0D && z != 0.0D && getCollidingBoundingBoxes(state, state.boundingBox.offset(x, -1.0D, z)).isEmpty()) {
                if (x < d6 && x >= -d6)
                {
                    x = 0.0D;
                }
                else if (x > 0.0D)
                {
                    x -= d6;
                }
                else
                {
                    x += d6;
                }
                originalX = x;
                if (z < d6 && z >= -d6)
                {
                    z = 0.0D;
                }
                else if (z > 0.0D)
                {
                    z -= d6;
                }
                else
                {
                    z += d6;
                }
                originalZ = z;
            }
        }
        AxisAlignedBB originalAxisalignedbb = state.boundingBox;
        List<AxisAlignedBB> boundingBoxes=getCollidingBoundingBoxes(state, state.boundingBox.addCoord(x,y,z));
        // Step 1: Y-axis collision
        y = calculateAxisOffsetY(state,y,boundingBoxes);
        state.boundingBox = state.boundingBox.offset(0, y, 0);

        // Step 2: X-axis collision
        x = calculateAxisOffsetX(state,x,boundingBoxes);
        state.boundingBox = state.boundingBox.offset(x, 0, 0);

        // Step 3: Z-axis collision
        z = calculateAxisOffsetZ(state,z,boundingBoxes);
        state.boundingBox = state.boundingBox.offset(0, 0, z);

        // Step handling
        boolean flag = state.onGround || originalY != y && originalY < 0.0D;
        if (state.stepHeight > 0.0F &&flag&& (originalX != x || originalZ != z)) {//handleStepClimbing
            Vector3d vector3d=handleStepClimbing(state,originalAxisalignedbb,originalX,originalY,originalZ,x,y,z);
            x=vector3d.x;
            y=vector3d.y;
            z=vector3d.z;
        }

        updatePositionFromBB(state);

        state.isCollidedHorizontally = originalX != x || originalZ != z;
        state.isCollidedVertically = originalY != y;
        state.onGround = originalY != y && originalY < 0.0D;
        state.isCollided = state.isCollidedHorizontally || state.isCollidedVertically;

        int i = MathHelper.floor_double(state.posX);
        int j = MathHelper.floor_double(state.posY - 0.20000000298023224D);
        int k = MathHelper.floor_double(state.posZ);
        BlockPos blockpos = new BlockPos(i, j, k);
        Block block1 = state.worldIn.getBlockState(blockpos).getBlock();

        if (block1.getMaterial() == Material.air)
        {
            Block block = state.worldIn.getBlockState(blockpos.down()).getBlock();

            if (block instanceof BlockFence || block instanceof BlockWall || block instanceof BlockFenceGate)
            {
                block1 = block;
                blockpos = blockpos.down();
            }
        }

        EntityLivingBaseSim.updateFallState(state,y, state.onGround, block1, blockpos);
        if (originalX != x) state.motionX = 0.0D;
        if (originalZ != z) state.motionZ = 0.0D;
        if (originalY != y) {
            onLanded(block1,state);
        }
        if (!state.isFlying&&!(state.onGround && state.isSneaking())&&state.onGround)
        {
            onCollision2(blockpos,state.worldIn.getBlockState(blockpos),state);
        }
        doBlockCollisions(state);
    }
    protected static void doBlockCollisions(PlayerState state)
    {
        BlockPos blockpos = new BlockPos(state.boundingBox.minX + 0.001D, state.boundingBox.minY + 0.001D, state.boundingBox.minZ + 0.001D);
        BlockPos blockpos1 = new BlockPos(state.boundingBox.maxX - 0.001D, state.boundingBox.maxY - 0.001D, state.boundingBox.maxZ - 0.001D);


        for (int i = blockpos.getX(); i <= blockpos1.getX(); ++i)
        {
            for (int j = blockpos.getY(); j <= blockpos1.getY(); ++j)
            {
                for (int k = blockpos.getZ(); k <= blockpos1.getZ(); ++k)
                {
                    BlockPos blockpos2 = new BlockPos(i, j, k);
                    IBlockState iblockstate = state.worldIn.getBlockState(blockpos2);

                    try
                    {
                        onCollision(blockpos2,iblockstate,state);
                    }
                    catch (Throwable throwable)
                    {
                        CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Colliding entity with block");
                        CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being collided with");
                        CrashReportCategory.addBlockInfo(crashreportcategory, blockpos2, iblockstate);
                        throw new ReportedException(crashreport);
                    }
                }
            }
        }

    }
    public static void updateFallState(PlayerState state, double y, boolean onGroundIn, Block blockIn, BlockPos pos)
    {
        if (onGroundIn)
        {
            if (state.fallDistance > 0.0F)
            {
                state.fallDistance = 0.0F;
            }
        }
        else if (y < 0.0D)
        {
            state.fallDistance = (float)((double)state.fallDistance - y);
        }
    }
    private static void onLanded(Block blockIn, PlayerState state){
        BlockInteractionManager.getInstance().OnLanded(blockIn.getClass(),state);
    }
    private static void onCollision( BlockPos pos, IBlockState blockState, PlayerState state){
        BlockInteractionManager.getInstance().OnCollision(blockState.getBlock().getClass(),state, pos,blockState);
    }
    private static void onCollision2(BlockPos pos, IBlockState blockState, PlayerState state){
        BlockInteractionManager.getInstance().OnCollision(blockState.getBlock().getClass(),state, pos);
    }
    private static double calculateAxisOffsetY(PlayerState state, double y, List<AxisAlignedBB> boundingBoxes) {//testPerfect
        for (AxisAlignedBB box : boundingBoxes) {
            y = box.calculateYOffset(state.boundingBox, y);
        }
        return y;
    }

    private static double calculateAxisOffsetX(PlayerState state, double x, List<AxisAlignedBB> boundingBoxes) {//testPerfect
        for (AxisAlignedBB box : boundingBoxes) {
            x = box.calculateXOffset(state.boundingBox, x);
        }
        return x;
    }

    private static double calculateAxisOffsetZ(PlayerState state, double z, List<AxisAlignedBB> boundingBoxes) {//testPerfect
        for (AxisAlignedBB box : boundingBoxes) {
            z = box.calculateZOffset(state.boundingBox, z);
        }
        return z;
    }

    private static void updatePositionFromBB(PlayerState state) {//testPerfect
        state.posX = (state.boundingBox.minX + state.boundingBox.maxX) / 2.0D;
        state.posY = state.boundingBox.minY;
        state.posZ = (state.boundingBox.minZ + state.boundingBox.maxZ) / 2.0D;
    }


    private static Vector3d handleStepClimbing(PlayerState state, AxisAlignedBB originalAxisalignedbb, double originalX, double d4, double originalZ, double x, double y, double z) {//testPerfect
        double startX = x;
        double startY = y;
        double startZ = z;
        AxisAlignedBB axisalignedbb3 = state.boundingBox;
        state.boundingBox= originalAxisalignedbb;
        y = (double)state.stepHeight;
        List<AxisAlignedBB> list = getCollidingBoundingBoxes(state,state.boundingBox.addCoord(originalX, y, originalZ));//原移动方式,不过y+0.6
        AxisAlignedBB axisalignedbb4 = state.boundingBox;
        AxisAlignedBB axisalignedbb5 = axisalignedbb4.addCoord(originalX, 0.0D, originalZ);//扩展originalX, originalZ
        double d9 = y;

        for (AxisAlignedBB axisalignedbb6 : list)
        {
            d9 = axisalignedbb6.calculateYOffset(axisalignedbb5, d9);
        }

        axisalignedbb4 = axisalignedbb4.offset(0.0D, d9, 0.0D);//往上移动0.6或者顶头就降低
        double d15 = originalX;

        for (AxisAlignedBB axisalignedbb7 : list)
        {
            d15 = axisalignedbb7.calculateXOffset(axisalignedbb4, d15);
        }

        axisalignedbb4 = axisalignedbb4.offset(d15, 0.0D, 0.0D);//往x移动originalX或者撞墙
        double d16 = originalZ;

        for (AxisAlignedBB axisalignedbb8 : list)
        {
            d16 = axisalignedbb8.calculateZOffset(axisalignedbb4, d16);
        }

        axisalignedbb4 = axisalignedbb4.offset(0.0D, 0.0D, d16);//往z移动originalZ或者撞墙
        AxisAlignedBB axisalignedbb14 = state.boundingBox;
        double d17 = y;

        for (AxisAlignedBB axisalignedbb9 : list)
        {
            d17 = axisalignedbb9.calculateYOffset(axisalignedbb14, d17);
        }

        axisalignedbb14 = axisalignedbb14.offset(0.0D, d17, 0.0D);
        double d18 = originalX;

        for (AxisAlignedBB axisalignedbb10 : list)
        {
            d18 = axisalignedbb10.calculateXOffset(axisalignedbb14, d18);
        }

        axisalignedbb14 = axisalignedbb14.offset(d18, 0.0D, 0.0D);
        double d19 = originalZ;

        for (AxisAlignedBB axisalignedbb11 : list)
        {
            d19 = axisalignedbb11.calculateZOffset(axisalignedbb14, d19);
        }

        axisalignedbb14 = axisalignedbb14.offset(0.0D, 0.0D, d19);
        double d20 = d15 * d15 + d16 * d16;
        double d10 = d18 * d18 + d19 * d19;

        if (d20 > d10)
        {
            x = d15;
            z = d16;
            y = -d9;
            state.boundingBox=axisalignedbb4;
        }
        else
        {
            x = d18;
            z = d19;
            y = -d17;
            state.boundingBox=axisalignedbb14;
        }

        for (AxisAlignedBB axisalignedbb12 : list)
        {
            y = axisalignedbb12.calculateYOffset(state.boundingBox, y);
        }

        state.boundingBox=(state.boundingBox.offset(0.0D, y, 0.0D));

        if (startX * startX + startZ * startZ >= x * x + z * z)
        {
            x = startX;
            y = startY;
            z = startZ;
            state.boundingBox=axisalignedbb3;
        }
        Vector3d vector3d= new Vector3d();
        vector3d.x = x;
        vector3d.y = y;
        vector3d.z = z;
        return vector3d;
    }

    public static List<AxisAlignedBB> getCollidingBoundingBoxes(PlayerState state, AxisAlignedBB bb) {//testPerfect
        List<AxisAlignedBB> list = new ArrayList<>();
        int i = MathHelper.floor_double(bb.minX);
        int j = MathHelper.floor_double(bb.maxX + 1.0D);
        int k = MathHelper.floor_double(bb.minY);
        int l = MathHelper.floor_double(bb.maxY + 1.0D);
        int m = MathHelper.floor_double(bb.minZ);
        int n = MathHelper.floor_double(bb.maxZ + 1.0D);

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int x = i; x < j; ++x) {
            for (int z = m; z < n; ++z) {
                for (int y = k - 1; y < l; ++y) {
                    pos.set(x, y, z);
                    IBlockState stateHere = state.worldIn.getBlockState(pos);
                    Block block = stateHere.getBlock();
                    if (block != null) {
                        block.addCollisionBoxesToList(state.worldIn, pos, stateHere, bb, list, null);
                    }
                }
            }
        }

        return list;
    }
}