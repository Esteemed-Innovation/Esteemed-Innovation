package flaxbeard.steamcraft.item.firearm;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.api.IEngineerable;
import flaxbeard.steamcraft.api.util.UtilMisc;
import flaxbeard.steamcraft.api.enhancement.IEnhancement;
import flaxbeard.steamcraft.api.enhancement.IEnhancementFirearm;
import flaxbeard.steamcraft.api.enhancement.UtilEnhancements;
import flaxbeard.steamcraft.entity.EntityMusketBall;
import flaxbeard.steamcraft.gui.GuiEngineeringTable;
import flaxbeard.steamcraft.handler.SteamcraftEventHandler;
import flaxbeard.steamcraft.item.ItemExosuitArmor;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.List;

public class ItemFirearm extends Item implements IEngineerable {
    public float damage;
    public int reloadTime;
    public int shellCount;
    public float accuracy;
    public float knockback;
    public boolean shotgun;
    public Object repairMaterial = null;
    /**
     * Used only for the Reloading Holsters, hence why it is private. Don't make it public, as that
     * could confuse other developers into thinking they should use it for things.
     */
    private int ticksSinceReload;
    private boolean wasSprinting = false;

    public ItemFirearm(float par2, int par3, float par4, float par5, boolean par6, int par7) {
        this.maxStackSize = 1;
        this.setMaxDamage(384);
        this.damage = par2;
        this.reloadTime = par3;
        this.accuracy = par4;
        this.knockback = par5;
        this.shotgun = par6;
        this.shellCount = par7;
    }

