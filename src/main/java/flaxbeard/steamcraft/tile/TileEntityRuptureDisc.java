package flaxbeard.steamcraft.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.block.BlockRuptureDisc;

public class TileEntityRuptureDisc extends TileEntity {
	
	private boolean isLeaking = false;
	
	@Override
	public Packet getDescriptionPacket(){
		NBTTagCompound access = new NBTTagCompound();
		access.setBoolean("isLeaking", this.isLeaking);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, access);
	}
	
	@Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt){
		super.onDataPacket(net, pkt);
		NBTTagCompound access = pkt.func_148857_g();
		this.isLeaking = access.getBoolean("isLeaking");
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	public void updateEntity(){
		if (worldObj.isRemote){
			if (this.isLeaking){
				float offset = 10.0F/16.0F;
				float xOffset = myDir().getOpposite().offsetX * offset;
				float yOffset = myDir().getOpposite().offsetY * offset;
				float zOffset = myDir().getOpposite().offsetZ * offset;
				//for (int i = 0; i < 10; i++){
					this.worldObj.spawnParticle("smoke", xCoord+0.5F+xOffset, yCoord+0.5F+yOffset, zCoord+0.5F+zOffset, myDir().offsetX*0.1F, myDir().offsetY*0.1F, myDir().offsetZ*0.1F);
				//}
			}
		} else {
			if (getPressure() > 1.1F) {
				if (this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord) < 6) {
					this.worldObj.createExplosion(null, xCoord+0.5F, yCoord+0.5F, zCoord+0.5F, 0.0F, true);
					this.worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord) + 10, 2);
				}

			}
			if (this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord) > 9) {
				int i = 0;
				if (this.getSteam() > 0) {
					if (!this.isLeaking){
						this.isLeaking = true;
						worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
					}
					this.worldObj.playSoundEffect(this.xCoord+0.5F, this.yCoord+0.5F, this.zCoord+0.5F, "steamcraft:leaking", 2.0F, 0.9F);
					
				} else {
					if (this.isLeaking){
						this.isLeaking = false;
						worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
					}
				}
				while (this.getSteam() > 0 && i < 10) {
					this.drainSteam(1);
					
					float offset = 10.0F/16.0F;
					float xOffset = myDir().getOpposite().offsetX * offset;
					float yOffset = myDir().getOpposite().offsetY * offset;
					float zOffset = myDir().getOpposite().offsetZ * offset;
					//if (worldObj.isRemote) System.out.println("draining steam");
					
					
					i++;
				}
			} else {
				if (this.isLeaking){
					this.isLeaking = false;
					worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				}
			}
		}
		
	}
	
	private void drainSteam(int s) {
		ForgeDirection d = myDir().getOpposite();
		ISteamTransporter source = null;
		TileEntity te = worldObj.getTileEntity(xCoord + d.offsetX, yCoord, zCoord+ d.offsetZ);
		if (te != null && te instanceof ISteamTransporter){
			source = (ISteamTransporter) te;
			source.decrSteam(s);
		}
	}
	
	private int getSteam(){
		ForgeDirection d = myDir().getOpposite();
		ISteamTransporter source = null;
		TileEntity te = worldObj.getTileEntity(xCoord + d.offsetX, yCoord, zCoord+ d.offsetZ);
		if (te != null && te instanceof ISteamTransporter){
			source = (ISteamTransporter) te;
			if (worldObj.isRemote){
				System.out.println(source.getSteam());
			}
			return source.getSteam();
		}
		return 0;
	}
	
	private float getPressure(){
		ForgeDirection d = myDir().getOpposite();
		ISteamTransporter source = null;
		TileEntity te = worldObj.getTileEntity(xCoord + d.offsetX, yCoord, zCoord+ d.offsetZ);
		if (te != null && te instanceof ISteamTransporter){
			source = (ISteamTransporter) te;
			return source.getPressure();
		}
		return 0.0F;
	}

	private ForgeDirection myDir(){
		return ForgeDirection.getOrientation(BlockRuptureDisc.getMeta(worldObj.getBlockMetadata(xCoord, yCoord, zCoord)));
	}
	
	
}
