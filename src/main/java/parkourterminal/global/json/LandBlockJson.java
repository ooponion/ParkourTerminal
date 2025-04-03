package parkourterminal.global.json;

import net.minecraft.util.BlockPos;

public class LandBlockJson {
    private boolean displayable=true;
    private boolean sendChatPb=true;

    public void setDisplayable(boolean displayable) {
        this.displayable = displayable;
    }

    public boolean isDisplayable() {
        return displayable;
    }

    public boolean isSendChatPb() {
        return sendChatPb;
    }

    public void setSendChatPb(boolean sendChatPb) {
        this.sendChatPb = sendChatPb;
    }
}
