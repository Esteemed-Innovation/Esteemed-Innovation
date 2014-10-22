package flaxbeard.steamcraft.common.item.firearm;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.api.IEngineerable;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.UtilMisc;
import flaxbeard.steamcraft.api.enhancement.IEnhancement;
import flaxbeard.steamcraft.api.enhancement.IEnhancementRocketLauncher;
import flaxbeard.steamcraft.api.enhancement.UtilEnhancements;
import flaxbeard.steamcraft.common.entity.EntityRocket;
import flaxbeard.steamcraft.client.gui.GuiEngineeringTable;
import flaxbeard.steamcraft.common.handler.SteamcraftEventHandler;
import flaxbeard.steamcraft.common.item.exosuit.ItemExosuitArmor;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.List;

public class ItemRocketLauncher extends Item implements IEngineerable {
    public float explosionSize;
    public int reloadTime;
    public int shellCount;
    public float accuracy;
    public Object repairMaterial = null;
    private int timeBetweenFire;
    private boolean wasSprinting = false;

    public ItemRocketLauncher(float sizeExplosion, int timeReload, int fireTime, float rocketAccuracy, int rocketCount) {
        this.maxStackSize = 1;
        this.setMaxDamage(384);
        this.explosionSize = sizeExplosion;
        this.reloadTime = timeReload;
        this.timeBetweenFire = fireTime;
        this.accuracy = rocketAccuracy;
        this.shellCount = rocketCount;
    }

