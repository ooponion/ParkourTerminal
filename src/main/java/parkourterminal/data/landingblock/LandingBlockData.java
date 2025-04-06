package parkourterminal.data.landingblock;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.AxisAlignedBB;
import parkourterminal.data.landingblock.intf.AABB;
import parkourterminal.data.landingblock.intf.LBaxis;
import parkourterminal.data.landingblock.intf.LBbox;
import parkourterminal.data.landingblock.intf.LBmod;
import parkourterminal.global.GlobalConfig;
import parkourterminal.global.json.TerminalJsonConfig;
import parkourterminal.util.LandingBlockHelper;
import parkourterminal.util.NumberWrapper;
import parkourterminal.util.SendMessageHelper;

import java.util.ArrayList;
import java.util.List;

public class LandingBlockData {
    private List<AABB> bbs=new ArrayList<AABB>();
    private LBaxis lBaxis =LBaxis.BOTH;
    private LBbox lBbox=LBbox.NON_BOX;
    private LBmod lBmod=LBmod.Land;
    private Double[] offsets =new Double[]{Double.NaN,Double.NaN,Double.NaN};
    private Double[] pb =new Double[]{Double.NaN,Double.NaN,Double.NaN};
    double last2posZ=Double.NEGATIVE_INFINITY;

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

    public LBmod getlBmod() {
        return lBmod;
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

    public void setlBmod(LBmod lBmod) {
        this.lBmod = lBmod;
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
        if(Double.NEGATIVE_INFINITY==last2posZ){
            last2posZ=player.lastTickPosZ;
        }
        UpdateOffsets(player);
        UpdatePB(player);
        last2posZ=player.lastTickPosZ;
    }
    private void UpdateOffsets(EntityPlayerSP player){
        AxisAlignedBB union=LandingBlockHelper.UnionAll(getWrappedAABBs());
        if(union==null){
            return;
        }
        double posX=player.lastTickPosX;
        double posY=player.lastTickPosY;
        double posZ=player.lastTickPosZ;
        if(getlBmod()==LBmod.Land){
            posX=player.lastTickPosX;
            posY=player.lastTickPosY;
            posZ=player.lastTickPosZ;
        } else if (getlBmod()==LBmod.Hit) {
            posX=player.posX;
            posY=player.posY;
            posZ=player.posZ;
        } else if (getlBmod()==LBmod.Z_neo) {
            posX=player.lastTickPosX;
            posY=player.lastTickPosY;
            posZ=last2posZ;
        }else if (getlBmod()==LBmod.Enter){
            posX=player.posX;
            posY=player.posY;
            posZ=player.posZ;
            if(!(player.posY<union.maxY&&player.posY>union.minY&&player.posY<player.lastTickPosY)){
                return;
            }
        }
        if(!(player.posY<=union.maxY&&union.maxY<player.lastTickPosY)&&getlBmod()!=LBmod.Enter){
            return;
        }
        double minX=posX-0.3;
        double maxX=posX+0.3;
        double minZ=posZ-0.3;
        double maxZ=posZ+0.3;

        double offsetMinXR;
        double offsetMinXL;
        double offsetMinZT;
        double offsetMinZB;
        //这部分之后要去掉
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
        if (TerminalJsonConfig.getLandBlockJson().isSendChatOffset()){
            SendMessageHelper.addChatMessage(player,"X Offset:"+NumberWrapper.toDecimalString(Xoffset));
            SendMessageHelper.addChatMessage(player,"Z Offset:"+NumberWrapper.toDecimalString(Zoffset));
        }
        setOffsets( new Double[]{Xoffset,Zoffset,totalOffset});
    }
    private void UpdatePB(EntityPlayerSP player){
        if(getlBaxis()==LBaxis.X_AXIS){
            double lastX=getPb()[0].isNaN()?Double.NEGATIVE_INFINITY:getPb()[0];
            double offsetX=getOffsets()[0].isNaN()?Double.NEGATIVE_INFINITY:getOffsets()[0];
            if(lastX<offsetX){
                if (TerminalJsonConfig.getLandBlockJson().isSendChatPb()){
                    SendMessageHelper.addChatMessage(player,"new pb x:"+NumberWrapper.toDecimalString(offsetX));
                }
                setPb(getOffsets());
            }
            return;
        } else if (getlBaxis()==LBaxis.Z_AXIS) {
            double lastZ=getPb()[1].isNaN()?Double.NEGATIVE_INFINITY:getPb()[1];
            double offsetZ=getOffsets()[1].isNaN()?Double.NEGATIVE_INFINITY:getOffsets()[1];
            if(lastZ<offsetZ){
                if (TerminalJsonConfig.getLandBlockJson().isSendChatPb()){
                    SendMessageHelper.addChatMessage(player,"new pb z:"+NumberWrapper.toDecimalString(offsetZ));
                }
                setPb(getOffsets());
            }
            return;
        }
        double lastPB=getPb()[2].isNaN()?Double.NEGATIVE_INFINITY:getPb()[2];
        double offset=getOffsets()[2].isNaN()?Double.NEGATIVE_INFINITY:getOffsets()[2];
        if(lastPB<offset){
            if (TerminalJsonConfig.getLandBlockJson().isSendChatPb()){
                SendMessageHelper.addChatMessage(player,"new pb:"+NumberWrapper.toDecimalString(offset));
            }
            setPb(getOffsets());
        }
    }
}
