package eiteam.esteemedinnovation.commons.handler;

import eiteam.esteemedinnovation.api.SteamChargable;
import eiteam.esteemedinnovation.api.SmasherRegistry;
import eiteam.esteemedinnovation.api.book.BookPageRegistry;
import eiteam.esteemedinnovation.api.enhancement.EnhancementRegistry;
import eiteam.esteemedinnovation.api.exosuit.ExosuitArmor;
import eiteam.esteemedinnovation.api.exosuit.ExosuitPlate;
import eiteam.esteemedinnovation.api.exosuit.UtilPlates;
import eiteam.esteemedinnovation.api.steamnet.SteamNetworkRegistry;
import eiteam.esteemedinnovation.api.steamnet.data.SteamNetworkData;
import eiteam.esteemedinnovation.api.tool.SteamTool;
import eiteam.esteemedinnovation.api.tool.UtilSteamTool;
import eiteam.esteemedinnovation.api.util.ItemStackUtility;
import eiteam.esteemedinnovation.api.util.SPLog;
import eiteam.esteemedinnovation.api.wrench.PipeWrench;
import eiteam.esteemedinnovation.api.wrench.WrenchDisplay;
import eiteam.esteemedinnovation.armor.exosuit.ItemExosuitArmor;
import eiteam.esteemedinnovation.armor.exosuit.upgrades.frequency.AnimalData;
import eiteam.esteemedinnovation.armor.exosuit.upgrades.frequency.AnimalDataSerializer;
import eiteam.esteemedinnovation.armor.tophat.VillagerDataSerializer;
import eiteam.esteemedinnovation.book.BookPieceUnlockedStateChangePacket;
import eiteam.esteemedinnovation.book.GuiJournal;
import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.commons.capabilities.player.PlayerData;
import eiteam.esteemedinnovation.commons.capabilities.player.PlayerDataSerializer;
import eiteam.esteemedinnovation.commons.network.JumpValueChangePacket;
import eiteam.esteemedinnovation.commons.util.EnchantmentUtility;
import eiteam.esteemedinnovation.commons.util.OreDictHelper;
import eiteam.esteemedinnovation.firearms.flintlock.ItemFirearm;
import eiteam.esteemedinnovation.firearms.rocket.ItemRocketLauncher;
import eiteam.esteemedinnovation.init.items.armor.ExosuitUpgradeItems;
import eiteam.esteemedinnovation.init.misc.integration.CrossMod;
import eiteam.esteemedinnovation.init.misc.integration.EnchiridionIntegration;
import eiteam.esteemedinnovation.init.misc.integration.baubles.BaublesIntegration;
import eiteam.esteemedinnovation.storage.item.canister.EntityCanisterItem;
import eiteam.esteemedinnovation.tools.steam.ItemSteamAxe;
import eiteam.esteemedinnovation.tools.steam.ItemSteamDrill;
import eiteam.esteemedinnovation.tools.steam.ItemSteamShovel;
import eiteam.esteemedinnovation.tools.steam.SteamToolHelper;
import eiteam.esteemedinnovation.tools.steam.upgrades.drillhead.DrillHeadMaterial;
import eiteam.esteemedinnovation.tools.steam.upgrades.drillhead.ItemDrillHeadUpgrade;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.*;
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
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
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
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ISpecialArmor.ArmorProperties;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.*;

