package eiteam.esteemedinnovation.commons.capabilities.player;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import org.apache.commons.lang3.tuple.MutablePair;

public class PlayerDataStorage implements Capability.IStorage<IPlayerData> {
    @Override
    public NBTBase writeNBT(Capability<IPlayerData> capability, IPlayerData instance, EnumFacing side) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setBoolean("isRangeExtended", instance.isRangeExtended());
        nbt.setInteger("tickCache", instance.getTickCache());
        Float step = instance.getPreviousStepHeight();
        if (step != null) {
            nbt.setFloat("prevStep", step);
        }
        MutablePair<Double, Double> pair = instance.getLastMotions();
        if (pair != null) {
            nbt.setDouble("lastMotionX", pair.left);
            nbt.setDouble("lastMotionZ", pair.right);
        }
        return nbt;
    }

    @Override
    public void readNBT(Capability<IPlayerData> capability, IPlayerData instance, EnumFacing side, NBTBase nbtBase) {
        NBTTagCompound nbt = (NBTTagCompound) nbtBase;
        if (nbt.hasKey("lastMotionX") && nbt.hasKey("lastMotionZ")) {
            instance.setLastMotions(MutablePair.of(nbt.getDouble("lastMotionX"), nbt.getDouble("lastMotionZ")));
        } else {
            instance.setLastMotions(null);
        }
        if (nbt.hasKey("prevStep")) {
            instance.setPreviousStepHeight(nbt.getFloat("prevStep"));
        } else {
            instance.setPreviousStepHeight(null);
        }
        instance.setTickCache(nbt.getInteger("tickCache"));
        instance.setRangeExtended(nbt.getBoolean("isRangeExtended"));
    }
}
