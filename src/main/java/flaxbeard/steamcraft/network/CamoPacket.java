package flaxbeard.steamcraft.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class CamoPacket implements IMessage {
    public int blockX;
    public int blockY;
    public int blockZ;

    public CamoPacket() {}

    public CamoPacket(int blockX, int blockY, int blockZ) {
        this.blockX = blockX;
        this.blockY = blockY;
        this.blockZ = blockZ;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.blockX = buf.readInt();
        this.blockY = buf.readInt();
        this.blockZ = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.blockX);
        buf.writeInt(this.blockY);
        buf.writeInt(this.blockZ);
    }
}
