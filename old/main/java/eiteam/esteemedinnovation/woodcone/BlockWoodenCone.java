package eiteam.esteemedinnovation.woodcone;

import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockWoodenCone extends Block {
    public BlockWoodenCone() {
        super(Material.WOOD);
        setCreativeTab(EsteemedInnovation.tab);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public void onEntityWalk(World world, BlockPos pos, Entity entity) {
        entity.attackEntityFrom(DamageSource.CACTUS, 1);
    }
}
