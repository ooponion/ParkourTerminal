package parkourterminal.data.inputdata.intf;

import parkourterminal.data.inputdata.TickInput;

public class ActionPulse {
    private long duration=0;
    private TickInput tickInput=null;
    public ActionPulse(long duration,TickInput input){
        this.duration=duration;
        tickInput=input;
    }
    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public TickInput getTickInput() {
        return tickInput;
    }

    public void setTickInput(TickInput tickInput) {
        this.tickInput = tickInput;
    }
}
