package parkourterminal.data.landingblock;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import parkourterminal.data.globalData.GlobalData;
import parkourterminal.data.landingblock.intf.AABB;
import parkourterminal.data.landingblock.intf.LBaxis;
import parkourterminal.data.landingblock.intf.LBbox;
import parkourterminal.global.GlobalConfig;
import parkourterminal.global.json.TerminalJsonConfig;
import parkourterminal.util.LandingBlockHelper;
import parkourterminal.util.SendMessageHelper;

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
        UpdatePB(player);
    }
    private void UpdateOffsets(EntityPlayerSP player){
        double lposX=player.lastTickPosX;
        double lposY=player.lastTickPosY;
        double lposZ=player.lastTickPosZ;
        double posY=player.posY;
        double minX=lposX-0.3;
        double maxX=lposX+0.3;
        double minZ=lposZ-0.3;
        double maxZ=lposZ+0.3;
        AxisAlignedBB union=LandingBlockHelper.UnionAll(getWrappedAABBs());
        if(union==null){
            return;
        }
        if(!(posY<=union.maxY&&union.maxY<lposY)){
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
            offsetMinXR= union.maxX-lposX;
            offsetMinXL= lposX-union.minX;
            offsetMinZT= union.maxZ-lposZ;
            offsetMinZB= lposZ-union.minZ;
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
        if (TerminalJsonConfig.getLandBlockJson().isSendChatOffset()){
            SendMessageHelper.addChatMessage(player,"X Offset:"+
                    String.format("%." + GlobalConfig.precision + "f", Xoffset));
            SendMessageHelper.addChatMessage(player,"Z Offset:"+
                    String.format("%." + GlobalConfig.precision + "f", Zoffset));
        }
        setOffsets( new Double[]{Xoffset,Zoffset,totalOffset});
    }
    private void UpdatePB(EntityPlayerSP player){
        if(getlBaxis()==LBaxis.X_AXIS){
            double lastX=getPb()[0].isNaN()?Double.NEGATIVE_INFINITY:getPb()[0];
            double offsetX=getOffsets()[0].isNaN()?Double.NEGATIVE_INFINITY:getOffsets()[0];
            if(lastX<offsetX){
                if (TerminalJsonConfig.getLandBlockJson().isSendChatPb()){
                    SendMessageHelper.addChatMessage(player,"new pb x:"+
                            String.format("%." + GlobalConfig.precision + "f", offsetX));
                }
                setPb(getOffsets());
            }
            return;
        } else if (getlBaxis()==LBaxis.Z_AXIS) {
            double lastZ=getPb()[1].isNaN()?Double.NEGATIVE_INFINITY:getPb()[1];
            double offsetZ=getOffsets()[1].isNaN()?Double.NEGATIVE_INFINITY:getOffsets()[1];
            if(lastZ<offsetZ){
                if (TerminalJsonConfig.getLandBlockJson().isSendChatPb()){
                    SendMessageHelper.addChatMessage(player,"new pb z:"+
                            String.format("%." + GlobalConfig.precision + "f", offsetZ));
                }
                setPb(getOffsets());
            }
            return;
        }
        double lastPB=getPb()[2].isNaN()?Double.NEGATIVE_INFINITY:getPb()[2];
        double offset=getOffsets()[2].isNaN()?Double.NEGATIVE_INFINITY:getOffsets()[2];
        if(lastPB<offset){
            if (TerminalJsonConfig.getLandBlockJson().isSendChatPb()){
                SendMessageHelper.addChatMessage(player,"new pb:"+
                        String.format("%." + GlobalConfig.precision + "f", offset));
            }
            setPb(getOffsets());
        }
    }
}
