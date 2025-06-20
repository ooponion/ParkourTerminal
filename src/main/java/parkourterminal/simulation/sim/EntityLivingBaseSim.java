package parkourterminal.simulation.sim;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

import java.util.Map;
import java.util.UUID;

public class EntityLivingBaseSim {
    private static BaseAttributeMap attributeMap;
    private static final UUID sprintingSpeedBoostModifierUUID = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
    private static final AttributeModifier sprintingSpeedBoostModifier = (new AttributeModifier(sprintingSpeedBoostModifierUUID, "Sprinting speed boost", 0.30000001192092896D, 2)).setSaved(false);
    private static final Map<Integer, PotionEffect> activePotionsMap = Maps.<Integer, PotionEffect>newHashMap();
    static{
        applyEntityAttributes();
    }
    public static void applyEntityAttributes()
    {
        //EntityPlayerSim.applyEntityAttributes();//因为static 报nullPointer,所以直接在EntityPlayer 的static字段加载了
//        getAttributeMap().registerAttribute(SharedMonsterAttributes.maxHealth);
//        getAttributeMap().registerAttribute(SharedMonsterAttributes.knockbackResistance);
        getAttributeMap().registerAttribute(SharedMonsterAttributes.movementSpeed);
    }
    public static void onUpdate(PlayerState state){
        EntitySim.onUpdate(state);
        EntityPlayerSPSim.onLivingUpdate(state);
//        double d0 = state.posX - state.prevPosX;
//        double d1 = state.posZ - state.prevPosZ;
//        float f = (float)(d0 * d0 + d1 * d1);
//        float f1 = state.renderYawOffset;
//        float f2 = 0.0F;
//        state.prevOnGroundSpeedFactor = state.onGroundSpeedFactor;
//        float f3 = 0.0F;
//
//        if (f > 0.0025000002F)
//        {
//            f3 = 1.0F;
//            f2 = (float)Math.sqrt((double)f) * 3.0F;
//            f1 = (float)MathHelper.atan2(d1, d0) * 180.0F / (float)Math.PI - 90.0F;
//        }
//
//        if (state.swingProgress > 0.0F)
//        {
//            f1 = state.rotationYaw;
//        }
//
//        if (!state.onGround)
//        {
//            f3 = 0.0F;
//        }
//
//        state.onGroundSpeedFactor += (f3 - state.onGroundSpeedFactor) * 0.3F;
    }
    public static void onLivingUpdate(PlayerState state) {//almost pass,check when the jumptick is correct(when deal with the onUpdate)
        if (state.jumpTicks > 0)
        {
            --state.jumpTicks;
        }
        EntityPlayerSPSim.updateEntityActionState(state);
//        if(state.isJumping){
//            SystemOutHelper.printf("Jumping at %d", state.jumpTicks);
//        }
        if (Math.abs(state.motionX) < 0.005D)
        {
            state.motionX = 0.0D;
        }
        if (Math.abs(state.motionY) < 0.005D)
        {
            state.motionY = 0.0D;
        }
        if (Math.abs(state.motionZ) < 0.005D)
        {
            state.motionZ = 0.0D;
        }
        if (state.isJumping)
        {
            if (state.isInWater)
            {
                state.motionY += 0.03999999910593033D;
            }
            else if (state.isInLava())
            {
                state.motionY += 0.03999999910593033D;
            }
            else if (state.onGround && state.jumpTicks == 0)
            {
                jump(state);
                state.jumpTicks = 10;
//                SystemOutHelper.printf("jumpTick=10");
            }
        }else{
            state.jumpTicks = 0;
//            SystemOutHelper.printf("jumpTick=0");
        }
        state.moveStrafing *= 0.98F;
        state.moveForward *= 0.98F;
        moveEntityWithHeading(state);
    }

    public static void jump(PlayerState state)//pass
    {
        state.motionY = 0.42F;

//        if (state.isPotionActive(Potion.jump))
//        {
//            state.motionY += (double)((float)(state.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
//        }

        if (state.isSprinting)
        {
            float f = state.rotationYaw * 0.017453292F;
            state.motionX -= (double)(MathHelper.sin(f) * 0.2F);
            state.motionZ += (double)(MathHelper.cos(f) * 0.2F);
        }
    }

