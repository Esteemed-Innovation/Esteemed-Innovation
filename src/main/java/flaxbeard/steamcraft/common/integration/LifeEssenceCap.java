package flaxbeard.steamcraft.common.integration;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

public class LifeEssenceCap extends WorldSavedData {

    public int cap;

    public LifeEssenceCap(String par1Str) {
        super(par1Str);
        this.cap = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        this.cap = nbttagcompound.getInteger("cap");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setInteger("cap", this.cap);
    }

}
