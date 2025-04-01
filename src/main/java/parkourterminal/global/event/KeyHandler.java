package parkourterminal.global.event;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import parkourterminal.gui.screens.intf.instantiationScreen.intf.ScreenID;
import parkourterminal.gui.screens.intf.instantiationScreen.manager.ScreenManager;
import parkourterminal.input.TerminalWindowManager;

public class KeyHandler {
    private static final KeyBinding OPEN_TERMINAL = new KeyBinding(
            "Open Parkour Terminal",
            Keyboard.KEY_O,
            "Parkour Terminal"
    );
    private static final KeyBinding OPEN_GUI = new KeyBinding(
            "Open GUI",
            Keyboard.KEY_EQUALS,
            "Parkour Terminal"
    );

    private final TerminalWindowManager windowManager = new TerminalWindowManager();

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new KeyHandler());
        net.minecraftforge.fml.client.registry.ClientRegistry.registerKeyBinding(OPEN_TERMINAL);
        net.minecraftforge.fml.client.registry.ClientRegistry.registerKeyBinding(OPEN_GUI);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {

        if (OPEN_TERMINAL.isPressed()) {
            if (windowManager.isWindowVisible()) {
                windowManager.focusWindow();
            } else {
                windowManager.createWindow();
            }
        } else if (OPEN_GUI.isPressed()) {
            ScreenManager.SwitchToScreen(new ScreenID("TerminalGuiScreen"));
        }
    }
}