    public static void moveEntityWithHeading(PlayerState state) {
        if (!state.isInWater || state.isFlying) {
            if (!state.isInLava() || state.isFlying) {
                float f4 = 0.91F;

                if (state.onGround) {
                    f4 = state.worldIn.getBlockState(new BlockPos(MathHelper.floor_double(state.posX), MathHelper.floor_double(state.boundingBox.minY) - 1, MathHelper.floor_double(state.posZ))).getBlock().slipperiness * 0.91F;
                }

                float f = 0.16277136F / (f4 * f4 * f4);
                float f5;

                if (state.onGround) {
                    f5 = EntityPlayerSim.getAIMoveSpeed() * f;
                } else {
                    f5 = state.jumpMovementFactor;
                }

                moveFlying(state,  state.moveStrafing,state.moveForward, f5);
                f4 = 0.91F;

                if (state.onGround) {
                    f4 = state.worldIn.getBlockState(new BlockPos(MathHelper.floor_double(state.posX), MathHelper.floor_double(state.boundingBox.minY) - 1, MathHelper.floor_double(state.posZ))).getBlock().slipperiness * 0.91F;
                }

                if (state.isOnLadder()) {
                    float f6 = 0.15F;
                    state.motionX = MathHelper.clamp_double(state.motionX, (double) (-f6), (double) f6);
                    state.motionZ = MathHelper.clamp_double(state.motionZ, (double) (-f6), (double) f6);
                    state.fallDistance = 0.0F;

                    if (state.motionY < -0.15D) {
                        state.motionY = -0.15D;
                    }

                    if (state.isSneaking() && state.motionY < 0.0D) {
                        state.motionY = 0.0D;
                    }
                }

                EntitySim.moveEntity(state,state.motionX, state.motionY, state.motionZ);

                if (state.isCollidedHorizontally && state.isOnLadder()) {
                    state.motionY = 0.2D;
                }

                if (state.worldIn.isRemote && (!state.worldIn.isBlockLoaded(new BlockPos((int) state.posX, 0, (int) state.posZ)) || !state.worldIn.getChunkFromBlockCoords(new BlockPos((int) state.posX, 0, (int) state.posZ)).isLoaded())) {
                    if (state.posY > 0.0D) {
                        state.motionY = -0.1D;
                    } else {
                        state.motionY = 0.0D;
                    }
                } else {
                    state.motionY -= 0.08D;
                }

                state.motionY *= 0.9800000190734863D;
                state.motionX *= (double) f4;
                state.motionZ *= (double) f4;

            } else {
                double d1 = state.posY;
                moveFlying(state,state.moveStrafing, state.moveForward, 0.02F);
                EntitySim.moveEntity(state,state.motionX, state.motionY, state.motionZ);
                state.motionX *= 0.5D;
                state.motionY *= 0.5D;
                state.motionZ *= 0.5D;
                state.motionY -= 0.02D;

                if (state.isCollidedHorizontally && isOffsetPositionInLiquid(state,state.motionX, state.motionY + 0.6000000238418579D - state.posY + d1, state.motionZ)) {
                    state.motionY = 0.30000001192092896D;
                }
            }
        } else {
            double d0 = state.posY;
            float f1 = 0.8F;
            float f2 = 0.02F;
//            float f3 = (float) EnchantmentHelper.getDepthStriderModifier(state);
//
//            if (f3 > 3.0F) {
//                f3 = 3.0F;
//            }
//
//            if (!state.onGround) {
//                f3 *= 0.5F;
//            }
//
//            if (f3 > 0.0F) {
//                f1 += (0.54600006F - f1) * f3 / 3.0F;
//                f2 += (state.landMovementFactor - f2) * f3 / 3.0F;
//            }

            moveFlying(state,state.moveStrafing,state.moveForward, f2);
            EntitySim.moveEntity(state,state.motionX, state.motionY, state.motionZ);
            state.motionX *= (double) f1;
            state.motionY *= 0.800000011920929D;
            state.motionZ *= (double) f1;
            state.motionY -= 0.02D;

            if (state.isCollidedHorizontally && isOffsetPositionInLiquid(state,state.motionX, state.motionY + 0.6000000238418579D - state.posY + d0, state.motionZ)) {
                state.motionY = 0.30000001192092896D;
            }
        }

    }
    private static boolean isOffsetPositionInLiquid(PlayerState state, double x, double y, double z)
    {
        AxisAlignedBB axisalignedbb = state.boundingBox.offset(x, y, z);
        return isLiquidPresentInAABB( state,axisalignedbb);
    }
    private static boolean isLiquidPresentInAABB(PlayerState state, AxisAlignedBB bb)
    {
        return  EntitySim.getCollidingBoundingBoxes(state, bb).isEmpty() && !state.worldIn.isAnyLiquid(bb);
    }
    public static void moveFlying(PlayerState state, float strafe, float forward, float friction)
    {
        float f = strafe * strafe + forward * forward;

        if (f >= 1.0E-4F)
        {
            f = MathHelper.sqrt_float(f);

            if (f < 1.0F)
            {
                f = 1.0F;
            }

            f = friction / f;
            strafe = strafe * f;
            forward = forward * f;
            float f1 = MathHelper.sin(state.rotationYaw * (float)Math.PI / 180.0F);
            float f2 = MathHelper.cos(state.rotationYaw * (float)Math.PI / 180.0F);
            state.motionX += (double)(strafe * f2 - forward * f1);
            state.motionZ += (double)(forward * f2 + strafe * f1);
        }
    }
    public static BaseAttributeMap getAttributeMap()
    {
        if (attributeMap == null)
        {
            attributeMap = new ServersideAttributeMap();
        }

        return attributeMap;
    }
    public static IAttributeInstance getEntityAttribute(IAttribute attribute)
    {
        return getAttributeMap().getAttributeInstance(attribute);
    }
    public static void setSprinting(PlayerState state, boolean sprinting)
    {
        state.isSprinting = sprinting;
        IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.movementSpeed);

