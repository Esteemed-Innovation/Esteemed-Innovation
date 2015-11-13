package flaxbeard.steamcraft.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.SteamcraftBlocks;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.api.block.IDisguisableBlock;
import flaxbeard.steamcraft.api.enhancement.UtilEnhancements;
import flaxbeard.steamcraft.item.ItemExosuitArmor;
import flaxbeard.steamcraft.packet.SteamcraftClientPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

public class SteamcraftTickHandler {
    private static float zoom = 0.0F;
    ResourceLocation spyglassfiller = new ResourceLocation("steamcraft:textures/gui/spyglassfiller.png");
    ResourceLocation spyglass = new ResourceLocation("steamcraft:textures/gui/spyglassfiller.png");
    private boolean inUse = false;
    private boolean wasInUse = false;
    private float fov = 0;
    private float sensitivity = 0;
    private int zoomSettingOn = 0;
    private boolean lastPressingKey = false;

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void tickStart(TickEvent.ClientTickEvent event) {
        wasInUse = inUse;
        Minecraft mc = Minecraft.getMinecraft();
        inUse = false;
        if (event.side == Side.CLIENT && mc.thePlayer != null) {
            if (mc.currentScreen == null || !(mc.currentScreen instanceof GuiMerchant)) {
                SteamcraftEventHandler.lastViewVillagerGui = false;
            }
            EntityPlayer player = mc.thePlayer;
            if (Mouse.isButtonDown(1) && player.isSneaking() && player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemBlock) {
                MovingObjectPosition pos = mc.objectMouseOver;
                if (pos != null) {
                    TileEntity te = mc.theWorld.getTileEntity(pos.blockX, pos.blockY, pos.blockZ);
                    if (mc.theWorld.getBlock(pos.blockX, pos.blockY, pos.blockZ) == SteamcraftBlocks.pipe || (mc.theWorld.getTileEntity(pos.blockX, pos.blockY, pos.blockZ) != null && mc.theWorld.getTileEntity(pos.blockX, pos.blockY, pos.blockZ) instanceof IDisguisableBlock)) {
                        SteamcraftClientPacketHandler.sendCamoPacket(player, pos);
                    }
                }
            }
            if (SteamcraftEventHandler.hasPower(player, 1)
                    && player.getEquipmentInSlot(2) != null
                    && player.getEquipmentInSlot(2).getItem() instanceof ItemExosuitArmor) {
                ItemExosuitArmor leggings = (ItemExosuitArmor) player.getEquipmentInSlot(2).getItem();
                if (player.worldObj.isRemote && leggings.hasUpgrade(player.getEquipmentInSlot(2), SteamcraftItems.thrusters)) {
                    if (!player.onGround && Math.abs(player.motionX) + Math.abs(player.motionZ) > 0.0F && !player.isInWater() && !(player.capabilities.isFlying)) {
                        double rotation = Math.toRadians(player.renderYawOffset + 90.0F);
                        double rotation2 = Math.toRadians(player.renderYawOffset + 270.0F);

                        player.worldObj.spawnParticle("smoke", player.posX + 0.5 * Math.sin(rotation), player.posY - 1F, player.posZ - 0.5 * Math.cos(rotation), player.motionX * -0.1F, 0, player.motionZ * -0.1F);
                        player.worldObj.spawnParticle("smoke", player.posX + 0.5 * Math.sin(rotation2), player.posY - 1F, player.posZ - 0.5 * Math.cos(rotation2), player.motionX * -0.1F, 0, player.motionZ * -0.1F);

                    }
                }
            }

            if (mc.gameSettings.keyBindJump.getIsKeyPressed()) {
                SteamcraftClientPacketHandler.sendSpacePacket(player);
                if (player != null) {
                    ItemStack armor = player.getCurrentArmor(2);
                    if (armor != null && armor.getItem() == SteamcraftItems.exoArmorBody) {
                        ItemExosuitArmor item = (ItemExosuitArmor) armor.getItem();
                        if (item.hasUpgrade(armor, SteamcraftItems.jetpack) && SteamcraftEventHandler.hasPower(player, 5)) {
                            if (!player.onGround && !player.capabilities.isFlying) {
                                player.motionY = player.motionY + 0.06D;
                                double rotation = Math.toRadians(player.renderYawOffset);

                                player.worldObj.spawnParticle("smoke", player.posX + 0.4 * Math.sin(rotation + 0.9F), player.posY - 1F, player.posZ - 0.4 * Math.cos(rotation + 0.9F), 0.0F, -1.0F, 0.0F);
                                player.worldObj.spawnParticle("smoke", player.posX + 0.4 * Math.sin(rotation - 0.9F), player.posY - 1F, player.posZ - 0.4 * Math.cos(rotation - 0.9F), 0.0F, -1.0F, 0.0F);
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
                                player.motionY = 0.65D;
                            }
                            armor2.stackTagCompound.setBoolean("releasedSpace", false);
                        }
                    }
                }
            } else {
                SteamcraftClientPacketHandler.sendNoSpacePacket(player);
                if (player != null) {
                    ItemStack armor2 = player.getCurrentArmor(0);
                    if (armor2 != null && armor2.getItem() == SteamcraftItems.exoArmorFeet) {
                        ItemExosuitArmor item = (ItemExosuitArmor) armor2.getItem();
                        if (item.hasUpgrade(armor2, SteamcraftItems.doubleJump) && !player.onGround) {
                            armor2.stackTagCompound.setBoolean("releasedSpace", true);
                        }
                    }
                }
            }
            ItemStack hat = player.inventory.armorInventory[3];
            boolean hasHat = hat != null && (hat.getItem() == SteamcraftItems.monacle || hat.getItem() == SteamcraftItems.goggles || (hat.getItem() == SteamcraftItems.exoArmorHead && (((ItemExosuitArmor) hat.getItem()).hasUpgrade(hat, SteamcraftItems.goggles) || ((ItemExosuitArmor) hat.getItem()).hasUpgrade(hat, SteamcraftItems.monacle))));
            if (hasHat) {
                if (mc.gameSettings.thirdPersonView == 0) {
                    if (Steamcraft.proxy.isKeyPressed() && !lastPressingKey) {
                        zoomSettingOn++;
                        zoomSettingOn = zoomSettingOn % 4;
                        switch (zoomSettingOn) {
                            case 0:
                                mc.gameSettings.fovSetting = fov;
                                mc.gameSettings.mouseSensitivity = sensitivity;
                                break;
                            case 1:
                                mc.gameSettings.fovSetting = fov;
                                mc.gameSettings.mouseSensitivity = sensitivity;
                                int i = 0;
                                while (Math.abs((mc.gameSettings.fovSetting - ((fov + 5F)) / 2.0F)) > 2.5F && i < 200) {
                                    zoom += 1.0F;
                                    mc.gameSettings.fovSetting -= 2.5F;
                                    mc.gameSettings.mouseSensitivity -= 0.01F;
                                    i++;
                                }
                                break;
                            case 2:
                                mc.gameSettings.fovSetting = fov;
                                mc.gameSettings.mouseSensitivity = sensitivity;
                                i = 0;
                                while (Math.abs((mc.gameSettings.fovSetting - ((fov + 5F)) / 5.0F)) > 2.5F && i < 200) {
                                    zoom += 1.0F;
                                    mc.gameSettings.fovSetting -= 2.5F;
                                    mc.gameSettings.mouseSensitivity -= 0.01F;
                                    i++;
                                }
                                break;
                            case 3:
                                mc.gameSettings.fovSetting = fov;
                                mc.gameSettings.mouseSensitivity = sensitivity;
                                i = 0;
                                while (Math.abs((mc.gameSettings.fovSetting - ((fov + 5F)) / 12.0F)) > 2.5F && i < 200) {
                                    zoom += 1.0F;
                                    mc.gameSettings.fovSetting -= 2.5F;
                                    mc.gameSettings.mouseSensitivity -= 0.01F;
                                    i++;
                                }
                                break;
                        }
                        lastPressingKey = true;
                    } else if (!Steamcraft.proxy.isKeyPressed()) {
                        lastPressingKey = false;
                    }
                    inUse = zoomSettingOn != 0;

                }
            }
            ItemStack item = player.inventory.getStackInSlot(player.inventory.currentItem);
            if (item != null && item.getItem() == SteamcraftItems.spyglass) {
                if (mc.gameSettings.thirdPersonView == 0) {
                    inUse = true;
                    this.renderTelescopeOverlay();
                }
            }
            if (!wasInUse && item != null && player.isUsingItem() && item.getItem() == SteamcraftItems.musket && UtilEnhancements.getEnhancementFromItem(item) == SteamcraftItems.spyglass) {
                boolean isShooting = false;
                if (item.stackTagCompound != null) {
                    NBTTagCompound nbt = item.getTagCompound();
                    if (nbt.getInteger("loaded") > 0) {
                        isShooting = true;
                    }
                }
                if (isShooting && mc.gameSettings.thirdPersonView == 0) {
                    inUse = true;
                    mc.gameSettings.fovSetting -= 30F;
                    mc.gameSettings.mouseSensitivity -= 0.3F;
                    this.renderTelescopeOverlay();
                }
            }

            if (!inUse && !wasInUse) {
                fov = mc.gameSettings.fovSetting;
                sensitivity = mc.gameSettings.mouseSensitivity;
            }
            if (!inUse && wasInUse) {
                mc.gameSettings.fovSetting = fov;
                mc.gameSettings.mouseSensitivity = sensitivity;
            }
            if (inUse && !wasInUse) {
                zoom = 0.0F;
            }
            if (inUse && mc.gameSettings.keyBindAttack.getIsKeyPressed() && zoom > 0F && item != null && item.getItem() == SteamcraftItems.spyglass) {
                zoom -= 1.0F;
                mc.gameSettings.fovSetting += 2.5F;
                mc.gameSettings.mouseSensitivity += 0.01F;

            }
            if (inUse && mc.gameSettings.keyBindUseItem.getIsKeyPressed() && mc.gameSettings.fovSetting > 5F && item != null && item.getItem() == SteamcraftItems.spyglass) {
                zoom += 1.0F;
                mc.gameSettings.fovSetting -= 2.5F;
                mc.gameSettings.mouseSensitivity -= 0.01F;
            }
        }
    }

