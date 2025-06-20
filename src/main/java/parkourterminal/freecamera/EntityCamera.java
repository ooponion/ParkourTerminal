package parkourterminal.freecamera;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import parkourterminal.simulation.sim.PlayerState;

public class EntityCamera extends EntityPlayer {
    private PlayerState playerState;
    private Minecraft mc;
    private MovementInput movementInput;
    private int sprintingTicksLeft=0;
    protected int sprintToggleTimer=0;
    public EntityCamera(Minecraft mc) {
        super(mc.theWorld,mc.thePlayer.getGameProfile());
        this.copyLocationAndAnglesFrom(mc.thePlayer);
        this.noClip = false;
        this.mc = mc;
        this.playerState=new PlayerState(mc.thePlayer);
        this.movementInput = new MovementInputFromOptions(mc.gameSettings);
        this.capabilities = new PlayerCapabilities();
        this.capabilities.allowFlying = true;
        this.capabilities.isFlying = false;
        this.capabilities.setFlySpeed(0.05f);
        this.capabilities.disableDamage = true;
        this.capabilities.allowEdit = false;
        this.inventory=this.mc.thePlayer.inventory;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }
    public void updateEveryTick(){
        lastTickPosX=prevPosX = posX;
        lastTickPosY=prevPosY = posY;
        lastTickPosZ=prevPosZ = posZ;

        prevRotationYaw = rotationYaw;
        prevRotationPitch = rotationPitch;
        onUpdate();
    }

    @Override
    public boolean isInvisible() {
        return true;
    }

    @Override
    public boolean isInvisibleToPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public boolean isSpectator() {
        return false;
    }
    @Override
    public void setSprinting(boolean sprinting)
    {
        super.setSprinting(sprinting);
        sprintingTicksLeft = sprinting ? 600 : 0;
    }
    protected boolean isCurrentViewEntity()
    {
        return this.mc.getRenderViewEntity() == this;
    }
    @Override
    public void onLivingUpdate(){
        if (this.sprintingTicksLeft > 0)
        {
            --this.sprintingTicksLeft;

            if (this.sprintingTicksLeft == 0)
            {
                this.setSprinting(false);
            }
        }

        if (this.sprintToggleTimer > 0)
        {
            --this.sprintToggleTimer;
        }

        if (this.timeUntilPortal > 0)
        {
            --this.timeUntilPortal;
        }

        boolean flag = this.movementInput.jump;
        boolean flag1 = this.movementInput.sneak;
        float f = 0.8F;
        boolean flag2 = this.movementInput.moveForward >= f;
        this.movementInput.updatePlayerMoveState();
        if (this.isUsingItem() && !this.isRiding())
        {
            this.movementInput.moveStrafe *= 0.2F;
            this.movementInput.moveForward *= 0.2F;
            this.sprintToggleTimer = 0;
        }

       boolean flag3 = (float)this.getFoodStats().getFoodLevel() > 6.0F || this.capabilities.allowFlying;

        if (this.onGround && !flag1 && !flag2 && this.movementInput.moveForward >= f && !this.isSprinting() && flag3 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness))
        {
            if (this.sprintToggleTimer <= 0 && !this.mc.gameSettings.keyBindSprint.isKeyDown())
            {
                this.sprintToggleTimer = 7;
            }
            else
            {
                this.setSprinting(true);
            }
        }

        if (!this.isSprinting() && this.movementInput.moveForward >= f && flag3 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness) && this.mc.gameSettings.keyBindSprint.isKeyDown())
        {
            this.setSprinting(true);
        }

        if (this.isSprinting() && (this.movementInput.moveForward < f || this.isCollidedHorizontally || !flag3))
        {
            this.setSprinting(false);
        }

        if (this.capabilities.allowFlying)
        {
            if (this.mc.playerController.isSpectatorMode())
            {
                if (!this.capabilities.isFlying)
                {
                    this.capabilities.isFlying = true;
                    this.sendPlayerAbilities();
                }
            }
            else if (!flag && this.movementInput.jump)
            {
                if (this.flyToggleTimer == 0)
                {
                    this.flyToggleTimer = 7;
                }
                else
                {
                    this.capabilities.isFlying = !this.capabilities.isFlying;
                    this.sendPlayerAbilities();
                    this.flyToggleTimer = 0;
                }
            }
        }

        if (this.capabilities.isFlying && this.isCurrentViewEntity())
        {
            if (this.movementInput.sneak)
            {
                this.motionY -= (double)(this.capabilities.getFlySpeed() * 3.0F);
            }

            if (this.movementInput.jump)
            {
                this.motionY += (double)(this.capabilities.getFlySpeed() * 3.0F);
            }
        }
        super.onLivingUpdate();
        if (this.onGround && this.capabilities.isFlying && !this.mc.playerController.isSpectatorMode())
        {
            this.capabilities.isFlying = false;
            this.sendPlayerAbilities();
        }
    }
    @Override
    public void updateEntityActionState()
    {
        super.updateEntityActionState();

        if (this.isCurrentViewEntity())
        {
            this.moveStrafing = this.movementInput.moveStrafe;
            this.moveForward = this.movementInput.moveForward;
            this.isJumping = this.movementInput.jump;
            this.setSneaking(this.movementInput.sneak);
        }
    }
    @Override
    public boolean isServerWorld()
    {
        return true;
    }
    @Override
    public boolean canAttackPlayer(EntityPlayer other)
    {
        return false;
    }
    @Override
    public void attackTargetEntityWithCurrentItem(Entity targetEntity){
        return;
    }
    @Override
    public boolean canAttackWithItem()
    {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }
    public boolean canBePushed()
    {
        return false;
    }
    @Override
    public boolean hitByEntity(Entity entityIn) {
        return false;
    }
}

