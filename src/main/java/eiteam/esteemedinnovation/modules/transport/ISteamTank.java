package eiteam.esteemedinnovation.modules.transport;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

public interface ISteamTank extends IFluidTank {
    
    @Override
    default boolean isFluidValid(FluidStack stack) {
        return stack.getFluid().isEquivalentTo(TransportModule.STEAM_SOURCE.get());
    }
    
    default float getPressure() {
        return ((float)getFluidAmount()) / getCapacity();
    }
    
    float getMaxPressure();
    
    default boolean shouldExplode() {
        return getPressure() > getMaxPressure();
    }
}
