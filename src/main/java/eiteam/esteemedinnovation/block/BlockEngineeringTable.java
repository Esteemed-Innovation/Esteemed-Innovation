package eiteam.esteemedinnovation.block;

import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.tile.TileEntityEngineeringTable;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockEngineeringTable extends BlockContainer {
    public BlockEngineeringTable() {
        super(Material.ROCK);
        setHardness(3.5F);
        setCreativeTab(EsteemedInnovation.tab);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack held, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        } else {
            TileEntity tileentity = world.getTileEntity(pos);

            if (tileentity != null && tileentity instanceof TileEntityEngineeringTable) {
                player.openGui(EsteemedInnovation.instance, 2, world, pos.getX(), pos.getY(), pos.getZ());
            }

            return true;
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityEngineeringTable();
    }
}