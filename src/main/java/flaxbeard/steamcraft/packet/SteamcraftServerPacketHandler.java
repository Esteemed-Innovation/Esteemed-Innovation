package flaxbeard.steamcraft.packet;

import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.gui.ContainerSteamAnvil;
import flaxbeard.steamcraft.item.ItemExosuitArmor;
import flaxbeard.steamcraft.tile.TileEntitySteamHammer;
import io.netty.buffer.ByteBufInputStream;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;

public class SteamcraftServerPacketHandler {

	  
	private void handleSpacePacket(ByteBufInputStream dat, World world)
 	{
		try {
			int id = dat.readInt();
			EntityPlayer player = (EntityPlayer) world.getEntityByID(id);
			if (player != null) {
				ItemStack armor = player.getCurrentArmor(2);
				if (armor != null && armor.getItem() == SteamcraftItems.exoArmorBody) {
					ItemExosuitArmor item = (ItemExosuitArmor) armor.getItem();
					if (item.hasUpgrade(armor, SteamcraftItems.jetpack) && armor.getItemDamage() < armor.getMaxDamage()-5) {
						if (!player.onGround && !player.capabilities.isFlying) {
							player.motionY=player.motionY+0.06D;
							player.fallDistance = 0.0F;
							player.getCurrentArmor(2).damageItem(5, player);
						}
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
 	}
	  
	private void handleItemNamePacket(ByteBufInputStream dat, World world)
 	{
		try {
			int x = dat.readInt();
			int y = dat.readInt();
			int z = dat.readInt();
			String s = dat.readUTF();
			int id = dat.readInt();
			EntityPlayer player = (EntityPlayer) world.getEntityByID(id);
			if (player != null) {
				TileEntitySteamHammer hammer = (TileEntitySteamHammer) world.getTileEntity(x, y, z);
				if (hammer != null) {
					hammer.itemName = s;
					ContainerSteamAnvil anvil = (ContainerSteamAnvil) player.openContainer;
					anvil.updateItemName(s);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
 	}

	@SubscribeEvent
	public void onServerPacket(ServerCustomPacketEvent event) {
		EntityPlayerMP player = ((NetHandlerPlayServer)event.handler).playerEntity;
		ByteBufInputStream bbis = new ByteBufInputStream(event.packet.payload());
        byte packetType;
        int dimension;
        byte packetID;

        try
        {
        	packetID = bbis.readByte();
        	dimension = bbis.readInt();
            World world = DimensionManager.getWorld(dimension);
           
            if (packetID == 0) {
            	this.handleSpacePacket(bbis, world);
            }
            if (packetID == 1) {
            	this.handleItemNamePacket(bbis, world);
            }
            bbis.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
	}
}
