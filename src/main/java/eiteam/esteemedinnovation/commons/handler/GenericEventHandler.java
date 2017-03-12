package eiteam.esteemedinnovation.commons.handler;

import eiteam.esteemedinnovation.api.ChargableUtility;
import eiteam.esteemedinnovation.api.SteamChargable;
import eiteam.esteemedinnovation.api.book.BookPageRegistry;
import eiteam.esteemedinnovation.api.enhancement.EnhancementRegistry;
import eiteam.esteemedinnovation.api.exosuit.ExosuitPlate;
import eiteam.esteemedinnovation.api.exosuit.ExosuitUtility;
import eiteam.esteemedinnovation.api.exosuit.UtilPlates;
import eiteam.esteemedinnovation.api.steamnet.SteamNetworkRegistry;
import eiteam.esteemedinnovation.api.steamnet.data.SteamNetworkData;
import eiteam.esteemedinnovation.api.util.ItemStackUtility;
import eiteam.esteemedinnovation.api.util.SPLog;
import eiteam.esteemedinnovation.api.wrench.PipeWrench;
import eiteam.esteemedinnovation.api.wrench.WrenchDisplay;
import eiteam.esteemedinnovation.armor.exosuit.steam.ItemSteamExosuitArmor;
import eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.frequency.AnimalData;
import eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.frequency.AnimalDataSerializer;
import eiteam.esteemedinnovation.armor.tophat.VillagerDataSerializer;
import eiteam.esteemedinnovation.book.BookPieceUnlockedStateChangePacket;
import eiteam.esteemedinnovation.book.GuiJournal;
import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.commons.CrossMod;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.commons.capabilities.player.PlayerData;
import eiteam.esteemedinnovation.commons.capabilities.player.PlayerDataSerializer;
import eiteam.esteemedinnovation.commons.network.JumpValueChangePacket;
import eiteam.esteemedinnovation.commons.util.BaublesUtility;
import eiteam.esteemedinnovation.commons.util.ReflectionHelper;
import eiteam.esteemedinnovation.firearms.flintlock.ItemFirearm;
import eiteam.esteemedinnovation.firearms.rocket.ItemRocketLauncher;
import eiteam.esteemedinnovation.misc.integration.EnchiridionIntegration;
import eiteam.esteemedinnovation.storage.item.canister.EntityCanisterItem;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.PotionTypes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.ISpecialArmor.ArmorProperties;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.*;

import static eiteam.esteemedinnovation.armor.ArmorModule.*;
import static eiteam.esteemedinnovation.armor.exosuit.steam.upgrades.frequency.AnimalDataStorage.POSSIBLE_NAMES;
import static eiteam.esteemedinnovation.book.BookModule.BOOK;
import static eiteam.esteemedinnovation.firearms.FirearmModule.ROCKET_LAUNCHER;
import static eiteam.esteemedinnovation.tools.ToolsModule.SURVIVALIST_TOOLKIT;

public class GenericEventHandler {
    private static final UUID uuid = UUID.fromString("bbd786a9-611f-4c31-88ad-36dc9da3e15c");
    private static final AttributeModifier exoBoost = new AttributeModifier(uuid, "EXOMOD", 0.2D, 2).setSaved(true);
    private static final UUID uuid2 = UUID.fromString("33235dc2-bf3d-40e4-ae0e-78037c7535e6");
    private static final AttributeModifier exoBoostBad = new AttributeModifier(uuid2, "EXOMODBAD", -0.2D, 2).setSaved(true);
    private static final UUID uuid3 = UUID.fromString("33235dc2-bf3d-40e4-ae0e-78037c7535e7");
    private static final AttributeModifier exoSwimBoost = new AttributeModifier(uuid3, "EXOSWIMBOOST", 1.0D, 2).setSaved(true);
    private static final ResourceLocation icons = new ResourceLocation(EsteemedInnovation.MOD_ID + ":textures/gui/icons.png");
    public static boolean lastViewVillagerGui = false;
    public static int use = -1;
    boolean lastWearing = false;
    boolean worldStartUpdate = false;
    private SPLog log = EsteemedInnovation.log;
    private static boolean isShiftDown;
    private static final Potion SLOWNESS_POTION = Potion.getPotionById(PotionType.getID(PotionTypes.SLOWNESS));

    private Map<UUID, Boolean> prevIsJumping = new HashMap<>();

