package parkourterminal.data.macroData.intf;

import javax.crypto.Mac;
import java.util.Vector;

public class Macro {
    private final Vector<Operation> operation_lists;
    private final String name;
    public Macro(Macro macro){
        this.name=macro.name;
        operation_lists=new Vector<Operation>();
        for(Operation operation:macro.operation_lists){
            this.operation_lists.add(new Operation(operation));
        }
    }
    public Macro(String name){
        operation_lists=new Vector<Operation>();
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public int getSize(){
        return operation_lists.size();
    }
    public Vector<Operation> getMacro(){
        return this.operation_lists;
    }
    public Operation duplicateLine(int index){
        if(index<0||index>=operation_lists.size()){
            return null;
        }
        Operation oldOne=operation_lists.get(index);
        Operation newOne= new Operation(oldOne);
        operation_lists.insertElementAt(newOne,index+1);
        return newOne;
    }
    public Operation newLine(int index){
        if(index<-1||index>=operation_lists.size()){
            return null;
        }
        Operation newOne= new Operation();
        operation_lists.insertElementAt(newOne,index+1);
        return newOne;
    }
    public void deleteLine(int index){
        if(index<0||index>=operation_lists.size()){
            return;
        }
        operation_lists.remove(index);
    }
}
