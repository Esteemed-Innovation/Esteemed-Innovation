package eiteam.esteemedinnovation.transport.fluid.pipes;

public class TileEntityColdFluidPipe extends TileEntityTemperatureFluidPipe {
    public TileEntityColdFluidPipe() {
        super(100, BlockColdFluidPipe.MIN_TEMPERATURE, BlockColdFluidPipe.MAX_TEMPERATURE);
    }
}
