package eiteam.esteemedinnovation.armor;

import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.api.exosuit.IExosuitUpgrade;
import eiteam.esteemedinnovation.api.exosuit.ModelExosuitUpgrade;
import eiteam.esteemedinnovation.init.items.armor.ArmorItems;
import eiteam.esteemedinnovation.commons.util.JavaHelper;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class ItemGoggles extends ItemGenericArmor implements IExosuitUpgrade {

    public ItemGoggles(ArmorMaterial armorMaterial, int renderIndex, EntityEquipmentSlot armorType, Object repair, String n) {
        super(armorMaterial, renderIndex, armorType, repair, n);
    }

    @Override
    public int renderPriority() {
        return 0;
    }

    @Override
    public ExosuitSlot getSlot() {
        return ExosuitSlot.HEAD_GOGGLES;
    }

    @Override
    public ResourceLocation getOverlay() {
        return new ResourceLocation(EsteemedInnovation.MOD_ID + ":textures/models/armor/" + JavaHelper.decapitalize(name) + "Exosuit.png");
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        if (stack.getItem() == ArmorItems.Items.GOGGLES.getItem()) {
            return EsteemedInnovation.MOD_ID + ":textures/models/armor/" + JavaHelper.decapitalize(name) + "_layer_1.png";
        }
        return null;
    }

    @Override
    public Class<? extends ModelExosuitUpgrade> getModel() {
        return null;
    }

    @Override
    public void updateModel(ModelBiped parentModel, EntityLivingBase entityLivingBase, ItemStack itemStack, ModelExosuitUpgrade modelExosuitUpgrade) {}

    @Override
    public void writeInfo(List list) {}

    @Override
    public String toString() {
        return getOverlay().toString();
    }
}
