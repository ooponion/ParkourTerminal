package parkourterminal.util;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.AxisAlignedBB;
import parkourterminal.data.GlobalData;
import parkourterminal.data.landingblock.intf.AABB;

import java.util.ArrayList;
import java.util.List;

public class LandingBlockHelper {
    public static boolean isLanded(EntityPlayerSP player) {
        double lastPosY = player.lastTickPosY;
        double posY = player.posY;
        if (lastPosY <= posY) {
            return false;
        }
        List<AABB> bbs = WrappedAABBList(BlockUtils.getAABBsUnderPlayerFeet(player.worldObj, player));
        List<AABB> intersection = new ArrayList<AABB>();
        for (AABB landBb : GlobalData.getLandingBlock().getWrappedAABBs()) {
            for (AABB bb : bbs) {
                if (landBb.equals(bb)) {
                    intersection.add(landBb);
                    break;
                }
            }
        }

        return (!intersection.isEmpty());
    }


    public static List<AxisAlignedBB> UnwrappedAABBList(List<AABB> aabbs){
        List<AxisAlignedBB> list=new ArrayList<AxisAlignedBB>();
        for(AABB i:aabbs){
            list.add(i.getAxisAlignedBB());
        }
        return list;
    }
    public static List<AABB> WrappedAABBList(List<AxisAlignedBB> axisAlignedBBs){
        List<AABB> list=new ArrayList<AABB>();
        for(AxisAlignedBB i:axisAlignedBBs){
            list.add(new AABB(i));
        }
        return list;
    }
    public static AxisAlignedBB UnionAll(List<AABB> axisAlignedBBs){
        if(axisAlignedBBs.isEmpty()){
            return null;
        }
        if(axisAlignedBBs.size()==1){
            return axisAlignedBBs.get(0).getAxisAlignedBB();
        }
        AxisAlignedBB lastUnion=axisAlignedBBs.get(0).getAxisAlignedBB();
        for (int i=1;i<axisAlignedBBs.size();i++){
            lastUnion=axisAlignedBBs.get(i).getAxisAlignedBB().union(lastUnion);
        }
        return lastUnion;
    }

}
