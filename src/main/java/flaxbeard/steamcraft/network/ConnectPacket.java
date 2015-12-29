package flaxbeard.steamcraft.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;

public class ConnectPacket implements IMessage {
    public World world;
    public int x;
    public int y;
    public int z;
    public int subHit;

    public ConnectPacket() {}

    public ConnectPacket(World world, int x, int y, int z, int subHit) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
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
