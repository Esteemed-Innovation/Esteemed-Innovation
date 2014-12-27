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
    public int renderPriority();

    /**
     * The slot that the upgrade can be used on
     *
     * See ExosuitSlot.java for the list of slots.
     */
    public ExosuitSlot getSlot();

    public ResourceLocation getOverlay();

    public Class<? extends ModelExosuitUpgrade> getModel();

    public void updateModel(ModelBiped parentModel, EntityLivingBase entityLivingBase, ItemStack itemStack, ModelExosuitUpgrade modelExosuitUpgrade);

    public void writeInfo(List list);
}
