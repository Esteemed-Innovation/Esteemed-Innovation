package eiteam.esteemedinnovation.handler;

import eiteam.esteemedinnovation.Config;
import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.api.block.IDisguisableBlock;
import eiteam.esteemedinnovation.api.enhancement.UtilEnhancements;
import eiteam.esteemedinnovation.api.tool.ISteamTool;
import eiteam.esteemedinnovation.client.ClientProxy;
import eiteam.esteemedinnovation.init.blocks.PipeBlocks;
import eiteam.esteemedinnovation.init.items.armor.ArmorItems;
import eiteam.esteemedinnovation.init.items.firearms.FirearmItems;
import eiteam.esteemedinnovation.init.items.tools.GadgetItems;
import eiteam.esteemedinnovation.init.misc.integration.CrossMod;
import eiteam.esteemedinnovation.init.misc.integration.baubles.BaublesIntegration;
import eiteam.esteemedinnovation.item.ItemSteamCell;
import eiteam.esteemedinnovation.item.armor.exosuit.ItemExosuitArmor;
import eiteam.esteemedinnovation.misc.ItemStackUtility;
import eiteam.esteemedinnovation.network.CamoPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.MutablePair;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

import static eiteam.esteemedinnovation.init.items.armor.ExosuitUpgradeItems.Items.*;

