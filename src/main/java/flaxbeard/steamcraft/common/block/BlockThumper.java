package flaxbeard.steamcraft.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.SteamcraftBlocks;
import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.block.BlockSteamTransporter;
import flaxbeard.steamcraft.common.tile.TileEntityThumper;
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
    public void onNeighborBlockChange(World world, int x, int y, int z, Block p_149695_5_) {
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
    public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_) {
        int l = MathHelper.floor_double((double) (p_149689_5_.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
        p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, l, 2);
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileEntityThumper();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        this.blockIcon = p_149651_1_.registerIcon("steamcraft:blankTexture");
    }

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
            }
            return true;
        }
        return false;
    }
}
