package eiteam.esteemedinnovation.armor.tophat;

import eiteam.esteemedinnovation.armor.ArmorModule;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class VillagerDataSerializer implements ICapabilitySerializable<NBTTagByte> {
    VillagerData instance = ArmorModule.VILLAGER_DATA.getDefaultInstance();

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == ArmorModule.VILLAGER_DATA;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return hasCapability(capability, facing) ? ArmorModule.VILLAGER_DATA.<T>cast(instance) : null;
    }

    @Override
    public NBTTagByte serializeNBT() {
        return (NBTTagByte) ArmorModule.VILLAGER_DATA.getStorage().writeNBT(ArmorModule.VILLAGER_DATA, instance, null);
    }

    @Override
    public void deserializeNBT(NBTTagByte nbt) {
        ArmorModule.VILLAGER_DATA.getStorage().readNBT(ArmorModule.VILLAGER_DATA, instance, null, nbt);
    }
}
