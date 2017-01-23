package eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.frequency;

import eiteam.esteemedinnovation.armor.ArmorModule;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class AnimalDataSerializer implements ICapabilitySerializable<NBTTagCompound> {
    private AnimalData instance;

    public AnimalDataSerializer() {
        this(ArmorModule.ANIMAL_DATA.getDefaultInstance());
    }

    public AnimalDataSerializer(AnimalData data) {
        instance = data;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == ArmorModule.ANIMAL_DATA;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return hasCapability(capability, facing) ? ArmorModule.ANIMAL_DATA.cast(instance) : null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return (NBTTagCompound) ArmorModule.ANIMAL_DATA.getStorage().writeNBT(ArmorModule.ANIMAL_DATA, instance, null);
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        ArmorModule.ANIMAL_DATA.getStorage().readNBT(ArmorModule.ANIMAL_DATA, instance, null, nbt);
    }
}
