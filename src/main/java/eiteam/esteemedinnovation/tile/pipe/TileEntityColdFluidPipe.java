package eiteam.esteemedinnovation.tile.pipe;

import eiteam.esteemedinnovation.block.pipe.BlockColdFluidPipe;

public class TileEntityColdFluidPipe extends TileEntityTemperatureFluidPipe {
    public TileEntityColdFluidPipe() {
        super(100, BlockColdFluidPipe.MIN_TEMPERATURE, BlockColdFluidPipe.MAX_TEMPERATURE);
    }
}
