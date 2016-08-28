package eiteam.esteemedinnovation.item.firearm;

import eiteam.esteemedinnovation.Config;
import eiteam.esteemedinnovation.api.IEngineerable;
import eiteam.esteemedinnovation.api.IRenderItem;
import eiteam.esteemedinnovation.api.util.UtilMisc;
import eiteam.esteemedinnovation.api.enhancement.IEnhancement;
import eiteam.esteemedinnovation.api.enhancement.IEnhancementFirearm;
import eiteam.esteemedinnovation.api.enhancement.UtilEnhancements;
import eiteam.esteemedinnovation.entity.projectile.EntityMusketBall;
import eiteam.esteemedinnovation.gui.GuiEngineeringTable;
import eiteam.esteemedinnovation.handler.GenericEventHandler;
import eiteam.esteemedinnovation.init.items.armor.ExosuitUpgradeItems;
import eiteam.esteemedinnovation.init.items.firearms.FirearmItems;
import eiteam.esteemedinnovation.item.armor.exosuit.ItemExosuitArmor;
import eiteam.esteemedinnovation.misc.ItemStackUtility;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.List;

import static eiteam.esteemedinnovation.init.items.firearms.FirearmAmmunitionItems.Items.MUSKET_CARTRIDGE;

public class ItemFirearm extends Item implements IEngineerable, IRenderItem {
    public float damage;
    public int reloadTime;
    public int shellCount;
    public float accuracy;
    public float knockback;
    public boolean shotgun;
    public Object repairMaterial;
    /**
     * Used only for the Reloading Holsters, hence why it is private. Don't make it public, as that
     * could confuse other developers into thinking they should use it for things.
     */
    private int ticksSinceReload;

    public ItemFirearm(float damage, int reloadTime, float accuracy, float knockback, boolean shotgun, int shellCount) {
        this(damage, reloadTime, accuracy, knockback, shotgun, shellCount, null);
    }

    public ItemFirearm(float damage, int reloadTime, float accuracy, float knockback, boolean shotgun, int shellCount, Object repair) {
        maxStackSize = 1;
        setMaxDamage(384);
        this.damage = damage;
        this.reloadTime = reloadTime;
        this.accuracy = accuracy;
        this.knockback = knockback;
        this.shotgun = shotgun;
        this.shellCount = shellCount;
        this.repairMaterial = repair;
    }

