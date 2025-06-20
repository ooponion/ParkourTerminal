package parkourterminal.simulation.manager;

import net.minecraft.block.BlockSlime;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import parkourterminal.simulation.sim.PlayerState;
import parkourterminal.simulation.intf.IBlockOnCollision;
import parkourterminal.simulation.intf.IBlockOnLanded;

import java.util.HashMap;

public class BlockInteractionManager {
    private HashMap<String, IBlockOnLanded> onLandedHashMap=new HashMap<>();
    private HashMap<String, IBlockOnCollision> onCollisionHashMap=new HashMap<>();
    private static BlockInteractionManager instance = new BlockInteractionManager();
    public BlockInteractionManager() {
        addOnLandedInterfaces();
        addOnCollisionInterfaces();
    }
    public static BlockInteractionManager getInstance() {
        return instance;
    }
    private void addOnLandedInterfaces(){
        addOnLandedInterface(BlockSlime.class, new IBlockOnLanded() {
            @Override
            public void OnLanded(PlayerState state) {
                if (state.isSneaking())
                {
                    state.motionY = 0.0D;
                }
                else if (state.motionY < 0.0D)
                {
                    state.motionY  = -state.motionY;
                }
            }
        });
    }
    private void addOnCollisionInterfaces(){
        addOnCollisionInterface(BlockSlime.class,new IBlockOnCollision() {

            @Override
            public void onEntityCollidedWithBlock(PlayerState state, BlockPos pos, IBlockState blockstate) {

            }

            @Override
            public void onEntityCollidedWithBlock(PlayerState state, BlockPos pos) {
                if (Math.abs(state.motionY) < 0.1D && !state.isSneaking())
                {
                    double d0 = 0.4D + Math.abs(state.motionY) * 0.2D;
                    state.motionX *= d0;
                    state.motionZ *= d0;
                }
            }
        });
        addOnCollisionInterface(BlockWeb.class,new IBlockOnCollision() {
            @Override
            public void onEntityCollidedWithBlock(PlayerState state, BlockPos pos, IBlockState blockstate) {
                state.setInWeb(true);
            }

            @Override
            public void onEntityCollidedWithBlock(PlayerState state, BlockPos pos) {

            }
        });
        addOnCollisionInterface(BlockSoulSand.class,new IBlockOnCollision() {

            @Override
            public void onEntityCollidedWithBlock(PlayerState state, BlockPos pos, IBlockState blockstate) {
                state.motionX *= 0.4D;
                state.motionZ *= 0.4D;
            }
            @Override
            public void onEntityCollidedWithBlock(PlayerState state, BlockPos pos) {

            }
        });
    }
    private void addOnLandedInterface(Class<?> clazz, IBlockOnLanded onLandedInterface) {
       onLandedHashMap.put(clazz.getName(), onLandedInterface);
    }
    private void addOnCollisionInterface(Class<?> clazz, IBlockOnCollision onCollisionInterface) {
        onCollisionHashMap.put(clazz.getName(), onCollisionInterface);
    }
    public void OnLanded(Class<?> clazz, PlayerState state) {
        onLandedHashMap.getOrDefault(clazz.getName(), new IBlockOnLanded() {
            @Override
            public void OnLanded(PlayerState state) {
                state.motionY = 0.0D;
            }
        }).OnLanded(state);
    }
    public void OnCollision(Class<?> clazz, PlayerState state, BlockPos pos, IBlockState blockState) {

        onCollisionHashMap.getOrDefault(clazz.getName(),new IBlockOnCollision() {
            @Override
            public void onEntityCollidedWithBlock(PlayerState state, BlockPos pos, IBlockState blockstate) {

            }

            @Override
            public void onEntityCollidedWithBlock(PlayerState state, BlockPos pos) {

            }
        }).onEntityCollidedWithBlock(state,pos,blockState);
    }
    public void OnCollision(Class<?> clazz, PlayerState state, BlockPos pos) {

        onCollisionHashMap.getOrDefault(clazz.getName(),new IBlockOnCollision() {
            @Override
            public void onEntityCollidedWithBlock(PlayerState state, BlockPos pos, IBlockState blockstate) {

            }

            @Override
            public void onEntityCollidedWithBlock(PlayerState state, BlockPos pos) {

            }
        }).onEntityCollidedWithBlock(state,pos);
    }
}
