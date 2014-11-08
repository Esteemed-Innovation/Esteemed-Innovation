package flaxbeard.steamcraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.UtilMisc;
import flaxbeard.steamcraft.tile.TileEntityRuptureDisc;
import flaxbeard.steamcraft.tile.TileEntitySteamPipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import static net.minecraftforge.common.util.ForgeDirection.*;

public class BlockRuptureDisc extends BlockContainer {
    public IIcon front;
    public IIcon back;
    public IIcon top;
    public IIcon top2;
    public IIcon backR;

    public BlockRuptureDisc() {
        super(Material.iron);
    }

    public static int getMeta(int input) {
        if (input > 9) {
            input = input - 10;
        }
        return input;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int xl, int yl, int zl) {
        int meta = world.getBlockMetadata(xl, yl, zl);
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
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        int trueMeta = meta;
        meta = getMeta(meta);
        return side == meta || side == ForgeDirection.OPPOSITES[meta] ? (side == meta ? (trueMeta > 9 ? backR : back) : front) : ((meta == 5 || meta == 4) ? blockIcon : top2);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister ir) {
        this.blockIcon = ir.registerIcon("steamcraft:discTop");
        this.back = ir.registerIcon("steamcraft:discFromt");
        this.backR = ir.registerIcon("steamcraft:discFromtRuptured");
        this.front = ir.registerIcon("steamcraft:discBack");
        this.top = blockIcon;
        this.top2 = ir.registerIcon("steamcraft:discTop2");

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
    public int damageDropped(int meta) {
        return (meta > 9 ? 1 : 0);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        TileEntityRuptureDisc tile = (TileEntityRuptureDisc) world.getTileEntity(x, y, z);
        if (world.getBlockMetadata(x, y, z) > 9) {
            if (player.getHeldItem() != null) {
                if (UtilMisc.doesMatch(player.getHeldItem(), "plateSteamcraftZinc")) {
                    world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) - 10, 2);
                    if (!player.capabilities.isCreativeMode) {
                        player.getCurrentEquippedItem().stackSize -= 1;
                    }
                    return true;
                }
            }
        }
        return false;
    }

}
