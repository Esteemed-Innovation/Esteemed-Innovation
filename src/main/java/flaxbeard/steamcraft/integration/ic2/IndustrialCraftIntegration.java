package flaxbeard.steamcraft.integration.ic2;

import ic2.api.item.IC2Items;
import ic2.api.recipe.Recipes;
import net.minecraft.item.ItemStack;
import flaxbeard.steamcraft.SteamcraftItems;

public class IndustrialCraftIntegration {
	public final static IC2RecipeInput EMPTY_X10 = new IC2RecipeInput(new ItemStack(IC2Items.getItem("tinCan").getItem(), 10));
	public final static IC2RecipeInput EMPTY_X8 = new IC2RecipeInput(new ItemStack(IC2Items.getItem("tinCan").getItem(), 8));
	public final static IC2RecipeInput EMPTY_X7 = new IC2RecipeInput(new ItemStack(IC2Items.getItem("tinCan").getItem(), 7));
	
	public final static ItemStack FILLED_X10 = new ItemStack(IC2Items.getItem("filledTinCan").getItem(), 10);
	public final static ItemStack FILLED_X8 = new ItemStack(IC2Items.getItem("filledTinCan").getItem(), 8);
	public final static ItemStack FILLED_X7 = new ItemStack(IC2Items.getItem("filledTinCan").getItem(), 7);
	
	private IndustrialCraftIntegration() throws InstantiationException{
		throw new InstantiationException("This class is not meant to be instantiated");
	}
	
	public static void postInit() {
		Recipes.cannerBottle.addRecipe(EMPTY_X10, new IC2RecipeInput(new ItemStack(SteamcraftItems.steamedBeef)), FILLED_X10);
		Recipes.cannerBottle.addRecipe(EMPTY_X8, new IC2RecipeInput(new ItemStack(SteamcraftItems.steamedChicken)), FILLED_X8);
		Recipes.cannerBottle.addRecipe(EMPTY_X7, new IC2RecipeInput(new ItemStack(SteamcraftItems.steamedFish)), FILLED_X7);
		Recipes.cannerBottle.addRecipe(EMPTY_X10, new IC2RecipeInput(new ItemStack(SteamcraftItems.steamedPorkchop)), FILLED_X10);
	}
}
