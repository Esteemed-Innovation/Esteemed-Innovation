package flaxbeard.steamcraft.block;

import flaxbeard.steamcraft.tile.TileEntityCreativeTank;
import flaxbeard.steamcraft.tile.TileEntitySteamTank;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockSteamTank extends BlockContainer {
    public static final PropertyBool IS_CREATIVE = PropertyBool.create("is_creative");

    public BlockSteamTank() {
        super(Material.IRON);
        setHardness(5F);
        setResistance(10F);
        setDefaultState(blockState.getBaseState().withProperty(IS_CREATIVE, false));
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, IS_CREATIVE);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(IS_CREATIVE) ? 1 : 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return meta == 0 ? getDefaultState() : getDefaultState().withProperty(IS_CREATIVE, true);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public ItemStack createStackedBlock(IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(this), 1, damageDropped(state));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return meta == 1 ? new TileEntityCreativeTank() : new TileEntitySteamTank();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> subBlocks) {
        subBlocks.add(new ItemStack(item, 1, 0));
        subBlocks.add(new ItemStack(item, 1, 1));
    }
}
