package flaxbeard.steamcraft.block;

import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.block.BlockSteamTransporter;
import flaxbeard.steamcraft.tile.TileEntityChargingPad;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockChargingPad extends BlockSteamTransporter implements IWrenchable {
    private IIcon[] top = new IIcon[4];

    public BlockChargingPad() {
        super(Material.iron);
        setHardness(3.5F);
    }

    @Override
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
    public void registerBlockIcons(IIconRegister register) {
        top[0] = register.registerIcon("steamcraft:feetIndent2");
        top[1] = register.registerIcon("steamcraft:feetIndent3");
        top[2] = register.registerIcon("steamcraft:feetIndent4");
        top[3] = register.registerIcon("steamcraft:feetIndent5");

        super.registerBlockIcons(register);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (side == 1 && meta > 1) {
            return this.top[meta - 2];
        }
        return this.blockIcon;
    }


    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityChargingPad();
        //return null;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world,
                            int x, int y, int z, int side, float xO, float yO, float zO) {
        int meta = world.getBlockMetadata(x, y, z);
        if (side != 0 && side != 1) {
            int output = meta;
            switch (side) {
                case 2:
                    output = 2;
                    break;
                case 3:
                    output = 0;
                    break;
                case 4:
                    output = 1;
                    break;
                case 5:
                    output = 3;
                    break;
            }
            if (output == meta && side > 1 && side < 6) {
                switch (ForgeDirection.getOrientation(side).getOpposite().ordinal()) {
                    case 2:
                        output = 2;
                        break;
                    case 3:
                        output = 0;
                        break;
                    case 4:
                        output = 1;
                        break;
                    case 5:
                        output = 3;
                        break;
                }
            }
            world.setBlockMetadataWithNotify(x, y, z, output, 2);
            return true;
        }
        return false;
    }

}
