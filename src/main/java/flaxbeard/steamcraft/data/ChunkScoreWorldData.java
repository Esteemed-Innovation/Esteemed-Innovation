package flaxbeard.steamcraft.data;
import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class ChunkScoreWorldData extends WorldSavedData {

    public HashMap<ChunkCoordinates,Integer> cc = new HashMap<ChunkCoordinates,Integer>();
    private static final String ID = "ChunkScoreWorldData";
    
    public ChunkScoreWorldData() {
        super(ID);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
    	System.out.println("NEED OLD DATA");

    	NBTTagList nbtl =  (NBTTagList) nbt.getTag("ccs");
    	for (int i = 0; i < nbtl.tagCount(); ++i)
    	{
    		NBTTagCompound nbt2 = (NBTTagCompound)nbtl.getCompoundTagAt(i);
    		ChunkCoordinates c = new ChunkCoordinates(nbt2.getInteger("x"),nbt2.getInteger("y"),nbt2.getInteger("z"));
    		int score = nbt2.getInteger("score");
    		cc.put(c,score);
    	}
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
		NBTTagList nbtl = new NBTTagList();
		for (ChunkCoordinates c : cc.keySet()) {
			NBTTagCompound nbt2 = new NBTTagCompound();
			nbt2.setInteger("x", c.posX);
			nbt2.setInteger("y", c.posY);
			nbt2.setInteger("z", c.posZ);
			nbt2.setInteger("score", cc.get(c));
			nbtl.appendTag(nbt2);
		}
		nbt.setTag("ccs", nbtl);
    }
    
    public void up(ChunkScoreWorldData data, ChunkCoordinates c) {
    	int score = 0;
    	if (cc.containsKey(c)) {
    		score = cc.get(c);
    	}
    	score++;
    	cc.put(c, score);
    	this.markDirty();

    }
    
    public void down(ChunkScoreWorldData data, ChunkCoordinates c) {
    	int score = 0;
    	if (cc.containsKey(c)) {
    		score = cc.get(c);
    	}
    	score--;
    	if (score < 0) {
    		score = 0;
    	}
    	cc.put(c, score);
    	this.markDirty();

    }
    
    public int getScore(ChunkCoordinates c)
    {
    	if (cc.containsKey(c)) {
    		return cc.get(c);
    	}
    	return 0;
    }
    
    public static ChunkScoreWorldData get(World world) {
		ChunkScoreWorldData data = (ChunkScoreWorldData) world.perWorldStorage.loadData(ChunkScoreWorldData.class, ID);
        if (data == null) {
        	System.out.println("!!NEED NEW CHUNK SCORE DATA!!");
            data = new ChunkScoreWorldData();
            world.perWorldStorage.setData(ID, data);
            world.perWorldStorage.saveAllData();
        }
        return data;
    }
}