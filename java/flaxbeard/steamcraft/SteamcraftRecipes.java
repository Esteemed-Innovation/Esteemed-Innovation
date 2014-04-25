package flaxbeard.steamcraft;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;
import flaxbeard.steamcraft.api.CrucibleFormula;
import flaxbeard.steamcraft.api.CrucibleLiquid;
import flaxbeard.steamcraft.api.SteamcraftRegistry;

public class SteamcraftRecipes {
    
    public static CrucibleLiquid liquidIron;
    public static CrucibleLiquid liquidZinc;
    public static CrucibleLiquid liquidCopper;
    public static CrucibleLiquid liquidGold;
    public static CrucibleLiquid liquidBrass;
	
    
	public static void registerRecipes() {
		registerCraftingRecipes();
		registerFluid();
	}

	private static void registerFluid() {
		liquidIron = new CrucibleLiquid("iron", new ItemStack(Items.iron_ingot), new ItemStack(SteamcraftItems.steamcraftPlate,1,2), new ItemStack(SteamcraftItems.steamcraftNugget,1,2), null,200,200,200);
		SteamcraftRegistry.liquids.add(liquidIron);
		liquidGold = new CrucibleLiquid("gold", new ItemStack(Items.gold_ingot), new ItemStack(SteamcraftItems.steamcraftPlate,1,3), new ItemStack(Items.gold_nugget), null,220,157,11);
		SteamcraftRegistry.liquids.add(liquidGold);
		liquidZinc = new CrucibleLiquid("zinc", new ItemStack(SteamcraftItems.steamcraftIngot,1,1), new ItemStack(SteamcraftItems.steamcraftPlate,1,1), new ItemStack(SteamcraftItems.steamcraftNugget,1,1), null,225,225,225);
		SteamcraftRegistry.liquids.add(liquidZinc);
		liquidCopper = new CrucibleLiquid("copper", new ItemStack(SteamcraftItems.steamcraftIngot,1,0), new ItemStack(SteamcraftItems.steamcraftPlate,1,0), new ItemStack(SteamcraftItems.steamcraftNugget,1,0), null,140,66,12);
		SteamcraftRegistry.liquids.add(liquidCopper);
		liquidBrass = new CrucibleLiquid("brass", new ItemStack(SteamcraftItems.steamcraftIngot,1,2), new ItemStack(SteamcraftItems.steamcraftPlate,1,4), new ItemStack(SteamcraftItems.steamcraftNugget,1,3), new CrucibleFormula(liquidZinc, 1, liquidCopper, 3, 4),242,191,66);
		SteamcraftRegistry.liquids.add(liquidBrass);
		
		SteamcraftRegistry.registerSmeltThingOredict("ingotGold", liquidGold, 9);
		SteamcraftRegistry.registerSmeltThingOredict("ingotIron", liquidIron, 9);
		SteamcraftRegistry.registerSmeltThingOredict("ingotZinc", liquidZinc, 9);
		SteamcraftRegistry.registerSmeltThingOredict("ingotCopper", liquidCopper, 9);
		SteamcraftRegistry.registerSmeltThingOredict("ingotBrass", liquidBrass, 9);
		
		SteamcraftRegistry.registerSmeltThingOredict("plateGold", liquidGold, 6);
		SteamcraftRegistry.registerSmeltThingOredict("plateIron", liquidIron, 6);
		SteamcraftRegistry.registerSmeltThingOredict("plateZinc", liquidZinc, 6);
		SteamcraftRegistry.registerSmeltThingOredict("plateCopper", liquidCopper, 6);
		SteamcraftRegistry.registerSmeltThingOredict("plateBrass", liquidBrass, 6);
		
		SteamcraftRegistry.registerSmeltThingOredict("nuggetGold", liquidGold, 1);
		SteamcraftRegistry.registerSmeltThingOredict("nuggetIron", liquidIron, 1);
		SteamcraftRegistry.registerSmeltThingOredict("nuggetZinc", liquidZinc, 1);
		SteamcraftRegistry.registerSmeltThingOredict("nuggetCopper", liquidCopper, 1);
		SteamcraftRegistry.registerSmeltThingOredict("nuggetBrass", liquidBrass, 1);
		SteamcraftRegistry.registerSmeltThing(Items.gold_nugget, liquidGold, 1);
		
		SteamcraftRegistry.registerSmeltTool(Items.iron_sword, liquidIron, 18);
		
		SteamcraftRegistry.registerDunkThing(Items.iron_ingot, liquidGold, 1, new ItemStack(SteamcraftItems.steamcraftIngot,1,3));
	}

	
	private static void registerCraftingRecipes() {
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(SteamcraftItems.book), Items.book, "oreZinc", "oreCopper"));
		
		GameRegistry.addRecipe(new ItemStack(SteamcraftBlocks.crucible), "x x", "x x", "xxx", 
		        'x', Items.brick);
		GameRegistry.addRecipe(new ItemStack(SteamcraftBlocks.mold), "xxx", "x x", "xxx", 
		        'x', Items.brick);
		GameRegistry.addRecipe(new ItemStack(SteamcraftItems.blankMold), "xx", 
		        'x', Items.brick);
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.carving), "xzx", "x x", "xxx", 
		        'x', "plankWood", 'z', SteamcraftItems.blankMold));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.boiler), "xxx", "xfx", "xxx", 
		        'x', "ingotBrass", 'f', Blocks.furnace));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.boiler), "xxx", "xfx", "xxx", 
		        'x', "plateBrass", 'f', Blocks.furnace));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.pipe,4,0), "xxx", "   ", "xxx", 
		        'x', "ingotBrass"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.pipe,4,0), "xxx", "   ", "xxx", 
		        'x', "plateBrass"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.heater), "ccc", "xfx", " p ", 
		        'x', "ingotBrass", 'c', "nuggetCopper", 'f', Blocks.furnace,'p', SteamcraftBlocks.pipe));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.heater), "ccc", "xfx", " p ", 
		        'x', "plateBrass", 'c', "nuggetCopper", 'f', Blocks.furnace,'p', SteamcraftBlocks.pipe));
		
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(SteamcraftItems.steamcraftNugget, 9, 0), "ingotCopper"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(SteamcraftItems.steamcraftNugget, 9, 1), "ingotZinc"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(SteamcraftItems.steamcraftNugget, 9, 1), "ingotIron"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(SteamcraftItems.steamcraftNugget, 9, 1), new ItemStack(Items.iron_ingot)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(SteamcraftItems.steamcraftNugget, 9, 3), "ingotBrass"));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamcraftIngot, 1, 0), "xxx", "xxx", "xxx", 
		        'x', "nuggetCopper"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamcraftIngot, 1, 1), "xxx", "xxx", "xxx", 
		        'x', "nuggetZinc"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.iron_ingot), "xxx", "xxx", "xxx", 
		        'x', "nuggetIron"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamcraftIngot, 1, 2), "xxx", "xxx", "xxx", 
		        'x', "nuggetBrass"));

	}
}
