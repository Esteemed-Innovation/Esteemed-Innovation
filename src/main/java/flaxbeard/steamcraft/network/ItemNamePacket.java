package flaxbeard.steamcraft.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ItemNamePacket implements IMessage {
    public World world;
    public EntityPlayer player;
    public int x;
    public int y;
    public int z;
    public String name;

    public ItemNamePacket() {}

    public ItemNamePacket(World world, int x, int y, int z, String name, EntityPlayer player) {
        this.world = world;
        this.player = player;
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.name = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        ByteBufUtils.writeUTF8String(buf, this.name);
    }
}
