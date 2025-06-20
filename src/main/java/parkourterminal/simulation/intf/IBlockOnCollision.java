package parkourterminal.simulation.intf;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import parkourterminal.simulation.sim.PlayerState;

public interface IBlockOnCollision {
    void onEntityCollidedWithBlock(PlayerState state, BlockPos pos, IBlockState blockstate);
    void onEntityCollidedWithBlock(PlayerState state, BlockPos pos);
}
