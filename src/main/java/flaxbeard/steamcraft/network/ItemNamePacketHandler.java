package flaxbeard.steamcraft.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import flaxbeard.steamcraft.gui.ContainerSteamAnvil;
import flaxbeard.steamcraft.tile.TileEntitySteamHammer;

public class ItemNamePacketHandler implements IMessageHandler<ItemNamePacket, IMessage> {
    @Override
    public IMessage onMessage(ItemNamePacket message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        TileEntity hammer = player.worldObj.getTileEntity(message.x, message.y, message.z);
        if (hammer != null && hammer instanceof TileEntitySteamHammer) {
            ContainerSteamAnvil anvil = (ContainerSteamAnvil) player.openContainer;
            anvil.updateItemName(message.name);
        }
        return null;
    }
}
