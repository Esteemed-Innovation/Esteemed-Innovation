package eiteam.esteemedinnovation.commons.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ConnectPacket implements IMessage {
    public int x;
    public int y;
    public int z;
    public int subHit;

    public ConnectPacket() {}

    public ConnectPacket(BlockPos pos, int subHit) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        this.subHit = subHit;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.subHit = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        buf.writeInt(this.subHit);
    }
}
