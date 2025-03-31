package parkourterminal.data.EntitySpeedData;

import net.minecraft.client.entity.EntityPlayerSP;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector2f;
import javax.vecmath.Vector3d;

public class SpeedData {
    Double deltaX = Double.NaN;
    Double deltaY = Double.NaN;
    Double deltaZ = Double.NaN;

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
        Vector2d vector=new Vector2d(deltaX,deltaZ);
        vector.normalize();
        return new Vector2f(vector);
    }
}
