package flaxbeard.steamcraft.integration.ic2;

import ic2.api.item.IC2Items;
import ic2.api.recipe.Recipes;
import net.minecraft.item.ItemStack;
import flaxbeard.steamcraft.SteamcraftItems;

public class IndustrialCraftIntegration {
	public static void addIC2Recipes(){
		IC2RecipeInput tincanx10 = new IC2RecipeInput(new ItemStack(IC2Items.getItem("tinCan").getItem(), 10));
		IC2RecipeInput tincanx8 = new IC2RecipeInput(new ItemStack(IC2Items.getItem("tinCan").getItem(), 8));
		IC2RecipeInput tincanx7 = new IC2RecipeInput(new ItemStack(IC2Items.getItem("tinCan").getItem(), 7));
		
		IC2RecipeInput input1 = new IC2RecipeInput(new ItemStack(SteamcraftItems.steamedBeef));
		Recipes.cannerBottle.addRecipe(tincanx10, input1, new ItemStack(IC2Items.getItem("filledTinCan").getItem(), 10));
		
		IC2RecipeInput input2 = new IC2RecipeInput(new ItemStack(SteamcraftItems.steamedChicken));
		Recipes.cannerBottle.addRecipe(tincanx8, input2, new ItemStack(IC2Items.getItem("filledTinCan").getItem(), 8));
		
		IC2RecipeInput input3 = new IC2RecipeInput(new ItemStack(SteamcraftItems.steamedFish));
		Recipes.cannerBottle.addRecipe(tincanx7, input3, new ItemStack(IC2Items.getItem("filledTinCan").getItem(), 7));
		
		IC2RecipeInput input4 = new IC2RecipeInput(new ItemStack(SteamcraftItems.steamedPorkchop));
		Recipes.cannerBottle.addRecipe(tincanx10, input4, new ItemStack(IC2Items.getItem("filledTinCan").getItem(), 10));
	}
}
