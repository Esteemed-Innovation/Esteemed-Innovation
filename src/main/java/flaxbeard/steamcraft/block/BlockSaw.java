package flaxbeard.steamcraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.block.BlockSteamTransporter;
import flaxbeard.steamcraft.tile.TileEntitySaw;
import flaxbeard.steamcraft.tile.TileEntitySmasher;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by elijahfoster-wysocki on 10/18/14.
 */
public class BlockSaw extends BlockSteamTransporter implements IWrenchable {
    @SideOnly(Side.CLIENT)
    public IIcon frontIcon;


    public BlockSaw() {
        super(Material.iron);

    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
        int l = MathHelper.floor_double((double) (entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (l == 0) {
            world.setBlockMetadataWithNotify(x, y, z, 2, 2);
        }

        if (l == 1) {
            world.setBlockMetadataWithNotify(x, y, z, 5, 2);
        }

        if (l == 2) {
            world.setBlockMetadataWithNotify(x, y, z, 3, 2);
        }

        if (l == 3) {
            world.setBlockMetadataWithNotify(x, y, z, 4, 2);
        }


    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (meta == 0) {
            meta = 3;
        }
        return this.blockIcon;
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        this.blockIcon = register.registerIcon("steamcraft:blockBrass");
    }


    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileEntitySaw();

    }

    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor) {
        TileEntitySaw saw = (TileEntitySaw) world.getTileEntity(x, y, z);
        saw.blockUpdate();
    }

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
            if (side != 0 && side != 1) {
                int output = meta;
                switch (side) {
                    case 3:
                        output = 3;
                        break;
                    case 2:
                        output = 2;
                        break;
                    case 5:
                        output = 5;
                        break;
                    case 4:
                        output = 4;
                        break;
                }
                if (output == meta && side > 1 && side < 6) {
                    switch (ForgeDirection.getOrientation(side).getOpposite().ordinal()) {
                        case 3:
                            output = 3;
                            break;
                        case 2:
                            output = 2;
                            break;
                        case 5:
                            output = 5;
                            break;
                        case 4:
                            output = 4;
                            break;
                    }
                }
                world.setBlockMetadataWithNotify(x, y, z, output, 2);
                return true;
            }
            return false;
        }
    }
}
