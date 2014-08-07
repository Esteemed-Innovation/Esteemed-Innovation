package flaxbeard.steamcraft.integration;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
//import tconstruct.TConstruct;
//import tconstruct.common.TRepo;
//import tconstruct.library.TConstructRegistry;
//import tconstruct.library.crafting.CastingRecipe;
//import tconstruct.library.crafting.LiquidCasting;
import flaxbeard.steamcraft.SteamcraftItems;

public class TinkersIntegration {
	/*
	public static void registerRecipes(String str, ItemStack plate, ItemStack nugget) {
		 LiquidCasting tableCasting = TConstructRegistry.instance.getTableCasting();
	     ItemStack scPlatecast = new ItemStack(SteamcraftItems.plateMold, 1, 0);
	     ItemStack scNuggetcast = new ItemStack(SteamcraftItems.plateMold, 1, 0);
		 if (FluidRegistry.isFluidRegistered(str + ".molten")) {
			 Fluid fluid = FluidRegistry.getFluid(str + ".molten");
		     tableCasting.addCastingRecipe(plate, new FluidStack(fluid, (int) (TConstruct.ingotLiquidValue*0.6666F)), scPlatecast, 34);
		     tableCasting.addCastingRecipe(nugget, new FluidStack(fluid, (int) (TConstruct.ingotLiquidValue/9.0F)), scNuggetcast, 5);
		 }
	}

	public static void addIngotRecipes() {
		LiquidCasting tableCasting = TConstructRegistry.instance.getTableCasting();
        ItemStack ingotcast = new ItemStack(TRepo.metalPattern, 1, 0);
        ItemStack scIngotcast = new ItemStack(SteamcraftItems.ingotMold, 1, 0);
		ArrayList<CastingRecipe> recipes = tableCasting.getCastingRecipes();
		ArrayList<CastingRecipe> goodRecipes = new ArrayList<CastingRecipe>();
		for (CastingRecipe recipe : recipes) {
			if (recipe.cast != null && recipe.cast.isItemEqual(ingotcast)) {
				goodRecipes.add(recipe);
			}
		}
		for (CastingRecipe recipe : goodRecipes) {
			//Steamcraft.log.debug("INTEGRATION YUCK JOE");
	        tableCasting.addCastingRecipe(recipe.output, recipe.castingMetal, scIngotcast, recipe.coolTime);
		}
	}*/
}
