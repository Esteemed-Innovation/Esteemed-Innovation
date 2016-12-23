package eiteam.esteemedinnovation.hammer;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ItemNamePacketHandler implements IMessageHandler<ItemNamePacket, IMessage> {
    @Override
    public IMessage onMessage(ItemNamePacket message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        BlockPos pos = new BlockPos(message.x, message.y, message.z);
        TileEntity hammer = player.worldObj.getTileEntity(pos);
        if (hammer != null && hammer instanceof TileEntitySteamHammer) {
            ContainerSteamAnvil anvil = (ContainerSteamAnvil) player.openContainer;
            anvil.updateItemName(message.name);
        }
        return null;
    }
}
