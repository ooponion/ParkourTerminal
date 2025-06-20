package parkourterminal.global.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import parkourterminal.freecamera.CameraController;
import parkourterminal.gui.screens.intf.instantiationScreen.intf.ScreenID;
import parkourterminal.gui.screens.intf.instantiationScreen.manager.ScreenManager;
import parkourterminal.input.TerminalWindowManager;
import parkourterminal.tests.SpeedTest;
import parkourterminal.util.SendMessageHelper;
import parkourterminal.util.SystemOutHelper;

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
    private static final KeyBinding TEST_KEY = new KeyBinding(
            "TEST ONLY",
            Keyboard.KEY_N,
            "Parkour Terminal"
    );

    private final TerminalWindowManager windowManager = new TerminalWindowManager();

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new KeyHandler());
        net.minecraftforge.fml.client.registry.ClientRegistry.registerKeyBinding(OPEN_TERMINAL);
        net.minecraftforge.fml.client.registry.ClientRegistry.registerKeyBinding(OPEN_GUI);
        net.minecraftforge.fml.client.registry.ClientRegistry.registerKeyBinding(TEST_KEY);
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
            if (Minecraft.getMinecraft().currentScreen != null) {
                System.out.println("currentScreen:" + Minecraft.getMinecraft().currentScreen.getClass());
            }
            ScreenManager.SwitchToScreen(new ScreenID("TerminalGuiScreen"));
            if (Minecraft.getMinecraft().currentScreen != null) {
                System.out.println("openScreen:" + Minecraft.getMinecraft().currentScreen.getClass());
            }
        }else if (TEST_KEY.isPressed()) {
            CameraController.FREECAM.toggle();
            String message="disable free camera.";
            if(CameraController.FREECAM.isActive()){
                message="enable free camera.";
            }
            SendMessageHelper.addChatMessage(Minecraft.getMinecraft().thePlayer, message);
        }
    }
}