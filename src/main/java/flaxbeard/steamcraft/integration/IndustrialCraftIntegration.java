package flaxbeard.steamcraft.integration;

import ic2.api.item.IC2Items;
import ic2.api.recipe.Recipes;
import net.minecraft.item.ItemStack;
import flaxbeard.steamcraft.SteamcraftItems;

public class IndustrialCraftIntegration {
	public static void addIC2Recipes(){
		IC2RecipeInput tincan = new IC2RecipeInput(IC2Items.getItem("tinCan"));
		ItemStack filled = IC2Items.getItem("filledTinCan");
		
		IC2RecipeInput input1 = new IC2RecipeInput(new ItemStack(SteamcraftItems.steamedBeef));
		Recipes.cannerBottle.addRecipe(tincan, input1, filled);
		
		IC2RecipeInput input2 = new IC2RecipeInput(new ItemStack(SteamcraftItems.steamedChicken));
		Recipes.cannerBottle.addRecipe(tincan, input2, filled);
		
		IC2RecipeInput input3 = new IC2RecipeInput(new ItemStack(SteamcraftItems.steamedFish));
		Recipes.cannerBottle.addRecipe(tincan, input3, filled);
		
		IC2RecipeInput input4 = new IC2RecipeInput(new ItemStack(SteamcraftItems.steamedPorkchop));
		Recipes.cannerBottle.addRecipe(tincan, input4, filled);
	}
}
