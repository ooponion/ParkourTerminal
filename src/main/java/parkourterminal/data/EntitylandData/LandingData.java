package parkourterminal.data.EntitylandData;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.world.WorldSettings;
import parkourterminal.data.GlobalData;
import parkourterminal.util.BlockUtils;

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
    private boolean continuousBlip=true;
    public void Update(EntityPlayerSP player){
        if(GlobalData.getInputData().getOperation().isActualJump()){
            tier=12;
            if(!continuousBlip){
                blipTimes=0;
                blipY=Double.NaN;
                continuousBlip=true;
            }
        }
        else if(lastOnGround&&!player.onGround){
            tier=1;
        }
        if (!player.onGround) {
            tier--;
            if(player.capabilities.isCreativeMode && player.capabilities.isFlying){//flying
                tier=12;
            }
            wasInAir = true; // 玩家在空中
        } else if (wasInAir) {//着陆tick
            tier--;
            wasInAir = false;
            hitX= player.posX;
            hitY= player.posY;
            hitZ=player.posZ;
            landingX=player.lastTickPosX;
            landingY=player.lastTickPosY;
            landingZ=player.lastTickPosZ;
            //blip
            if(BlockUtils.getAABBsUnderPlayerFeet(player).isEmpty()&&player.lastTickPosY==player.posY){
                //触发成功
                blipTimes++;
                blipY=player.posY;
            }else{
                continuousBlip=false;
            }
        }
        lastOnGround= player.onGround;
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
}
