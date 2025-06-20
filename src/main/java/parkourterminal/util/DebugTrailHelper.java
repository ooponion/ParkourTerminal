package parkourterminal.util;

public class DebugTrailHelper {
    public static String getCaller(int index){
        if(index<=0){
            return "NULL";
        }
        StackTraceElement caller = Thread.currentThread().getStackTrace()[index+1];
        return "Caller["+index+"]: " + caller.getClassName() + "." + caller.getMethodName() + " at line " + caller.getLineNumber();
    }
}
