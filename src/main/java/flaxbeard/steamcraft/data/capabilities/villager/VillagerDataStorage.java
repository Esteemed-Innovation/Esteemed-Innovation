package flaxbeard.steamcraft.data.capabilities.villager;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class VillagerDataStorage implements Capability.IStorage<IVillagerData> {
    @Override
    public NBTBase writeNBT(Capability<IVillagerData> capability, IVillagerData instance, EnumFacing side) {
        Boolean lastHadCustomer = instance.hadCustomer();
        if (lastHadCustomer != null) {
            return new NBTTagByte((byte) (lastHadCustomer ? 1 : 0));
        }
        return null;
    }

    @Override
    public void readNBT(Capability<IVillagerData> capability, IVillagerData instance, EnumFacing side, NBTBase nbt) {
        instance.setHadCustomer(((NBTTagByte) nbt).getByte() == 1);
    }
}
