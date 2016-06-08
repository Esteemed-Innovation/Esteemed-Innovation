package flaxbeard.steamcraft.data.capabilities.animal;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import flaxbeard.steamcraft.Steamcraft;

@SuppressWarnings("ConstantConditions")
public class AnimalDataSerializer implements ICapabilitySerializable<NBTTagCompound> {
    IAnimalData instance = Steamcraft.ANIMAL_DATA.getDefaultInstance();

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == Steamcraft.ANIMAL_DATA;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return hasCapability(capability, facing) ? Steamcraft.ANIMAL_DATA.<T>cast(instance) : null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return (NBTTagCompound) Steamcraft.ANIMAL_DATA.getStorage().writeNBT(Steamcraft.ANIMAL_DATA, instance, null);
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        Steamcraft.ANIMAL_DATA.getStorage().readNBT(Steamcraft.ANIMAL_DATA, instance, null, nbt);
    }
}
