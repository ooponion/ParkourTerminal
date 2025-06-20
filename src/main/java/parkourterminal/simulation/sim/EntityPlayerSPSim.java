package parkourterminal.simulation.sim;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;

public class EntityPlayerSPSim {
    private static int sprintingTicksLeft=0;
    protected static int sprintToggleTimer=0;
    public static void updateEntityActionState(PlayerState state)
    {
//        int[] strat=GlobalData.getInputData().getOperation().actualDirectionKey();
//        state.moveStrafing= -strat[1];
//        state.moveForward=strat[0];
//        state.isJumping =GlobalData.getInputData().getOperation().isJump();
//        state.prevRenderArmYaw = state.renderArmYaw;
//        state.prevRenderArmPitch = state.renderArmPitch;
//        state.renderArmPitch = (float)((double)state.renderArmPitch + (double)(state.rotationPitch - state.renderArmPitch) * 0.5D);
//        state.renderArmYaw = (float)((double)state.renderArmYaw + (double)(state.rotationYaw - state.renderArmYaw) * 0.5D);
        EntityPlayerSim.updateEntityActionState(state);
        state.moveStrafing = state.movementInput.moveStrafe;
        state.moveForward = state.movementInput.moveForward;
        state.isJumping = state.movementInput.jump;
        state.prevRenderArmYaw = state.renderArmYaw;
        state.prevRenderArmPitch = state.renderArmPitch;
        state.renderArmPitch = (float)((double)state.renderArmPitch + (double)(state.rotationPitch - state.renderArmPitch) * 0.5D);
        state.renderArmYaw = (float)((double)state.renderArmYaw + (double)(state.rotationYaw - state.renderArmYaw) * 0.5D);
    }
    public static void onUpdate(PlayerState state){
        EntityPlayerSim.onUpdate(state);
    }
    public static void setSprinting(PlayerState state, boolean sprinting)
    {
        EntityLivingBaseSim.setSprinting(state,sprinting);
        sprintingTicksLeft = sprinting ? 600 : 0;
    }
    public static void onLivingUpdate(PlayerState state){
        if (sprintingTicksLeft > 0)
        {
            --sprintingTicksLeft;

            if (sprintingTicksLeft == 0)
            {
                setSprinting(state,false);
            }
        }
        if (sprintToggleTimer > 0)
        {
            --sprintToggleTimer;
        }
        boolean flag1 = state.movementInput.sneak;
        float f = 0.8F;
        boolean flag2 = state.movementInput.moveForward >= f;
        state.movementInput.updatePlayerMoveState();

        if (state.isUsingItem())
        {
            state.movementInput.moveStrafe *= 0.2F;
            state.movementInput.moveForward *= 0.2F;
        }

        if (state.onGround && !flag1 && !flag2 && state.movementInput.moveForward >= f && !state.isSprinting && !state.isUsingItem() && !EntityLivingBaseSim.isPotionActive(Potion.blindness))
        {
            if (sprintToggleTimer <= 0 && !Minecraft.getMinecraft().gameSettings.keyBindSprint.isKeyDown())
            {
                sprintToggleTimer = 7;
            }
            else
            {
                setSprinting(state,true);
            }
        }

        if (!state.isSprinting && state.movementInput.moveForward >= f && !state.isUsingItem() && !EntityLivingBaseSim.isPotionActive(Potion.blindness) && Minecraft.getMinecraft().gameSettings.keyBindSprint.isKeyDown())
        {
            setSprinting(state,true);
        }

        if (state.isSprinting && (state.movementInput.moveForward < f || state.isCollidedHorizontally ))
        {
            setSprinting(state,false);
        }

        EntityPlayerSim.onLivingUpdate(state);

    }
}
