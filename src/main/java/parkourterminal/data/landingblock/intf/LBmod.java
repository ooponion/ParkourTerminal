package parkourterminal.data.landingblock.intf;

public enum LBmod {
    Land(0,"Land"),
    Hit(1,"Hit"),
    Z_neo(2,"Z neo"),
    Enter(3,"Enter");
    private final String displayName;
    private final int index;

    LBmod(int index,String displayName) {
        this.displayName = displayName;
        this.index=index;
    }
    public String getDisplayName() {
        return displayName;
    }
    public int getIndex(){
        return index;
    }
    public static LBmod getMod(int index){
        switch (index%4){
            case 0: return Land;
            case 1: return Hit;
            case 2: return Z_neo;
            case 3: return Enter;
        }
        return Land;
    }
}
