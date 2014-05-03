package flaxbeard.steamcraft.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import flaxbeard.steamcraft.api.CrucibleLiquid;
import flaxbeard.steamcraft.api.ICrucibleMold;

public class ItemNuggetMold extends Item implements ICrucibleMold {
	
	
	private ResourceLocation icon = new ResourceLocation("steamcraft:textures/models/moldNugget.png");

	@Override
	public ResourceLocation getBlockTexture() {
		return icon;
	}
	
	public ItemNuggetMold() {
		 this.maxStackSize = 1;
	}
	
	@Override
	public boolean canUseOn(CrucibleLiquid liquid) {
		return (liquid.nugget != null);
	}

	@Override
	public ItemStack getItemFromLiquid(CrucibleLiquid liquid) {
		ItemStack newStack = liquid.nugget.copy();
		newStack.stackSize = 1;
		return newStack;
	}
	
	@Override
	public int getCostToMold(CrucibleLiquid liquid) {
		return 1;
	}
}
