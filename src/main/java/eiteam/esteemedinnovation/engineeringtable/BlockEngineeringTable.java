package eiteam.esteemedinnovation.engineeringtable;

import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

import static net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

public class BlockEngineeringTable extends Block {
    BlockEngineeringTable() {
        super(Material.ROCK);
        setHardness(3.5F);
        setHarvestLevel("pickaxe", 1);
        setCreativeTab(EsteemedInnovation.tab);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tileentity = world.getTileEntity(pos);

        if (tileentity instanceof TileEntityEngineeringTable) {
            player.openGui(EsteemedInnovation.instance, 2, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        TileEntity tileentity = world.getTileEntity(pos);

        if (tileentity instanceof TileEntityEngineeringTable) {
            IItemHandler itemHandler = tileentity.getCapability(ITEM_HANDLER_CAPABILITY, null);
            assert itemHandler != null;
            InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), itemHandler.getStackInSlot(0));
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileEntityEngineeringTable();
    }
}