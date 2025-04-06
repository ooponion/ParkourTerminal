package parkourterminal.data.EntitySpeedData;

import net.minecraft.client.entity.EntityPlayerSP;
import parkourterminal.data.GlobalData;
import parkourterminal.data.inputdata.InputData;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3d;

public class SpeedData {
    private Double deltaX = 0D;
    private Double deltaY = 0D;
    private Double deltaZ = 0D;
    private Vector2d speedVector =new Vector2d(0,0);
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
            if(firstTickStart==false){
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
