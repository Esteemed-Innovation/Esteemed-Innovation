package eiteam.esteemedinnovation.armor.tophat;

import net.minecraft.nbt.NBTTagByte;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import eiteam.esteemedinnovation.commons.EsteemedInnovation;

public class VillagerDataSerializer implements ICapabilitySerializable<NBTTagByte> {
    IVillagerData instance = EsteemedInnovation.VILLAGER_DATA.getDefaultInstance();

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == EsteemedInnovation.VILLAGER_DATA;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return hasCapability(capability, facing) ? EsteemedInnovation.VILLAGER_DATA.<T>cast(instance) : null;
    }

    @Override
    public NBTTagByte serializeNBT() {
        return (NBTTagByte) EsteemedInnovation.VILLAGER_DATA.getStorage().writeNBT(EsteemedInnovation.VILLAGER_DATA, instance, null);
    }

    @Override
    public void deserializeNBT(NBTTagByte nbt) {
        EsteemedInnovation.VILLAGER_DATA.getStorage().readNBT(EsteemedInnovation.VILLAGER_DATA, instance, null, nbt);
    }
}