    public ItemRocketLauncher(float sizeExplosion, int timeReload, int fireTime, float rocketAccuracy, int rocketCount, Object repair) {
        this.maxStackSize = 1;
        this.setMaxDamage(384);
        this.explosionSize = sizeExplosion;
        this.reloadTime = timeReload;
        this.timeBetweenFire = fireTime;
        this.accuracy = rocketAccuracy;
        this.shellCount = rocketCount;
        this.repairMaterial = repair;
    }

    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        if (UtilEnhancements.hasEnhancement(stack)) {
            list.add(UtilEnhancements.getEnhancementDisplayText(stack));
        }
        super.addInformation(stack, player, list, par4);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5) {
        super.onUpdate(stack, world, entity, par4, par5);
        if (stack.hasTagCompound()) {
            if (stack.stackTagCompound.hasKey("fireDelay")) {
                int delay = stack.stackTagCompound.getInteger("fireDelay");
                if (delay > 0) {
                    delay--;
                }
                stack.stackTagCompound.setInteger("fireDelay", delay);
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


        if (nbt.getBoolean("done")) {
            //done = false;
            //par3EntityPlayer.inventoryContainer.putStackInSlot(par3EntityPlayer.inventory.currentItem + 36, new ItemStack(BoilerMod.musket, 1));
            //par3EntityPlayer.inventoryContainer.detectAndSendChanges();
            nbt.setInteger("loaded", nbt.getInteger("numloaded"));
            nbt.setBoolean("done", false);
        }

    }

    @Override
    public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        NBTTagCompound nbt = par1ItemStack.getTagCompound();
        boolean var5 = par3EntityPlayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, par1ItemStack) > 0;
        int enhancementShells = 0;
        if (UtilEnhancements.hasEnhancement(par1ItemStack)) {
            if (UtilEnhancements.getEnhancementFromItem(par1ItemStack) instanceof IEnhancementRocketLauncher) {
                enhancementShells = ((IEnhancementRocketLauncher) UtilEnhancements.getEnhancementFromItem(par1ItemStack)).getClipSizeChange(this);
            }
        }
        int selectedRocketType = 0;
        if (nbt.hasKey("rocketType")) {
            selectedRocketType = nbt.getInteger("rocketType");
        }

        if (var5 || par3EntityPlayer.inventory.hasItem((Item) SteamcraftRegistry.rockets.get(selectedRocketType))) {
            if (nbt.getBoolean("done") == false) {
                nbt.setInteger("numloaded", 1);
                if (var5) {
                    nbt.setInteger("numloaded", this.shellCount + enhancementShells);
                } else {
                    par3EntityPlayer.inventory.consumeInventoryItem(((Item) SteamcraftRegistry.rockets.get(selectedRocketType)));
                    if ((this.shellCount + enhancementShells) > 1) {
                        for (int i = 1; i < (this.shellCount + enhancementShells); i++) {
                            if (par3EntityPlayer.inventory.hasItem((Item) SteamcraftRegistry.rockets.get(selectedRocketType))) {
                                par3EntityPlayer.inventory.consumeInventoryItem(((Item) SteamcraftRegistry.rockets.get(selectedRocketType)));
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
            if (UtilEnhancements.getEnhancementFromItem(par1ItemStack) instanceof IEnhancementRocketLauncher) {
                enhancementReload = ((IEnhancementRocketLauncher) UtilEnhancements.getEnhancementFromItem(par1ItemStack)).getReloadChange(this);
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
        System.out.println("1 - " + par3EntityPlayer.worldObj.isRemote);
        NBTTagCompound nbt = par1ItemStack.getTagCompound();
        boolean crouched = par3EntityPlayer.isSneaking();

        if (!crouched) {
            if (!par1ItemStack.hasTagCompound()) {
                par1ItemStack.setTagCompound(new NBTTagCompound());
                nbt = par1ItemStack.getTagCompound();
                nbt.setInteger("loaded", 0);
                nbt.setBoolean("done", false);
                nbt.setInteger("numloaded", 0);
            }
            System.out.println("2 - " + par3EntityPlayer.worldObj.isRemote);
            if (nbt.getInteger("loaded") > 0 || par3EntityPlayer.capabilities.isCreativeMode) {
                System.out.println("3 - " + par3EntityPlayer.worldObj.isRemote);
                if (!par1ItemStack.stackTagCompound.hasKey("fireDelay") || par1ItemStack.stackTagCompound.getInteger("fireDelay") == 0) {
                    System.out.println("4 - " + par3EntityPlayer.worldObj.isRemote);
                    float enhancementAccuracy = 0.0F;
                    float enhancementExplosionSize = 0.0F;
                    int enhancementDelay = 0;

                    if (UtilEnhancements.hasEnhancement(par1ItemStack)) {
                        if (UtilEnhancements.getEnhancementFromItem(par1ItemStack) instanceof IEnhancementRocketLauncher) {
                            enhancementAccuracy = ((IEnhancementRocketLauncher) UtilEnhancements.getEnhancementFromItem(par1ItemStack)).getAccuracyChange(this);
                            enhancementExplosionSize = ((IEnhancementRocketLauncher) UtilEnhancements.getEnhancementFromItem(par1ItemStack)).getExplosionChange(this);
                            enhancementDelay = ((IEnhancementRocketLauncher) UtilEnhancements.getEnhancementFromItem(par1ItemStack)).getFireDelayChange(par1ItemStack);
                        }
                    }

                    float var7 = 1.0F;

                    if (var7 < 0.1D) {
                        return par1ItemStack;
                    }

                    if (var7 > 1.0F) {
                        var7 = 1.0F;
                    }

                    EntityRocket var8 = new EntityRocket(par2World, par3EntityPlayer, ((1.0F + accuracy + enhancementAccuracy) - var7), this.explosionSize + enhancementExplosionSize);

                    int selectedRocketType = 0;
                    if (par1ItemStack.hasTagCompound()) {
                        if (par1ItemStack.stackTagCompound.hasKey("rocketType")) {
                            selectedRocketType = par1ItemStack.stackTagCompound.getInteger("rocketType");
                        }
                    }
                    var8 = SteamcraftRegistry.rockets.get(selectedRocketType).changeBullet(var8);

                    if (UtilEnhancements.hasEnhancement(par1ItemStack)) {
                        if (UtilEnhancements.getEnhancementFromItem(par1ItemStack) instanceof IEnhancementRocketLauncher) {
                            var8 = ((IEnhancementRocketLauncher) UtilEnhancements.getEnhancementFromItem(par1ItemStack)).changeBullet(var8);
                        }
                    }

                    par1ItemStack.damageItem(1, par3EntityPlayer);
                    par2World.playSoundAtEntity(par3EntityPlayer, "steamcraft:rocket", (1.0F * (2F / 5F)) * (UtilEnhancements.getEnhancementFromItem(par1ItemStack) != null && UtilEnhancements.getEnhancementFromItem(par1ItemStack).getID() == "Silencer" ? 0.4F : 1.0F), 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + var7 * 0.5F);


                    if (!par2World.isRemote) {
                        par2World.spawnEntityInWorld(var8);
                    }

                    nbt.setInteger("loaded", nbt.getInteger("loaded") - 1);

                    if (par2World.isRemote && !par3EntityPlayer.capabilities.isCreativeMode) {
                        boolean crouching = par3EntityPlayer.isSneaking();

                        //                 if (crouching)
                        //                 {
                        //                     thiskb = thiskb / 2;
                        //                 }
                        //
                        //                 par3EntityPlayer.rotationPitch = par3EntityPlayer.rotationPitch - (thiskb * 3F);
                        //                 par3EntityPlayer.motionZ = -MathHelper.cos((par3EntityPlayer.rotationYaw) * (float)Math.PI / 180.0F) * (thiskb * (4F / 50F));
                        //                 par3EntityPlayer.motionX = MathHelper.sin((par3EntityPlayer.rotationYaw) * (float)Math.PI / 180.0F) * (thiskb * (4F / 50F));
                    }
                    if (!(SteamcraftEventHandler.isJumping(par3EntityPlayer) && !par3EntityPlayer.capabilities.isFlying && UtilEnhancements.hasEnhancement(par1ItemStack) && UtilEnhancements.getEnhancementFromItem(par1ItemStack) instanceof ItemEnhancementAirStrike)) {
                        par1ItemStack.stackTagCompound.setInteger("fireDelay", this.timeBetweenFire + enhancementDelay);
                    }
                }
                // par3EntityPlayer.inventory.setInventorySlotContents(par3EntityPlayer.inventory.currentItem, new ItemStack(BoilerMod.musketEmpty));
            } else {

                NBTTagCompound nbtt = par1ItemStack.getTagCompound();
                if (par3EntityPlayer.capabilities.isCreativeMode) {
                    int enhancementShells = 0;
                    if (UtilEnhancements.hasEnhancement(par1ItemStack)) {
                        if (UtilEnhancements.getEnhancementFromItem(par1ItemStack) instanceof IEnhancementRocketLauncher) {
                            enhancementShells = ((IEnhancementRocketLauncher) UtilEnhancements.getEnhancementFromItem(par1ItemStack)).getClipSizeChange(this);
                        }
                    }
                    nbtt.setInteger("loaded", 1);
                    nbtt.setInteger("numloaded", this.shellCount + enhancementShells);
                }
                par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
            }
        } else {
            if (!par1ItemStack.hasTagCompound()) {
                par1ItemStack.setTagCompound(new NBTTagCompound());
                nbt = par1ItemStack.getTagCompound();
                nbt.setInteger("loaded", 0);
                nbt.setBoolean("done", false);
                nbt.setInteger("numloaded", 0);
            }
            int selectedRocketType = 0;
            if (par1ItemStack.hasTagCompound()) {
                if (par1ItemStack.stackTagCompound.hasKey("rocketType")) {
                    selectedRocketType = par1ItemStack.stackTagCompound.getInteger("rocketType");
                }
            }
            int prevRocketType = selectedRocketType;
            selectedRocketType = (selectedRocketType + 1) % SteamcraftRegistry.rockets.size();
            nbt.setInteger("rocketType", selectedRocketType);
            if (selectedRocketType != prevRocketType && par1ItemStack.stackTagCompound.getInteger("loaded") > 0) {
                ItemStack stack = new ItemStack(((Item) SteamcraftRegistry.rockets.get(prevRocketType)), nbt.getInteger("loaded"), 0);
                if (!par3EntityPlayer.worldObj.isRemote) {
                    EntityItem entityItem = new EntityItem(par3EntityPlayer.worldObj, par3EntityPlayer.posX, par3EntityPlayer.posY, par3EntityPlayer.posZ, stack);
                    par3EntityPlayer.worldObj.spawnEntityInWorld(entityItem);
                }
                nbt.setInteger("loaded", 0);
            }

        }
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
    public void drawSlot(GuiContainer guiEngineeringTable, int slotnum, int i,
                         int j) {
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
    public void drawBackground(GuiEngineeringTable guiEngineeringTable, int i,
                               int j, int k) {
        guiEngineeringTable.mc.getTextureManager().bindTexture(ItemExosuitArmor.largeIcons);
        guiEngineeringTable.drawTexturedModalRect(j + 26, k + 3, 192, 64, 64, 64);
    }

}