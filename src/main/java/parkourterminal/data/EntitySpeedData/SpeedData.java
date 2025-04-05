package parkourterminal.data.EntitySpeedData;

import net.minecraft.client.entity.EntityPlayerSP;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3d;

public class SpeedData {
    Double deltaX = 0D;
    Double deltaY = 0D;
    Double deltaZ = 0D;

    public SpeedData(){

    }
    public void Update(EntityPlayerSP player){
        deltaX= player.posX-player.lastTickPosX;
        deltaY= player.posY-player.lastTickPosY;
        deltaZ= player.posZ-player.lastTickPosZ;
    }
    public Vector3d getSpeed(){
        return new Vector3d(deltaX,deltaY,deltaZ);
    }
    public Vector2f getSpeedVector(){
        Vector2d vector=new Vector2d(Math.hypot(deltaX,deltaZ),-Math.toDegrees(Math.atan2(deltaX,deltaZ))+0.0000000000000000001D);
        return new Vector2f(vector);
    }
}
