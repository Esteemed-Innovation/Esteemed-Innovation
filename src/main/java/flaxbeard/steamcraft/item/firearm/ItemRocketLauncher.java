package flaxbeard.steamcraft.item.firearm;

import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.IEngineerable;
import flaxbeard.steamcraft.api.IRenderItem;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.enhancement.IRocket;
import flaxbeard.steamcraft.api.util.UtilMisc;
import flaxbeard.steamcraft.api.enhancement.IEnhancement;
import flaxbeard.steamcraft.api.enhancement.IEnhancementRocketLauncher;
import flaxbeard.steamcraft.api.enhancement.UtilEnhancements;
import flaxbeard.steamcraft.entity.projectile.EntityRocket;
import flaxbeard.steamcraft.gui.GuiEngineeringTable;
import flaxbeard.steamcraft.item.armor.exosuit.ItemExosuitArmor;
import flaxbeard.steamcraft.item.firearm.enhancement.ItemEnhancementAirStrike;
import flaxbeard.steamcraft.misc.ItemStackUtility;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.List;

public class ItemRocketLauncher extends Item implements IEngineerable, IRenderItem {
    public float explosionSize;
    public int reloadTime;
    public int shellCount;
    public float accuracy;
    public Object repairMaterial;
    private int timeBetweenFire;

    public ItemRocketLauncher(float sizeExplosion, int timeReload, int fireTime, float rocketAccuracy, int rocketCount) {
        this(sizeExplosion, timeReload, fireTime, rocketAccuracy, rocketCount, null);
    }

