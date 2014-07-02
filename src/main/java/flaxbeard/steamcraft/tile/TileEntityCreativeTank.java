package flaxbeard.steamcraft.tile;


public class TileEntityCreativeTank extends TileEntitySteamTank {
	@Override
	public int getSteam() {
		return (int) (90001);
	}
	
	@Override
	public float getPressure() {
		return 1.0F;
	}
	
	@Override
	public int getCapacity() {
		return 90001;
	}
	
	@Override
	public void decrSteam(int i) {
	}
}
