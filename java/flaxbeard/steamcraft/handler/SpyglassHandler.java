package flaxbeard.steamcraft.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.api.UtilEnhancements;

public class SpyglassHandler {
	private boolean inUse = false;
	private boolean wasInUse = false;
	private float fov = 0;
	private float sensitivity = 0;
	ResourceLocation spyglassfiller = new ResourceLocation("steamcraft:textures/gui/spyglassfiller.png");
	ResourceLocation spyglass = new ResourceLocation("steamcraft:textures/gui/spyglassfiller.png");
	@SubscribeEvent
	public void tickStart(TickEvent.ClientTickEvent event) {
		wasInUse = inUse;
		inUse = false;
		if(Minecraft.getMinecraft().thePlayer != null){
			ItemStack item = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(Minecraft.getMinecraft().thePlayer.inventory.currentItem);
			if (item != null && item.getItem() == SteamcraftItems.spyglass) {
				if (Minecraft.getMinecraft().thePlayer.isUsingItem() && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
					inUse = true;
					Minecraft.getMinecraft().gameSettings.fovSetting = -1.7F;
					Minecraft.getMinecraft().gameSettings.mouseSensitivity = 0.0F;
					this.renderTelescopeOverlay();
				}
			}
			if (!wasInUse && item != null && item.getItem() == SteamcraftItems.musket && UtilEnhancements.getEnhancementFromItem(item) == SteamcraftItems.spyglass) {
				boolean isShooting = false;
				if (item.stackTagCompound != null) {
			        NBTTagCompound nbt = item.getTagCompound();
			        if (nbt.getInteger("loaded") > 0)
			        {
			            isShooting = true;
			        }
				}
				if (Minecraft.getMinecraft().thePlayer.isUsingItem() && isShooting && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
					inUse = true;
					Minecraft.getMinecraft().gameSettings.fovSetting = -0.85F;
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