    public ItemRocketLauncher(float sizeExplosion, int timeReload, int fireTime, float rocketAccuracy, int rocketCount, Object repair) {
        maxStackSize = 1;
        setMaxDamage(384);
        explosionSize = sizeExplosion;
        reloadTime = timeReload;
        timeBetweenFire = fireTime;
        accuracy = rocketAccuracy;
        shellCount = rocketCount;
        repairMaterial = repair;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean advanced) {
        if (UtilEnhancements.hasEnhancement(stack)) {
            list.add(UtilEnhancements.getEnhancementDisplayText(stack));
        }
        super.addInformation(stack, player, list, advanced);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, world, entity, itemSlot, isSelected);
        if (stack.hasTagCompound()) {
            if (stack.getTagCompound().hasKey("fireDelay")) {
                int delay = stack.getTagCompound().getInteger("fireDelay");
                if (delay > 0) {
                    delay--;
                }
                stack.getTagCompound().setInteger("fireDelay", delay);
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
    public int renderPasses(ItemStack stack) {
        return 1;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int ticksLeft) {
        NBTTagCompound nbt = stack.getTagCompound();

        if (nbt.getBoolean("done")) {
            nbt.setInteger("loaded", nbt.getInteger("numloaded"));
            nbt.setBoolean("done", false);
        }
    }

    private int getEnhancementShells(ItemStack stack) {
        if (UtilEnhancements.hasEnhancement(stack)) {
            IEnhancement enhancement = UtilEnhancements.getEnhancementFromItem(stack);
            if (enhancement instanceof IEnhancementRocketLauncher) {
                IEnhancementRocketLauncher enhancementRocketLauncher = (IEnhancementRocketLauncher) enhancement;
                return enhancementRocketLauncher.getClipSizeChange(this);
            }
        }
        return 0;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity) {
        if (!(entity instanceof EntityPlayer)) {
            return stack;
        }
        EntityPlayer player = (EntityPlayer) entity;
        NBTTagCompound nbt = stack.getTagCompound();
        boolean infiniteShots = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
        int enhancementShells = getEnhancementShells(stack);
        int selectedRocketType = 0;
        if (nbt.hasKey("rocketType")) {
            selectedRocketType = nbt.getInteger("rocketType");
        }

        Item rocketType = (Item) SteamcraftRegistry.rockets.get(selectedRocketType);
        if (!nbt.getBoolean("done") && (infiniteShots || ItemStackUtility.inventoryHasItem(player.inventory, rocketType))) {
            nbt.setInteger("numloaded", 1);
            int totalShells = shellCount + enhancementShells;
            if (infiniteShots) {
                nbt.setInteger("numloaded", totalShells);
            } else {
                ItemStackUtility.consumePlayerInventoryItem(player.inventory, rocketType);
                if (totalShells > 1) {
                    for (int i = 1; i < totalShells; i++) {
                        if (ItemStackUtility.inventoryHasItem(player.inventory, rocketType)) {
                            ItemStackUtility.consumePlayerInventoryItem(player.inventory, rocketType);
                            nbt.setInteger("numloaded", nbt.getInteger("numloaded") + 1);
                        }
                    }
                }
            }

            nbt.setBoolean("done", true);
            float vol = UtilEnhancements.getEnhancementFromItem(stack) != null && "Silencer".equals(UtilEnhancements.getEnhancementFromItem(stack).getID()) ? 0.4F : 1F;
            float pitch = world.rand.nextFloat() * 0.1F + 0.9F;
            world.playSound(player, player.getPosition(), SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.PLAYERS, vol, pitch);
        }

        return stack;
    }

    @Override
    public boolean isFull3D() {
        return true;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        ItemFirearm.initializeNBT(stack);

        NBTTagCompound nbt = stack.getTagCompound();

        int enhancementReload = 0;
        if (UtilEnhancements.hasEnhancement(stack)) {
            if (UtilEnhancements.getEnhancementFromItem(stack) instanceof IEnhancementRocketLauncher) {
                enhancementReload = ((IEnhancementRocketLauncher) UtilEnhancements.getEnhancementFromItem(stack)).getReloadChange(this);
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
        ItemFirearm.initializeNBT(stack);

        NBTTagCompound nbt = stack.getTagCompound();

        if (nbt.getInteger("loaded") > 0) {
            return EnumAction.BOW;
        } else {
            return EnumAction.BLOCK;

        }
    }

    public static void initializeNBTWithDone(ItemStack self) {
        if (!self.hasTagCompound()) {
            self.setTagCompound(new NBTTagCompound());
            NBTTagCompound nbt = self.getTagCompound();
            nbt.setInteger("loaded", 0);
            nbt.setBoolean("done", false);
            nbt.setInteger("numloaded", 0);
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack self, World world, EntityPlayer player, EnumHand hand) {
        NBTTagCompound nbt = self.getTagCompound();
        boolean crouched = player.isSneaking();

        if (!crouched) {
            initializeNBTWithDone(self);
            nbt = self.getTagCompound();
            if (nbt.getInteger("loaded") > 0 || player.capabilities.isCreativeMode) {
                if (!self.getTagCompound().hasKey("fireDelay") || self.getTagCompound().getInteger("fireDelay") == 0) {
                    float enhancementAccuracy = 0.0F;
                    float enhancementExplosionSize = 0.0F;
                    int enhancementDelay = 0;

                    if (UtilEnhancements.hasEnhancement(self)) {
                        IEnhancement enhancement = UtilEnhancements.getEnhancementFromItem(self);
                        if (enhancement instanceof IEnhancementRocketLauncher) {
                            IEnhancementRocketLauncher enhancementRocketLauncher = (IEnhancementRocketLauncher) UtilEnhancements.getEnhancementFromItem(self);
                            enhancementAccuracy = enhancementRocketLauncher.getAccuracyChange(this);
                            enhancementExplosionSize = enhancementRocketLauncher.getExplosionChange(this);
                            enhancementDelay = enhancementRocketLauncher.getFireDelayChange(self);
                        }
                    }

                    EntityRocket rocket = new EntityRocket(world, player, ((1.0F + accuracy + enhancementAccuracy) - 1F), explosionSize + enhancementExplosionSize);

                    int selectedRocketType = 0;
                    if (self.hasTagCompound() && self.getTagCompound().hasKey("rocketType")) {
                        selectedRocketType = self.getTagCompound().getInteger("rocketType");
                    }
                    IRocket irocket = SteamcraftRegistry.rockets.get(selectedRocketType);
                    rocket = irocket.changeBullet(rocket);

                    if (UtilEnhancements.hasEnhancement(self)) {
                        IEnhancement enhancement = UtilEnhancements.getEnhancementFromItem(self);
                        if (enhancement instanceof IEnhancementRocketLauncher) {
                            IEnhancementRocketLauncher enhancementRocketLauncher = (IEnhancementRocketLauncher) enhancement;
                            rocket = enhancementRocketLauncher.changeBullet(rocket);
                        }
                    }

                    self.damageItem(1, player);
                    float vol = (1.0F * (2F / 5F)) * (UtilEnhancements.getEnhancementFromItem(self) != null && "Silencer".equals(UtilEnhancements.getEnhancementFromItem(self).getID()) ? 0.4F : 1.0F);
                    float pitch = 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 1F * 0.5F;
                    world.playSound(player, player.getPosition(), Steamcraft.SOUND_ROCKET, SoundCategory.PLAYERS, vol, pitch);

                    if (!world.isRemote) {
                        world.spawnEntityInWorld(rocket);
                    }
                    ArrowLooseEvent event = new ArrowLooseEvent(player, self, world, 1, ItemStackUtility.inventoryHasItem(player.inventory, (Item) irocket));
                    MinecraftForge.EVENT_BUS.post(event);

                    nbt.setInteger("loaded", nbt.getInteger("loaded") - 1);
                    if (player.capabilities.isFlying && !(player.onGround &&
                      UtilEnhancements.hasEnhancement(self) &&
                      UtilEnhancements.getEnhancementFromItem(self) instanceof ItemEnhancementAirStrike)) {
                        self.getTagCompound().setInteger("fireDelay", timeBetweenFire + enhancementDelay);
                    }
                }
                // par3EntityPlayer.inventory.setInventorySlotContents(par3EntityPlayer.inventory.currentItem, new ItemStack(BoilerMod.musketEmpty));
            }
            if (player.capabilities.isCreativeMode) {

                NBTTagCompound nbtt = self.getTagCompound();
                int enhancementShells = getEnhancementShells(self);
                nbtt.setInteger("loaded", 1);
                nbtt.setInteger("numloaded", shellCount + enhancementShells);
                player.setActiveHand(hand);
            }
        } else {
            initializeNBTWithDone(self);
            int selectedRocketType = 0;
            if (self.hasTagCompound() && self.getTagCompound().hasKey("rocketType")) {
                selectedRocketType = self.getTagCompound().getInteger("rocketType");
            }
            int prevRocketType = selectedRocketType;
            selectedRocketType = (selectedRocketType + 1) % SteamcraftRegistry.rockets.size();
            nbt.setInteger("rocketType", selectedRocketType);
            if (selectedRocketType != prevRocketType && self.getTagCompound().getInteger("loaded") > 0) {
                ItemStack stack = new ItemStack(((Item) SteamcraftRegistry.rockets.get(prevRocketType)), nbt.getInteger("loaded"), 0);
                if (!player.worldObj.isRemote) {
                    EntityItem entityItem = new EntityItem(player.worldObj, player.posX, player.posY, player.posZ, stack);
                    player.worldObj.spawnEntityInWorld(entityItem);
                }
                nbt.setInteger("loaded", 0);
            }

        }
        return ActionResult.newResult(EnumActionResult.SUCCESS, self);

    }

    // See ItemFirearm#engineerCoordinates().
    @SuppressWarnings("unchecked")
    @Override
    public MutablePair<Integer, Integer>[] engineerCoordinates() {
        return new MutablePair[]{MutablePair.of(53, 29)};
    }

    @Override
    public ItemStack getStackInSlot(ItemStack me, int var1) {
        if (UtilEnhancements.hasEnhancement(me)) {
            Item item = (Item) UtilEnhancements.getEnhancementFromItem(me);
            return item == null ? null : new ItemStack(item);
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(ItemStack me, int slot, ItemStack stack) {
        ItemFirearm.initializeEnhancementsNBT(me, stack);
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
            return item == null ? null : new ItemStack(item);
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
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        if (repairMaterial != null) {
            if (repairMaterial instanceof ItemStack) {
                return par2ItemStack.isItemEqual((ItemStack) repairMaterial) || super.getIsRepairable(par1ItemStack, par2ItemStack);
            }
            if (repairMaterial instanceof String) {
                return UtilMisc.doesMatch(par2ItemStack, (String) repairMaterial) || super.getIsRepairable(par1ItemStack, par2ItemStack);
            }
        }
        return super.getIsRepairable(par1ItemStack, par2ItemStack);
    }

    @Override
    public void drawBackground(GuiEngineeringTable guiEngineeringTable, int i, int j, int k) {
        guiEngineeringTable.mc.getTextureManager().bindTexture(ItemExosuitArmor.LARGE_ICONS);
        guiEngineeringTable.drawTexturedModalRect(j + 26, k + 3, 192, 64, 64, 64);
    }

}