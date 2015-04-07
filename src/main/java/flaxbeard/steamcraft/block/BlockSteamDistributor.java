package flaxbeard.steamcraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.api.block.BlockSteamTransporter;
import flaxbeard.steamcraft.tile.TileEntitySteamDistributor;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

/**
 * @author SatanicSanta
 */
public class BlockSteamDistributor extends BlockSteamTransporter {

    public BlockSteamDistributor() {
        super(Material.iron);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return this.blockIcon;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntitySteamDistributor();
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor) {
        TileEntitySteamDistributor distributor = (TileEntitySteamDistributor) world.getTileEntity(x, y, z);
        if (neighbor == world.getBlock(x, y + 1, z)) {
            distributor.updateEntity();
        }
    }
}