//copy
/*
* public class EntityCamera extends EntityPlayer {
    private final Minecraft mc;
    public MovementInput movementInput;
    public EntityCamera(Minecraft mc) {
        super(mc.theWorld,mc.thePlayer.getGameProfile());
        this.mc = mc;
        this.ignoreFrustumCheck = true; // 关键：允许在视锥体外渲染
        this.setInvisible(true);
        this.capabilities.disableDamage=true;
        this.capabilities.allowFlying=false;
        this.movementInput=new MovementInputFromOptions(mc.gameSettings);
    }

    @Override
    public boolean isSpectator() {
        return false;
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 0; // 确保在正确渲染通道
    }
    private static int sprintingTicksLeft=0;
    protected static int sprintToggleTimer=0;
    @Override
    public void updateEntityActionthis()
    {
        super.updateEntityActionthis();

        if (this.isCurrentViewEntity())
        {
            this.moveStrafing = this.movementInput.moveStrafe;
            this.moveForward = this.movementInput.moveForward;
            this.isJumping = this.movementInput.jump;
        }
    }
    protected boolean isCurrentViewEntity()
    {
        return this.mc.getRenderViewEntity() == this;
    }
    @Override
    public void onUpdate(){
        super.onUpdate();
        EntityPlayerSPSim.onUpdate(Playerthis.getInstance());
        Playerthis.setFromPlayerthis(this);
        this.foodStats.setFoodLevel(20);
    }
    public void setSprinting(boolean sprinting)
    {
        super.setSprinting(sprinting);
        sprintingTicksLeft = sprinting ? 600 : 0;
    }
    @Override
    public void onLivingUpdate(){
        super.onLivingUpdate();
        //EntityPlayerSPSim.onLivingUpdate(Playerthis.getInstance());
//        if (sprintingTicksLeft > 0)
//        {
//            --sprintingTicksLeft;
//
//            if (sprintingTicksLeft == 0)
//            {
//                setSprinting(false);
//            }
//        }
//        if (sprintToggleTimer > 0)
//        {
//            --sprintToggleTimer;
//        }
//        boolean flag = this.movementInput.jump;
//        boolean flag1 = movementInput.sneak;
//        float f = 0.8F;
//        boolean flag2 = movementInput.moveForward >= f;
//        movementInput.updatePlayerMovethis();
//        boolean flag3 = this.capabilities.allowFlying;
//        if (isUsingItem())
//        {
//            movementInput.moveStrafe *= 0.2F;
//            movementInput.moveForward *= 0.2F;
//        }
//        if (this.onGround && !flag1 && !flag2 && this.movementInput.moveForward >= f && !this.isSprinting() && flag3 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness))
//        {
//            if (this.sprintToggleTimer <= 0 && !this.mc.gameSettings.keyBindSprint.isKeyDown())
//            {
//                this.sprintToggleTimer = 7;
//            }
//            else
//            {
//                this.setSprinting(true);
//            }
//        }
//
//        if (!this.isSprinting() && this.movementInput.moveForward >= f && flag3 && !this.isUsingItem() && !this.isPotionActive(Potion.blindness) && this.mc.gameSettings.keyBindSprint.isKeyDown())
//        {
//            this.setSprinting(true);
//        }
//
//        if (this.isSprinting() && (this.movementInput.moveForward < f || this.isCollidedHorizontally || !flag3))
//        {
//            this.setSprinting(false);
//        }
//
//        if (this.capabilities.allowFlying)
//        {
//            if (this.mc.playerController.isSpectatorMode())
//            {
//                if (!this.capabilities.isFlying)
//                {
//                    this.capabilities.isFlying = true;
//                    this.sendPlayerAbilities();
//                }
//            }
//            else if (!flag && this.movementInput.jump)
//            {
//                if (this.flyToggleTimer == 0)
//                {
//                    this.flyToggleTimer = 7;
//                }
//                else
//                {
//                    this.capabilities.isFlying = !this.capabilities.isFlying;
//                    this.sendPlayerAbilities();
//                    this.flyToggleTimer = 0;
//                }
//            }
//        }
//
//        if (this.capabilities.isFlying && this.isCurrentViewEntity())
//        {
//            if (this.movementInput.sneak)
//            {
//                this.motionY -= (double)(this.capabilities.getFlySpeed() * 3.0F);
//            }
//
//            if (this.movementInput.jump)
//            {
//                this.motionY += (double)(this.capabilities.getFlySpeed() * 3.0F);
//            }
//        }
//        super.onLivingUpdate();
    }
}*/