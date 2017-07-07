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
            ItemStack plateStack = new ItemStack(plateNBT.getCompoundTag("Stack"));
            if (!plateStack.isEmpty()) {
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

    @Nonnull
    @Override
    public ItemStack getStackInSlot(@Nonnull ItemStack me, int slot) {
        NBTTagCompound plateNBT = getPlateNBT(me);
        if (plateNBT != null) {
            return new ItemStack(plateNBT.getCompoundTag("Stack"));
        }
        return ItemStack.EMPTY;
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
    public void setInventorySlotContents(@Nonnull ItemStack me, int slot, @Nonnull ItemStack stack) {
        if (stack.isEmpty() && me.hasTagCompound()) {
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
    public boolean isItemValidForSlot(@Nonnull ItemStack me, int slot, @Nonnull ItemStack var2) {
        return true;
    }

    @Nonnull
    @Override
    public ItemStack decrStackSize(@Nonnull ItemStack me, int slot, int size) {
        NBTTagCompound plateNBT = getPlateNBT(me);
        if (plateNBT != null) {
            ItemStack toBeRemoved = new ItemStack(plateNBT.getCompoundTag("Stack"));
            me.getTagCompound().removeTag("Plate");
            return toBeRemoved;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void drawSlot(GuiContainer guiEngineeringTable, int slotnum, int i, int j) {
        guiEngineeringTable.mc.getTextureManager().bindTexture(Constants.ENG_GUI_TEXTURES);
        guiEngineeringTable.drawTexturedModalRect(i, j, 194, 0, 18, 18);
    }

    @Override
    public boolean canPutInSlot(@Nonnull ItemStack me, int slotNum, @Nonnull ItemStack upgrade) {
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
    public boolean hasUpgrade(@Nonnull ItemStack me, Item check) {
        NBTTagCompound plateNBT = getPlateNBT(me);
        return plateNBT != null && new ItemStack(plateNBT.getCompoundTag("Stack")).getItem() == check;
    }

    @Nonnull
    @Override
    public ExosuitUpgrade[] getUpgrades(@Nonnull ItemStack self) {
        return new ExosuitUpgrade[0];
    }

    @Nonnull
    @Override
    public ExosuitEventHandler[] getInstalledEventHandlers(@Nonnull ItemStack self) {
        NBTTagCompound plateNBT = getPlateNBT(self);
        if (plateNBT != null) {
            ExosuitPlate plate = UtilPlates.getPlate(plateNBT.getString("ID"));
            return new ExosuitEventHandler[] { plate };
        }
        return new ExosuitEventHandler[0];
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, @Nonnull ItemStack armor, DamageSource source, double damage, int slot) {
        return null;
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, @Nonnull ItemStack armor, int slot) {
        NBTTagCompound plateNBT = getPlateNBT(armor);
        if (plateNBT != null) {
            return UtilPlates.getPlate(plateNBT.getString("ID")).getDamageReductionAmount(armorType, DamageSource.GENERIC);
        }
        return ArmorMaterial.LEATHER.getDamageReductionAmount(armorType);
    }

    @Override
    public void damageArmor(EntityLivingBase entity, @Nonnull ItemStack stack, DamageSource source, int damage, int slot) {
        NBTTagCompound plateNBT = getPlateNBT(stack);
        if (plateNBT != null) {
            ExosuitPlate plate = UtilPlates.getPlate(plateNBT.getString("ID"));
            int damageReduction = plate.getDamageReductionAmount(armorType, source);
            if ((entity.world.rand.nextInt(20 - damage) + 1) > damageReduction) {
                if (plateNBT.hasKey("Damage")) {
                    plateNBT.setInteger("Damage", plateNBT.getInteger("Damage") - 1);
                } else {
                    plateNBT.setInteger("Damage", plate.getDamageReductionAmount(armorType, DamageSource.GENERIC));
                }
            }
        }
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, @Nullable ItemStack repair) {
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
