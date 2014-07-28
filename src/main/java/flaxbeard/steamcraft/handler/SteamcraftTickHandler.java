package flaxbeard.steamcraft.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.api.enhancement.UtilEnhancements;
import flaxbeard.steamcraft.item.ItemExosuitArmor;
import flaxbeard.steamcraft.packet.SteamcraftClientPacketHandler;

public class SteamcraftTickHandler {
	private boolean inUse = false;
	private boolean wasInUse = false;
	private float fov = 0;
	private float sensitivity = 0;
	private static float zoom = 0.0F;
	ResourceLocation spyglassfiller = new ResourceLocation("steamcraft:textures/gui/spyglassfiller.png");
	ResourceLocation spyglass = new ResourceLocation("steamcraft:textures/gui/spyglassfiller.png");
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void tickStart(TickEvent.ClientTickEvent event) {
		wasInUse = inUse;
		inUse = false;
		if(event.side == Side.CLIENT && Minecraft.getMinecraft().thePlayer != null){
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			
			if (SteamcraftEventHandler.hasPower(player, 1) 
					&& player.getEquipmentInSlot(2) != null 
					&& player.getEquipmentInSlot(2).getItem() instanceof ItemExosuitArmor) {
				ItemExosuitArmor chest = (ItemExosuitArmor) player.getEquipmentInSlot(2).getItem();
				if (player.worldObj.isRemote && chest.hasUpgrade(player.getEquipmentInSlot(2), SteamcraftItems.thrusters)) {
					if (!player.onGround && Math.abs(player.motionX) + Math.abs(player.motionZ) > 0.0F && !player.isInWater() && !(player.capabilities.isFlying)) {
						double rotation = Math.toRadians(player.renderYawOffset+90.0F);
						double rotation2 = Math.toRadians(player.renderYawOffset+270.0F);

						player.worldObj.spawnParticle("smoke", player.posX+0.5*Math.sin(rotation), player.posY-1F, player.posZ-0.5*Math.cos(rotation), player.motionX*-0.1F, 0, player.motionZ*-0.1F);
						player.worldObj.spawnParticle("smoke", player.posX+0.5*Math.sin(rotation2), player.posY-1F, player.posZ-0.5*Math.cos(rotation2), player.motionX*-0.1F, 0, player.motionZ*-0.1F);

					}
				}
			}
			
			if (Minecraft.getMinecraft().gameSettings.keyBindJump.getIsKeyPressed()) {
				SteamcraftClientPacketHandler.sendSpacePacket(player);
				if (player != null) {
					ItemStack armor = player.getCurrentArmor(2);
					if (armor != null && armor.getItem() == SteamcraftItems.exoArmorBody) {
						ItemExosuitArmor item = (ItemExosuitArmor) armor.getItem();
						if (item.hasUpgrade(armor, SteamcraftItems.jetpack) && SteamcraftEventHandler.hasPower(player, 5)) {
							if (!player.onGround && !player.capabilities.isFlying) {
								player.motionY=player.motionY+0.06D;
								double rotation = Math.toRadians(player.renderYawOffset);
								
								player.worldObj.spawnParticle("smoke", player.posX+0.4*Math.sin(rotation+0.9F), player.posY-1F, player.posZ-0.4*Math.cos(rotation+0.9F), 0.0F, -1.0F, 0.0F);
								player.worldObj.spawnParticle("smoke", player.posX+0.4*Math.sin(rotation-0.9F), player.posY-1F, player.posZ-0.4*Math.cos(rotation-0.9F), 0.0F, -1.0F, 0.0F);
							}
						}
					}
					
//					ItemStack armor2 = player.getCurrentArmor(0);
//					if (armor2 != null && armor2.getItem() == SteamcraftItems.exoArmorFeet) {
//						ItemExosuitArmor item = (ItemExosuitArmor) armor2.getItem();
//						if (item.hasUpgrade(armor2, SteamcraftItems.doubleJump) && SteamcraftEventHandler.hasPower(player, 15)) {
//							if (!armor2.stackTagCompound.hasKey("usedJump")) {
//								armor2.stackTagCompound.setBoolean("usedJump", false);
//							}
//							if (!armor2.stackTagCompound.hasKey("releasedSpace")) {
//								armor2.stackTagCompound.setBoolean("releasedSpace", false);
//							}
//							if (!player.onGround && armor2.stackTagCompound.getBoolean("releasedSpace") && !armor2.stackTagCompound.getBoolean("usedJump") && !player.capabilities.isFlying) {
//								armor2.stackTagCompound.setBoolean("usedJump", true);
//								player.motionY=player.motionY+0.3D;
//							}
//							armor2.stackTagCompound.setBoolean("releasedSpace", false);
//						}
//					}
				}
			}
			else
			{
//				SteamcraftClientPacketHandler.sendNoSpacePacket(player);
//				if (player != null) {
//					ItemStack armor2 = player.getCurrentArmor(0);
//					if (armor2 != null && armor2.getItem() == SteamcraftItems.exoArmorFeet) {
//						ItemExosuitArmor item = (ItemExosuitArmor) armor2.getItem();
//						if (item.hasUpgrade(armor2, SteamcraftItems.doubleJump) && !player.onGround) {
//							armor2.stackTagCompound.setBoolean("releasedSpace", true);
//						}
//					}
//				}
			}
			
			ItemStack item = player.inventory.getStackInSlot(player.inventory.currentItem);
			if (item != null && item.getItem() == SteamcraftItems.spyglass) {
				if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
					inUse = true;
					this.renderTelescopeOverlay();
				}
			}
			if (!wasInUse && item != null && player.isUsingItem() && item.getItem() == SteamcraftItems.musket && UtilEnhancements.getEnhancementFromItem(item) == SteamcraftItems.spyglass) {
				boolean isShooting = false;
				if (item.stackTagCompound != null) {
			        NBTTagCompound nbt = item.getTagCompound();
			        if (nbt.getInteger("loaded") > 0)
			        {
			            isShooting = true;
			        }
				}
				if (isShooting && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
					inUse = true;
					Minecraft.getMinecraft().gameSettings.fovSetting -= 30F;
					Minecraft.getMinecraft().gameSettings.mouseSensitivity -= 0.3F;
					this.renderTelescopeOverlay();
				}
			}
		}
		if (!inUse && !wasInUse) {
			fov = Minecraft.getMinecraft().gameSettings.fovSetting;
			sensitivity = Minecraft.getMinecraft().gameSettings.mouseSensitivity;
		}
		if (!inUse && wasInUse) {
			Minecraft.getMinecraft().gameSettings.fovSetting = fov;
			Minecraft.getMinecraft().gameSettings.mouseSensitivity = sensitivity;
		}
		if (inUse && !wasInUse) {
			this.zoom = 0.0F;
		}
		if (inUse && Minecraft.getMinecraft().gameSettings.keyBindAttack.getIsKeyPressed() && zoom > 0F) {
			this.zoom-=1.0F;
			Minecraft.getMinecraft().gameSettings.fovSetting += 2.5F;
			Minecraft.getMinecraft().gameSettings.mouseSensitivity += 0.01F;

		}
		if (inUse && Minecraft.getMinecraft().gameSettings.keyBindUseItem.getIsKeyPressed() && Minecraft.getMinecraft().gameSettings.fovSetting > 5F) {
			this.zoom+=1.0F;
			Minecraft.getMinecraft().gameSettings.fovSetting -= 2.5F;
			Minecraft.getMinecraft().gameSettings.mouseSensitivity -= 0.01F;
		}
	}
	
	private void renderTelescopeOverlay() {
//		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
//        ScaledResolution var5 = new ScaledResolution(Minecraft.getMinecraft().gameSettings, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
//        int par1 = var5.getScaledWidth();
//        int par2 = var5.getScaledHeight();
//        int par3 = par1-par2;
//		GL11.glDisable(GL11.GL_DEPTH_TEST);
//        GL11.glDepthMask(false);
//        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        
//        GL11.glDisable(GL11.GL_ALPHA_TEST);
//        ITextureObject test = Minecraft.getMinecraft().renderEngine.getTexture(spyglass);
//        try {
//        	IResourceManager resourceManager = ObfuscationReflectionHelper.getPrivateValue(TextureManager.class, Minecraft.getMinecraft().renderEngine, "theResourceManager");
//			test.loadTexture(resourceManager);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//        GL11.glBindTexture(GL11.GL_TEXTURE_2D, test.getGlTextureId());
//        //Minecraft.getMinecraft().renderEngine.bindTexture(spyglass,test.getGlTextureId());
//        Tessellator var3 = Tessellator.instance;
//        var3.startDrawingQuads();
//        var3.addVertexWithUV(par3/2, par2, -90.0D, 0.0D, 1.0D);
//        var3.addVertexWithUV((par3/2)+par2, par2, -90.0D, 1.0D, 1.0D);
//        var3.addVertexWithUV((par3/2)+par2, 0.0D, -90.0D, 1.0D, 0.0D);
//        var3.addVertexWithUV(par3/2, 0.0D, -90.0D, 0.0D, 0.0D);
//        var3.draw();
//
//        Minecraft.getMinecraft().renderEngine.bindTexture(spyglassfiller);
//        var3 = Tessellator.instance;
//        var3.startDrawingQuads();
//        var3.addVertexWithUV(0, par2, -90.0D, 0.0D, 1.0D);
//        var3.addVertexWithUV(par3/2, par2, -90.0D, 1.0D, 1.0D);
//        var3.addVertexWithUV(par3/2, 0.0D, -90.0D, 1.0D, 0.0D);
//        var3.addVertexWithUV(0, 0.0D, -90.0D, 0.0D, 0.0D);
//        var3.draw();
//
//
//        Minecraft.getMinecraft().renderEngine.bindTexture(spyglassfiller);
//        var3 = Tessellator.instance;
//        var3.startDrawingQuads();
//        var3.addVertexWithUV((par3/2)+par2, par2, -90.0D, 0.0D, 1.0D);
//        var3.addVertexWithUV(par1, par2, -90.0D, 1.0D, 1.0D);
//        var3.addVertexWithUV(par1, 0.0D, -90.0D, 1.0D, 0.0D);
//        var3.addVertexWithUV((par3/2)+par2, 0.0D, -90.0D, 0.0D, 0.0D);
//        var3.draw();
//
//        GL11.glDepthMask(true);
//        GL11.glEnable(GL11.GL_DEPTH_TEST);
//        GL11.glEnable(GL11.GL_ALPHA_TEST);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        GL11.glPopAttrib();
	}

}
