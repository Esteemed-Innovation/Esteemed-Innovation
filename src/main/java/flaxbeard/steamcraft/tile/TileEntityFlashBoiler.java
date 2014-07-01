package flaxbeard.steamcraft.tile;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import flaxbeard.steamcraft.SteamcraftBlocks;

public class TileEntityFlashBoiler extends TileEntity {
	
	private int frontSide = -1;
	
	private boolean loaded = false;
	
	// ====================================================
	//          All the possible configurations
	// ====================================================
	// bottom   top
	// OO       OO     Z ^
	// XO       OO     X--> 
	private static int[][] bbl = new int[][]{
		new int[]{0, 0, 0}, new int[]{1, 0, 0}, new int[]{0, 0, 1}, new int[]{1, 0, 1},
		new int[]{0, 1, 0}, new int[]{1, 1, 0}, new int[]{0, 1, 1}, new int[]{1, 1, 1}
	};
	
	// bottom   top
	// OO       OO     Z ^
	// OO       XO     X--> 
	private static int[][] tbl = new int[][]{
		new int[]{0, -1, 0}, new int[]{1, -1, 0}, new int[]{0, -1, 1}, new int[]{1, -1, 1},
		new int[]{0,  0, 0}, new int[]{1,  0, 0}, new int[]{0,  0, 1}, new int[]{1,  0, 1}
	};
	
	// bottom   top
	// OO       OO     Z ^
	// OX       OO     X--> 
	private static int [][]bbr = new int[][]{
		new int[]{-1, 0, 0}, new int[]{0, 0, 0}, new int[]{-1, 0, 1}, new int[]{0, 0, 1},
		new int[]{-1, 1, 0}, new int[]{0, 1, 0}, new int[]{-1, 1, 1}, new int[]{0, 1, 1}
	};
	
	// bottom   top
	// OO       OO     Z ^
	// OO       OX     X--> 
	private static int [][]tbr = new int[][]{
		new int[]{-1, -1, 0}, new int[]{0, -1, 0}, new int[]{-1, -1, 1}, new int[]{0, -1, 1},
		new int[]{-1,  0, 0}, new int[]{0,  0, 0}, new int[]{-1,  0, 1}, new int[]{0,  0, 1}
	};
	
	// bottom   top
	// XO       OO     Z ^
	// OO       OO     X--> 
	private static int [][]btl = new int[][]{
		new int[]{0, 0, -1}, new int[]{1, 0, -1}, new int[]{0, 0, 0}, new int[]{1, 0, 0},
		new int[]{0, 1, -1}, new int[]{1, 1, -1}, new int[]{0, 1, 0}, new int[]{1, 1, 0}
	};
	
	// bottom   top
	// OO       XO     Z ^
	// OO       OO     X--> 
	private static int [][]ttl = new int[][]{
		new int[]{0, -1, -1}, new int[]{1, -1, -1}, new int[]{0, -1, 0}, new int[]{1, -1, 0},
		new int[]{0,  0, -1}, new int[]{1,  0, -1}, new int[]{0,  0, 0}, new int[]{1,  0, 0}
	};
	
	// bottom   top
	// OX       OO     Z ^
	// OO       OO     X--> 
	private static int [][]btr = new int[][]{
		new int[]{-1, 0, -1}, new int[]{0, 0, -1}, new int[]{-1, 0, 0}, new int[]{0, 0, 0},
		new int[]{-1, 1, -1}, new int[]{0, 1, -1}, new int[]{-1, 1, 0}, new int[]{0, 1, 0}
	};
	
	// bottom   top
	// OO       OX     Z ^
	// OO       OO     X--> 
	private static int [][]ttr = new int[][]{
		new int[]{-1, -1, -1}, new int[]{0, -1, -1}, new int[]{-1, -1, 0}, new int[]{0, -1, 0},
		new int[]{-1,  0, -1}, new int[]{0,  0, -1}, new int[]{-1,  0, 0}, new int[]{0,  0, 0}
	};
	
	private static int[][][] validConfigs = new int[][][]{
		bbl, tbl, bbr, tbr, btl, ttl, btr, ttr
	};
	
	public void readFromNBT(NBTTagCompound access)
    {
        super.readFromNBT(access);
        this.frontSide = access.getInteger("frontSide");
    	
        //worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    	
    }

    @Override
    public void writeToNBT(NBTTagCompound access)
    {
        super.writeToNBT(access);
        access.setInteger("frontSide", this.frontSide);
        
    }
	
