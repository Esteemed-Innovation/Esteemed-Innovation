package flaxbeard.steamcraft.data.capabilities.villager;

import net.minecraft.nbt.NBTTagByte;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import flaxbeard.steamcraft.Steamcraft;

public class VillagerDataSerializer implements ICapabilitySerializable<NBTTagByte> {
    IVillagerData instance = Steamcraft.VILLAGER_DATA.getDefaultInstance();

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == Steamcraft.VILLAGER_DATA;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return hasCapability(capability, facing) ? Steamcraft.VILLAGER_DATA.<T>cast(instance) : null;
    }

    @Override
    public NBTTagByte serializeNBT() {
        return (NBTTagByte) Steamcraft.VILLAGER_DATA.getStorage().writeNBT(Steamcraft.VILLAGER_DATA, instance, null);
    }

    @Override
    public void deserializeNBT(NBTTagByte nbt) {
        Steamcraft.VILLAGER_DATA.getStorage().readNBT(Steamcraft.VILLAGER_DATA, instance, null, nbt);
    }
}
