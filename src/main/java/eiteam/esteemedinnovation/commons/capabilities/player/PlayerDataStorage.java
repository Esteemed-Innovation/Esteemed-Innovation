package eiteam.esteemedinnovation.commons.capabilities.player;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.tuple.Pair;

public class PlayerDataStorage implements Capability.IStorage<PlayerData> {
    @Override
    public NBTBase writeNBT(Capability<PlayerData> capability, PlayerData instance, EnumFacing side) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setBoolean("IsRangeExtended", instance.isRangeExtended());
        nbt.setInteger("TickCache", instance.getTickCache());
        Float step = instance.getPreviousStepHeight();
        if (step != null) {
            nbt.setFloat("PreviousStepHeight", step);
        }
        Pair<Double, Double> pair = instance.getLastMotions();
        if (pair != null) {
            nbt.setDouble("PreviousPositionX", pair.getLeft());
            nbt.setDouble("PreviousPositionZ", pair.getLeft());
        }
        NBTTagList unlockedPieces = new NBTTagList();
        for (String p : instance.getAllUnlockedPieces()) {
            unlockedPieces.appendTag(new NBTTagString(p));
        }
        nbt.setTag("UnlockedBookPieces", unlockedPieces);
        return nbt;
    }

    @Override
    public void readNBT(Capability<PlayerData> capability, PlayerData instance, EnumFacing side, NBTBase nbtBase) {
        NBTTagCompound nbt = (NBTTagCompound) nbtBase;
        if (nbt.hasKey("PreviousPositionX") && nbt.hasKey("PreviousPositionZ")) {
            instance.setLastMotions(Pair.of(nbt.getDouble("PreviousPositionX"), nbt.getDouble("PreviousPositionZ")));
        } else {
            instance.setLastMotions(null);
        }
        if (nbt.hasKey("PreviousStepHeight")) {
            instance.setPreviousStepHeight(nbt.getFloat("PreviousStepHeight"));
        } else {
            instance.setPreviousStepHeight(null);
        }
        instance.setTickCache(nbt.getInteger("TickCache"));
        instance.setRangeExtended(nbt.getBoolean("IsRangeExtended"));
        NBTTagList unlockedPieces = nbt.getTagList("UnlockedBookPieces", Constants.NBT.TAG_STRING);
        for (int i = 0; i < unlockedPieces.tagCount(); i++) {
            instance.setHasUnlockedBookPiece(unlockedPieces.getStringTagAt(i), true);
        }
    }
}
