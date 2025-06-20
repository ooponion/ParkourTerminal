package parkourterminal.data.EntitySpeedData;

import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import parkourterminal.data.GlobalData;
import parkourterminal.global.json.TerminalJsonConfig;
import parkourterminal.util.NumberWrapper;
import parkourterminal.util.SendMessageHelper;
import parkourterminal.util.SystemOutHelper;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3d;

public class SpeedData {
    private Double deltaX = 0D;
    private Double deltaY = 0D;
    private Double deltaZ = 0D;
    private Vector2d speedVector =new Vector2d(0,0);
    private Vector3d lastSpeed =new Vector3d(0,0,0);
    private Vector3d speed=new Vector3d(0,0,0);
    private Vector2d maxSpeedVector=new Vector2d(0,0);
    private Vector3d maxSpeed=new Vector3d(0,0,0);

    private boolean firstTickStart=false;
    private Vector2d lastMotion=new Vector2d(0,0);

    public SpeedData(){

    }
    public void Update(EntityPlayerSP player){
        deltaX= player.posX-player.lastTickPosX;
        deltaY= player.posY-player.lastTickPosY;
        deltaZ= player.posZ-player.lastTickPosZ;
        speedVector =new Vector2d(Math.hypot(deltaX,deltaZ),-Math.toDegrees(Math.atan2(deltaX,deltaZ))+0.0000000000000000001D);
        speed=new Vector3d(deltaX,deltaY,deltaZ);
        if(!GlobalData.getInputData().getOperation().equals("none")){//在有操作的时候计算max
            if(!firstTickStart){
                firstTickStart=true;
                maxSpeed=new Vector3d(0,0,0);
                maxSpeedVector=new Vector2d(0,0);
            }
            if(maxSpeedVector.x<speedVector.x){//更新maxSpeed
                maxSpeed=new Vector3d(speed);
                maxSpeedVector=new Vector2d(speedVector);
            }
        }else{//无操作 none
            firstTickStart=false;
        }
        if(TerminalJsonConfig.getProperties().isSendHitInertia()){
            if(lastMotion.x!=0&&Math.abs(lastMotion.x) < 0.005D&&Math.abs(player.motionX)>=0.005D){
                SendMessageHelper.addChatMessage(player,"Hit Inertia x: v = "+NumberWrapper.toDecimalString(lastSpeed.x));
            }
            if(lastMotion.y!=0&&Math.abs(lastMotion.y) < 0.005D&&Math.abs(player.motionZ)>=0.005D){
                SendMessageHelper.addChatMessage(player,"Hit Inertia z v = "+ NumberWrapper.toDecimalString(lastSpeed.z));
            }
        }
        lastSpeed =speed;
        lastMotion=new Vector2d(player.motionX,player.motionZ);

    }
    public Vector3d getSpeed(){
        return speed;
    }
    public Vector2f getSpeedVector(){

        return new Vector2f(speedVector);
    }

    public Vector2f getMaxSpeedVector() {
        return new Vector2f(maxSpeedVector);
    }

    public Vector3d getMaxSpeed() {
        return maxSpeed;
    }
    public void resetMaxSpeed(){
        maxSpeed=new Vector3d(0,0,0);
        maxSpeedVector=new Vector2d(0,0);
    }

}
