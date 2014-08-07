package flaxbeard.steamcraft.client.audio;

import flaxbeard.steamcraft.tile.TileEntityWhistle;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.util.ResourceLocation;

public class HornSound extends MovingSound {
	
	private static final ResourceLocation SOUND = new ResourceLocation("steamcraft:horn");
	private TileEntityWhistle whistle;
	private int xCoord, yCoord, zCoord;
	
	public HornSound(TileEntityWhistle whistle) {
		super(SOUND);
		this.whistle = whistle;
		this.repeat = true;
		this.xPosF = (float)(whistle.xCoord + 0.5F);
		this.yPosF = (float)(whistle.yCoord + 0.7F);
		this.zPosF = (float)(whistle.zCoord + 0.5F);
		this.xCoord = whistle.xCoord;
		this.yCoord = whistle.yCoord;
		this.zCoord = whistle.zCoord;
	}

	@Override
	public void update() {
		if (whistle.isInvalid()){
			//Steamcraft.log.debug("It's dead.");
			if (this.volume > 0.0F){
				//Steamcraft.log.debug("Decreasing volume: "+this.volume);
				this.volume -= 0.25F;
				if (this.volume <= 0){
					this.volume = 0.0F;
				}
			} else {
				//Steamcraft.log.debug("Killing it.");
				this.donePlaying = true;
			}
		} else {
			this.volume = whistle.getVolume();
		}
		
	}

}
