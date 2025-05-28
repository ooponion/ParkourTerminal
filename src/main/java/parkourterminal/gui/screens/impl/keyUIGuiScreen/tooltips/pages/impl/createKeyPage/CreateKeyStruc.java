package parkourterminal.gui.screens.impl.keyUIGuiScreen.tooltips.pages.impl.createKeyPage;

import net.minecraft.client.settings.KeyBinding;

public class CreateKeyStruc {
    public KeyBinding keyBinding;
    public int x;
    public int y;
    public CreateKeyStruc(KeyBinding keyBinding, int x, int y) {
        this.keyBinding = keyBinding;
        this.x = x;
        this.y = y;
    }
}