        if (iattributeinstance.getModifier(sprintingSpeedBoostModifierUUID) != null)
        {
            iattributeinstance.removeModifier(sprintingSpeedBoostModifier);
        }

        if (sprinting)
        {
            iattributeinstance.applyModifier(sprintingSpeedBoostModifier);
        }
    }

    public static boolean isPotionActive(int potionId)
    {
        //return activePotionsMap.containsKey(Integer.valueOf(potionId));
        return false;
    }

    public static boolean isPotionActive(Potion potionIn)
    {
        //return activePotionsMap.containsKey(Integer.valueOf(potionIn.id));
        return false;
    }
    public static void updateFallState(PlayerState state, double y, boolean onGroundIn, Block blockIn, BlockPos pos)
    {
        if (!state.isInWater)
        {
            EntitySim.handleWaterMovement(state);
        }
//
//        if (!this.worldObj.isRemote && this.fallDistance > 3.0F && onGroundIn)
//        {
//            IBlockState iblockstate = this.worldObj.getBlockState(pos);
//            Block block = iblockstate.getBlock();
//            float f = (float)MathHelper.ceiling_float_int(this.fallDistance - 3.0F);
//
//            if (block.getMaterial() != Material.air)
//            {
//                double d0 = (double)Math.min(0.2F + f / 15.0F, 10.0F);
//
//                if (d0 > 2.5D)
//                {
//                    d0 = 2.5D;
//                }
//
//                int i = (int)(150.0D * d0);
//                if ( !block.addLandingEffects( (WorldServer)this.worldObj, pos, iblockstate, this, i ) )
//                    ((WorldServer)this.worldObj).spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX, this.posY, this.posZ, i, 0.0D, 0.0D, 0.0D, 0.15000000596046448D, new int[] {Block.getStateId(iblockstate)});
//            }
//        }

        EntitySim.updateFallState(state,y, onGroundIn, blockIn, pos);
    }
}
