package flaxbeard.steamcraft.block;

import flaxbeard.steamcraft.SteamcraftBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDummy extends BlockContainer {

    public BlockDummy() {
        super(Material.iron);
        setHardness(50.0F);
        setLightOpacity(0);
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon("steamcraft:blockBrass");
    }

    @Override
    public boolean onBlockActivated(World world, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata){
        return new TileEntityDummyBlock();
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor) {
        int smasherCount = 0;
        smasherCount += world.getBlock(x + 1, y, z) == SteamcraftBlocks.smasher ? 1 : 0;
        smasherCount += world.getBlock(x - 1, y, z) == SteamcraftBlocks.smasher ? 1 : 0;
        smasherCount += world.getBlock(x, y, z + 1) == SteamcraftBlocks.smasher ? 1 : 0;
        smasherCount += world.getBlock(x, y, z - 1) == SteamcraftBlocks.smasher ? 1 : 0;

        if (smasherCount < 2) {
            world.setBlockToAir(x, y, z);
        }

    }

    @Override
    public boolean isAir(IBlockAccess world, int x, int y, int z){
        return true;
    }

    @Override
    public boolean canHarvestBlock(EntityPlayer player, int meta){
        return false;
    }


}