    public static void initializeNBT(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
            NBTTagCompound nbt = stack.getTagCompound();
            nbt.setInteger("loaded", 0);
            nbt.setInteger("numloaded", 0);
        }
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean advanced) {
        if (UtilEnhancements.hasEnhancement(stack)) {
            list.add(UtilEnhancements.getEnhancementDisplayText(stack));
        }
        super.addInformation(stack, player, list, advanced);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isCurrentItem) {
        super.onUpdate(stack, world, entity, itemSlot, isCurrentItem);
        if (entity instanceof EntityPlayerSP) {
            EntityPlayerSP player = (EntityPlayerSP) entity;
            ItemStack usingItem = player.getActiveItemStack();
            if (usingItem != null && usingItem.getItem() == this && UtilEnhancements.hasEnhancement(usingItem) &&
              "Speedy".equals(UtilEnhancements.getEnhancementFromItem(usingItem).getID())) {
                player.movementInput.moveForward *= 5.0F;
                player.movementInput.moveStrafe *= 5.0F;
            }
        }

        // Reloading Holster code.
        if (!isCurrentItem && entity instanceof EntityPlayer && stack.hasTagCompound()) {
            ticksSinceReload += 1;
            NBTTagCompound nbt = stack.getTagCompound();
            EntityPlayer player = (EntityPlayer) entity;
            ItemStack legs = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
            if (legs != null && nbt.getInteger("loaded") < 1 && ticksSinceReload >= (reloadTime + 10)) {
                Item legsItem = legs.getItem();
                if (legsItem instanceof ItemExosuitArmor) {
                    ItemExosuitArmor legsArmor = (ItemExosuitArmor) legsItem;
                    if (legsArmor.hasUpgrade(legs, ExosuitUpgradeItems.Items.RELOADING_HOLSTERS.getItem()) &&
                      GenericEventHandler.hasPower(player, Config.reloadingConsumption) &&
                      ItemStackUtility.inventoryHasItem(player.inventory, MUSKET_CARTRIDGE.getItem())) {
                        onItemUseFinish(stack, world, player);
                        onItemRightClick(stack, world, player, player.getActiveHand());
                        GenericEventHandler.drainSteam(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST),
                          Config.reloadingConsumption);
                        ticksSinceReload = 0;
                        // TODO: Reload sound
                        // TODO: Out of ammo- cannot reload sound
                    }
                }
            }
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        if (UtilEnhancements.hasEnhancement(stack)) {
            return UtilEnhancements.getNameFromEnhancement(stack);
        } else {
            return super.getUnlocalizedName(stack);
        }
    }

    @Override
    public ResourceLocation getIcon(ItemStack stack) {
        if (UtilEnhancements.hasEnhancement(stack)) {
            return UtilEnhancements.getIconFromEnhancement(stack);
        }
        return null;
    }

    @Override
    public int renderPasses(ItemStack self) {
        return 1;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityLivingBase entity, int timeLeft) {
        NBTTagCompound nbt = itemstack.getTagCompound();
        boolean crouched = entity.isSneaking();

        if (nbt.getInteger("loaded") > 0) {
            float enhancementAccuracy = 0.0F;
            float enhancementDamage = 0;
            float enhancementKnockback = 0;
            float crouchingBonus = crouched ? 0.10F : 0;

            if (UtilEnhancements.hasEnhancement(itemstack)) {
                IEnhancement enhancement = UtilEnhancements.getEnhancementFromItem(itemstack);
                if (UtilEnhancements.getEnhancementFromItem(itemstack) instanceof IEnhancementFirearm) {
                    IEnhancementFirearm enhancementFirearm = (IEnhancementFirearm) enhancement;
                    enhancementAccuracy = enhancementFirearm.getAccuracyChange(this);
                    enhancementDamage = enhancementFirearm.getDamageChange(this);
                    enhancementKnockback = enhancementFirearm.getKnockbackChange(this);
                }
            }
            int timeUsed = getMaxItemUseDuration(itemstack) - timeLeft;
            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                ArrowLooseEvent event = new ArrowLooseEvent(player, itemstack, world, timeUsed,
                  ItemStackUtility.inventoryHasItem(player.inventory, MUSKET_CARTRIDGE.getItem()));
                MinecraftForge.EVENT_BUS.post(event);

                if (event.isCanceled()) {
                    return;
                }

                timeUsed = event.getCharge();
                float timeUsedSecs = timeUsed / 20.0F;
                timeUsedSecs = (timeUsedSecs * timeUsedSecs + timeUsedSecs * 2.0F) / 3.0F;

                if (timeUsedSecs < 0.1D) {
                    return;
                }

                if (timeUsedSecs > 1.0F) {
                    timeUsedSecs = 1.0F;
                }

                EntityMusketBall musketBall = new EntityMusketBall(world, player, 2.0F, ((1.0F + accuracy + enhancementAccuracy - crouchingBonus) - timeUsedSecs), (damage + enhancementDamage), true);

                if (UtilEnhancements.hasEnhancement(itemstack)) {
                    if (UtilEnhancements.getEnhancementFromItem(itemstack) instanceof IEnhancementFirearm) {
                        musketBall = ((IEnhancementFirearm) UtilEnhancements.getEnhancementFromItem(itemstack)).changeBullet(musketBall);
                    }
                }

                itemstack.damageItem(1, entity);
                float vol = ((knockback + enhancementKnockback) * (2F / 5F)) * (UtilEnhancements.getEnhancementFromItem(itemstack) != null && "Silencer".equals(UtilEnhancements.getEnhancementFromItem(itemstack).getID()) ? 0.4F : 1.0F);
                float pitch = 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + timeUsedSecs * 0.5F;
                world.playSound(player, player.getPosition(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, vol, pitch);
                for (int i = 1; i < 16; i++) {
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, entity.posX + itemRand.nextFloat() - 0.5F,
                      entity.posY + (itemRand.nextFloat() / 2) - 0.25F,
                      entity.posZ + itemRand.nextFloat() - 0.5F, 0D, 0.01D, 0D);
                }

                if (!world.isRemote) {
                    if (shotgun) {
                        for (int i = 1; i < 21; i++) {
                            EntityMusketBall shotgunRound = new EntityMusketBall(world, player, 2.0F,
                              (1.0F + accuracy + enhancementAccuracy - crouchingBonus) - timeUsedSecs,
                              (damage + enhancementDamage), false);
                            if (UtilEnhancements.hasEnhancement(itemstack)) {
                                if (UtilEnhancements.getEnhancementFromItem(itemstack) instanceof IEnhancementFirearm) {
                                    shotgunRound = ((IEnhancementFirearm) UtilEnhancements.getEnhancementFromItem(itemstack)).changeBullet(shotgunRound);
                                }
                            }
                            world.spawnEntityInWorld(shotgunRound);
                        }
                    } else {
                        world.spawnEntityInWorld(musketBall);
                    }
                }

                nbt.setInteger("loaded", nbt.getInteger("loaded") - 1);

                if (world.isRemote && !player.capabilities.isCreativeMode) {
                    float thiskb = this.knockback + enhancementKnockback;
                    boolean crouching = entity.isSneaking();

                    if (crouching) {
                        thiskb = thiskb / 2;
                    }

                    entity.rotationPitch = entity.rotationPitch - (thiskb * 3F);
                    entity.motionZ = -MathHelper.cos((entity.rotationYaw) * (float) Math.PI / 180.0F) * (thiskb * (4F / 50F));
                    entity.motionX = MathHelper.sin((entity.rotationYaw) * (float) Math.PI / 180.0F) * (thiskb * (4F / 50F));
                }
            }
        } else if (nbt.getBoolean("done")) {
            nbt.setInteger("loaded", nbt.getInteger("numloaded"));
            nbt.setBoolean("done", false);
        }
    }

    private int getEnhancementShells(ItemStack self) {
        if (UtilEnhancements.hasEnhancement(self)) {
            IEnhancement enhancement = UtilEnhancements.getEnhancementFromItem(self);
            if (enhancement instanceof IEnhancementFirearm) {
                IEnhancementFirearm enhancementFirearm = (IEnhancementFirearm) enhancement;
                return enhancementFirearm.getClipSizeChange(this);
            }
        }
        return 0;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity) {
        NBTTagCompound nbt = stack.getTagCompound();
        if (!(entity instanceof EntityPlayer)) {
            // ? Not sure what else to return here. Null?
            return stack;
        }
        EntityPlayer player = (EntityPlayer) entity;
        boolean infiniteShots = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
        int enhancementShells = getEnhancementShells(stack);
        if (infiniteShots || ItemStackUtility.inventoryHasItem(player.inventory, MUSKET_CARTRIDGE.getItem())) {
            if (!nbt.getBoolean("done")) {
                nbt.setInteger("numloaded", 1);
                if (infiniteShots) {
                    nbt.setInteger("numloaded", shellCount + enhancementShells);
                } else {
                    ItemStackUtility.consumePlayerInventoryItem(player.inventory, MUSKET_CARTRIDGE.getItem());
                    int totalShells = shellCount + enhancementShells;
                    if (totalShells > 1) {
                        for (int i = 1; i < totalShells; i++) {
                            if (ItemStackUtility.inventoryHasItem(player.inventory, MUSKET_CARTRIDGE.getItem())) {
                                ItemStackUtility.consumePlayerInventoryItem(player.inventory, MUSKET_CARTRIDGE.getItem());
                                nbt.setInteger("numloaded", nbt.getInteger("numloaded") + 1);
                            }
                        }
                    }
                }

                nbt.setBoolean("done", true);
                float vol = UtilEnhancements.getEnhancementFromItem(stack) != null && "Silencer".equals(UtilEnhancements.getEnhancementFromItem(stack).getID()) ? 0.4F : 1.0F;
                float pitch = world.rand.nextFloat() * 0.1F + 0.9F;
                world.playSound(player, player.getPosition(), SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.PLAYERS, vol, pitch);
            }
        }

        return stack;
    }

    @Override
    public boolean isFull3D() {
        return true;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        initializeNBT(stack);
        NBTTagCompound nbt = stack.getTagCompound();

        int enhancementReload = 0;
        if (UtilEnhancements.hasEnhancement(stack)) {
            IEnhancement enhancement = UtilEnhancements.getEnhancementFromItem(stack);
            if (enhancement instanceof IEnhancementFirearm) {
                IEnhancementFirearm enhancementFirearm = (IEnhancementFirearm) enhancement;
                enhancementReload = enhancementFirearm.getReloadChange(this);
            }
        }

        if ((nbt.getInteger("loaded") > 0) || (nbt.getBoolean("done"))) {
            return 72000;
        } else {
            return reloadTime + enhancementReload;
        }
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        initializeNBT(stack);
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt.getInteger("loaded") > 0) {
            return EnumAction.BOW;
        } else {
            return EnumAction.BLOCK;
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand) {
        initializeNBT(itemStack);
        NBTTagCompound nbt = itemStack.getTagCompound();
        if (player.capabilities.isCreativeMode) {
            int enhancementShells = getEnhancementShells(itemStack);
            nbt.setInteger("loaded", 1);
            nbt.setInteger("numloaded", shellCount + enhancementShells);
        }
        player.setActiveHand(hand);
        return ActionResult.newResult(EnumActionResult.SUCCESS, itemStack);
    }

    // Note: The alternative to this warning is pretty horrible and like 3 lines longer, because generics are not invariant.
    @SuppressWarnings("unchecked")
    @Override
    public MutablePair<Integer, Integer>[] engineerCoordinates() {
        return new MutablePair[] { MutablePair.of(53, 29) };
    }

    @Override
    public ItemStack getStackInSlot(ItemStack me, int var1) {
        if (UtilEnhancements.hasEnhancement(me)) {
            Item item = (Item) UtilEnhancements.getEnhancementFromItem(me);
            return new ItemStack(item);
        }
        return null;
    }

    public static void initializeEnhancementsNBT(ItemStack me, ItemStack stack) {
        if (!me.hasTagCompound()) {
            me.setTagCompound(new NBTTagCompound());
        }
        if (stack != null) {
            IEnhancement enhancement = (IEnhancement) stack.getItem();
            NBTTagCompound enhancements = new NBTTagCompound();
            enhancements.setString("id", enhancement.getID());
            me.getTagCompound().setTag("enhancements", enhancements);
        }
    }

    @Override
    public void setInventorySlotContents(ItemStack me, int var1, ItemStack stack) {
        initializeEnhancementsNBT(me, stack);
    }

    @Override
    public boolean isItemValidForSlot(ItemStack me, int var1, ItemStack var2) {
        return true;
    }

    @Override
    public ItemStack decrStackSize(ItemStack me, int var1, int var2) {
        if (UtilEnhancements.hasEnhancement(me)) {
            Item item = (Item) UtilEnhancements.getEnhancementFromItem(me);
            UtilEnhancements.removeEnhancement(me);
            return new ItemStack(item);
        }
        return null;
    }

    @Override
    public void drawSlot(GuiContainer guiEngineeringTable, int slotnum, int i, int j) {
        guiEngineeringTable.mc.getTextureManager().bindTexture(GuiEngineeringTable.GUI_TEXTURES);
        guiEngineeringTable.drawTexturedModalRect(i, j, 176, 0, 18, 18);
    }

    @Override
    public boolean canPutInSlot(ItemStack me, int slotNum, ItemStack upgrade) {
        return upgrade.getItem() instanceof IEnhancement && ((IEnhancement) upgrade.getItem()).canApplyTo(me);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        if (repairMaterial != null) {
            if (repairMaterial instanceof ItemStack) {
                return repair.isItemEqual((ItemStack) repairMaterial) || super.getIsRepairable(toRepair, repair);
            }
            if (repairMaterial instanceof String) {
                return UtilMisc.doesMatch(repair, (String) repairMaterial) || super.getIsRepairable(toRepair, repair);
            }
        }
        return super.getIsRepairable(toRepair, repair);
    }

    @Override
    public void drawBackground(GuiEngineeringTable guiEngineeringTable, int i, int j, int k) {
        guiEngineeringTable.mc.getTextureManager().bindTexture(ItemExosuitArmor.LARGE_ICONS);
        int textureX = 0;
        if (this == FirearmItems.Items.MUSKET.getItem()) {
            textureX = 0;
        } else if (this == FirearmItems.Items.BLUNDERBUSS.getItem()) {
            textureX = 64;
        } else if (this == FirearmItems.Items.PISTOL.getItem()) {
            textureX = 128;
        }
        guiEngineeringTable.drawTexturedModalRect(j + 26, k + 3, textureX, 64, 64, 64);
    }
}