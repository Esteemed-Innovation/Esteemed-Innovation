package eiteam.esteemedinnovation.transport.fluid.pipes;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nonnull;

public class TemperatureFluidTank extends FluidTank {
    public int minTemp;
    public int maxTemp;

    public TemperatureFluidTank(int minTemp, int maxTemp) {
        super(Fluid.BUCKET_VOLUME);

        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
    }

    @Override
    public boolean canFillFluidType(FluidStack fluid) {
        return canFill() && canInteract(fluid);
    }

    @Override
    public boolean canDrainFluidType(FluidStack fluid) {
        return canDrain() && canInteract(fluid);
    }

    public boolean isFluidWithinTempRange(@Nonnull FluidStack fluid) {
        int temp = fluid.getFluid().getTemperature();
        return temp <= maxTemp && temp >= minTemp;
    }

    public boolean canInteract(FluidStack fluid) {
        if (fluid == null) {
            return false;
        }
        if (this.fluid == null) {
            return isFluidWithinTempRange(fluid);
        } else {
            return isFluidWithinTempRange(fluid) && this.fluid.getFluid() == fluid.getFluid();
        }
    }
}
