package eiteam.esteemedinnovation.network;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import eiteam.esteemedinnovation.tile.TileEntitySteamPipe;

public class ConnectPacketHandler implements IMessageHandler<ConnectPacket, IMessage> {
    @Override
    public IMessage onMessage(ConnectPacket message, MessageContext ctx) {
        World world = ctx.getServerHandler().playerEntity.worldObj;
        int x = message.x;
        int y = message.y;
        int z = message.z;
        BlockPos pos = new BlockPos(x, y, z);
        int subHit = message.subHit;
        TileEntity tile = world.getTileEntity(pos);
        if (tile != null && tile instanceof TileEntitySteamPipe) {
            ((TileEntitySteamPipe) tile).connectDisconnect(world, new BlockPos(x, y, z), subHit);
        }
        return null;
    }
}