	@Override
	public Packet getDescriptionPacket()
	{
    	super.getDescriptionPacket();
        NBTTagCompound access = new NBTTagCompound();
        access.setInteger("frontSide", this.frontSide);
        
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, access);
	}
	
	@Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
    	super.onDataPacket(net, pkt);
    	NBTTagCompound access = pkt.func_148857_g();
    	this.frontSide = access.getInteger("frontSide");

    	worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
	
	public void checkMultiblock(boolean isBreaking, int frontSide) {
		//System.out.println(frontSideIn);
		int x = xCoord, y=yCoord, z=zCoord;
		
		//System.out.println("Checking multiblock; frontSide: "+this.frontSide);
		
		boolean isMultiblock = false;
		boolean isTooManyBlocks = false;
		
		
		if (!worldObj.isRemote){
			if (!isBreaking){
				//System.out.println(this.frontSide);
				int[] validClusters = getValidClusters(); 
				//System.out.println("valid configs found: " +validClusters.length);
				
				if (validClusters.length == 1){
					
					updateMultiblock(validClusters[0], true, frontSide);
					
					
				}
			}
		}
		
	}
	
	public void destroyMultiblock(){
		updateMultiblock(this.getValidClusterFromMetadata(), false, -1);
	}
	
	private int getValidClusterFromMetadata(){
		int validCluster = -1;
		// Because the clusters at the top are doofy and not in the right order =\
		switch(getBlockMetadata()){
		case 1: validCluster = 0; break;
		case 2: validCluster = 2; break;
		case 3: validCluster = 4; break;
		case 4: validCluster = 6; break;
		case 5: validCluster = 1; break;
		case 6: validCluster = 3; break;
		case 7: validCluster = 5; break;
		case 8: validCluster = 7; break;
		}
		
		return validCluster;
	}
	
	private int checkCluster(int[][] cluster){
		int count = 0;
		for (int pos = 0; pos < 8; pos++){
			int x = cluster[pos][0]+xCoord, y= cluster[pos][1]+yCoord, z=cluster[pos][2]+zCoord;
			Block b = worldObj.getBlock(x,y,z);
			if (b == SteamcraftBlocks.flashBoiler){
				TileEntityFlashBoiler fb = (TileEntityFlashBoiler) worldObj.getTileEntity(x, y, z);
				if (! (worldObj.getBlockMetadata(x, y, z) > 0)){
					count++;
				}
				
			}
			
		}
		
		return count;
	}
	
	private int[] getValidClusters(){
		int[] valid = new int[8];
		int[] out;
		int count = 0;
		for (int clusterIndex = 0; clusterIndex< 8; clusterIndex++){
			//System.out.println("Checking cluster "+clusterIndex);
			boolean isValid = false;
			if (checkCluster(validConfigs[clusterIndex])==8){
				valid[count] = clusterIndex;
				count++;
				isValid = true;
			}
		}
		out = new int[count];
		for (int i = 0; i < count; i++){
			out[i] = valid[i];
		}
		return out;
	}
	
	private int[][] getClusterCoords(int clusterIndex){
		int[][] cluster = validConfigs[clusterIndex];
		int[][] out = new int[8][3];
		for (int pos = 0; pos < 8; pos++){
			out[pos]= new int[]{cluster[pos][0]+xCoord, cluster[pos][1]+yCoord, cluster[pos][2]+zCoord};
		}
		return out;
	}
	
	private void updateMultiblock(int clusterIndex, boolean isMultiblock, int frontSide){
		int[][] cluster = getClusterCoords(clusterIndex);
		for (int pos = 0; pos < 8; pos++){
			int x = cluster[pos][0], y= cluster[pos][1],z= cluster[pos][2];
			if (worldObj.getBlock(x, y, z) == SteamcraftBlocks.flashBoiler){
				worldObj.setBlockMetadataWithNotify(
						cluster[pos][0], cluster[pos][1], cluster[pos][2], 
						isMultiblock ? pos+1 : 0, 
						2
					);
				TileEntityFlashBoiler boiler = (TileEntityFlashBoiler) worldObj.getTileEntity(cluster[pos][0], cluster[pos][1], cluster[pos][2]);
				boiler.setFront(frontSide, false);
			} else {
				System.out.println("ERROR! ("+x+","+y+","+z+") is not a flashBoiler!");
			}
			
		}
	}
	
	private void printClusterCoords(int clusterIndex){
		int[][] cluster = getClusterCoords(clusterIndex);
		for (int pos = 0; pos < 8; pos++){
			System.out.println(cluster[pos][0]+ ", " + cluster[pos][1] + ", " +cluster[pos][2]);
		}
	}
	
	public TileEntityFlashBoiler getMasterTileEntity(){
		int[][] cluster = getClusterCoords(getValidClusterFromMetadata());
		int x = cluster[0][0], y=cluster[0][1], z=cluster[0][2];
		TileEntityFlashBoiler boiler = null;
		if (worldObj.getBlock(x, y, z)==SteamcraftBlocks.flashBoiler && worldObj.getBlockMetadata(x, y, z) > 0){
			boiler = (TileEntityFlashBoiler) worldObj.getTileEntity(x, y, z);
		}
		
		return boiler;
	}

	public void setFront(int frontSide, boolean print) {
		if (print) System.out.println("Setting front side to "+frontSide);
		if (!worldObj.isRemote)
			this.frontSide = frontSide;
	}
	
	public int getFront(){
		return this.frontSide;
	}
	
	public void updateEntity(){
		String worldLoc = worldObj.isRemote? "Client: " : "Server: ";
		//System.out.println(worldLoc + this.frontSide);
		if (!loaded){
			System.out.println("Loading up the block!");
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			this.loaded = true;
		}
	}
}