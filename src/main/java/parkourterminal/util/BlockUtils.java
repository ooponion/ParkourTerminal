package parkourterminal.util;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import net.minecraft.world.World;

import javax.vecmath.Vector3d;
import java.util.ArrayList;
import java.util.List;

public class BlockUtils {

    public static Vector3d getMinPos(AxisAlignedBB axisAlignedBB){
        return new Vector3d(axisAlignedBB.minX,axisAlignedBB.minY,axisAlignedBB.minZ);
    }
    public static Vector3d getMaxPos(AxisAlignedBB axisAlignedBB) {
        return new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
    }
    public static List<AxisAlignedBB> getAABBsUnderPlayerFeet( EntityPlayerSP player){
        BlockPos feetPos1=new BlockPos(player.posX, player.posY-0.00000000001f, player.posZ);
        BlockPos feetPos2=feetPos1.add(1,0,0);
        BlockPos feetPos3=feetPos1.add(0,0,1);
        BlockPos feetPos4=feetPos1.add(-1,0,0);
        BlockPos feetPos5=feetPos1.add(0,0,-1);
        BlockPos feetPos6=feetPos1.add(1,0,1);
        BlockPos feetPos7=feetPos1.add(1,0,-1);
        BlockPos feetPos8=feetPos1.add(-1,0,-1);
        BlockPos feetPos9=feetPos1.add(-1,0,1);
        BlockPos[] posList=new BlockPos[]{feetPos9,feetPos1,feetPos2,feetPos3,feetPos4,feetPos5,feetPos6,feetPos7,feetPos8};
        List<AxisAlignedBB> boxes = new ArrayList<AxisAlignedBB>();
        AxisAlignedBB bounding=player.getEntityBoundingBox().offset(0,-0.00000000001f,0);
        for(BlockPos feetPos:posList){
            IBlockState state=player.worldObj.getBlockState(feetPos);
            Block block=state.getBlock();
            BlockPos fencePos=feetPos.add(0,-0.5,0);
            IBlockState state2=player.worldObj.getBlockState(fencePos);
            Block block2=state2.getBlock();
            block2.addCollisionBoxesToList(player.worldObj, fencePos,state2,bounding,boxes,null);
            if(block instanceof BlockAnvil){
                //要不要得到铁帧正确碰撞箱?
            }
            block.addCollisionBoxesToList(player.worldObj, feetPos, state, bounding, boxes, null);
        }
        return boxes;
    }
    public static AxisAlignedBB getBiggestAABBUnderPlayerFeet( EntityPlayerSP player){
        List<AxisAlignedBB> boxes = getAABBsUnderPlayerFeet(player);
        AxisAlignedBB bounding=player.getEntityBoundingBox().offset(0,-0.00000000001f,0);
        AxisAlignedBB maxBox=null;
        double maxV=0;
        for(AxisAlignedBB axisAlignedBB:boxes){
            double volume=getIntersectionVolume(bounding,axisAlignedBB);
            if(volume>maxV){
                maxV=volume;
                maxBox=axisAlignedBB;
            }
        }
        return maxBox;
    }
    public static Block getBlockOnPlayerFeet(World worldIn,EntityPlayerSP player){
        BlockPos feetPos=new BlockPos(player.posX, player.posY-0.00000000001, player.posZ);
        IBlockState state=worldIn.getBlockState(feetPos);
        return state.getBlock();
    }
    public static double getIntersectionVolume(AxisAlignedBB aabb1, AxisAlignedBB aabb2) {
        if (!aabb1.intersectsWith(aabb2)) {
            return 0;
        }

        // 计算相交区域
        double minX = Math.max(aabb1.minX, aabb2.minX);
        double minY = Math.max(aabb1.minY, aabb2.minY);
        double minZ = Math.max(aabb1.minZ, aabb2.minZ);

        double maxX = Math.min(aabb1.maxX, aabb2.maxX);
        double maxY = Math.min(aabb1.maxY, aabb2.maxY);
        double maxZ = Math.min(aabb1.maxZ, aabb2.maxZ);

        // 计算相交体积
        double width = maxX - minX;
        double height = maxY - minY;
        double depth = maxZ - minZ;

        // 防止负值
        if (width <= 0 || height <= 0 || depth <= 0) {
            return 0;
        }

        return width * height * depth;
    }
    public static AxisAlignedBB getLookingAtAABB(EntityPlayer player, float blockReachDistance, float partialTicks) {
        List<AxisAlignedBB> list= getLookingAtAABBs(player, blockReachDistance, partialTicks);
        if(list.isEmpty()){
            return null;
        }
        return list.get(0);

    }
    public static List<AxisAlignedBB> getLookingAtAABBs(EntityPlayer player, float blockReachDistance, float partialTicks) {

        Vec3 vec3 = player.getPositionEyes(partialTicks);
        Vec3 vec31 = player.getLook(partialTicks);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * blockReachDistance, vec31.yCoord * blockReachDistance, vec31.zCoord * blockReachDistance);
        return rayTraceBlocks(player.worldObj, vec3, vec32, false);

    }
    public static List<AxisAlignedBB> rayTraceBlocks(World worldIn,Vec3 vec31, Vec3 vec32, boolean stopOnLiquid)
    {
        if (!Double.isNaN(vec31.xCoord) && !Double.isNaN(vec31.yCoord) && !Double.isNaN(vec31.zCoord))
        {
            if (!Double.isNaN(vec32.xCoord) && !Double.isNaN(vec32.yCoord) && !Double.isNaN(vec32.zCoord))
            {
                int i = MathHelper.floor_double(vec32.xCoord);
                int j = MathHelper.floor_double(vec32.yCoord);
                int k = MathHelper.floor_double(vec32.zCoord);
                int l = MathHelper.floor_double(vec31.xCoord);
                int i1 = MathHelper.floor_double(vec31.yCoord);
                int j1 = MathHelper.floor_double(vec31.zCoord);
                BlockPos blockpos = new BlockPos(l, i1, j1);
                IBlockState iblockstate = worldIn.getBlockState(blockpos);
                Block block = iblockstate.getBlock();

                if (( block.getCollisionBoundingBox(worldIn, blockpos, iblockstate) != null) && block.canCollideCheck(iblockstate, stopOnLiquid))
                {
                    return null;
                }

                int k1 = 200;

                while (k1-- >= 0)
                {
                    if (Double.isNaN(vec31.xCoord) || Double.isNaN(vec31.yCoord) || Double.isNaN(vec31.zCoord))
                    {
                        return null;
                    }

                    if (l == i && i1 == j && j1 == k)
                    {
                        return null;
                    }

                    boolean flag2 = true;
                    boolean flag = true;
                    boolean flag1 = true;
                    double d0 = 999.0D;
                    double d1 = 999.0D;
                    double d2 = 999.0D;

                    if (i > l)
                    {
                        d0 = (double)l + 1.0D;
                    }
                    else if (i < l)
                    {
                        d0 = (double)l + 0.0D;
                    }
                    else
                    {
                        flag2 = false;
                    }

                    if (j > i1)
                    {
                        d1 = (double)i1 + 1.0D;
                    }
                    else if (j < i1)
                    {
                        d1 = (double)i1 + 0.0D;
                    }
                    else
                    {
                        flag = false;
                    }

                    if (k > j1)
                    {
                        d2 = (double)j1 + 1.0D;
                    }
                    else if (k < j1)
                    {
                        d2 = (double)j1 + 0.0D;
                    }
                    else
                    {
                        flag1 = false;
                    }

                    double d3 = 999.0D;
                    double d4 = 999.0D;
                    double d5 = 999.0D;
                    double d6 = vec32.xCoord - vec31.xCoord;
                    double d7 = vec32.yCoord - vec31.yCoord;
                    double d8 = vec32.zCoord - vec31.zCoord;

                    if (flag2)
                    {
                        d3 = (d0 - vec31.xCoord) / d6;
                    }

                    if (flag)
                    {
                        d4 = (d1 - vec31.yCoord) / d7;
                    }

                    if (flag1)
                    {
                        d5 = (d2 - vec31.zCoord) / d8;
                    }

                    if (d3 == -0.0D)
                    {
                        d3 = -1.0E-4D;
                    }

                    if (d4 == -0.0D)
                    {
                        d4 = -1.0E-4D;
                    }

                    if (d5 == -0.0D)
                    {
                        d5 = -1.0E-4D;
                    }

                    EnumFacing enumfacing;

                    if (d3 < d4 && d3 < d5)
                    {
                        enumfacing = i > l ? EnumFacing.WEST : EnumFacing.EAST;
                        vec31 = new Vec3(d0, vec31.yCoord + d7 * d3, vec31.zCoord + d8 * d3);
                    }
                    else if (d4 < d5)
                    {
                        enumfacing = j > i1 ? EnumFacing.DOWN : EnumFacing.UP;
                        vec31 = new Vec3(vec31.xCoord + d6 * d4, d1, vec31.zCoord + d8 * d4);
                    }
                    else
                    {
                        enumfacing = k > j1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
                        vec31 = new Vec3(vec31.xCoord + d6 * d5, vec31.yCoord + d7 * d5, d2);
                    }

                    l = MathHelper.floor_double(vec31.xCoord) - (enumfacing == EnumFacing.EAST ? 1 : 0);
                    i1 = MathHelper.floor_double(vec31.yCoord) - (enumfacing == EnumFacing.UP ? 1 : 0);
                    j1 = MathHelper.floor_double(vec31.zCoord) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
                    blockpos = new BlockPos(l, i1, j1);
                    IBlockState iblockstate1 = worldIn.getBlockState(blockpos);
                    Block block1 = iblockstate1.getBlock();

                    if ( block1.getCollisionBoundingBox(worldIn, blockpos, iblockstate1) != null)
                    {
                        if (block1.canCollideCheck(iblockstate1, stopOnLiquid))
                        {
                            List<AxisAlignedBB> boxes = new ArrayList<AxisAlignedBB>();
                            AxisAlignedBB bounding=new AxisAlignedBB(blockpos,blockpos.add(1,1,1));
                            block1.addCollisionBoxesToList(Minecraft.getMinecraft().theWorld, blockpos, iblockstate1, bounding, boxes, null);

                            AxisAlignedBB MinObject=null;
                            double minDistance=Double.MAX_VALUE;
                            for(AxisAlignedBB axisAlignedBB:boxes){
                                MovingObjectPosition movingobjectposition = collisionRayTrace(blockpos, vec31, vec32,axisAlignedBB);
                                if(movingobjectposition==null){continue;}
                                double distance=vec31.squareDistanceTo(movingobjectposition.hitVec);
                                if(distance<minDistance){
                                    minDistance=distance;
                                    MinObject=axisAlignedBB;
                                }
                            }
                            if (MinObject != null)
                            {
                                boxes.remove(MinObject);
                                boxes.add(0,MinObject);
                            }
                            return boxes;
                        }

                    }
                }

                return new ArrayList<AxisAlignedBB>();
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }
    public static boolean isVecInsideBB(AxisAlignedBB axisAlignedBB,Vec3 vec)
    {
        return vec.xCoord >= axisAlignedBB.minX && vec.xCoord <= axisAlignedBB.maxX && (vec.yCoord >= axisAlignedBB.minY && vec.yCoord <= axisAlignedBB.maxY && vec.zCoord >= axisAlignedBB.minZ && vec.zCoord <= axisAlignedBB.maxZ);
    }
    public static boolean isFenceLike(Block block) {
        return block instanceof BlockFence || block instanceof BlockWall || block instanceof BlockFenceGate;
    }
    private static MovingObjectPosition collisionRayTrace(BlockPos pos, Vec3 start, Vec3 end, AxisAlignedBB axisAlignedBB)
    {
        if(axisAlignedBB==null){
            return null;
        }
        start = start.addVector((double)(-pos.getX()), (double)(-pos.getY()), (double)(-pos.getZ()));
        end = end.addVector((double)(-pos.getX()), (double)(-pos.getY()), (double)(-pos.getZ()));
        axisAlignedBB=axisAlignedBB.offset((double)(-pos.getX()), (double)(-pos.getY()), (double)(-pos.getZ()));
        Vec3 vec3 = start.getIntermediateWithXValue(end, axisAlignedBB.minX);
        Vec3 vec31 = start.getIntermediateWithXValue(end, axisAlignedBB.maxX);
        Vec3 vec32 = start.getIntermediateWithYValue(end, axisAlignedBB.minY);
        Vec3 vec33 = start.getIntermediateWithYValue(end, axisAlignedBB.maxY);
        Vec3 vec34 = start.getIntermediateWithZValue(end, axisAlignedBB.minZ);
        Vec3 vec35 = start.getIntermediateWithZValue(end, axisAlignedBB.maxZ);

        if (!isVecInsideYZBounds(vec3,axisAlignedBB))
        {
            vec3 = null;
        }

        if (!isVecInsideYZBounds(vec31,axisAlignedBB))
        {
            vec31 = null;
        }

        if (!isVecInsideXZBounds(vec32,axisAlignedBB))
        {
            vec32 = null;
        }

        if (!isVecInsideXZBounds(vec33,axisAlignedBB))
        {
            vec33 = null;
        }

        if (!isVecInsideXYBounds(vec34,axisAlignedBB))
        {
            vec34 = null;
        }

        if (!isVecInsideXYBounds(vec35,axisAlignedBB))
        {
            vec35 = null;
        }

        Vec3 vec36 = null;

        if (vec3 != null && (vec36 == null || start.squareDistanceTo(vec3) < start.squareDistanceTo(vec36)))
        {
            vec36 = vec3;
        }

        if (vec31 != null && (vec36 == null || start.squareDistanceTo(vec31) < start.squareDistanceTo(vec36)))
        {
            vec36 = vec31;
        }

        if (vec32 != null && (vec36 == null || start.squareDistanceTo(vec32) < start.squareDistanceTo(vec36)))
        {
            vec36 = vec32;
        }

        if (vec33 != null && (vec36 == null || start.squareDistanceTo(vec33) < start.squareDistanceTo(vec36)))
        {
            vec36 = vec33;
        }

        if (vec34 != null && (vec36 == null || start.squareDistanceTo(vec34) < start.squareDistanceTo(vec36)))
        {
            vec36 = vec34;
        }

        if (vec35 != null && (vec36 == null || start.squareDistanceTo(vec35) < start.squareDistanceTo(vec36)))
        {
            vec36 = vec35;
        }
        if (vec36 == null)
        {
            return null;
        }
        else
        {
            EnumFacing enumfacing = null;

            if (vec36 == vec3)
            {
                enumfacing = EnumFacing.WEST;
            }

            if (vec36 == vec31)
            {
                enumfacing = EnumFacing.EAST;
            }

            if (vec36 == vec32)
            {
                enumfacing = EnumFacing.DOWN;
            }

            if (vec36 == vec33)
            {
                enumfacing = EnumFacing.UP;
            }

            if (vec36 == vec34)
            {
                enumfacing = EnumFacing.NORTH;
            }

            if (vec36 == vec35)
            {
                enumfacing = EnumFacing.SOUTH;
            }
            return new MovingObjectPosition(vec36.addVector((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()), enumfacing, pos);
        }
    }

    /**
     * Checks if a vector is within the Y and Z bounds of the block.
     */
    private static boolean isVecInsideYZBounds(Vec3 point, AxisAlignedBB axisAlignedBB)
    {
        return point != null && point.yCoord >= axisAlignedBB.minY && point.yCoord <= axisAlignedBB.maxY && point.zCoord >= axisAlignedBB.minZ && point.zCoord <= axisAlignedBB.maxZ;
    }

    /**
     * Checks if a vector is within the X and Z bounds of the block.
     */
    private static boolean isVecInsideXZBounds(Vec3 point, AxisAlignedBB axisAlignedBB)
    {
        return point != null && point.xCoord >= axisAlignedBB.minX && point.xCoord <= axisAlignedBB.maxX && point.zCoord >= axisAlignedBB.minZ && point.zCoord <= axisAlignedBB.maxZ;
    }

    /**
     * Checks if a vector is within the X and Y bounds of the block.
     */
    private static boolean isVecInsideXYBounds(Vec3 point, AxisAlignedBB axisAlignedBB)
    {
        return point != null && point.xCoord >= axisAlignedBB.minX && point.xCoord <= axisAlignedBB.maxX && point.yCoord >= axisAlignedBB.minY && point.yCoord <= axisAlignedBB.maxY;
    }

}
