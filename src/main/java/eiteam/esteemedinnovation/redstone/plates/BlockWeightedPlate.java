package eiteam.esteemedinnovation.redstone.plates;

import eiteam.esteemedinnovation.init.items.MetalItems;
import net.minecraft.block.BlockPressurePlateWeighted;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.Random;

public class BlockWeightedPlate extends BlockPressurePlateWeighted {
    private int plateMeta;

    public BlockWeightedPlate(int maxWeight, int plateMeta) {
        super(Material.IRON, maxWeight);
        setHardness(0.5F);
        setSoundType(SoundType.WOOD);
        this.plateMeta = plateMeta;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return plateMeta;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return MetalItems.Items.Types.PLATE.getItem();
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(MetalItems.Items.Types.PLATE.getItem(), 1, damageDropped(state));
    }
}
