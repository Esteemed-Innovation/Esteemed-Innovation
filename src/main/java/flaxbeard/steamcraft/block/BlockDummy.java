package flaxbeard.steamcraft.block;

import flaxbeard.steamcraft.SteamcraftBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockDummy extends BlockContainer {

    public BlockDummy() {
        super(Material.iron);
        setHardness(50.0F);
        setLightOpacity(0);
    }

    public int getRenderType() {
        return -1;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
        return true;

    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {

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


}