    public ItemFirearm(float par2, int par3, float par4, float par5, boolean par6, int par7, Object repair) {
        this.maxStackSize = 1;
        this.setMaxDamage(384);
        this.damage = par2;
        this.reloadTime = par3;
        this.accuracy = par4;
        this.knockback = par5;
        this.shotgun = par6;
        this.shellCount = par7;
        this.repairMaterial = repair;
    }

    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        if (UtilEnhancements.hasEnhancement(stack)) {
            list.add(UtilEnhancements.getEnhancementDisplayText(stack));
        }
        super.addInformation(stack, player, list, par4);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean isCurrentItem) {
        super.onUpdate(stack, world, entity, par4, isCurrentItem);
        if (entity instanceof EntityPlayerSP) {
            EntityPlayerSP player = (EntityPlayerSP) entity;
            ItemStack usingItem = player.getItemInUse();
            if (usingItem != null && usingItem.getItem() == this && UtilEnhancements.hasEnhancement(usingItem) && UtilEnhancements.getEnhancementFromItem(usingItem).getID() == "Speedy") {
                player.movementInput.moveForward *= 5.0F;
                player.movementInput.moveStrafe *= 5.0F;
            } else {
                wasSprinting = player.isSprinting();
            }
        }

        // Reloading Holster code.
        if (!isCurrentItem && entity instanceof EntityPlayer && stack.hasTagCompound()) {
            this.ticksSinceReload += 1;
            NBTTagCompound nbt = stack.getTagCompound();
            EntityPlayer player = (EntityPlayer) entity;
            ItemStack legs = player.getEquipmentInSlot(2);
            if (legs != null && nbt.getInteger("loaded") < 1 &&
              this.ticksSinceReload >= (this.reloadTime + 10)) {
                Item legsItem = legs.getItem();
                if (legsItem instanceof ItemExosuitArmor) {
                    ItemExosuitArmor legsArmor = (ItemExosuitArmor) legsItem;
                    if (legsArmor.hasUpgrade(legs, SteamcraftItems.reloadingHolsters) &&
                      SteamcraftEventHandler.hasPower(player, Config.reloadingConsumption) &&
                      player.inventory.hasItem(SteamcraftItems.musketCartridge)) {
                        this.onEaten(stack, world, player);
                        this.onItemRightClick(stack, world, player);
                        SteamcraftEventHandler.drainSteam(player.getEquipmentInSlot(3),
                          Config.reloadingConsumption);
                        this.ticksSinceReload = 0;
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
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir) {
        super.registerIcons(ir);
        UtilEnhancements.registerEnhancementsForItem(ir, this);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        if (UtilEnhancements.hasEnhancement(stack)) {
            return UtilEnhancements.getIconFromEnhancement(stack);
        } else {
            return super.getIcon(stack, renderPass, player, usingItem, useRemaining);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconIndex(ItemStack stack) {
        if (UtilEnhancements.hasEnhancement(stack)) {
            return UtilEnhancements.getIconFromEnhancement(stack);
        } else {
            return super.getIconIndex(stack);
        }
    }

    /**
     * called when the player releases the use item button. Args: itemstack, world, entityplayer, itemInUseCount
     */
    @Override
    public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4) {
        NBTTagCompound nbt = par1ItemStack.getTagCompound();
        boolean crouched = par3EntityPlayer.isSneaking();

        if (nbt.getInteger("loaded") > 0) {
            float enhancementAccuracy = 0.0F;
            float enhancementDamage = 0;
            float enhancementKnockback = 0;
            float crouchingBonus = crouched ? 0.10F : 0;

            if (UtilEnhancements.hasEnhancement(par1ItemStack)) {
                if (UtilEnhancements.getEnhancementFromItem(par1ItemStack) instanceof IEnhancementFirearm) {
                    enhancementAccuracy = ((IEnhancementFirearm) UtilEnhancements.getEnhancementFromItem(par1ItemStack)).getAccuracyChange(this);
                    enhancementDamage = ((IEnhancementFirearm) UtilEnhancements.getEnhancementFromItem(par1ItemStack)).getDamageChange(this);
                    enhancementKnockback = ((IEnhancementFirearm) UtilEnhancements.getEnhancementFromItem(par1ItemStack)).getKnockbackChange(this);
                }
            }
            int var6 = this.getMaxItemUseDuration(par1ItemStack) - par4;
            ArrowLooseEvent event = new ArrowLooseEvent(par3EntityPlayer, par1ItemStack, var6);
            MinecraftForge.EVENT_BUS.post(event);

            if (event.isCanceled()) {
                return;
            }

            var6 = event.charge;
            float var7 = var6 / 20.0F;
            var7 = (var7 * var7 + var7 * 2.0F) / 3.0F;

            if (var7 < 0.1D) {
                return;
            }

            if (var7 > 1.0F) {
                var7 = 1.0F;
            }

            EntityMusketBall var8 = new EntityMusketBall(par2World, par3EntityPlayer, 2.0F, ((1.0F + accuracy + enhancementAccuracy - crouchingBonus) - var7), (damage + enhancementDamage), true);

            if (UtilEnhancements.hasEnhancement(par1ItemStack)) {
                if (UtilEnhancements.getEnhancementFromItem(par1ItemStack) instanceof IEnhancementFirearm) {
                    var8 = ((IEnhancementFirearm) UtilEnhancements.getEnhancementFromItem(par1ItemStack)).changeBullet(var8);
                }
            }

            par1ItemStack.damageItem(1, par3EntityPlayer);
            par2World.playSoundAtEntity(par3EntityPlayer, "random.explode", ((knockback + enhancementKnockback) * (2F / 5F)) * (UtilEnhancements.getEnhancementFromItem(par1ItemStack) != null && UtilEnhancements.getEnhancementFromItem(par1ItemStack).getID() == "Silencer" ? 0.4F : 1.0F), 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + var7 * 0.5F);
            for (int i = 1; i < 16; i++) {
                par2World.spawnParticle("smoke", par3EntityPlayer.posX + itemRand.nextFloat() - 0.5F, par3EntityPlayer.posY + (itemRand.nextFloat() / 2) - 0.25F, par3EntityPlayer.posZ + itemRand.nextFloat() - 0.5F, 0.0D, 0.01D, 0.0D);
            }

            if (!par2World.isRemote) {
                if (shotgun) {
                    for (int i = 1; i < 21; i++) {
                        EntityMusketBall var12 = new EntityMusketBall(par2World, par3EntityPlayer, 2.0F, (1.0F + accuracy + enhancementAccuracy - crouchingBonus) - var7, (damage + enhancementDamage), false);
                        if (UtilEnhancements.hasEnhancement(par1ItemStack)) {
                            if (UtilEnhancements.getEnhancementFromItem(par1ItemStack) instanceof IEnhancementFirearm) {
                                var12 = ((IEnhancementFirearm) UtilEnhancements.getEnhancementFromItem(par1ItemStack)).changeBullet(var12);
                            }
                        }
                        par2World.spawnEntityInWorld(var12);
                    }
                } else {
                    par2World.spawnEntityInWorld(var8);
                }

                //par3EntityPlayer.rotationPitch = par3EntityPlayer.rotationPitch + 100.0F;
                //Minecraft minecraft = Minecraft.getMinecraft();
                //minecraft.entityRenderer.
            }

            nbt.setInteger("loaded", nbt.getInteger("loaded") - 1);

            if (par2World.isRemote && !par3EntityPlayer.capabilities.isCreativeMode) {
                float thiskb = this.knockback + enhancementKnockback;
                boolean crouching = par3EntityPlayer.isSneaking();

                if (crouching) {
                    thiskb = thiskb / 2;
                }

                par3EntityPlayer.rotationPitch = par3EntityPlayer.rotationPitch - (thiskb * 3F);
                par3EntityPlayer.motionZ = -MathHelper.cos((par3EntityPlayer.rotationYaw) * (float) Math.PI / 180.0F) * (thiskb * (4F / 50F));
                par3EntityPlayer.motionX = MathHelper.sin((par3EntityPlayer.rotationYaw) * (float) Math.PI / 180.0F) * (thiskb * (4F / 50F));
            }

            // par3EntityPlayer.inventory.setInventorySlotContents(par3EntityPlayer.inventory.currentItem, new ItemStack(BoilerMod.musketEmpty));
        } else {
            if (nbt.getBoolean("done")) {
                //done = false;
                //par3EntityPlayer.inventoryContainer.putStackInSlot(par3EntityPlayer.inventory.currentItem + 36, new ItemStack(BoilerMod.musket, 1));
                //par3EntityPlayer.inventoryContainer.detectAndSendChanges();
                nbt.setInteger("loaded", nbt.getInteger("numloaded"));
                nbt.setBoolean("done", false);
            }
        }
    }

    @Override
    public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        NBTTagCompound nbt = par1ItemStack.getTagCompound();
        boolean var5 = par3EntityPlayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, par1ItemStack) > 0;
        int enhancementShells = 0;
        if (UtilEnhancements.hasEnhancement(par1ItemStack)) {
            if (UtilEnhancements.getEnhancementFromItem(par1ItemStack) instanceof IEnhancementFirearm) {
                enhancementShells = ((IEnhancementFirearm) UtilEnhancements.getEnhancementFromItem(par1ItemStack)).getClipSizeChange(this);
            }
        }
        if (var5 || par3EntityPlayer.inventory.hasItem(SteamcraftItems.musketCartridge)) {
            if (nbt.getBoolean("done") == false) {
                nbt.setInteger("numloaded", 1);
                if (var5) {
                    nbt.setInteger("numloaded", this.shellCount + enhancementShells);
                } else {
                    par3EntityPlayer.inventory.consumeInventoryItem(SteamcraftItems.musketCartridge);
                    if ((this.shellCount + enhancementShells) > 1) {
                        for (int i = 1; i < (this.shellCount + enhancementShells); i++) {
                            if (par3EntityPlayer.inventory.hasItem(SteamcraftItems.musketCartridge)) {
                                par3EntityPlayer.inventory.consumeInventoryItem(SteamcraftItems.musketCartridge);
                                nbt.setInteger("numloaded", nbt.getInteger("numloaded") + 1);
                            }
                        }
                    }
                }

                nbt.setBoolean("done", true);
                par2World.playSoundAtEntity(par3EntityPlayer, "random.click", (UtilEnhancements.getEnhancementFromItem(par1ItemStack) != null && UtilEnhancements.getEnhancementFromItem(par1ItemStack).getID() == "Silencer" ? 0.4F : 1.0F), par2World.rand.nextFloat() * 0.1F + 0.9F);
            }
        }

        return par1ItemStack;
    }

    @Override
    public boolean isFull3D() {
        return true;
    }

    /**
     * How long it takes to use or consume an item
     */
    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack) {
        if (!par1ItemStack.hasTagCompound()) {
            par1ItemStack.setTagCompound(new NBTTagCompound());
            NBTTagCompound nbt = par1ItemStack.getTagCompound();
            nbt.setInteger("loaded", 0);
            nbt.setInteger("numloaded", 0);
            //nbt.setBoolean("done", false);
        }

        NBTTagCompound nbt = par1ItemStack.getTagCompound();

        int enhancementReload = 0;
        if (UtilEnhancements.hasEnhancement(par1ItemStack)) {
            if (UtilEnhancements.getEnhancementFromItem(par1ItemStack) instanceof IEnhancementFirearm) {
                enhancementReload = ((IEnhancementFirearm) UtilEnhancements.getEnhancementFromItem(par1ItemStack)).getReloadChange(this);
            }
        }

        if ((nbt.getInteger("loaded") > 0) || (nbt.getBoolean("done"))) {
            return 72000;
        } else {
            return reloadTime + enhancementReload;
        }
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack) {
        if (!par1ItemStack.hasTagCompound()) {
            par1ItemStack.setTagCompound(new NBTTagCompound());
            NBTTagCompound nbt = par1ItemStack.getTagCompound();
            nbt.setInteger("loaded", 0);
            nbt.setInteger("numloaded", 0);
            //nbt.setBoolean("done", false);
        }

        NBTTagCompound nbt = par1ItemStack.getTagCompound();

        if (nbt.getInteger("loaded") > 0) {
            return EnumAction.bow;
        } else {
            return EnumAction.block;

        }
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        if (!par1ItemStack.hasTagCompound()) {
            par1ItemStack.setTagCompound(new NBTTagCompound());
            NBTTagCompound nbt = par1ItemStack.getTagCompound();
            nbt.setInteger("loaded", 0);
            nbt.setBoolean("done", false);
            nbt.setInteger("numloaded", 0);
        }

        NBTTagCompound nbtt = par1ItemStack.getTagCompound();
        if (par3EntityPlayer.capabilities.isCreativeMode) {
            int enhancementShells = 0;
            if (UtilEnhancements.hasEnhancement(par1ItemStack)) {
                if (UtilEnhancements.getEnhancementFromItem(par1ItemStack) instanceof IEnhancementFirearm) {
                    enhancementShells = ((IEnhancementFirearm) UtilEnhancements.getEnhancementFromItem(par1ItemStack)).getClipSizeChange(this);
                }
            }
            nbtt.setInteger("loaded", 1);
            nbtt.setInteger("numloaded", this.shellCount + enhancementShells);
        }
        par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
        return par1ItemStack;
    }

    @Override
    public MutablePair<Integer, Integer>[] engineerCoordinates() {
        return new MutablePair[]{MutablePair.of(53, 29)};
    }

    @Override
    public ItemStack getStackInSlot(ItemStack me, int var1) {
        if (UtilEnhancements.hasEnhancement(me)) {
            Item item = (Item) UtilEnhancements.getEnhancementFromItem(me);
            return new ItemStack(item);
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(ItemStack me, int var1, ItemStack stack) {
        if (!me.hasTagCompound()) {
            me.setTagCompound(new NBTTagCompound());
        }
        if (stack != null) {
            IEnhancement enhancement = (IEnhancement) stack.getItem();
            NBTTagCompound enhancements = new NBTTagCompound();
            enhancements.setString("id", enhancement.getID());
            me.stackTagCompound.setTag("enhancements", enhancements);
        }
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
        guiEngineeringTable.mc.getTextureManager().bindTexture(GuiEngineeringTable.furnaceGuiTextures);
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
                return par2ItemStack.isItemEqual((ItemStack) repairMaterial) ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
            }
            if (repairMaterial instanceof String) {
                return UtilMisc.doesMatch(par2ItemStack, (String) repairMaterial) ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
            }
        }
        return super.getIsRepairable(par1ItemStack, par2ItemStack);
    }

    @Override
    public void drawBackground(GuiEngineeringTable guiEngineeringTable, int i, int j, int k) {
        guiEngineeringTable.mc.getTextureManager().bindTexture(ItemExosuitArmor.largeIcons);
        if (this == SteamcraftItems.musket) {
            guiEngineeringTable.drawTexturedModalRect(j + 26, k + 3, 0, 64, 64, 64);
        } else if (this == SteamcraftItems.blunderbuss) {
            guiEngineeringTable.drawTexturedModalRect(j + 26, k + 3, 64, 64, 64, 64);
        } else if (this == SteamcraftItems.pistol) {
            guiEngineeringTable.drawTexturedModalRect(j + 26, k + 3, 128, 64, 64, 64);
        }

    }

}