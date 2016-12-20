package eiteam.esteemedinnovation.misc;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityDummyBlock extends TileEntity implements ITickable {
    private int timeToLive = 25;

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        super.getUpdatePacket();
        NBTTagCompound access = new NBTTagCompound();
        access.setInteger("ttl", timeToLive);
        return new SPacketUpdateTileEntity(pos, 1, access);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound access = pkt.getNbtCompound();
        this.timeToLive = access.getInteger("ttl");
        markDirty();
    }

    @Override
    public void update() {
        if (this.timeToLive <= 0) {
            worldObj.setBlockToAir(pos);
        }
        this.timeToLive--;
    }
}
