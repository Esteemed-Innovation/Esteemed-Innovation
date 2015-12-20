package flaxbeard.steamcraft.packet;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.misc.ExplosionRocket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.io.IOException;

public class SteamcraftClientPacketHandler extends SteamcraftServerPacketHandler {
    public static void sendSpacePacket(Entity player) {
        ByteBuf buf = Unpooled.buffer();
        ByteBufOutputStream out = new ByteBufOutputStream(buf);
        try {
            out.writeByte(0);
            out.writeInt(player.worldObj.provider.dimensionId);
            out.writeInt(player.getEntityId());
        } catch (IOException ignored) {}
        FMLProxyPacket packet = new FMLProxyPacket(buf, "steamcraft");
        Steamcraft.channel.sendToServer(packet);
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void sendCamoPacket(Entity player, MovingObjectPosition pos) {
        ByteBuf buf = Unpooled.buffer();
        ByteBufOutputStream out = new ByteBufOutputStream(buf);
        try {
            out.writeByte(3);
            out.writeInt(player.worldObj.provider.dimensionId);
            out.writeInt(player.getEntityId());
            out.writeInt(pos.blockX);
            out.writeInt(pos.blockY);
            out.writeInt(pos.blockZ);
        } catch (IOException ignored) {}
        FMLProxyPacket packet = new FMLProxyPacket(buf, "steamcraft");
        Steamcraft.channel.sendToServer(packet);
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendConnectPacket(Entity player, int x, int y, int z, MovingObjectPosition pos) {
        ByteBuf buf = Unpooled.buffer();
        ByteBufOutputStream out = new ByteBufOutputStream(buf);
        try {
            out.writeByte(4);
            out.writeInt(player.worldObj.provider.dimensionId);
            out.writeInt(player.getEntityId());
            out.writeInt(x);
            out.writeInt(y);
            out.writeInt(z);
            out.writeInt(pos.subHit);
        } catch (IOException ignored) {}
        FMLProxyPacket packet = new FMLProxyPacket(buf, "steamcraft");
        Steamcraft.channel.sendToServer(packet);
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendNoSpacePacket(Entity player) {
        ByteBuf buf = Unpooled.buffer();
        ByteBufOutputStream out = new ByteBufOutputStream(buf);
        try {
            out.writeByte(2);
            out.writeInt(player.worldObj.provider.dimensionId);
            out.writeInt(player.getEntityId());
        } catch (IOException ignored) {}
        FMLProxyPacket packet = new FMLProxyPacket(buf, "steamcraft");
        Steamcraft.channel.sendToServer(packet);
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendItemNamePacket(World worldObj, int x, int y, int z, String s, EntityPlayer player) {
        ByteBuf buf = Unpooled.buffer();
        ByteBufOutputStream out = new ByteBufOutputStream(buf);
        try {
            out.writeByte(1);
            out.writeInt(worldObj.provider.dimensionId);
            out.writeInt(x);
            out.writeInt(y);
            out.writeInt(z);
            out.writeUTF(s);
            out.writeInt(player.getEntityId());
        } catch (IOException ignored) {}
        FMLProxyPacket packet = new FMLProxyPacket(buf, "steamcraft");
        Steamcraft.channel.sendToServer(packet);
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendGrapplePacket(EntityPlayer player, int x, int y, int z) {
        ByteBuf buf = Unpooled.buffer();
        ByteBufOutputStream out = new ByteBufOutputStream(buf);
        try {
            out.writeByte(5);
            out.writeInt(player.worldObj.provider.dimensionId);
            out.writeInt(player.getEntityId());
            out.writeInt(x);
            out.writeInt(y);
            out.writeInt(z);
            out.writeDouble(player.posX);
            out.writeDouble(player.posY);
            out.writeDouble(player.posZ);
        } catch (IOException ignored) {}
        FMLProxyPacket packet = new FMLProxyPacket(buf, "steamcraft");
        Steamcraft.channel.sendToServer(packet);
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onClientPacket(ClientCustomPacketEvent event) {
        ByteBufInputStream bbis = new ByteBufInputStream(event.packet.payload());
        byte packetType;
        int dimension;
        try {
            packetType = bbis.readByte();
            dimension = bbis.readInt();
            World world = DimensionManager.getWorld(dimension);
            switch (packetType) {
                case 2: {
                    this.handleRocketJumpHackyPacket(bbis, world);
                }
                case 3: {
                    this.handleExplodePacket(bbis, world);
                }
            }
//        	for (int i = 0; i < 3; i++){
//        		player.worldObj.spawnParticle("smoke", x, y, z, -0.005D+(Math.random()*0.01D), 0.025D, -0.005D+(Math.random()*0.01D));
//        	}
            bbis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleRocketJumpHackyPacket(ByteBufInputStream dat, World world) {
        try {
            int id = dat.readInt();
            Entity entity = world.getEntityByID(id);
            if (entity == null || !(entity instanceof EntityPlayer)) {
                return;
            }
            EntityPlayer player = (EntityPlayer) entity;
            double motionX = dat.readDouble();
            double motionY = dat.readDouble();
            double motionZ = dat.readDouble();
            player.motionX += motionX;
            player.motionY += motionY;
            player.motionZ += motionZ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleExplodePacket(ByteBufInputStream dat, World world) {
        try {
            newExplosion(world, null, dat.readDouble(), dat.readDouble(), dat.readDouble(), dat.readFloat(), true, world.getGameRules().getGameRuleBooleanValue("mobGriefing"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Explosion newExplosion(World world, Entity entity, double p_72885_2_, double p_72885_4_, double p_72885_6_, float p_72885_8_, boolean p_72885_9_, boolean p_72885_10_) {
        Explosion explosion = new ExplosionRocket(world, entity, p_72885_2_, p_72885_4_, p_72885_6_, p_72885_8_);
        explosion.isFlaming = p_72885_9_;
        explosion.isSmoking = p_72885_10_;
        explosion.doExplosionA();
        explosion.doExplosionB(true);
        return explosion;
    }
}
