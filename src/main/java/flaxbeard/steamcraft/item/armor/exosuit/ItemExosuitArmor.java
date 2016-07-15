package flaxbeard.steamcraft.item.armor.exosuit;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.api.IEngineerable;
import flaxbeard.steamcraft.api.ISteamChargable;
import flaxbeard.steamcraft.api.exosuit.*;
import flaxbeard.steamcraft.client.render.model.exosuit.ExosuitModelCache;
import flaxbeard.steamcraft.client.render.model.exosuit.ModelExosuit;
import flaxbeard.steamcraft.gui.GuiEngineeringTable;
import flaxbeard.steamcraft.handler.SteamcraftEventHandler;
import flaxbeard.steamcraft.init.misc.integration.CrossMod;
import flaxbeard.steamcraft.item.BlockTankItem;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemExosuitArmor extends ItemArmor implements ISpecialArmor, IEngineerable, ISteamChargable {
    public static final ResourceLocation largeIcons = new ResourceLocation("steamcraft:textures/gui/engineering2.png");

    public EntityEquipmentSlot slot;

    public ItemExosuitArmor(EntityEquipmentSlot slot, ArmorMaterial mat) {
        super(mat, 1, slot);
        this.slot = slot;
        setMaxDamage(0);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return false;
    }

    public String getString() {
        return this.iconString;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
        if (!(entityLiving instanceof EntityPlayer)) {
            return null;
        }

        ModelExosuit modelExosuit = ExosuitModelCache.INSTANCE.getModel((EntityPlayer) entityLiving, armorSlot);

        modelExosuit.bipedHead.showModel = armorSlot == 0;
        modelExosuit.bipedHeadwear.showModel = armorSlot == 0;
        modelExosuit.bipedBody.showModel = armorSlot == 1 || armorSlot == 2;
        modelExosuit.bipedRightArm.showModel = armorSlot == 1;
        modelExosuit.bipedLeftArm.showModel = armorSlot == 1;
        modelExosuit.bipedRightLeg.showModel = armorSlot == 2 || armorSlot == 3;
        modelExosuit.bipedLeftLeg.showModel = armorSlot == 2 || armorSlot == 3;

        return modelExosuit;
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int armorType) {
        ItemArmor armorStack = (ItemArmor) armor.getItem();
        if (armor.hasTagCompound()) {
            if (armor.stackTagCompound.hasKey("plate")) {
                ExosuitPlate plate = UtilPlates.getPlate(armor.stackTagCompound.getString("plate"));
                return new ArmorProperties(0, plate.getDamageReductionAmount(armorType, source) / 25.0D, ItemArmor.ArmorMaterial.IRON.getDurability(armorType));
            }
        }
        return new ArmorProperties(0, ItemArmor.ArmorMaterial.IRON.getDamageReductionAmount(armorType) / 25.0D, ItemArmor.ArmorMaterial.IRON.getDurability(armorType));
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        updateSteamNBT(stack);
        //return 0.9D;
        return 1.0D - (stack.stackTagCompound.getInteger("steamFill") / (double) stack.stackTagCompound.getInteger("maxFill"));
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        if (!stack.stackTagCompound.hasKey("maxFill")) {
            stack.stackTagCompound.setInteger("maxFill", 0);
        }
        return stack.stackTagCompound.getInteger("maxFill") > 0;
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int armorType) {
        ItemArmor armorStack = (ItemArmor) armor.getItem();
        if (armor.hasTagCompound()) {
            if (armor.stackTagCompound.hasKey("plate")) {
                ExosuitPlate plate = UtilPlates.getPlate(armor.stackTagCompound.getString("plate"));
                return plate.getDamageReductionAmount(armorType, DamageSource.generic);
            }
        }
        return ItemArmor.ArmorMaterial.CLOTH.getDamageReductionAmount(armorType);
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
        if (this.slot == 1) {
            SteamcraftEventHandler.drainSteam(stack, damage * 40);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public MutablePair<Integer, Integer>[] engineerCoordinates() {
        if (this.slot == 0) {
            return new MutablePair[]{MutablePair.of(1, 19), MutablePair.of(1, 1), MutablePair.of(39, 16), MutablePair.of(59, 36)};
        }
        if (this.slot == 2) {
            return new MutablePair[]{MutablePair.of(1, 19), MutablePair.of(1, 1), MutablePair.of(60, 12), MutablePair.of(37, 40)};
        }
        if (this.slot == 1) {
            return new MutablePair[]{MutablePair.of(1, 19), MutablePair.of(1, 1), MutablePair.of(49, 33), MutablePair.of(75, 26), MutablePair.of(1, 37)};
        }
        if (this.slot == 3) {
            return new MutablePair[]{MutablePair.of(1, 19), MutablePair.of(1, 1), MutablePair.of(60, 18), MutablePair.of(28, 40)};
        }
        return new MutablePair[]{MutablePair.of(49, 26)};
    }

    public boolean hasPlates(ItemStack me) {
        if (this.getStackInSlot(me, 1) != null) {
            if (!me.hasTagCompound()) {
                me.setTagCompound(new NBTTagCompound());
            }
            ItemStack clone = this.getStackInSlot(me, 1).copy();
            clone.stackSize = 1;
            if (UtilPlates.getPlate(clone) != null) {
                me.stackTagCompound.setString("plate", UtilPlates.getPlate(clone).getIdentifier());
                return true;
            } else {
                UtilPlates.removePlate(me);
                return false;
            }
        } else {
            if (!me.hasTagCompound()) {
                me.setTagCompound(new NBTTagCompound());
            }
            UtilPlates.removePlate(me);
            return false;
        }
    }

    @Override
    public ItemStack getStackInSlot(ItemStack me, int var1) {
        if (me.hasTagCompound()) {
            if (me.stackTagCompound.hasKey("inv")) {
                if (me.stackTagCompound.getCompoundTag("inv").hasKey(Integer.toString(var1))) {
                    return ItemStack.loadItemStackFromNBT(me.stackTagCompound.getCompoundTag("inv").getCompoundTag(Integer.toString(var1)));
                }
            }
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(ItemStack me, int var1, ItemStack stack) {
        if (!me.hasTagCompound()) {
            me.setTagCompound(new NBTTagCompound());
        }
        if (!me.stackTagCompound.hasKey("inv")) {
            me.stackTagCompound.setTag("inv", new NBTTagCompound());
        }
        if (me.stackTagCompound.getCompoundTag("inv").hasKey(Integer.toString(var1))) {
            me.stackTagCompound.getCompoundTag("inv").removeTag(Integer.toString(var1));
        }
        NBTTagCompound stc = new NBTTagCompound();
        if (stack != null) {
            stack.writeToNBT(stc);
            me.stackTagCompound.getCompoundTag("inv").setTag(Integer.toString(var1), stc);
            if (var1 == 5 && slot == 1) {
                me.stackTagCompound.setInteger("steamFill", 0);
                me.stackTagCompound.setInteger("maxFill", ((IExosuitTank) stack.getItem()).getStorage(me));
                if (stack.getItem() instanceof BlockTankItem && stack.getItemDamage() == 1) {
                    me.stackTagCompound.setInteger("steamFill", me.stackTagCompound.getInteger("maxFill"));
                }
            }
        }
        this.hasPlates(me);
    }

    @Override
    public boolean isItemValidForSlot(ItemStack me, int var1, ItemStack var2) {
        return true;
    }

    @Override
    public ItemStack decrStackSize(ItemStack me, int var1, int var2) {
        if (this.getStackInSlot(me, var1) != null) {
            ItemStack itemstack;
            if (this.getStackInSlot(me, var1).stackSize <= var2) {
                itemstack = this.getStackInSlot(me, var1);
                this.setInventorySlotContents(me, var1, null);
                this.hasPlates(me);
                return itemstack;
            } else {
                ItemStack stack2 = this.getStackInSlot(me, var1);
                itemstack = stack2.splitStack(var2);
                this.setInventorySlotContents(me, var1, stack2);

                if (this.getStackInSlot(me, var1).stackSize == 0) {
                    this.setInventorySlotContents(me, var1, null);
                }
                this.hasPlates(me);
                return itemstack;
            }
        } else {
            return null;
        }
    }

    @Override
    public boolean canPutInSlot(ItemStack me, int slotNum, ItemStack upgrade) {
        if (slotNum == 0) {
            ItemStack clone = upgrade.copy();
            clone.stackSize = 1;
            return UtilPlates.getPlate(clone) != null;
        }
        if (upgrade.getItem() instanceof IExosuitUpgrade) {
            IExosuitUpgrade upgradeItem = (IExosuitUpgrade) upgrade.getItem();
            return (upgradeItem.getSlot().armor == this.slot && upgradeItem.getSlot().slot == slotNum) || (upgradeItem.getSlot() == ExosuitSlot.VANITY && upgradeItem.getSlot().slot == slotNum);
        } else if (slotNum == ExosuitSlot.VANITY.slot) {
            int[] ids = OreDictionary.getOreIDs(upgrade);
            for (int id : ids) {
                String str = OreDictionary.getOreName(id);
                if (str.contains("dye")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the stack has the steam-related NBT values, and if not, sets them to 0.
     * @param me The ItemStack to check.
     */
    public void updateSteamNBT(ItemStack me) {
        if (!me.hasTagCompound()) {
            me.setTagCompound(new NBTTagCompound());
        }
        if (!me.stackTagCompound.hasKey("steamFill")) {
            me.stackTagCompound.setInteger("steamFill", 0);
        }
        if (!me.stackTagCompound.hasKey("maxFill")) {
            me.stackTagCompound.setInteger("maxFill", 0);
        }
    }

    /**
     * Checks whether the ItemStack has the amount of power in its steam storage. The opposite of
     * @param me The ItemStack
     * @param powerNeeded The amount of power needed
     * @return True if it has power, false if it doesn't, or isn't a chestplate.
     * @see #needsPower(ItemStack, int)
     */
    public boolean hasPower(ItemStack me, int powerNeeded) {
        if (this.slot == 1) {
            updateSteamNBT(me);
            if (me.stackTagCompound.getInteger("steamFill") > powerNeeded) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether the ItemStack can have the amount of power added to its steam storage.
     * @param me The ItemStack
     * @param powerNeeded The amount of power to add
     * @return True if it will not exceed the limit with this amount of power added to it, false if
     *         it will, or if it is not a chestplate.
     */
    public boolean needsPower(ItemStack me, int powerNeeded) {
        if (this.slot == 1) {
            updateSteamNBT(me);
            if (me.stackTagCompound.getInteger("steamFill") + powerNeeded < me.stackTagCompound.getInteger("maxFill")) {
                return true;
            }
        }
        return false;
    }

    public boolean hasUpgrade(ItemStack me, Item check) {
        if (check == null) {
            return false;
        }

        if (me.hasTagCompound()) {
            if (me.stackTagCompound.hasKey("inv")) {
                for (int i = 1; i < 10; i++) {
                    if (me.stackTagCompound.getCompoundTag("inv").hasKey(Integer.toString(i))) {
                        ItemStack stack = ItemStack.loadItemStackFromNBT(me.stackTagCompound.getCompoundTag("inv").getCompoundTag(Integer.toString(i)));
                        if (stack.getItem() == check) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Gets whether the given armor has a tank upgrade.
     * @param me The ItemStack
     */
    public boolean hasTank(ItemStack me) {
        if (slot != 1) {
            return false;
        }
        if (!me.hasTagCompound()) {
            return false;
        }
        if (!me.stackTagCompound.hasKey("inv")) {
            return false;
        }
        NBTTagCompound inv = me.stackTagCompound.getCompoundTag("inv");
        for (int i = 1; i < 10; i++) {
            String s = Integer.toString(i);
            if (inv.hasKey(s)) {
                ItemStack stack = ItemStack.loadItemStackFromNBT(inv.getCompoundTag(s));
                if (stack != null && stack.getItem() != null && stack.getItem() instanceof IExosuitTank) {
                    return true;
                }
            }
        }
        return false;
    }

    public IExosuitUpgrade[] getUpgrades(ItemStack me) {
        ArrayList<IExosuitUpgrade> upgrades = new ArrayList<IExosuitUpgrade>();
        if (me.hasTagCompound()) {
            if (me.stackTagCompound.hasKey("inv")) {
                for (int i = 2; i < 10; i++) {
                    if (me.stackTagCompound.getCompoundTag("inv").hasKey(Integer.toString(i))) {
                        ItemStack stack = ItemStack.loadItemStackFromNBT(me.stackTagCompound.getCompoundTag("inv").getCompoundTag(Integer.toString(i)));
                        if (stack.getItem() instanceof IExosuitUpgrade) {
                            upgrades.add((IExosuitUpgrade) stack.getItem());
                        }
                    }
                }
            }
        }
        return upgrades.toArray(new IExosuitUpgrade[0]);
    }

    @Override
    public void drawSlot(GuiContainer guiEngineeringTable, int slotNum, int i, int j) {
        guiEngineeringTable.mc.getTextureManager().bindTexture(GuiEngineeringTable.GUI_TEXTURES);
        if (this.slot == 0) {
            switch (slotNum) {
                case 0:
                    guiEngineeringTable.drawTexturedModalRect(i, j, 194, 0, 18, 18);
                    break;
                case 1:
                    guiEngineeringTable.drawTexturedModalRect(i, j, 230, 36, 18, 18);
                    break;
//				case 2:
//					guiEngineeringTable.drawTexturedModalRect(i, j, 194, 18, 18, 18);
//					break;
//				case 3:
//					guiEngineeringTable.drawTexturedModalRect(i, j, 212, 18, 18, 18);
//					break;
                default:
                    guiEngineeringTable.drawTexturedModalRect(i, j, 176, 0, 18, 18);
            }
        }
        if (this.slot == 1) {
            switch (slotNum) {
                case 0:
                    guiEngineeringTable.drawTexturedModalRect(i, j, 194, 0, 18, 18);
                    break;
                case 1:
                    guiEngineeringTable.drawTexturedModalRect(i, j, 230, 36, 18, 18);
                    break;
//				case 2:
//					guiEngineeringTable.drawTexturedModalRect(i, j, 230, 18, 18, 18);
//					break;
//				case 3:
//					guiEngineeringTable.drawTexturedModalRect(i, j, 176, 18, 18, 18);
//					break;
                case 4:
                    guiEngineeringTable.drawTexturedModalRect(i, j, 176, 36, 18, 18);
                    break;
                default:
                    guiEngineeringTable.drawTexturedModalRect(i, j, 176, 0, 18, 18);
            }
        }
        if (this.slot == 2) {
            switch (slotNum) {
                case 0:
                    guiEngineeringTable.drawTexturedModalRect(i, j, 194, 0, 18, 18);
                    break;
                case 1:
                    guiEngineeringTable.drawTexturedModalRect(i, j, 230, 36, 18, 18);
                    break;
//				case 2:
//					guiEngineeringTable.drawTexturedModalRect(i, j, 212, 36, 18, 18);
//					break;
//				case 3:
//					guiEngineeringTable.drawTexturedModalRect(i, j, 194, 36, 18, 18);
//					break;
//				case 4:
//					guiEngineeringTable.drawTexturedModalRect(i, j, 176, 36, 18, 18);
//					break;
                default:
                    guiEngineeringTable.drawTexturedModalRect(i, j, 176, 0, 18, 18);
            }
        }
        if (this.slot == 3) {
            switch (slotNum) {
                case 0:
                    guiEngineeringTable.drawTexturedModalRect(i, j, 194, 0, 18, 18);
                    break;
                case 1:
                    guiEngineeringTable.drawTexturedModalRect(i, j, 230, 36, 18, 18);
                    break;
//				case 2:
//					guiEngineeringTable.drawTexturedModalRect(i, j, 230, 0, 18, 18);
//					break;
//				case 3:
//					guiEngineeringTable.drawTexturedModalRect(i, j, 212, 0, 18, 18);
//					break;
                default:
                    guiEngineeringTable.drawTexturedModalRect(i, j, 176, 0, 18, 18);
            }
        }
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        if (this.slot == 1) {
            return 10000;
        }
        return 0;
    }

    @Override
    public int getDamage(ItemStack stack) {
        updateSteamNBT(stack);
        return (int) (((double) stack.stackTagCompound.getInteger("steamFill")) /
          (double) stack.stackTagCompound.getInteger("maxFill") * 10000.0D);
    }

    @Override
    public int steamPerDurability() {
        return Config.exoConsumption;
    }

    @Override
    public boolean canCharge(ItemStack stack) {
        if (this.slot == 1) {
            ItemExosuitArmor item = (ItemExosuitArmor) stack.getItem();
            if (item.getStackInSlot(stack, 5) != null && item.getStackInSlot(stack, 5).getItem() instanceof IExosuitTank) {
                IExosuitTank tank = (IExosuitTank) item.getStackInSlot(stack, 5).getItem();
                return tank.canFill(stack);
            }
        }
        return false;
    }

    @Override
    public boolean addSteam(ItemStack me, int amount, EntityPlayer player) {
        int curSteam = me.stackTagCompound.getInteger("steamFill");
        int newSteam = curSteam + amount;
        if (needsPower(me, amount)) {
            me.stackTagCompound.setInteger("steamFill", newSteam);
            return true;
        }
        return false;
    }

    @Override
    public Multimap getAttributeModifiers(ItemStack stack) {
        Multimap map = HashMultimap.create();
        ItemExosuitArmor armor = (ItemExosuitArmor) stack.getItem();
        if (CrossMod.BOTANIA) {
            map = BotaniaIntegration.addModifiers(map, stack, armorType);
        }
        boolean hasKnockback = false;
        double knockbackAmount = 0.0D;
        if (armor.hasPlates(stack) &&
          UtilPlates.getPlate(stack.stackTagCompound.getString("plate")).getIdentifier().equals("Lead")) {
            hasKnockback = true;
            knockbackAmount += 0.25D;
        }
        if (armor.hasUpgrade(stack, SteamcraftItems.anchorHeels)) {
            hasKnockback = true;
            knockbackAmount += 0.25D;
        }
        if (hasKnockback) {
            map.put(SharedMonsterAttributes.knockbackResistance.getAttributeUnlocalizedName(),
              new AttributeModifier(new UUID(776437, armorType), "Lead exosuit " + armorType,
                knockbackAmount, 0));
        }
        return map;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack me, EntityPlayer player, List list, boolean par4) {
        super.addInformation(me, player, list, par4);
        if (me.hasTagCompound()) {
            if (hasPlates(me) && UtilPlates.getPlate(me.stackTagCompound.getString("plate")).getIdentifier() != "Thaumium" && UtilPlates.getPlate(me.stackTagCompound.getString("plate")).getIdentifier() != "Terrasteel") {
                list.add(EnumChatFormatting.BLUE + UtilPlates.getPlate(me.stackTagCompound.getString("plate")).effect());
            }
            if (me.stackTagCompound.hasKey("inv")) {
                for (int i = 3; i < 10; i++) {
                    if (me.stackTagCompound.getCompoundTag("inv").hasKey(Integer.toString(i))) {
                        ItemStack stack = ItemStack.loadItemStackFromNBT(me.stackTagCompound.getCompoundTag("inv").getCompoundTag(Integer.toString(i)));
                        list.add(EnumChatFormatting.RED + stack.getDisplayName());
                    }
                }
            }
            if (me.stackTagCompound.getCompoundTag("inv").hasKey("2")) {
                ItemStack stack = ItemStack.loadItemStackFromNBT(me.stackTagCompound.getCompoundTag("inv").getCompoundTag("2"));
                if (stack.getItem() != null && stack.getItem() == SteamcraftItems.enderShroud) {
                    list.add(EnumChatFormatting.DARK_GREEN + StatCollector.translateToLocal("steamcraft.exosuit.shroud"));
                } else {
                    int[] ids = OreDictionary.getOreIDs(stack);
                    int dye = -1;
                    outerloop:
                    for (int id : ids) {
                        String str = OreDictionary.getOreName(id);
                        if (str.contains("dye")) {
                            for (int i = 0; i < ModelExosuit.DYES.length; i++) {
                                if (ModelExosuit.DYES[i].equals(str.substring(3))) {
                                    dye = 15 - i;
                                    break outerloop;
                                }
                            }
                        }
                    }
                    if (dye != -1) {
                        list.add(EnumChatFormatting.DARK_GREEN + StatCollector.translateToLocal("steamcraft.color." + ModelExosuit.DYES[15 - dye].toLowerCase()));
                    } else {
                        list.add(EnumChatFormatting.DARK_GREEN + stack.getDisplayName());
                    }
                }
            }
        }
        updateSteamNBT(me);
        if (slot == 1) {
           list.add(EnumChatFormatting.WHITE + "" + me.stackTagCompound.getInteger("steamFill") * 5 + "/" + me.stackTagCompound.getInteger("maxFill") * 5 + " SU");
        }
    }

    @Override
    public void drawBackground(GuiEngineeringTable guiEngineeringTable, int i,
                               int j, int k) {
        guiEngineeringTable.mc.getTextureManager().bindTexture(largeIcons);
        guiEngineeringTable.drawTexturedModalRect(j + 26, k + 3, 0 + 64 * slot, 0, 64, 64);
    }

}