package parkourterminal.data.landingblock;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.AxisAlignedBB;
import parkourterminal.data.landingblock.intf.AABB;
import parkourterminal.data.landingblock.intf.LBaxis;
import parkourterminal.data.landingblock.intf.LBbox;
import parkourterminal.util.LandingBlockHelper;

import java.util.ArrayList;
import java.util.List;

public class LandingBlockData {
    private List<AABB> bbs=new ArrayList<AABB>();
    private LBaxis lBaxis =LBaxis.BOTH;
    private LBbox lBbox=LBbox.NON_BOX;
    private Double[] offsets =new Double[]{Double.NaN,Double.NaN,Double.NaN};
    private Double[] pb =new Double[]{Double.NaN,Double.NaN,Double.NaN};
    public LandingBlockData(List<AABB> bbs,LBaxis lBaxis,LBbox lBbox){
        this.bbs=bbs;
        this.lBaxis=lBaxis;
        this.lBbox=lBbox;
    }

    public LandingBlockData() {

    }

    public List<AxisAlignedBB> getAABBs() {
        return LandingBlockHelper.UnwrappedAABBList(this.bbs);
    }
    public List<AABB> getWrappedAABBs() {
        return bbs;
    }

    public LBaxis getlBaxis() {
        return lBaxis;
    }

    public LBbox getlBbox() {
        return lBbox;
    }

    public void setAABBs(List<AxisAlignedBB> bbs) {
        this.bbs=LandingBlockHelper.WrappedAABBList(bbs);
    }
    public void setWrappedAABBs(List<AABB> bbs) {
        this.bbs = bbs;
    }

    public void setlBaxis(LBaxis lBaxis) {
        this.lBaxis = lBaxis;
    }

    public void setlBbox(LBbox lBbox) {
        this.lBbox = lBbox;
    }

    public Double[] getOffsets() {
        return offsets;
    }

    public void setOffsets(Double[] offsets) {
        this.offsets = offsets;
    }

    public void setPb(Double[] pb) {
        this.pb = pb;
    }

    public Double[] getPb() {
        return pb;
    }

    public void Update( EntityPlayerSP player){

        UpdateOffsets(player);
        UpdatePB();
    }
    private void UpdateOffsets(EntityPlayerSP player){
        double lastPosY=player.lastTickPosY;
        double posX=player.posX;
        double posY=player.posY;
        double posZ=player.posZ;
        double minX=posX-0.3;
        double maxX=posX+0.3;
        double minZ=posZ-0.3;
        double maxZ=posZ+0.3;
        if(lastPosY<=posY){
            return;
        }
        AxisAlignedBB union=LandingBlockHelper.UnionAll(getWrappedAABBs());
        if(union==null){
            return;
        }
        if(!(posY<=union.maxY&&union.maxY<lastPosY)){
            return;
        }
        double offsetMinXR;
        double offsetMinXL;
        double offsetMinZT;
        double offsetMinZB;
        if(getlBbox()== LBbox.NON_BOX){//Non_Box case
            offsetMinXR= union.maxX-minX;
            offsetMinXL= maxX-union.minX;
            offsetMinZT= union.maxZ-minZ;
            offsetMinZB= maxZ-union.minZ;
        }else{//Box case
            offsetMinXR= union.maxX-posX;
            offsetMinXL= posX-union.minX;
            offsetMinZT= union.maxZ-posZ;
            offsetMinZB= posZ-union.minZ;
        }
        double totalOffset;
        double Xoffset=Math.min(offsetMinXR,offsetMinXL);
        double Zoffset=Math.min(offsetMinZB,offsetMinZT);
        if(Xoffset<-1.0|Zoffset<-1.0){
            return;
        }
        if(Xoffset*Zoffset>0){
            totalOffset=Math.hypot(Xoffset,Zoffset);
            if(Xoffset<0|Zoffset<0){
                totalOffset*=-1;
            }
        }else{
            totalOffset=Math.min(Xoffset,Zoffset);
        }
        setOffsets( new Double[]{Xoffset,Zoffset,totalOffset});
    }
    private void UpdatePB(){
        double lastPB=getPb()[2].isNaN()?Double.NEGATIVE_INFINITY:getPb()[2];
        double offset=getOffsets()[2].isNaN()?Double.NEGATIVE_INFINITY:getOffsets()[2];
        if(lastPB<offset){
            setPb(getOffsets());
        }
    }
}