public class GenericTickHandler {
    private static float zoom = 0.0F;
    ResourceLocation spyglassfiller = new ResourceLocation(EsteemedInnovation.MOD_ID + ":textures/gui/spyglassfiller.png");
    ResourceLocation spyglass = new ResourceLocation(EsteemedInnovation.MOD_ID + ":textures/gui/spyglassfiller.png");
    private boolean inUse = false;
    private boolean wasInUse = false;
    private float fov = 0;
    private float sensitivity = 0;
    private int zoomSettingOn = 0;
    private boolean lastPressingKey = false;
    private boolean isJumping = false;
    private int ticksSinceLastCellFill = 0;

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        World world = player.worldObj;
        boolean isServer = event.side == Side.SERVER;
        ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        ItemStack boots = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
        if (!isServer) {
            this.isJumping = Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown();
        }
        if (CrossMod.BAUBLES) {
            ticksSinceLastCellFill++;
            if (BaublesIntegration.checkForSteamCellFiller(player)) {
                if (ticksSinceLastCellFill >= 10) {
                    for (int i = 0; i < InventoryPlayer.getHotbarSize(); i++) {
                        ItemStack item = player.inventory.getStackInSlot(i);
                        if (item != null && item.getItem() instanceof ItemSteamCell &&
                          ItemSteamCell.chargeItems(player, false)) {
                            player.inventory.decrStackSize(i, 1);
                            ticksSinceLastCellFill = 0;
                            break;
                        }
                    }
                }
            } else {
                ticksSinceLastCellFill = -40;
            }
        }
        if (isJumping) {
            if (boots != null && boots.getItem() instanceof ItemExosuitArmor) {
                ItemExosuitArmor item = (ItemExosuitArmor) boots.getItem();
                if (item.hasUpgrade(boots, DOUBLE_JUMP.getItem()) &&
                  GenericEventHandler.hasPower(player, 15)) {
                    if (isServer) {
                        if (!boots.getTagCompound().hasKey("usedJump")) {
                            boots.getTagCompound().setBoolean("usedJump", false);
                        }
                        if (!boots.getTagCompound().hasKey("releasedSpace")) {
                            boots.getTagCompound().setBoolean("releasedSpace", false);
                        }
                    }
                    if (!player.onGround && boots.getTagCompound().getBoolean("releasedSpace") &&
                      !boots.getTagCompound().getBoolean("usedJump") &&
                      !player.capabilities.isFlying) {
                        if (isServer) {
                            boots.getTagCompound().setBoolean("usedJump", true);
                            GenericEventHandler.drainSteam(chest, 10);
                        }
                        player.motionY = 0.65D;
                        player.fallDistance = 0.0F;
                    }
                    if (isServer) {
                        boots.getTagCompound().setBoolean("releasedSpace", false);
                    }
                }
            }
            if (chest != null && chest.getItem() instanceof ItemExosuitArmor) {
                ItemExosuitArmor item = (ItemExosuitArmor) chest.getItem();
                if (item.hasUpgrade(chest, JETPACK.getItem()) &&
                  GenericEventHandler.hasPower(player, 5)) {
                    if (!player.onGround && !player.capabilities.isFlying) {
                        player.motionY += 0.06D;
                        player.fallDistance = 0.0F;
                        if (!isServer) {
                            double rotation = Math.toRadians(player.renderYawOffset);
                            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL,
                              player.posX + 0.4 * Math.sin(rotation + 0.9F),
                              player.posY - 1F, player.posZ - 0.4 * Math.cos(rotation + 0.9F),
                              0.0F, -1.0F, 0.0F);
                            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL,
                              player.posX + 0.4 * Math.sin(rotation - 0.9F),
                              player.posY - 1F, player.posZ - 0.4 * Math.cos(rotation - 0.9F),
                              0.0F, -1.0F, 0.0F);
                        } else {
                            GenericEventHandler.drainSteam(chest, Config.jetpackConsumption);
                        }
                    }
                }
                if (item.hasUpgrade(chest, PITON_DEPLOYER.getItem()) && isServer) {
                    if (chest.getTagCompound().hasKey("grappled") && chest.getTagCompound().getBoolean("grappled")) {
                        chest.getTagCompound().setBoolean("grappled", false);
                    }
                }
            }
        } else {
            if (boots != null && boots.getItem() instanceof ItemExosuitArmor) {
                ItemExosuitArmor item = (ItemExosuitArmor) boots.getItem();
                if (item.hasUpgrade(boots, DOUBLE_JUMP.getItem()) && !player.onGround && isServer) {
                    boots.getTagCompound().setBoolean("releasedSpace", true);
                }
            }
        }
    }

    private static Field itemInMainHandField;
    private static Field itemInOffHandField;

    static {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            FMLLog.info("[EI] Getting some fields from reflection for Tick Handling.");
            itemInMainHandField = GenericEventHandler.getField("itemStackMainHand", "field_187467_d", ItemRenderer.class);
            itemInOffHandField = GenericEventHandler.getField("itemStackOffHand", "field_187468_e", ItemRenderer.class);

            if (itemInMainHandField != null) {
                itemInMainHandField.setAccessible(true);
            }
            if (itemInOffHandField != null) {
                itemInOffHandField.setAccessible(true);
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void tickStart(TickEvent.ClientTickEvent event) {
        wasInUse = inUse;
        Minecraft mc = Minecraft.getMinecraft();
        inUse = false;
        if (event.side == Side.CLIENT && mc.thePlayer != null) {
            /*
             Prevents caching of ISteamTool ItemStacks in the ItemRenderer, so that the ItemOverrideList has access to
             the new NBT added in ItemSteamTool#onUpdate.
              */
            ItemStack mainHandStack = mc.thePlayer.getHeldItemMainhand();
            ItemStack offHandStack = mc.thePlayer.getHeldItemOffhand();
            if (mainHandStack != null && mainHandStack.getItem() instanceof ISteamTool) {
                try {
                    itemInMainHandField.set(mc.getItemRenderer(), mainHandStack);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if (offHandStack != null && offHandStack.getItem() instanceof ISteamTool) {
                try {
                    itemInOffHandField.set(mc.getItemRenderer(), offHandStack);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }


            if (mc.currentScreen == null || !(mc.currentScreen instanceof GuiMerchant)) {
                GenericEventHandler.lastViewVillagerGui = false;
            }
            EntityPlayer player = mc.thePlayer;
            ItemStack held = ItemStackUtility.getHeldItemStack(player);
            if (mc.gameSettings.keyBindUseItem.isKeyDown() && player.isSneaking() && held != null &&
              held.getItem() instanceof ItemBlock) {
                RayTraceResult pos = mc.objectMouseOver;
                if (pos != null) {
                    BlockPos blockPos = pos.getBlockPos();
                    TileEntity te = mc.theWorld.getTileEntity(blockPos);
                    if (mc.theWorld.getBlockState(blockPos).getBlock() == PipeBlocks.Blocks.BRASS_PIPE.getBlock() ||
                      (te != null && te instanceof IDisguisableBlock)) {
                        CamoPacket packet = new CamoPacket(blockPos);
                        EsteemedInnovation.channel.sendToServer(packet);
                    }
                }
            }

            ItemStack legs = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
            if (GenericEventHandler.hasPower(player, 1) && legs != null && legs.getItem() instanceof ItemExosuitArmor) {
                ItemExosuitArmor leggings = (ItemExosuitArmor) legs.getItem();
                if (player.worldObj.isRemote && leggings.hasUpgrade(legs, THRUSTERS.getItem())) {
                    if (!player.onGround && Math.abs(player.motionX) + Math.abs(player.motionZ) > 0.0F && !player.isInWater() && !(player.capabilities.isFlying)) {
                        double rotation = Math.toRadians(player.renderYawOffset + 90.0F);
                        double rotation2 = Math.toRadians(player.renderYawOffset + 270.0F);

                        player.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL,
                          player.posX + 0.5 * Math.sin(rotation),
                          player.posY - 1F, player.posZ - 0.5 * Math.cos(rotation),
                          player.motionX * -0.1F, 0, player.motionZ * -0.1F);
                        player.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL,
                          player.posX + 0.5 * Math.sin(rotation2),
                          player.posY - 1F, player.posZ - 0.5 * Math.cos(rotation2),
                          player.motionX * -0.1F, 0, player.motionZ * -0.1F);
                    }
                }
            }

            ItemStack hat = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
            Item monacle = ArmorItems.Items.MONOCLE.getItem();
            Item goggles = ArmorItems.Items.GOGGLES.getItem();
            boolean hasHat = hat != null && (hat.getItem() == monacle ||
              hat.getItem() == goggles || (hat.getItem() == ArmorItems.Items.EXOSUIT_HEADPIECE.getItem() &&
              (((ItemExosuitArmor) hat.getItem()).hasUpgrade(hat, goggles) ||
                ((ItemExosuitArmor) hat.getItem()).hasUpgrade(hat, monacle))));
            if (hasHat) {
                if (mc.gameSettings.thirdPersonView == 0) {
                    if (ClientProxy.keyBindings.get("monocle").isKeyDown() && !lastPressingKey) {
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
                    } else if (!ClientProxy.keyBindings.get("monocle").isKeyDown()) {
                        lastPressingKey = false;
                    }
                    inUse = zoomSettingOn != 0;
                }
            }
            ItemStack item = player.inventory.getStackInSlot(player.inventory.currentItem);
            if (item != null && item.getItem() == GadgetItems.Items.SPYGLASS.getItem()) {
                if (mc.gameSettings.thirdPersonView == 0) {
                    inUse = true;
                    this.renderTelescopeOverlay();
                }
            }
            if (!wasInUse && item != null && player.isHandActive() && item.getItem() == FirearmItems.Items.MUSKET.getItem() &&
              UtilEnhancements.getEnhancementFromItem(item) == GadgetItems.Items.SPYGLASS.getItem()) {
                boolean isShooting = false;
                if (item.getTagCompound() != null) {
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
            if (inUse && mc.gameSettings.keyBindAttack.isKeyDown() && zoom > 0F && item != null &&
              item.getItem() == GadgetItems.Items.SPYGLASS.getItem()) {
                zoom -= 1.0F;
                mc.gameSettings.fovSetting += 2.5F;
                mc.gameSettings.mouseSensitivity += 0.01F;

            }
            if (inUse && mc.gameSettings.keyBindUseItem.isKeyDown() && mc.gameSettings.fovSetting > 5F &&
              item != null && item.getItem() == GadgetItems.Items.SPYGLASS.getItem()) {
                zoom += 1.0F;
                mc.gameSettings.fovSetting -= 2.5F;
                mc.gameSettings.mouseSensitivity -= 0.01F;
            }
        }
    }

    @SubscribeEvent
    public void fallFast(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if (GenericEventHandler.hasPower(player, 1)) {
            ItemStack bootStack = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
            if (bootStack != null) {
                Item boots = bootStack.getItem();
                if (boots instanceof ItemExosuitArmor) {
                    ItemExosuitArmor bootArmor = (ItemExosuitArmor) boots;
                    if (bootArmor.hasUpgrade(bootStack, ANCHOR_HEELS.getItem())) {
                        double newY = player.isInWater() ? -0.6 : -1.1;
                        if (player.motionY < -0.3 && player.motionY != newY) {
                            player.motionY = newY;
                        }
                    }
                }
            }
        }
    }

    private int lavaTicks = 0;
    private int chargeTicks = 0;

    @SubscribeEvent
    public void deleteLavaAndExplodeCharges(TickEvent.WorldTickEvent event) {
        if (event.side.isClient()) {
            return;
        }
        lavaTicks++;
        chargeTicks++;

        Iterator<Map.Entry<MutablePair<Integer, BlockPos>, Integer>> lava = GenericEventHandler.quickLavaBlocks.entrySet().iterator();
        while (lava.hasNext()) {
            Map.Entry<MutablePair<Integer, BlockPos>, Integer> entry = lava.next();
            MutablePair<Integer, BlockPos> dimCoords = entry.getKey();
            BlockPos pos = dimCoords.getRight();
            int dim = dimCoords.getLeft();
            WorldServer worldServer = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(dim);
            int waitTicks = entry.getValue();
            if (lavaTicks == waitTicks) {
                worldServer.setBlockToAir(pos);
                lava.remove();
            }
        }

        Iterator<Map.Entry<MutablePair<EntityPlayer, BlockPos>, Integer>> charge = GenericEventHandler.charges.entrySet().iterator();
        while (charge.hasNext()) {
            Map.Entry<MutablePair<EntityPlayer, BlockPos>, Integer> entry = charge.next();
            MutablePair<EntityPlayer, BlockPos> playerCoords = entry.getKey();
            BlockPos pos = playerCoords.getRight();
            EntityPlayer player = playerCoords.getLeft();
            int dim = player.dimension;
            WorldServer worldServer = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(dim);
            int waitTicks = entry.getValue();
            if (chargeTicks >= waitTicks) {
                // Explosion is half the size of a TNT explosion.
                double x = (double) pos.getX();
                double y = (double) pos.getY();
                double z = (double) pos.getZ();
                worldServer.createExplosion(player, x, y, z, 2.0F, true);
                charge.remove();
            }
        }

        if (lavaTicks >= 30) {
            lavaTicks = 0;
        }
        if (chargeTicks >= GenericEventHandler.HARD_CHARGE_CAP) {
            chargeTicks = 0;
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