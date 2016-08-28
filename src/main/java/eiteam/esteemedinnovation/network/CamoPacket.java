package eiteam.esteemedinnovation.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CamoPacket implements IMessage {
    public int blockX;
    public int blockY;
    public int blockZ;

    public CamoPacket() {}

    public CamoPacket(BlockPos pos) {
        this.blockX = pos.getX();
        this.blockY = pos.getY();
        this.blockZ = pos.getZ();
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
