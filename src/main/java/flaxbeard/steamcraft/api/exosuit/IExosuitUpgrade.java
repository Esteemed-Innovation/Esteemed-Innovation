package flaxbeard.steamcraft.api.exosuit;

import flaxbeard.steamcraft.client.render.model.ModelExosuit;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public interface IExosuitUpgrade {


    public int renderPriority();

    public ExosuitSlot getSlot();

    public boolean hasOverlay();

    public ResourceLocation getOverlay();

    public boolean hasModel();

    public void renderModel(ModelExosuit model, Entity par1Entity, int armor, float par7, ItemStack me);

    public void writeInfo(List list);
}
