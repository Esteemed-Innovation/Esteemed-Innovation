package eiteam.esteemedinnovation.modules.materials;

import net.minecraft.block.AbstractPressurePlateBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Predicate;

public class ClassSensitivePlateBlock<T extends Entity> extends AbstractPressurePlateBlock {
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	
	private Class<T> entityClass;
	private Predicate<T> predicate;
	
	public ClassSensitivePlateBlock(Block.Properties properties, Class<T> clazz) {
		this(properties, clazz, e -> true);
	}
	
	public ClassSensitivePlateBlock(Block.Properties properties, Class<T> clazz, Predicate<T> predicate) {
		super(properties);
		this.entityClass = clazz;
		this.predicate = predicate;
		this.setDefaultState(this.stateContainer.getBaseState().with(POWERED, false));
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(POWERED);
	}
	
	@Override
	protected void playClickOnSound(IWorld worldIn, BlockPos pos) {
		worldIn.playSound(null, pos, SoundEvents.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.90F);
	}
	
	@Override
	protected void playClickOffSound(IWorld worldIn, BlockPos pos) {
		worldIn.playSound(null, pos, SoundEvents.BLOCK_METAL_PRESSURE_PLATE_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.75F);
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
	protected int getRedstoneStrength(BlockState state) {
		return state.get(POWERED) ? 15 : 0;
	}
	
	@Override
	protected BlockState setRedstoneStrength(BlockState state, int strength) {
		return state.with(POWERED, strength > 0);
	}
}
