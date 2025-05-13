package parkourterminal.data.landingblock.intf;

public enum  LBaxis {
    BOTH(0,"Both"),
    X_AXIS(1,"X"),
    Z_AXIS(2,"Z");

    private final String displayName;
    private final int index;

    LBaxis(int index,String displayName) {
        this.displayName = displayName;
        this.index=index;
    }
    public String getDisplayName() {
        return displayName;
    }
    public int getIndex(){
        return index;
    }
    public static LBaxis getAxis(int index){
        switch (index%3){
            case 0: return BOTH;
            case 1: return X_AXIS;
            case 2: return Z_AXIS;
        }
        return BOTH;
    }
}
