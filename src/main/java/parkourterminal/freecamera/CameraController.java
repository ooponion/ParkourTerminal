package parkourterminal.freecamera;

import net.minecraft.client.Minecraft;

import java.util.Optional;

public class CameraController {
    public static final CameraController FREECAM = new CameraController();
    private final Minecraft mc = Minecraft.getMinecraft();
    private Optional<EntityCamera> camera=Optional.empty();
    private boolean allowingInteraction=false;
    private boolean active = false;

    public void enable() {
        if (active) return;

        camera=Optional.of( new EntityCamera(mc));
        mc.setRenderViewEntity(camera.get());
        active = true;
    }

    public void disable() {
        if (!active) return;

        mc.setRenderViewEntity(mc.thePlayer);
        camera = Optional.empty();
        active = false;
    }

    public void toggle() {
        if (active) disable(); else enable();
    }

    public void update() {
        if (!active || !camera.isPresent()) return;
        camera.ifPresent(EntityCamera::updateEveryTick);
    }

    public boolean isActive() {
        return active;
    }
    public boolean isAllowingInteraction() {
        return allowingInteraction;
    }
    public void setAllowingInteraction(boolean allowingInteraction) {
        this.allowingInteraction = allowingInteraction;
    }
    public void setYawAndPitch(float yawDelta,float pitchDelta){
        if(camera.isPresent()) {
            camera.get().setAngles(yawDelta,pitchDelta);
        }
    }
    public boolean shouldBlockInteraction() {
        return CameraController.FREECAM.isActive() &&
                !CameraController.FREECAM.isAllowingInteraction();
    }
}