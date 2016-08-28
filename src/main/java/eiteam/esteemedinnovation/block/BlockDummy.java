package eiteam.esteemedinnovation.block;

import eiteam.esteemedinnovation.init.blocks.SteamMachineryBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import static eiteam.esteemedinnovation.init.blocks.SteamMachineryBlocks.Blocks.ROCK_SMASHER;

public class BlockDummy extends Block {
    public BlockDummy() {
        super(Material.IRON);
        setHardness(50.0F);
        setLightOpacity(0);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        return true;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return false;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityDummyBlock();
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block) {
        int smasherCount = 0;
        smasherCount += world.getBlockState(pos.east()).getBlock() == ROCK_SMASHER.getBlock() ? 1 : 0;
        smasherCount += world.getBlockState(pos.west()) == ROCK_SMASHER.getBlock() ? 1 : 0;
        smasherCount += world.getBlockState(pos.north()) == ROCK_SMASHER.getBlock() ? 1 : 0;
        smasherCount += world.getBlockState(pos.south()) == ROCK_SMASHER.getBlock() ? 1 : 0;

        if (smasherCount < 2) {
            world.setBlockToAir(pos);
        }
    }

    @Override
    public boolean isAir(IBlockState state, IBlockAccess world, BlockPos pos){
        return true;
    }

    @Override
    public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player){
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return new ItemStack(SteamMachineryBlocks.Blocks.ROCK_SMASHER.getBlock());
    }
}
