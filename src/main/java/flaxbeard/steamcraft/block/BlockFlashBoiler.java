package flaxbeard.steamcraft.block;

import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.block.BlockSteamTransporter;
import flaxbeard.steamcraft.misc.FluidHelper;
import flaxbeard.steamcraft.tile.TileEntityFlashBoiler;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

import javax.annotation.Nullable;

// Notes:
// ATM meta 1 has a special texture, because that's going to be the master TE for the multi.
// meta 1 is always the one with the lowest x, y, and z.

// meta 0  : not in multiblock
// meta 1-8: in multiblock, signifies which corner


public class BlockFlashBoiler extends BlockSteamTransporter {
    public static final PropertyEnum<Corners> CORNER = PropertyEnum.create("corner", Corners.class);

    public BlockFlashBoiler() {
        super(Material.IRON);
        setHardness(5F);
        setResistance(10F);
        setDefaultState(blockState.getBaseState().withProperty(CORNER, Corners.NONE));
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityFlashBoiler();
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, CORNER);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(CORNER).ordinal();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(CORNER, Corners.LOOKUP[meta]);
    }

    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        super.breakBlock(world, pos, state);
        TileEntityFlashBoiler boiler = (TileEntityFlashBoiler) world.getTileEntity(pos);

        if (boiler != null) {
            InventoryHelper.dropInventoryItems(world, pos, boiler);
            if (state.getValue(CORNER) != Corners.NONE) {
                boiler.destroyMultiblock();
            }
        }
    }

    private void checkMultiblock(World world, BlockPos pos, boolean isBreaking, int frontSide) {
        if (!world.isRemote) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile != null && tile instanceof TileEntityFlashBoiler) {
                TileEntityFlashBoiler boiler = (TileEntityFlashBoiler) tile;
                boiler.checkMultiblock(isBreaking, frontSide);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
    	if (state.getValue(CORNER) == Corners.NONE) {
    		return false;
    	}

    	TileEntityFlashBoiler tileEntity = (TileEntityFlashBoiler) world.getTileEntity(pos);

        if (tileEntity == null) {
            return false;
        }

        boolean isClient = !world.isRemote;
        
	    if (!FluidHelper.playerIsHoldingWaterContainer(player) && isClient) {
    		player.openGui(Steamcraft.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
        } else {
        	FluidHelper.fillTankFromHeldItem(player, tileEntity.getTank());
        }

        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        EnumFacing l = placer.getHorizontalFacing().getOpposite();
        int frontSide = -1;
        switch (l) {
            case DOWN: {
                frontSide = 2;
                break;
            }
            case UP: {
                frontSide = 5;
                break;
            }
            case NORTH: {
                frontSide = 3;
                break;
            }
            case SOUTH: {
                frontSide = 4;
                break;
            }
            default: {
                break;
            }
        }

        checkMultiblock(world, pos, false, frontSide);
    }

    private static final float FIFTYTWO = 0.52F;

    private void spawnParticles(World world, int front, float xOffset, double y, float zOffset, Random random) {
        float offset = getRandomFlameOffset(random);
        double x;
        double z;
        if (front == 4) {
            x = (double) (xOffset - FIFTYTWO);
            z = (double) (zOffset + offset);
        } else if (front == 5 || front == 3) {
            x = (double) (xOffset + FIFTYTWO);
            z = (double) (zOffset + offset);
        } else if (front == 2) {
            x = (double) (xOffset + offset);
            z = (double) (zOffset - FIFTYTWO);
        } else {
            return;
        }

        spawnParticles(world, x, y, z);
    }

    private void spawnParticles(World world, double x, double y, double z) {
        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 0.0D, 0.0D, 0.0D);
        world.spawnParticle(EnumParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random random) {
        Corners corner = state.getValue(CORNER);
        if (corner == Corners.NONE || corner.getDirection() == Corners.Direction.FRONT) {
            TileEntityFlashBoiler boiler = (TileEntityFlashBoiler) world.getTileEntity(pos);
            if (boiler == null) {
                return;
            }

            if (boiler.isBurning()) {
                int front = boiler.getFront();
                float xOffset = (float) pos.getX() + 0.5F;
                float yOffset = (float) pos.getY() + 0.25F + random.nextFloat() * 0.8F;
                float zOffset = (float) pos.getZ() + 0.5F;
                spawnParticles(world, front, xOffset, yOffset, zOffset, random);
                spawnParticles(world, front, xOffset, yOffset, zOffset, random);
                spawnParticles(world, front, xOffset, yOffset, zOffset, random);
                spawnParticles(world, front, xOffset, yOffset, zOffset, random);
            }
        }

    }

    private float getRandomFlameOffset(Random random) {
        return random.nextFloat() * 0.8F - 0.4F;
    }

    public enum Corners implements IStringSerializable {
        // This one is for when the flash boiler is not a fully constructed multiblock.
        NONE("none", null, null, null),
        // This one is arbitrarily the primary flash boiler block.
        TOP_LEFT_BACK("top_left_back", Vertical.TOP, Side.LEFT, Direction.BACK),
        TOP_LEFT_FRONT("top_left_front", Vertical.TOP, Side.LEFT, Direction.FRONT),
        TOP_RIGHT_BACK("top_right_back", Vertical.TOP, Side.RIGHT, Direction.BACK),
        TOP_RIGHT_FRONT("top_right_front", Vertical.TOP, Side.RIGHT, Direction.FRONT),
        BOTTOM_LEFT_BACK("bottom_left_back", Vertical.BOTTOM, Side.LEFT, Direction.BACK),
        BOTTOM_LEFT_FRONT("bottom_left_front", Vertical.BOTTOM, Side.LEFT, Direction.FRONT),
        BOTTOM_RIGHT_BACK("bottom_right_back", Vertical.BOTTOM, Side.RIGHT, Direction.BACK),
        BOTTOM_RIGHT_FRONT("bottom_right_front", Vertical.BOTTOM, Side.RIGHT, Direction.FRONT);

        public static Corners[] LOOKUP = new Corners[values().length];

        static {
            for (Corners corner : values()) {
                LOOKUP[corner.ordinal()] = corner;
            }
        }

        private String name;
        private Vertical vertical;
        private Side side;
        private Direction direction;

        Corners(String name, Vertical vertical, Side side, Direction direction) {
            this.name = name;
            this.vertical = vertical;
            this.side = side;
            this.direction = direction;
        }

        public String getName() {
            return name;
        }

        @Nullable
        public Vertical getVertical() {
            return vertical;
        }

        @Nullable
        public Side getSide() {
            return side;
        }

        @Nullable
        public Direction getDirection() {
            return direction;
        }

        public enum Vertical {
            TOP,
            BOTTOM
        }

        public enum Side {
            LEFT,
            RIGHT
        }

        public enum Direction {
            FRONT,
            BACK
        }
    }
}
