package flaxbeard.steamcraft.data.capabilities.player;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import flaxbeard.steamcraft.Steamcraft;

public class PlayerDataSerializer implements ICapabilitySerializable<NBTTagCompound> {
    IPlayerData instance = Steamcraft.PLAYER_DATA.getDefaultInstance();

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == Steamcraft.PLAYER_DATA;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return hasCapability(capability, facing) ? Steamcraft.PLAYER_DATA.<T>cast(instance) : null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return (NBTTagCompound) Steamcraft.PLAYER_DATA.getStorage().writeNBT(Steamcraft.PLAYER_DATA, instance, null);
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        Steamcraft.PLAYER_DATA.getStorage().readNBT(Steamcraft.PLAYER_DATA, instance, null, nbt);
    }
}
