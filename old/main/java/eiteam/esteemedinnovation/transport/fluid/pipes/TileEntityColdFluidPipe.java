package eiteam.esteemedinnovation.transport.fluid.pipes;

import eiteam.esteemedinnovation.transport.TransportationModule;
import net.minecraft.block.state.IBlockState;

public class TileEntityColdFluidPipe extends TileEntityTemperatureFluidPipe {
    public TileEntityColdFluidPipe() {
        super(100, BlockColdFluidPipe.MIN_TEMPERATURE, BlockColdFluidPipe.MAX_TEMPERATURE);
    }

    @Override
    public boolean canUpdate(IBlockState target) {
        return target.getBlock() == TransportationModule.COPPER_PIPE;
    }
}
