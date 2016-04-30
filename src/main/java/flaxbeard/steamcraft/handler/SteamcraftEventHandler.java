package flaxbeard.steamcraft.handler;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.SteamcraftBlocks;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.api.*;
import flaxbeard.steamcraft.api.block.IDisguisableBlock;
import flaxbeard.steamcraft.api.event.AnimalTradeEvent;
import flaxbeard.steamcraft.api.exosuit.ExosuitPlate;
import flaxbeard.steamcraft.api.exosuit.UtilPlates;
import flaxbeard.steamcraft.api.steamnet.SteamNetworkRegistry;
import flaxbeard.steamcraft.api.steamnet.data.SteamNetworkData;
import flaxbeard.steamcraft.api.tool.ISteamTool;
import flaxbeard.steamcraft.api.tool.UtilSteamTool;
import flaxbeard.steamcraft.api.util.SPLog;
import flaxbeard.steamcraft.entity.EntityCanisterItem;
import flaxbeard.steamcraft.entity.ExtendedPropertiesMerchant;
import flaxbeard.steamcraft.entity.ExtendedPropertiesPlayer;
import flaxbeard.steamcraft.entity.ExtendedPropertiesVillager;
import flaxbeard.steamcraft.gui.GuiSteamcraftBook;
import flaxbeard.steamcraft.integration.BloodMagicIntegration;
import flaxbeard.steamcraft.integration.BotaniaIntegration;
import flaxbeard.steamcraft.integration.CrossMod;
import flaxbeard.steamcraft.integration.EnchiridionIntegration;
import flaxbeard.steamcraft.integration.baubles.BaublesIntegration;
import flaxbeard.steamcraft.item.ItemExosuitArmor;
import flaxbeard.steamcraft.item.ItemSteamcraftBook;
import flaxbeard.steamcraft.item.firearm.ItemFirearm;
import flaxbeard.steamcraft.item.firearm.ItemRocketLauncher;
import flaxbeard.steamcraft.item.tool.steam.*;
import flaxbeard.steamcraft.misc.FrequencyMerchant;
import flaxbeard.steamcraft.misc.DrillHeadMaterial;
import flaxbeard.steamcraft.misc.OreDictHelper;
import flaxbeard.steamcraft.tile.TileEntitySmasher;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.*;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.sound.PlaySoundEvent17;
import net.minecraftforge.common.ISpecialArmor.ArmorProperties;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.MutablePair;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SteamcraftEventHandler {
    private static final UUID uuid = UUID.fromString("bbd786a9-611f-4c31-88ad-36dc9da3e15c");
    private static final AttributeModifier exoBoost = new AttributeModifier(uuid, "EXOMOD", 0.2D, 2).setSaved(true);
    private static final UUID uuid2 = UUID.fromString("33235dc2-bf3d-40e4-ae0e-78037c7535e6");
    private static final AttributeModifier exoBoostBad = new AttributeModifier(uuid2, "EXOMODBAD", -0.2D, 2).setSaved(true);
    private static final UUID uuid3 = UUID.fromString("33235dc2-bf3d-40e4-ae0e-78037c7535e7");
    private static final AttributeModifier exoSwimBoost = new AttributeModifier(uuid3, "EXOSWIMBOOST", 1.0D, 2).setSaved(true);
    private static final ResourceLocation icons = new ResourceLocation("steamcraft:textures/gui/icons.png");
    public static boolean lastViewVillagerGui = false;
    public static int use = -1;
    boolean lastWearing = false;
    boolean worldStartUpdate = false;
    private SPLog log = Steamcraft.log;
    private static boolean isShiftDown;

    public static void drainSteam(ItemStack stack, int amount) {
        if (stack != null) {
            if (!stack.hasTagCompound()) {
                stack.setTagCompound(new NBTTagCompound());
            }
            if (!stack.stackTagCompound.hasKey("steamFill")) {
                stack.stackTagCompound.setInteger("steamFill", 0);
            }
            int fill = stack.stackTagCompound.getInteger("steamFill");
            fill = Math.max(0, fill - amount);
            stack.stackTagCompound.setInteger("steamFill", fill);
        }
    }

//	@SubscribeEvent
//	public void handleMobDrop(LivingDropsEvent event) {
//		if (event.entityLiving instanceof EntityCreeper) {
//			int gunpowder = 0;
//			for (EntityItem drop : event.drops) {
//				if (drop.getEntityItem().getItem() == Items.gunpowder) {
//					gunpowder+=drop.getEntityItem().stackSize;
//				}
//			}
//			if (gunpowder >= 2 && !event.entityLiving.worldObj.isRemote && event.entityLiving.worldObj.rand.nextBoolean()) {
//				int dropsLeft = 2;
//				ArrayList<EntityItem> dropsToRemove = new ArrayList<EntityItem>();
//				EntityItem baseItem = null;
//				for (EntityItem drop : event.drops) {
//					if (baseItem == null && drop.getEntityItem().getItem() == Items.gunpowder) {
//						baseItem = drop;
//					}
//					if (dropsLeft > 0 && drop.getEntityItem().getItem() == Items.gunpowder) {
//						if (drop.getEntityItem().stackSize <= dropsLeft) {
//							dropsLeft -= drop.getEntityItem().stackSize;
//							dropsToRemove.add(drop);
//						}
//						else
//						{
//							drop.getEntityItem().stackSize -= dropsLeft;
//							dropsLeft = 0;
//						}
//					}
//				}
//				for (EntityItem drop : dropsToRemove) {
//					event.drops.remove(drop);
//				}
//				baseItem.setEntityItemStack(new ItemStack(SteamcraftItems.steamcraftCrafting,1,5));
//                event.drops.add(baseItem);
//			}
//		}
//	}

    public static boolean hasPower(EntityLivingBase entityLiving, int i) {
        if (entityLiving.getEquipmentInSlot(3) != null) {
            ItemStack chestStack = entityLiving.getEquipmentInSlot(3);
            if (chestStack.getItem() instanceof ItemExosuitArmor) {
                return ((ItemExosuitArmor) chestStack.getItem()).hasPower(chestStack, i);
            }
        }
        return false;
    }

    @SubscribeEvent
    public void initializeEntityProperties(EntityEvent.EntityConstructing event) {
        Entity entity = event.entity;
        if (entity instanceof EntityPlayer) {
            entity.registerExtendedProperties(Steamcraft.PLAYER_PROPERTY_ID,
              new ExtendedPropertiesPlayer());
        } else if (entity instanceof EntityVillager) {
            entity.registerExtendedProperties(Steamcraft.VILLAGER_PROPERTY_ID,
              new ExtendedPropertiesVillager());
        } else if (entity instanceof EntityWolf || entity instanceof EntityOcelot) {
            entity.registerExtendedProperties(Steamcraft.MERCHANT_PROPERTY_ID,
              new ExtendedPropertiesMerchant());
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void handleRocketDisplay(RenderGameOverlayEvent.Post event) {
        if (event.type == ElementType.ALL && Minecraft.getMinecraft().thePlayer.getHeldItem() != null && Minecraft.getMinecraft().thePlayer.getHeldItem().getItem() == SteamcraftItems.rocketLauncher) {
            Minecraft mc = Minecraft.getMinecraft();
            ScaledResolution var5 = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
            int var6 = var5.getScaledWidth();
            int var7 = var5.getScaledHeight();
            FontRenderer var8 = mc.fontRenderer;
            int selectedRocketType = 0;
            if (Minecraft.getMinecraft().thePlayer.getHeldItem().hasTagCompound()) {
                if (Minecraft.getMinecraft().thePlayer.getHeldItem().stackTagCompound.hasKey("rocketType")) {
                    selectedRocketType = Minecraft.getMinecraft().thePlayer.getHeldItem().stackTagCompound.getInteger("rocketType");
                }
            }
            if (selectedRocketType > SteamcraftRegistry.rockets.size() - 1) {
                selectedRocketType = 0;
            }
            String tooltip = StatCollector.translateToLocal("steamcraft.rocket") + " " +
              (selectedRocketType == 0 ? StatCollector.translateToLocal("item.steamcraft:rocket.name.2") : StatCollector.translateToLocal(((Item) SteamcraftRegistry.rockets.get(selectedRocketType)).getUnlocalizedName() + ".name"));

            int tooltipStartX = (var6 - var8.getStringWidth(tooltip)) / 2;
            int tooltipStartY = var7 - 35 - (Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode ? 0 : 35);

            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            var8.drawStringWithShadow(tooltip, tooltipStartX, tooltipStartY, 0xFFFFFF);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }
    }
//
//	@SubscribeEvent
//	public void preRender(RenderLivingEvent.Pre event) {
//		if (event.entity.isPotionActive(Steamcraft.semiInvisible)) {
//	        GL11.glPushMatrix();
//	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.10F);
//	        GL11.glDepthMask(false);
//	        GL11.glEnable(GL11.GL_BLEND);
//	        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//	        GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
//		}
//	}

//	@SubscribeEvent
//	public void postRender(RenderLivingEvent.Post event) {
//		if (event.entity.isPotionActive(Steamcraft.semiInvisible)) {
//	        GL11.glDisable(GL11.GL_BLEND);
//	        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
//	        GL11.glPopMatrix();
//	        GL11.glDepthMask(true);
//		}
//	}

    @SubscribeEvent
    public void handleCanningMachine(EntityItemPickupEvent event) {
        if (event.entityLiving instanceof EntityPlayer && !event.entityLiving.worldObj.isRemote) {
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            if (hasPower(player, 10) && player.getEquipmentInSlot(2) != null && player.getEquipmentInSlot(2).getItem() instanceof ItemExosuitArmor) {
                ItemExosuitArmor leggings = (ItemExosuitArmor) player.getEquipmentInSlot(2).getItem();
                if (leggings.hasUpgrade(player.getEquipmentInSlot(2), SteamcraftItems.canner)) {

                    boolean isCannable = false;
                    ItemStack item = event.item.getEntityItem().copy();
                    if (item.hasTagCompound() && item.stackTagCompound.hasKey("canned")) {
                        return;
                    }

                    if (item.getItem().getUnlocalizedName(item).toLowerCase().contains("ingot")
                      || item.getItem().getUnlocalizedName(item).toLowerCase().contains("gem")
                      || item.getItem().getUnlocalizedName(item).toLowerCase().contains("ore")) {
                        isCannable = true;
                    }
                    for (int id : OreDictionary.getOreIDs(item)) {
                        String str = OreDictionary.getOreName(id);
                        if (str.toLowerCase().contains("ingot")
                          || str.toLowerCase().contains("gem")
                          || str.toLowerCase().contains("ore")) {
                            isCannable = true;
                        }
                    }
                    if (isCannable) {
                        int numCans = 0;
                        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                            if (player.inventory.getStackInSlot(i) != null) {
                                if (player.inventory.getStackInSlot(i).getItem() == SteamcraftItems.canister) {
                                    numCans += player.inventory.getStackInSlot(i).stackSize;
                                }
                            }
                        }
                        if (numCans >= item.stackSize) {
                            if (!item.hasTagCompound()) {
                                item.setTagCompound(new NBTTagCompound());
                            }
                            item.stackTagCompound.setInteger("canned", 0);
                            event.item.setEntityItemStack(item);
                            for (int i = 0; i < item.stackSize; i++) {
                                player.inventory.consumeInventoryItem(SteamcraftItems.canister);
                                player.inventoryContainer.detectAndSendChanges();
                            }
                        } else if (numCans != 0) {
                            item.stackSize -= numCans;
                            event.item.setEntityItemStack(item);
                            ItemStack item2 = item.copy();
                            item2.stackSize = numCans;
                            if (!item2.hasTagCompound()) {
                                item2.setTagCompound(new NBTTagCompound());
                            }
                            item2.stackTagCompound.setInteger("canned", 0);
                            EntityItem entityItem = new EntityItem(player.worldObj, player.posX, player.posY, player.posZ, item2);
                            player.worldObj.spawnEntityInWorld(entityItem);
                            for (int i = 0; i < numCans; i++) {
                                player.inventory.consumeInventoryItem(SteamcraftItems.canister);
                                player.inventoryContainer.detectAndSendChanges();
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void handleCans(EntityJoinWorldEvent event) {
        if (event.entity instanceof EntityItem && !(event.entity instanceof EntityCanisterItem)) {
            EntityItem item = (EntityItem) event.entity;
            if (item.getEntityItem().hasTagCompound() && item.getEntityItem().stackTagCompound.hasKey("canned")) {
                if (!event.world.isRemote) {
                    EntityCanisterItem item2 = new EntityCanisterItem(item.worldObj, item.posX, item.posY, item.posZ, item);
                    item2.motionX = item.motionX;
                    item2.motionY = item.motionY;
                    item2.motionZ = item.motionZ;
                    item2.delayBeforeCanPickup = item.delayBeforeCanPickup;
                    item.worldObj.spawnEntityInWorld(item2);
                }
                item.setDead();
            }
        }
    }

    @SubscribeEvent
    public void handleWorldLoad(WorldEvent.Load event) {
        if (!event.world.isRemote) {
            SteamNetworkData.get(event.world);
            SteamNetworkRegistry.initialize();
        }

    }

    public void renderTexture(int screenX, int screenY, int screenEndX, int screenEndY, double startU, double startV, double endU, double endV) {

        int zLevel = 1;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double) (screenX + 0), (double) (screenY + screenEndY), (double) zLevel, startU,
          endV);
        tessellator.addVertexWithUV((double) (screenX + screenEndX), (double) (screenY + screenEndY), (double) zLevel,
          endU, endV);
        tessellator.addVertexWithUV((double) (screenX + screenEndX), (double) (screenY + 0), (double) zLevel, endU,
          startV);
        tessellator.addVertexWithUV((double) (screenX + 0), (double) (screenY + 0), (double) zLevel, startU, startV);
        tessellator.draw();
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onDrawScreen(RenderGameOverlayEvent.Post event) {
        if (event.type == ElementType.ALL) {
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayer player = mc.thePlayer;
//			if (!player.capabilities.isCreativeMode && player.inventory.armorItemInSlot(1) != null && player.inventory.armorItemInSlot(1).getItem() instanceof ItemExosuitArmor) {
//				ItemStack stack = player.inventory.armorItemInSlot(1);
//				ItemExosuitArmor item = (ItemExosuitArmor) stack.getItem();
//				//if (item.hasUpgrade(stack, SteamcraftItems.doubleJump)) {
//					if (!stack.stackTagCompound.hasKey("aidTicks")) {
//						stack.stackTagCompound.setInteger("aidTicks", -1);
//					}
//					int aidTicks = stack.stackTagCompound.getInteger("aidTicks");
//
//					int aidTicksScaled = 7-(int)(aidTicks*7.0F / 100.0F);
//					int screenX = event.resolution.getScaledWidth() / 2  - 101;
//					int screenY = event.resolution.getScaledHeight() - 39;
//					mc.getTextureManager().bindTexture(icons);
//					renderTexture(screenX,screenY,9,9,0,0,9D/256D,9D/256D);
//					if (aidTicks > 0) {
//						renderTexture(screenX+1,screenY,aidTicksScaled,9,10D/256D,0,(10D+aidTicksScaled)/256D,9D/256D);
//					}
//					else if (aidTicks == 0) {
//						renderTexture(screenX,screenY,9,9,18D/256D,0,27D/256D,9D/256D);
//					}
//					//}
//
//			}
            Item equipped = player.getCurrentEquippedItem() != null ? player.getCurrentEquippedItem().getItem() : null;
            MovingObjectPosition pos = mc.objectMouseOver;
            if (pos != null && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof IPipeWrench && ((IPipeWrench) equipped).canWrench(player, pos.blockX, pos.blockY, pos.blockZ)) {
                TileEntity te = mc.theWorld.getTileEntity(pos.blockX, pos.blockY, pos.blockZ);
                if (te instanceof IWrenchDisplay) {
                    ((IWrenchDisplay) te).displayWrench(event);
                }
            }
            if (CrossMod.BOTANIA) {
                if (pos != null && player.getEquipmentInSlot(3) != null && player.getEquipmentInSlot(3).getItem() instanceof ItemExosuitArmor && player.getEquipmentInSlot(4) != null && player.getEquipmentInSlot(4).getItem() instanceof ItemExosuitArmor && (player.getHeldItem() == null || player.getHeldItem().getItem() != BotaniaIntegration.twigWand())) {
                    ItemExosuitArmor chest = (ItemExosuitArmor) player.getEquipmentInSlot(3).getItem();
                    if (chest.hasUpgrade(player.getEquipmentInSlot(3), BotaniaIntegration.floralLaurel)) {
                        Block block = mc.theWorld.getBlock(pos.blockX, pos.blockY, pos.blockZ);
                        BotaniaIntegration.displayThings(pos, event);
                    }
                }
            }
            if (pos != null && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() == SteamcraftItems.book) {
                Block block = mc.theWorld.getBlock(pos.blockX, pos.blockY, pos.blockZ);
                ItemStack stack = block.getPickBlock(pos, player.worldObj, pos.blockX, pos.blockY, pos.blockZ, player);
                if (stack != null) {
                    for (ItemStack stack2 : SteamcraftRegistry.bookRecipes.keySet()) {
                        if (stack2.getItem() == stack.getItem() && stack2.getItemDamage() == stack.getItemDamage()) {
                            GL11.glPushMatrix();
                            int x = event.resolution.getScaledWidth() / 2 - 8;
                            int y = event.resolution.getScaledHeight() / 2 - 8;

                            int color = 0x6600FF00;
                            RenderItem.getInstance().renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, new ItemStack(SteamcraftItems.book), x, y);
                            GL11.glDisable(GL11.GL_LIGHTING);
                            mc.fontRenderer.drawStringWithShadow("", x + 15, y + 13, 0xC6C6C6);
                            GL11.glPopMatrix();
                            GL11.glEnable(GL11.GL_BLEND);
                        }
                    }
                }
            }

        }
    }

    private static Field lastBuyingPlayerField = null;
    private static Field timeUntilResetField = null;
    private static Field merchantField = null;
    private static Field buyingListField = null;

    private static Field getField(String fieldName, String obfName, Class clazz) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(obfName);
        } catch (NoSuchFieldException e) {
            FMLLog.warning("[FSP] Unable to find field " + fieldName + " with its obfuscated " +
              "name. Trying to find it by its name " + fieldName);
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e1) {
                e1.printStackTrace();
                String fields = "";
                for (Field field1 : clazz.getDeclaredFields()) {
                    if (fields.isEmpty()) {
                        fields += field1.getName();
                    } else {
                        fields += ", " + field1.getName();
                    }
                }
                FMLLog.warning("Unable to find " + fieldName + " field in " + clazz.getName() +
                  "class. Available fields are: " + fields + ". Things are not going to work right.");
            }
        }
        return field;
    }

    static {
        FMLLog.info("[FSP] Getting some fields through reflection.");
        lastBuyingPlayerField = getField("lastBuyingPlayer", "field_82189_bL", EntityVillager.class);
        timeUntilResetField = getField("timeUntilReset", "field_70961_j", EntityVillager.class);
        buyingListField = getField("buyingList", "field_70963_i", EntityVillager.class);

        if (lastBuyingPlayerField != null) {
            lastBuyingPlayerField.setAccessible(true);
        }

        if (timeUntilResetField != null) {
            timeUntilResetField.setAccessible(true);
        }

        if (buyingListField != null) {
            buyingListField.setAccessible(true);
        }

        try {
            merchantField = getField("field_147037_w", "field_147037_w", GuiMerchant.class);
            if (merchantField != null) {
                merchantField.setAccessible(true);
            }
        } catch (NoClassDefFoundError ignore) {
            FMLLog.warning("[FSP] GuiMerchant class not found. You are probably a server.");
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void updateVillagersClientside(GuiScreenEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (merchantField != null && event.gui instanceof GuiMerchant && !lastViewVillagerGui) {
            GuiMerchant gui = (GuiMerchant) event.gui;
            if (mc.thePlayer.inventory.armorInventory[3] != null && (mc.thePlayer.inventory.armorInventory[3].getItem() == SteamcraftItems.tophat
              || (mc.thePlayer.inventory.armorInventory[3].getItem() == SteamcraftItems.exoArmorHead
              && ((ItemExosuitArmor) mc.thePlayer.inventory.armorInventory[3].getItem()).hasUpgrade(mc.thePlayer.inventory.armorInventory[3], SteamcraftItems.tophat)))) {
                IMerchant merch = gui.func_147035_g();
                MerchantRecipeList recipeList = merch.getRecipes(mc.thePlayer);
                if (recipeList != null) {
                    for (Object obj : recipeList) {
                        MerchantRecipe recipe = (MerchantRecipe) obj;
                        if (recipe.getItemToSell().stackSize > 1 && recipe.getItemToSell().stackSize != MathHelper.floor_float(recipe.getItemToSell().stackSize * 1.25F)) {
                            recipe.getItemToSell().stackSize = MathHelper.floor_float(recipe.getItemToSell().stackSize * 1.25F);
                        } else if (recipe.getItemToBuy().stackSize > 1 && recipe.getItemToBuy().stackSize != MathHelper.ceiling_float_int(recipe.getItemToBuy().stackSize / 1.25F)) {
                            recipe.getItemToBuy().stackSize = MathHelper.ceiling_float_int(recipe.getItemToBuy().stackSize / 1.25F);
                        } else if (recipe.getSecondItemToBuy() != null && recipe.getSecondItemToBuy().stackSize > 1 && recipe.getSecondItemToBuy().stackSize != MathHelper.ceiling_float_int(recipe.getSecondItemToBuy().stackSize / 1.25F)) {
                            recipe.getSecondItemToBuy().stackSize = MathHelper.ceiling_float_int(recipe.getSecondItemToBuy().stackSize / 1.25F);
                        }
                    }
                    lastViewVillagerGui = true;
                }
                merch.setRecipes(recipeList);
                try {
                    merchantField.set(gui, merch);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SubscribeEvent
    public void updateVillagers(LivingUpdateEvent event) {
        if (event.entityLiving instanceof EntityVillager && timeUntilResetField != null &&
          lastBuyingPlayerField != null) {
            EntityVillager villager = (EntityVillager) event.entityLiving;
            Integer timeUntilReset = null;
            String lastBuyingPlayer = null;
            try {
                timeUntilReset = timeUntilResetField.getInt(villager);
                lastBuyingPlayer = (String) lastBuyingPlayerField.get(villager);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (!villager.isTrading() && timeUntilReset != null && timeUntilReset == 39 &&
              lastBuyingPlayer != null) {
                EntityPlayer player = villager.worldObj.getPlayerEntityByName(lastBuyingPlayer);
                if (player != null) {
                    if (player.inventory.armorInventory[3] != null && (player.inventory.armorInventory[3].getItem() == SteamcraftItems.tophat)) {
                        ItemStack hat = player.inventory.armorInventory[3];
                        if (!hat.hasTagCompound()) {
                            hat.setTagCompound(new NBTTagCompound());
                        }
                        if (!hat.stackTagCompound.hasKey("level")) {
                            hat.stackTagCompound.setInteger("level", 0);
                        }
                        int level = hat.stackTagCompound.getInteger("level");
                        level++;
                        hat.stackTagCompound.setInteger("level", level);
                    } else if (player.inventory.armorInventory[3] != null && player.inventory.armorInventory[3].getItem() == SteamcraftItems.exoArmorHead && ((ItemExosuitArmor) player.inventory.armorInventory[3].getItem()).hasUpgrade(player.inventory.armorInventory[3], SteamcraftItems.tophat)) {
                        ItemStack hat = ((ItemExosuitArmor) player.inventory.armorInventory[3].getItem()).getStackInSlot(player.inventory.armorInventory[3], 3);
                        if (!hat.hasTagCompound()) {
                            hat.setTagCompound(new NBTTagCompound());
                        }
                        if (!hat.stackTagCompound.hasKey("level")) {
                            hat.stackTagCompound.setInteger("level", 0);
                        }
                        int level = hat.stackTagCompound.getInteger("level");
                        level++;
                        hat.stackTagCompound.setInteger("level", level);
                        ((ItemExosuitArmor) player.inventory.armorInventory[3].getItem()).setInventorySlotContents(player.inventory.armorInventory[3], 3, hat);
                    }
                }
            }
        }
        if (event.entityLiving instanceof EntityVillager && !event.entityLiving.worldObj.isRemote &&
          buyingListField != null) {
            EntityVillager villager = (EntityVillager) event.entityLiving;
            ExtendedPropertiesVillager nbt = (ExtendedPropertiesVillager)
              villager.getExtendedProperties(Steamcraft.VILLAGER_PROPERTY_ID);
            if (nbt.lastHadCustomer == null) {
                nbt.lastHadCustomer = false;
            }
            boolean hasCustomer = false;
            if (villager.getCustomer() != null && villager.getCustomer().inventory.armorInventory[3] != null && (villager.getCustomer().inventory.armorInventory[3].getItem() == SteamcraftItems.tophat
              || (villager.getCustomer().inventory.armorInventory[3].getItem() == SteamcraftItems.exoArmorHead && ((ItemExosuitArmor) villager.getCustomer().inventory.armorInventory[3].getItem()).hasUpgrade(villager.getCustomer().inventory.armorInventory[3], SteamcraftItems.tophat)))) {
                EntityPlayer customer = villager.getCustomer();
                hasCustomer = true;

                if (!nbt.lastHadCustomer) {
                    MerchantRecipeList recipeList = villager.getRecipes(customer);
                    for (Object obj : recipeList) {
                        MerchantRecipe recipe = (MerchantRecipe) obj;
                        if (recipe.getItemToSell().stackSize > 1 && recipe.getItemToSell().stackSize != MathHelper.floor_float(recipe.getItemToSell().stackSize * 1.25F)) {
                            recipe.getItemToSell().stackSize = MathHelper.floor_float(recipe.getItemToSell().stackSize * 1.25F);
                        } else if (recipe.getItemToBuy().stackSize > 1 && recipe.getItemToBuy().stackSize != MathHelper.ceiling_float_int(recipe.getItemToBuy().stackSize / 1.25F)) {
                            recipe.getItemToBuy().stackSize = MathHelper.ceiling_float_int(recipe.getItemToBuy().stackSize / 1.25F);
                        } else if (recipe.getSecondItemToBuy() != null && recipe.getSecondItemToBuy().stackSize > 1 && recipe.getSecondItemToBuy().stackSize != MathHelper.ceiling_float_int(recipe.getSecondItemToBuy().stackSize / 1.25F)) {
                            recipe.getSecondItemToBuy().stackSize = MathHelper.ceiling_float_int(recipe.getSecondItemToBuy().stackSize / 1.25F);
                        }
                    }

                    try {
                        buyingListField.set(villager, recipeList);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    //customer.closeScreen();
                    //customer.displayGUIMerchant(villager, villager.getCustomNameTag());
                }
            }

            if (!hasCustomer && nbt.lastHadCustomer) {
                // We need to do reflection because we do not have the customer in this case.
                MerchantRecipeList recipeList = null;
                try {
                    recipeList = (MerchantRecipeList) buyingListField.get(villager);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (recipeList != null) {
                    for (Object obj : recipeList) {
                        MerchantRecipe recipe = (MerchantRecipe) obj;
                        if (recipe.getItemToSell().stackSize > 1 && recipe.getItemToSell().stackSize != MathHelper.ceiling_float_int(recipe.getItemToSell().stackSize / 1.25F)) {
                            recipe.getItemToSell().stackSize = MathHelper.ceiling_float_int(recipe.getItemToSell().stackSize / 1.25F);
                        } else if (recipe.getItemToBuy().stackSize > 1 && recipe.getItemToBuy().stackSize != MathHelper.floor_float(recipe.getItemToBuy().stackSize * 1.25F)) {
                            recipe.getItemToBuy().stackSize = MathHelper.floor_float(recipe.getItemToBuy().stackSize * 1.25F);
                        } else if (recipe.getSecondItemToBuy() != null && recipe.getSecondItemToBuy().stackSize > 1 && recipe.getSecondItemToBuy().stackSize != MathHelper.floor_float(recipe.getSecondItemToBuy().stackSize * 1.25F)) {
                            recipe.getSecondItemToBuy().stackSize = MathHelper.floor_float(recipe.getSecondItemToBuy().stackSize * 1.25F);
                        }
                    }
                }
                try {
                    buyingListField.set(villager, recipeList);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            nbt.lastHadCustomer = hasCustomer;
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void muffleSounds(PlaySoundEvent17 event) {
        if (event.name.contains("step")) {
            float x = event.sound.getXPosF();
            float y = event.sound.getYPosF();
            float z = event.sound.getZPosF();
            List entities = Minecraft.getMinecraft().thePlayer.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(x - 0.5F, y - 0.5F, z - 0.5F, x + 0.5F, y + 0.5F, z + 0.5F));
            for (Object obj : entities) {
                EntityLivingBase entity = (EntityLivingBase) obj;
                if (entity.getEquipmentInSlot(2) != null && entity.getEquipmentInSlot(2).getItem() instanceof ItemExosuitArmor) {
                    ItemExosuitArmor leggings = (ItemExosuitArmor) entity.getEquipmentInSlot(2).getItem();
                    if (leggings.hasUpgrade(entity.getEquipmentInSlot(2), SteamcraftItems.stealthUpgrade)) {
                        event.result = null;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void hideCloakedPlayers(LivingUpdateEvent event) {
        if (event.entityLiving instanceof EntityLiving) {
            EntityLiving entity = (EntityLiving) event.entityLiving;
            if (entity.getAttackTarget() != null) {
                if (entity.getAttackTarget().getEquipmentInSlot(2) != null && entity.getAttackTarget().getEquipmentInSlot(2).getItem() instanceof ItemExosuitArmor) {
                    ItemExosuitArmor leggings = (ItemExosuitArmor) entity.getAttackTarget().getEquipmentInSlot(2).getItem();
                    if (leggings.hasUpgrade(entity.getAttackTarget().getEquipmentInSlot(2), SteamcraftItems.stealthUpgrade)) {
                        IAttributeInstance iattributeinstance = entity.getEntityAttribute(SharedMonsterAttributes.followRange);
                        double d0 = iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
                        d0 = d0 / 1.5D;
                        List list = entity.worldObj.getEntitiesWithinAABB(Entity.class, entity.boundingBox.expand(d0, 4.0D, d0));
                        boolean foundPlayer = false;
                        for (Object mob : list) {
                            Entity ent = (Entity) mob;
                            if (ent == entity.getAttackTarget()) {
                                foundPlayer = true;
                            }
                        }
                        if (!foundPlayer) {
                            entity.setAttackTarget(null);
                        }
                    }

                }
            }
        }
    }

    @SubscribeEvent
    public void hideCloakedPlayers(LivingSetAttackTargetEvent event) {
        if (event.entityLiving instanceof EntityLiving) {
            EntityLiving entity = (EntityLiving) event.entityLiving;
            if (event.target != null) {
                if (event.target.getEquipmentInSlot(2) != null && event.target.getEquipmentInSlot(2).getItem() instanceof ItemExosuitArmor) {
                    ItemExosuitArmor leggings = (ItemExosuitArmor) event.target.getEquipmentInSlot(2).getItem();
                    if (leggings.hasUpgrade(event.target.getEquipmentInSlot(2), SteamcraftItems.stealthUpgrade)) {
                        IAttributeInstance iattributeinstance = entity.getEntityAttribute(SharedMonsterAttributes.followRange);
                        double d0 = iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
                        d0 = d0 / 1.5D;
                        List list = entity.worldObj.getEntitiesWithinAABB(Entity.class, entity.boundingBox.expand(d0, 4.0D, d0));
                        boolean foundPlayer = false;
                        for (Object mob : list) {
                            Entity ent = (Entity) mob;
                            if (ent == event.target) {
                                foundPlayer = true;
                            }
                        }
                        if (!foundPlayer) {
                            entity.setAttackTarget(null);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @SideOnly(Side.CLIENT)
    public void plateTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.itemStack;
        if (UtilPlates.getPlate(stack) != null) {
            event.toolTip.add(EnumChatFormatting.BLUE + StatCollector.translateToLocal("steamcraft.plate.bonus") + UtilPlates.getPlate(stack).effect());
        }
        if (stack.hasTagCompound()) {
            if (stack.stackTagCompound.hasKey("canned")) {
                event.toolTip.add(EnumChatFormatting.GOLD + StatCollector.translateToLocal("steamcraft.canned"));
            }
        }
        if (stack.getItem() instanceof ItemExosuitArmor || stack.getItem() instanceof ISteamChargable) {
            ArrayList<String> linesToRemove = new ArrayList<String>();
            for (String str : event.toolTip) {
                if (str == "") {
                    linesToRemove.add(str);
                }
                if (str.contains("+")) {
                    linesToRemove.add(str);
                }
                if (str.contains("/") && !str.contains("SU")) {
                    linesToRemove.add(str);
                }
            }
            for (String str : linesToRemove) {
                if (str.contains("+") && (!str.contains("+0.25"))) {
                    event.toolTip.remove(str);
                    event.toolTip.add(1, str);
                } else {
                    event.toolTip.remove(str);
                }
            }
        }
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (Minecraft.getMinecraft().currentScreen instanceof GuiContainer) {

            for (ItemStack stack2 : SteamcraftRegistry.bookRecipes.keySet()) {
                if (stack2.getItem() == stack.getItem() && (stack2.getItemDamage() == stack.getItemDamage() || stack.getItem() instanceof ItemArmor || stack.getItem() instanceof ItemTool)) {
                    boolean foundBook = CrossMod.ENCHIRIDION ? EnchiridionIntegration.hasBook(SteamcraftItems.book, player) : false;
                    for (int p = 0; p < player.inventory.getSizeInventory(); p++) {
                        if (player.inventory.getStackInSlot(p) != null && player.inventory.getStackInSlot(p).getItem() instanceof ItemSteamcraftBook) {
                            foundBook = true;
                            break;
                        }
                    }
                    if (foundBook) {
                        event.toolTip.add(EnumChatFormatting.ITALIC + "" + EnumChatFormatting.GRAY + StatCollector.translateToLocal("steamcraft.book.shiftright"));
                        boolean mouseDown = Mouse.isButtonDown(0);
                        if (Config.singleButtonTrackpad && !mouseDown) {
                            mouseDown = Mouse.isButtonDown(1);
                        }
                        if (mouseDown && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
                            GuiSteamcraftBook.openRecipeFor(stack2, player);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void doubleExp(PlayerPickupXpEvent event) {
        EntityPlayer player = event.entityPlayer;
        for (int i = 1; i < 5; i++) {
            float multiplier = 1;
            if (player.getEquipmentInSlot(i) != null) {
                ItemStack stack = player.getEquipmentInSlot(i);
                if (stack.getItem() instanceof ItemExosuitArmor) {
                    ItemExosuitArmor armor = (ItemExosuitArmor) stack.getItem();
                    if (armor.hasPlates(stack)) {
                        ExosuitPlate plate = UtilPlates.getPlate(stack.stackTagCompound.getString("plate"));
                        String id = plate.getIdentifier();
                        if (id.equals("Gold")) {
                            multiplier *= 1.25F;
                        } else if (id.equals("Gilded Iron")) {
                            multiplier *= 1.125;
                        }
                    }
                }
            }
            event.orb.xpValue = MathHelper.ceiling_float_int(event.orb.xpValue * multiplier);
        }
    }

    @SubscribeEvent
    public void useItem(PlayerUseItemEvent.Tick event) {
        if (event.item.getItem() instanceof ItemFirearm || event.item.getItem() instanceof ItemRocketLauncher) {
            use = event.duration;
        }
    }

    @SubscribeEvent
    public void useItemEnd(PlayerUseItemEvent.Finish event) {
        if (event.item.getItem() instanceof ItemFirearm || event.item.getItem() instanceof ItemRocketLauncher) {
            use = -1;
        }
    }

    @SubscribeEvent
    public void useItemEnd(PlayerUseItemEvent.Stop event) {
        if (event.item.getItem() instanceof ItemFirearm || event.item.getItem() instanceof ItemRocketLauncher) {
            use = -1;
        }
    }

    @SubscribeEvent
    public void handleFirePunch(LivingAttackEvent event) {
        if (event.source.getSourceOfDamage() instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase) event.source.getSourceOfDamage();
            boolean hasPower = hasPower(entity, Config.powerFistConsumption);
            if (hasPower && entity.getEquipmentInSlot(3) != null && entity.getHeldItem() == null) {
                ItemExosuitArmor chest = (ItemExosuitArmor) entity.getEquipmentInSlot(3).getItem();
                if (chest.hasUpgrade(entity.getEquipmentInSlot(3), SteamcraftItems.powerFist)) {
                    entity.worldObj.playSoundEffect(entity.posX, entity.posY, entity.posZ, "random.explode", 4.0F, (1.0F + (entity.worldObj.rand.nextFloat() - entity.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
                    event.entityLiving.motionX += 3.0F * entity.getLookVec().normalize().xCoord;
                    event.entityLiving.motionY += (entity.getLookVec().normalize().yCoord > 0.0F ? 2.0F * entity.getLookVec().normalize().yCoord : 0.0F) + 1.5F;
                    event.entityLiving.motionZ += 3.0F * entity.getLookVec().normalize().zCoord;
                    entity.motionX += -0.5F * entity.getLookVec().normalize().xCoord;
                    entity.motionZ += -0.5F * entity.getLookVec().normalize().zCoord;
                    drainSteam(event.entityLiving.getEquipmentInSlot(3), Config.powerFistConsumption);
                }
            }
        }
    }

    @SubscribeEvent
    public void handlePistonPunch(PlayerInteractEvent event) {
        EntityPlayer entity = event.entityPlayer;
        int consumption = Config.pistonPushConsumption;
        boolean hasPower = hasPower(entity, consumption);
        ItemStack heldItem = entity.getHeldItem();
        if (hasPower && heldItem == null) {
            ItemStack chestStack = entity.getEquipmentInSlot(3);
            Item chest = chestStack.getItem();
            if (chest instanceof ItemExosuitArmor) {
                ItemExosuitArmor armor = (ItemExosuitArmor) chest;
                if (armor.hasUpgrade(chestStack, SteamcraftItems.pistonPush)) {
                    World world = event.world;
                    int face = event.face;
                    int x = event.x;
                    int y = event.y;
                    int z = event.z;
                    if (face == 0) {
                        if (y != 256) {
                            y += 1;
                        }
                    } else if (face == 1) {
                        if (y != 0) {
                            y -= 1;
                        }
                    } else if (face == 2) {
                        if (z < 0) {
                            z += 1;
                        } else {
                            z -= 1;
                        }
                    } else if (face == 3) {
                        if (z < 0) {
                            z -= 1;
                        } else {
                            z += 1;
                        }
                    } else if (face == 4) {
                        if (x < 0) {
                            x -= 1;
                        } else {
                            x += 1;
                        }
                    } else if (face == 5) {
                        if (x < 0) {
                            x += 1;
                        } else {
                            x -= 1;
                        }
                    }
                    int i = event.x;
                    int j = event.y;
                    int k = event.z;
                    Block clickedBlock = world.getBlock(i, j, k);
                    Block blockInPlace = world.getBlock(x, y, z);
                    int clickedMeta = world.getBlockMetadata(i, j, k);
                    if ((blockInPlace == null || blockInPlace == Blocks.air ||
                      blockInPlace instanceof BlockFluidBase) &&
                      clickedBlock.getBlockHardness(world, x, y, z) >= 0.0F &&
                      clickedBlock.getMobilityFlag() != 2 && clickedBlock != Blocks.obsidian &&
                      j >= 0 && !clickedBlock.hasTileEntity(clickedMeta)) {
                        world.setBlockToAir(i, j, k);
                        world.setBlock(x, y, z, clickedBlock, clickedMeta, 3);
                        world.playSoundEffect((double) x + 0.5D, (double) y + 0.5D, (double) z +
                          0.5D, "tile.piston.out", 0.5F, world.rand.nextFloat() * 0.25F + 0.6F);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void hearMeRoar(LivingAttackEvent event) {
        // Explosions must be ignored in order to prevent infinite recursive hearMeRoar calls.
        if (event.source.getSourceOfDamage() instanceof EntityLivingBase &&
          !event.source.isExplosion()) {
            EntityLivingBase entity = (EntityLivingBase) event.source.getSourceOfDamage();
            World world = entity.worldObj;
            ItemStack equipment = entity.getEquipmentInSlot(4);
            ItemStack chest = entity.getEquipmentInSlot(3);
            if (entity.getHeldItem() == null && entity.isSneaking() && equipment != null &&
              chest != null && chest.getItem() instanceof ItemExosuitArmor &&
              chest.hasTagCompound()) {
                int consumption = (chest.stackTagCompound.getInteger("maxFill") / 2) +
                  Config.dragonRoarConsumption;
                Item helmet = equipment.getItem();
                if (hasPower(entity, consumption) && helmet instanceof ItemExosuitArmor) {
                    ItemExosuitArmor helmetArmor = (ItemExosuitArmor) helmet;
                    if (helmetArmor.hasUpgrade(equipment, SteamcraftItems.dragonRoar)) {
                        if (world.isRemote) {
                            world.playSound(entity.posX, entity.posY, entity.posZ,
                              "mob.enderdragon.growl", 5.0F, 0.8F + world.rand.nextFloat() * 0.3F,
                              false);
                        } else {
                            world.createExplosion(entity, entity.posX + 0.5F, entity.posY,
                              entity.posZ + 0.5F, 10.0F, false);
                        }
                        drainSteam(chest, consumption);
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void handleFallDamage(LivingHurtEvent event) {
        if (CrossMod.BLOOD_MAGIC) {
            BloodMagicIntegration.handleAttack(event);
        }
        if (((event.entityLiving instanceof EntityPlayer)) && (event.source.damageType.equals("mob")) && (event.source.getEntity() != null) && (!event.entityLiving.worldObj.isRemote)) {
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            if (player.getHealth() <= 5.0F) {
                int vibrantLevel = 0;
                for (int i = 0; i < player.inventory.armorInventory.length; i++) {
                    ItemStack armor = player.inventory.armorInventory[i];
                    if (armor != null && armor.getItem() instanceof ItemExosuitArmor) {
                        ItemExosuitArmor armorItem = (ItemExosuitArmor) armor.getItem();
                        if (armorItem.hasPlates(armor) && UtilPlates.getPlate(armor.stackTagCompound.getString("plate")).getIdentifier() == "Vibrant") {
                            vibrantLevel += 1;
                        }
                    }
                }

                if ((vibrantLevel > 0) && (player.worldObj.rand.nextInt(5 - vibrantLevel) == 0)) {
                    int startRotation = player.worldObj.rand.nextInt(360);
                    boolean foundSpot = false;
                    int range = 14;
                    int counter = 0;
                    int yO = 2;
                    int tX = 0;
                    int tY = 0;
                    int tZ = 0;
                    int safeRange = 7;
                    int safe = 0;
                    while (!foundSpot && range < 28 && safe < 10000) {
                        safe++;
                        tX = (int) (player.posX + range * Math.sin(Math.toRadians(startRotation)));
                        tZ = (int) (player.posZ + range * Math.cos(Math.toRadians(startRotation)));
                        tY = (int) player.posY + yO;
                        List mobs = player.worldObj.getEntitiesWithinAABB(EntityMob.class, AxisAlignedBB.getBoundingBox(tX + 0.5F - safeRange, tY + 0.5F - safeRange, tZ + 0.5F - safeRange, tX + 0.5F + safeRange, tY + 0.5F + safeRange, tZ + 0.5F + safeRange));
                        if (mobs.size() == 0 && player.worldObj.isSideSolid(tX, tY - 1, tZ, ForgeDirection.UP) && !player.worldObj.isAnyLiquid(AxisAlignedBB.getBoundingBox(tX, tY - 1, tZ, tX, tY + 1, tZ)) && player.worldObj.isAirBlock(tX, tZ, tY) && player.worldObj.isAirBlock(tX, tZ, tY + 1)) {
                            foundSpot = true;
                        } else {
                            if (counter >= 36) {
                                if (yO > -2) {
                                    yO--;
                                    counter = 0;
                                } else {
                                    counter = 0;
                                    yO = 2;
                                    range += 2;
                                }
                            } else {
                                startRotation += 10;
                                counter++;
                            }
                        }
                    }

                    if (foundSpot) {
                        ((EntityPlayerMP) player).playerNetServerHandler.setPlayerLocation(tX, tY, tZ, player.worldObj.rand.nextInt(360), player.rotationPitch);
                    }
                }
            }
        }

        if (((event.entityLiving instanceof EntityPlayer)) && (event.source.damageType.equals("mob")) && (event.source.getEntity() != null) && (!event.entityLiving.worldObj.isRemote)) {
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            Entity mob = event.source.getEntity();
            int enderiumLevel = 0;
            for (int i = 0; i < player.inventory.armorInventory.length; i++) {
                ItemStack armor = player.inventory.armorInventory[i];
                if (armor != null && armor.getItem() instanceof ItemExosuitArmor) {
                    ItemExosuitArmor armorItem = (ItemExosuitArmor) armor.getItem();
                    if (armorItem.hasPlates(armor) && UtilPlates.getPlate(armor.stackTagCompound.getString("plate")).getIdentifier() == "Enderium") {
                        enderiumLevel += 1;
                    }
                }
            }
            if ((enderiumLevel > 0) && (player.worldObj.rand.nextFloat() < (enderiumLevel * 0.075F))) {
                int startRotation = player.worldObj.rand.nextInt(360);
                boolean foundSpot = false;
                int range = 8;
                int counter = 0;
                int yO = 2;
                int tX = 0;
                int tY = 0;
                int tZ = 0;
                int safe = 0;
                while (!foundSpot && range < 16 && safe < 10000) {
                    safe++;
                    tX = (int) (mob.posX + range * Math.sin(Math.toRadians(startRotation)));
                    tZ = (int) (mob.posZ + range * Math.cos(Math.toRadians(startRotation)));
                    tY = (int) mob.posY + yO;
                    if (player.worldObj.isSideSolid(tX, tY - 1, tZ, ForgeDirection.UP) && !player.worldObj.isAnyLiquid(AxisAlignedBB.getBoundingBox(tX, tY - 1, tZ, tX, tY + 1, tZ)) && player.worldObj.isAirBlock(tX, tZ, tY) && player.worldObj.isAirBlock(tX, tZ, tY + 1)) {
                        foundSpot = true;
                    } else {
                        if (counter >= 36) {
                            if (yO > -2) {
                                yO--;
                                counter = 0;
                            } else {
                                counter = 0;
                                yO = 2;
                                range += 2;
                            }
                        } else {
                            startRotation += 10;
                            counter++;
                        }
                    }
                }

                if (foundSpot) {
                    mob.setPositionAndRotation(tX, tY, tZ, mob.rotationYaw, mob.rotationPitch);
                }
            }
        }
        if (((event.entityLiving instanceof EntityPlayer)) && (event.source.damageType.equals("mob")) && (event.source.getEntity() != null)) {
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            int fireLevel = 0;
            for (int i = 0; i < player.inventory.armorInventory.length; i++) {
                ItemStack armor = player.inventory.armorInventory[i];
                if (armor != null && armor.getItem() instanceof ItemExosuitArmor) {
                    ItemExosuitArmor armorItem = (ItemExosuitArmor) armor.getItem();
                    if (armorItem.hasPlates(armor) && UtilPlates.getPlate(armor.stackTagCompound.getString("plate")).getIdentifier() == "Fiery") {
                        fireLevel += 3;
                    }
                }
            }
            if ((fireLevel > 0) && (player.worldObj.rand.nextInt(25) < fireLevel)) {
                event.source.getEntity().setFire(fireLevel / 2);
            }
        }
        if (((event.entityLiving instanceof EntityPlayer)) && (event.source.damageType.equals("mob")) &&
          (event.source.getEntity() != null) && ((event.source.getEntity() instanceof EntityLivingBase))) {
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            int chillLevel = 0;
            for (int i = 0; i < player.inventory.armorInventory.length; i++) {
                ItemStack armor = player.inventory.armorInventory[i];
                if (armor != null && armor.getItem() instanceof ItemExosuitArmor) {
                    ItemExosuitArmor armorItem = (ItemExosuitArmor) armor.getItem();
                    if (armorItem.hasPlates(armor) && UtilPlates.getPlate(armor.stackTagCompound.getString("plate")).getIdentifier() == "Yeti") {
                        chillLevel += 1;
                    }
                }
            }
            if (chillLevel > 0) {
                ((EntityLivingBase) event.source.getEntity()).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, chillLevel * 3 + 5, MathHelper.ceiling_float_int((float) chillLevel / 2F)));
            }
        }
        if (event.source == DamageSource.fall) {
            boolean hasPower = hasPower(event.entityLiving, (int) (event.ammount / Config.fallAssistDivisor));
            int armor = getExoArmor(event.entityLiving);
            EntityLivingBase entity = event.entityLiving;
            if (hasPower && entity.getEquipmentInSlot(3) != null && entity.getEquipmentInSlot(1) != null && entity.getEquipmentInSlot(1).getItem() instanceof ItemExosuitArmor) {
                ItemExosuitArmor boots = (ItemExosuitArmor) entity.getEquipmentInSlot(1).getItem();
                if (boots.hasUpgrade(entity.getEquipmentInSlot(1), SteamcraftItems.fallAssist)) {
                    if (event.ammount <= 6.0F) {
                        event.ammount = 0.0F;
                    }
                    event.ammount = event.ammount / 3.0F;
                    drainSteam(entity.getEquipmentInSlot(3), (int) (event.ammount / Config.fallAssistDivisor));
                    if (event.ammount == 0.0F) {
                        event.setResult(Event.Result.DENY);
                        event.setCanceled(true);
                    }
                }
            }
        }
        if (((event.entity instanceof EntityPlayer)) && (((EntityPlayer) event.entity).inventory.armorItemInSlot(1) != null) && (((EntityPlayer) event.entity).inventory.armorItemInSlot(1).getItem() instanceof ItemExosuitArmor)) {
            ItemStack stack = ((EntityPlayer) event.entity).inventory.armorItemInSlot(1);
            ItemExosuitArmor item = (ItemExosuitArmor) stack.getItem();
            //if (item.hasUpgrade(stack, SteamcraftItems.doubleJump)) {
            float amount = event.ammount;
            EntityPlayer player = ((EntityPlayer) event.entity);
            DamageSource src = event.source;
            if (!player.isEntityInvulnerable()) {
                if (amount <= 0) return;
                if (!src.isUnblockable() && player.isBlocking() && amount > 0.0F) {
                    amount = (1.0F + amount) * 0.5F;
                }

                amount = ArmorProperties.ApplyArmor(player, player.inventory.armorInventory, src, amount);
                if (amount <= 0) return;
                float f1 = amount;
                amount = Math.max(amount - player.getAbsorptionAmount(), 0.0F);
            }
            if (amount > 0.0F) {
//				stack.stackTagCompound.setFloat("damageAmount", amount);
//				stack.stackTagCompound.setInteger("aidTicks", 100);

            }
            //}
        }
    }

    @SubscribeEvent
    public void playerJumps(LivingEvent.LivingJumpEvent event) {
        boolean hasPower = hasPower(event.entityLiving, Config.jumpBoostConsumptionShiftJump);
        if (((event.entity instanceof EntityPlayer)) && (((EntityPlayer) event.entity).inventory.armorItemInSlot(0) != null) && (((EntityPlayer) event.entity).inventory.armorItemInSlot(0).getItem() instanceof ItemExosuitArmor)) {
            ItemStack stack = ((EntityPlayer) event.entity).inventory.armorItemInSlot(0);
            ItemExosuitArmor item = (ItemExosuitArmor) stack.getItem();

            if ((event.entity.isSneaking() && hasPower) || hasPower(event.entityLiving, Config.jumpBoostConsumption)) {

                if (item.hasUpgrade(stack, SteamcraftItems.jumpAssist)) {
                    if (event.entity.isSneaking()) {
                        Vec3 vector = event.entityLiving.getLook(0.5F);
                        double total = Math.abs(vector.zCoord + vector.xCoord);
                        EntityPlayer player = (EntityPlayer) event.entity;
                        double jump = 0;
                        if (jump >= 1) {
                            jump = (jump + 2D) / 4D;
                        }

                        if (vector.yCoord < total)
                            vector.yCoord = total;

                        event.entityLiving.motionY += ((jump + 1) * vector.yCoord) / 1.5F;
                        event.entityLiving.motionZ += (jump + 1) * vector.zCoord * 2;
                        event.entityLiving.motionX += (jump + 1) * vector.xCoord * 2;
                        drainSteam(event.entityLiving.getEquipmentInSlot(3), Config.jumpBoostConsumptionShiftJump);
                    } else {
                        drainSteam(event.entityLiving.getEquipmentInSlot(3), Config.jumpBoostConsumption);
                        event.entityLiving.motionY += 0.2750000059604645D;
                    }
                }
            }
            if (item.hasUpgrade(stack, SteamcraftItems.doubleJump)) {
                stack.stackTagCompound.setBoolean("releasedSpace", false);

            }

        }
    }

    public boolean hasItemInHotbar(EntityPlayer player, Item item) {
        for (int i = 0; i < 10; i++) {
            if (player.inventory.getStackInSlot(i) != null && player.inventory.getStackInSlot(i).getItem() == item) {
                return true;
            }
        }
        return false;
    }

    @SubscribeEvent
    public void handleSteamcraftArmorMining(PlayerEvent.BreakSpeed event) {
        EntityLivingBase entity = event.entityLiving;
        if (!(entity instanceof EntityPlayer)) {
            return;
        }

        boolean hasPower = hasPower(entity, 1);
        int armor = getExoArmor(entity);
        if (hasPower && armor == 4) {
            event.newSpeed = event.newSpeed * 1.2F;
        }
        EntityPlayer player = (EntityPlayer) entity;
        ItemStack heldItemStack = player.getHeldItem();
        if (heldItemStack == null) {
            return;
        }
        Item heldItem = heldItemStack.getItem();
        int itemDamage = heldItemStack.getItemDamage();
        int maxDamage = heldItemStack.getMaxDamage();

        /*
        Survivalist's Toolkit
         */
        if (CrossMod.BAUBLES) {
            if (BaublesIntegration.checkForSurvivalist(player)) {
                if (heldItem instanceof ItemTool) {
                    if (itemDamage >= maxDamage - 1) {
                        event.newSpeed = 0.0F;
                    }
                }

            }
        } else if (hasItemInHotbar(player, SteamcraftItems.survivalist)) {
            if (heldItem instanceof ItemTool) {
                if (itemDamage >= maxDamage - 1) {
                    event.newSpeed = 0.0F;
                }
            }
        }

        if (heldItem instanceof ItemSteamDrill) {
            NBTTagCompound nbt = SteamToolHelper.checkNBT(heldItemStack);
            int speed = nbt.getInteger("Speed");
            if (speed > 0 && Items.iron_pickaxe.func_150893_a(heldItemStack, event.block) != 1.0F) {
                event.newSpeed *= 1.0F + 11.0F * (speed / 1000.0F);
            }
        } else if (heldItem instanceof ItemSteamAxe) {
            NBTTagCompound nbt = SteamToolHelper.checkNBT(heldItemStack);
            int speed = nbt.getInteger("Speed");
            if (speed > 0 && Items.diamond_axe.func_150893_a(player.getHeldItem(), event.block) != 1.0F) {
                event.newSpeed *= 1.0F + 11.0F * (speed / 1000.0F);
            }
        } else if (heldItem instanceof ItemSteamShovel) {
            NBTTagCompound nbt = SteamToolHelper.checkNBT(heldItemStack);
            int speed = nbt.getInteger("Speed");
            if (speed > 0 && Items.diamond_shovel.func_150893_a(heldItemStack, event.block) != 1.0F) {
                event.newSpeed *= 1.0F + 19.0F * (speed / 3000.0F);
            }
        }
    }

    @SubscribeEvent
    public void handleFlippers(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase entity = event.entityLiving;
        if (!(entity instanceof EntityPlayer)) {
            return;
        }
        boolean hasPower = hasPower(entity, 1);

        if (entity.getEquipmentInSlot(3) != null && entity.getEquipmentInSlot(3).getItem() instanceof ItemExosuitArmor) {
            ItemExosuitArmor chest = (ItemExosuitArmor) entity.getEquipmentInSlot(3).getItem();
            if (chest.hasUpgrade(entity.getEquipmentInSlot(3), SteamcraftItems.pitonDeployer)) {
                if (entity.getEquipmentInSlot(3).stackTagCompound.hasKey("grappled") && entity.getEquipmentInSlot(3).stackTagCompound.getBoolean("grappled")) {

                    double lastX = entity.getEquipmentInSlot(3).stackTagCompound.getFloat("x");
                    double lastY = entity.getEquipmentInSlot(3).stackTagCompound.getFloat("y");
                    double lastZ = entity.getEquipmentInSlot(3).stackTagCompound.getFloat("z");
                    int blockX = entity.getEquipmentInSlot(3).stackTagCompound.getInteger("blockX");
                    int blockY = entity.getEquipmentInSlot(3).stackTagCompound.getInteger("blockY");
                    int blockZ = entity.getEquipmentInSlot(3).stackTagCompound.getInteger("blockZ");

                    if ((Math.abs(lastX - entity.posX) > 0.1F || Math.abs(lastZ - entity.posZ) > 0.1F || entity.isSneaking() || entity.worldObj.isAirBlock(blockX, blockY, blockZ))) {
                        entity.getEquipmentInSlot(3).stackTagCompound.setBoolean("grappled", false);
                    } else {
                        entity.motionX = 0.0F;
                        entity.motionY = (entity.motionY > 0) ? entity.motionY : 0.0F;
                        entity.motionZ = 0.0F;
                    }
                }
            }
        }

        EntityPlayer player = (EntityPlayer) entity;
        ExtendedPropertiesPlayer nbt = (ExtendedPropertiesPlayer)
          player.getExtendedProperties(Steamcraft.PLAYER_PROPERTY_ID);
        ItemStack boots = player.inventory.armorItemInSlot(0);
        ItemStack chest = player.getEquipmentInSlot(3);
        ItemStack leggings = player.getEquipmentInSlot(2);
        /*
        if (armorItem1 != null && armorItem1.getItem() instanceof ItemExosuitArmor) {
            if (item.hasUpgrade(stack, SteamcraftItems.doubleJump)) {
				if (!stack.stackTagCompound.hasKey("aidTicks")) {
					stack.stackTagCompound.setInteger("aidTicks", -1);
				}
				int aidTicks = stack.stackTagCompound.getInteger("aidTicks");

				if (aidTicks > 0) {
					aidTicks--;
				}
				if (aidTicks == 0) {
					if (!stack.stackTagCompound.hasKey("ticksNextHeal")) {
						stack.stackTagCompound.setInteger("ticksNextHeal", 0);
					}
					float damageAmount = stack.stackTagCompound.getInteger("damageAmount");
					int ticksNextHeal = stack.stackTagCompound.getInteger("ticksNextHeal");
					if (ticksNextHeal > 0) {
						ticksNextHeal--;
					}
					if (ticksNextHeal == 0) {
						//event.entityLiving.heal(1.0F);
						damageAmount -=1.0F;
						stack.stackTagCompound.setFloat("damageAmount", damageAmount);
						ticksNextHeal=5;
					}
					if (damageAmount == 0.0F) {
						aidTicks = -1;
					}
					stack.stackTagCompound.setInteger("ticksNextHeal", ticksNextHeal);
				}
				stack.stackTagCompound.setInteger("aidTicks", aidTicks);
            }
        }
        */

        if (boots != null && boots.getItem() instanceof ItemExosuitArmor) {
            ItemExosuitArmor item = (ItemExosuitArmor) boots.getItem();
            if (item.hasUpgrade(boots, SteamcraftItems.doubleJump) && event.entity.onGround) {
                boots.stackTagCompound.setBoolean("usedJump", false);
            }
        }
        if (chest != null && chest.getItem() instanceof ItemExosuitArmor) {
            ItemExosuitArmor item = (ItemExosuitArmor) chest.getItem();
            if (item.hasUpgrade(entity.getEquipmentInSlot(3), SteamcraftItems.wings)) {
                if (entity.fallDistance > 1.5F && !entity.isSneaking()) {
                    entity.fallDistance = 1.5F;
                    entity.motionY = Math.max(entity.motionY, -0.1F);
                    entity.moveEntity(entity.motionX, 0, entity.motionZ);
                }
            }
        }

        if (hasPower && leggings != null && leggings.getItem() instanceof ItemExosuitArmor) {
            ItemExosuitArmor item = (ItemExosuitArmor) leggings.getItem();
            if (item.hasUpgrade(leggings, SteamcraftItems.thrusters)) {
                if (nbt.lastMotions == null) {
                    nbt.lastMotions = MutablePair.of(entity.posX, entity.posZ);
                }
                double lastX = nbt.lastMotions.left;
                double lastZ = nbt.lastMotions.right;
                if ((lastX != entity.posX || lastZ != entity.posZ) && !entity.onGround && !entity.isInWater() && !player.capabilities.isFlying) {
                    entity.moveEntity(entity.motionX, 0, entity.motionZ);
                    if (!event.entityLiving.getEquipmentInSlot(3).stackTagCompound.hasKey("ticksUntilConsume")) {
                        event.entityLiving.getEquipmentInSlot(3).stackTagCompound.setInteger("ticksUntilConsume", 2);
                    }
                    if (event.entityLiving.getEquipmentInSlot(3).stackTagCompound.getInteger("ticksUntilConsume") <= 0) {
                        drainSteam(event.entityLiving.getEquipmentInSlot(3), Config.thrusterConsumption);
                    }
                }
            } else if (item.hasUpgrade(leggings, SteamcraftItems.runAssist)) {
                if (nbt.lastMotions == null) {
                    nbt.lastMotions = MutablePair.of(entity.posX, entity.posZ);
                }
                double lastX = nbt.lastMotions.left;
                double lastZ = nbt.lastMotions.right;
                if ((entity.moveForward > 0.0F) && (lastX != entity.posX || lastZ != entity.posZ) && entity.onGround && !entity.isInWater()) {
                    entity.moveFlying(0.0F, 1.0F, 0.075F);
                    if (!event.entityLiving.getEquipmentInSlot(3).stackTagCompound.hasKey("ticksUntilConsume")) {
                        event.entityLiving.getEquipmentInSlot(3).stackTagCompound.setInteger("ticksUntilConsume", 2);
                    }
                    if (event.entityLiving.getEquipmentInSlot(3).stackTagCompound.getInteger("ticksUntilConsume") <= 0) {
                        drainSteam(event.entityLiving.getEquipmentInSlot(3), Config.runAssistConsumption);
                    }
                }
            }
        }

        /*
		if (hasPower(entity,100) && entity.getEquipmentInSlot(2) != null && entity.getEquipmentInSlot(2).getItem() instanceof ItemExosuitArmor && !entity.worldObj.isRemote) {
			ItemExosuitArmor leggings = (ItemExosuitArmor) entity.getEquipmentInSlot(2).getItem();
			if (leggings.hasUpgrade(entity.getEquipmentInSlot(2), SteamcraftItems.antiFire)) {
				if (entity.isBurning()) {

					event.entityLiving.getEquipmentInSlot(3).damageItem(10, event.entityLiving);
					if (entity.worldObj.isAirBlock((int)entity.posX, (int)entity.posY, (int)entity.posZ) || entity.worldObj.getBlock((int)entity.posX, (int)entity.posY, (int)entity.posZ).isReplaceable(entity.worldObj, (int)entity.posX, (int)entity.posY, (int)entity.posZ) || entity.worldObj.getBlock((int)entity.posX, (int)entity.posY, (int)entity.posZ) == Blocks.fire) {

						entity.worldObj.setBlock((int)entity.posX, (int)entity.posY, (int)entity.posZ, Blocks.water, 1, 1);
					}
				}
			}
		}
		*/
    }

    @SideOnly(Side.CLIENT)
    public void updateRangeClient(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase entity = event.entityLiving;
        if (entity == Minecraft.getMinecraft().thePlayer) {
//			if (!worldStartUpdate && entity.getEquipmentInSlot(3) != null && entity.getEquipmentInSlot(3).getItem() instanceof ItemExosuitArmor) {
//				ItemExosuitArmor chest = (ItemExosuitArmor) entity.getEquipmentInSlot(3).getItem();
//				if (chest.hasUpgrade(entity.getEquipmentInSlot(3), SteamcraftItems.extendoFist)) {
//
//					Steamcraft.proxy.extendRange(entity,Config.extendedRange);
//				}
//			}
            worldStartUpdate = true;

            //Steamcraft.proxy.extendRange(entity,1.0F);
            boolean wearing = false;
            if (entity.getEquipmentInSlot(3) != null && entity.getEquipmentInSlot(3).getItem() instanceof ItemExosuitArmor) {
                ItemExosuitArmor chest = (ItemExosuitArmor) entity.getEquipmentInSlot(3).getItem();
                if (chest.hasUpgrade(entity.getEquipmentInSlot(3), SteamcraftItems.extendoFist)) {
                    Steamcraft.proxy.checkRange(entity);

                    wearing = true;
                }
            }
//			if (wearing && !lastWearing && entity.worldObj.isRemote) {
//				Steamcraft.proxy.extendRange(entity,Config.extendedRange);
//			}
            if (!wearing && lastWearing && entity.worldObj.isRemote) {
                Steamcraft.proxy.extendRange(entity, -Config.extendedRange);
            }
            lastWearing = wearing;
        }
    }

    @SubscribeEvent
    public void handleSteamcraftArmor(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase entity = event.entityLiving;
        if (!(entity instanceof EntityPlayer)) {
            return;
        }
        boolean hasPower = hasPower(entity, 1);
        int armor = getExoArmor(entity);
//        ItemStack armor2 = entity.getEquipmentInSlot(1);
        //Steamcraft.proxy.extendRange(entity,1.0F);

        ExtendedPropertiesPlayer tag =
          (ExtendedPropertiesPlayer) entity.getExtendedProperties(Steamcraft.PLAYER_PROPERTY_ID);

        if (entity.worldObj.isRemote) {
            this.updateRangeClient(event);
        } else {
            boolean wearing = false;
            if (entity.getEquipmentInSlot(3) != null && entity.getEquipmentInSlot(3).getItem() instanceof ItemExosuitArmor) {
                ItemExosuitArmor chest = (ItemExosuitArmor) entity.getEquipmentInSlot(3).getItem();
                if (chest.hasUpgrade(entity.getEquipmentInSlot(3), SteamcraftItems.extendoFist)) {
                    if (!tag.isRangeExtended) {
                        wearing = true;
                        tag.isRangeExtended = true;
                        Steamcraft.proxy.extendRange(entity, Config.extendedRange);
                    }
                }
            }
            if (!wearing && tag.isRangeExtended) {
                Steamcraft.proxy.extendRange(entity, -Config.extendedRange);
                tag.isRangeExtended = false;
            }
        }

        if (hasPower) {
            /*
            if (entity.isSneaking()) {
				if ((!event.entityLiving.isPotionActive(Steamcraft.semiInvisible) || event.entityLiving.getActivePotionEffect(Steamcraft.semiInvisible).getDuration() < 2)) {
					event.entityLiving.addPotionEffect(new PotionEffect(Steamcraft.semiInvisible.id, 2, 0, false));
				}
            }
            */

            if (tag.lastMotions == null) {
                tag.lastMotions = MutablePair.of(entity.posX, entity.posZ);
            }
            if (entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getModifier(uuid2) != null) {
                entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(exoBoostBad);
            }
            if (entity.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getModifier(uuid2) != null) {
                entity.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).removeModifier(exoBoostBad);
            }
            ItemStack stack = entity.getEquipmentInSlot(3);
            if (!stack.hasTagCompound()) {
                stack.setTagCompound(new NBTTagCompound());
            }
            if (!stack.stackTagCompound.hasKey("ticksUntilConsume")) {
                stack.stackTagCompound.setInteger("ticksUntilConsume", 2);
            }
            int ticksLeft = stack.stackTagCompound.getInteger("ticksUntilConsume");
            double lastX = tag.lastMotions.left;
            double lastZ = tag.lastMotions.right;
            if (ticksLeft <= 0) {
                if (Config.passiveDrain && (lastX != entity.posX || lastZ != entity.posZ)) {
                    drainSteam(stack, 1);
                }
                ticksLeft = 2;
            }
            tag.lastMotions = MutablePair.of(entity.posX, entity.posZ);

            ticksLeft--;
            stack.stackTagCompound.setInteger("ticksUntilConsume", ticksLeft);
            if (armor == 4) {
                if (entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getModifier(uuid) == null) {
                    entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).applyModifier(exoBoost);
                }
                if (entity.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getModifier(uuid) == null) {
                    entity.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).applyModifier(exoBoost);
                }
                if (tag.prevStep == null) {
                    tag.prevStep = entity.stepHeight;
                }
                entity.stepHeight = 1.0F;
            } else {
                removeGoodExoBoost(entity);
            }
        } else {
            removeGoodExoBoost(entity);
        }

        if (armor > 0 && !hasPower) {
            if (entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getModifier(uuid2) == null) {
                entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).applyModifier(exoBoostBad);
            }
            if (entity.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getModifier(uuid2) == null) {
                entity.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).applyModifier(exoBoostBad);
            }
        } else {
            removeBadExoBoost(entity);
        }
    }

    private void removeGoodExoBoost(EntityLivingBase entity) {
        if (entity.ticksExisted % 20 == 0) {
            if (entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getModifier(uuid) != null) {
                entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(exoBoost);
            }
            if (entity.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getModifier(uuid) != null) {
                entity.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).removeModifier(exoBoost);
            }
            if (entity instanceof EntityPlayer) {
                ExtendedPropertiesPlayer tag =
                  (ExtendedPropertiesPlayer) entity.getExtendedProperties(Steamcraft.PLAYER_PROPERTY_ID);
                if (tag.prevStep != null) {
                    entity.stepHeight = tag.prevStep;
                    tag.prevStep = null;
                }
            }
        }
    }

//	public boolean isMoving(EntityLivingBase entity) {
//		return (entity.isp)
//	}

    private void removeBadExoBoost(EntityLivingBase entity) {
        if (entity.ticksExisted % 20 == 0) {
            if (entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getModifier(uuid2) != null) {
                entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(exoBoostBad);
            }
            if (entity.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getModifier(uuid2) != null) {
                entity.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).removeModifier(exoBoostBad);
            }
        }
    }

    public int getExoArmor(EntityLivingBase entityLiving) {
        int num = 0;
        for (int i = 1; i < 5; i++) {
            if (entityLiving.getEquipmentInSlot(i) != null) {
                ItemStack stack = entityLiving.getEquipmentInSlot(i);
                if (stack.getItem() instanceof ItemExosuitArmor) {
                    num++;
                }
            }
        }
        return num;
    }

    @SubscribeEvent
    public void clickLeft(PlayerInteractEvent event) {
        if (CrossMod.BLOOD_MAGIC) {
            BloodMagicIntegration.clickLeft(event);
        }
        int x = event.x;
        int y = event.y;
        int z = event.z;
        World world = event.world;
        int face = event.face;
        Action action = event.action;
        EntityPlayer player = event.entityPlayer;
        ItemStack chest = player.getEquipmentInSlot(3);
        if (action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            Block block = world.getBlock(x, y, z);
            if (face != 1 &&
              block.isSideSolid(world, x, y, z, ForgeDirection.getOrientation(face))) {
                if (chest != null && chest.getItem() instanceof ItemExosuitArmor) {
                    AxisAlignedBB aabb;
                    ForgeDirection dir = ForgeDirection.getOrientation(face);
                    if (face == 0) {
                        aabb = AxisAlignedBB.getBoundingBox(x - 0.5F, y + (dir.offsetY / 6F) - 0.4F,
                          z - 0.20F, x + 0.5F + 1, y + (dir.offsetY / 6F) + 1, z + 0.5F + 1);
                    } else {
                        aabb = AxisAlignedBB.getBoundingBox(x + (dir.offsetX / 6F),
                          y + (dir.offsetY / 6F) - 1.0F, z + (dir.offsetZ / 6F),
                          x + (dir.offsetX / 6F) + 1, y + (dir.offsetY / 6F) + 2.0F,
                          z + (dir.offsetZ / 6F) + 1);
                    }
                    ItemExosuitArmor chestArmor = (ItemExosuitArmor) chest.getItem();
                    boolean canStick = false;
                    List list = world.getEntitiesWithinAABB(EntityPlayer.class, aabb);
                    for (Object obj : list) {
                        if (obj == player) {
                            canStick = true;
                        }
                    }
                    if (canStick && chestArmor.hasUpgrade(chest, SteamcraftItems.pitonDeployer)) {
                        if (!world.isRemote) {
                            chest.stackTagCompound.setFloat("x", (float) player.posX);
                            chest.stackTagCompound.setFloat("z", (float) player.posZ);
                            chest.stackTagCompound.setFloat("y", (float) player.posY);
                            chest.stackTagCompound.setInteger("blockX", event.x);
                            chest.stackTagCompound.setInteger("blockY", event.y);
                            chest.stackTagCompound.setInteger("blockZ", event.z);
                            chest.stackTagCompound.setBoolean("grappled", true);
                        }
                        player.motionX = 0.0F;
                        player.motionY = 0.0F;
                        player.motionZ = 0.0F;
                        player.fallDistance = 0.0F;
                    }
                }
            }

            TileEntity tile = world.getTileEntity(x, y, z);
            ItemStack held = player.getHeldItem();
            if (player.isSneaking() && ((tile != null && tile instanceof IDisguisableBlock) ||
              block == SteamcraftBlocks.pipe) && held != null &&
              held.getItem() instanceof ItemBlock) {
                Block block1 = Block.getBlockFromItem(event.entityPlayer.getHeldItem().getItem());
                if (!(block1 instanceof BlockContainer) && !(block1 instanceof ITileEntityProvider) &&
                  (block1.getRenderType() == 0 || block1.getRenderType() == 39 ||
                    block1.getRenderType() == 31) && (block1.renderAsNormalBlock() ||
                  (block1 == Blocks.glass && block == SteamcraftBlocks.pipe))) {
                    event.setCanceled(true);
                }
            }
        }
    }

    List<DamageSource> invalidSources = Arrays.asList(
      DamageSource.drown,
      DamageSource.outOfWorld,
      DamageSource.starve,
      DamageSource.wither
    );

    @SubscribeEvent
    public void burstZincPlate(LivingHurtEvent event) {
        EntityLivingBase entity = event.entityLiving;
        int consumption = Config.zincPlateConsumption;
        float amount = event.ammount;

        if (!invalidSources.contains(event.source)) {
            if (entity instanceof EntityPlayer && hasPower(entity, consumption)) {
                EntityPlayer player = (EntityPlayer) entity;
                float health = player.getHealth();
                float maxHealth = player.getMaxHealth();
                float halfOfMax = maxHealth / 2;
                if (amount >= halfOfMax || health <= halfOfMax) {
                    ItemStack stackWithPlate = null;
                    boolean hasZincPlate = false;
                    for (int i = 1; i < 5; i++) {
                        ItemStack equipment = player.getEquipmentInSlot(i);
                        if (equipment != null) {
                            Item item = equipment.getItem();
                            if (item instanceof ItemExosuitArmor) {
                                ItemExosuitArmor armor = (ItemExosuitArmor) item;
                                if (armor.hasPlates(equipment) &&
                                  UtilPlates.getPlate(equipment.stackTagCompound.getString("plate")).getIdentifier().equals("Zinc")) {
                                    stackWithPlate = equipment;
                                    hasZincPlate = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (hasZincPlate) {
                        ItemStack zincPlates = new ItemStack(SteamcraftItems.steamcraftPlate, 2, 1);
                        World world = player.worldObj;
                        drainSteam(player.getEquipmentInSlot(3), consumption);
                        UtilPlates.removePlate(stackWithPlate);
                        EntityItem entityItem = new EntityItem(world, player.posX, player.posY,
                          player.posZ, zincPlates);
                        world.spawnEntityInWorld(entityItem);
//                        player.setHealth(health - (amount - 10.0F));
                        player.setHealth(health);
                        player.performHurtAnimation();
                        world.playSoundEffect(player.posX, player.posY, player.posZ,
                          "steamcraft:hiss", 2.0F, 0.9F);
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void dropDrops(BlockEvent.HarvestDropsEvent event) {
        if (event.harvester == null || event.block == null) {
            return;
        }

        Random rand = new Random();
        EntityPlayer player = event.harvester;
        Block block = event.block;
        int meta = event.blockMetadata;
        Item otherBlockItem = block.getItemDropped(meta, rand, 0);
        Item blockItem = Item.getItemFromBlock(block);
        MutablePair pair = MutablePair.of(blockItem, meta);
        ItemStack equipped = player.getCurrentEquippedItem();
        if (equipped == null || equipped.getItem() == null) {
            return;
        }

        if (equipped.getItem() instanceof ItemSteamDrill) {
            ItemSteamDrill drill = (ItemSteamDrill) equipped.getItem();
            if (!drill.isWound(equipped)) {
                return;
            }

            if (drill.hasUpgrade(equipped, SteamcraftItems.fortuneUpgrade)) {
                event.drops.clear();
                ArrayList<ItemStack> trueDrops = block.getDrops(event.world, event.x,
                  event.y, event.z, meta, EnchantmentHelper.getFortuneModifier(player) + 2);
                event.drops.addAll(trueDrops);
            }

            if (OreDictHelper.cobblestones.contains(pair)) {
                return;
            }

            if (drill.hasUpgrade(equipped, SteamcraftItems.stoneGrinder)) {
                String harvestTool = block.getHarvestTool(meta);
                if (harvestTool == null || !harvestTool.equals(drill.toolClass())) {
                    return;
                }
                boolean addedNugget = false;
                for (int i = 0; i < event.drops.size(); i++) {
                    ItemStack drop = event.drops.get(i);
                    MutablePair item = MutablePair.of(drop.getItem(), drop.getItemDamage());
                    if (!OreDictHelper.stones.contains(item) && !OreDictHelper.cobblestones.contains(item)) {
                        continue;
                    }

                    event.drops.remove(i);
                    int chance = rand.nextInt(5);
                    if (chance != 3 || addedNugget) {
                        continue;
                    }

                    int index = rand.nextInt(OreDictHelper.nuggets.size());
                    MutablePair nuggetPair = OreDictHelper.nuggets.get(index);
                    int size = rand.nextInt(3) + 1;
                    ItemStack nugget = new ItemStack((Item) nuggetPair.left,
                      size, (int) nuggetPair.right);
                    event.drops.add(nugget);
                    addedNugget = true;
                }
            }

            if (drill.hasUpgrade(equipped, SteamcraftItems.preciseCuttingHead) &&
              block.isToolEffective(drill.toolClass(), meta) &&
              block.canSilkHarvest(event.world, player, event.x, event.y, event.z, meta) &&
              drill.canHarvestBlock(block, equipped)) {
                for (int i = 0; i < event.drops.size(); i++) {
                    event.drops.remove(i);
                }
                event.drops.add(new ItemStack(block, 1, meta));
            }

            if (drill.hasUpgrade(equipped, SteamcraftItems.internalProcessingUnit)) {
                ItemStack out = TileEntitySmasher.REGISTRY.getOutput(new ItemStack(block, 1, meta));
                if (out != null && out.getItem() != null) {
                    if (rand.nextInt(Config.chance) == 0) {
                        out.stackSize *= 2;
                    }
                    for (int i = 0; i < event.drops.size(); i++) {
                        ItemStack drop = event.drops.get(i);
                        if (drop.getItem() == Item.getItemFromBlock(block) && drop.getItemDamage() == meta) {
                            event.drops.remove(i);
                        }
                    }
                    event.drops.add(out);
                    drill.addSteam(equipped, -(2 * drill.steamPerDurability()), player);
                }
            }
        } else if (equipped.getItem() instanceof ItemSteamShovel) {
            ItemSteamShovel shovel = (ItemSteamShovel) equipped.getItem();
            if (!shovel.hasUpgrade(equipped, SteamcraftItems.sifter) || !shovel.isWound(equipped)) {
                return;
            }

            for (int i = 0; i < event.drops.size(); i++) {
                Item item = event.drops.get(i).getItem();
                if (item == blockItem || item == otherBlockItem) {
                    event.drops.remove(i);
                }
            }

            if (OreDictHelper.sands.contains(pair)) {
                int chance = rand.nextInt(8);
                if (chance == 5) {
                    int index = rand.nextInt(OreDictHelper.goldNuggets.size());
                    MutablePair nuggetPair = OreDictHelper.goldNuggets.get(index);
                    int size = rand.nextInt(3) + 1;
                    ItemStack nugget = new ItemStack((Item) nuggetPair.left, size, (int) nuggetPair.right);
                    event.drops.add(nugget);
                    return;
                }
            }

            if (block == Blocks.dirt || block == Blocks.grass || block == Blocks.farmland) {
                int chance = rand.nextInt(6);
                if (chance == 4) {
                    int boneOrSeeds = rand.nextInt(2);
                    int stackSize = rand.nextInt(3) + 1;
                    ItemStack itemstack;
                    if (boneOrSeeds == 1) {
                        itemstack = new ItemStack(Items.bone);
                    } else {
                        itemstack = new ItemStack(Items.wheat_seeds);
                    }
                    itemstack.stackSize = stackSize;
                    event.drops.add(itemstack);
                    return;
                }
            }

            if (event.block == Blocks.gravel) {
                for (int i = 0; i < event.drops.size(); i++) {
                    if (event.drops.get(i).getItem() == Items.flint) {
                        event.dropChance = 90;
                        return;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void rebreath(LivingAttackEvent event) {
        int consumption = Config.rebreatherConsumption;
        if (event.source == DamageSource.drown) {
            EntityLivingBase entity = event.entityLiving;
            if (entity instanceof EntityPlayer && hasPower(entity, consumption)) {
                EntityPlayer player = (EntityPlayer) entity;
                ItemStack equipment = player.getEquipmentInSlot(4);
                if (equipment != null) {
                    Item helmet = equipment.getItem();
                    if (helmet instanceof ItemExosuitArmor) {
                        ItemExosuitArmor helmetArmor = (ItemExosuitArmor) helmet;
                        if (helmetArmor.hasUpgrade(equipment, SteamcraftItems.rebreather)) {
                            drainSteam(player.getEquipmentInSlot(3), consumption);
                            event.setCanceled(true);
                        }
                    }
                }
            }
        }
    }

    private int sideHit;

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        Block block = event.block;
        int meta = event.blockMetadata;
        int x = event.x;
        int y = event.y;
        int z = event.z;
        World world = event.world;
        if (player == null) {
            return;
        }
//            Vec3 vec = player.getLookVec();
        ItemStack equipped = player.getCurrentEquippedItem();
        if (equipped == null || equipped.getItem() == null || block == null) {
            return;
        }

        if (equipped.getItem() instanceof ItemSteamDrill) {
            ItemSteamDrill drill = (ItemSteamDrill) equipped.getItem();
            ItemStack upgrade = UtilSteamTool.getHarvestLevelModifier(equipped);
            if (!drill.isWound(equipped)) {
                return;
            }
            if (upgrade != null) {
                String mat = ItemDrillHeadUpgrade.getMyMaterial(upgrade);
                int harvestLevel = DrillHeadMaterial.materials.get(mat).harvestLevel;
                if (harvestLevel >= block.getHarvestLevel(meta)) {
                    block.harvestBlock(world, player, x, y, z, meta);
                    world.setBlockToAir(x, y, z);
                } else {
                    event.setCanceled(true);
                }
            }
            if (drill.hasUpgrade(equipped, SteamcraftItems.bigDrill) &&
              block.isToolEffective(drill.toolClass(), meta)) {
                mineExtraBlocks(getExtraBlockCoordinates(sideHit), x, y, z, world, drill, equipped, player);
            }
            if (drill.hasUpgrade(equipped, SteamcraftItems.preciseCuttingHead)) {
                event.setExpToDrop(0);
            }
        } else if (equipped.getItem() instanceof ItemSteamShovel) {
            ItemSteamShovel shovel = (ItemSteamShovel) equipped.getItem();
            if (!shovel.isWound(equipped)) {
                return;
            }
            if (shovel.hasUpgrade(equipped, SteamcraftItems.rotaryBlades) &&
              block.isToolEffective(shovel.toolClass(), meta)) {
                mineExtraBlocks(getExtraBlockCoordinates(sideHit), x, y, z, world, shovel, equipped, player);
            } else if (shovel.hasUpgrade(equipped, SteamcraftItems.backhoe)) {
                boolean isFalling = block instanceof BlockFalling;
                int end = isFalling ? y + Config.backhoeRange : y;
                for (int i = y - Config.backhoeRange; i < end; i++) {
                    if (i < 0) {
                        continue;
                    }
                    Block block1 = world.getBlock(x, i, z);
                    int meta1 = world.getBlockMetadata(x, i, z);
                    if (!block1.isToolEffective(shovel.toolClass(), meta) || !block1.canHarvestBlock(player, meta)) {
                        continue;
                    }
                    if (Item.getItemFromBlock(block) == Item.getItemFromBlock(block1)) {
                        world.setBlockToAir(x, i, z);
                        block.harvestBlock(world, player, x, i, z, meta1);
                    } else {
                        break;
                    }
                }
            }
        } else if (equipped.getItem() instanceof ItemSteamAxe) {
            ItemSteamAxe axe = (ItemSteamAxe) equipped.getItem();
            if (!axe.isWound(equipped)) {
                return;
            }
            if (axe.hasUpgrade(equipped, SteamcraftItems.treeFeller) &&
              block.isToolEffective(axe.toolClass(), meta)) {
                fellBlocks(world, x, y, z, player, equipped);
            }
            if (axe.hasUpgrade(equipped, SteamcraftItems.forestFire)) {
                burnBlocks(world, x, y, z, player, equipped);
            }
        }

        if (equipped.getItem() instanceof ISteamTool) {
            ISteamTool tool = (ISteamTool) equipped.getItem();
            if (tool.isWound(equipped) && tool.hasUpgrade(equipped, SteamcraftItems.overclocker)) {
                tool.addSteam(equipped, -tool.steamPerDurability(), player);
            }
        }
    }

    /**
     * The Hash of quick lava blocks to delete.
     * Key: Pair of Integer and Tuple3. Integer is dimension, Tuple3 are the coordinates.
     * Value: Integer, number of ticks to wait. Cannot be more than 30 or bad things will happen.
     */
    public static HashMap<MutablePair<Integer, Tuple3>, Integer> quickLavaBlocks = new HashMap<>();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void placeLava(BlockEvent.HarvestDropsEvent event) {
        EntityPlayer player = event.harvester;
        Block block = event.block;
        int x = event.x;
        int y = event.y;
        int z = event.z;
        World world = event.world;
        if (player == null) {
            return;
        }
        ItemStack equipped = player.getCurrentEquippedItem();
        if (equipped == null || equipped.getItem() == null || block == null) {
            return;
        }
        if (equipped.getItem() instanceof ItemSteamDrill) {
            ItemSteamDrill drill = (ItemSteamDrill) equipped.getItem();
            if (drill.hasUpgrade(equipped, SteamcraftItems.thermalDrill) && drill.isWound(equipped)) {
                world.setBlock(x, y, z, Blocks.lava);
                quickLavaBlocks.put(MutablePair.of(player.dimension, new Tuple3(x, y, z)), new Random().nextInt(30) + 1);
                event.drops.clear();
            }
        }
    }

    public static HashMap<MutablePair<EntityPlayer, Tuple3>, Integer> charges = new HashMap<>();
    public static final int PEACEFUL_CHARGE = 12 * 20;
    public static final int EASY_CHARGE_CAP = 14 * 20;
    public static final int EASY_CHARGE_MIN = 8 * 20;
    public static final int NORMAL_CHARGE_CAP = 16 * 20;
    public static final int NORMAL_CHARGE_MIN = 6 * 20;
    public static final int HARD_CHARGE_CAP = 18 * 20;
    public static final int HARD_CHARGE_MIN = 4 * 20;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void placeCharge(BlockEvent.BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        int x = event.x;
        int y = event.y;
        int z = event.z;
        if (player == null) {
            return;
        }
        ItemStack equipped = player.getCurrentEquippedItem();
        if (equipped == null || equipped.getItem() == null) {
            return;
        }
        if (equipped.getItem() instanceof ItemSteamDrill) {
            ItemSteamDrill drill = (ItemSteamDrill) equipped.getItem();
            if (drill.hasUpgrade(equipped, SteamcraftItems.chargePlacer) && drill.isWound(equipped)) {
                Random rand = new Random();
                drill.addSteam(equipped, -(2 * drill.steamPerDurability()), player);
                if (player.worldObj.difficultySetting == EnumDifficulty.HARD && rand.nextInt(100) < 15) {
                    return;
                }
                int max = 0;
                int min = 0;
                int constant = 0;
                boolean useConstant = false;
                switch (player.worldObj.difficultySetting) {
                    case HARD: {
                        max = HARD_CHARGE_CAP;
                        min = HARD_CHARGE_MIN;
                        break;
                    }
                    case NORMAL: {
                        max = NORMAL_CHARGE_CAP;
                        min = NORMAL_CHARGE_MIN;
                        break;
                    }
                    case EASY: {
                        max = EASY_CHARGE_CAP;
                        min = EASY_CHARGE_MIN;
                        break;
                    }
                    case PEACEFUL: {
                        constant = PEACEFUL_CHARGE;
                        useConstant = true;
                        break;
                    }
                    default: {}
                }
                MutablePair<EntityPlayer, Tuple3> pair = MutablePair.of(player, new Tuple3(x, y, z));
                if (useConstant) {
                    charges.put(pair, constant);
                } else {
                    charges.put(pair, rand.nextInt((max - min) + 1) + min);
                }
            }
        }
    }

    /**
     * Mines all of the log blocks above the starting coordinate.
     * @param world The world instance.
     * @param startX The starting X coordinate.
     * @param startY The starting Y coordinate.
     * @param startZ The starting Z coordinate.
     * @param player The player doing the felling.
     * @param axe The axe's ItemStack
     */
    private void fellBlocks(World world, int startX, int startY, int startZ, EntityPlayer player, ItemStack axe) {
        ItemSteamAxe sAxe = (ItemSteamAxe) axe.getItem();
        for (int y = startY; y < 256; y++) {
            Block block = world.getBlock(startX, y, startZ);
            if (OreDictHelper.arrayHasItem(OreDictHelper.logs, Item.getItemFromBlock(block))) {
                int meta = world.getBlockMetadata(startX, y, startZ);
                world.setBlockToAir(startX, y, startZ);
                block.harvestBlock(world, player, startX, y, startZ, meta);
                if (y % 2 == 0) {
                    if (!sAxe.addSteam(axe, -sAxe.steamPerDurability(), player)) {
                        break;
                    }
                }
            } else {
                break;
            }
        }
    }

    /**
     * Burns all log blocks within a 5 block radius.
     * @param world The world
     * @param startX The starting X coordinate
     * @param y The permanent Y coordinate
     * @param startZ The starting Z coordinate
     * @param player The player doing the burning
     * @param axe The steam axe ItemStack.
     */
    private void burnBlocks(World world, int startX, int y, int startZ, EntityPlayer player, ItemStack axe) {
        ItemSteamAxe sAxe = (ItemSteamAxe) axe.getItem();
        for (int x = startX - 5; x < startX + 5; x++) {
            for (int z = startZ - 5; z < startZ + 5; z++) {
                Block block = world.getBlock(x, y, z);
                if (OreDictHelper.arrayHasItem(OreDictHelper.logs, Item.getItemFromBlock(block))) {
                    world.setBlock(x, y, z, Blocks.fire);
                    if (!sAxe.addSteam(axe, -(sAxe.steamPerDurability() * 2), player)) {
                        return;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void autosmelt(BlockEvent.HarvestDropsEvent event) {
        if (event.harvester == null || event.block == null) {
            return;
        }
        EntityPlayer player = event.harvester;
        Block block = event.block;
        ItemStack equipped = player.getCurrentEquippedItem();
        if (equipped == null || equipped.getItem() == null || !(equipped.getItem() instanceof ISteamTool)) {
            return;
        }
        ISteamTool tool = (ISteamTool) equipped.getItem();
        if (!tool.hasUpgrade(equipped, SteamcraftItems.autosmelting) || !tool.isWound(equipped) ||
          event.drops.isEmpty() || !block.isToolEffective(tool.toolClass(), event.blockMetadata)) {
            return;
        }
        int itemsSmelted = 0;
        for (int i = 0; i < event.drops.size(); i++) {
            ItemStack drop = event.drops.get(i);
            if (drop == null || drop.getItem() == null) {
                continue;
            }

            int meta = drop.getItemDamage() == OreDictionary.WILDCARD_VALUE ? 0 : drop.getItemDamage();
            MutablePair<Item, Integer> input = MutablePair.of(drop.getItem(), meta);
            if (SteamcraftRegistry.steamingRecipes.containsKey(input)) {
                event.drops.remove(i);
                MutablePair<Item, Integer> output = SteamcraftRegistry.steamingRecipes.get(input);
                ItemStack stack = new ItemStack(output.left, drop.stackSize, output.right);
                event.drops.add(stack);
                itemsSmelted += 1;
            } else {
                ItemStack output = FurnaceRecipes.smelting().getSmeltingResult(drop);
                if (output == null || output.getItem() == null) {
                    continue;
                }
                event.drops.remove(i);
                event.drops.add(i, output.copy());
                itemsSmelted += 1;
            }
        }
        if (itemsSmelted > 0) {
            tool.addSteam(equipped, -(itemsSmelted * tool.steamPerDurability()), player);
        }
    }

    /**
     * Gets whether the player can perform actions using the Void upgrade.
     *
     * @param equipped The tool ItemStack.
     * @return Whether they are using a ISteamTool that is wound and has the void upgrade.
     */
    private boolean canDoVoidThings(ItemStack equipped) {
        if (equipped == null || equipped.getItem() == null || !(equipped.getItem() instanceof ISteamTool)) {
            return false;
        }

        ISteamTool tool = (ISteamTool) equipped.getItem();
        return !(!tool.isWound(equipped) || !tool.hasUpgrade(equipped, SteamcraftItems.theVoid));
    }

    /**
     * Adds the drops to the inventory.
     *
     * @param drops An ArrayList of items to add to the inventory.
     * @param inv   The inventory to add items to.
     * @return The items that did not get added.
     */
    private ArrayList<ItemStack> addToInventory(ArrayList<ItemStack> drops, IInventory inv) {
        ArrayList<ItemStack> failures = new ArrayList<>();
        for (ItemStack drop : drops) {
            if (drop == null) {
                continue;
            }
            boolean added = false;
            for (int i = 0; i < inv.getSizeInventory(); i++) {
                ItemStack stackInSlot = inv.getStackInSlot(i);
                if (stackInSlot == null) {
                    inv.setInventorySlotContents(i, drop);
                    added = true;
                    break;
                } else if (stackInSlot.getItem() == drop.getItem() &&
                  stackInSlot.getItemDamage() == drop.getItemDamage() &&
                  stackInSlot.stackSize + drop.stackSize < stackInSlot.getMaxStackSize()) {
                    stackInSlot.stackSize += drop.stackSize;
                    inv.setInventorySlotContents(i, stackInSlot);
                    added = true;
                    break;
                }
            }
            if (!added) {
                failures.add(drop);
            }
        }
        return failures;
    }

    /**
     * Sets the X, Y, and Z NBT data for the given NBT compound.
     * @param nbt The NBTTagCompound to set the data in.
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @param z The Z coordinate.
     * @return The modified NBTTagCompound.
     */
    private NBTTagCompound setVoidInventoryNBT(NBTTagCompound nbt, int x, int y, int z) {
        nbt.setInteger("x", x);
        nbt.setInteger("y", y);
        nbt.setInteger("z", z);
        return nbt;
    }

    /**
     * Plays the sounds and particles that indicate a void inventory set/unset.
     * @param world The world
     * @param x The X coordinate of the inventory
     * @param y The Y coordinate of the inventory
     * @param z The Z coordinate of the inventory
     */
    private void indicateVoidSet(World world, int x, int y, int z) {
        Random rand = new Random();
        if (world.isRemote) {
            for (int i = 0; i < rand.nextInt(10) + 5; i++) {
                // Particle spawning code taken from EntityEnderman.
                world.spawnParticle("portal", x + (rand.nextDouble() - 0.5D),
                  y + rand.nextDouble(), z + (rand.nextDouble() - 0.5D),
                  (rand.nextDouble() - 0.5D) * 2.0D, -rand.nextDouble(),
                  (rand.nextDouble() - 0.5D) * 2.0D);
            }
        }
        world.playSoundEffect(x, y, z, "mob.endermen.portal", 1F, 1F);
    }

    @SubscribeEvent
    public void setVoidInventory(PlayerInteractEvent event) {
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        EntityPlayer player = event.entityPlayer;
        if (!player.isSneaking()) {
            return;
        }
        World world = event.world;
        int x = event.x;
        int y = event.y;
        int z = event.z;
        ItemStack equipped = player.getCurrentEquippedItem();
        TileEntity tile = world.getTileEntity(x, y, z);
        if (!canDoVoidThings(equipped) || !(tile instanceof IInventory) ||
          ((IInventory) tile).getSizeInventory() < 1) {
            return;
        }

        if (!equipped.hasTagCompound()) {
            equipped.setTagCompound(new NBTTagCompound());
        }
        if (equipped.stackTagCompound.hasKey("voidInventory")) {
            NBTTagCompound nbt = equipped.stackTagCompound.getCompoundTag("voidInventory");
            int existingX = nbt.getInteger("x");
            int existingY = nbt.getInteger("y");
            int existingZ = nbt.getInteger("z");
            if (existingX == x && existingY == y && existingZ == z) {
                equipped.stackTagCompound.removeTag("voidInventory");
            } else {
                equipped.stackTagCompound.setTag("voidInventory", setVoidInventoryNBT(nbt, x, y, z));
            }
        } else {
            equipped.stackTagCompound.setTag("voidInventory", setVoidInventoryNBT(new NBTTagCompound(), x, y, z));
        }
        indicateVoidSet(world, x, y, z);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void sendToVoid(BlockEvent.HarvestDropsEvent event) {
        if (event.harvester == null || event.block == null) {
            return;
        }

        EntityPlayer player = event.harvester;
        World world = event.world;
        ItemStack equipped = player.getCurrentEquippedItem();
        if (!canDoVoidThings(equipped) || player.isSneaking()) {
            return;
        }

        ArrayList<ItemStack> failures = new ArrayList<>();
        if (equipped.hasTagCompound() && equipped.stackTagCompound.hasKey("voidInventory")) {
            NBTTagCompound nbt = equipped.stackTagCompound.getCompoundTag("voidInventory");
            int invX = nbt.getInteger("x");
            int invY = nbt.getInteger("y");
            int invZ = nbt.getInteger("z");
            TileEntity tile = world.getTileEntity(invX, invY, invZ);
            int x = event.x;
            int y = event.y;
            int z = event.z;
            if (tile == null || (invX == x && invY == y && invZ == z)) {
                equipped.getTagCompound().removeTag("voidInventory");
                indicateVoidSet(world, x, y, z);
                return;
            }
            if (tile instanceof IInventory) {
                failures = addToInventory(event.drops, (IInventory) tile);
            }
            tile.updateEntity();
        } else {
            InventoryEnderChest ender = player.getInventoryEnderChest();
            failures = addToInventory(event.drops, ender);
            ender.saveInventoryToNBT();
        }
        event.drops.clear();
        if (!failures.isEmpty()) {
            event.drops.addAll(failures);
        }
    }

    /**
     * Gets whether the block can be tilled into farmland.
     * @param block The block to check
     * @return True if it is dirt or grass, else false.
     */
    private boolean isFarmable(Block block) {
        return (block != null && (block == Blocks.dirt || block == Blocks.grass));
    }

    @SubscribeEvent
    public void onLeftClickBlock(PlayerInteractEvent event) {
        if (event.action == Action.LEFT_CLICK_BLOCK) {
            sideHit = event.face;
            World world = event.world;
            if (world.isRemote) {
                return;
            }
            EntityPlayer player = event.entityPlayer;
            ItemStack equipped = player.getCurrentEquippedItem();
            if (equipped == null) {
                return;
            }
            if (equipped.getItem() instanceof ItemSteamAxe) {
                ItemSteamAxe axe = (ItemSteamAxe) equipped.getItem();
                if (!axe.isWound(equipped)) {
                    return;
                }

                if (axe.hasUpgrade(equipped, SteamcraftItems.leafBlower)) {
                    mineExtraBlocks(getExtraBlockCoordinates(sideHit), event.x, event.y, event.z, world,
                      axe, equipped, player);
                }
            } else if (equipped.getItem() instanceof ItemSteamShovel) {
                ItemSteamShovel shovel = (ItemSteamShovel) equipped.getItem();
                if (shovel.hasUpgrade(equipped, SteamcraftItems.cultivator) &&
                  shovel.isWound(equipped)) {
                    int[][] coords = extraBlocksVertical;
                    for (int[] aCoordinateArray : coords) {
                        int thisX = event.x + aCoordinateArray[0];
                        int thisY = event.y + aCoordinateArray[1];
                        int thisZ = event.z + aCoordinateArray[2];

                        Block block1 = world.getBlock(thisX, thisY, thisZ);
                        if (isFarmable(block1)) {
                            world.setBlockToAir(thisX, thisY, thisZ);
                            world.setBlock(thisX, thisY, thisZ, Blocks.farmland);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void openMerchant(EntityInteractEvent event) {
        EntityPlayer player = event.entityPlayer;
        Entity target = event.target;
        ItemStack held = player.getHeldItem();
        if (playerHasFrequencyShifter(player) && (target instanceof EntityWolf ||
          target instanceof EntityOcelot)) {
            boolean flag = held == null || !(held.getItem() instanceof ItemNameTag);
            if (!flag) {
                return;
            }
            EntityLiving living = (EntityLiving) target;
            ExtendedPropertiesMerchant nbt = (ExtendedPropertiesMerchant)
              living.getExtendedProperties(Steamcraft.MERCHANT_PROPERTY_ID);
            if (nbt.totalTrades > nbt.maximumTrades) {
                if (living instanceof EntityWolf) {
                    EntityWolf wolf = (EntityWolf) living;
                    wolf.setAngry(true);
                } else {
                    EntityOcelot cat = (EntityOcelot) living;
                    living.targetTasks.addTask(3, new EntityAIHurtByTarget(cat, true));
                }
            } else {
                if (living.hasCustomNameTag()) {
                    nbt.merchantName = living.getCustomNameTag();
                }
                String name = nbt.merchantName;
                FrequencyMerchant merchant = new FrequencyMerchant(living, name);
                merchant.setCustomer(player);
                player.displayGUIMerchant(merchant, name);
                nbt.totalTrades += 1;
            }
        }
    }

    @SubscribeEvent
    public void doChainsaw(LivingAttackEvent event) {
        if (!(event.source.getSourceOfDamage() instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) event.source.getSourceOfDamage();
        ItemStack equipped = player.getCurrentEquippedItem();
        if (equipped == null || equipped.getItem() == null || !(equipped.getItem() instanceof ItemSteamAxe)) {
            return;
        }
        ItemSteamAxe axe = (ItemSteamAxe) equipped.getItem();
        if (!axe.isWound(equipped) || !axe.hasUpgrade(equipped, SteamcraftItems.chainsaw)) {
            return;
        }

        if (!player.worldObj.isRemote) {
            event.entityLiving.attackEntityFrom(DamageSource.generic, 9.0F);
            event.entityLiving.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 10, 10));
        }
    }

    @SubscribeEvent
    public void toggleDrillDash(LivingEvent.LivingJumpEvent event) {
        if (!(event.entityLiving instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) event.entityLiving;
        ItemStack equipped = player.getCurrentEquippedItem();
        if (equipped == null || !player.isSneaking()) {
            return;
        }
        Item equippedItem = equipped.getItem();
        if (equippedItem == null || !(equippedItem instanceof ItemSteamDrill)) {
            return;
        }
        ItemSteamDrill drill = (ItemSteamDrill) equippedItem;
        if (!drill.isWound(equipped) || !drill.hasUpgrade(equipped, SteamcraftItems.battleDrill)) {
            return;
        }

        Vec3 vector = player.getLook(0.5F);

        double total = Math.abs(vector.zCoord + vector.xCoord);
        if (vector.yCoord < total) {
            vector.yCoord = total;
        }

        player.motionZ += vector.zCoord * 2.5;
        player.motionX += vector.xCoord * 2.5;

        EntityLivingBase target = getEntityFromPlayer(player);
        if (target == null) {
            return;
        }

        target.attackEntityFrom(DamageSource.causePlayerDamage(player), 9.0F);
        drill.addSteam(equipped, -(Config.battleDrillConsumption * drill.steamPerDurability()), player);
    }

    /**
     * Gets a single entity from the player's look vec. Scans in a 5 block radius around the player,
     * and returns the "first" result.
     * @param player The player
     * @return The EntityLivingBase near the player.
     */
    private EntityLivingBase getEntityFromPlayer(EntityPlayer player) {
        Vec3 vec = player.getLookVec();
        double x = vec.xCoord + player.posX;
        double y = vec.yCoord + player.posY;
        double z = vec.zCoord + player.posZ;

        AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(x - 5, y - 5, z - 5, x + 5, y + 5, z + 5);
        List entities = player.worldObj.getEntitiesWithinAABBExcludingEntity(player, aabb);
        for (Object object : entities) {
            if (object instanceof EntityLivingBase) {
                EntityLivingBase target = (EntityLivingBase) object;
                if (player.canEntityBeSeen(target) && target.canBeCollidedWith()) {
                    return target;
                }
            }
        }
        return null;
    }

    @SubscribeEvent
    public void updateBlockBreakSpeed(PlayerEvent.BreakSpeed event) {
        ItemStack equipped = event.entityPlayer.getCurrentEquippedItem();
        Block block = event.block;
        if (equipped != null && equipped.getItem() != null && block != null) {
            float newSpeed = 0.0F;
            float original = event.originalSpeed;
            if (equipped.getItem() instanceof ItemSteamDrill) {
                ItemSteamDrill drill = (ItemSteamDrill) equipped.getItem();
                if (drill.isWound(equipped)) {
                    if (drill.hasUpgrade(equipped, SteamcraftItems.bigDrill)) {
                        newSpeed = original * 0.7F;
                    }
                    if (drill.hasUpgrade(equipped, SteamcraftItems.internalProcessingUnit)) {
                        if (newSpeed == 0.0F) {
                            newSpeed = original / 2;
                        } else {
                            newSpeed /= 2;
                        }
                    }
                    if (drill.hasUpgrade(equipped, SteamcraftItems.thermalDrill) ||
                      drill.hasUpgrade(equipped, SteamcraftItems.chargePlacer)) {
                        if (newSpeed == 0.0F) {
                            newSpeed = original * 5;
                        } else {
                            newSpeed *= 5;
                        }
                    }
                    if (drill.hasUpgrade(equipped, SteamcraftItems.battleDrill)) {
                        if (newSpeed == 0.0F) {
                            newSpeed = original / 1.7F;
                        } else {
                            newSpeed /= 1.7F;
                        }
                    }
                }
            } else if (equipped.getItem() instanceof ItemSteamAxe) {
                ItemSteamAxe axe = (ItemSteamAxe) equipped.getItem();
                if (axe.isWound(equipped)) {
                    if (axe.hasUpgrade(equipped, SteamcraftItems.leafBlower)) {
                        newSpeed = original / 5F;
                    }
                    if (axe.hasUpgrade(equipped, SteamcraftItems.treeFeller)) {
                        if (newSpeed == 0.0F) {
                            newSpeed = original * 0.7F;
                        } else {
                            newSpeed *= 0.7F;
                        }
                    }
                    if (axe.hasUpgrade(equipped, SteamcraftItems.chainsaw)) {
                        if (newSpeed == 0.0F) {
                            newSpeed = original / 1.7F;
                        } else {
                            newSpeed /= 1.7F;
                        }
                    }
                }
            } else if (equipped.getItem() instanceof ItemSteamShovel) {
                ItemSteamShovel shovel = (ItemSteamShovel) equipped.getItem();
                if (shovel.isWound(equipped) && shovel.hasUpgrade(equipped, SteamcraftItems.rotaryBlades)) {
                    newSpeed = original * 0.425F;
                }
            }
            if (equipped.getItem() instanceof ISteamTool) {
                ISteamTool tool = (ISteamTool) equipped.getItem();
                if (tool.isWound(equipped) && tool.hasUpgrade(equipped, SteamcraftItems.overclocker)) {
                    if (newSpeed == 0.0F) {
                        newSpeed = original * 2.5F;
                    } else {
                        newSpeed *= 2.5F;
                    }
                }
            }
            if (newSpeed != 0.0F) {
                event.newSpeed = newSpeed;
            }
        }
    }

    @SubscribeEvent
    public void handlePainfulFrequencies(AnimalTradeEvent event) {
        EntityLiving entity = event.salesperson;
        NBTTagCompound nbt = entity.getEntityData();
        if (nbt.getInteger("totalTrades") > nbt.getInteger("maximumTrades")) {
            entity.setAttackTarget(event.customer);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void ignoreChatMessage(ClientChatReceivedEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.thePlayer;
        World world = mc.theWorld;
        String message = event.message.getUnformattedText();
        Matcher matcher = Pattern.compile("<(.+?)>").matcher(message);
        if (matcher.find()) {
            EntityPlayer messager = world.getPlayerEntityByName(matcher.group(0));
            if (messager != null) {
                if (!messager.getDisplayName().equals(player.getDisplayName()) &&
                  playerHasFrequencyShifter(messager) && playerHasFrequencyShifter(player)) {
                    event.setCanceled(true);
                }
            }
        }
    }

    /**
     * Checks whether the given player has the Frequency Shifter upgrade, and enough steam in their suit.
     * @param player The player to check.
     * @return True if the player has the Frequency Shifter upgrade.
     */
    private boolean playerHasFrequencyShifter(EntityPlayer player) {
        ItemStack helmet = player.getEquipmentInSlot(4);
        if (helmet != null && hasPower(player, 1)) {
            Item helmetItem = helmet.getItem();
            if (helmetItem instanceof ItemExosuitArmor) {
                ItemExosuitArmor helmetArmor = (ItemExosuitArmor) helmetItem;
                if (helmetArmor.hasUpgrade(helmet, SteamcraftItems.frequencyShifter)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int[][] extraBlocksSide = {
      { 0, 1, -1 }, { 0, 1, 0 }, { 0, 1, 1 },
      { 0, 0, -1 }, { 0, 0, 0 }, { 0, 0, 1 },
      { 0, -1, 0 }, { 0, -1, 0 }, { 0, -1, 1 }
    };

    private static int[][] extraBlocksForward = {
      { -1, 1, 0 }, { 0, 1, 0 }, { 1, 1, 0 },
      { -1, 0, 0 }, { 0, 0, 0 }, { 1, 0, 0 },
      { -1, -1, 0 }, { 0, -1, 0 }, { 1, -1, 0 }
    };

    private static int[][] extraBlocksVertical = {
      { -1, 0, 1 }, { 0, 0, 1 }, { 1, 0, 1 },
      { -1, 0, 0 }, { 0, 0, 0 }, { 1, 0, 0 },
      { -1, 0, -1 }, { 0, 0, -1 }, { 1, 0, -1 }
    };

    private int[][] getExtraBlockCoordinates(int sideHit) {
        switch (sideHit) {
            case 5: return extraBlocksSide;
            case 4: return extraBlocksSide;
            case 3: return extraBlocksForward;
            case 1: return extraBlocksVertical;
            case 0: return extraBlocksVertical;
            default: return extraBlocksForward;
        }
    }

    /**
     * This mines the extra blocks within the coordinate array.
     * @param coordinateArray The array of arrays containing the coordinates to add to x, y, z.
     * @param x The start X coordinate.
     * @param y The start Y coordinate.
     * @param z The start Z coordinate.
     * @param world The world.
     * @param tool The tool mining.
     * @param toolStack The ItemStack of the tool.
     */
    private void mineExtraBlocks(int[][] coordinateArray, int x, int y, int z, World world, ItemTool tool, ItemStack toolStack, EntityPlayer player) {
//        boolean isDrill = tool instanceof ItemSteamDrill;
        boolean isAxe = tool instanceof ItemSteamAxe;
        boolean isShovel = tool instanceof ItemSteamShovel;
        for (int[] aCoordinateArray : coordinateArray) {
            int thisX = x + aCoordinateArray[0];
            int thisY = y + aCoordinateArray[1];
            int thisZ = z + aCoordinateArray[2];
            Block block = world.getBlock(thisX, thisY, thisZ);
            int meta = world.getBlockMetadata(thisX, thisY, thisZ);
            boolean flag = tool.canHarvestBlock(block, toolStack);
            if (!flag && isShovel) {
                // For some reason, flag is false when using the Steam Shovel.
                String toolClass = block.getHarvestTool(meta);
                flag = ((toolClass != null) && toolClass.equals(((ISteamTool) tool).toolClass()));
            }
            if (isAxe) {
                MutablePair item = MutablePair.of(block.getItem(world, thisX, thisY, thisZ), meta);
                flag = OreDictHelper.leaves.contains(item);
            }
            if (block != null && !block.isAir(world, thisX, thisY, thisZ) && flag) {
//                world.spawnParticle("")
//                world.func_147480_a(thisX, thisY, thisZ, false);
                world.setBlockToAir(thisX, thisY, thisZ);
                block.harvestBlock(world, player, thisX, thisY, thisZ, meta);
            }
        }
    }
}
