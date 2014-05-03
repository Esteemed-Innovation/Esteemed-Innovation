package flaxbeard.steamcraft.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public interface ICrucibleMold {
	public ResourceLocation getBlockTexture();
	
	public boolean canUseOn(CrucibleLiquid liquid);

	public ItemStack getItemFromLiquid(CrucibleLiquid liquid);

	public int getCostToMold(CrucibleLiquid liquid);
}
