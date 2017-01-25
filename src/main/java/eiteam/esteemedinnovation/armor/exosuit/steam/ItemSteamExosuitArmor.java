package eiteam.esteemedinnovation.armor.exosuit.steam;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import eiteam.esteemedinnovation.api.Constants;
import eiteam.esteemedinnovation.api.SteamChargable;
import eiteam.esteemedinnovation.api.exosuit.*;
import eiteam.esteemedinnovation.armor.ArmorModule;
import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.storage.steam.BlockTankItem;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemSteamExosuitArmor extends ItemArmor implements ExosuitArmor, SteamChargable {
    public static final ResourceLocation LARGE_ICONS = new ResourceLocation(EsteemedInnovation.MOD_ID + ":textures/gui/engineering2.png");

    public EntityEquipmentSlot slot;

    public ItemSteamExosuitArmor(EntityEquipmentSlot slot, ArmorMaterial mat) {
        super(mat, 1, slot);
        this.slot = slot;
        setMaxDamage(0);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return false;
    }

    @Override
    public String getString() {
        return EsteemedInnovation.MOD_ID + ":items/steam_exosuit_" + slot.getName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped defaultModel) {
        if (!(entityLiving instanceof EntityPlayer)) {
            return null;
        }

        ModelSteamExosuit modelExosuit = SteamExosuitModelCache.INSTANCE.getModel((EntityPlayer) entityLiving, armorSlot);

        boolean head = armorSlot == EntityEquipmentSlot.HEAD;
        modelExosuit.bipedHead.showModel = head;
        modelExosuit.bipedHeadwear.showModel = head;

        boolean body = armorSlot == EntityEquipmentSlot.CHEST;
        boolean legs = armorSlot == EntityEquipmentSlot.LEGS;
        modelExosuit.bipedBody.showModel = body || legs;
        modelExosuit.bipedRightArm.showModel = body;
        modelExosuit.bipedLeftArm.showModel = body;

        boolean feet = armorSlot == EntityEquipmentSlot.FEET;
        modelExosuit.bipedRightLeg.showModel = legs || feet;
        modelExosuit.bipedLeftLeg.showModel = legs || feet;

        return modelExosuit;
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int armorType) {
        if (armor.hasTagCompound()) {
            if (armor.getTagCompound().hasKey("plate")) {
                ExosuitPlate plate = UtilPlates.getPlate(armor.getTagCompound().getString("plate"));
                return new ArmorProperties(0, plate.getDamageReductionAmount(slot, source) / 25.0D, ItemArmor.ArmorMaterial.IRON.getDurability(slot));
            }
        }
        return new ArmorProperties(0, ItemArmor.ArmorMaterial.IRON.getDamageReductionAmount(slot) / 25.0D, ItemArmor.ArmorMaterial.IRON.getDurability(slot));
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        updateSteamNBT(stack);
        //return 0.9D;
        return 1.0D - (stack.getTagCompound().getInteger("steamFill") / (double) stack.getTagCompound().getInteger("maxFill"));
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        if (!stack.getTagCompound().hasKey("maxFill")) {
            stack.getTagCompound().setInteger("maxFill", 0);
        }
        return stack.getTagCompound().getInteger("maxFill") > 0;
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int armorType) {
        if (armor.hasTagCompound()) {
            if (armor.getTagCompound().hasKey("plate")) {
                ExosuitPlate plate = UtilPlates.getPlate(armor.getTagCompound().getString("plate"));
                return plate.getDamageReductionAmount(slot, DamageSource.generic);
            }
        }
        return ItemArmor.ArmorMaterial.LEATHER.getDamageReductionAmount(slot);
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
        if (this.slot == EntityEquipmentSlot.CHEST) {
            drainSteam(stack, damage * 40, entity);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Pair<Integer, Integer>[] engineerCoordinates() {
        switch (slot) {
            case HEAD: {
                return new Pair[] {
                  Pair.of(1, 19),
                  Pair.of(1, 1),
                  Pair.of(39, 16),
                  Pair.of(59, 36)
                };
            }
            case CHEST: {
                return new Pair[] {
                  Pair.of(1, 19),
                  Pair.of(1, 1),
                  Pair.of(49, 33),
                  Pair.of(75, 26),
                  Pair.of(1, 37)
                };
            }
            case LEGS: {
                return new Pair[] {
                  Pair.of(1, 19),
                  Pair.of(1, 1),
                  Pair.of(60, 12),
                  Pair.of(37, 40)
                };
            }
            case FEET: {
                return new Pair[] {
                  Pair.of(1, 19),
                  Pair.of(1, 1),
                  Pair.of(60, 18),
                  Pair.of(28, 40)
                };
            }
            default: {
                break;
            }
        }
        return new Pair[] { Pair.of(49, 26) };
    }

    public boolean hasPlates(ItemStack me) {
        if (getStackInSlot(me, 1) != null) {
            if (!me.hasTagCompound()) {
                me.setTagCompound(new NBTTagCompound());
            }
            ItemStack clone = getStackInSlot(me, 1).copy();
            clone.stackSize = 1;
            if (UtilPlates.getPlate(clone) != null) {
                me.getTagCompound().setString("plate", UtilPlates.getPlate(clone).getIdentifier());
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
    public boolean hasUpgrade(ItemStack me, Item check) {
        if (me != null && check != null && me.hasTagCompound() && me.getTagCompound().hasKey("inv")) {
            for (int i = 1; i < 10; i++) {
                if (me.getTagCompound().getCompoundTag("inv").hasKey(Integer.toString(i))) {
                    ItemStack stack = ItemStack.loadItemStackFromNBT(me.getTagCompound().getCompoundTag("inv").getCompoundTag(Integer.toString(i)));
                    if (stack.getItem() == check) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public ItemStack getStackInSlot(ItemStack me, int var1) {
        if (me.hasTagCompound()) {
            if (me.getTagCompound().hasKey("inv")) {
                if (me.getTagCompound().getCompoundTag("inv").hasKey(Integer.toString(var1))) {
                    return ItemStack.loadItemStackFromNBT(me.getTagCompound().getCompoundTag("inv").getCompoundTag(Integer.toString(var1)));
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
        if (!me.getTagCompound().hasKey("inv")) {
            me.getTagCompound().setTag("inv", new NBTTagCompound());
        }
        if (me.getTagCompound().getCompoundTag("inv").hasKey(Integer.toString(var1))) {
            me.getTagCompound().getCompoundTag("inv").removeTag(Integer.toString(var1));
        }
        NBTTagCompound stc = new NBTTagCompound();
        if (stack != null) {
            stack.writeToNBT(stc);
            me.getTagCompound().getCompoundTag("inv").setTag(Integer.toString(var1), stc);
            if (var1 == 5 && slot == EntityEquipmentSlot.CHEST) {
                me.getTagCompound().setInteger("steamFill", 0);
                me.getTagCompound().setInteger("maxFill", ((ExosuitTank) stack.getItem()).getStorage(me));
                if (stack.getItem() instanceof BlockTankItem && stack.getItemDamage() == 1) {
                    me.getTagCompound().setInteger("steamFill", me.getTagCompound().getInteger("maxFill"));
                }
            }
        }
        hasPlates(me);
    }

    @Override
    public boolean isItemValidForSlot(ItemStack me, int var1, ItemStack var2) {
        return true;
    }

    @Override
    public ItemStack decrStackSize(ItemStack me, int var1, int var2) {
        if (getStackInSlot(me, var1) != null) {
            ItemStack itemstack;
            if (getStackInSlot(me, var1).stackSize <= var2) {
                itemstack = getStackInSlot(me, var1);
                setInventorySlotContents(me, var1, null);
                hasPlates(me);
                return itemstack;
            } else {
                ItemStack stack2 = getStackInSlot(me, var1);
                itemstack = stack2.splitStack(var2);
                setInventorySlotContents(me, var1, stack2);

                if (getStackInSlot(me, var1).stackSize == 0) {
                    setInventorySlotContents(me, var1, null);
                }
                hasPlates(me);
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
        if (upgrade.getItem() instanceof ExosuitUpgrade) {
            ExosuitUpgrade upgradeItem = (ExosuitUpgrade) upgrade.getItem();
            ExosuitSlot upgradeSlot = upgradeItem.getSlot();
            return (upgradeSlot.getArmorPiece() == slot && upgradeSlot.getEngineeringSlot() == slotNum) || (upgradeItem.getSlot() == ExosuitSlot.VANITY && upgradeSlot.getEngineeringSlot() == slotNum);
        } else if (slotNum == ExosuitSlot.VANITY.getEngineeringSlot()) {
            // TODO: Optimize by using a static list of dye oredicts generated at load time (OreDictHelper).
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
        if (!me.getTagCompound().hasKey("steamFill")) {
            me.getTagCompound().setInteger("steamFill", 0);
        }
        if (!me.getTagCompound().hasKey("maxFill")) {
            me.getTagCompound().setInteger("maxFill", 0);
        }
    }

    @Override
    public boolean hasPower(ItemStack me, int powerNeeded) {
        if (slot == EntityEquipmentSlot.CHEST) {
            updateSteamNBT(me);
            if (me.getTagCompound().getInteger("steamFill") > powerNeeded) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean needsPower(ItemStack me, int powerNeeded) {
        if (slot == EntityEquipmentSlot.CHEST) {
            updateSteamNBT(me);
            if (me.getTagCompound().getInteger("steamFill") + powerNeeded < me.getTagCompound().getInteger("maxFill")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets whether the given armor has a tank upgrade.
     * @param me The ItemStack
     */
    public boolean hasTank(ItemStack me) {
        if (slot != EntityEquipmentSlot.CHEST) {
            return false;
        }
        if (!me.hasTagCompound()) {
            return false;
        }
        if (!me.getTagCompound().hasKey("inv")) {
            return false;
        }
        NBTTagCompound inv = me.getTagCompound().getCompoundTag("inv");
        for (int i = 1; i < 10; i++) {
            String s = Integer.toString(i);
            if (inv.hasKey(s)) {
                ItemStack stack = ItemStack.loadItemStackFromNBT(inv.getCompoundTag(s));
                if (stack != null && stack.getItem() != null && stack.getItem() instanceof ExosuitTank) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    @Nonnull
    public ExosuitUpgrade[] getUpgrades(ItemStack me) {
        ArrayList<ExosuitUpgrade> upgrades = new ArrayList<>();
        if (me.hasTagCompound()) {
            if (me.getTagCompound().hasKey("inv")) {
                for (int i = 2; i < 10; i++) {
                    if (me.getTagCompound().getCompoundTag("inv").hasKey(Integer.toString(i))) {
                        ItemStack stack = ItemStack.loadItemStackFromNBT(me.getTagCompound().getCompoundTag("inv").getCompoundTag(Integer.toString(i)));
                        if (stack.getItem() instanceof ExosuitUpgrade) {
                            upgrades.add((ExosuitUpgrade) stack.getItem());
                        }
                    }
                }
            }
        }
        return upgrades.toArray(new ExosuitUpgrade[0]);
    }

    @Override
    public void drawSlot(GuiContainer guiEngineeringTable, int slotNum, int i, int j) {
        guiEngineeringTable.mc.getTextureManager().bindTexture(Constants.ENG_GUI_TEXTURES);
        switch (slot) {
            case HEAD:
            case LEGS:
            case FEET: {
                switch (slotNum) {
                    case 0: {
                        guiEngineeringTable.drawTexturedModalRect(i, j, 194, 0, 18, 18);
                        break;
                    }
                    case 1: {
                        guiEngineeringTable.drawTexturedModalRect(i, j, 230, 36, 18, 18);
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }
            case CHEST: {
                switch (slotNum) {
                    case 0: {
                        guiEngineeringTable.drawTexturedModalRect(i, j, 194, 0, 18, 18);
                        break;
                    }
                    case 1: {
                        guiEngineeringTable.drawTexturedModalRect(i, j, 230, 36, 18, 18);
                        break;
                    }
                    case 4: {
                        guiEngineeringTable.drawTexturedModalRect(i, j, 176, 36, 18, 18);
                        break;
                    }
                    default: {
                        guiEngineeringTable.drawTexturedModalRect(i, j, 176, 0, 18, 18);
                    }
                }
            }
            default: {
                break;
            }
        }
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        if (slot == EntityEquipmentSlot.CHEST) {
            return 10_000;
        }
        return 0;
    }

    @Override
    public int getDamage(ItemStack stack) {
        updateSteamNBT(stack);
        return (int) (((double) stack.getTagCompound().getInteger("steamFill")) /
          (double) stack.getTagCompound().getInteger("maxFill") * 10_000.0D);
    }

    @Override
    public int steamPerDurability() {
        return Config.exoConsumption;
    }

    @Override
    public boolean canCharge(ItemStack stack) {
        if (slot == EntityEquipmentSlot.CHEST) {
            ItemSteamExosuitArmor item = (ItemSteamExosuitArmor) stack.getItem();
            if (item.getStackInSlot(stack, 5) != null && item.getStackInSlot(stack, 5).getItem() instanceof ExosuitTank) {
                ExosuitTank tank = (ExosuitTank) item.getStackInSlot(stack, 5).getItem();
                return tank.canFill(stack);
            }
        }
        return false;
    }

    @Override
    public boolean addSteam(ItemStack me, int amount, EntityLivingBase player) {
        int curSteam = me.getTagCompound().getInteger("steamFill");
        int newSteam = curSteam + amount;
        if (needsPower(me, amount)) {
            me.getTagCompound().setInteger("steamFill", newSteam);
            return true;
        }
        return false;
    }

    @Override
    public boolean drainSteam(ItemStack me, int amountToDrain, EntityLivingBase entity) {
        if (me != null) {
            if (me.getTagCompound() == null) {
                me.setTagCompound(new NBTTagCompound());
            }
            if (!me.getTagCompound().hasKey("steamFill")) {
                me.getTagCompound().setInteger("steamFill", 0);
            }
            int fill = me.getTagCompound().getInteger("steamFill");
            fill = Math.max(0, fill - amountToDrain);
            me.getTagCompound().setInteger("steamFill", fill);
            return true;
        }
        return false;
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot armorSlot, ItemStack stack) {
        Multimap<String, AttributeModifier> map = HashMultimap.create();

        ItemSteamExosuitArmor armor = (ItemSteamExosuitArmor) stack.getItem();

        if (armor.hasPlates(stack)) {
            ExosuitPlate plate = UtilPlates.getPlate(stack);
            if (plate != null) {
                map.putAll(plate.getAttributeModifiersForExosuit(armorSlot, stack));
            }
        }

        ExosuitUpgrade[] upgrades = armor.getUpgrades(stack);
        for (ExosuitUpgrade upgrade : upgrades) {
            map.putAll(upgrade.getAttributeModifiersForExosuit(armorSlot, stack));
        }

        return map;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack me, EntityPlayer player, List<String> list, boolean advanced) {
        super.addInformation(me, player, list, advanced);
        if (me.hasTagCompound()) {
            // TODO: Abstract into API
            if (hasPlates(me) && !"Thaumium".equals(UtilPlates.getPlate(me.getTagCompound().getString("plate")).getIdentifier()) &&
              !"Terrasteel".equals(UtilPlates.getPlate(me.getTagCompound().getString("plate")).getIdentifier())) {
                list.add(TextFormatting.BLUE + UtilPlates.getPlate(me.getTagCompound().getString("plate")).effect());
            }
            if (me.getTagCompound().hasKey("inv")) {
                for (int i = 3; i < 10; i++) {
                    if (me.getTagCompound().getCompoundTag("inv").hasKey(Integer.toString(i))) {
                        ItemStack stack = ItemStack.loadItemStackFromNBT(me.getTagCompound().getCompoundTag("inv").getCompoundTag(Integer.toString(i)));
                        list.add(TextFormatting.RED + stack.getDisplayName());
                    }
                }
            }
            if (me.getTagCompound().getCompoundTag("inv").hasKey("2")) {
                ItemStack stack = ItemStack.loadItemStackFromNBT(me.getTagCompound().getCompoundTag("inv").getCompoundTag("2"));
                // TODO: Abstract into API
                if (stack.getItem() == ArmorModule.ENDER_SHROUD) {
                    list.add(TextFormatting.DARK_GREEN + I18n.format("esteemedinnovation.exosuit.shroud"));
                } else {
                    int dye = -1;
                    int dyeIndex = ModelSteamExosuit.findDyeIndexFromItemStack(stack);
                    if (dyeIndex != -1) {
                        dye = dyeIndex;
                    }
                    if (dye != -1) {
                        list.add(TextFormatting.DARK_GREEN + I18n.format("esteemedinnovation.color." + ModelSteamExosuit.DYES[dye].toLowerCase()));
                    } else {
                        list.add(TextFormatting.DARK_GREEN + stack.getDisplayName());
                    }
                }
            }
        }
        updateSteamNBT(me);
        if (slot == EntityEquipmentSlot.CHEST) {
           list.add(TextFormatting.WHITE + "" + me.getTagCompound().getInteger("steamFill") * 5 + "/" + me.getTagCompound().getInteger("maxFill") * 5 + " SU");
        }
    }

    @Override
    public void drawBackground(GuiContainer guiEngineeringTable, int i, int j, int k) {
        guiEngineeringTable.mc.getTextureManager().bindTexture(LARGE_ICONS);
        guiEngineeringTable.drawTexturedModalRect(j + 26, k + 3, 64 * slot.getIndex(), 0, 64, 64);
    }

    @Nonnull
    @Override
    public ExosuitEventHandler[] getInstalledEventHandlers(ItemStack self) {
        List<ExosuitEventHandler> handlers = new ArrayList<>(Arrays.asList(getUpgrades(self)));
        if (self.hasTagCompound() && self.getTagCompound().hasKey("plate")) {
            handlers.add(UtilPlates.getPlate(self.getTagCompound().getString("plate")));
        }
        return handlers.toArray(new ExosuitEventHandler[handlers.size()]);
    }
}