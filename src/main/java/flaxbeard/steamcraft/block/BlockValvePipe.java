package flaxbeard.steamcraft.block;

import flaxbeard.steamcraft.tile.TileEntityValvePipe;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BlockValvePipe extends BlockPipe {
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
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase elb, ItemStack stack) {
        int l = determineOrientation(world, x, y, z, elb);
        world.setBlockMetadataWithNotify(x, y, z, l, 2);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if ((tileEntity != null && tileEntity instanceof TileEntityValvePipe) &&
          neighbor.canProvidePower()) {
            TileEntityValvePipe valve = (TileEntityValvePipe) tileEntity;
            boolean flag = !world.isBlockIndirectlyGettingPowered(x, y, z);
            valve.updateRedstoneState(flag);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityValvePipe();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        if (player.getHeldItem() == null || !(player.getHeldItem().getItem() instanceof ItemBlock)) {
            TileEntityValvePipe tile = (TileEntityValvePipe) world.getTileEntity(x, y, z);
            if (!tile.isTurning()) {
                tile.setTurning();
                //Steamcraft.log.debug(tile.getNetworkName());
            }
        }
        return true;
    }
}
