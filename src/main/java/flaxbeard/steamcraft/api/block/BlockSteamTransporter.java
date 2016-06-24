package flaxbeard.steamcraft.api.block;

import flaxbeard.steamcraft.api.ISteamTransporter;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockSteamTransporter extends BlockContainer {
    public BlockSteamTransporter(Material material) {
        super(material);
    }

    @Override
    public void onBlockPreDestroy(World world, BlockPos pos, IBlockState state) {
        ISteamTransporter te = (ISteamTransporter) world.getTileEntity(pos);
        if (te == null) {
            return;
        }
        if (te.getNetwork() != null) {
            te.getNetwork().split(te, true);
        }
        te.refresh();
    }
}
