package parkourterminal.data.EntitylandData;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.world.WorldSettings;
import parkourterminal.data.globalData.GlobalData;

public class LandingData {
    private Double hitX= Double.NaN;
    private Double hitY= Double.NaN;
    private Double hitZ= Double.NaN;
    private Double landingX= Double.NaN;
    private Double landingY= Double.NaN;
    private Double landingZ= Double.NaN;
    private int tier=12;
    private boolean wasInAir = false;
    public void Update(EntityPlayerSP player){
        if(GlobalData.getInputData().getOperation().isActualJump()){
            tier=11;
        }
        if (!player.onGround) {
            tier--;
            if(isCreativeFlying(player)){
                tier=12;
            }
            wasInAir = true; // 玩家在空中
        } else if (wasInAir) {
            wasInAir = false;
            System.out.printf("player::%s\n",player);
            hitX= player.posX;
            hitY= player.posY;
            hitZ=player.posZ;
            landingX=player.lastTickPosX;
            landingY=player.lastTickPosY;
            landingZ=player.lastTickPosZ;
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
    private boolean isCreativeFlying(EntityPlayerSP player) {
        // 判断是否创造模式
        boolean isCreativeMode = Minecraft.getMinecraft().playerController.getCurrentGameType() == WorldSettings.GameType.CREATIVE;
        // 判断是否飞行
        boolean isFlying = player.capabilities.isFlying;

        return isCreativeMode && isFlying;
    }
}
