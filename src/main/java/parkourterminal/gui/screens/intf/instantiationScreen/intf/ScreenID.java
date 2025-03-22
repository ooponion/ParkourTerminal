package parkourterminal.gui.screens.intf.instantiationScreen.intf;

public class ScreenID {
    private String id="defaultScreen";
    public ScreenID(String id){
        setID(id);
    }
    public String getID(){return id;}
    void setID(String id){this.id=id;}
    @Override
    public boolean equals(Object obj){
        if(obj instanceof ScreenID){
            String id=((ScreenID) obj).getID();
            return (id.equals(getID()));
        }
        return false;
    }
}
