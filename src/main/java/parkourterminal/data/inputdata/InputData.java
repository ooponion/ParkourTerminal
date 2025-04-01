package parkourterminal.data.inputdata;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import parkourterminal.data.inputdata.intf.ActionPulse;
import parkourterminal.data.inputdata.intf.IndexedStack;

import java.util.Arrays;

public class InputData {
    private TickInput operation =new TickInput(false,false,false,false,false,false,false,false,0,0);
    private IndexedStack<ActionPulse> pulseStack=new IndexedStack<ActionPulse>(20);
    private String strat="none";
    private boolean lastOnGround=false;
    private double lastMotionY=0d;
    private String currentStart="unknown";
    public TickInput getOperation() {
        return operation;
    }
    public void UpdateOperation(EntityPlayerSP player){
        GameSettings settings = Minecraft.getMinecraft().gameSettings;
        boolean forward = settings.keyBindForward.isKeyDown();
        boolean back = settings.keyBindBack.isKeyDown();
        boolean left = settings.keyBindLeft.isKeyDown();
        boolean right = settings.keyBindRight.isKeyDown();
        boolean sprint = settings.keyBindSprint.isKeyDown();
        boolean jump = settings.keyBindJump.isKeyDown();
        boolean sneak = settings.keyBindSneak.isKeyDown();
        operation=new TickInput(left,forward,back,right,sneak,sprint,jump, lastOnGround, player.motionY,lastMotionY);
        lastOnGround= player.onGround;
        lastMotionY=player.motionY;
        if(!operation.equals(currentStart)){
            currentStart=operation.getStrategy();
            pulseStack.push(new ActionPulse(0,operation));
        }
        if(pulseStack.get(0)!=null){
            pulseStack.get(0).setDuration(pulseStack.get(0).getDuration()+1);
        }
        String matchedStrat=getMatchedStrat();
        if(matchedStrat!=null){
            strat=matchedStrat;
        }
    }
    public String getStrat(){
        return strat;
    }
    private boolean stratsMatched(String... args){
        if(args.length>pulseStack.size()){
            return false;
        }
        boolean allMatched=true;
        for(int i=0;i<args.length;i++){
            ActionPulse actionPulse=pulseStack.get(i);
            allMatched&=(actionPulse!=null&&actionPulse.getTickInput().getStrategy().contains(args[args.length-i-1]));
        }
        return allMatched;
    }
    private String getMatchedStrat(){
        if(stratsMatched("burstStart","hhStart","Jam")){
            ActionPulse actionPulse=pulseStack.get(2);
            if(actionPulse!=null){
                return "Burst "+actionPulse.getDuration()+"t";
            }
            return "Burst";
        }else if(stratsMatched("walkJam","walkAir","sprintAir")){
            ActionPulse actionPulse=pulseStack.get(1);
            if(actionPulse!=null){
                return "Fmm "+(actionPulse.getDuration()+1)+"t";
            }
            return "Fmm";
        }else if(stratsMatched("walkJam","sprintAir")){
            return "Max Fmm";
        }else if(stratsMatched("markStart","Air")){
            return "Max Mark";
        }else if(stratsMatched("markStart","markMid","Air")){
            ActionPulse actionPulse=pulseStack.get(1);
            if(actionPulse!=null){
                return "Mark "+(1+actionPulse.getDuration())+"t";
            }
            return "Mark";
        }else if(stratsMatched("jumpOnly","none","Air")){
            ActionPulse actionPulse=pulseStack.get(1);
            if(actionPulse!=null){
                return "Pessi "+(actionPulse.getDuration()+1)+"t";
            }
            return "Pessi";
        }else if(stratsMatched("jumpOnly","Air")){
            return "Max Pessi";
        }
        else if(stratsMatched("hhStart","Jam")){
            ActionPulse actionPulse=pulseStack.get(1);
            if(actionPulse!=null){
                return "HH "+actionPulse.getDuration()+"t";
            }
            return "HH";
        }else if(stratsMatched("none","bwJam")){
            return "BWJam";
        }else if(stratsMatched("none","Jam")){
            return "Jam";
        }
        return null;
    }
}
