package eiteam.esteemedinnovation.transport.item;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static eiteam.esteemedinnovation.transport.TransportationModule.ASTROLABE;

public class BlockItemMortar extends Block {
    public BlockItemMortar() {
        super(Material.IRON);
        setHardness(3.5F);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityItemMortar();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntityItemMortar tile = (TileEntityItemMortar) world.getTileEntity(pos);
        if (tile == null) {
            return false;
        }

        ItemStack heldItem = player.getHeldItem(hand);
        if (heldItem != null && heldItem.getItem() == ASTROLABE &&
          heldItem.hasTagCompound() && heldItem.getTagCompound().hasKey("targetX") &&
          world.provider.getDimension() == heldItem.getTagCompound().getInteger("dim")) {
            tile.xTarget = heldItem.getTagCompound().getInteger("targetX");
            tile.zTarget = heldItem.getTagCompound().getInteger("targetZ");
            tile.hasTarget = true;
        }
        return false;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntityItemMortar tileentitymortar = (TileEntityItemMortar) world.getTileEntity(pos);
        if (tileentitymortar != null) {
            InventoryHelper.dropInventoryItems(world, pos, tileentitymortar);
            world.updateComparatorOutputLevel(pos, state.getBlock());
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
}