package flaxbeard.steamcraft.block;

import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.block.BlockSteamTransporter;
import flaxbeard.steamcraft.tile.TileEntityVacuum;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BlockVacuum extends BlockSteamTransporter implements IWrenchable {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    private IIcon iconOn;
    private IIcon iconOff;

    public BlockVacuum() {
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
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor) {
        boolean flag = world.isBlockIndirectlyGettingPowered(x, y, z);
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if ((tileEntity != null && tileEntity instanceof TileEntityVacuum)) {
            TileEntityVacuum tileentityvacuum = (TileEntityVacuum) tileEntity;
            tileentityvacuum.updateRedstoneState(flag);
        }
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        onNeighborBlockChange(world, x, y, z, null);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase elb, ItemStack stack) {
        int l = determineOrientation(world, x, y, z, elb);
        world.setBlockMetadataWithNotify(x, y, z, l, 2);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityVacuum();
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

    @Override
    public boolean renderAsNormalBlock(){
        return false;
    }
}
