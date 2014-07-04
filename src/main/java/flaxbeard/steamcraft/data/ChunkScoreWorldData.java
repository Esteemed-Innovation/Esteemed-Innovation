package flaxbeard.steamcraft.data;
import java.util.HashMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class ChunkScoreWorldData extends WorldSavedData {

    public HashMap<Long, Integer> cc = new HashMap<Long, Integer>();
    private static final String ID = "ChunkScoreWorldData";
    
    public ChunkScoreWorldData(String str) {
        super(ID);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
    	System.out.println("NEED OLD DATA");

    	NBTTagList nbtl =  (NBTTagList) nbt.getTag("ccs");
    	for (int i = 0; i < nbtl.tagCount(); ++i)
    	{
    		NBTTagCompound nbt2 = (NBTTagCompound)nbtl.getCompoundTagAt(i);
    		Long l = nbt2.getLong("long");
    		int score = nbt2.getInteger("score");
    		cc.put(l,score);
    	}
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
		NBTTagList nbtl = new NBTTagList();
		for (long c : cc.keySet()) {
			NBTTagCompound nbt2 = new NBTTagCompound();
			nbt2.setLong("long", c);
			nbt2.setInteger("score", cc.get(c));
			nbtl.appendTag(nbt2);
		}
		nbt.setTag("ccs", nbtl);
    }
    
    public void up(int x, int y) {
    	int score = 0;
    	long key = (((long)x) << 32) | (y & 0xffffffffL);
    	if (cc.containsKey(key)) {
    		score = cc.get(key);
    	}
    	score++;
    	cc.put(key, score);
    	this.markDirty();

    }
    
    public void down(int x, int y) {
    	int score = 0;
    	long key = (((long)x) << 32) | (y & 0xffffffffL);
    	if (cc.containsKey(key)) {
    		score = cc.get(key);
    	}
    	score--;
    	if (score < 0) {
    		score = 0;
    	}
    	cc.put(key, score);
    	this.markDirty();
    }
    
    public int getScore(int x, int y) {
    	long key = (((long)x) << 32) | (y & 0xffffffffL);
    	if (cc.containsKey(key)) {
    		return cc.get(key);
    	}
    	return 0;
    }
    
    public static ChunkScoreWorldData get(World world) {
		ChunkScoreWorldData data = (ChunkScoreWorldData) world.perWorldStorage.loadData(ChunkScoreWorldData.class, ID);
        if (data == null) {
        	System.out.println("!!NEED NEW CHUNK SCORE DATA!!");
            data = new ChunkScoreWorldData("");
            world.perWorldStorage.setData(ID, data);
        }
        return data;
    }
}