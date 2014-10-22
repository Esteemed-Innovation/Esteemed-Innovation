package flaxbeard.steamcraft.api.exosuit;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public interface IExosuitUpgrade {

    public int renderPriority();

    public ExosuitSlot getSlot();

    public ResourceLocation getOverlay();

    public Class<? extends ModelExosuitUpgrade> getModel();

    public void updateModel(ModelBiped parentModel, EntityLivingBase entityLivingBase, ItemStack itemStack, ModelExosuitUpgrade modelExosuitUpgrade);

    public void writeInfo(List list);
}
