package flaxbeard.steamcraft.tile;


import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.SteamcraftBlocks;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.item.ItemSmashedOre;

public class TileEntitySmasher extends TileEntity {
	
	private boolean hasBlockUpdate = false;
	private boolean isActive = false;
	private boolean isBreaking = false;
	private boolean shouldStop = false;
	public int spinup = 0;
	public float extendedLength = 0.0F;
	public Block smooshingBlock;
	public int smooshingMeta;
	public int extendedTicks = 0;
	public ItemStack smooshedStack;
	
	@Override
	public Packet getDescriptionPacket()
	{
    	super.getDescriptionPacket();
        NBTTagCompound access = new NBTTagCompound();
        access.setInteger("spinup", spinup);
        access.setFloat("extendedLength", extendedLength);
        access.setInteger("extendedTicks", extendedTicks);
        access.setInteger("block", Block.getIdFromBlock(smooshingBlock));
        access.setInteger("smooshingMeta", smooshingMeta);
        
        access.setInteger("doParticleEffects", encodeParticles());
        

        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, access);
	}
	
	
	private int encodeParticles(){
		int smash=0 , smoke1=0, smoke2=0, smoke3=0;
		if (!worldObj.isRemote){
			if (extendedTicks==3) smash = 1;
			if (extendedTicks >= 8 && extendedTicks <= 16 && (extendedTicks % 4) == 0) smoke1 = 2;
			//int smoke1 = (extendedTicks == 3) ? (1 << 1) : 0;
			//int smoke2 = (extendedTicks >= 8) && (extendedTicks <= 16) && ((extendedTicks % 4) == 0) ? (1  << 2): 0;
			//int smoke3 =  (extendedTicks == 6) ? (1 << 3) : 0;
			
		}
		
		
		return smash + smoke1 + smoke2 + smoke3;
	}

	private void decodeAndCreateParticles(int flags){
		if (worldObj.isRemote){
			boolean smash = (flags & 1) == 1; // 0b0001
			boolean smoke1 = ((flags & 2) >> 1 ) == 1; // 0b0010
			boolean smoke2 = ((flags & 4) >> 2) == 1; // 0b0100
			boolean smoke3 = ((flags & 8) >> 3) == 1; // 0b1000
			int[] tgt = getTarget(1);
			int x= tgt[0], y = yCoord, z=tgt[1];

			if (smash){
				System.out.println((float)(Math.random()-0.5F)/3.0F);
				//worldObj.spawnParticle("smoke", x+0.5D, y+0.5D, z+0.5D, (float)(Math.random()-0.5F)/3.0F, (float)(Math.random()-0.5F)/3.0F, (float)(Math.random()-0.5F)/3.0F);
				//worldObj.spawnParticle("smoke", x+0.5D, y+0.5D, z+0.5D, (float)(Math.random()-0.5F)/3.0F, (float)(Math.random()-0.5F)/6.0F, (float)(Math.random()-0.5F)/3.0F);
				//worldObj.spawnParticle("smoke", x+0.5D, y+0.5D, z+0.5D, (float)(Math.random()-0.5F)/32.0F, (float)(Math.random()-0.5F)/12.0F, (float)(Math.random()-0.5F)/3.0F);
			}
			if (smoke1){
				float xV = 0F, zV = 0F;
				switch (getBlockMetadata()){
				case 2: zV = 0.05F; break;
				case 3: zV = -0.05F; break;
				case 4: xV = 0.05F; break;
				case 5: xV = -0.05F; break;
				default: break;
				}
				worldObj.spawnParticle("smoke", xCoord+0.5D, y+1.1D, zCoord+0.5D, xV, 0.05F, zV);
			}
		}
		
	}
	
	
    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
    	super.onDataPacket(net, pkt);
    	NBTTagCompound access = pkt.func_148857_g();
    	this.extendedLength = access.getFloat("extendedLength");
    	this.extendedTicks = access.getInteger("extendedTicks");
    	this.spinup = access.getInteger("spinup");
    	this.smooshingBlock = Block.getBlockById(access.getInteger("block"));
    	this.smooshingMeta = access.getInteger("smooshingMeta");
    	decodeAndCreateParticles(access.getInteger("doParticleEffects"));
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
	
	public void updateEntity(){
		int[] target = getTarget(1);
		int x = target[0], y = yCoord, z = target[1];
		//Remote == client, might as well not run on server
		
		
		//handle state changes
		if (this.hasBlockUpdate && this.hasPartner()){
			if (this.shouldStop){
				System.out.println("shouldStop");
				this.spinup = 0;
				this.extendedLength = 0;
				this.extendedTicks = 0;
				this.isActive = false;
				this.shouldStop = false;
				this.isBreaking = false;
				return;
			} else {
				System.out.println("shouldn'tStop");
			}
			//System.out.println("Status: isActive: "+isActive+"; isBreaking: "+isBreaking+"; shouldStop: "+shouldStop);
			if (this.hasSomethingToSmash() && !this.isActive){
				this.isActive = true;
				this.isBreaking = true;
			}
			this.hasBlockUpdate = false;
		}
		
		//handle processing
		if (this.isActive){

			// if we haven't spun up yet, do it.
			if (this.isBreaking){
				if (this.spinup < 41){
					//System.out.println("Spinning up!" + spinup);
					// spinup complete. SMAASH!
					if (this.spinup == 40){
						//System.out.println("SMAAAAASH");
						
						if (!worldObj.isAirBlock(x, y, z)){
							this.smooshingBlock = worldObj.getBlock(x, y, z);
							this.smooshingMeta = worldObj.getBlockMetadata(x, y, z);
							this.smooshedStack = new ItemStack(smooshingBlock.getItem(worldObj, x, y, z),1, smooshingMeta);
							this.spinup++;
							if (this.getBlockMetadata() % 2 == 0)
								worldObj.setBlockToAir(x, y, z); //TODO: create dummy block instead
						} else {
							if (this.hasPartner()){
								int[] pc = getTarget(2);
								TileEntitySmasher partner =  (TileEntitySmasher) worldObj.getTileEntity(pc[0], yCoord, pc[1]);
								if (partner.spinup == 40 || (partner.spinup == 41 && partner.shouldStop)){
									this.shouldStop = true;
									this.spinup++;
									return;
								}
							}
							
							
						}
						

						
						//TODO: play smashing sound
						//TODO: drop item(s)
						// if (meta % 2 == 0) I'm the drop handler.
					}
					this.spinup++;
				
				// if we've spun up, extend
				} else if (this.extendedLength < 0.5F){
					//System.out.println("Extending: "+this.extendedLength);
					this.extendedLength += 0.1F;
					if (this.extendedTicks == 3){
						
						if (this.getBlockMetadata() % 2 == 0 && !worldObj.isRemote) spawnItems(x, y, z);
						
					}
					this.extendedTicks++;
				
				// we're done extending. Time to go inactive and start retracting	
				} else {
					this.isBreaking = false;
					this.spinup = 0;
				}
			} else {
				// Get back in line!
				if (this.extendedLength > 0.0F){
					this.extendedLength -= 0.025F;
					this.extendedTicks++;
					//System.out.println("Retracting: "+this.extendedLength);
					if (this.extendedLength < 0F) this.extendedLength = 0F;
				} else {
					//TODO: destroy dummy block
					//System.out.println("Done!");
					this.isActive = false;
					this.extendedTicks = 0;
				}
			}
			
			//Mark for sync
			this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	
	}
	
	private void spawnItems(int x, int y, int z){
		int id[] = OreDictionary.getOreIDs(this.smooshedStack);
		
		if (id.length > 0 && ItemSmashedOre.oreTypesFromOre.containsKey(OreDictionary.getOreName(id[0]))) {
			//Chance you'll get double
			boolean doubleItems = worldObj.rand.nextInt(3) == 0;
			ItemStack items = new ItemStack(SteamcraftItems.smashedOre, doubleItems ? 2 : 1, ItemSmashedOre.oreTypesFromOre.get(OreDictionary.getOreName(id[0])));
			EntityItem entityItem = new EntityItem(this.worldObj, x+0.5F, y+0.5F, z+0.5F, items);
			this.worldObj.spawnEntityInWorld(entityItem);
			this.smooshedStack = null;
		}
		else
		{
			smooshingBlock.dropBlockAsItem(worldObj, x, y, z, this.smooshingMeta, 0);
		}
	}
	
	private boolean hasSomethingToSmash() {
		//System.out.println("Can I smash anything?");
		int[] target = getTarget(1);
		int x = target[0], y=yCoord, z=target[1];
		if (!worldObj.isAirBlock(x, y, z)){
			System.out.println("Maybe?");
			return true;
		} else {
			System.out.println("No =( " + x + ", " + y + ", " + z);
			return false;
		}
		
	}

	public boolean hasPartner(){
		System.out.println("Do I have a partner?");
		int[] target = getTarget(2);
		int x = target[0], y=yCoord, z=target[1], opposite=target[2];
		
		if (worldObj.getBlock(x, y, z) == SteamcraftBlocks.smasher && worldObj.getBlockMetadata(x, y, z) == opposite){
			System.out.println("I have a partner!");
			return true;
		}
		
		return false;
	}
	
	// returns x, z, opposite
	private int[] getTarget(int distance){
		int x = xCoord, z = zCoord, opposite = 0, meta = getBlockMetadata();
		opposite = meta % 2 == 0 ? meta +1 : meta - 1;
		switch (meta){
			case 2: z-=distance; opposite=3; break;
			case 3: z+=distance; opposite=2; break;
			case 4: x-=distance; opposite=5; break;
			case 5: x+=distance; opposite=4; break;
			default: break;
		}
		
		return new int[]{x, z, opposite};
	}
		

	public void blockUpdate() {
		this.hasBlockUpdate = true;
	}
	
	public boolean hasUpdate(){
		return hasBlockUpdate;
	}

}
