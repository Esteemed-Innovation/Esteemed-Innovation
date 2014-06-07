package flaxbeard.steamcraft.api.exosuit;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import flaxbeard.steamcraft.client.render.ModelExosuit;
import flaxbeard.steamcraft.item.ItemExosuitArmor;

public interface IExosuitUpgrade {
	public int renderPriority();
	public ItemExosuitArmor.ExosuitSlot getSlot();
	public boolean hasOverlay();
	public ResourceLocation getOverlay();
	
	public boolean hasModel();
	public void renderModel(ModelExosuit model, Entity par1Entity, int armor, float par7, ItemStack me);
	public void writeInfo(List list);
}
