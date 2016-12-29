package eiteam.esteemedinnovation.api.block;

import eiteam.esteemedinnovation.api.SteamTransporter;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockSteamTransporter extends Block {
    public BlockSteamTransporter(Material material) {
        super(material);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        SteamTransporter te = (SteamTransporter) world.getTileEntity(pos);
        if (te == null) {
            return;
        }
        if (te.getNetwork() != null) {
            te.getNetwork().split(te, true);
        }
        te.refresh();
    }
}
