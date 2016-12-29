package eiteam.esteemedinnovation.armor.tophat;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class VillagerDataStorage implements Capability.IStorage<VillagerData> {
    @Override
    public NBTBase writeNBT(Capability<VillagerData> capability, VillagerData instance, EnumFacing side) {
        Boolean lastHadCustomer = instance.hadCustomer();
        if (lastHadCustomer != null) {
            return new NBTTagByte((byte) (lastHadCustomer ? 1 : 0));
        }
        return null;
    }

    @Override
    public void readNBT(Capability<VillagerData> capability, VillagerData instance, EnumFacing side, NBTBase nbt) {
        instance.setHadCustomer(((NBTTagByte) nbt).getByte() == 1);
    }
}