    /**
     * Sends a JumpValueChangePacket to the server from the client whenever an entity's clientside isJumping value
     * changes. The rationale behind this is that isJumping is only set on the server when the entity is riding
     * another entity (minecart, horse, etc), and it is different than simply subscribing to LivingJumpEvent because
     * isJumping is true when the player has already jumped but is still holding the key, whereas LivingJumpEvent is
     * fired once per physical jump. It is probably a good idea to think of isJumping as isJumpKeybindDown.
     */
    @SubscribeEvent
    public void sendPlayerInputPacketToServer(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase elb = event.getEntityLiving();
        if (elb.worldObj.isRemote) {
            boolean isJumping;
            try {
                isJumping = ReflectionHelper.getIsEntityJumping(elb);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return;
            }

            UUID id = elb.getUniqueID();

            if (!prevIsJumping.containsKey(id) || prevIsJumping.get(id) != isJumping) {
                prevIsJumping.put(id, isJumping);
                EsteemedInnovation.channel.sendToServer(new JumpValueChangePacket(isJumping));
            }
        }
    }

//    @SubscribeEvent the fuck kind of commenting out is this
//    public void handleMobDrop(LivingDropsEvent event) {
//        if (event.entityLiving instanceof EntityCreeper) {
//            int gunpowder = 0;
//            for (EntityItem drop : event.drops) {
//                if (drop.getEntityItem().getItem() == Items.gunpowder) {
//                    gunpowder+=drop.getEntityItem().stackSize;
//                }
//            }
//            if (gunpowder >= 2 && !event.entityLiving.worldObj.isRemote && event.entityLiving.worldObj.rand.nextBoolean()) {
//                int dropsLeft = 2;
//                ArrayList<EntityItem> dropsToRemove = new ArrayList<EntityItem>();
//                EntityItem baseItem = null;
//                for (EntityItem drop : event.drops) {
//                    if (baseItem == null && drop.getEntityItem().getItem() == Items.gunpowder) {
//                        baseItem = drop;
//                    }
//                    if (dropsLeft > 0 && drop.getEntityItem().getItem() == Items.gunpowder) {
//                        if (drop.getEntityItem().stackSize <= dropsLeft) {
//                            dropsLeft -= drop.getEntityItem().stackSize;
//                            dropsToRemove.add(drop);
//                        }
//                        else
//                        {
//                            drop.getEntityItem().stackSize -= dropsLeft;
//                            dropsLeft = 0;
//                        }
//                    }
//                }
//                for (EntityItem drop : dropsToRemove) {
//                    event.drops.remove(drop);
//                }
//                baseItem.setEntityItemStack(new ItemStack(SteamcraftItems.steamcraftCrafting,1,5));
//                event.drops.add(baseItem);
//            }
//        }
//    }

    @SubscribeEvent
    public void initializeEntityCapabilities(AttachCapabilitiesEvent.Entity event) {
        Entity entity = event.getEntity();
        if (entity instanceof EntityPlayer) {
            event.addCapability(new ResourceLocation(EsteemedInnovation.MOD_ID, "PlayerData"), new PlayerDataSerializer());
        } else if (entity instanceof EntityVillager) {
            event.addCapability(new ResourceLocation(EsteemedInnovation.MOD_ID, "VillagerData"), new VillagerDataSerializer());
        } else if (entity instanceof EntityWolf || entity instanceof EntityOcelot) {
            Random rand = entity.worldObj.rand;
            AnimalData data = new AnimalData.DefaultImplementation(rand.nextInt(7), 0, POSSIBLE_NAMES[rand.nextInt(POSSIBLE_NAMES.length)], null);
            event.addCapability(new ResourceLocation(EsteemedInnovation.MOD_ID, "AnimalData"), new AnimalDataSerializer(data));
        }
    }

