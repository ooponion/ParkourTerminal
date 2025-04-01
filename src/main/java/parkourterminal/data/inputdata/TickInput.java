package parkourterminal.data.inputdata;

import java.util.BitSet;

public class TickInput {
    private final BitSet keys = new BitSet(8);
    private final String strategy;
    private final boolean isJump;

    public TickInput(boolean a, boolean w, boolean s, boolean d, boolean sneak, boolean sprint, boolean jump,boolean onGround,double motionY,double lastMotionY){
        keys.set(0, a);
        keys.set(1, w);
        keys.set(2, s);
        keys.set(3, d);
        keys.set(4, sneak);
        keys.set(5, sprint);
        keys.set(6, jump);
        keys.set(7, onGround);
        strategy=initStrategy();
        isJump=lastMotionY<=0&&motionY>0;
    }
    public int[] actualDirectionKey(){//longitudinal & sideways (a:-1,d:+1,w:+1,s:-1)
        int sideways=(isA()?-1:0)+(isD()?1:0);
        int longitudinal=(isS()?-1:0)+(isW()?1:0);
        return new int[]{longitudinal,sideways};
    };
    public boolean isActualJump(){
        return keys.get(7)&&isJump()&&isJump;
    }
    public boolean isActualSprint(){
        return actualDirectionKey()[0]==1&&isSprint();
    }
    public boolean isA(){
        return keys.get(0);
    }
    public boolean isD(){
        return keys.get(3);
    }
    public boolean isW(){
        return keys.get(1);
    }
    public boolean isS(){
        return keys.get(2);
    }
    public boolean isSneak(){
        return keys.get(4);
    }
    public boolean isSprint(){
        return keys.get(5);
    }
    public boolean isJump(){
        return keys.get(6);
    }
    public boolean isOnGround(){
        return keys.get(7);
    }
    private boolean compareTo(int longitudinal,int sideways,Boolean sneak,Boolean actualSprint,Boolean actualJump,Boolean isOnGround){
        int[] motion=actualDirectionKey();
        boolean result =
                ((longitudinal == 3&&motion[0]!=0)||(longitudinal == 2) || longitudinal == motion[0]) &&
                        ((sideways == 3&&motion[1]!=0)||(sideways == 2)  || sideways == motion[1]) &&
                        (sneak == null || sneak == isSneak()) &&
                        (actualSprint == null || actualSprint == isActualSprint()) &&
                        (actualJump == null || actualJump == isActualJump())&&
                        (isOnGround==null||isOnGround==isOnGround());
        return result;
    }

    public boolean equals(String strategy){
        return strategy.equals(getStrategy());
    }
    public String getStrategy() {
        return strategy;
    }
    private String initStrategy(){
        if (compareTo(0, 0, false, false, false,null)) return "none"; // jam
        if (compareTo(1, 2, false, true, true,true)) return "Jam";
        if (compareTo(-1, 2, false, false, true,true)) return "bwJam";
        if (compareTo(1, 2, false, null, false,true)) return "hhStart"; // hh
        if (compareTo(1, 0, false, false, true,true)) return "walkJam"; // fmm
        if (compareTo(1, 2, false, true, false,false)) return "sprintAir";
        if (compareTo(0, 0, false, null, true,true)) return "jumpOnly"; // pessi
        if (compareTo(3, 2, false, false, false,false)) return "walkAir";
        if (compareTo(0, 3, false, null, true,true)) return "markStart"; // mark
        if (compareTo(0, 3, false, null, false,false)) return "markMid";
        if (compareTo(1, 2, true, null, false,true)) return "burstStart"; // burst
        return "unknown";
    }
    public String toString(){
        return String.format("name:%s<<a:%s,w:%s,s:%s,d:%s,sneak:%s,sprint:%s,jump:%s,onGround:%s",getStrategy(),keys.get(0),keys.get(1),keys.get(2),keys.get(3),keys.get(4),keys.get(5),keys.get(6),keys.get(7));
    }
    public String getDirectionKeys(){
        String keys="";
        if(isA()){
            keys+="A";
        }
        if(isW()){
            keys+="W";
        }
        if(isD()){
            keys+="D";
        }
        if(isS()){
            keys+="S";
        }
        return keys;
    }
}
