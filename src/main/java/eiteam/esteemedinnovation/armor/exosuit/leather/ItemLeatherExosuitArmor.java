package eiteam.esteemedinnovation.armor.exosuit.leather;

import eiteam.esteemedinnovation.api.Constants;
import eiteam.esteemedinnovation.api.exosuit.*;
import eiteam.esteemedinnovation.armor.ArmorModule;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static eiteam.esteemedinnovation.api.Constants.EI_MODID;

public class ItemLeatherExosuitArmor extends ItemArmor implements ExosuitArmor {
    static final ResourceLocation MODEL_TEXTURE = new ResourceLocation(EI_MODID, "textures/models/armor/leather_exo.png");

    public ItemLeatherExosuitArmor(EntityEquipmentSlot slot) {
        super(ArmorModule.LEATHER_EXO_MAT, 1, slot);
        setMaxDamage(0);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        NBTTagCompound plateNBT = getPlateNBT(stack);
        if (plateNBT != null) {
            ItemStack plateStack = ItemStack.loadItemStackFromNBT(plateNBT.getCompoundTag("Stack"));
            if (plateStack != null) {
                tooltip.add(TextFormatting.BLUE + plateStack.getDisplayName());
            }
        }

        super.addInformation(stack, playerIn, tooltip, advanced);
    }

    @Override
    public Pair<Integer, Integer>[] engineerCoordinates() {
        return new Pair[] { Pair.of(49, 29) };
    }

    /**
     * @return The Plate NBT (contains an ID key and a Stack key) or null.
     */
    @Nullable
    private NBTTagCompound getPlateNBT(ItemStack armorStack) {
        if (armorStack.hasTagCompound() && armorStack.getTagCompound().hasKey("Plate")) {
            return armorStack.getTagCompound().getCompoundTag("Plate");
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlot(ItemStack me, int slot) {
        NBTTagCompound plateNBT = getPlateNBT(me);
        if (plateNBT != null) {
            return ItemStack.loadItemStackFromNBT(plateNBT.getCompoundTag("Stack"));
        }
        return null;
    }

    /**
     * @param armorStack The armor piece
     * @return The Exosuit Plate installed in this armor piece
     */
    @Nullable
    public ExosuitPlate getPlate(ItemStack armorStack) {
        NBTTagCompound plateNBT = getPlateNBT(armorStack);
        if (plateNBT != null) {
            return UtilPlates.getPlate(plateNBT.getString("ID"));
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(ItemStack me, int slot, ItemStack stack) {
        if (stack == null && me.hasTagCompound()) {
            me.getTagCompound().removeTag("Plate");
            return;
        }

        if (!me.hasTagCompound()) {
            me.setTagCompound(new NBTTagCompound());
        }
        ExosuitPlate plate = UtilPlates.getPlate(stack);
        if (plate != null) {
            NBTTagCompound plateNBT = new NBTTagCompound();
            plateNBT.setString("ID", plate.getIdentifier());
            plateNBT.setTag("Stack", stack.writeToNBT(new NBTTagCompound()));
            me.getTagCompound().setTag("Plate", plateNBT);
        }
    }

    @Override
    public boolean isItemValidForSlot(ItemStack me, int slot, ItemStack var2) {
        return true;
    }

    @Override
    public ItemStack decrStackSize(ItemStack me, int slot, int size) {
        NBTTagCompound plateNBT = getPlateNBT(me);
        if (plateNBT != null) {
            ItemStack toBeRemoved = ItemStack.loadItemStackFromNBT(plateNBT.getCompoundTag("Stack"));
            me.getTagCompound().removeTag("Plate");
            return toBeRemoved;
        }
        return null;
    }

    @Override
    public void drawSlot(GuiContainer guiEngineeringTable, int slotnum, int i, int j) {
        guiEngineeringTable.mc.getTextureManager().bindTexture(Constants.ENG_GUI_TEXTURES);
        guiEngineeringTable.drawTexturedModalRect(i, j, 194, 0, 18, 18);
    }

    @Override
    public boolean canPutInSlot(ItemStack me, int slotNum, ItemStack upgrade) {
        return UtilPlates.getPlate(upgrade) != null;
    }

    @Override
    public void drawBackground(GuiContainer guiEngineeringTable, int i, int j, int k) {
        guiEngineeringTable.mc.getTextureManager().bindTexture(Constants.ENG_ARMOR_TEXTURES);
        guiEngineeringTable.drawTexturedModalRect(j + 26, k + 3, 64 * armorType.getIndex(), 0, 64, 64);
    }

    @Override
    public ResourceLocation getItemIconResource() {
        return new ResourceLocation(EI_MODID, "items/leather_exosuit/" + armorType.getName());
    }

    @Override
    public boolean hasUpgrade(ItemStack me, Item check) {
        NBTTagCompound plateNBT = getPlateNBT(me);
        return plateNBT != null && ItemStack.loadItemStackFromNBT(plateNBT.getCompoundTag("Stack")).getItem() == check;
    }

    @Nonnull
    @Override
    public ExosuitUpgrade[] getUpgrades(ItemStack self) {
        return new ExosuitUpgrade[0];
    }

    @Nonnull
    @Override
    public ExosuitEventHandler[] getInstalledEventHandlers(ItemStack self) {
        NBTTagCompound plateNBT = getPlateNBT(self);
        if (plateNBT != null) {
            ExosuitPlate plate = UtilPlates.getPlate(plateNBT.getString("ID"));
            return new ExosuitEventHandler[] { plate };
        }
        return new ExosuitEventHandler[0];
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
        return null;
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        NBTTagCompound plateNBT = getPlateNBT(armor);
        if (plateNBT != null) {
            return UtilPlates.getPlate(plateNBT.getString("ID")).getDamageReductionAmount(armorType, DamageSource.generic);
        }
        return ArmorMaterial.LEATHER.getDamageReductionAmount(armorType);
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
        NBTTagCompound plateNBT = getPlateNBT(stack);
        if (plateNBT != null) {
            ExosuitPlate plate = UtilPlates.getPlate(plateNBT.getString("ID"));
            int damageReduction = plate.getDamageReductionAmount(armorType, source);
            if ((entity.worldObj.rand.nextInt(20 - damage) + 1) > damageReduction) {
                if (plateNBT.hasKey("Damage")) {
                    plateNBT.setInteger("Damage", plateNBT.getInteger("Damage") - 1);
                } else {
                    plateNBT.setInteger("Damage", plate.getDamageReductionAmount(armorType, DamageSource.generic));
                }
            }
        }
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        if (!(entityLiving instanceof EntityPlayer)) {
            return _default;
        }

        ModelLeatherExosuit modelExosuit = (ModelLeatherExosuit) LeatherExosuitModelCache.INSTANCE.getModel((EntityPlayer) entityLiving, armorSlot);

        modelExosuit.showHead(armorSlot == EntityEquipmentSlot.HEAD);
        modelExosuit.showChest(armorSlot == EntityEquipmentSlot.CHEST);
        modelExosuit.showLegs(armorSlot == EntityEquipmentSlot.LEGS);
        modelExosuit.showBoots(armorSlot == EntityEquipmentSlot.FEET);

        return modelExosuit;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return MODEL_TEXTURE.toString();
    }
}
