package parkourterminal.data.landingblock;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.AxisAlignedBB;
import parkourterminal.data.landingblock.intf.*;
import parkourterminal.global.json.TerminalJsonConfig;
import parkourterminal.util.BlockUtils;
import parkourterminal.util.NumberWrapper;
import parkourterminal.util.SendMessageHelper;

import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;
import java.util.ArrayList;
import java.util.List;

public class LandingBlockData {
    private boolean bbVisible =true;
    private boolean hasBox=false;
    private WholeCollisionBox wholeCollisionBox=new WholeCollisionBox(new ArrayList<AxisAlignedBB>(),LBbox.NON_BOX);
    private LBaxis lBaxis =LBaxis.BOTH;
    private LBbox lBbox=LBbox.NON_BOX;
    private LBmod lBmod=LBmod.Land;
    private Double[] offsets =new Double[]{Double.NaN,Double.NaN,Double.NaN};
    private Double[] pb =new Double[]{Double.NaN,Double.NaN,Double.NaN};
    double last2posZ=Double.NEGATIVE_INFINITY;

    public LandingBlockData() {

    }

    public boolean hasBox() {
        return hasBox;
    }

    public WholeCollisionBox getWholeCollisionBox() {
        return wholeCollisionBox;
    }

    public List<AxisAlignedBB> getAABBs() {
        return this.wholeCollisionBox.getBoxes();
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

    public void setAABBs(WholeCollisionBox wholeCollisionBox) {
        hasBox=true;
        lBaxis =LBaxis.BOTH;
        lBbox=LBbox.NON_BOX;
        lBmod=LBmod.Land;
        this.wholeCollisionBox=wholeCollisionBox;
    }
    public void clearAABBs(){
        hasBox=false;
        this.wholeCollisionBox=new WholeCollisionBox(new ArrayList<AxisAlignedBB>(),LBbox.NON_BOX);
    }

    public void setlBaxis(LBaxis lBaxis) {
        this.lBaxis = lBaxis;
    }
    public void toggleLbAxis(){
        int index=this.lBaxis.getIndex()+1;
        this.lBaxis=LBaxis.getAxis(index);
    }

    public void setlBbox(LBbox lBbox) {
        this.lBbox = lBbox;
    }

    public void setlBmod(LBmod lBmod) {
        this.lBmod = lBmod;
    }
    public void toggleLbmod(){
        int index=this.lBmod.getIndex()+1;
        this.lBmod=LBmod.getMod(index);
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
        AxisAlignedBB union= BlockUtils.UnionAll(getAABBs());
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
        Vector3d offset=wholeCollisionBox.calculateOffset(new Vector2d(posX,posZ));
        if (TerminalJsonConfig.getLandBlockJson().isSendChatOffset()){
            SendMessageHelper.addChatMessage(player,"X Offset:"+NumberWrapper.toDecimalString(offset.x));
            SendMessageHelper.addChatMessage(player,"Z Offset:"+NumberWrapper.toDecimalString(offset.y));
        }
        setOffsets( new Double[]{offset.x,offset.y,offset.z});
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

    public boolean isBbVisible() {
        return bbVisible;
    }

    public void setBbVisible(boolean bbVisible) {
        this.bbVisible = bbVisible;
    }
    public void toggleBbVisible() {
        this.bbVisible = !this.bbVisible;
    }
}
