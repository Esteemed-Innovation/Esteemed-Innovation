package flaxbeard.steamcraft.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import flaxbeard.steamcraft.tile.TileEntitySteamPipe;

public class ConnectPacketHandler implements IMessageHandler<ConnectPacket, IMessage> {
    @Override
    public IMessage onMessage(ConnectPacket message, MessageContext ctx) {
        World world = ctx.getServerHandler().playerEntity.worldObj;
        int x = message.x;
        int y = message.y;
        int z = message.z;
        int subHit = message.subHit;
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null && tile instanceof TileEntitySteamPipe) {
            ((TileEntitySteamPipe) tile).connectDisconnect(world, x, y, z, subHit);
        }
        return null;
    }
}
