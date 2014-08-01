package flaxbeard.steamcraft.packet;

import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.gui.ContainerSteamAnvil;
import flaxbeard.steamcraft.handler.SteamcraftEventHandler;
import flaxbeard.steamcraft.item.ItemExosuitArmor;
import flaxbeard.steamcraft.tile.TileEntityBoiler;
import flaxbeard.steamcraft.tile.TileEntitySteamHammer;
import flaxbeard.steamcraft.tile.TileEntitySteamPipe;
import io.netty.buffer.ByteBufInputStream;

import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
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
					if (item.hasUpgrade(armor, SteamcraftItems.jetpack) && SteamcraftEventHandler.hasPower(player, 5)) {
						if (!player.onGround && !player.capabilities.isFlying) {
							player.motionY=player.motionY+0.06D;
							player.fallDistance = 0.0F;
							player.getCurrentArmor(2).damageItem(1, player);
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

							player.motionY=player.motionY+0.3D;
							double rotation = Math.toRadians(player.renderYawOffset);
							player.fallDistance = 0.0F;
							player.getCurrentArmor(2).damageItem(10, player);

						}
						armor2.stackTagCompound.setBoolean("releasedSpace", false);
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
 	}
	
	private void handleCamoPacket(ByteBufInputStream dat, World world)
 	{
		try {
			int id = dat.readInt();
			EntityPlayer player = (EntityPlayer) world.getEntityByID(id);
			if (player != null) {
				int x = dat.readInt();
				int y = dat.readInt();
				int z = dat.readInt();
				Block block = Block.getBlockFromItem(player.getHeldItem().getItem());
				if (!(block instanceof BlockContainer) && !(block instanceof ITileEntityProvider) && (block.renderAsNormalBlock())) {
					TileEntity tile = world.getTileEntity(x, y, z);

					if (!world.isRemote && tile instanceof TileEntitySteamPipe) {
						TileEntitySteamPipe pipe = ((TileEntitySteamPipe)tile);
						if (!(pipe.disguiseBlock == block && pipe.disguiseMeta == ((ItemBlock)player.getHeldItem().getItem()).getMetadata(player.getHeldItem().getItemDamage()))) {
							if (pipe.disguiseBlock != Blocks.air && !player.capabilities.isCreativeMode) {
								EntityItem entityItem = new EntityItem(world,player.posX, player.posY, player.posZ, new ItemStack(pipe.disguiseBlock,1,pipe.disguiseMeta));
								world.spawnEntityInWorld(entityItem);
								pipe.disguiseBlock = null;
							}
	
							pipe.disguiseBlock = block;
							if (!player.capabilities.isCreativeMode) {
								player.inventory.getCurrentItem().stackSize--;
								player.inventoryContainer.detectAndSendChanges();
							}
			                world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), block.stepSound.func_150496_b(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
	
							pipe.disguiseMeta = ((ItemBlock)player.getHeldItem().getItem()).getMetadata(player.getHeldItem().getItemDamage());
							world.markBlockForUpdate(x, y, z);
						}
					}
					if (!world.isRemote && tile instanceof TileEntityBoiler) {
						TileEntityBoiler pipe = ((TileEntityBoiler)tile);
						if (!(pipe.disguiseBlock == block && pipe.disguiseMeta == ((ItemBlock)player.getHeldItem().getItem()).getMetadata(player.getHeldItem().getItemDamage()))) {
							if (pipe.disguiseBlock != Blocks.air && !player.capabilities.isCreativeMode) {
								EntityItem entityItem = new EntityItem(world,player.posX, player.posY, player.posZ, new ItemStack(pipe.disguiseBlock,1,pipe.disguiseMeta));
								world.spawnEntityInWorld(entityItem);
								pipe.disguiseBlock = null;
							}
	
							pipe.disguiseBlock = block;
							if (!player.capabilities.isCreativeMode) {
								player.inventory.getCurrentItem().stackSize--;
								player.inventoryContainer.detectAndSendChanges();
							}
			                world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), block.stepSound.func_150496_b(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
	
							pipe.disguiseMeta = ((ItemBlock)player.getHeldItem().getItem()).getMetadata(player.getHeldItem().getItemDamage());
							world.markBlockForUpdate(x, y, z);
						}
					}
				}
			}
		}
		catch (Exception e) {
			return;
		}
 	}
	
	private void handleNoSpacePacket(ByteBufInputStream dat, World world)
 	{
//		try {
//			int id = dat.readInt();
//			EntityPlayer player = (EntityPlayer) world.getEntityByID(id);
//			if (player != null) {
//				ItemStack armor2 = player.getCurrentArmor(0);
//				if (armor2 != null && armor2.getItem() == SteamcraftItems.exoArmorFeet) {
//					ItemExosuitArmor item = (ItemExosuitArmor) armor2.getItem();
//					if (item.hasUpgrade(armor2, SteamcraftItems.doubleJump) && !player.onGround) {
//						armor2.stackTagCompound.setBoolean("releasedSpace", true);
//					}
//				}
//			}
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//			return;
//		}
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
            if (packetID == 2) {
            	this.handleNoSpacePacket(bbis, world);
            }
            if (packetID == 1) {
            	this.handleItemNamePacket(bbis, world);
            }
            if (packetID == 3) {
            	this.handleCamoPacket(bbis, world);
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
