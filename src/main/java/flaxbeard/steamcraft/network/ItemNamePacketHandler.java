package flaxbeard.steamcraft.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import flaxbeard.steamcraft.gui.ContainerSteamAnvil;
import flaxbeard.steamcraft.tile.TileEntitySteamHammer;

public class ItemNamePacketHandler implements IMessageHandler<ItemNamePacket, IMessage> {
    @Override
    public IMessage onMessage(ItemNamePacket message, MessageContext ctx) {
        System.out.println("doing anvil shit");
        int x = message.x;
        int y = message.y;
        int z = message.z;
        String name = message.name;
        EntityPlayer player = message.player;
        World world = message.world;
        if (player == null) {
            return null;
        }

        TileEntitySteamHammer hammer = (TileEntitySteamHammer) world.getTileEntity(x, y, z);
        if (hammer != null) {
            hammer.itemName = name;
            ContainerSteamAnvil anvil = (ContainerSteamAnvil) player.openContainer;
            anvil.updateItemName(name);
        }
        return null;
    }
}
