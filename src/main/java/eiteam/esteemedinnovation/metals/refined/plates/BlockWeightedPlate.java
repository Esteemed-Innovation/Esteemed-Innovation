package eiteam.esteemedinnovation.metals.refined.plates;

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

import static eiteam.esteemedinnovation.metals.MetalsModule.METAL_PLATE;

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
        return METAL_PLATE;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(METAL_PLATE, 1, damageDropped(state));
    }
}