    @SubscribeEvent
    public void clonePlayerDataOnDeath(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) {
            return;
        }
        EntityPlayer original = event.getOriginal();
        if (original instanceof EntityPlayerMP) {
            EntityPlayer newP = event.getEntityPlayer();
            PlayerData newData = newP.getCapability(EsteemedInnovation.PLAYER_DATA, null);
            PlayerData originalData = original.getCapability(EsteemedInnovation.PLAYER_DATA, null);
            for (String piece : originalData.getAllUnlockedPieces()) {
                if (newData.setHasUnlockedBookPiece(piece, true)) {
                    EsteemedInnovation.channel.sendTo(new BookPieceUnlockedStateChangePacket(piece, true), (EntityPlayerMP) newP);
                }
            }
        }
    }

    @SubscribeEvent
    public void sendPlayerDataToClientOnRespawn(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent event) {
        sendPlayerDataToClient(event.player);
    }

    @SubscribeEvent
    public void sendPlayerDataToClientOnSpawn(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event) {
        sendPlayerDataToClient(event.player);
    }

    private void sendPlayerDataToClient(EntityPlayer player) {
        PlayerData data = player.getCapability(EsteemedInnovation.PLAYER_DATA, null);
        for (String piece : data.getAllUnlockedPieces()) {
            EsteemedInnovation.channel.sendTo(new BookPieceUnlockedStateChangePacket(piece, true), (EntityPlayerMP) player);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void handleRocketDisplay(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getMinecraft();
        ItemStack heldStack = ItemStackUtility.getHeldItemStack(mc.thePlayer);
        if (event.getType() == ElementType.ALL && heldStack != null &&
          heldStack.getItem() == ROCKET_LAUNCHER) {
            ScaledResolution resolution = new ScaledResolution(mc);
            int width = resolution.getScaledWidth();
            int height = resolution.getScaledHeight();
            FontRenderer fontRenderer = mc.fontRendererObj;
            int selectedRocketType = 0;
            if (heldStack.hasTagCompound()) {
                if (heldStack.getTagCompound().hasKey("rocketType")) {
                    selectedRocketType = heldStack.getTagCompound().getInteger("rocketType");
                }
            }
            if (selectedRocketType > EnhancementRegistry.rockets.size() - 1) {
                selectedRocketType = 0;
            }
            String name = selectedRocketType == 0 ? "item.esteemedinnovation:rocket.name.2" : ((Item) EnhancementRegistry.rockets.get(selectedRocketType)).getUnlocalizedName() + ".name";
            String tooltip = I18n.format("esteemedinnovation.rocket", I18n.format(name));

            int tooltipStartX = (width - fontRenderer.getStringWidth(tooltip)) / 2;
            int tooltipStartY = height - 35 - (Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode ? 0 : 35);

            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            fontRenderer.drawStringWithShadow(tooltip, tooltipStartX, tooltipStartY, 0xFFFFFF);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }
    }
//
//    @SubscribeEvent
//    public void preRender(RenderLivingEvent.Pre event) {
//        if (event.entity.isPotionActive(EsteemedInnovation.semiInvisible)) {
//            GL11.glPushMatrix();
//            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.10F);
//            GL11.glDepthMask(false);
//            GL11.glEnable(GL11.GL_BLEND);
//            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//            GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
//        }
//    }

//    @SubscribeEvent
//    public void postRender(RenderLivingEvent.Post event) {
//        if (event.entity.isPotionActive(EsteemedInnovation.semiInvisible)) {
//            GL11.glDisable(GL11.GL_BLEND);
//            GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
//            GL11.glPopMatrix();
//            GL11.glDepthMask(true);
//        }
//    }

    @SubscribeEvent
    public void handleCans(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof EntityItem && !(entity instanceof EntityCanisterItem)) {
            EntityItem item = (EntityItem) entity;
            ItemStack stack = item.getEntityItem();
            if (stack.hasTagCompound() && stack.getTagCompound().hasKey("canned")) {
                if (!event.getWorld().isRemote) {
                    EntityCanisterItem item2 = new EntityCanisterItem(item.worldObj, item.posX, item.posY, item.posZ, item);
                    item2.motionX = item.motionX;
                    item2.motionY = item.motionY;
                    item2.motionZ = item.motionZ;
//                    item2.delayBeforeCanPickup = item.delayBeforeCanPickup;
                    item.worldObj.spawnEntityInWorld(item2);
                }
                item.setDead();
            }
        }
    }

    @SubscribeEvent
    public void handleWorldLoad(WorldEvent.Load event) {
        World world = event.getWorld();
        if (!world.isRemote) {
            SteamNetworkData.get(world);
            SteamNetworkRegistry.initialize();
        }

    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onDrawScreen(RenderGameOverlayEvent.Post event) {
        if (event.getType() == ElementType.ALL) {
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayer player = mc.thePlayer;
            /*if (!player.capabilities.isCreativeMode && player.inventory.armorItemInSlot(1) != null && player.inventory.armorItemInSlot(1).getItem() instanceof ItemSteamExosuitArmor) {
                ItemStack stack = player.inventory.armorItemInSlot(1);
                ItemSteamExosuitArmor item = (ItemSteamExosuitArmor) stack.getItem();
                //if (item.hasUpgrade(stack, SteamcraftItems.doubleJump)) {
                    if (!stack.getTagCompound().hasKey("aidTicks")) {
                        stack.getTagCompound().setInteger("aidTicks", -1);
                    }
                    int aidTicks = stack.getTagCompound().getInteger("aidTicks");

                    int aidTicksScaled = 7-(int)(aidTicks*7.0F / 100.0F);
                    int screenX = event.resolution.getScaledWidth() / 2  - 101;
                    int screenY = event.resolution.getScaledHeight() - 39;
                    mc.getTextureManager().bindTexture(icons);
                    renderTexture(screenX,screenY,9,9,0,0,9D/256D,9D/256D);
                    if (aidTicks > 0) {
                        renderTexture(screenX+1,screenY,aidTicksScaled,9,10D/256D,0,(10D+aidTicksScaled)/256D,9D/256D);
                    }
                    else if (aidTicks == 0) {
                        renderTexture(screenX,screenY,9,9,18D/256D,0,27D/256D,9D/256D);
                    }
                }
            }*/
            ItemStack equipped = player.getHeldItemMainhand();
            RayTraceResult pos = mc.objectMouseOver;
            if (pos != null && equipped != null && equipped.getItem() != null && pos.typeOfHit == RayTraceResult.Type.BLOCK) {
                if (equipped.getItem() instanceof PipeWrench) {
                    PipeWrench wrench = (PipeWrench) equipped.getItem();
                    if (wrench.canWrench(player, pos.getBlockPos())) {
                        TileEntity te = mc.theWorld.getTileEntity(pos.getBlockPos());
                        if (te instanceof WrenchDisplay) {
                            ((WrenchDisplay) te).displayWrench(event);
                        }
                    }
                }

                if (equipped.getItem() == BOOK) {
                    IBlockState state = mc.theWorld.getBlockState(pos.getBlockPos());
                    Block block = state.getBlock();
                    ItemStack stack = block.getPickBlock(state, pos, player.worldObj, pos.getBlockPos(), player);
                    if (stack != null) {
                        for (ItemStack s : BookPageRegistry.bookRecipes.keySet()) {
                            if (s.getItem() == stack.getItem() && s.getItemDamage() == stack.getItemDamage()) {
                                GL11.glPushMatrix();
                                int x = event.getResolution().getScaledWidth() / 2 - 8;
                                int y = event.getResolution().getScaledHeight() / 2 - 8;

                                mc.getRenderItem().renderItemIntoGUI(new ItemStack(BOOK), x, y);
                                GL11.glDisable(GL11.GL_LIGHTING);
                                mc.fontRendererObj.drawStringWithShadow("", x + 15, y + 13, 0xC6C6C6);
                                GL11.glPopMatrix();
                                GL11.glEnable(GL11.GL_BLEND);
                            }
                        }
                    }
                }
            }
        }
    }

    public static EntityEquipmentSlot[] ARMOR_SLOTS = new EntityEquipmentSlot[4];

    static {
        for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
            if (slot.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
                ARMOR_SLOTS[slot.getIndex()] = slot;
            }
        }
    }

    private void updateTradingStackSizes(MerchantRecipeList recipeList) {
        if (recipeList != null) {
            for (Object obj : recipeList) {
                MerchantRecipe recipe = (MerchantRecipe) obj;
                ItemStack toSell = recipe.getItemToSell();
                int stackSizeToSell = toSell.stackSize;
                ItemStack toBuy = recipe.getItemToBuy();
                int stackSizeToBuy = toBuy.stackSize;
                ItemStack secondBuy = recipe.getSecondItemToBuy();
                int stackSizeSecond = secondBuy.stackSize;
                if (stackSizeToSell > 1 && stackSizeToSell != MathHelper.floor_float(stackSizeToSell * 1.25F)) {
                    stackSizeToSell = MathHelper.floor_float(stackSizeToSell * 1.25F);
                } else if (stackSizeToBuy > 1 &&
                  stackSizeToBuy != MathHelper.ceiling_float_int(stackSizeToBuy / 1.25F)) {
                    stackSizeToBuy = MathHelper.ceiling_float_int(stackSizeToBuy / 1.25F);
                } else if (secondBuy != null && stackSizeSecond > 1 &&
                  stackSizeSecond != MathHelper.ceiling_float_int(stackSizeSecond / 1.25F)) {
                    stackSizeSecond = MathHelper.ceiling_float_int(stackSizeSecond / 1.25F);
                }
                toSell.stackSize = stackSizeToSell;
                toBuy.stackSize = stackSizeToBuy;
                secondBuy.stackSize = stackSizeSecond;
            }
            lastViewVillagerGui = true;
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void updateVillagersClientSide(GuiScreenEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        GuiScreen guiScreen = event.getGui();
        if (ReflectionHelper.merchantField != null && guiScreen instanceof GuiMerchant && !lastViewVillagerGui) {
            GuiMerchant gui = (GuiMerchant) guiScreen;
            ItemStack head = mc.thePlayer.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
            if (head != null && (head.getItem() == ENTREPRENEUR_TOP_HAT || (head.getItem() == STEAM_EXO_HEAD
              && ((ItemSteamExosuitArmor) head.getItem()).hasUpgrade(head, ENTREPRENEUR_TOP_HAT)))) {
                IMerchant merch = gui.getMerchant();
                MerchantRecipeList recipeList = merch.getRecipes(mc.thePlayer);
                updateTradingStackSizes(recipeList);
                merch.setRecipes(recipeList);
                try {
                    ReflectionHelper.merchantField.set(gui, merch);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SubscribeEvent
    public void updateVillagers(LivingUpdateEvent event) {
        EntityLivingBase entityLiving = event.getEntityLiving();
        if (entityLiving instanceof EntityVillager && ReflectionHelper.timeUntilResetField != null && ReflectionHelper.lastBuyingPlayerField != null) {
            EntityVillager villager = (EntityVillager) entityLiving;
            Integer timeUntilReset = null;
            String lastBuyingPlayer = null;
            try {
                timeUntilReset = ReflectionHelper.timeUntilResetField.getInt(villager);
                lastBuyingPlayer = (String) ReflectionHelper.lastBuyingPlayerField.get(villager);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (!villager.isTrading() && timeUntilReset != null && timeUntilReset == 39 &&
              lastBuyingPlayer != null) {
                EntityPlayer player = villager.worldObj.getPlayerEntityByName(lastBuyingPlayer);
                if (player != null) {
                    ItemStack hat = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                    if (hat != null && hat.getItem() == ENTREPRENEUR_TOP_HAT) {
                        if (!hat.hasTagCompound()) {
                            hat.setTagCompound(new NBTTagCompound());
                        }
                        if (!hat.getTagCompound().hasKey("level")) {
                            hat.getTagCompound().setInteger("level", 0);
                        }
                        int level = hat.getTagCompound().getInteger("level");
                        level++;
                        hat.getTagCompound().setInteger("level", level);
                    } else if (hat != null && hat.getItem() == STEAM_EXO_HEAD &&
                      ((ItemSteamExosuitArmor) hat.getItem()).hasUpgrade(hat, ENTREPRENEUR_TOP_HAT)) {
                        ItemStack exoHat = ((ItemSteamExosuitArmor) hat.getItem()).getStackInSlot(hat, 3);
                        if (!exoHat.hasTagCompound()) {
                            exoHat.setTagCompound(new NBTTagCompound());
                        }
                        if (!exoHat.getTagCompound().hasKey("level")) {
                            exoHat.getTagCompound().setInteger("level", 0);
                        }
                        int level = exoHat.getTagCompound().getInteger("level");
                        level++;
                        exoHat.getTagCompound().setInteger("level", level);
                        ((ItemSteamExosuitArmor) player.inventory.armorInventory[3].getItem()).setInventorySlotContents(player.inventory.armorInventory[3], 3, hat);
                    }
                }
            }
        }
        if (entityLiving instanceof EntityVillager && !entityLiving.worldObj.isRemote && ReflectionHelper.buyingListField != null) {
            EntityVillager villager = (EntityVillager) entityLiving;
            Boolean hadCustomer = VILLAGER_DATA.getDefaultInstance().hadCustomer();
            if (hadCustomer == null) {
                hadCustomer = false;
            }
            boolean hasCustomer = false;
            if (villager.getCustomer() != null) {
                EntityPlayer player = villager.getCustomer();
                ItemStack head = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                if (head != null && (head.getItem() == ENTREPRENEUR_TOP_HAT ||
                  (head.getItem() == STEAM_EXO_HEAD &&
                  ((ItemSteamExosuitArmor) head.getItem()).hasUpgrade(head, ENTREPRENEUR_TOP_HAT)))) {
                    hasCustomer = true;

                    if (!hadCustomer) {
                        MerchantRecipeList recipeList = villager.getRecipes(player);
                        updateTradingStackSizes(recipeList);

                        try {
                            ReflectionHelper.buyingListField.set(villager, recipeList);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        //customer.closeScreen();
                        //customer.displayGUIMerchant(villager, villager.getCustomNameTag());
                    }
                }
            }

            if (!hasCustomer && hadCustomer) {
                // We need to do reflection because we do not have the customer in this case.
                MerchantRecipeList recipeList = null;
                try {
                    recipeList = (MerchantRecipeList) ReflectionHelper.buyingListField.get(villager);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (recipeList != null) {
                    updateTradingStackSizes(recipeList);
                }
                try {
                    ReflectionHelper.buyingListField.set(villager, recipeList);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            hadCustomer = hasCustomer;
            VILLAGER_DATA.getDefaultInstance().setHadCustomer(hadCustomer);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @SideOnly(Side.CLIENT)
    public void plateTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        ExosuitPlate plate = UtilPlates.getPlate(stack);
        if (plate != null) {
            event.getToolTip().add(TextFormatting.BLUE + I18n.format("esteemedinnovation.plate.bonus", plate.effect()));
        }
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("canned")) {
            event.getToolTip().add(TextFormatting.GOLD + I18n.format("esteemedinnovation.canned"));
        }
        if (stack.getItem() instanceof ItemSteamExosuitArmor || stack.getItem() instanceof SteamChargable) {
            ArrayList<String> linesToRemove = new ArrayList<>();
            for (String str : event.getToolTip()) {
                if (str.equals("")) {
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
                    event.getToolTip().remove(str);
                    event.getToolTip().add(1, str);
                } else {
                    event.getToolTip().remove(str);
                }
            }
        }

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.thePlayer;
        if (mc.currentScreen instanceof GuiContainer) {
            for (ItemStack stack2 : BookPageRegistry.bookRecipes.keySet()) {
                if (stack2.getItem() == stack.getItem() && (stack2.getItemDamage() == stack.getItemDamage() || stack.getItem() instanceof ItemArmor || stack.getItem() instanceof ItemTool)) {
                    boolean foundBook = (CrossMod.ENCHIRIDION && EnchiridionIntegration.hasBook(BOOK, player)) ||
                      player.inventory.hasItemStack(new ItemStack(BOOK));
                    if (foundBook) {
                        event.getToolTip().add(TextFormatting.ITALIC + "" + TextFormatting.GRAY +
                          I18n.format("esteemedinnovation.book.shiftright"));
                        boolean mouseDown = Mouse.isButtonDown(0);
                        if (Config.singleButtonTrackpad && !mouseDown) {
                            mouseDown = Mouse.isButtonDown(1);
                        }
                        if (mouseDown && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
                            GuiJournal.openRecipeFor(stack2, player);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void useItem(LivingEntityUseItemEvent.Tick event) {
        Item item = event.getItem().getItem();
        if (item instanceof ItemFirearm || item instanceof ItemRocketLauncher) {
            use = event.getDuration();
        }
    }

    @SubscribeEvent
    public void useItemEnd(LivingEntityUseItemEvent.Finish event) {
        Item item = event.getItem().getItem();
        if (item instanceof ItemFirearm || item instanceof ItemRocketLauncher) {
            use = -1;
        }
    }

    @SubscribeEvent
    public void useItemEnd(LivingEntityUseItemEvent.Stop event) {
        Item item = event.getItem().getItem();
        if (item instanceof ItemFirearm || item instanceof ItemRocketLauncher) {
            use = -1;
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void handleFallDamage(LivingHurtEvent event) {
        EntityLivingBase entityLiving = event.getEntityLiving();
        DamageSource source = event.getSource();
        if (entityLiving instanceof EntityPlayer && source.damageType.equals("mob") && source.getEntity() != null &&
          !entityLiving.worldObj.isRemote) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            if (player.getHealth() <= 5.0F) {
                int vibrantLevel = 0;
                for (int i = 0; i < ItemStackUtility.ARMOR_SLOTS.length; i++) {
                    EntityEquipmentSlot slot = ItemStackUtility.getSlotFromSlotIndex(i);
                    ItemStack armor = player.getItemStackFromSlot(slot);
                    if (armor != null && armor.getItem() instanceof ItemSteamExosuitArmor) {
                        ItemSteamExosuitArmor armorItem = (ItemSteamExosuitArmor) armor.getItem();
                        if (armorItem.hasPlates(armor) && UtilPlates.getPlate(armor.getTagCompound().getString("plate")).getIdentifier().equals("Vibrant")) {
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
                        List mobs = player.worldObj.getEntitiesWithinAABB(EntityMob.class,
                          new AxisAlignedBB(tX + 0.5F - safeRange, tY + 0.5F - safeRange, tZ + 0.5F - safeRange,
                            tX + 0.5F + safeRange, tY + 0.5F + safeRange, tZ + 0.5F + safeRange));
                        BlockPos posBase = new BlockPos(tX, tY, tZ);
                        BlockPos posDown = new BlockPos(tX, tY - 1, tZ);
                        BlockPos posUp = new BlockPos(tX, tY + 1, tZ);
                        if (mobs.size() == 0 && player.worldObj.isSideSolid(posDown, EnumFacing.UP) &&
                          !player.worldObj.containsAnyLiquid(new AxisAlignedBB(tX, tY - 1, tZ, tX, tY + 1, tZ)) &&
                          player.worldObj.isAirBlock(new BlockPos(tX, tZ, tY)) &&
                          player.worldObj.isAirBlock(new BlockPos(tX, tZ, tY + 1))) {
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
                        player.setPositionAndRotation(tX, tY, tZ, player.worldObj.rand.nextInt(360), player.rotationPitch);
                    }
                }
            }
        }

        if (entityLiving instanceof EntityPlayer && source.damageType.equals("mob") && source.getEntity() != null &&
          !entityLiving.worldObj.isRemote) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            Entity mob = event.getSource().getEntity();
            int enderiumLevel = 0;
            for (int i = 0; i < player.inventory.armorInventory.length; i++) {
                ItemStack armor = player.inventory.armorInventory[i];
                if (armor != null && armor.getItem() instanceof ItemSteamExosuitArmor) {
                    ItemSteamExosuitArmor armorItem = (ItemSteamExosuitArmor) armor.getItem();
                    if (armorItem.hasPlates(armor) && UtilPlates.getPlate(armor.getTagCompound().getString("plate")).getIdentifier() == "Enderium") {
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
                    if (player.worldObj.isSideSolid(new BlockPos(tX, tY - 1, tZ), EnumFacing.UP) &&
                      !player.worldObj.containsAnyLiquid(new AxisAlignedBB(tX, tY - 1, tZ, tX, tY + 1, tZ)) &&
                      player.worldObj.isAirBlock(new BlockPos(tX, tZ, tY)) && player.worldObj.isAirBlock(new BlockPos(tX, tZ, tY + 1))) {
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
        if (((event.getEntityLiving() instanceof EntityPlayer)) && (event.getSource().damageType.equals("mob")) &&
          (event.getSource().getEntity() != null)) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            int fireLevel = 0;
            for (int i = 0; i < player.inventory.armorInventory.length; i++) {
                ItemStack armor = player.inventory.armorInventory[i];
                if (armor != null && armor.getItem() instanceof ItemSteamExosuitArmor) {
                    ItemSteamExosuitArmor armorItem = (ItemSteamExosuitArmor) armor.getItem();
                    if (armorItem.hasPlates(armor) && UtilPlates.getPlate(armor.getTagCompound().getString("plate")).getIdentifier() == "Fiery") {
                        fireLevel += 3;
                    }
                }
            }
            if ((fireLevel > 0) && (player.worldObj.rand.nextInt(25) < fireLevel)) {
                event.getSource().getEntity().setFire(fireLevel / 2);
            }

            if (event.getSource().getEntity() instanceof EntityLivingBase) {
                int chillLevel = 0;
                for (int i = 0; i < player.inventory.armorInventory.length; i++) {
                    ItemStack armor = player.inventory.armorInventory[i];
                    if (armor != null && armor.getItem() instanceof ItemSteamExosuitArmor) {
                        ItemSteamExosuitArmor armorItem = (ItemSteamExosuitArmor) armor.getItem();
                        if (armorItem.hasPlates(armor) && UtilPlates.getPlate(armor.getTagCompound().getString("plate")).getIdentifier() == "Yeti") {
                            chillLevel += 1;
                        }
                    }
                }
                if (chillLevel > 0) {
                    ((EntityLivingBase) event.getSource().getEntity()).addPotionEffect(new PotionEffect(SLOWNESS_POTION, chillLevel * 3 + 5, MathHelper.ceiling_float_int((float) chillLevel / 2F)));
                }
            }
        }

        if (event.getEntity() instanceof EntityPlayer) {
//            ItemStack stack = ((EntityPlayer) event.getEntity()).inventory.armorItemInSlot(1);
//            ItemSteamExosuitArmor item = (ItemSteamExosuitArmor) stack.getItem();
            //if (item.hasUpgrade(stack, SteamcraftItems.doubleJump)) {
            EntityPlayer player = ((EntityPlayer) event.getEntity());
            ItemStack leggings = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
            if (leggings != null && leggings.getItem() instanceof ItemSteamExosuitArmor) {
                float amount = event.getAmount();
                DamageSource src = event.getSource();
                if (!player.isEntityInvulnerable(src)) {
                    if (amount <= 0) {
                        return;
                    }
                    if (!src.isUnblockable() && player.isActiveItemStackBlocking() && amount > 0.0F) {
                        amount = (1.0F + amount) * 0.5F;
                    }

                    amount = ArmorProperties.applyArmor(player, player.inventory.armorInventory, src, amount);
                    if (amount <= 0) {
                        return;
                    }
                    amount = Math.max(amount - player.getAbsorptionAmount(), 0.0F);
                }
                event.setAmount(amount);
            }
        }
    }

    @SubscribeEvent
    public void handleArmorMining(PlayerEvent.BreakSpeed event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (!(entity instanceof EntityPlayer)) {
            return;
        }

        boolean hasPower = ChargableUtility.hasPower(entity, 1);
        int armor = ExosuitUtility.getExoArmor(entity);
        if (hasPower && armor == 4) {
            event.setNewSpeed(event.getNewSpeed() * 1.2F);
        }
        EntityPlayer player = (EntityPlayer) entity;
        ItemStack heldItemStack = player.getHeldItemMainhand();
        if (heldItemStack == null) {
            return;
        }
        Item heldItem = heldItemStack.getItem();
        int itemDamage = heldItemStack.getItemDamage();
        int maxDamage = heldItemStack.getMaxDamage();

        /*
        Survivalist's Toolkit
         */
        if (BaublesUtility.checkForUpgrade(player, SURVIVALIST_TOOLKIT)) {
            if (heldItem instanceof ItemTool) {
                if (itemDamage >= maxDamage - 1) {
                    event.setNewSpeed(0F);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void updateRangeClient(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (entity == Minecraft.getMinecraft().thePlayer) {
//            if (!worldStartUpdate && entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST) != null && entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() instanceof ItemSteamExosuitArmor) {
//                ItemSteamExosuitArmor chest = (ItemSteamExosuitArmor) entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem();
//                if (chest.hasUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST), SteamcraftItems.extendoFist)) {
//
//                    EsteemedInnovation.proxy.extendRange(entity,Config.extendedRange);
//                }
//            }
            worldStartUpdate = true;

            //EsteemedInnovation.proxy.extendRange(entity,1.0F);
            boolean wearing = false;
            ItemStack chestStack = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            if (chestStack == null) {
                return;
            }
            Item chestItem = chestStack.getItem();
            if (chestItem == null) {
                return;
            }
            if (chestItem instanceof ItemSteamExosuitArmor) {
                ItemSteamExosuitArmor chest = (ItemSteamExosuitArmor) chestItem;
                if (chest.hasUpgrade(chestStack, EXTENDO_FIST)) {
                    EsteemedInnovation.proxy.checkRange(entity);

                    wearing = true;
                }
            }
//            if (wearing && !lastWearing && entity.worldObj.isRemote) {
//                EsteemedInnovation.proxy.extendRange(entity,Config.extendedRange);
//            }
            if (!wearing && lastWearing && entity.worldObj.isRemote) {
                EsteemedInnovation.proxy.extendRange(entity, -Config.extendedRange);
            }
            lastWearing = wearing;
        }
    }

    @SubscribeEvent
    public void handleArmor(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (!(entity instanceof EntityPlayer)) {
            return;
        }
        boolean hasPower = ChargableUtility.hasPower(entity, 1);
        // We only handle the boosts for the steam-powered suits.
        int armor = ExosuitUtility.getExoArmorMatchesPredicate(entity, (slot, stack) -> stack.getItem() instanceof ItemSteamExosuitArmor);
//        ItemStack armor2 = entity.getItemStackFromSlot(EntityEquipmentSlot.FEET);
        //EsteemedInnovation.proxy.extendRange(entity,1.0F);

        PlayerData tag = entity.getCapability(EsteemedInnovation.PLAYER_DATA, null);

        if (entity.worldObj.isRemote) {
            updateRangeClient(event);
        } else {
            boolean wearing = false;

            if (entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST) != null && entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() instanceof ItemSteamExosuitArmor) {
                ItemSteamExosuitArmor chest = (ItemSteamExosuitArmor) entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem();
                if (chest.hasUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST), EXTENDO_FIST)) {
                    if (!tag.isRangeExtended()) {
                        wearing = true;
                        tag.setRangeExtended(true);
                        EsteemedInnovation.proxy.extendRange(entity, Config.extendedRange);
                    }
                }
            }
            if (!wearing && tag.isRangeExtended()) {
                EsteemedInnovation.proxy.extendRange(entity, -Config.extendedRange);
                tag.setRangeExtended(false);
            }
        }

        if (hasPower) {
            /*
            if (entity.isSneaking()) {
                if ((!event.entityLiving.isPotionActive(EsteemedInnovation.semiInvisible) || event.entityLiving.getActivePotionEffect(EsteemedInnovation.semiInvisible).getDuration() < 2)) {
                    event.entityLiving.addPotionEffect(new PotionEffect(EsteemedInnovation.semiInvisible.id, 2, 0, false));
                }
            }
            */

            if (tag.getLastMotions() == null) {
                tag.setLastMotions(Pair.of(entity.posX, entity.posZ));
            }
            if (entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getModifier(uuid2) != null) {
                entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(exoBoostBad);
            }
            if (entity.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getModifier(uuid2) != null) {
                entity.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).removeModifier(exoBoostBad);
            }
            ItemStack stack = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);

            if (!stack.hasTagCompound()) {
                stack.setTagCompound(new NBTTagCompound());
            }
            if (!stack.getTagCompound().hasKey("ticksUntilConsume")) {
                stack.getTagCompound().setInteger("ticksUntilConsume", 2);
            }
            int ticksLeft = stack.getTagCompound().getInteger("ticksUntilConsume");
            double lastX = tag.getLastMotions().getLeft();
            double lastZ = tag.getLastMotions().getRight();
            if (ticksLeft <= 0) {
                if (Config.passiveDrain && (lastX != entity.posX || lastZ != entity.posZ)) {
                    ChargableUtility.drainSteam(stack, 1, entity);
                }
                ticksLeft = 2;
            }

            tag.setLastMotions(Pair.of(entity.posX, entity.posZ));

            ticksLeft--;
            stack.getTagCompound().setInteger("ticksUntilConsume", ticksLeft);
            if (armor == 4) {
                if (entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getModifier(uuid) == null) {
                    entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(exoBoost);
                }
                if (entity.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getModifier(uuid) == null) {
                    entity.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).applyModifier(exoBoost);
                }
                if (tag.getPreviousStepHeight() == null) {
                    tag.setPreviousStepHeight(entity.stepHeight);
                }
                entity.stepHeight = 1.0F;
            } else {
                removeGoodExoBoost(entity);
            }
        } else {
            removeGoodExoBoost(entity);
        }

        if (armor > 0 && !hasPower) {
            if (entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getModifier(uuid2) == null) {
                entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(exoBoostBad);
            }
            if (entity.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getModifier(uuid2) == null) {
                entity.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).applyModifier(exoBoostBad);
            }
        } else {
            removeBadExoBoost(entity);
        }
    }

    private void removeGoodExoBoost(EntityLivingBase entity) {
        if (entity.ticksExisted % 20 == 0) {
            if (entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getModifier(uuid) != null) {
                entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(exoBoost);
            }
            if (entity.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getModifier(uuid) != null) {
                entity.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).removeModifier(exoBoost);
            }
            if (entity instanceof EntityPlayer) {
                PlayerData tag = entity.getCapability(EsteemedInnovation.PLAYER_DATA, null);
                if (tag.getPreviousStepHeight() != null) {
                    entity.stepHeight = tag.getPreviousStepHeight();
                    tag.setPreviousStepHeight(null);
                }
            }
        }
    }

//    public boolean isMoving(EntityLivingBase entity) {
//        return (entity.isp)
//    }

    private void removeBadExoBoost(EntityLivingBase entity) {
        if (entity.ticksExisted % 20 == 0) {
            if (entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getModifier(uuid2) != null) {
                entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(exoBoostBad);
            }
            if (entity.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getModifier(uuid2) != null) {
                entity.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).removeModifier(exoBoostBad);
            }
        }
    }

}
