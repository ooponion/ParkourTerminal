package parkourterminal.data.macroData;

import parkourterminal.data.macroData.intf.Macro;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MacroData {
    private Map<String, Macro> macrosMap=new HashMap<String, Macro>();
    private String current_macro_name="";
    public Macro getMacroByName(String name){
        if(macrosMap.get(name)==null){
            return null;
        }
        return new Macro(macrosMap.get(name));
    }
    public Set<String> getMacroNames(){
        return getMacrosMap().keySet();
    }
    public void addMacro(Macro macro){
        macrosMap.put(macro.getName(), macro);
    }
    public void deleteMacro(String name){
        macrosMap.remove(name);
    }
    public Macro getCurrentMacro() {
        return getMacroByName(current_macro_name);
    }
    public String getCurrentMacroName() {
        return current_macro_name;
    }
    public void setCurrentMacro(String name){
        current_macro_name=name;
    }
    public Map<String, Macro> getMacrosMap() {
        return new HashMap<String, Macro>(macrosMap);
    }
    public void setMacrosMap(Map<String, Macro> macrosMap) {
        this.macrosMap = new HashMap<String, Macro>(macrosMap);
    }
}