import static eiteam.esteemedinnovation.armor.exosuit.upgrades.frequency.AnimalDataStorage.POSSIBLE_NAMES;
import static eiteam.esteemedinnovation.init.items.armor.ArmorItems.Items.ENTREPRENEUR_TOP_HAT;
import static eiteam.esteemedinnovation.init.items.armor.ArmorItems.Items.EXOSUIT_HEADPIECE;
import static eiteam.esteemedinnovation.init.items.armor.ExosuitUpgradeItems.Items.*;
import static eiteam.esteemedinnovation.init.items.firearms.FirearmItems.Items.ROCKET_LAUNCHER;
import static eiteam.esteemedinnovation.init.items.tools.GadgetItems.Items.*;
import static eiteam.esteemedinnovation.init.items.tools.ToolUpgradeItems.Items.*;

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

    // TODO: Migrate to ExosuitArmor#drainSteam.
    public static void drainSteam(ItemStack stack, int amount) {
        if (stack != null) {
            if (stack.getTagCompound() == null) {
                stack.setTagCompound(new NBTTagCompound());
            }
            if (!stack.getTagCompound().hasKey("steamFill")) {
                stack.getTagCompound().setInteger("steamFill", 0);
            }
            int fill = stack.getTagCompound().getInteger("steamFill");
            fill = Math.max(0, fill - amount);
            stack.getTagCompound().setInteger("steamFill", fill);
        }
    }

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
                isJumping = FieldHandler.getIsEntityJumping(elb);
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

    // TODO: Migrate to ExosuitArmor#hasPower in cases where access to the chestplate is already needed.
    // TODO: Move into a proper util class.
    public static boolean hasPower(EntityLivingBase entityLiving, int i) {
        ItemStack equipment = entityLiving.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (equipment != null) {
            Item item = equipment.getItem();
            if (item instanceof ExosuitArmor) {
                return ((ExosuitArmor) item).hasPower(equipment, i);
            }
        }
        return false;
    }

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
          heldStack.getItem() == ROCKET_LAUNCHER.getItem()) {
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
    public void handleCanningMachine(EntityItemPickupEvent event) {
        EntityLivingBase entityLiving = event.getEntityLiving();
        if (entityLiving instanceof EntityPlayer && !entityLiving.worldObj.isRemote) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            ItemStack legStack = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
            if (hasPower(player, 10) && legStack != null && legStack.getItem() instanceof ItemExosuitArmor) {
                ItemExosuitArmor leggings = (ItemExosuitArmor) legStack.getItem();
                if (leggings.hasUpgrade(legStack, CANNING_MACHINE.getItem())) {

                    ItemStack item = event.getItem().getEntityItem().copy();
                    if (item.hasTagCompound() && item.getTagCompound().hasKey("canned")) {
                        return;
                    }

                    boolean isCannable = OreDictHelper.mapHasItem(OreDictHelper.ingots, item.getItem()) ||
                      OreDictHelper.mapHasItem(OreDictHelper.gems, item.getItem()) ||
                      OreDictHelper.listHasItem(OreDictHelper.nuggets, item.getItem());

                    if (isCannable) {
                        int numCans = 0;
                        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                            ItemStack stackInSlot = player.inventory.getStackInSlot(i);
                            if (stackInSlot != null && stackInSlot.getItem() == ITEM_CANISTER.getItem()) {
                                numCans += stackInSlot.stackSize;
                            }
                        }
                        if (numCans >= item.stackSize) {
                            if (!item.hasTagCompound()) {
                                item.setTagCompound(new NBTTagCompound());
                            }
                            item.getTagCompound().setInteger("canned", 0);
                            event.getItem().setEntityItemStack(item);
                            for (int i = 0; i < item.stackSize; i++) {
                                consumeInventoryItem(player, ITEM_CANISTER.getItem());
                                player.inventoryContainer.detectAndSendChanges();
                            }
                        } else if (numCans != 0) {
                            item.stackSize -= numCans;
                            event.getItem().setEntityItemStack(item);
                            ItemStack item2 = item.copy();
                            item2.stackSize = numCans;
                            if (!item2.hasTagCompound()) {
                                item2.setTagCompound(new NBTTagCompound());
                            }
                            item2.getTagCompound().setInteger("canned", 0);
                            EntityItem entityItem = new EntityItem(player.worldObj, player.posX, player.posY, player.posZ, item2);
                            player.worldObj.spawnEntityInWorld(entityItem);
                            for (int i = 0; i < numCans; i++) {
                                consumeInventoryItem(player, ITEM_CANISTER.getItem());
                                player.inventoryContainer.detectAndSendChanges();
                            }
                        }
                    }
                }
            }
        }
    }

    public static void consumeInventoryItem(EntityPlayer player, Item item) {
        for (int slot = 0; slot < player.inventory.getSizeInventory(); slot++) {
            ItemStack stackInSlot = player.inventory.getStackInSlot(slot);
            if (stackInSlot != null && stackInSlot.getItem() == item) {
                if (stackInSlot.stackSize > 1) {
                    stackInSlot.stackSize--;
                    player.inventory.setInventorySlotContents(slot, stackInSlot);
                } else {
                    player.inventory.setInventorySlotContents(slot, null);
                }
            }
        }
    }

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
            /*if (!player.capabilities.isCreativeMode && player.inventory.armorItemInSlot(1) != null && player.inventory.armorItemInSlot(1).getItem() instanceof ItemExosuitArmor) {
                ItemStack stack = player.inventory.armorItemInSlot(1);
                ItemExosuitArmor item = (ItemExosuitArmor) stack.getItem();
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

                if (equipped.getItem() == BOOK.getItem()) {
                    IBlockState state = mc.theWorld.getBlockState(pos.getBlockPos());
                    Block block = state.getBlock();
                    ItemStack stack = block.getPickBlock(state, pos, player.worldObj, pos.getBlockPos(), player);
                    if (stack != null) {
                        for (ItemStack s : BookPageRegistry.bookRecipes.keySet()) {
                            if (s.getItem() == stack.getItem() && s.getItemDamage() == stack.getItemDamage()) {
                                GL11.glPushMatrix();
                                int x = event.getResolution().getScaledWidth() / 2 - 8;
                                int y = event.getResolution().getScaledHeight() / 2 - 8;

                                mc.getRenderItem().renderItemIntoGUI(new ItemStack(BOOK.getItem()), x, y);
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
        if (FieldHandler.merchantField != null && guiScreen instanceof GuiMerchant && !lastViewVillagerGui) {
            GuiMerchant gui = (GuiMerchant) guiScreen;
            ItemStack head = mc.thePlayer.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
            if (head != null && (head.getItem() == ENTREPRENEUR_TOP_HAT.getItem()
              || (head.getItem() == EXOSUIT_HEADPIECE.getItem()
              && ((ItemExosuitArmor) head.getItem()).hasUpgrade(head, ENTREPRENEUR_TOP_HAT.getItem())))) {
                IMerchant merch = gui.getMerchant();
                MerchantRecipeList recipeList = merch.getRecipes(mc.thePlayer);
                updateTradingStackSizes(recipeList);
                merch.setRecipes(recipeList);
                try {
                    FieldHandler.merchantField.set(gui, merch);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SubscribeEvent
    public void updateVillagers(LivingUpdateEvent event) {
        EntityLivingBase entityLiving = event.getEntityLiving();
        if (entityLiving instanceof EntityVillager && FieldHandler.timeUntilResetField != null && FieldHandler.lastBuyingPlayerField != null) {
            EntityVillager villager = (EntityVillager) entityLiving;
            Integer timeUntilReset = null;
            String lastBuyingPlayer = null;
            try {
                timeUntilReset = FieldHandler.timeUntilResetField.getInt(villager);
                lastBuyingPlayer = (String) FieldHandler.lastBuyingPlayerField.get(villager);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (!villager.isTrading() && timeUntilReset != null && timeUntilReset == 39 &&
              lastBuyingPlayer != null) {
                EntityPlayer player = villager.worldObj.getPlayerEntityByName(lastBuyingPlayer);
                if (player != null) {
                    ItemStack hat = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                    if (hat != null && hat.getItem() == ENTREPRENEUR_TOP_HAT.getItem()) {
                        if (!hat.hasTagCompound()) {
                            hat.setTagCompound(new NBTTagCompound());
                        }
                        if (!hat.getTagCompound().hasKey("level")) {
                            hat.getTagCompound().setInteger("level", 0);
                        }
                        int level = hat.getTagCompound().getInteger("level");
                        level++;
                        hat.getTagCompound().setInteger("level", level);
                    } else if (hat != null && hat.getItem() == EXOSUIT_HEADPIECE.getItem() &&
                      ((ItemExosuitArmor) hat.getItem()).hasUpgrade(hat, ENTREPRENEUR_TOP_HAT.getItem())) {
                        ItemStack exoHat = ((ItemExosuitArmor) hat.getItem()).getStackInSlot(hat, 3);
                        if (!exoHat.hasTagCompound()) {
                            exoHat.setTagCompound(new NBTTagCompound());
                        }
                        if (!exoHat.getTagCompound().hasKey("level")) {
                            exoHat.getTagCompound().setInteger("level", 0);
                        }
                        int level = exoHat.getTagCompound().getInteger("level");
                        level++;
                        exoHat.getTagCompound().setInteger("level", level);
                        ((ItemExosuitArmor) player.inventory.armorInventory[3].getItem()).setInventorySlotContents(player.inventory.armorInventory[3], 3, hat);
                    }
                }
            }
        }
        if (entityLiving instanceof EntityVillager && !entityLiving.worldObj.isRemote && FieldHandler.buyingListField != null) {
            EntityVillager villager = (EntityVillager) entityLiving;
            Boolean hadCustomer = EsteemedInnovation.VILLAGER_DATA.getDefaultInstance().hadCustomer();
            if (hadCustomer == null) {
                hadCustomer = false;
            }
            boolean hasCustomer = false;
            if (villager.getCustomer() != null) {
                EntityPlayer player = villager.getCustomer();
                ItemStack head = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                if (head != null && (head.getItem() == ENTREPRENEUR_TOP_HAT.getItem() ||
                  (head.getItem() == EXOSUIT_HEADPIECE.getItem() &&
                  ((ItemExosuitArmor) head.getItem()).hasUpgrade(head, ENTREPRENEUR_TOP_HAT.getItem())))) {
                    hasCustomer = true;

                    if (!hadCustomer) {
                        MerchantRecipeList recipeList = villager.getRecipes(player);
                        updateTradingStackSizes(recipeList);

                        try {
                            FieldHandler.buyingListField.set(villager, recipeList);
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
                    recipeList = (MerchantRecipeList) FieldHandler.buyingListField.get(villager);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (recipeList != null) {
                    updateTradingStackSizes(recipeList);
                }
                try {
                    FieldHandler.buyingListField.set(villager, recipeList);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            hadCustomer = hasCustomer;
            EsteemedInnovation.VILLAGER_DATA.getDefaultInstance().setHadCustomer(hadCustomer);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void muffleSounds(PlaySoundEvent event) {
        if (event.getName().contains("step")) {
            float x = event.getSound().getXPosF();
            float y = event.getSound().getYPosF();
            float z = event.getSound().getZPosF();
            List entities = Minecraft.getMinecraft().thePlayer.worldObj.getEntitiesWithinAABB(
              EntityLivingBase.class, new AxisAlignedBB(x - 0.5F, y - 0.5F, z - 0.5F, x + 0.5F, y + 0.5F, z + 0.5F));
            for (Object obj : entities) {
                EntityLivingBase entity = (EntityLivingBase) obj;
                ItemStack legs = entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
                if (legs != null && legs.getItem() instanceof ItemExosuitArmor) {
                    ItemExosuitArmor leggings = (ItemExosuitArmor) legs.getItem();
                    if (leggings.hasUpgrade(legs, STEALTH.getItem())) {
                        event.setResultSound(null);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void hideCloakedPlayers(LivingUpdateEvent event) {
        EntityLivingBase entityLiving = event.getEntityLiving();
        if (entityLiving instanceof EntityLiving) {
            EntityLiving entity = (EntityLiving) entityLiving;
            hideCloakedPlayers(entity, entity.getAttackTarget());
        }
    }

    @SubscribeEvent
    public void hideCloakedPlayers(LivingSetAttackTargetEvent event) {
        EntityLivingBase entityLiving = event.getEntityLiving();
        if (entityLiving instanceof EntityLiving) {
            hideCloakedPlayers((EntityLiving) entityLiving, event.getTarget());
        }
    }

    private void hideCloakedPlayers(EntityLiving entity, EntityLivingBase target) {
        if (target == null) {
            return;
        }
        ItemStack targetLegs = target.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
        if (targetLegs == null || !(targetLegs.getItem() instanceof ItemExosuitArmor)) {
            return;
        }
        ItemExosuitArmor leggings = (ItemExosuitArmor) targetLegs.getItem();
        if (!leggings.hasUpgrade(targetLegs, STEALTH.getItem())) {
            return;
        }
        IAttributeInstance iattributeinstance = entity.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        double d0 = (iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue()) / 1.5D;
        List<Entity> list = entity.worldObj.getEntitiesWithinAABB(Entity.class,
          entity.getEntityBoundingBox().expand(d0, 4.0D, d0));
        boolean foundPlayer = false;
        for (Entity mob : list) {
            if (mob == target) {
                foundPlayer = true;
                break;
            }
        }
        if (!foundPlayer) {
            entity.setAttackTarget(null);
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
        if (stack.getItem() instanceof ItemExosuitArmor || stack.getItem() instanceof SteamChargable) {
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
                    boolean foundBook = (CrossMod.ENCHIRIDION && EnchiridionIntegration.hasBook(BOOK.getItem(), player)) ||
                      player.inventory.hasItemStack(new ItemStack(BOOK.getItem()));
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
    public void doubleExp(PlayerPickupXpEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        for (int i = 0; i < ItemStackUtility.EQUIPMENT_SLOTS.length; i++) {
            float multiplier = 1;
            EntityEquipmentSlot slot = ItemStackUtility.getSlotFromSlotIndex(i);
            if (slot == null) {
                continue;
            }
            if (slot.getSlotType() == EntityEquipmentSlot.Type.HAND) {
                continue;
            }
            ItemStack stack = player.getItemStackFromSlot(slot);
            if (stack != null) {
                if (stack.getItem() instanceof ItemExosuitArmor) {
                    ItemExosuitArmor armor = (ItemExosuitArmor) stack.getItem();
                    if (armor.hasPlates(stack)) {
                        ExosuitPlate plate = UtilPlates.getPlate(stack.getTagCompound().getString("plate"));
                        String id = plate.getIdentifier();
                        if (id.equals("Gold")) {
                            multiplier *= 1.25F;
                        } else if (id.equals("Gilded Iron")) {
                            multiplier *= 1.125;
                        }
                    }
                }
            }
            event.getOrb().xpValue = MathHelper.ceiling_float_int(event.getOrb().xpValue * multiplier);
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

    @SubscribeEvent
    public void handleFirePunch(LivingAttackEvent event) {
        DamageSource source = event.getSource();
        Entity sourceEntity = source.getSourceOfDamage();
        if (sourceEntity instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase) sourceEntity;
            boolean hasPower = hasPower(entity, Config.powerFistConsumption);
            ItemStack stack = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            if (hasPower && stack != null && entity.getHeldItemMainhand() == null &&
              stack.getItem() instanceof ItemExosuitArmor) {
                ItemExosuitArmor chest = (ItemExosuitArmor) stack.getItem();
                if (chest.hasUpgrade(stack, POWER_FIST.getItem())) {
                    entity.worldObj.playSound(entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE,
                      SoundCategory.PLAYERS, 4F, (1F + (entity.worldObj.rand.nextFloat() - entity.worldObj.rand.nextFloat()) * 0.2F) * 0.7F, false);
                    entity.motionX += 3.0F * entity.getLookVec().normalize().xCoord;
                    entity.motionY += (entity.getLookVec().normalize().yCoord > 0.0F ? 2.0F * entity.getLookVec().normalize().yCoord : 0.0F) + 1.5F;
                    entity.motionZ += 3.0F * entity.getLookVec().normalize().zCoord;
                    entity.motionX += -0.5F * entity.getLookVec().normalize().xCoord;
                    entity.motionZ += -0.5F * entity.getLookVec().normalize().zCoord;
                    drainSteam(stack, Config.powerFistConsumption);
                }
            }
        }
    }

    @SubscribeEvent
    public void handlePistonPunch(PlayerInteractEvent.LeftClickBlock event) {
        EntityPlayer entity = event.getEntityPlayer();
        int consumption = Config.pistonPushConsumption;
        boolean hasPower = hasPower(entity, consumption);
        ItemStack heldItem = entity.getHeldItemMainhand();
        if (hasPower && heldItem == null) {
            ItemStack chestStack = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            Item chest = chestStack.getItem();
            if (chest instanceof ItemExosuitArmor) {
                ItemExosuitArmor armor = (ItemExosuitArmor) chest;
                if (armor.hasUpgrade(chestStack, PISTON_PUSH.getItem())) {
                    World world = event.getWorld();
                    EnumFacing face = event.getFace();
                    if (face == null) {
                        return;
                    }
                    BlockPos curPos = event.getPos();
                    int x = curPos.getX();
                    int y = curPos.getY();
                    int z = curPos.getZ();
                    switch (face) {
                        case UP: {
                            if (y != 0) {
                                y--;
                            }
                        }
                        case DOWN: {
                            if (y != 0) {
                                y++;
                            }
                        }
                        case WEST: {
                            x--;
                        }
                        case EAST: {
                            x++;
                        }
                        case SOUTH: {
                            z++;
                        }
                        case NORTH: {
                            z--;
                        }
                    }
                    BlockPos newPos = new BlockPos(x, y, z);

                    IBlockState clickedState = world.getBlockState(curPos);
                    IBlockState stateInPlace = world.getBlockState(newPos);
                    Block clickedBlock = clickedState.getBlock();
                    Block blockInPlace = stateInPlace.getBlock();
                    EnumPushReaction reaction = clickedBlock.getMobilityFlag(clickedState);
                    if ((blockInPlace == null || blockInPlace == Blocks.AIR ||
                      blockInPlace instanceof BlockFluidBase) &&
                      clickedBlock.getBlockHardness(clickedState, world, curPos) >= 0.0F &&
                      reaction != EnumPushReaction.IGNORE && reaction != EnumPushReaction.DESTROY &&
                      clickedBlock != Blocks.OBSIDIAN &&
                      !clickedBlock.hasTileEntity(clickedState)) {
                        world.setBlockToAir(curPos);
                        world.setBlockState(newPos, clickedState, 3);
                        world.playSound(x + 0.5D, y + 0.5D, z + 0.5D, SoundEvents.BLOCK_PISTON_EXTEND,
                          SoundCategory.PLAYERS, 0.5F, world.rand.nextFloat() * 0.25F + 0.6F, false);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void hearMeRoar(LivingAttackEvent event) {
        // Explosions must be ignored in order to prevent infinite recursive hearMeRoar calls.
        DamageSource source = event.getSource();
        Entity sourceEntity = source.getSourceOfDamage();
        if (sourceEntity instanceof EntityLivingBase && !source.isExplosion()) {
            EntityLivingBase entity = (EntityLivingBase) sourceEntity;
            World world = entity.worldObj;
            ItemStack head = entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
            ItemStack chest = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            if (entity.getHeldItemMainhand() == null && entity.isSneaking() && head != null &&
              chest != null && chest.getItem() instanceof ItemExosuitArmor &&
              chest.hasTagCompound()) {
                int consumption = (chest.getTagCompound().getInteger("maxFill") / 2) + Config.dragonRoarConsumption;
                Item helmet = head.getItem();
                if (hasPower(entity, consumption) && helmet instanceof ItemExosuitArmor) {
                    ItemExosuitArmor helmetArmor = (ItemExosuitArmor) helmet;
                    if (helmetArmor.hasUpgrade(head, DRAGON_ROAR.getItem())) {
                        if (world.isRemote) {
                            world.playSound(entity.posX, entity.posY, entity.posZ,
                              SoundEvents.ENTITY_ENDERDRAGON_GROWL, SoundCategory.PLAYERS, 5.0F,
                              0.8F + world.rand.nextFloat() * 0.3F, false);
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
        EntityLivingBase entityLiving = event.getEntityLiving();
        DamageSource source = event.getSource();
        if (entityLiving instanceof EntityPlayer && source.damageType.equals("mob") && source.getEntity() != null &&
          !entityLiving.worldObj.isRemote) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            if (player.getHealth() <= 5.0F) {
                int vibrantLevel = 0;
                for (int i = 0; i < ItemStackUtility.EQUIPMENT_SLOTS.length; i++) {
                    EntityEquipmentSlot slot = ItemStackUtility.getSlotFromSlotIndex(i);
                    if (slot.getSlotType() == EntityEquipmentSlot.Type.HAND) {
                        continue;
                    }
                    ItemStack armor = player.getItemStackFromSlot(slot);
                    if (armor != null && armor.getItem() instanceof ItemExosuitArmor) {
                        ItemExosuitArmor armorItem = (ItemExosuitArmor) armor.getItem();
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
                if (armor != null && armor.getItem() instanceof ItemExosuitArmor) {
                    ItemExosuitArmor armorItem = (ItemExosuitArmor) armor.getItem();
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
                if (armor != null && armor.getItem() instanceof ItemExosuitArmor) {
                    ItemExosuitArmor armorItem = (ItemExosuitArmor) armor.getItem();
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
                    if (armor != null && armor.getItem() instanceof ItemExosuitArmor) {
                        ItemExosuitArmor armorItem = (ItemExosuitArmor) armor.getItem();
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
        if (event.getSource() == DamageSource.fall) {
            boolean hasPower = hasPower(event.getEntityLiving(), (int) (event.getAmount() / Config.fallAssistDivisor));
            EntityLivingBase entity = event.getEntityLiving();
            if (hasPower && entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST) != null && entity.getItemStackFromSlot(EntityEquipmentSlot.FEET) != null && entity.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() instanceof ItemExosuitArmor) {
                ItemExosuitArmor boots = (ItemExosuitArmor) entity.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem();
                if (boots.hasUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.FEET), FALL_ASSIST.getItem())) {
                    if (event.getAmount() <= 6.0F) {
                        event.setAmount(0F);
                    }
                    event.setAmount(event.getAmount() / 3F);
                    drainSteam(entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST), (int) (event.getAmount() / Config.fallAssistDivisor));
                    if (event.getAmount() == 0.0F) {
                        event.setResult(Event.Result.DENY);
                        event.setCanceled(true);
                    }
                }
            }
        }

        if (event.getEntity() instanceof EntityPlayer) {
//            ItemStack stack = ((EntityPlayer) event.getEntity()).inventory.armorItemInSlot(1);
//            ItemExosuitArmor item = (ItemExosuitArmor) stack.getItem();
            //if (item.hasUpgrade(stack, SteamcraftItems.doubleJump)) {
            EntityPlayer player = ((EntityPlayer) event.getEntity());
            ItemStack leggings = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
            if (leggings != null && leggings.getItem() instanceof ItemExosuitArmor) {
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
    public void playerJumps(LivingEvent.LivingJumpEvent event) {
        EntityLivingBase entityLiving = event.getEntityLiving();
        Entity entity = event.getEntity();

        if (!(entity instanceof EntityPlayer)) {
            return;
        }

        EntityPlayer player = (EntityPlayer) entity;

        boolean hasPower = hasPower(entityLiving, Config.jumpBoostConsumptionShiftJump);

        ItemStack stack = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);

        if (stack == null) {
            return;
        }

        Item item = stack.getItem();

        if (item instanceof ItemExosuitArmor) {
            ItemExosuitArmor boots = (ItemExosuitArmor) item;

            boolean shiftJump = entity.isSneaking() && hasPower;

            if (shiftJump || hasPower(entityLiving, Config.jumpBoostConsumption)) {
                if (boots.hasUpgrade(stack, JUMP_ASSIST.getItem())) {
                    if (shiftJump) {
                        Vec3d vector = entityLiving.getLook(0.5F);
                        double total = Math.abs(vector.zCoord + vector.xCoord);
                        double jump = 0;
                        if (jump >= 1) {
                            jump = (jump + 2D) / 4D;
                        }

                        double y = vector.yCoord < total ? total : vector.yCoord;

                        entityLiving.motionY += ((jump + 1) * y) / 1.5F;
                        entityLiving.motionZ += (jump + 1) * vector.zCoord * 2;
                        entityLiving.motionX += (jump + 1) * vector.xCoord * 2;
                        drainSteam(entityLiving.getItemStackFromSlot(EntityEquipmentSlot.CHEST), Config.jumpBoostConsumptionShiftJump);
                    } else {
                        drainSteam(entityLiving.getItemStackFromSlot(EntityEquipmentSlot.CHEST), Config.jumpBoostConsumption);
                        entityLiving.motionY += 0.2750000059604645D;
                    }
                }
            }

            if (boots.hasUpgrade(stack, DOUBLE_JUMP.getItem())) {
                stack.getTagCompound().setBoolean("releasedSpace", false);
            }
        }
    }

    public boolean hasItemInHotbar(EntityPlayer player, Item item) {
        for (int i = 0; i < 10; i++) {
            ItemStack stackInSlot = player.inventory.getStackInSlot(i);
            if (stackInSlot != null && stackInSlot.getItem() == item) {
                return true;
            }
        }
        return false;
    }

    @SubscribeEvent
    public void handleArmorMining(PlayerEvent.BreakSpeed event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (!(entity instanceof EntityPlayer)) {
            return;
        }

        boolean hasPower = hasPower(entity, 1);
        int armor = getExoArmor(entity);
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
        if (CrossMod.BAUBLES) {
            if (BaublesIntegration.checkForSurvivalist(player)) {
                if (heldItem instanceof ItemTool) {
                    if (itemDamage >= maxDamage - 1) {
                        event.setNewSpeed(0F);
                    }
                }

            }
        } else if (hasItemInHotbar(player, SURVIVALIST_TOOLKIT.getItem())) {
            if (heldItem instanceof ItemTool) {
                if (itemDamage >= maxDamage - 1) {
                    event.setNewSpeed(0F);
                }
            }
        }

        IBlockState state = event.getState();

        if (heldItem instanceof ItemSteamDrill) {
            NBTTagCompound nbt = UtilSteamTool.checkNBT(heldItemStack);
            int speed = nbt.getInteger("Speed");
            if (speed > 0 && Items.IRON_PICKAXE.getStrVsBlock(heldItemStack, state) != 1.0F) {
                event.setNewSpeed(event.getNewSpeed() * 1F + 11F * (speed / 1000F));
            }
        } else if (heldItem instanceof ItemSteamAxe) {
            NBTTagCompound nbt = UtilSteamTool.checkNBT(heldItemStack);
            int speed = nbt.getInteger("Speed");
            if (speed > 0 && Items.DIAMOND_AXE.getStrVsBlock(heldItemStack, state) != 1.0F) {
                event.setNewSpeed(event.getNewSpeed() * 1F + 11F * (speed / 1000F));
            }
        } else if (heldItem instanceof ItemSteamShovel) {
            NBTTagCompound nbt = UtilSteamTool.checkNBT(heldItemStack);
            int speed = nbt.getInteger("Speed");
            if (speed > 0 && Items.DIAMOND_SHOVEL.getStrVsBlock(heldItemStack, state) != 1.0F) {
                event.setNewSpeed(event.getNewSpeed() * 1F + 11F * (speed / 1000F));
            }
        }
    }

    @SubscribeEvent
    public void handleFlippers(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (!(entity instanceof EntityPlayer)) {
            return;
        }
        boolean hasPower = hasPower(entity, 1);
        EntityPlayer player = (EntityPlayer) entity;

        ItemStack chestStack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (chestStack != null && chestStack.getItem() instanceof ItemExosuitArmor && chestStack.hasTagCompound()) {
            NBTTagCompound compound = chestStack.getTagCompound();
            ItemExosuitArmor chest = (ItemExosuitArmor) chestStack.getItem();
            if (chest.hasUpgrade(chestStack, PITON_DEPLOYER.getItem())) {
                if (compound.hasKey("grappled") && compound.getBoolean("grappled")) {
                    double lastX = compound.getFloat("x");
                    double lastY = compound.getFloat("y");
                    double lastZ = compound.getFloat("z");
                    int blockX = compound.getInteger("blockX");
                    int blockY = compound.getInteger("blockY");
                    int blockZ = compound.getInteger("blockZ");

                    BlockPos blockPos = new BlockPos(blockX, blockY, blockZ);

                    if ((Math.abs(lastX - entity.posX) > 0.1F || Math.abs(lastZ - entity.posZ) > 0.1F || entity.isSneaking() || entity.worldObj.isAirBlock(blockPos))) {
                        compound.setBoolean("grappled", false);
                    } else {
                        entity.motionX = 0.0F;
                        entity.motionY = (entity.motionY > 0) ? entity.motionY : 0.0F;
                        entity.motionZ = 0.0F;
                    }
                }
            }
        }

        ItemStack boots = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
        ItemStack leggings = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
        /*
        if (armorItem1 != null && armorItem1.getItem() instanceof ItemExosuitArmor) {
            if (item.hasUpgrade(stack, SteamcraftItems.doubleJump)) {
                if (!stack.getTagCompound().hasKey("aidTicks")) {
                    stack.getTagCompound().setInteger("aidTicks", -1);
                }
                int aidTicks = stack.getTagCompound().getInteger("aidTicks");

                if (aidTicks > 0) {
                    aidTicks--;
                }
                if (aidTicks == 0) {
                    if (!stack.getTagCompound().hasKey("ticksNextHeal")) {
                        stack.getTagCompound().setInteger("ticksNextHeal", 0);
                    }
                    float damageAmount = stack.getTagCompound().getInteger("damageAmount");
                    int ticksNextHeal = stack.getTagCompound().getInteger("ticksNextHeal");
                    if (ticksNextHeal > 0) {
                        ticksNextHeal--;
                    }
                    if (ticksNextHeal == 0) {
                        //event.entityLiving.heal(1.0F);
                        damageAmount -=1.0F;
                        stack.getTagCompound().setFloat("damageAmount", damageAmount);
                        ticksNextHeal=5;
                    }
                    if (damageAmount == 0.0F) {
                        aidTicks = -1;
                    }
                    stack.getTagCompound().setInteger("ticksNextHeal", ticksNextHeal);
                }
                stack.getTagCompound().setInteger("aidTicks", aidTicks);
            }
        }
        */

        if (boots != null && boots.getItem() instanceof ItemExosuitArmor) {
            ItemExosuitArmor item = (ItemExosuitArmor) boots.getItem();
            if (item.hasUpgrade(boots, DOUBLE_JUMP.getItem()) && player.onGround && boots.hasTagCompound()) {
                boots.getTagCompound().setBoolean("usedJump", false);
            }
        }

        if (hasPower && leggings != null && leggings.getItem() instanceof ItemExosuitArmor) {
            PlayerData data = player.getCapability(EsteemedInnovation.PLAYER_DATA, null);
            ItemExosuitArmor item = (ItemExosuitArmor) leggings.getItem();
            if (item.hasUpgrade(leggings, RUN_ASSIST.getItem())) {
                if (data.getLastMotions() == null) {
                    data.setLastMotions(MutablePair.of(entity.posX, entity.posZ));
                }
                double lastX = data.getLastMotions().left;
                double lastZ = data.getLastMotions().right;
                if ((entity.moveForward > 0.0F) && (lastX != entity.posX || lastZ != entity.posZ) && entity.onGround && !entity.isInWater()) {
                    entity.moveRelative(0F, 1F, 0.075F); //entity.moveFlying(0.0F, 1.0F, 0.075F); TODO Test this.
                    if (!chestStack.getTagCompound().hasKey("ticksUntilConsume")) {
                        chestStack.getTagCompound().setInteger("ticksUntilConsume", 2);
                    }
                    if (chestStack.getTagCompound().getInteger("ticksUntilConsume") <= 0) {
                        drainSteam(chestStack, Config.runAssistConsumption);
                    }
                }
            }
        }

        /*
        if (hasPower(entity,100) && entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS) != null && entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() instanceof ItemExosuitArmor && !entity.worldObj.isRemote) {
            ItemExosuitArmor leggings = (ItemExosuitArmor) entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem();
            if (leggings.hasUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS), SteamcraftItems.antiFire)) {
                if (entity.isBurning()) {

                    event.entityLiving.getItemStackFromSlot(EntityEquipmentSlot.CHEST).damageItem(10, event.entityLiving);
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
        EntityLivingBase entity = event.getEntityLiving();
        if (entity == Minecraft.getMinecraft().thePlayer) {
//            if (!worldStartUpdate && entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST) != null && entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() instanceof ItemExosuitArmor) {
//                ItemExosuitArmor chest = (ItemExosuitArmor) entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem();
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
            if (chestItem instanceof ItemExosuitArmor) {
                ItemExosuitArmor chest = (ItemExosuitArmor) chestItem;
                if (chest.hasUpgrade(chestStack, EXTENDO_FIST.getItem())) {
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
        boolean hasPower = hasPower(entity, 1);
        int armor = getExoArmor(entity);
//        ItemStack armor2 = entity.getItemStackFromSlot(EntityEquipmentSlot.FEET);
        //EsteemedInnovation.proxy.extendRange(entity,1.0F);

        PlayerData tag = entity.getCapability(EsteemedInnovation.PLAYER_DATA, null);

        if (entity.worldObj.isRemote) {
            updateRangeClient(event);
        } else {
            boolean wearing = false;

            if (entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST) != null && entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() instanceof ItemExosuitArmor) {
                ItemExosuitArmor chest = (ItemExosuitArmor) entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem();
                if (chest.hasUpgrade(entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST), EXTENDO_FIST.getItem())) {
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
                tag.setLastMotions(MutablePair.of(entity.posX, entity.posZ));
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
            double lastX = tag.getLastMotions().left;
            double lastZ = tag.getLastMotions().right;
            if (ticksLeft <= 0) {
                if (Config.passiveDrain && (lastX != entity.posX || lastZ != entity.posZ)) {
                    drainSteam(stack, 1);
                }
                ticksLeft = 2;
            }

            tag.setLastMotions(MutablePair.of(entity.posX, entity.posZ));

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

    /**
     * Returns the number of Exosuit pieces the Entity is wearing.
     * @param entityLiving The entity being checked against.
     * @return int
     */
    public int getExoArmor(EntityLivingBase entityLiving) {
        int num = 0;

        for (EntityEquipmentSlot armor : ARMOR_SLOTS) {
            ItemStack stack = entityLiving.getItemStackFromSlot(armor);
            if (stack != null && stack.getItem() instanceof ItemExosuitArmor) {
                num++;
            }
        }
        return num;
    }

    @SubscribeEvent
    public void clickLeft(PlayerInteractEvent.RightClickBlock event) {
        BlockPos pos = event.getPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        World world = event.getWorld();
        EnumFacing face = event.getFace();
        EntityPlayer player = event.getEntityPlayer();
        ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (face != EnumFacing.UP && block.isSideSolid(state, world, pos, face)) {
            if (chest != null && chest.getItem() instanceof ItemExosuitArmor) {
                AxisAlignedBB aabb;
                if (face == EnumFacing.DOWN) {
                    aabb = new AxisAlignedBB(x - 0.5F, y + (face.getFrontOffsetY() / 6F) - 0.4F,
                      z - 0.20F, x + 0.5F + 1, y + (face.getFrontOffsetY() / 6F) + 1, z + 0.5F + 1);
                } else {
                    aabb = new AxisAlignedBB(x + (face.getFrontOffsetX() / 6F),
                      y + (face.getFrontOffsetY() / 6F) - 1.0F, z + (face.getFrontOffsetZ() / 6F),
                      x + (face.getFrontOffsetX() / 6F) + 1, y + (face.getFrontOffsetY() / 6F) + 2.0F,
                      z + (face.getFrontOffsetZ() / 6F) + 1);
                }
                ItemExosuitArmor chestArmor = (ItemExosuitArmor) chest.getItem();
                boolean canStick = false;
                List list = world.getEntitiesWithinAABB(EntityPlayer.class, aabb);
                for (Object obj : list) {
                    if (obj == player) {
                        canStick = true;
                    }
                }
                if (canStick && chestArmor.hasUpgrade(chest, PITON_DEPLOYER.getItem())) {
                    if (!world.isRemote) {
                        chest.getTagCompound().setFloat("x", (float) player.posX);
                        chest.getTagCompound().setFloat("z", (float) player.posZ);
                        chest.getTagCompound().setFloat("y", (float) player.posY);
                        chest.getTagCompound().setInteger("blockX", x);
                        chest.getTagCompound().setInteger("blockY", y);
                        chest.getTagCompound().setInteger("blockZ", z);
                        chest.getTagCompound().setBoolean("grappled", true);
                    }
                    player.motionX = 0.0F;
                    player.motionY = 0.0F;
                    player.motionZ = 0.0F;
                    player.fallDistance = 0.0F;
                }
            }
        }

        /*
        I don't know about this stuff, since block shapes and stuff are handled with JSON now. I don't know how we are going to check if it is a solid cube.
        TileEntity tile = world.getTileEntity(pos);
        ItemStack held = player.getHeldItem(player.getActiveHand());
        if (player.isSneaking() && ((tile != null && tile instanceof DisguisableBlock) ||
          block == PipeBlocks.Blocks.BRASS_PIPE.getBlock()) && held != null &&
          held.getItem() instanceof ItemBlock) {
            Block block1 = Block.getBlockFromItem(held.getItem());
            if (!(block1 instanceof BlockContainer) && !(block1 instanceof ITileEntityProvider) &&
              (block1.getRenderType(state) == EnumBlockRenderType.LIQUID || block1.getRenderType() == 39 ||
                block1.getRenderType() == 31) && (block1.renderAsNormalBlock() ||
              (block1 == Blocks.glass && block == SteamcraftBlocks.pipe))) {
                event.setCanceled(true);
            }
        }
        */
    }

    List<DamageSource> invalidSources = Arrays.asList(
      DamageSource.drown,
      DamageSource.outOfWorld,
      DamageSource.starve,
      DamageSource.wither
    );

    private static final int ZINC_CONSUMPTION = Config.zincPlateConsumption;

    @SubscribeEvent
    public void burstZincPlate(LivingHurtEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        float amount = event.getAmount();

        if (!invalidSources.contains(event.getSource())) {
            if (entity instanceof EntityPlayer && hasPower(entity, ZINC_CONSUMPTION)) {
                EntityPlayer player = (EntityPlayer) entity;
                float health = player.getHealth();
                float maxHealth = player.getMaxHealth();
                float halfOfMax = maxHealth / 2;
                if (amount >= halfOfMax || health <= halfOfMax) {
                    ItemStack stackWithPlate = null;
                    boolean hasZincPlate = false;
                    for (int i = 1; i < 5; i++) {
                        ItemStack equipment = entity.getItemStackFromSlot(ItemStackUtility.getSlotFromSlotIndex(i));
                        if (equipment != null) {
                            Item item = equipment.getItem();
                            if (item instanceof ItemExosuitArmor) {
                                ItemExosuitArmor armor = (ItemExosuitArmor) item;
                                if (armor.hasPlates(equipment) &&
                                  UtilPlates.getPlate(equipment.getTagCompound().getString("plate")).getIdentifier().equals("Zinc")) {
                                    stackWithPlate = equipment;
                                    hasZincPlate = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (hasZincPlate) {
                        ItemStack zincPlates = ExosuitUpgradeItems.PlateItems.ZINC_EXO.createItemStack(2);
                        World world = player.worldObj;
                        drainSteam(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST), ZINC_CONSUMPTION);
                        UtilPlates.removePlate(stackWithPlate);
                        EntityItem entityItem = new EntityItem(world, player.posX, player.posY,
                          player.posZ, zincPlates);
                        world.spawnEntityInWorld(entityItem);
//                        player.setHealth(health - (amount - 10.0F));
                        player.setHealth(health);
                        player.performHurtAnimation();
                        world.playSound(player.posX, player.posY, player.posZ, EsteemedInnovation.SOUND_HISS,
                          SoundCategory.PLAYERS, 2F, 0.9F, false);
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void dropDrops(BlockEvent.HarvestDropsEvent event) {
        if (event.getHarvester() == null) {
            return;
        }

        Random rand = new Random();
        BlockPos pos = event.getPos();
        EntityPlayer player = event.getHarvester();
        World world = event.getWorld();
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        Item otherBlockItem = block.getItemDropped(state, rand, 0);
        Item blockItem = Item.getItemFromBlock(block);
        int meta = block.getMetaFromState(state);
        Pair<Item, Integer> pair = Pair.of(blockItem, meta);
        ItemStack equipped = player.getHeldItemMainhand();
        if (equipped == null) {
            return;
        }

        if (equipped.getItem() instanceof ItemSteamDrill) {
            ItemSteamDrill drill = (ItemSteamDrill) equipped.getItem();
            if (!drill.isWound(equipped)) {
                return;
            }

            if (drill.hasUpgrade(equipped, MULTIPLICATIVE_RESONATOR.getItem())) {
                event.getDrops().clear();
                List<ItemStack> trueDrops = block.getDrops(world, pos, state, EnchantmentUtility.getFortuneModifier(player) + 2);
                event.getDrops().addAll(trueDrops);
            }

            if (OreDictHelper.cobblestones.contains(pair)) {
                return;
            }

            if (drill.hasUpgrade(equipped, STONE_GRINDER.getItem())) {
                String harvestTool = block.getHarvestTool(state);
                // Docs say it can be null.
                //noinspection ConstantConditions
                if (harvestTool == null || !harvestTool.equals(drill.toolClass())) {
                    return;
                }
                boolean addedNugget = false;
                for (int i = 0; i < event.getDrops().size(); i++) {
                    ItemStack drop = event.getDrops().get(i);
                    MutablePair item = MutablePair.of(drop.getItem(), drop.getItemDamage());
                    if (!OreDictHelper.stones.contains(item) && !OreDictHelper.cobblestones.contains(item)) {
                        continue;
                    }

                    event.getDrops().remove(i);
                    int chance = rand.nextInt(5);
                    if (chance != 3 || addedNugget) {
                        continue;
                    }

                    int index = rand.nextInt(OreDictHelper.stoneGrinderNuggets.size());
                    Pair nuggetPair = OreDictHelper.stoneGrinderNuggets.get(index);
                    int size = rand.nextInt(3) + 1;
                    ItemStack nugget = new ItemStack((Item) nuggetPair.getLeft(), size, (int) nuggetPair.getRight());
                    event.getDrops().add(nugget);
                    addedNugget = true;
                }
            }

            if (drill.hasUpgrade(equipped, INTERNAL_PROCESSING_UNIT.getItem())) {
                ItemStack out = SmasherRegistry.getOutput(new ItemStack(block, 1, meta));
                if (out != null) {
                    if (rand.nextInt(100) >= Config.smasherDoubleChance) {
                        out.stackSize *= 2;
                    }
                    for (int i = 0; i < event.getDrops().size(); i++) {
                        ItemStack drop = event.getDrops().get(i);
                        if (drop.getItem() == Item.getItemFromBlock(block) && drop.getItemDamage() == meta) {
                            event.getDrops().remove(i);
                        }
                    }
                    event.getDrops().add(out);
                    drill.addSteam(equipped, -(2 * drill.steamPerDurability()), player);
                }
            }
        } else if (equipped.getItem() instanceof ItemSteamShovel) {
            ItemSteamShovel shovel = (ItemSteamShovel) equipped.getItem();
            if (!shovel.hasUpgrade(equipped, SIFTER.getItem()) || !shovel.isWound(equipped)) {
                return;
            }

            for (int i = 0; i < event.getDrops().size(); i++) {
                Item item = event.getDrops().get(i).getItem();
                if (item == blockItem || item == otherBlockItem) {
                    event.getDrops().remove(i);
                }
            }

            if (OreDictHelper.sands.contains(pair)) {
                int chance = rand.nextInt(8);
                if (chance == 5) {
                    int index = rand.nextInt(OreDictHelper.goldNuggets.size());
                    Pair nuggetPair = OreDictHelper.goldNuggets.get(index);
                    int size = rand.nextInt(3) + 1;
                    ItemStack nugget = new ItemStack((Item) nuggetPair.getLeft(), size, (int) nuggetPair.getRight());
                    event.getDrops().add(nugget);
                    return;
                }
            }

            if (block == Blocks.DIRT || block == Blocks.GRASS || block == Blocks.FARMLAND) {
                int chance = rand.nextInt(6);
                if (chance == 4) {
                    int boneOrSeeds = rand.nextInt(2);
                    int stackSize = rand.nextInt(3) + 1;
                    ItemStack itemstack;
                    if (boneOrSeeds == 1) {
                        itemstack = new ItemStack(Items.BONE);
                    } else {
                        itemstack = new ItemStack(Items.WHEAT_SEEDS);
                    }
                    itemstack.stackSize = stackSize;
                    event.getDrops().add(itemstack);
                    return;
                }
            }

            if (block == Blocks.GRAVEL) {
                for (int i = 0; i < event.getDrops().size(); i++) {
                    if (event.getDrops().get(i).getItem() == Items.FLINT) {
                        event.setDropChance(90);
                        return;
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void doPreciseCuttingHead(BlockEvent.HarvestDropsEvent event) {
        if (event.getHarvester() == null) {
            return;
        }

        EntityPlayer player = event.getHarvester();
        BlockPos pos = event.getPos();
        World world = event.getWorld();
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        int meta = block.getMetaFromState(state);
        ItemStack equipped = player.getHeldItemMainhand();
        if (equipped == null) {
            return;
        }

        if (equipped.getItem() instanceof ItemSteamDrill) {
            ItemSteamDrill drill = (ItemSteamDrill) equipped.getItem();
            if (!drill.isWound(equipped)) {
                return;
            }

            if (drill.hasUpgrade(equipped, PRECISE_CUTTING_HEAD.getItem()) &&
              ForgeHooks.canHarvestBlock(block, player, world, pos)) {
                ItemStack toAdd = new ItemStack(block, 1, meta);
                if (toAdd.getItem() == null) {
                    // Special case because lit redstone ore and redstone ore are different blocks entirely...
                    // Hopefully this is changed, or there is a better way to do this available in 1.9.
                    if (block == Blocks.LIT_REDSTONE_ORE) {
                        toAdd = new ItemStack(Blocks.REDSTONE_ORE);
                    } else {
                        toAdd = null;
                    }
                }
                if (toAdd != null) {
                    event.getDrops().clear();
                    event.getDrops().add(toAdd);
                }
            }
        }
    }

    @SubscribeEvent
    public void rebreath(LivingAttackEvent event) {
        int consumption = Config.rebreatherConsumption;
        if (event.getSource() == DamageSource.drown) {
            EntityLivingBase entity = event.getEntityLiving();
            if (entity instanceof EntityPlayer && hasPower(entity, consumption)) {
                EntityPlayer player = (EntityPlayer) entity;
                ItemStack equipment = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                if (equipment != null) {
                    Item helmet = equipment.getItem();
                    if (helmet instanceof ItemExosuitArmor) {
                        ItemExosuitArmor helmetArmor = (ItemExosuitArmor) helmet;
                        if (helmetArmor.hasUpgrade(equipment, REBREATHER.getItem())) {
                            drainSteam(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST), consumption);
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
        BlockPos pos = event.getPos();
        World world = event.getWorld();
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        TileEntity tile = world.getTileEntity(pos);
        if (player == null) {
            return;
        }
//            Vec3 vec = player.getLookVec();
        ItemStack equipped = player.getHeldItemMainhand();
        if (equipped == null || equipped.getItem() == null || block == null) {
            return;
        }

        if (equipped.getItem() instanceof ItemSteamDrill) {
            ItemSteamDrill drill = (ItemSteamDrill) equipped.getItem();
            ItemStack upgrade = SteamToolHelper.getHarvestLevelModifier(equipped);
            if (!drill.isWound(equipped)) {
                return;
            }
            if (upgrade != null) {
                String mat = ItemDrillHeadUpgrade.getMyMaterial(upgrade);
                int harvestLevel = DrillHeadMaterial.materials.get(mat).harvestLevel;
                if (harvestLevel >= block.getHarvestLevel(state)) {
                    block.harvestBlock(world, player, pos, state, tile, equipped);
                    world.setBlockToAir(pos);
                } else {
                    event.setCanceled(true);
                }
            }
            if (drill.hasUpgrade(equipped, BIG_DRILL.getItem()) &&
              block.isToolEffective(drill.toolClass(), state)) {
                mineExtraBlocks(getExtraBlockCoordinates(sideHit), pos, world, drill, equipped, player);
            }
            if (drill.hasUpgrade(equipped, PRECISE_CUTTING_HEAD.getItem())) {
                event.setExpToDrop(0);
            }
        } else if (equipped.getItem() instanceof ItemSteamShovel) {
            ItemSteamShovel shovel = (ItemSteamShovel) equipped.getItem();
            if (!shovel.isWound(equipped)) {
                return;
            }
            if (shovel.hasUpgrade(equipped, ROTARY_BLADES.getItem()) &&
              block.isToolEffective(shovel.toolClass(), state)) {
                mineExtraBlocks(getExtraBlockCoordinates(sideHit), pos, world, shovel, equipped, player);
            } else if (shovel.hasUpgrade(equipped, BACKHOE.getItem())) {
                boolean isFalling = block instanceof BlockFalling;
                int end = isFalling ? pos.getY() + Config.backhoeRange : pos.getY();
                for (int i = pos.getY() - Config.backhoeRange; i < end; i++) {
                    if (i < 0) {
                        continue;
                    }
                    BlockPos pos1 = new BlockPos(pos.getX(), i, pos.getZ());
                    IBlockState state1 = world.getBlockState(pos1);
                    Block block1 = state1.getBlock();
                    if (!block1.isToolEffective(shovel.toolClass(), state1) ||
                      !block1.canHarvestBlock(world, pos1, player)) {
                        continue;
                    }
                    if (Item.getItemFromBlock(block) == Item.getItemFromBlock(block1)) {
                        world.setBlockToAir(pos1);
                        block.harvestBlock(world, player, pos1, state1, world.getTileEntity(pos1), equipped);
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
            if (axe.hasUpgrade(equipped, TIMBER_CHAIN.getItem()) &&
              block.isToolEffective(axe.toolClass(), state)) {
                fellBlocks(world, pos, player, equipped);
            }
            if (axe.hasUpgrade(equipped, FOREST_FIRE.getItem())) {
                burnBlocks(world, pos);
            }
        }
    }

    /**
     * The Hash of quick lava blocks to delete.
     * Key: Pair of dimension ID and BlockPos.
     * Value: Integer, number of ticks to wait. Cannot be more than 30 or bad things will happen.
     */
    public static HashMap<MutablePair<Integer, BlockPos>, Integer> quickLavaBlocks = new HashMap<>();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void placeLava(BlockEvent.HarvestDropsEvent event) {
        EntityPlayer player = event.getHarvester();
        IBlockState state = event.getState();
        BlockPos pos = event.getPos();
        Block block = state.getBlock();
        World world = event.getWorld();
        if (player == null) {
            return;
        }
        ItemStack equipped = player.getHeldItemMainhand();
        if (equipped == null || equipped.getItem() == null || block == null) {
            return;
        }
        if (equipped.getItem() instanceof ItemSteamDrill) {
            ItemSteamDrill drill = (ItemSteamDrill) equipped.getItem();
            if (drill.hasUpgrade(equipped, THERMAL_DRILL.getItem()) && drill.isWound(equipped)) {
                world.setBlockState(pos, Blocks.LAVA.getDefaultState());
                quickLavaBlocks.put(MutablePair.of(player.dimension, pos), new Random().nextInt(30) + 1);
                event.getDrops().clear();
            }
        }
    }

    public static HashMap<MutablePair<EntityPlayer, BlockPos>, Integer> charges = new HashMap<>();
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
        BlockPos pos = event.getPos();
        if (player == null) {
            return;
        }
        ItemStack equipped = player.getHeldItemMainhand();
        if (equipped == null || equipped.getItem() == null) {
            return;
        }
        if (equipped.getItem() instanceof ItemSteamDrill) {
            ItemSteamDrill drill = (ItemSteamDrill) equipped.getItem();
            if (drill.hasUpgrade(equipped, CHARGE_PLACER.getItem()) && drill.isWound(equipped)) {
                Random rand = new Random();
                drill.addSteam(equipped, -(2 * drill.steamPerDurability()), player);
                if (player.worldObj.getDifficulty() == EnumDifficulty.HARD && rand.nextInt(100) < 15) {
                    return;
                }
                int max = 0;
                int min = 0;
                int constant = 0;
                boolean useConstant = false;
                switch (player.worldObj.getDifficulty()) {
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
                MutablePair<EntityPlayer, BlockPos> pair = MutablePair.of(player, pos);
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
     * @param startPos The starting Block Position
     * @param player The player doing the felling.
     * @param axe The axe's ItemStack
     */
    private void fellBlocks(World world, BlockPos startPos, EntityPlayer player, ItemStack axe) {
        ItemSteamAxe sAxe = (ItemSteamAxe) axe.getItem();
        for (int y = startPos.getY(); y < 256; y++) {
            BlockPos curPos = new BlockPos(startPos.getX(), y, startPos.getZ());
            IBlockState state = world.getBlockState(curPos);
            Block block = state.getBlock();
            if (OreDictHelper.listHasItem(OreDictHelper.logs, Item.getItemFromBlock(block))) {
                world.setBlockToAir(curPos);
                block.harvestBlock(world, player, curPos, state, world.getTileEntity(curPos), axe);
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
     * @param startPos The starting Block Position
     */
    private void burnBlocks(World world, BlockPos startPos) {
        int startX = startPos.getX();
        int startY = startPos.getY();
        int startZ = startPos.getZ();
        for (int x = startX - 5; x < startX + 5; x++) {
            for (int y = startY - 5; y < startY + 5; y++) {
                for (int z = startZ - 5; z < startZ + 5; z++) {
                    BlockPos curPos = new BlockPos(x, y, z);
                    IBlockState state = world.getBlockState(curPos);
                    Block block = state.getBlock();
                    if (block == null || world.isAirBlock(curPos)) {
                        continue;
                    }
                    if (block.isFlammable(world, curPos, EnumFacing.getFront(sideHit))) {
                        world.setBlockState(curPos, Blocks.FIRE.getDefaultState());
                    }
                }
            }
        }
    }

    /**
     * Gets whether the block can be tilled into farmland.
     * @param block The block to check
     * @return True if it is dirt or grass, else false.
     */
    private boolean isFarmable(Block block) {
        return (block != null && (block == Blocks.DIRT || block == Blocks.GRASS));
    }

    @SubscribeEvent
    public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        // TODO: Perhaps make sideHit an EnumFacing?
        sideHit = event.getFace().getIndex();
        World world = event.getWorld();
        if (world.isRemote) {
            return;
        }
        EntityPlayer player = event.getEntityPlayer();
        ItemStack equipped = player.getHeldItemMainhand();
        if (equipped == null) {
            return;
        }
        BlockPos pos = event.getPos();
        if (equipped.getItem() instanceof ItemSteamAxe) {
            ItemSteamAxe axe = (ItemSteamAxe) equipped.getItem();
            if (!axe.isWound(equipped)) {
                return;
            }

            if (axe.hasUpgrade(equipped, LEAF_BLOWER.getItem())) {
                blowLeaves(getExtraBlock9Coordinates(sideHit), pos, world, player, equipped);
            }
        } else if (equipped.getItem() instanceof ItemSteamShovel) {
            ItemSteamShovel shovel = (ItemSteamShovel) equipped.getItem();
            if (shovel.hasUpgrade(equipped, CULTIVATOR.getItem()) &&
              shovel.isWound(equipped)) {
                int[][] coords = extraBlocksVertical;
                for (int[] aCoordinateArray : coords) {
                    int thisX = pos.getX() + aCoordinateArray[0];
                    int thisY = pos.getY() + aCoordinateArray[1];
                    int thisZ = pos.getZ() + aCoordinateArray[2];

                    BlockPos thisPos = new BlockPos(thisX, thisY, thisZ);

                    Block block1 = world.getBlockState(thisPos).getBlock();
                    if (isFarmable(block1)) {
                        world.setBlockToAir(thisPos);
                        world.setBlockState(thisPos, Blocks.FARMLAND.getDefaultState());
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void doChainsaw(LivingAttackEvent event) {
        if (!(event.getSource().getSourceOfDamage() instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) event.getSource().getSourceOfDamage();
        ItemStack equipped = player.getHeldItemMainhand();
        if (equipped == null || equipped.getItem() == null || !(equipped.getItem() instanceof ItemSteamAxe)) {
            return;
        }
        ItemSteamAxe axe = (ItemSteamAxe) equipped.getItem();
        if (!axe.isWound(equipped) || !axe.hasUpgrade(equipped, CHAINSAW.getItem())) {
            return;
        }

        if (!player.worldObj.isRemote) {
            event.getEntityLiving().attackEntityFrom(DamageSource.generic, 9.0F);
            event.getEntityLiving().addPotionEffect(new PotionEffect(SLOWNESS_POTION, 10, 10));
        }
    }

    @SubscribeEvent
    public void toggleDrillDash(LivingEvent.LivingJumpEvent event) {
        if (!(event.getEntityLiving() instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        ItemStack equipped = player.getHeldItemMainhand();
        if (equipped == null || !player.isSneaking()) {
            return;
        }
        Item equippedItem = equipped.getItem();
        if (equippedItem == null || !(equippedItem instanceof ItemSteamDrill)) {
            return;
        }
        ItemSteamDrill drill = (ItemSteamDrill) equippedItem;
        if (!drill.isWound(equipped) || !drill.hasUpgrade(equipped, BATTLE_DRILL.getItem())) {
            return;
        }

        Vec3d vector = player.getLook(0.5F);

        double total = Math.abs(vector.zCoord + vector.xCoord);
//        if (vector.yCoord < total) {
//            vector.yCoord = total;
//        }

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
        Vec3d vec = player.getLookVec();
        double x = vec.xCoord + player.posX;
        double y = vec.yCoord + player.posY;
        double z = vec.zCoord + player.posZ;

        AxisAlignedBB aabb = new AxisAlignedBB(x - 5, y - 5, z - 5, x + 5, y + 5, z + 5);
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
        ItemStack equipped = event.getEntityPlayer().getHeldItemMainhand();
        BlockPos pos = event.getPos();
        World world = event.getEntity().worldObj;
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (equipped != null && equipped.getItem() != null && block != null) {
            float newSpeed = 0.0F;
            float original = event.getOriginalSpeed();
            if (equipped.getItem() instanceof ItemSteamDrill) {
                ItemSteamDrill drill = (ItemSteamDrill) equipped.getItem();
                if (drill.isWound(equipped)) {
                    if (drill.hasUpgrade(equipped, BIG_DRILL.getItem())) {
                        newSpeed = original * 0.7F;
                    }
                    if (drill.hasUpgrade(equipped, INTERNAL_PROCESSING_UNIT.getItem())) {
                        if (newSpeed == 0.0F) {
                            newSpeed = original / 2;
                        } else {
                            newSpeed /= 2;
                        }
                    }
                    if (drill.hasUpgrade(equipped, THERMAL_DRILL.getItem()) ||
                      drill.hasUpgrade(equipped, CHARGE_PLACER.getItem())) {
                        if (newSpeed == 0.0F) {
                            newSpeed = original * 5;
                        } else {
                            newSpeed *= 5;
                        }
                    }
                    if (drill.hasUpgrade(equipped, BATTLE_DRILL.getItem())) {
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
                    if (axe.hasUpgrade(equipped, LEAF_BLOWER.getItem())) {
                        newSpeed = original / 5F;
                    }
                    if (axe.hasUpgrade(equipped, TIMBER_CHAIN.getItem())) {
                        if (newSpeed == 0.0F) {
                            newSpeed = original * 0.7F;
                        } else {
                            newSpeed *= 0.7F;
                        }
                    }
                    if (axe.hasUpgrade(equipped, CHAINSAW.getItem())) {
                        if (newSpeed == 0.0F) {
                            newSpeed = original / 1.7F;
                        } else {
                            newSpeed /= 1.7F;
                        }
                    }
                }
            } else if (equipped.getItem() instanceof ItemSteamShovel) {
                ItemSteamShovel shovel = (ItemSteamShovel) equipped.getItem();
                if (shovel.isWound(equipped) && shovel.hasUpgrade(equipped, ROTARY_BLADES.getItem())) {
                    newSpeed = original * 0.425F;
                }
            }
            if (newSpeed != 0.0F) {
                event.setNewSpeed(newSpeed);
            }
        }
    }

    // { x, y, z } relatively

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

    private static int[][] extraBlocks9Side = {
      { 0, 4, -4 }, { 0, 4, -3 }, { 0, 4, -2 }, { 0, 4, -1 }, { 0, 4, 0 }, { 0, 4, 1 }, { 0, 4, 2 }, { 0, 4, 3 }, { 0, 4, 4 },
      { 0, 3, -4 }, { 0, 3, -3 }, { 0, 3, -2 }, { 0, 3, -1 }, { 0, 3, 0 }, { 0, 3, 1 }, { 0, 3, 2 }, { 0, 3, 3 }, { 0, 3, 4 },
      { 0, 2, -4 }, { 0, 3, -3 }, { 0, 2, -2 }, { 0, 2, -1 }, { 0, 2, 0 }, { 0, 2, 1 }, { 0, 2, 2 }, { 0, 2, 3 }, { 0, 2, 4 },
      { 0, 1, -4 }, { 0, 2, -3 }, { 0, 1, -2 }, { 0, 1, -1 }, { 0, 1, 0 }, { 0, 1, 1 }, { 0, 1, 2 }, { 0, 1, 3 }, { 0, 1, 4 },
      { 0, 0, -4 }, { 0, 0, -3 }, { 0, 0, -2 }, { 0, 0, -1 }, { 0, 0, 0 }, { 0, 0, 1 }, { 0, 0, 2 }, { 0, 0, 3 }, { 0, 0, 4 },
      { 0, -1, -4 }, { 0, -1, -3 }, { 0, -1, -2 }, { 0, -1, -1 }, { 0, -1, 0 }, { 0, -1, 1 }, { 0, -1, 2 }, { 0, -1, 3 }, { 0, -1, 4 },
      { 0, -2, -4 }, { 0, -2, -3 }, { 0, -2, -2 }, { 0, -2, -1 }, { 0, -2, 0 }, { 0, -2, 1 }, { 0, -2, 2 }, { 0, -2, 3 }, { 0, -2, 4 },
      { 0, -3, -4 }, { 0, -3, -3 }, { 0, -3, -2 }, { 0, -3, -1 }, { 0, -3, 0 }, { 0, -3, 1 }, { 0, -3, 2 }, { 0, -3, 3 }, { 0, -3, 4 },
      { 0, -4, -4 }, { 0, -4, -3 }, { 0, -4, -2 }, { 0, -4, -1 }, { 0, -4, 0 }, { 0, -4, 1 }, { 0, -4, 2 }, { 0, -4, 3 }, { 0, -4, 4 },
    };

    private static int[][] extraBlocks9Forward = {
      { -4, 4, 0 }, { -3, 4, 0 }, { -2, 4, 0 }, { -1, 4, 0 }, { 0, 4, 0 }, { 1, 4, 0 }, { 2, 4, 0 }, { 3, 4, 0 }, { 4, 4, 0 },
      { -4, 3, 0 }, { -3, 3, 0 }, { -2, 3, 0 }, { -1, 3, 0 }, { 0, 3, 0 }, { 1, 3, 0 }, { 2, 3, 0 }, { 3, 3, 0 }, { 4, 3, 0 },
      { -4, 2, 0 }, { -3, 2, 0 }, { -2, 2, 0 }, { -1, 2, 0 }, { 0, 2, 0 }, { 1, 2, 0 }, { 2, 2, 0 }, { 3, 2, 0 }, { 4, 2, 0 },
      { -4, 1, 0 }, { -3, 1, 0 }, { -2, 1, 0 }, { -1, 1, 0 }, { 0, 1, 0 }, { 1, 1, 0 }, { 2, 1, 0 }, { 3, 1, 0 }, { 4, 1, 0 },
      { -4, 0, 0 }, { -3, 0, 0 }, { -2, 0, 0 }, { -1, 0, 0 }, { 0, 0, 0 }, { 1, 0, 0 }, { 2, 0, 0 }, { 3, 0 , 0 }, { 4, 0, 0 },
      { -4, -1, 0 }, { -3, -1, 0 }, { -2, -1, 0 }, { -1, -1, 0 }, { 0, -1, 0 }, { 1, -1, 0 }, { 2, -1, 0 }, { 3, -1, 0 }, { 4, -1, 0 },
      { -4, -2, 0 }, { -3, -2, 0 }, { -2, -2, 0 }, { -1, -2, 0 }, { 0, -2, 0 }, { 1, -2, 0 }, { 2, -2, 0 }, { 3, -2, 0 }, { 4, -2, 0 },
      { -4, -3, 0 }, { -3, -3, 0 }, { -2, -3, 0 }, { -1, -3, 0 }, { 0, -3, 0 }, { 1, -3, 0 }, { 2, -3, 0 }, { 3, -3, 0 }, { 4, -3, 0 },
      { -4, -4, 0 }, { -3, -4, 0 }, { -2, -4, 0 }, { -1, -4, 0 }, { 0, -4, 0 }, { 1, -4, 0 }, { 2, -4, 0 }, { 3, -4, 0 }, { 4, -4, 0 },
    };

    private static int[] [] extraBlocks9Vertical = {
      { -4, 0, 4 }, { -4, 0, 4 }, { -2, 0, 4 }, { -4, 0, 4 }, { 0, 0, 4 }, { 1, 0, 4 }, { 2, 0, 4 }, { 3, 0, 4 }, { 4, 0, 4},
      { -4, 0, 3 }, { -3, 0, 3 }, { -2, 0, 3 }, { -3, 0, 3 }, { 0, 0, 3 }, { 1, 0, 3 }, { 2, 0, 3 }, { 3, 0, 3 }, { 4, 0, 3},
      { -4, 0, 2 }, { -3, 0, 2 }, { -2, 0, 2 }, { -1, 0, 2 }, { 0, 0, 2 }, { 1, 0, 2 }, { 2, 0, 2 }, { 3, 0, 2 }, { 4, 0, 2 },
      { -4, 0, 1 }, { -3, 0, 1 }, { -2, 0, 1 }, { -1, 0, 1 }, { 0, 0, 1 }, { 1, 0, 1 }, { 2, 0, 1 }, { 3, 0, 1 }, { 4, 0, 1 },
      { -4, 0, 0 }, { -3, 0, 0 }, { -2, 0, 0 }, { -1, 0, 0 }, { 0, 0, 0 }, { 1, 0, 0 }, { 2, 0, 0 }, { 3, 0, 0 }, { 4, 0, 0 },
      { -4, 0, -1 }, { -3, 0, -1 }, { -2, 0, -1 }, { -1, 0, -1 }, { 0, 0, -1 }, { 1, 0, -1 }, { 2, 0, -1 }, { 3, 0, -1 }, { 4, 0, -1 },
      { -4, 0, -2 }, { -3, 0, -2 }, { -2, 0, -2 }, { -1, 0, -2 }, { 0, 0, -2 }, { 1, 0, -2 }, { 2, 0, -2 }, { 3, 0, -2 }, { 4, 0, -2 },
      { -4, 0, -3 }, { -3, 0, -3 }, { -2, 0, -3 }, { -1, 0, -3 }, { 0, 0, -3 }, { 1, 0, -3 }, { 2, 0, -3 }, { 3, 0, -3 }, { 4, 0, -3 },
      { -4, 0, -4 }, { -3, 0, -4 }, { -2, 0, -4 }, { -1, 0, -4 }, { 0, 0, -4 }, { 1, 0, -4 }, { 2, 0, -4 }, { 3, 0, -4 }, { 4, 0, -4 },
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

    private int[][] getExtraBlock9Coordinates(int sideHit) {
        switch (sideHit) {
            case 5: return extraBlocks9Side;
            case 4: return extraBlocks9Side;
            case 3: return extraBlocks9Forward;
            case 1: return extraBlocks9Vertical;
            case 0: return extraBlocks9Vertical;
            default: return extraBlocks9Forward;
        }
    }

    /**
     * This mines the extra blocks within the coordinate array.
     * @param coordinateArray The array of arrays containing the coordinates to add to x, y, z.
     * @param startPos The starting position
     * @param world The world.
     * @param tool The tool mining.
     * @param toolStack The ItemStack of the tool.
     * @param player The player mining.
     */
    private void mineExtraBlocks(int[][] coordinateArray, BlockPos startPos, World world, ItemTool tool, ItemStack toolStack, EntityPlayer player) {
//        boolean isDrill = tool instanceof ItemSteamDrill;
//        boolean isAxe = tool instanceof ItemSteamAxe;
        boolean isShovel = tool instanceof ItemSteamShovel;
        for (int[] aCoordinateArray : coordinateArray) {
            int thisX = startPos.getX() + aCoordinateArray[0];
            int thisY = startPos.getY() + aCoordinateArray[1];
            int thisZ = startPos.getZ() + aCoordinateArray[2];
            BlockPos thisPos = new BlockPos(thisX, thisY, thisZ);
            IBlockState state = world.getBlockState(thisPos);
            Block block = state.getBlock();

            // For some reason, canHarvestBlock is false when using the Steam Shovel.
            String toolClass = block.getHarvestTool(state);
            boolean canHarvest = tool.canHarvestBlock(state, toolStack) ||
              (isShovel && toolClass != null && toolClass.equals(((SteamTool) tool).toolClass()));
            if (block != null && world.isAirBlock(thisPos) && canHarvest) {
//                world.spawnParticle("")
//                world.func_147480_a(thisX, thisY, thisZ, false);
                world.setBlockToAir(thisPos);
                block.harvestBlock(world, player, thisPos, state, world.getTileEntity(thisPos), toolStack);
            }
        }
    }

    /**
     * Harvests the coordinates in the coordinate array.
     * @param coordinateArray The two-dimensional array containing coordinates to add to x, y, z.
     * @param startPos The starting position
     * @param world The world.
     * @param player The player mining.
     * @param stack The tool being used to mine.
     */
    private void blowLeaves(int[][] coordinateArray, BlockPos startPos, World world, EntityPlayer player, ItemStack stack) {
        for (int[] aCoordinateArray : coordinateArray) {
            int thisX = startPos.getX() + aCoordinateArray[0];
            int thisY = startPos.getY() + aCoordinateArray[1];
            int thisZ = startPos.getZ() + aCoordinateArray[2];
            BlockPos thisPos = new BlockPos(thisX, thisY, thisZ);
            IBlockState state = world.getBlockState(thisPos);
            Block block = state.getBlock();
            if (block == null || world.isAirBlock(thisPos)) {
                continue;
            }
            if (isLeaves(block, world, thisPos)) {
                world.setBlockToAir(thisPos);
                block.harvestBlock(world, player, thisPos, state, world.getTileEntity(thisPos), stack);
            }
        }
    }

    private ArrayList<Material> LEAF_MATERIALS = new ArrayList<Material>() { {
        add(Material.LEAVES);
        add(Material.CORAL);
        add(Material.CRAFTED_SNOW);
        add(Material.PLANTS);
    }};

    /**
     * Returns whether the block can be blown by the leaf blower.
     * @param block The block
     * @param world The world
     * @param pos The block's position
     * @return Whether the leaf blower should blow this block away.
     */
    private boolean isLeaves(Block block, World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return (OreDictHelper.listHasItem(OreDictHelper.leaves, Item.getItemFromBlock(block)) ||
          block.isLeaves(state, world, pos) || LEAF_MATERIALS.contains(block.getMaterial(state)));
    }
}
