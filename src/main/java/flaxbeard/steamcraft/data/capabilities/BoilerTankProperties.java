package flaxbeard.steamcraft.data.capabilities;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;

public class BoilerTankProperties implements IFluidTankProperties {
    @Nullable
    @Override
    public FluidStack getContents() {
        return null;
    }

    @Override
    public int getCapacity() {
        return 0;
    }

    @Override
    public boolean canFill() {
        FluidStack contents = getContents();
        return contents != null && canFillFluidType(contents);
    }

    @Override
    public boolean canDrain() {
        return false;
    }

    @Override
    public boolean canFillFluidType(FluidStack fluidStack) {
        return fluidStack.getFluid() == FluidRegistry.WATER;
    }

    @Override
    public boolean canDrainFluidType(FluidStack fluidStack) {
        return false;
    }
}
