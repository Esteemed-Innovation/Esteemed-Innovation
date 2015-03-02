package flaxbeard.steamcraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.block.BlockSteamTransporter;
import flaxbeard.steamcraft.tile.TileEntitySteamFurnace;
import flaxbeard.steamcraft.tile.TileEntitySteamHeater;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockSteamHeater extends BlockSteamTransporter implements IWrenchable {
    private IIcon iconOn;
    private IIcon iconOff;

    public BlockSteamHeater() {
        super(Material.iron);
    }

    public static int determineOrientation(World world, int x, int y, int z, EntityLivingBase elb) {
        if (MathHelper.abs((float) elb.posX - (float) x) < 2.0F && MathHelper.abs((float) elb.posZ - (float) z) < 2.0F) {
            double d0 = elb.posY + 1.82D - (double) elb.yOffset;

            if (d0 - (double) y > 2.0D) {
                return 1;
            }

            if ((double) y- d0 > 0.0D) {
                return 0;
            }
        }

        int l = MathHelper.floor_double((double) (elb.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        return l == 0 ? 2 : (l == 1 ? 5 : (l == 2 ? 3 : (l == 3 ? 4 : 0)));
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        if (side == meta) {
            return iconOn;
        }
        return blockIcon;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int i, int j, int k) {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void registerBlockIcons(IIconRegister ir) {
        super.registerBlockIcons(ir);
        this.iconOn = ir.registerIcon("steamcraft:heaterOn");
        this.iconOff = ir.registerIcon("steamcraft:heaterOff");
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase elb, ItemStack stack) {
        int l = determineOrientation(world, x, y, z, elb);
        world.setBlockMetadataWithNotify(x, y, z, l, 2);
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileEntitySteamHeater();
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public int getRenderType() {
        return Steamcraft.heaterRenderID;
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world,
                            int x, int y, int z, int side, float xO, float yO, float zO) {
        int meta = world.getBlockMetadata(x, y, z);
        world.setBlockMetadataWithNotify(x, y, z, side == meta ? ForgeDirection.getOrientation(side).getOpposite().ordinal() : side, 2);
        return true;
    }

}
