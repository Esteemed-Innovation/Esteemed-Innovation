package flaxbeard.steamcraft.block;

import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.util.UtilMisc;
import flaxbeard.steamcraft.item.BlockRuptureDiscItem;
import flaxbeard.steamcraft.tile.TileEntityRuptureDisc;
import flaxbeard.steamcraft.tile.TileEntitySteamPipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class BlockRuptureDisc extends BlockContainer {
    public static final PropertyBool IS_BURST = PropertyBool.create("isBurst");

    public BlockRuptureDisc() {
        super(Material.IRON);
        setHardness(1F);

        for (BlockRuptureDiscItem.RuptureStates state : BlockRuptureDiscItem.RuptureStates.values()) {
            String modelName = "rupture_disc" + state.getName();
            ModelResourceLocation loc = new ModelResourceLocation(Steamcraft.MOD_ID + ":" + modelName, "inventory");
            int meta = state.getMetadata();
            ModelLoader.setCustomModelResourceLocation(this, meta, loc);
        }
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, IS_BURST);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(IS_BURST, false);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(IS_BURST) ? 0 : 1;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int xl, int yl, int zl) {
        meta = getMeta(meta);
        float px = 1.0F / 16.0F;
        float x = 4 * px;
        float y = 4 * px;
        float z = 0.0F;
        float x2 = 12 * px;
        float y2 = 12 * px;
        float z2 = 3 * px;
        ForgeDirection dir = ForgeDirection.getOrientation(meta).getOpposite();
        if (world.getTileEntity(xl + dir.offsetX, yl + dir.offsetY, zl + dir.offsetZ) != null && world.getTileEntity(xl + dir.offsetX, yl + dir.offsetY, zl + dir.offsetZ) instanceof TileEntitySteamPipe) {
            z = -5 * px;
            z2 = -2 * px + 0.0005F;
        }
        switch (meta) {
            case 5:
                this.setBlockBounds(z, y, x, z2, y2, x2);
                break;
            case 2:
                this.setBlockBounds(1 - x2, y, 1 - z2, 1 - x, y2, 1 - z);
                break;
            case 4:
                this.setBlockBounds(1 - z2, y, 1 - x2, 1 - z, y2, 1 - x);
                break;
            case 3:
                this.setBlockBounds(x, y, z, x2, y2, z2);
                break;
        }
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side) {
        ForgeDirection dir = ForgeDirection.getOrientation(side);
        return (dir == NORTH && world.getTileEntity(x, y, z + 1) != null && world.getTileEntity(x, y, z + 1) instanceof ISteamTransporter && ((ISteamTransporter) world.getTileEntity(x, y, z + 1)).acceptsGauge(dir.getOpposite())) ||
                (dir == SOUTH && world.getTileEntity(x, y, z - 1) != null && world.getTileEntity(x, y, z - 1) instanceof ISteamTransporter && ((ISteamTransporter) world.getTileEntity(x, y, z - 1)).acceptsGauge(dir.getOpposite())) ||
                (dir == WEST && world.getTileEntity(x + 1, y, z) != null && world.getTileEntity(x + 1, y, z) instanceof ISteamTransporter && ((ISteamTransporter) world.getTileEntity(x + 1, y, z)).acceptsGauge(dir.getOpposite())) ||
                (dir == EAST && world.getTileEntity(x - 1, y, z) != null && world.getTileEntity(x - 1, y, z) instanceof ISteamTransporter && ((ISteamTransporter) world.getTileEntity(x - 1, y, z)).acceptsGauge(dir.getOpposite()));
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor) {
        if (neighbor != this) {
            int l = world.getBlockMetadata(x, y, z);
            l = getMeta(l);
            boolean flag = false;
            if (!this.canPlaceBlockOnSide(world, x, y, z, l)) {
                flag = true;
            }

            if (flag) {
                this.dropBlockAsItem(world, x, y, z, l, 0);
                world.setBlockToAir(x, y, z);
            }
        }
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float p_149660_6_, float p_149660_7_, float p_149660_8_, int meta) {
        return (meta == 1 ? side + 10 : side);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityRuptureDisc();
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return Steamcraft.ruptureDiscRenderID;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
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
}
