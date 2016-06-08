package flaxbeard.steamcraft.api.block;

import flaxbeard.steamcraft.api.ISteamTransporter;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockSteamTransporter extends BlockContainer {
    public BlockSteamTransporter(Material material) {
        super(material);
    }

    public void onBlockPreDestroy(World world, BlockPos pos, int meta) {
        ISteamTransporter te = (ISteamTransporter) world.getTileEntity(pos);
        if (te != null && te.getNetwork() != null) {
            te.getNetwork().split(te, true);
        }
        te.refresh();
    }
}
