package parkourterminal.input;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class KeyHandler {
    private static final KeyBinding OPEN_TERMINAL = new KeyBinding(
            "Open Parkour Terminal",
            Keyboard.KEY_O,
            "Parkour Terminal"
    );

    private final TerminalWindowManager windowManager = new TerminalWindowManager();

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new KeyHandler());
        net.minecraftforge.fml.client.registry.ClientRegistry.registerKeyBinding(OPEN_TERMINAL);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (OPEN_TERMINAL.isPressed()) {
            if (windowManager.isWindowVisible()) {
                windowManager.focusWindow();
            } else {
                windowManager.createWindow();
            }
        }
    }
}