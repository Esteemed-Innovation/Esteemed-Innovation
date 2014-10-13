package flaxbeard.steamcraft.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

import java.util.ArrayList;

public class AetherBlockData extends WorldSavedData {

    private static final String ID = "AetherBlockData";
    public ArrayList<ChunkCoordinates> cc = new ArrayList<ChunkCoordinates>();

    public AetherBlockData() {
        super(ID);
    }

    public static AetherBlockData get(World world) {
        AetherBlockData data = (AetherBlockData) world.loadItemData(AetherBlockData.class, ID);
        if (data == null) {
            //Steamcraft.log.debug("NEED NEW DATA");
            data = new AetherBlockData();
            world.setItemData(ID, data);
        }
        return data;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        //Steamcraft.log.debug("NEED OLD DATA");

        NBTTagList nbtl = (NBTTagList) nbt.getTag("ccs");
        for (int i = 0; i < nbtl.tagCount(); ++i) {
            NBTTagCompound nbt2 = (NBTTagCompound) nbtl.getCompoundTagAt(i);
            ChunkCoordinates c = new ChunkCoordinates(nbt2.getInteger("x"), nbt2.getInteger("y"), nbt2.getInteger("z"));
            cc.add(c);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        NBTTagList nbtl = new NBTTagList();
        for (ChunkCoordinates c : cc) {
            NBTTagCompound nbt2 = new NBTTagCompound();
            nbt2.setInteger("x", c.posX);
            nbt2.setInteger("y", c.posY);
            nbt2.setInteger("z", c.posZ);
            nbtl.appendTag(nbt2);
        }
        nbt.setTag("ccs", nbtl);
    }

    public void addCoord(ChunkCoordinates c) {
        cc.add(c);
    }

    public void removeCoord(ChunkCoordinates c) {
        cc.remove(c);
    }
}