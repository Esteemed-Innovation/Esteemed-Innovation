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
	
	private IC2NaturaIntegration() throws InstantiationException{
		throw new InstantiationException("This class is not meant to be instantiated");
	}
	
	public static void addIC2Recipes() {
		Recipes.cannerBottle.addRecipe(IndustrialCraftIntegration.EMPTY_X10,
		  new IC2RecipeInput(new ItemStack(NaturaIntegration.steamedImphide)),
		  IndustrialCraftIntegration.FILLED_X10);
	}
}
