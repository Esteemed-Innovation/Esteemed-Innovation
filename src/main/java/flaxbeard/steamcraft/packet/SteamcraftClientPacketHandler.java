package flaxbeard.steamcraft.packet;

import flaxbeard.steamcraft.Steamcraft;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;

public class SteamcraftClientPacketHandler extends SteamcraftServerPacketHandler {
	
	public static void sendSpacePacket(Entity player)
	{
		ByteBuf buf = Unpooled.buffer();
		ByteBufOutputStream out = new ByteBufOutputStream(buf);
		try
	    {
	    	out.writeByte(0);
	    	out.writeInt(player.worldObj.provider.dimensionId);
	    	out.writeInt(player.getEntityId());
	    }
	    catch (IOException e) {}
	    FMLProxyPacket packet = new FMLProxyPacket(buf,"steamcraft");
	    Steamcraft.channel.sendToServer(packet);
	    try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendNoSpacePacket(Entity player)
	{
		ByteBuf buf = Unpooled.buffer();
		ByteBufOutputStream out = new ByteBufOutputStream(buf);
		try
	    {
	    	out.writeByte(2);
	    	out.writeInt(player.worldObj.provider.dimensionId);
	    	out.writeInt(player.getEntityId());
	    }
	    catch (IOException e) {}
	    FMLProxyPacket packet = new FMLProxyPacket(buf,"steamcraft");
	    Steamcraft.channel.sendToServer(packet);
	    try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SubscribeEvent
	public void onClientPacket(ClientCustomPacketEvent event) {
		EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
		ByteBufInputStream bbis = new ByteBufInputStream(event.packet.payload());
        byte packetType;
        int dimension;
        byte packetID;
        try
        {
        	bbis.close();
        }
		catch (Exception e) {
            e.printStackTrace();
            return;
		}
	}

	public static void sendItemNamePacket(World worldObj, int x,
			int y, int z, String s, EntityPlayer player) {
		ByteBuf buf = Unpooled.buffer();
		ByteBufOutputStream out = new ByteBufOutputStream(buf);
		try
	    {
	    	out.writeByte(1);
	    	out.writeInt(worldObj.provider.dimensionId);
	    	out.writeInt(x);
	    	out.writeInt(y);
	    	out.writeInt(z);
	    	out.writeUTF(s);
	    	out.writeInt(player.getEntityId());
	    }
	    catch (IOException e) {}
	    FMLProxyPacket packet = new FMLProxyPacket(buf,"steamcraft");
	    Steamcraft.channel.sendToServer(packet);
	    try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
