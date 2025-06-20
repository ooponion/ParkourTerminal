package parkourterminal.simulation.sim;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;

public class EntityPlayerSim {
    private static float speedInAir = 0.02F;
    static{
        applyEntityAttributes();
    }
    protected static void applyEntityAttributes()
    {
//        EntityLivingBaseSim.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0D);
        EntityLivingBaseSim.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.10000000149011612D);
    }
    public static void updateEntityActionState(PlayerState state)
    {
        //this.rotationYawHead = this.rotationYaw;
    }
    public static void onUpdate(PlayerState state)
    {
        EntityLivingBaseSim.onUpdate(state);

//
//        this.prevChasingPosX = this.chasingPosX;
//        this.prevChasingPosY = this.chasingPosY;
//        this.prevChasingPosZ = this.chasingPosZ;
//        double d5 = this.posX - this.chasingPosX;
//        double d0 = this.posY - this.chasingPosY;
//        double d1 = this.posZ - this.chasingPosZ;
//        double d2 = 10.0D;
//
//        if (d5 > d2)
//        {
//            this.prevChasingPosX = this.chasingPosX = this.posX;
//        }
//
//        if (d1 > d2)
//        {
//            this.prevChasingPosZ = this.chasingPosZ = this.posZ;
//        }
//
//        if (d0 > d2)
//        {
//            this.prevChasingPosY = this.chasingPosY = this.posY;
//        }
//
//        if (d5 < -d2)
//        {
//            this.prevChasingPosX = this.chasingPosX = this.posX;
//        }
//
//        if (d1 < -d2)
//        {
//            this.prevChasingPosZ = this.chasingPosZ = this.posZ;
//        }
//
//        if (d0 < -d2)
//        {
//            this.prevChasingPosY = this.chasingPosY = this.posY;
//        }
//
//        this.chasingPosX += d5 * 0.25D;
//        this.chasingPosZ += d1 * 0.25D;
//        this.chasingPosY += d0 * 0.25D;
    }
    public static void onLivingUpdate(PlayerState state)
    {


//        this.prevCameraYaw = this.cameraYaw;
        EntityLivingBaseSim.onLivingUpdate(state);
        IAttributeInstance iattributeinstance = EntityLivingBaseSim.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
        iattributeinstance.setBaseValue((double)state.capabilities.getWalkSpeed());

        state.jumpMovementFactor = speedInAir;

        if (state.isSprinting)
        {
            state.jumpMovementFactor = (float)((double)state.jumpMovementFactor + (double)speedInAir * 0.3D);
        }
//        setAIMoveSpeed((float)iattributeinstance.getAttributeValue());
//        state.cameraYaw += (f - this.cameraYaw) * 0.4F;
//        state.cameraPitch += (f1 - this.cameraPitch) * 0.8F;


    }
    public static float getAIMoveSpeed()
    {
        return (float) EntityLivingBaseSim.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();
    }
}
