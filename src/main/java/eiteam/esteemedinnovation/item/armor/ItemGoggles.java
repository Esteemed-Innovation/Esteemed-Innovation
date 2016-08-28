package eiteam.esteemedinnovation.item.armor;

import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.api.exosuit.IExosuitUpgrade;
import eiteam.esteemedinnovation.api.exosuit.ModelExosuitUpgrade;

import net.minecraft.client.model.ModelBiped;
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
        return new ResourceLocation(EsteemedInnovation.MOD_ID + ":textures/models/armor/" + this.name.substring(0, 1).toLowerCase() + this.name.substring(1) + "Exosuit.png");
    }

    @Override
    public Class<? extends ModelExosuitUpgrade> getModel() {
        return null;
    }

    @Override
    public void updateModel(ModelBiped parentModel, EntityLivingBase entityLivingBase, ItemStack itemStack, ModelExosuitUpgrade modelExosuitUpgrade) {

    }

    @Override
    public void writeInfo(List list) {
    }
}
