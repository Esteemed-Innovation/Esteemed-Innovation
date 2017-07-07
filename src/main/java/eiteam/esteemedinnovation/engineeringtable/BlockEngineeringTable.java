package eiteam.esteemedinnovation.engineeringtable;

import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockEngineeringTable extends Block {
    public BlockEngineeringTable() {
        super(Material.ROCK);
        setHardness(3.5F);
        setCreativeTab(EsteemedInnovation.tab);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        } else {
            TileEntity tileentity = world.getTileEntity(pos);

            if (tileentity instanceof TileEntityEngineeringTable) {
                player.openGui(EsteemedInnovation.instance, 2, world, pos.getX(), pos.getY(), pos.getZ());
            }

            return true;
        }
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityEngineeringTable();
    }
}