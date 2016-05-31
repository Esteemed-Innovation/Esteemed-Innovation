package flaxbeard.steamcraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.SteamcraftBlocks;
import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.block.BlockSteamTransporter;
import flaxbeard.steamcraft.tile.TileEntityThumper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockThumper extends BlockSteamTransporter implements IWrenchable {

    public BlockThumper() {
        super(Material.iron);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        if (!(world.getBlock(x, y + 1, z) == SteamcraftBlocks.thumperDummy)) {
            if (!world.isRemote) {
                this.dropBlockAsItem(world, x, y, z, 0, 0);
            }
            world.setBlockToAir(x, y, z);
        }
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int i, int j, int k) {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 4.0F, 1.0F);
    }


    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase elb, ItemStack stack) {
        int l = MathHelper.floor_double((double) (elb.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
        world.setBlockMetadataWithNotify(x, y, z, l, 2);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityThumper();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister ir) {
        this.blockIcon = ir.registerIcon("steamcraft:blankTexture");
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world,
                            int x, int y, int z, int side, float xO, float yO, float zO) {
        if (side != 0 && side != 1) {
            switch (side) {
                case 2:
                    world.setBlockMetadataWithNotify(x, y, z, 2, 2);
                    break;
                case 3:
                    world.setBlockMetadataWithNotify(x, y, z, 0, 2);
                    break;
                case 4:
                    world.setBlockMetadataWithNotify(x, y, z, 1, 2);
                    break;
                case 5:
                    world.setBlockMetadataWithNotify(x, y, z, 3, 2);
                    break;
                default: 
                    break;
            }
            return true;
        }
        return false;
    }
}
