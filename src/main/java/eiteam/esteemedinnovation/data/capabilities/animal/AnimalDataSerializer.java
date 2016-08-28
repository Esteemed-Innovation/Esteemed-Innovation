package eiteam.esteemedinnovation.data.capabilities.animal;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import eiteam.esteemedinnovation.EsteemedInnovation;

public class AnimalDataSerializer implements ICapabilitySerializable<NBTTagCompound> {
    IAnimalData instance = EsteemedInnovation.ANIMAL_DATA.getDefaultInstance();

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == EsteemedInnovation.ANIMAL_DATA;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return hasCapability(capability, facing) ? EsteemedInnovation.ANIMAL_DATA.<T>cast(instance) : null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return (NBTTagCompound) EsteemedInnovation.ANIMAL_DATA.getStorage().writeNBT(EsteemedInnovation.ANIMAL_DATA, instance, null);
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        EsteemedInnovation.ANIMAL_DATA.getStorage().readNBT(EsteemedInnovation.ANIMAL_DATA, instance, null, nbt);
    }
}
