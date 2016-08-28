package flaxbeard.steamcraft.block;

import flaxbeard.steamcraft.tile.TileEntityFunnel;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFunnel extends Block {
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    public BlockFunnel() {
        super(Material.ANVIL);
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, POWERED);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        boolean powered = false;
        TileEntityFunnel funnel = (TileEntityFunnel) world.getTileEntity(pos);
        if (funnel != null) {
            powered = funnel.getWorld().isBlockPowered(pos);
        }
        return state.withProperty(POWERED, powered);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityFunnel();
    }
}
