package flaxbeard.steamcraft.integration.natura;

import ic2.api.item.IC2Items;
import ic2.api.recipe.Recipes;
import net.minecraft.item.ItemStack;
import flaxbeard.steamcraft.integration.ic2.IC2RecipeInput;
import flaxbeard.steamcraft.integration.ic2.IndustrialCraftIntegration;

/**
 * Ain't dat crazy?
 * 
 * @author xbony2
 */
public class IC2NaturaIntegration {
	public static void addIC2Recipes(){
		IC2RecipeInput input1 = new IC2RecipeInput(new ItemStack(NaturaIntegration.steamedImphide));
		Recipes.cannerBottle.addRecipe(IndustrialCraftIntegration.tincanx10, input1, new ItemStack(IC2Items.getItem("filledTinCan").getItem(), 10));
	}
}
