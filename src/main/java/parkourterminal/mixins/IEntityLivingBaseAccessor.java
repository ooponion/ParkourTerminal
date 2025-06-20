package parkourterminal.mixins;

import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityLivingBase.class)
public interface IEntityLivingBaseAccessor {
    @Accessor("moveStrafing")
    float getMoveStrafing();

    @Accessor("moveForward")
    float getMoveForward();
}
