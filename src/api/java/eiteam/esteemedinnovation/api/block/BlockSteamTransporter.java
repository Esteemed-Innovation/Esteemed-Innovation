package eiteam.esteemedinnovation.api.block;

import eiteam.esteemedinnovation.api.ISteamTransporter;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockSteamTransporter extends BlockContainer {
    public BlockSteamTransporter(Material material) {
        super(material);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        ISteamTransporter te = (ISteamTransporter) world.getTileEntity(pos);
        if (te == null) {
            return;
        }
        if (te.getNetwork() != null) {
            te.getNetwork().split(te, true);
        }
        te.refresh();
    }

    /**
     * Since BlockSteamTransporter extends BlockContainer rather than Block, we should specify the render type to
     * MODEL, as that is what most BlockSteamTransporters will be using.
     */
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
}
