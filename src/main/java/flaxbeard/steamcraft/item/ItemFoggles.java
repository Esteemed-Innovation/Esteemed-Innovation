package flaxbeard.steamcraft.item;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import flaxbeard.steamcraft.api.exosuit.ExosuitSlot;
import flaxbeard.steamcraft.api.exosuit.IExosuitUpgrade;
import flaxbeard.steamcraft.api.exosuit.ModelExosuitUpgrade;
import flaxbeard.steamcraft.item.tool.ItemSteamcraftArmor;

import java.util.List;

public class ItemFoggles extends ItemSteamcraftArmor implements IExosuitUpgrade {
    public ItemFoggles(ArmorMaterial armorMat, int renderIndex, int armorType, Object repair, String n) {
        super(armorMat, renderIndex, armorType, repair, n);
    }

    @Override
    public int renderPriority() {
        // TODO
        return 0;
    }

    @Override
    public ExosuitSlot getSlot() {
        return ExosuitSlot.headGoggles;
    }

    @Override
    public ResourceLocation getOverlay() {
        // TODO
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
}
