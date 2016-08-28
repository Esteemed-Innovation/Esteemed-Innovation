package eiteam.esteemedinnovation.data.capabilities.player;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import eiteam.esteemedinnovation.EsteemedInnovation;

public class PlayerDataSerializer implements ICapabilitySerializable<NBTTagCompound> {
    IPlayerData instance = EsteemedInnovation.PLAYER_DATA.getDefaultInstance();

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == EsteemedInnovation.PLAYER_DATA;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return hasCapability(capability, facing) ? EsteemedInnovation.PLAYER_DATA.<T>cast(instance) : null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return (NBTTagCompound) EsteemedInnovation.PLAYER_DATA.getStorage().writeNBT(EsteemedInnovation.PLAYER_DATA, instance, null);
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        EsteemedInnovation.PLAYER_DATA.getStorage().readNBT(EsteemedInnovation.PLAYER_DATA, instance, null, nbt);
    }
}
