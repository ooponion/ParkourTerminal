package parkourterminal.data.EntitySpeedData;

import net.minecraft.client.entity.EntityPlayerSP;
import parkourterminal.data.GlobalData;
import parkourterminal.data.inputdata.InputData;
import parkourterminal.global.json.TerminalJsonConfig;
import parkourterminal.util.NumberWrapper;
import parkourterminal.util.ParseHelper;
import parkourterminal.util.SendMessageHelper;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3d;

public class SpeedData {
    private Double deltaX = 0D;
    private Double deltaY = 0D;
    private Double deltaZ = 0D;
    private Vector2d speedVector =new Vector2d(0,0);
    private Vector3d speed1 =new Vector3d(0,0,0);
    private Vector3d speed2 =new Vector3d(0,0,0);
    private Vector3d speed=new Vector3d(0,0,0);
    private Vector2d maxSpeedVector=new Vector2d(0,0);
    private Vector3d maxSpeed=new Vector3d(0,0,0);

    private boolean firstTickStart=false;
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
        if(TerminalJsonConfig.getProperties().isSendHitInertia() &&!player.onGround){
            if(speed1.x==0&&speed2.x!=0&&speed.x!=0){
                SendMessageHelper.addChatMessage(player,"Hit Inertia x: v = "+NumberWrapper.toDecimalString(speed2.x));
            }
            if(speed1.z==0&&speed2.z!=0&&speed.z!=0){
                SendMessageHelper.addChatMessage(player,"Hit Inertia z v = "+ NumberWrapper.toDecimalString(speed2.z));
            }
        }
        speed2=speed1;
        speed1=speed;
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
