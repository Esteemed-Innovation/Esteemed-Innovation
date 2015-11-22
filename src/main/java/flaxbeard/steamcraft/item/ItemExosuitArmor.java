package flaxbeard.steamcraft.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.api.IEngineerable;
import flaxbeard.steamcraft.api.ISteamChargable;
import flaxbeard.steamcraft.api.exosuit.*;
import flaxbeard.steamcraft.client.render.model.exosuit.ExosuitModelCache;
import flaxbeard.steamcraft.client.render.model.exosuit.ModelExosuit;
import flaxbeard.steamcraft.gui.GuiEngineeringTable;
import flaxbeard.steamcraft.handler.SteamcraftEventHandler;
import flaxbeard.steamcraft.integration.BotaniaIntegration;
import flaxbeard.steamcraft.integration.CrossMod;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.tuple.MutablePair;

import vazkii.botania.api.item.IPixieSpawner;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Optional.Interface(iface = "vazkii.botania.api.item.IPixieSpawner", modid = "Botania")
public class ItemExosuitArmor extends ItemArmor implements IPixieSpawner, ISpecialArmor, IEngineerable, ISteamChargable {
    public static final ResourceLocation largeIcons = new ResourceLocation("steamcraft:textures/gui/engineering2.png");


    public int slot;
    public IIcon grey;

    public ItemExosuitArmor(int i, ArmorMaterial mat) {
        super(mat, 1, i);
        slot = i;
        this.setMaxDamage(0);
    }

    @Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return false;
    }

    public int getPlateReqs() {
        switch (slot) {
            case 0:
                return 5;
            case 1:
                return 8;
            case 2:
                return 7;
            case 3:
                return 4;
            default:
                return 1;
        }
    }

    public String getString() {
        return this.iconString;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        if (stack.getItem() == SteamcraftItems.exoArmorLegs) {
            return "steamcraft:textures/models/armor/exo_2.png";
        } else {
            return "steamcraft:textures/models/armor/exo_1.png";
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        super.registerIcons(par1IconRegister);
        UtilPlates.registerPlatesForItem(par1IconRegister, this);
        grey = par1IconRegister.registerIcon(this.iconString + "_grey");
    }

    @Override
    public int getRenderPasses(int metadata) {
        return 3;
    }

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack, int pass) {
        return (this.getStackInSlot(stack, 2) != null && this.getStackInSlot(stack, 2).getItem() == SteamcraftItems.enderShroud);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ItemStack stack, int pass) {

        if (pass == 0) {
            return this.itemIcon;
        }
        if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("plate") && pass > 1) {
            return UtilPlates.getIconFromPlate(stack.stackTagCompound.getString("plate"), this);
        } else if (this.getStackInSlot(stack, 2) != null) {
            Item vanity = getStackInSlot(stack, 2).getItem();
            int[] ids = OreDictionary.getOreIDs(getStackInSlot(stack, 2));
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
                return grey;
            } else {
                return this.itemIcon;
            }
        }
        return this.itemIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass) {
        if (this.getStackInSlot(stack, 2) != null && (pass == 1 || (pass > 1 && !stack.stackTagCompound.hasKey("plate")))) {
            Item vanity = getStackInSlot(stack, 2).getItem();
            int[] ids = OreDictionary.getOreIDs(getStackInSlot(stack, 2));
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
                float[] color = EntitySheep.fleeceColorTable[dye];
                return new Color(color[0], color[1], color[2]).getRGB();
            }
        }
        return super.getColorFromItemStack(stack, pass);
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
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        if (!stack.stackTagCompound.hasKey("steamFill")) {
            stack.stackTagCompound.setInteger("steamFill", 0);
        }
        if (!stack.stackTagCompound.hasKey("maxFill")) {
            stack.stackTagCompound.setInteger("maxFill", 0);
        }
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
            return (upgradeItem.getSlot().armor == this.slot && upgradeItem.getSlot().slot == slotNum) || (upgradeItem.getSlot() == ExosuitSlot.vanity && upgradeItem.getSlot().slot == slotNum);
        } else if (slotNum == ExosuitSlot.vanity.slot) {
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

    public boolean hasPower(ItemStack me, int powerNeeded) {
        if (this.slot == 1) {
            if (!me.hasTagCompound()) {
                me.setTagCompound(new NBTTagCompound());
            }
            if (!me.stackTagCompound.hasKey("steamFill")) {
                me.stackTagCompound.setInteger("steamFill", 0);
            }
            if (!me.stackTagCompound.hasKey("maxFill")) {
                me.stackTagCompound.setInteger("maxFill", 0);
            }
            if (me.stackTagCompound.getInteger("steamFill") > powerNeeded) {
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
        guiEngineeringTable.mc.getTextureManager().bindTexture(GuiEngineeringTable.furnaceGuiTextures);
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
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        if (!stack.stackTagCompound.hasKey("steamFill")) {
            stack.stackTagCompound.setInteger("steamFill", 0);
        }
        if (!stack.stackTagCompound.hasKey("maxFill")) {
            stack.stackTagCompound.setInteger("maxFill", 0);
        }
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
    public Multimap getAttributeModifiers(ItemStack stack) {
        Multimap map = HashMultimap.create();
        if (CrossMod.BOTANIA) {
            map = BotaniaIntegration.addModifiers(map, stack, armorType);
        }
        if ((((ItemExosuitArmor) stack.getItem()).hasPlates(stack) && UtilPlates.getPlate(stack.stackTagCompound.getString("plate")).getIdentifier() == "Lead")) {
            map.put(SharedMonsterAttributes.knockbackResistance.getAttributeUnlocalizedName(), new AttributeModifier(new UUID(776437, armorType), "Lead exosuit " + armorType, 0.25F, 0));
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
        if (!me.hasTagCompound()) {
            me.setTagCompound(new NBTTagCompound());
        }
        if (!me.stackTagCompound.hasKey("steamFill")) {
            me.stackTagCompound.setInteger("steamFill", 0);
        }
        if (!me.stackTagCompound.hasKey("maxFill")) {
            me.stackTagCompound.setInteger("maxFill", 0);
        }
        if (slot == 1) {
            list.add(EnumChatFormatting.WHITE + "" + me.stackTagCompound.getInteger("steamFill") * 5 + "/" + me.stackTagCompound.getInteger("maxFill") * 5 + " SU");
        }
    }

    @Optional.Method(modid = "Botania")
    @Override
    public float getPixieChance(ItemStack stack) {
        if ((((ItemExosuitArmor) stack.getItem()).hasPlates(stack) && UtilPlates.getPlate(stack.stackTagCompound.getString("plate")).getIdentifier() == "Elementium")) {
            switch (armorType) {
                case 0:
                    return 0.025F;
                case 1:
                    return 0.04F;
                case 2:
                    return 0.035F;
                case 3:
                    return 0.02F;
            }
        }
        return 0;
    }

    @Override
    public void drawBackground(GuiEngineeringTable guiEngineeringTable, int i,
                               int j, int k) {
        guiEngineeringTable.mc.getTextureManager().bindTexture(largeIcons);
        guiEngineeringTable.drawTexturedModalRect(j + 26, k + 3, 0 + 64 * slot, 0, 64, 64);
    }

}