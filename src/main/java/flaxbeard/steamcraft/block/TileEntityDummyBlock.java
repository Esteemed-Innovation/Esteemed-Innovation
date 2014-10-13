package flaxbeard.steamcraft.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityDummyBlock extends TileEntity {
    private int timeToLive = 25;

    @Override
    public Packet getDescriptionPacket() {
        super.getDescriptionPacket();
        NBTTagCompound access = new NBTTagCompound();
        access.setInteger("ttl", timeToLive);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, access);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound access = pkt.func_148857_g();
        this.timeToLive = access.getInteger("ttl");
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void updateEntity() {
        if (this.timeToLive <= 0) {
            worldObj.setBlockToAir(xCoord, yCoord, zCoord);
        }
        this.timeToLive--;


    }
}
