package eiteam.esteemedinnovation.block.pressureplate;

import eiteam.esteemedinnovation.init.items.MetalItems;
import net.minecraft.block.BlockBasePressurePlate;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class BlockClassSensitivePlate<T extends Entity> extends BlockBasePressurePlate {
    private static final IProperty<Boolean> POWERED = PropertyBool.create("powered");

    private Class<T> entityClass;
    private int plateMeta;
    private Predicate<T> predicate;

    public BlockClassSensitivePlate(int plateMeta, Class<T> entityClass) {
        this(plateMeta, entityClass, e -> true);
    }

    public BlockClassSensitivePlate(int plateMeta, Class<T> entityClass, Predicate<T> predicate) {
        super(Material.IRON);
        setHardness(0.5F);
        setSoundType(SoundType.WOOD);
        setDefaultState(blockState.getBaseState().withProperty(POWERED, false));
        this.entityClass = entityClass;
        this.plateMeta = plateMeta;
        this.predicate = predicate;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(POWERED, meta == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(POWERED) ? 1 : 0;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, POWERED);
    }

    @Override
    protected void playClickOnSound(World world, BlockPos pos) {
        world.playSound(null, pos, SoundEvents.BLOCK_METAL_PRESSPLATE_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.90000004F);
    }

    @Override
    protected void playClickOffSound(World world, BlockPos pos) {
        world.playSound(null, pos, SoundEvents.BLOCK_METAL_PRESSPLATE_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.75F);
    }

    @Override
    protected int computeRedstoneStrength(World world, BlockPos pos) {
        List<T> entities = world.getEntitiesWithinAABB(entityClass, PRESSURE_AABB.offset(pos));

        for (T entity : entities) {
            if (!entity.doesEntityNotTriggerPressurePlate() && predicate.test(entity)) {
                return 15;
            }
        }

        return 0;
    }

    @Override
    protected int getRedstoneStrength(IBlockState state) {
        return state.getValue(POWERED) ? 15 : 0;
    }

    @Override
    protected IBlockState setRedstoneStrength(IBlockState state, int strength) {
        return state.withProperty(POWERED, strength > 0);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return MetalItems.Items.Types.PLATE.getItem();
    }

    @Override
    public int damageDropped(IBlockState state) {
        return plateMeta;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(MetalItems.Items.Types.PLATE.getItem(), 1, damageDropped(state));
    }
}
