package eiteam.esteemedinnovation.armor;

import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.api.exosuit.ExosuitUpgrade;
import eiteam.esteemedinnovation.api.exosuit.ModelExosuitUpgrade;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.api.util.StringUtility;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class ItemGoggles extends ItemGenericArmor implements ExosuitUpgrade {

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
        return new ResourceLocation(EsteemedInnovation.MOD_ID + ":textures/models/armor/" + StringUtility.decapitalize(name) + "Exosuit.png");
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        if (stack.getItem() == ArmorModule.GOGGLES) {
            return EsteemedInnovation.MOD_ID + ":textures/models/armor/" + StringUtility.decapitalize(name) + "_layer_1.png";
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
