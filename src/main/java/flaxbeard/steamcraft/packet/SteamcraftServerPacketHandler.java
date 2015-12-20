package flaxbeard.steamcraft.packet;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.api.block.IDisguisableBlock;
import flaxbeard.steamcraft.entity.EntityRocket;
import flaxbeard.steamcraft.gui.ContainerSteamAnvil;
import flaxbeard.steamcraft.handler.SteamcraftEventHandler;
import flaxbeard.steamcraft.item.ItemExosuitArmor;
import flaxbeard.steamcraft.tile.TileEntitySteamHammer;
import flaxbeard.steamcraft.tile.TileEntitySteamPipe;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.io.IOException;

public class SteamcraftServerPacketHandler {

    public static void sendRocketJumpHackyPacket(EntityPlayerMP player, double xChange, double yChange, double zChange) {
        ByteBuf buf = Unpooled.buffer();
        ByteBufOutputStream out = new ByteBufOutputStream(buf);
        try {
            out.writeByte(2);
            out.writeInt(player.worldObj.provider.dimensionId);
            out.writeInt(player.getEntityId());
            out.writeDouble(xChange);
            out.writeDouble(yChange);
            out.writeDouble(zChange);

        } catch (IOException ignored) {}
        FMLProxyPacket packet = new FMLProxyPacket(buf, "steamcraft");
        Steamcraft.channel.sendTo(packet, player);
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendPipeConnectDisconnectPacket(int dimension, double x, double y, double z) {
        ByteBuf buf = Unpooled.buffer();
        ByteBufOutputStream out = new ByteBufOutputStream(buf);
        try {
            out.writeByte(1); //type?
            out.writeInt(dimension);
            out.writeByte(0); //packetID?
            out.writeDouble(x);
            out.writeDouble(y);
            out.writeDouble(z);
        } catch (IOException ignored) {}
        FMLProxyPacket packet = new FMLProxyPacket(buf, "steamcraft");
        Steamcraft.channel.sendToAllAround(packet, new TargetPoint(dimension, x, y, z, z));
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendExplodePacket(EntityRocket entityRocket) {
        ByteBuf buf = Unpooled.buffer();
        ByteBufOutputStream out = new ByteBufOutputStream(buf);
        try {
            out.writeByte(3);
            out.writeInt(entityRocket.worldObj.provider.dimensionId);
            out.writeDouble(entityRocket.posX);
            out.writeDouble(entityRocket.posY);
            out.writeDouble(entityRocket.posZ);
            out.writeFloat(entityRocket.explosionSize);

        } catch (IOException ignored) {}
        FMLProxyPacket packet = new FMLProxyPacket(buf, "steamcraft");
        Steamcraft.channel.sendToDimension(packet, entityRocket.worldObj.provider.dimensionId);
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleSpacePacket(ByteBufInputStream dat, World world) {
        try {
            int id = dat.readInt();
            Entity entity = world.getEntityByID(id);
            if (entity == null || !(entity instanceof EntityPlayer)) {
                return;
            }
            EntityPlayer player = (EntityPlayer) entity;
            if (player.getEquipmentInSlot(3) != null && player.getEquipmentInSlot(3).getItem() instanceof ItemExosuitArmor) {
                ItemExosuitArmor chest = (ItemExosuitArmor) player.getEquipmentInSlot(3).getItem();
                if (chest.hasUpgrade(player.getEquipmentInSlot(3), SteamcraftItems.pitonDeployer)) {
                    if (player.getEquipmentInSlot(3).stackTagCompound.hasKey("grappled") && player.getEquipmentInSlot(3).stackTagCompound.getBoolean("grappled")) {
                        player.getEquipmentInSlot(3).stackTagCompound.setBoolean("grappled", false);
                    }
                }
            }
            ItemStack armor = player.getCurrentArmor(2);
            if (armor != null && armor.getItem() == SteamcraftItems.exoArmorBody) {
                ItemExosuitArmor item = (ItemExosuitArmor) armor.getItem();
                if (item.hasUpgrade(armor, SteamcraftItems.jetpack) && SteamcraftEventHandler.hasPower(player, 5)) {
                    if (!player.onGround && !player.capabilities.isFlying) {
                        player.motionY = player.motionY + 0.06D;
                        player.fallDistance = 0.0F;
                        SteamcraftEventHandler.drainSteam(player.getCurrentArmor(2), Config.jetpackConsumption);
                    }
                }
            }
            ItemStack armor2 = player.getCurrentArmor(0);
            if (armor2 != null && armor2.getItem() == SteamcraftItems.exoArmorFeet) {
                ItemExosuitArmor item = (ItemExosuitArmor) armor2.getItem();
                if (item.hasUpgrade(armor2, SteamcraftItems.doubleJump) && SteamcraftEventHandler.hasPower(player, 15)) {
                    if (!armor2.stackTagCompound.hasKey("usedJump")) {
                        armor2.stackTagCompound.setBoolean("usedJump", false);
                    }
                    if (!armor2.stackTagCompound.hasKey("releasedSpace")) {
                        armor2.stackTagCompound.setBoolean("releasedSpace", false);
                    }
                    if (!player.onGround && armor2.stackTagCompound.getBoolean("releasedSpace") && !armor2.stackTagCompound.getBoolean("usedJump") && !player.capabilities.isFlying) {
                        armor2.stackTagCompound.setBoolean("usedJump", true);
                        player.motionY = player.motionY + 0.3D;
                        player.fallDistance = 0.0F;
                        SteamcraftEventHandler.drainSteam(player.getCurrentArmor(2), 10);
                    }
                    armor2.stackTagCompound.setBoolean("releasedSpace", false);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
		}
    }

    private void handleCamoPacket(ByteBufInputStream dat, World world) {
        try {
            int id = dat.readInt();
            Entity entity = world.getEntityByID(id);
            if (entity == null || !(entity instanceof EntityPlayer)) {
                return;
            }
            EntityPlayer player = (EntityPlayer) entity;
            int x = dat.readInt();
            int y = dat.readInt();
            int z = dat.readInt();
            if (player.getHeldItem() != null) {
                Block block = Block.getBlockFromItem(player.getHeldItem().getItem());
                TileEntity tile = world.getTileEntity(x, y, z);
                if (!(block instanceof BlockContainer) && !(block instanceof ITileEntityProvider) && (block.getRenderType() == 0 || block.getRenderType() == 39 || block.getRenderType() == 31) && block.isOpaqueCube() && (block.renderAsNormalBlock() || (block == Blocks.glass && tile instanceof TileEntitySteamPipe))) {
                    if (!world.isRemote && tile instanceof TileEntitySteamPipe) {
                        TileEntitySteamPipe pipe = ((TileEntitySteamPipe) tile);
                        if (!(pipe.disguiseBlock == block && pipe.disguiseMeta == player.getHeldItem().getItem()
                          .getMetadata(player.getHeldItem().getItemDamage()))) {
                            if (pipe.disguiseBlock != Blocks.air && !player.capabilities.isCreativeMode) {
                                EntityItem entityItem = new EntityItem(world, player.posX, player.posY, player.posZ, new ItemStack(pipe.disguiseBlock, 1, pipe.disguiseMeta));
                                world.spawnEntityInWorld(entityItem);
                                pipe.disguiseBlock = null;
                            }

                            pipe.disguiseBlock = block;
                            if (!player.capabilities.isCreativeMode) {
                                player.inventory.getCurrentItem().stackSize--;
                                player.inventoryContainer.detectAndSendChanges();
                            }
                            world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), block.stepSound.func_150496_b(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);

                            pipe.disguiseMeta = player.getHeldItem().getItem()
                              .getMetadata(player.getHeldItem().getItemDamage());
                            world.markBlockForUpdate(x, y, z);
                        }
                    }
                    if (!world.isRemote && tile instanceof IDisguisableBlock) {
                        IDisguisableBlock pipe = ((IDisguisableBlock) tile);
                        if (!(pipe.getDisguiseBlock() == block && pipe.getDisguiseMeta() == player.getHeldItem().getItem()
                          .getMetadata(player.getHeldItem().getItemDamage()))) {
                            if (pipe.getDisguiseBlock() != Blocks.air && !player.capabilities.isCreativeMode) {
                                EntityItem entityItem = new EntityItem(world, player.posX, player.posY, player.posZ, new ItemStack(pipe.getDisguiseBlock(), 1, pipe.getDisguiseMeta()));
                                world.spawnEntityInWorld(entityItem);
                                pipe.setDisguiseBlock(null);
                            }

                            pipe.setDisguiseBlock(block);
                            if (!player.capabilities.isCreativeMode) {
                                player.inventory.getCurrentItem().stackSize--;
                                player.inventoryContainer.detectAndSendChanges();
                            }
                            world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), block.stepSound.func_150496_b(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);

                            pipe.setDisguiseMeta(
                              player.getHeldItem().getItem().getMetadata(player.getHeldItem().getItemDamage()));
                            world.markBlockForUpdate(x, y, z);
                        }
                    }
                }
            }
        } catch (IOException ignored) {}
    }

    private void handleDRPacket(ByteBufInputStream dat, World world) {
        try {
            int id = dat.readInt();
            Entity entity = world.getEntityByID(id);
            if (entity == null || !(entity instanceof EntityPlayer)) {
                return;
            }
            int x = dat.readInt();
            int y = dat.readInt();
            int z = dat.readInt();
            int subHit = dat.readInt();
            if (world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof TileEntitySteamPipe) {
                ((TileEntitySteamPipe) world.getTileEntity(x, y, z)).connectDisconnect(world, x, y, z, subHit);
            }
        } catch (IOException ignored) {}
    }

    private void handleNoSpacePacket(ByteBufInputStream dat, World world) {
        try {
            int id = dat.readInt();
            Entity entity = world.getEntityByID(id);
            if (entity == null || !(entity instanceof EntityPlayer)) {
                return;
            }
            EntityPlayer player = (EntityPlayer) entity;
            ItemStack armor2 = player.getCurrentArmor(0);
            if (armor2 != null && armor2.getItem() == SteamcraftItems.exoArmorFeet) {
                ItemExosuitArmor item = (ItemExosuitArmor) armor2.getItem();
                if (item.hasUpgrade(armor2, SteamcraftItems.doubleJump) && !player.onGround) {
                    armor2.stackTagCompound.setBoolean("releasedSpace", true);
                }
            }
        } catch (IOException e) {
			e.printStackTrace();
		}
    }

    private void handleItemNamePacket(ByteBufInputStream dat, World world) {
        try {
            int x = dat.readInt();
            int y = dat.readInt();
            int z = dat.readInt();
            String s = dat.readUTF();
            int id = dat.readInt();
            Entity entity = world.getEntityByID(id);
            if (entity == null || !(entity instanceof EntityPlayer)) {
                return;
            }
            EntityPlayer player = (EntityPlayer) entity;
            TileEntitySteamHammer hammer = (TileEntitySteamHammer) world.getTileEntity(x, y, z);
            if (hammer != null) {
                hammer.itemName = s;
                ContainerSteamAnvil anvil = (ContainerSteamAnvil) player.openContainer;
                anvil.updateItemName(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
		}
    }

    private void handleGrapplePacket(ByteBufInputStream dat, World world) {
        try {
            int id = dat.readInt();
            Entity entity = world.getEntityByID(id);
            if (entity == null || !(entity instanceof EntityPlayer)) {
                return;
            }
            EntityPlayer player = (EntityPlayer) entity;
            int x = dat.readInt();
            int y = dat.readInt();
            int z = dat.readInt();
            double playerX = dat.readDouble();
            double playerY = dat.readDouble();
            double playerZ = dat.readDouble();

            //((EntityPlayerMP)player).playerNetServerHandler.setPlayerLocation(playerX, playerY-1, playerZ, player.rotationYaw, player.rotationPitch);
            player.getEquipmentInSlot(3).stackTagCompound.setFloat("x", (float) playerX);
            player.getEquipmentInSlot(3).stackTagCompound.setFloat("z", (float) playerZ);
            player.getEquipmentInSlot(3).stackTagCompound.setFloat("y", (float) playerY);
            player.getEquipmentInSlot(3).stackTagCompound.setInteger("blockX", x);
            player.getEquipmentInSlot(3).stackTagCompound.setInteger("blockY", y);
            player.getEquipmentInSlot(3).stackTagCompound.setInteger("blockZ", z);

            player.getEquipmentInSlot(3).stackTagCompound.setBoolean("grappled", true);
            player.motionX = 0.0F;
            player.motionY = 0.0F;
            player.motionZ = 0.0F;
            player.fallDistance = 0.0F;
        } catch (IOException e) {
            e.printStackTrace();
		}
    }

    @SubscribeEvent
    public void onServerPacket(ServerCustomPacketEvent event) {
        ByteBufInputStream bbis = new ByteBufInputStream(event.packet.payload());
        int dimension;
        byte packetID;

        try {
            packetID = bbis.readByte();
            dimension = bbis.readInt();
            World world = DimensionManager.getWorld(dimension);

            switch (packetID) {
                case 0: {
                    this.handleSpacePacket(bbis, world);
                }
                case 1: {
                    this.handleItemNamePacket(bbis, world);
                }
                case 2: {
                    this.handleNoSpacePacket(bbis, world);
                }
                case 3: {
                    this.handleCamoPacket(bbis, world);
                }
                case 4: {
                    this.handleDRPacket(bbis, world);
                }
                case 5: {
                    this.handleGrapplePacket(bbis, world);
                }
            }

            bbis.close();
        } catch (IOException e) {
            e.printStackTrace();
		}
    }
}
