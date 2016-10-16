package eiteam.esteemedinnovation.api.exosuit;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * The interface which allows an item to be upgraded into an Exosuit armor piece.
 * You must return something unique from {@link Object#toString()}, otherwise the exosuit will not render correctly as
 * an item (has no effect on the armor rendering). The result of {@link IExosuitUpgrade#getOverlay()} is usually
 * returned for toString().
 */
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

    /**
     * The texture overlay *for the armor model*. This is not for the item texture/model.
     * @return ResourceLocation or null if it does not add a texture overlay for the armor.
     */
    ResourceLocation getOverlay();

    Class<? extends ModelExosuitUpgrade> getModel();

    void updateModel(ModelBiped parentModel, EntityLivingBase entityLivingBase, ItemStack itemStack, ModelExosuitUpgrade modelExosuitUpgrade);

    void writeInfo(List<String> list);
}
