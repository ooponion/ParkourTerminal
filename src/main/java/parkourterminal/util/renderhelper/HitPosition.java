package parkourterminal.util.renderhelper;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

import javax.vecmath.Vector3d;


public class HitPosition {
    private final Vector3d hitVec;
    private final AxisAlignedBB hitBox;
    public HitPosition(Vec3 hitVec,AxisAlignedBB hitBox){
        this.hitVec=new Vector3d(hitVec.xCoord,hitVec.yCoord,hitVec.zCoord);
        this.hitBox=hitBox;
    }
    public HitPosition(Vector3d hitVec,AxisAlignedBB hitBox){
        this.hitVec=hitVec;
        this.hitBox=hitBox;
    }
    public Vector3d getHitVec() {
        return hitVec;
    }

    public AxisAlignedBB getHitBox() {
        return hitBox;
    }
}
