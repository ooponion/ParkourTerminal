package parkourterminal.data.EntityJumpData;

import net.minecraft.client.entity.EntityPlayerSP;
import parkourterminal.data.GlobalData;

public class JumpData {
    private Double jumpX= Double.NaN;
    private Double jumpY= Double.NaN;
    private Double jumpZ= Double.NaN;
    private float preturn=0f;
    private float firstTurning=0f;
    private float lastTurning=0f;
    private float lastTickTurning=0f;
    private float jumpAngle=0f;
    private float last45=0f;
    private float secondTurning=0f;
    private float thirdTurning=0f;
    private float lastFacing=0f;
    private int airTick=0;

    public Double getJumpX() {
        return jumpX;
    }

    public Double getJumpY() {
        return jumpY;
    }

    public Double getJumpZ() {
        return jumpZ;
    }

    public float getJumpAngle() {
        return jumpAngle;
    }

    public float getFirstTurning() {
        return firstTurning;
    }

    public float getSecondTurning() {
        return secondTurning;
    }

    public float getThirdTurning() {
        return thirdTurning;
    }
    public float getLastTurning(){
        return lastTurning;
    }
    public int getAirTime(){
        return airTick;
    }

    public float getLast45() {
        return last45;
    }

    public float getPreturn() {
        return preturn;
    }

    public void Update(EntityPlayerSP player){
        if(GlobalData.getInputData().getOperation().isActualJump()){//Jump
            airTick=0;
            jumpX= player.posX;
            jumpY= player.posY;
            jumpZ= player.posZ;
        }
        else if (!player.onGround) {// 玩家在空中
            airTick++;
        }
        lastTurning=player.rotationYaw-lastFacing;
        switch ((int) airTick){
            case 0:
                preturn=lastTickTurning;
                firstTurning=lastTurning;
                jumpAngle=player.rotationYaw;
                break;
            case 1:
                secondTurning=lastTurning;
                int[] input=GlobalData.getInputData().getOperation().actualDirectionKey();
                if((input[1]==-1||input[1]==1)&&input[0]==1){
                    last45=secondTurning;
                }
                break;
            case 2:thirdTurning=lastTurning;break;
            default:break;
        }

        lastFacing=player.rotationYaw;
        lastTickTurning=lastTurning;
    }
}
