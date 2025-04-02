package parkourterminal.data.EntitylandData;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import parkourterminal.data.globalData.GlobalData;
import parkourterminal.util.BlockUtils;
import parkourterminal.util.LandingBlockHelper;

import javax.vecmath.Vector3d;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LandingData {
    private Double hitX= Double.NaN;
    private Double hitY= Double.NaN;
    private Double hitZ= Double.NaN;
    private Double landingX= Double.NaN;
    private Double landingY= Double.NaN;
    private Double landingZ= Double.NaN;
    private Double blipY= Double.NaN;
    private int blipTimes = 0;
    private int tier=12;
    private boolean wasInAir = false;
    private boolean lastOnGround=false;
    public void Update(EntityPlayerSP player){
        if(GlobalData.getInputData().getOperation().isActualJump()){
            tier=12;
        }
        else if(lastOnGround&&!player.onGround){
            tier=1;
        }
        if (!player.onGround) {
            tier--;
            if(isCreativeFlying(player)){
                tier=12;
            }
            wasInAir = true; // 玩家在空中
        } else if (wasInAir) {
            tier--;
            wasInAir = false;
            hitX= player.posX;
            hitY= player.posY;
            hitZ=player.posZ;
            landingX=player.lastTickPosX;
            landingY=player.lastTickPosY;
            landingZ=player.lastTickPosZ;
        }
        lastOnGround= player.onGround;
        if(!player.onGround){//空中的每一tick
            Double groundY=fallingTouchGroundBlocks(player);
            if(groundY==null){
                return;
            }
            //接触地面的tick
            if(!touchBlock(player,new Vector3d(player.motionX,groundY-player.posY,player.motionZ))){
                //未接触墙;
                blipTimes=0;
                return;
            }
            //接触墙,触发步行辅助判断区
            double maxOffset=0.6;
            Double ceilingY=touchCeilingBlocks(player);
            if(ceilingY!=null){
                maxOffset=ceilingY;
            }
            if(touchBlock(player,new Vector3d(player.motionX,maxOffset,player.motionZ))){
                blipTimes=0;
                return;
            }
            if(touchBlock(player,new Vector3d(player.motionX, maxOffset-0.6, player.motionZ))){
                blipTimes=0;
                return;
            }
            //触发成功
            blipTimes++;
            blipY=maxOffset-0.6+player.posY;
        }
    }
    public int getTier(){
        return tier;
    }

    public Double getHitX() {
        return hitX;
    }

    public Double getHitY() {
        return hitY;
    }

    public Double getHitZ() {
        return hitZ;
    }

    public Double getLandingX() {
        return landingX;
    }

    public Double getLandingY() {
        return landingY;
    }

    public Double getLandingZ() {
        return landingZ;
    }

    public Double getBlipY() {
        return blipY;
    }
    public int getBlipTimes(){
        return blipTimes;
    }
    private boolean isCreativeFlying(EntityPlayerSP player) {
        // 判断是否创造模式
        boolean isCreativeMode = Minecraft.getMinecraft().playerController.getCurrentGameType() == WorldSettings.GameType.CREATIVE;
        // 判断是否飞行
        boolean isFlying = player.capabilities.isFlying;

        return isCreativeMode && isFlying;
    }
    private Double fallingTouchGroundBlocks(EntityPlayerSP player){
        if(player.onGround){
            return null;
        }
        World worldIn=player.worldObj;
        AxisAlignedBB bounding=player.getEntityBoundingBox().offset(0,player.motionY,0);
        List<AxisAlignedBB> boxes=worldIn.getCollidingBoundingBoxes(player, bounding);
        AxisAlignedBB wholeGround=LandingBlockHelper.UnionAll(LandingBlockHelper.WrappedAABBList(boxes));
        if(wholeGround==null){
            return null;
        }
        return wholeGround.maxY;
    }
    private Double touchCeilingBlocks(EntityPlayerSP player){
        if(player.onGround){
            return null;
        }
        World worldIn=player.worldObj;
        AxisAlignedBB bounding=player.getEntityBoundingBox().offset(0,0.6,0);
        List<AxisAlignedBB> boxes=worldIn.getCollidingBoundingBoxes(player, bounding);
        AxisAlignedBB wholeGround=LandingBlockHelper.UnionAll(LandingBlockHelper.WrappedAABBList(boxes));
        if(wholeGround==null){
            return null;
        }
        return wholeGround.minY;
    }
    private boolean touchBlock(EntityPlayerSP player, Vector3d offset){
        World worldIn=player.worldObj;
        AxisAlignedBB bounding=player.getEntityBoundingBox().offset(offset.x,offset.y,offset.z);
        List<AxisAlignedBB> boxes=worldIn.getCollidingBoundingBoxes(player, bounding);
        return !boxes.isEmpty();
    }
}