    private void renderTelescopeOverlay() {
//		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
//        ScaledResolution var5 = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
//        int par1 = var5.getScaledWidth();
//        int par2 = var5.getScaledHeight();
//        int par3 = par1-par2;
//		GL11.glDisable(GL11.GL_DEPTH_TEST);
//        GL11.glDepthMask(false);
//        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        
//        GL11.glDisable(GL11.GL_ALPHA_TEST);
//        ITextureObject test = mc.renderEngine.getTexture(spyglass);
//        try {
//        	IResourceManager resourceManager = ObfuscationReflectionHelper.getPrivateValue(TextureManager.class, mc.renderEngine, "theResourceManager");
//			test.loadTexture(resourceManager);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//        GL11.glBindTexture(GL11.GL_TEXTURE_2D, test.getGlTextureId());
//        //mc.renderEngine.bindTexture(spyglass,test.getGlTextureId());
//        Tessellator var3 = Tessellator.instance;
//        var3.startDrawingQuads();
//        var3.addVertexWithUV(par3/2, par2, -90.0D, 0.0D, 1.0D);
//        var3.addVertexWithUV((par3/2)+par2, par2, -90.0D, 1.0D, 1.0D);
//        var3.addVertexWithUV((par3/2)+par2, 0.0D, -90.0D, 1.0D, 0.0D);
//        var3.addVertexWithUV(par3/2, 0.0D, -90.0D, 0.0D, 0.0D);
//        var3.draw();
//
//        mc.renderEngine.bindTexture(spyglassfiller);
//        var3 = Tessellator.instance;
//        var3.startDrawingQuads();
//        var3.addVertexWithUV(0, par2, -90.0D, 0.0D, 1.0D);
//        var3.addVertexWithUV(par3/2, par2, -90.0D, 1.0D, 1.0D);
//        var3.addVertexWithUV(par3/2, 0.0D, -90.0D, 1.0D, 0.0D);
//        var3.addVertexWithUV(0, 0.0D, -90.0D, 0.0D, 0.0D);
//        var3.draw();
//
//
//        mc.renderEngine.bindTexture(spyglassfiller);
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
