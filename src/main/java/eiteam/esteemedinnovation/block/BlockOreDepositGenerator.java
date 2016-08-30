package eiteam.esteemedinnovation.block;

import eiteam.esteemedinnovation.init.blocks.OreBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

import static eiteam.esteemedinnovation.block.BlockGenericOre.OreBlockTypes.OVERWORLD_COPPER;
import static eiteam.esteemedinnovation.block.BlockGenericOre.OreBlockTypes.OVERWORLD_ZINC;

public class BlockOreDepositGenerator extends Block {
    public static final PropertyEnum<Types> VARIANT = PropertyEnum.create("variant", Types.class);
    public static final PropertyBool WORKED_OUT = PropertyBool.create("worked_out");

    public BlockOreDepositGenerator() {
        super(Material.ROCK);
        // Same values as BlockGenericOre.
        setResistance(5F);
        setHardness(3F);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 1);
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT, WORKED_OUT);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        // The variant occupies the first bit, the worked out value occupies the second bit.
        int variant = state.getValue(VARIANT).ordinal();
        int workedOut = (state.getValue(WORKED_OUT) ? 1 : 0) << 1;
        return variant + workedOut;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        Types variant = Types.LOOKUP[meta & 1];
        boolean workedOut = ((meta >> 1) & 1) == 1;
        return getDefaultState().withProperty(VARIANT, variant).withProperty(WORKED_OUT, workedOut);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(VARIANT).ordinal();
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(OreBlocks.Blocks.ORE_BLOCK);
    }

    @Override
    public int tickRate(World world) {
        // 20 minutes, the time in a single MC day.
        return 24_000;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        world.scheduleBlockUpdate(pos, this, tickRate(world), 0);
        generateAdjacentOres(world, pos, false);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        world.scheduleBlockUpdate(pos, this, tickRate(world), 0);
        generateAdjacentOres(world, pos, true);
    }

    /**
     * Generates ores adjacent in all directions to this deposit generator.
     * @param world The world
     * @param center The center (deposit generator) position
     * @param firstRun Whether this is the first time this is being generated. If this is not the first run, it will only
     *                 generate if the adjacent block is air, and will return as soon as it generates a single ore.
     *                 If it <b>is</b> the first run, then it will replace adjacent blocks if possible, and fill in all
     *                 spaces adjacent to it.
     */
    private void generateAdjacentOres(World world, BlockPos center, boolean firstRun) {
        IBlockState oreState = getOreState(world, center);
        boolean workedOut = world.getBlockState(center).getValue(WORKED_OUT);
        for (EnumFacing dir : EnumFacing.VALUES) {
            if (firstRun && workedOut && world.rand.nextBoolean()) {
                continue;
            }
            BlockPos adjacent = center.offset(dir);
            boolean place = firstRun ? canReplace(adjacent, world) : world.isAirBlock(adjacent);
            if (place) {
                world.setBlockState(adjacent, oreState);
                if (!firstRun) {
                    return;
                }
            }
        }
    }

    public static boolean canReplace(BlockPos replacePos, World world) {
        IBlockState replaceState = world.getBlockState(replacePos);
        return replaceState.getBlock().isReplaceableOreGen(replaceState, world, replacePos, BlockMatcher.forBlock(Blocks.STONE)) ||
          world.isAirBlock(replacePos);
    }

    private IBlockState getOreState(World world, BlockPos center) {
        return getOreState(world.getBlockState(center));
    }

    private IBlockState getOreState(IBlockState depositState) {
        return OreBlocks.Blocks.ORE_BLOCK.getDefaultState().withProperty(BlockGenericOre.VARIANT,
          depositState.getValue(VARIANT) == Types.COPPER ? OVERWORLD_COPPER : OVERWORLD_ZINC);
    }

    public enum Types implements IStringSerializable {
        COPPER,
        ZINC;

        public static Types[] LOOKUP = new Types[values().length];

        static {
            for (Types type : values()) {
                LOOKUP[type.ordinal()] = type;
            }
        }

        @Override
        public String getName() {
            return toString().toLowerCase();
        }
    }
}
