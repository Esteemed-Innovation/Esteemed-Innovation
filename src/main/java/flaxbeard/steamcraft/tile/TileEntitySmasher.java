package flaxbeard.steamcraft.tile;

import flaxbeard.steamcraft.SteamcraftBlocks;
import flaxbeard.steamcraft.misc.Tuple3;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntitySmasher extends TileEntity {
	
	private boolean hasBlockUpdate = false;
	private boolean isActive = false;
	private boolean isBreaking = false;
	private boolean shouldStop = false;
	private int spinup = 0;
	private float extendedLength = 0.0F;
	
	public void updateEntity(){
		//handle state changes
		if (this.hasBlockUpdate && this.hasPartner()){
			System.out.println("Status: isActive: "+isActive+"; isBreaking: "+isBreaking+"; shouldStop: "+shouldStop);
			if (this.hasSomethingToSmash() && !this.isActive){
				this.isActive = true;
				this.isBreaking = true;
			}
			this.hasBlockUpdate = false;
		} else if (this.shouldStop){
			this.spinup = 0;
			this.extendedLength = 0;
			this.isActive = false;
		}
		
		//handle processing
		if (this.isActive){
			// if we haven't spun up yet, do it.
			if (this.isBreaking){
				if (this.spinup < 41){
					System.out.println("Spinning up!" + spinup);
					// spinup complete. SMAASH!
					if (this.spinup == 40){
						System.out.println("SMAAAAASH");
						int[] target = getTarget(1);
						int x = target[0], y = yCoord, z = target[1];
						worldObj.setBlockToAir(x, y, z); //TODO: create dummy block instead
						
						//TODO: play smashing sound
						//TODO: drop item(s)
						// if (meta % 2 == 0) I'm the drop handler.
					}
					this.spinup++;
				
				// if we've spun up, extend
				} else if (this.extendedLength < 0.5F){
					System.out.println("Extending: "+this.extendedLength);
					this.extendedLength += 0.05F;
				
				// we're done extending. Time to go inactive and start retracting	
				} else {
					this.isBreaking = false;
					this.spinup = 0;
				}
			} else {
				// Get back in line!
				if (this.extendedLength > 0.0F){
					this.extendedLength -= 0.5F;
					if (this.extendedLength < 0F) this.extendedLength = 0F;
				} else {
					//TODO: destroy dummy block
					System.out.println("Done!");
					this.isActive = false;
				}
			}
			
		}
	
	}
	
	private boolean hasSomethingToSmash() {
		System.out.println("Can I smash anything?");
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
