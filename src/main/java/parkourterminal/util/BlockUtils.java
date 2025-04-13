package parkourterminal.util;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import net.minecraft.world.World;
import parkourterminal.data.landingblock.intf.Segment;
import parkourterminal.data.landingblock.intf.Vertex;
import parkourterminal.util.renderhelper.HitPosition;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;
import java.util.*;

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
    public static HitPosition getLookingAtHitPosition(EntityPlayer player, float blockReachDistance, float partialTicks) {
        List<HitPosition> list= getLookingAtHitPositions(player, blockReachDistance, partialTicks);
        if(list.isEmpty()){
            return null;
        }
        return list.get(0);

    }
    public static AxisAlignedBB getLookingAtAABB(EntityPlayer player, float blockReachDistance, float partialTicks) {
        HitPosition hitPosition= getLookingAtHitPosition(player, blockReachDistance, partialTicks);
        if(hitPosition==null){
            return null;
        }
        return hitPosition.getHitBox();

    }
    public static List<HitPosition> getLookingAtHitPositions(EntityPlayer player, float blockReachDistance, float partialTicks) {

        Vec3 vec3 = player.getPositionEyes(partialTicks);
        Vec3 vec31 = player.getLook(partialTicks);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * blockReachDistance, vec31.yCoord * blockReachDistance, vec31.zCoord * blockReachDistance);
        return rayTraceBlocks(player.worldObj, vec3, vec32, false);

    }
    public static List<AxisAlignedBB> getLookingAtAABBs(EntityPlayer player, float blockReachDistance, float partialTicks){
        List<HitPosition> hitPositions=getLookingAtHitPositions(player, blockReachDistance, partialTicks);
        List<AxisAlignedBB> aabbs=new ArrayList<AxisAlignedBB>();
        for(HitPosition hitPosition:hitPositions){
            aabbs.add(hitPosition.getHitBox());
        }
        return aabbs;
    }
    public static List<HitPosition> rayTraceBlocks(World worldIn, Vec3 vec31, Vec3 vec32, boolean stopOnLiquid)
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
                    return new ArrayList<HitPosition>();
                }

                int k1 = 200;

                while (k1-- >= 0)
                {
                    if (Double.isNaN(vec31.xCoord) || Double.isNaN(vec31.yCoord) || Double.isNaN(vec31.zCoord))
                    {
                        return new ArrayList<HitPosition>();
                    }

                    if (l == i && i1 == j && j1 == k)
                    {
                        return new ArrayList<HitPosition>();
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

                            List<HitPosition> hitPositions=new ArrayList<HitPosition>();
                            HitPosition MinObject=null;
                            double minDistance=Double.MAX_VALUE;
                            for(AxisAlignedBB axisAlignedBB:boxes){
                                MovingObjectPosition movingobjectposition = collisionRayTrace(blockpos, vec31, vec32,axisAlignedBB);
                                if(movingobjectposition==null){continue;}
                                double distance=vec31.squareDistanceTo(movingobjectposition.hitVec);
                                HitPosition hitPosition=new HitPosition(movingobjectposition.hitVec, axisAlignedBB);
                                hitPositions.add(hitPosition);
                                if(distance<minDistance) {
                                    minDistance = distance;
                                    MinObject = hitPosition;
                                }
                            }
                            if (MinObject != null)
                            {
                                hitPositions.remove(MinObject);
                                hitPositions.add(0,MinObject);
                                return hitPositions;
                            }
                        }

                    }
                }

                return new ArrayList<HitPosition>();
            }
            else
            {
                return new ArrayList<HitPosition>();
            }
        }
        else
        {
            return new ArrayList<HitPosition>();
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
    public static AxisAlignedBB getIntersection(AxisAlignedBB a, AxisAlignedBB b) {
        if (!a.intersectsWith(b)) return null;

        double minX = Math.max(a.minX, b.minX);
        double minY = Math.max(a.minY, b.minY);
        double minZ = Math.max(a.minZ, b.minZ);
        double maxX = Math.min(a.maxX, b.maxX);
        double maxY = Math.min(a.maxY, b.maxY);
        double maxZ = Math.min(a.maxZ, b.maxZ);

        return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
    }
    public static AxisAlignedBB getExtendedBox(AxisAlignedBB box) {
        double minX = box.minX-0.3;
        double minZ = box.minZ-0.3;
        double maxX = box.maxX+0.3;
        double maxZ = box.maxZ+0.3;
        return new AxisAlignedBB(minX, box.minY, minZ, maxX, box.maxY, maxZ);
    }
    public static List<AxisAlignedBB> unionAABBs(List<AxisAlignedBB> aabbs) {
//        if(aabbs.size()==1){
//            return aabbs.get(0);
//        } else if (aabbs.isEmpty()) {
//            return null;
//        }
//        AxisAlignedBB alignedBB=aabbs.get(0);
//        for(int i=1;i<aabbs.size();i++){
//            alignedBB=getIntersection(aabbs.get(i),alignedBB);
//            if(alignedBB==null){return null;}
//        }
//        return alignedBB;
        List<AxisAlignedBB> result =new ArrayList<AxisAlignedBB>();
        for(AxisAlignedBB aabb:aabbs){
            result=subtractAABBs(result,aabb);
            result.add(aabb);
        }
        return result;
    }
    public static  List<AxisAlignedBB> subtractAABBsWithSubtractors(List<AxisAlignedBB> aabbs, List<AxisAlignedBB> subtractors) {
        List<AxisAlignedBB> result = new ArrayList<AxisAlignedBB>(aabbs);
        for(AxisAlignedBB subtract:subtractors){
            List<AxisAlignedBB> subtracted=subtractAABBs(result,subtract);
            result.clear();
            result.addAll(subtracted);
        }
        return result;
    }
    public static  List<AxisAlignedBB> subtractAABBs(List<AxisAlignedBB> aabbs, AxisAlignedBB subtractor) {
        List<AxisAlignedBB> result = new ArrayList<AxisAlignedBB>();
        for(AxisAlignedBB aabb:aabbs){
            result.addAll(subtractAABB(aabb,subtractor));
        }
        return result;
    }
    public static List<AxisAlignedBB> subtractAABBWithSubtractors(AxisAlignedBB box, List<AxisAlignedBB> subtractors) {
        List<AxisAlignedBB> result = new ArrayList<AxisAlignedBB>();
        result.add(box);
        for(AxisAlignedBB subtract:subtractors){
            result=subtractAABBs(result,subtract);
        }
        return result;
    }
//    public static List<AxisAlignedBB> subtractAABB(AxisAlignedBB box, AxisAlignedBB subtractor) {
//        List<AxisAlignedBB> result = new ArrayList<AxisAlignedBB>();
//
//        AxisAlignedBB intersection = getIntersection(box, subtractor);
//        if (intersection == null) {
//            result.add(box);
//            return result;
//        }
//
//        // 6 个aabbs
//        // left
//        if (box.minX < intersection.minX) {
//            result.add(new AxisAlignedBB(
//                    box.minX, box.minY, box.minZ,
//                    intersection.minX, box.maxY, box.maxZ));
//        }
//
//        // right
//        if (box.maxX > intersection.maxX) {
//            result.add(new AxisAlignedBB(
//                    intersection.maxX, box.minY, box.minZ,
//                    box.maxX, box.maxY, box.maxZ));
//        }
//
//        // bottom
//        if (box.minY < intersection.minY) {
//            result.add(new AxisAlignedBB(
//                    intersection.minX, box.minY, box.minZ,
//                    intersection.maxX, intersection.minY, box.maxZ));
//        }
//
//        // top
//        if (box.maxY > intersection.maxY) {
//            result.add(new AxisAlignedBB(
//                    intersection.minX, intersection.maxY, box.minZ,
//                    intersection.maxX, box.maxY, box.maxZ));
//        }
//
//        // behind
//        if (box.minZ < intersection.minZ) {
//            result.add(new AxisAlignedBB(
//                    intersection.minX, intersection.minY, box.minZ,
//                    intersection.maxX, intersection.maxY, intersection.minZ));
//        }
//
//        // front
//        if (box.maxZ > intersection.maxZ) {
//            result.add(new AxisAlignedBB(
//                    intersection.minX, intersection.minY, intersection.maxZ,
//                    intersection.maxX, intersection.maxY, box.maxZ));
//        }
//
//        return result;
//    }
public static List<AxisAlignedBB> subtractAABB(AxisAlignedBB box, AxisAlignedBB subtractor) {
    List<AxisAlignedBB> result = new ArrayList<AxisAlignedBB>();

    AxisAlignedBB intersection = getIntersection(box, subtractor);
    if (intersection == null) {
        result.add(box);
        return result;
    }

    // cut along X axis
    if (box.minX < intersection.minX) {
        result.add(new AxisAlignedBB(box.minX, box.minY, box.minZ, intersection.minX, box.maxY, box.maxZ));
    }
    if (box.maxX > intersection.maxX) {
        result.add(new AxisAlignedBB(intersection.maxX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ));
    }

    double minX = Math.max(box.minX, intersection.minX);
    double maxX = Math.min(box.maxX, intersection.maxX);

    // cut along Y axis (in X-overlapping region)
    if (box.minY < intersection.minY) {
        result.add(new AxisAlignedBB(minX, box.minY, box.minZ, maxX, intersection.minY, box.maxZ));
    }
    if (box.maxY > intersection.maxY) {
        result.add(new AxisAlignedBB(minX, intersection.maxY, box.minZ, maxX, box.maxY, box.maxZ));
    }

    double minY = Math.max(box.minY, intersection.minY);
    double maxY = Math.min(box.maxY, intersection.maxY);

    // cut along Z axis (in X-Y overlapping region)
    if (box.minZ < intersection.minZ) {
        result.add(new AxisAlignedBB(minX, minY, box.minZ, maxX, maxY, intersection.minZ));
    }
    if (box.maxZ > intersection.maxZ) {
        result.add(new AxisAlignedBB(minX, minY, intersection.maxZ, maxX, maxY, box.maxZ));
    }

    return result;
}
    public static List<AxisAlignedBB> getWallsOfAABB(AxisAlignedBB aabb,World world){
        AxisAlignedBB extendedBox=getExtendedBox(aabb);
        AxisAlignedBB standableBox=new AxisAlignedBB(extendedBox.minX,extendedBox.maxY, extendedBox.minZ, extendedBox.maxX,extendedBox.maxY+1.8, extendedBox.maxZ);//玩家可站立空间的整体Box
        List<AxisAlignedBB> list = new ArrayList<AxisAlignedBB>( );
        for(AxisAlignedBB box:getCollidingBlockBoundingBoxes(world, standableBox)){
            list.add(new AxisAlignedBB(box.minX,aabb.minY, box.minZ,box.maxX,aabb.maxY,box.maxZ));
        }
        return list;
    }
    public static List<AxisAlignedBB> getWallsOfAABBs(List<AxisAlignedBB> aabbs,World world){
        List<AxisAlignedBB> list = new ArrayList<AxisAlignedBB>( );
        for(AxisAlignedBB alignedBB:aabbs){
            list.addAll(getWallsOfAABB(alignedBB,world));
        }
        return list;
    }
    public static List<AxisAlignedBB> getCollidingBlockBoundingBoxes(World world, AxisAlignedBB aabb) {
        List<AxisAlignedBB> list = new ArrayList<AxisAlignedBB>();

        int minX = MathHelper.floor_double(aabb.minX);
        int maxX = MathHelper.floor_double(aabb.maxX + 1);
        int minY = MathHelper.floor_double(aabb.minY);
        int maxY = MathHelper.floor_double(aabb.maxY + 1);
        int minZ = MathHelper.floor_double(aabb.minZ);
        int maxZ = MathHelper.floor_double(aabb.maxZ + 1);

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int x = minX; x < maxX; x++) {
            for (int y = minY-1; y < maxY; y++) {
                for (int z = minZ; z < maxZ; z++) {
                    pos.set(x, y, z);
                    IBlockState blockState=world.getBlockState(pos);
                    Block block = blockState.getBlock();

                    if (!block.isAir(world, pos)) {
                        block.addCollisionBoxesToList(
                                world, pos,blockState, aabb, list, null
                        );
                    }
                }
            }
        }

        return list;
    }
    public static List<Vertex> QuadVertices(AxisAlignedBB box){
        List<Vertex> vertices = new ArrayList<Vertex>();

        Vertex vertex1=new Vertex(new Vector2d(box.minX,box.minZ),0,box.maxY,box.minY);
        Vertex vertex2=new Vertex(new Vector2d(box.minX,box.maxZ),1,box.maxY,box.minY);
        Vertex vertex3 =new Vertex(new Vector2d(box.maxX,box.maxZ),2,box.maxY,box.minY);
        Vertex vertex4 =new Vertex(new Vector2d(box.maxX,box.minZ),3,box.maxY,box.minY);

        Segment segment12 =new Segment(1);
        Segment segment23 =new Segment(0);
        Segment segment34 =new Segment(1);
        Segment segment41 =new Segment(0);

        segment12.setPos1(vertex1);
        segment12.setPos2(vertex2);
        segment23.setPos1(vertex2);
        segment23.setPos2(vertex3);
        segment34.setPos1(vertex3);
        segment34.setPos2(vertex4);
        segment41.setPos1(vertex4);
        segment41.setPos2(vertex1);

        vertex1.setzSegment(segment12);
        vertex1.setxSegment(segment41);
        vertex2.setzSegment(segment12);
        vertex2.setxSegment(segment23);
        vertex3.setzSegment(segment34);
        vertex3.setxSegment(segment23);
        vertex4.setzSegment(segment34);
        vertex4.setxSegment(segment41);

        vertices.add(vertex1);
        vertices.add(vertex2);
        vertices.add(vertex3);
        vertices.add(vertex4);

        return vertices;
    }
    public static AxisAlignedBB UnionAll(List<AxisAlignedBB> axisAlignedBBs){
        if(axisAlignedBBs.isEmpty()){
            return null;
        }
        if(axisAlignedBBs.size()==1){
            return axisAlignedBBs.get(0);
        }
        AxisAlignedBB lastUnion=axisAlignedBBs.get(0);
        for (int i=1;i<axisAlignedBBs.size();i++){
            lastUnion=axisAlignedBBs.get(i).union(lastUnion);
        }
        return lastUnion;
    }
    private static boolean isAxisTouching(double minA, double maxA, double minB, double maxB) {
        return maxA >= minB && maxB >= minA;
    }
    public static boolean isTouching(AxisAlignedBB a, AxisAlignedBB b) {
        return isAxisTouching(a.minX, a.maxX, b.minX, b.maxX) &&
                isAxisTouching(a.minY, a.maxY, b.minY, b.maxY) &&
                isAxisTouching(a.minZ, a.maxZ, b.minZ, b.maxZ);
    }
    public static double distanceToAABBGroup(Vector3d point, List<AxisAlignedBB> boxes) {
        double minDistance = Double.POSITIVE_INFINITY;

        for (AxisAlignedBB box : boxes) {
            if (isInside(box, point)) {
                return 0.0;
            } else {
                double dist = distanceToBox(point, box);
                minDistance = Math.min(minDistance, dist);
            }
        }

        return minDistance;
    }

    private static boolean isInside(AxisAlignedBB box, Vector3d point) {
        return point.x >= box.minX && point.x <= box.maxX &&
                point.y >= box.minY && point.y <= box.maxY &&
                point.z >= box.minZ && point.z <= box.maxZ;
    }

    private static double distanceToBox(Vector3d p, AxisAlignedBB box) {
        double dx = Math.max(Math.max(box.minX - p.x, 0), p.x - box.maxX);
        double dy = Math.max(Math.max(box.minY - p.y, 0), p.y - box.maxY);
        double dz = Math.max(Math.max(box.minZ - p.z, 0), p.z - box.maxZ);
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
    public static List<AxisAlignedBB> collectConnectedAABBs(
            AxisAlignedBB target,
            List<AxisAlignedBB> allBoxes
    ) {
        List<AxisAlignedBB> result = new ArrayList<AxisAlignedBB>();
        Queue<AxisAlignedBB> queue = new LinkedList<AxisAlignedBB>();
        Set<AxisAlignedBB> visited = new HashSet<AxisAlignedBB>();

        queue.add(target);
        visited.add(target);
        while (!queue.isEmpty()) {
            AxisAlignedBB current = queue.poll();
            result.add(current);

            for (AxisAlignedBB box : allBoxes) {
                if (!visited.contains(box) && BlockUtils.isTouching(current,box)) {
                    visited.add(box);
                    queue.add(box);
                }
            }
        }
        return result;
    }
    public static List<List<AxisAlignedBB>> splitIntoConnectedGroups(List<AxisAlignedBB> boxes) {
        List<List<AxisAlignedBB>> groups = new ArrayList<List<AxisAlignedBB>>();
        Set<AxisAlignedBB> visited = new HashSet<AxisAlignedBB>();

        for (AxisAlignedBB box : boxes) {
            if (!visited.contains(box)) {
                List<AxisAlignedBB> group = collectConnectedAABBs(box, boxes);
                groups.add(group);
                visited.addAll(group);
            }
        }

        return groups;
    }
    public static List<AxisAlignedBB> nearestGroupedAABBs(Vector3d pos, List<AxisAlignedBB> boxes){
        List<List<AxisAlignedBB>> groups= BlockUtils.splitIntoConnectedGroups(boxes);
        List<AxisAlignedBB> nearestGroup=new ArrayList<AxisAlignedBB>();
        double minDis = Double.POSITIVE_INFINITY;
        for( List<AxisAlignedBB> group:groups){
            double dis=BlockUtils.distanceToAABBGroup(pos,group);
            if(dis< minDis){
                nearestGroup=group;
                minDis =dis;
            }
        }
        return nearestGroup;
    }

}

