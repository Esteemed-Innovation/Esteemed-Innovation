package flaxbeard.steamcraft.block;

import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.util.UtilMisc;
import flaxbeard.steamcraft.item.BlockRuptureDiscItem;
import flaxbeard.steamcraft.tile.TileEntityRuptureDisc;
import flaxbeard.steamcraft.tile.TileEntitySteamPipe;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

import java.util.Iterator;

import javax.annotation.Nullable;

/**
 * The Rupture Disc metadata is slightly complicated.
 * The DIRECTION property is a kinda simplified variant of the EnumFacing horizontal plane.
 * They add their ordinal to the meta.
 * The IS_BURST property has 2 values: True (10), False (0)
 * These two are added together to get the final meta value, 0-6 (closed), and 10-16 (burst).
 *
 * However, the Rupture Disc item only has 2 metadata values: Burst (1), and Closed (0). So when doing things relating
 * to dropping, we have to properly subtract 10 when appropriate.
 */
public class BlockRuptureDisc extends BlockContainer {
    public static final PropertyBool IS_BURST = PropertyBool.create("isBurst");
    public static final PropertyEnum<RuptureDiscDirection> DIRECTION = PropertyEnum.create("direction", RuptureDiscDirection.class);

    public BlockRuptureDisc() {
        super(Material.IRON);
        setHardness(1F);

        for (BlockRuptureDiscItem.RuptureStates state : BlockRuptureDiscItem.RuptureStates.values()) {
            String modelName = "rupture_disc" + state.getName();
            ModelResourceLocation loc = new ModelResourceLocation(Steamcraft.MOD_ID + ":" + modelName, "inventory");
            int meta = state.getMetadata();
            Item item = Item.getItemFromBlock(this);
            if (item != null) {
                ModelLoader.setCustomModelResourceLocation(item, meta, loc);
            }
        }
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, IS_BURST, DIRECTION);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(IS_BURST, meta >= 10)
          .withProperty(DIRECTION, RuptureDiscDirection.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int a = state.getValue(IS_BURST) ? 10 : 0;
        RuptureDiscDirection dir = state.getValue(DIRECTION);
        return a + dir.ordinal();
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing dir) {
        TileEntity tile = null;
        switch (dir) {
            case NORTH: {
                tile = world.getTileEntity(new BlockPos(pos.getX(), pos.getY(), pos.getZ() + 1));
                break;
            }
            case SOUTH: {
                tile = world.getTileEntity(new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 1));
                break;
            }
            case WEST: {
                tile = world.getTileEntity(new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ()));
                break;
            }
            case EAST: {
                tile = world.getTileEntity(new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ()));
            }
        }

        if (tile != null && tile instanceof ISteamTransporter) {
            ISteamTransporter trans = (ISteamTransporter) tile;
            return trans.acceptsGauge(dir.getOpposite());
        }
        return false;
    }

    @Override
    public void onNeighborChange(IBlockAccess iba, BlockPos pos, BlockPos neighbor) {
        TileEntity tileEntity = iba.getTileEntity(pos);
        if (neighbor != pos && tileEntity != null) {
            IBlockState state = iba.getBlockState(pos);
            int meta = getMetaFromState(state);
            World world = tileEntity.getWorld();
            if (!canPlaceBlockOnSide(world, pos, RuptureDiscDirection.byMetadata(meta).getEnumFacing())) {
                dropBlockAsItem(world, pos, state, 0);
                world.setBlockToAir(pos);
            }
        }
    }

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getStateFromMeta(meta == 1 ? side.ordinal() + 10 : side.ordinal());
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityRuptureDisc();
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos) {
        return null;
    }

    @Override
    public int damageDropped(IBlockState state) {
        int blockMeta = getMetaFromState(state);
        return blockMeta >= 10 ? blockMeta - 10 : blockMeta;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (state.getValue(IS_BURST) && heldItem != null && UtilMisc.doesMatch(heldItem, "plateSteamcraftZinc")) {
            world.setBlockState(pos, state.withProperty(IS_BURST, false));
            if (!player.capabilities.isCreativeMode) {
                heldItem.stackSize -= 1;
            }
            return true;
        }
        return false;
    }

    private enum RuptureDiscDirection implements IStringSerializable {
        NORTH(EnumFacing.NORTH),
        SOUTH(EnumFacing.SOUTH),
        EAST(EnumFacing.EAST),
        WEST(EnumFacing.WEST);

        private EnumFacing enumFacing;

        RuptureDiscDirection(EnumFacing enumFacing) {
            this.enumFacing = enumFacing;
        }

        public String getName() {
            return getEnumFacing().getName();
        }

        public EnumFacing getEnumFacing() {
            return enumFacing;
        }

        public static RuptureDiscDirection byMetadata(int metadata) {
            return values()[metadata >= 10 ? metadata - 10 : metadata];
        }
    }
}
