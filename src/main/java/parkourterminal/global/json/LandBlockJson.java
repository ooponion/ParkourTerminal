package parkourterminal.global.json;

import net.minecraft.util.BlockPos;

public class LandBlockJson {
    private boolean displayable=true;

    public void setDisplayable(boolean displayable) {
        this.displayable = displayable;
    }

    public boolean isDisplayable() {
        return displayable;
    }
}
