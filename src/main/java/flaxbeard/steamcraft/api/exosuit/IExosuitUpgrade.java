package flaxbeard.steamcraft.api.exosuit;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public interface IExosuitUpgrade {
    /**
     * The priority of the upgrade's rendering
     *
     * Use 1 if it should overwrite the exosuit's rendering
     * Use 0 if it should not overwrite the exosuit's rendering
     */
    int renderPriority();

    /**
     * The slot that the upgrade can be used on
     *
     * See ExosuitSlot.java for the list of slots.
     */
    ExosuitSlot getSlot();

    ResourceLocation getOverlay();

    Class<? extends ModelExosuitUpgrade> getModel();

    void updateModel(ModelBiped parentModel, EntityLivingBase entityLivingBase, ItemStack itemStack, ModelExosuitUpgrade modelExosuitUpgrade);

    void writeInfo(List list);
}
