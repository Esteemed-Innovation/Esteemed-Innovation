package flaxbeard.steamcraft.block;

import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.block.BlockSteamTransporter;
import flaxbeard.steamcraft.tile.TileEntityFan;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockFan extends BlockSteamTransporter implements IWrenchable {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    private IIcon iconOn;
    private IIcon iconOff;

    public BlockFan() {
        super(Material.iron);
        setHardness(3.5F);
        setResistance(7.5F);
    }

    public static int determineOrientation(World world, int x, int y, int z, EntityLivingBase elb) {
        if (MathHelper.abs((float) elb.posX - (float) x) < 2.0F && MathHelper.abs((float) elb.posZ - (float) z) < 2.0F) {
            double d0 = elb.posY + 1.82D - (double) elb.yOffset;

            if (d0 - (double) y > 2.0D) {
                return 1;
            }

            if ((double) y - d0 > 0.0D) {
                return 0;
            }
        }

        int l = MathHelper.floor_double((double) (elb.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        return l == 0 ? 2 : (l == 1 ? 5 : (l == 2 ? 3 : (l == 3 ? 4 : 0)));
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        boolean flag = world.isBlockIndirectlyGettingPowered(x, y, z);
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if ((tileEntity != null && tileEntity instanceof TileEntityFan)) {
            TileEntityFan ped = (TileEntityFan) tileEntity;
            ped.updateRedstoneState(flag);
        }
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        onNeighborBlockChange(world, x, y, z, null);
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
        int meta = world.getBlockMetadata(i, j, k);
        ForgeDirection dir = ForgeDirection.getOrientation(meta);
        return AxisAlignedBB.getBoundingBox(i + (dir.offsetX < 0 ? 0.625F : 0), j + (dir.offsetY < 0 ? 0.625F : 0), k + (dir.offsetZ < 0 ? 0.625F : 0), i + 1.0F + (dir.offsetX > 0 ? -0.625F : 0), j + 1.0F + (dir.offsetY > 0 ? -0.625F : 0), k + 1.0F + (dir.offsetZ > 0 ? -0.625F : 0));
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1iBlockAccess, int par2, int par3, int par4) {
        int meta = par1iBlockAccess.getBlockMetadata(par2, par3, par4);
        ForgeDirection dir = ForgeDirection.getOrientation(meta);
        setBlockBounds((dir.offsetX < 0 ? 0.625F : 0), (dir.offsetY < 0 ? 0.625F : 0), (dir.offsetZ < 0 ? 0.625F : 0), 1.0F + (dir.offsetX > 0 ? -0.625F : 0), 1.0F + (dir.offsetY > 0 ? -0.625F : 0), 1.0F + (dir.offsetZ > 0 ? -0.625F : 0));
        super.setBlockBoundsBasedOnState(par1iBlockAccess, par2, par3, par4);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase elb, ItemStack stack) {
        int l = determineOrientation(world, x, y, z, elb);
        world.setBlockMetadataWithNotify(x, y, z, l, 2);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityFan();
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world,
                            int x, int y, int z, int side, float xO, float yO, float zO) {
        int meta = world.getBlockMetadata(x, y, z);
        if (player.isSneaking()) {
            return true;
        } else {
            world.setBlockMetadataWithNotify(x, y, z, side == meta ? ForgeDirection.getOrientation(side).getOpposite().ordinal() : side, 2);
            return true;
        }
    }
}
