package flaxbeard.steamcraft;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import flaxbeard.steamcraft.api.CrucibleFormula;
import flaxbeard.steamcraft.api.CrucibleLiquid;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.book.BookRecipeRegistry;
import flaxbeard.steamcraft.item.ItemSmashedOre;

public class SteamcraftRecipes {
    
    public static CrucibleLiquid liquidIron;
    public static CrucibleLiquid liquidZinc;
    public static CrucibleLiquid liquidCopper;
    public static CrucibleLiquid liquidGold;
    public static CrucibleLiquid liquidBrass;
	
    
	public static void registerRecipes() {
		registerFluid();
		registerCraftingRecipes();
		registerSmeltingRecipes();
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
		

//		if (Loader.isModLoaded("TConstruct")) {
//			System.out.println("INTEGRATION FUCK YEAH");
//			TinkersIntegration.addIngotRecipes();
//		}
		
		SteamcraftRegistry.registerSmeltThingOredict("ingotGold", liquidGold, 9);
		SteamcraftRegistry.registerSmeltThingOredict("ingotIron", liquidIron, 9);
		SteamcraftRegistry.registerSmeltThingOredict("ingotZinc", liquidZinc, 9);
		SteamcraftRegistry.registerSmeltThingOredict("ingotCopper", liquidCopper, 9);
		SteamcraftRegistry.registerSmeltThingOredict("ingotBrass", liquidBrass, 9);
		
		SteamcraftRegistry.registerSmeltThingOredict("dustGold", liquidGold, 9);
		SteamcraftRegistry.registerSmeltThingOredict("dustIron", liquidIron, 9);
		SteamcraftRegistry.registerSmeltThingOredict("dustZinc", liquidZinc, 9);
		SteamcraftRegistry.registerSmeltThingOredict("dustCopper", liquidCopper, 9);
		SteamcraftRegistry.registerSmeltThingOredict("dustBrass", liquidBrass, 9);
		
		SteamcraftRegistry.registerSmeltThingOredict("dustTinyGold", liquidGold, 1);
		SteamcraftRegistry.registerSmeltThingOredict("dustTinyIron", liquidIron, 1);
		SteamcraftRegistry.registerSmeltThingOredict("dustTinyZinc", liquidZinc, 1);
		SteamcraftRegistry.registerSmeltThingOredict("dustTinyCopper", liquidCopper, 1);
		SteamcraftRegistry.registerSmeltThingOredict("dustTinyBrass", liquidBrass, 1);
		
		SteamcraftRegistry.registerSmeltThingOredict("plateSteamcraftGold", liquidGold, 6);
		SteamcraftRegistry.registerSmeltThingOredict("plateSteamcraftIron", liquidIron, 6);
		SteamcraftRegistry.registerSmeltThingOredict("plateSteamcraftZinc", liquidZinc, 6);
		SteamcraftRegistry.registerSmeltThingOredict("plateSteamcraftCopper", liquidCopper, 6);
		SteamcraftRegistry.registerSmeltThingOredict("plateSteamcraftBrass", liquidBrass, 6);
		
		SteamcraftRegistry.registerSmeltThingOredict("nuggetGold", liquidGold, 1);
		SteamcraftRegistry.registerSmeltThingOredict("nuggetIron", liquidIron, 1);
		SteamcraftRegistry.registerSmeltThingOredict("nuggetZinc", liquidZinc, 1);
		SteamcraftRegistry.registerSmeltThingOredict("nuggetCopper", liquidCopper, 1);
		SteamcraftRegistry.registerSmeltThingOredict("nuggetBrass", liquidBrass, 1);
		SteamcraftRegistry.registerSmeltThing(Items.gold_nugget, liquidGold, 1);
		
		SteamcraftRegistry.registerSmeltTool(Items.iron_sword, liquidIron, 18);
		SteamcraftRegistry.registerSmeltTool(Items.iron_pickaxe, liquidIron, 27);
		SteamcraftRegistry.registerSmeltTool(Items.iron_axe, liquidIron, 27);
		SteamcraftRegistry.registerSmeltTool(Items.iron_hoe, liquidIron, 18);
		SteamcraftRegistry.registerSmeltTool(Items.iron_shovel, liquidIron, 9);
		SteamcraftRegistry.registerSmeltTool(Items.iron_boots, liquidIron, 36);
		SteamcraftRegistry.registerSmeltTool(Items.iron_chestplate, liquidIron, 81);
		SteamcraftRegistry.registerSmeltTool(Items.iron_helmet, liquidIron, 45);
		SteamcraftRegistry.registerSmeltTool(Items.iron_leggings, liquidIron, 63);	
		
		SteamcraftRegistry.registerSmeltTool(Items.golden_sword, liquidGold, 18);
		SteamcraftRegistry.registerSmeltTool(Items.golden_pickaxe, liquidGold, 27);
		SteamcraftRegistry.registerSmeltTool(Items.golden_axe, liquidGold, 27);
		SteamcraftRegistry.registerSmeltTool(Items.golden_hoe, liquidGold, 18);
		SteamcraftRegistry.registerSmeltTool(Items.golden_shovel, liquidGold, 9);
		SteamcraftRegistry.registerSmeltTool(Items.golden_boots, liquidGold, 36);
		SteamcraftRegistry.registerSmeltTool(Items.golden_chestplate, liquidGold, 81);
		SteamcraftRegistry.registerSmeltTool(Items.golden_helmet, liquidGold, 45);
		SteamcraftRegistry.registerSmeltTool(Items.golden_leggings, liquidGold, 63);
		
		SteamcraftRegistry.registerSmeltTool(SteamcraftItems.sword("Brass"), liquidBrass, 18);
		SteamcraftRegistry.registerSmeltTool(SteamcraftItems.pick("Brass"), liquidBrass, 27);
		SteamcraftRegistry.registerSmeltTool(SteamcraftItems.axe("Brass"), liquidBrass, 27);
		SteamcraftRegistry.registerSmeltTool(SteamcraftItems.hoe("Brass"), liquidBrass, 18);
		SteamcraftRegistry.registerSmeltTool(SteamcraftItems.shovel("Brass"), liquidBrass, 9);
		SteamcraftRegistry.registerSmeltTool(SteamcraftItems.feet("Brass"), liquidBrass, 36);
		SteamcraftRegistry.registerSmeltTool(SteamcraftItems.chest("Brass"), liquidBrass, 81);
		SteamcraftRegistry.registerSmeltTool(SteamcraftItems.helm("Brass"), liquidBrass, 45);
		SteamcraftRegistry.registerSmeltTool(SteamcraftItems.legs("Brass"), liquidBrass, 63);

		
		SteamcraftRegistry.registerDunkThing(Items.iron_ingot, liquidGold, 1, new ItemStack(SteamcraftItems.steamcraftIngot,1,3));
	}
	
	
	private static void registerSmeltingRecipes() {
		GameRegistry.addSmelting(new ItemStack(SteamcraftBlocks.steamcraftOre,1,0), new ItemStack(SteamcraftItems.steamcraftIngot,1,0), 0.5F);
		GameRegistry.addSmelting(new ItemStack(SteamcraftBlocks.steamcraftOre,1,1), new ItemStack(SteamcraftItems.steamcraftIngot,1,1), 0.5F);
		if (Loader.isModLoaded("Railcraft")) {
			GameRegistry.addSmelting(new ItemStack(SteamcraftBlocks.steamcraftOre,1,2), new ItemStack(SteamcraftItems.steamcraftNugget,1,1), 0.5F);
		}
	}
	
	private static void registerCraftingRecipes() {
		BookRecipeRegistry.addRecipe("book",new ShapelessOreRecipe(new ItemStack(SteamcraftItems.book), Items.book, "oreZinc", "oreCopper"));

		BookRecipeRegistry.addRecipe("crucible",new ItemStack(SteamcraftBlocks.crucible), "x x", "x x", "xxx", 
		        'x', Items.brick);
		BookRecipeRegistry.addRecipe("mold",new ItemStack(SteamcraftBlocks.mold), "xxx", "xxx", 
		        'x', Items.brick);
		BookRecipeRegistry.addRecipe("blankMold",new ItemStack(SteamcraftItems.blankMold), "xx", 
		        'x', Items.brick);
		
		BookRecipeRegistry.addRecipe("survivalist",new ItemStack(SteamcraftItems.survivalist), "b s", "xwx", "xxx", 
		        'x', Items.leather, 's', Items.string, 'b', Items.brick, 'w', Items.stick);
		
		BookRecipeRegistry.addRecipe("astrolabe",new ShapedOreRecipe(new ItemStack(SteamcraftItems.astrolabe), " x ", "xrx", " x ", 
		        'x', "ingotBrass", 'r', Items.redstone));
		
		BookRecipeRegistry.addRecipe("carving",new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.carving), "xzx", "x x", "xxx", 
		        'x', "plankWood", 'z', SteamcraftItems.blankMold));
		BookRecipeRegistry.addRecipe("engineering1",new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.engineering), "xzx", "x x", "xxx", 
		        'x', "blockCobble", 'z', "plateSteamcraftIron"));
		BookRecipeRegistry.addRecipe("engineering2",new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.engineering), "xzx", "x x", "xxx", 
		        'x', Blocks.cobblestone, 'z', "plateSteamcraftIron"));
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.blockBrass), "iii", "iii", "iii",
		        'i', "ingotBrass"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamcraftIngot,9,2), "i",
		        'i', "blockBrass"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.blockCopper), "iii", "iii", "iii",
		        'i', "ingotCopper"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamcraftIngot,9,0), "i",
		        'i', "blockCopper"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.blockZinc), "iii", "iii", "iii",
		        'i', "ingotZinc"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamcraftIngot,9,1), "i",
		        'i', "blockZinc"));
		
		
		
		BookRecipeRegistry.addRecipe("filler1",new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.charger), " p ", "xpx", "xpx", 
		        'x', Blocks.cobblestone, 'p', SteamcraftBlocks.pipe));
		BookRecipeRegistry.addRecipe("filler2",new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.charger), " p ", "xpx", "xpx", 
		        'x', "blockCobble", 'p', SteamcraftBlocks.pipe));
		BookRecipeRegistry.addRecipe("tank1", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.tank), "iii", "i i", "iii",
		        'i', "plateSteamcraftBrass"));
		BookRecipeRegistry.addRecipe("tank2", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.tank), "iii", "i i", "iii",
		        'i', "ingotBrass"));
		
		BookRecipeRegistry.addRecipe("pump1", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.pump), "gng", "iii", "ngn",
		        'i', "plateSteamcraftBrass", 'n', "nuggetBrass", 'g', Blocks.glass_pane));
		BookRecipeRegistry.addRecipe("pump2", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.pump), "gng", "iii", "ngn",
		        'i', "ingotBrass", 'n', "nuggetBrass", 'g', Blocks.glass_pane));
		
		BookRecipeRegistry.addRecipe("piston1",new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamcraftCrafting,1,0), " x ", "xpx", " i ", 
		        'x', "ingotBrass", 'p', Blocks.piston, 'i', SteamcraftBlocks.pipe));
		BookRecipeRegistry.addRecipe("piston2",new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamcraftCrafting,1,0), " x ", "xpx", " i ", 
		        'x', "plateSteamcraftBrass", 'p', Blocks.piston, 'i', SteamcraftBlocks.pipe));
		
		BookRecipeRegistry.addRecipe("turbine1",new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamcraftCrafting,1,5), " x ", "xnx", " x ", 
		        'x', "ingotBrass", 'n', "nuggetBrass"));
		BookRecipeRegistry.addRecipe("turbine2",new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamcraftCrafting,1,5), " x ", "xnx", " x ", 
		        'x', "plateSteamcraftBrass", 'n', "nuggetBrass"));

		BookRecipeRegistry.addRecipe("drill1",new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamDrill,1,SteamcraftItems.steamDrill.getMaxDamage()-1), "xii", "pti", "xpx", 
		        'x', "ingotBrass", 'i', "ingotIron", 'p', SteamcraftBlocks.pipe, 't', new ItemStack(SteamcraftItems.steamcraftCrafting,1,5)));
		BookRecipeRegistry.addRecipe("drill2",new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamDrill,1,SteamcraftItems.steamDrill.getMaxDamage()-1), "xii", "pti", "xpx", 
		        'x', "plateSteamcraftBrass", 'i', "ingotIron", 'p', SteamcraftBlocks.pipe, 't', new ItemStack(SteamcraftItems.steamcraftCrafting,1,5)));
		BookRecipeRegistry.addRecipe("drill3",new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamDrill,1,SteamcraftItems.steamDrill.getMaxDamage()-1), "xii", "pti", "xpx", 
		        'x', "ingotBrass", 'i', "plateSteamcraftIron", 'p', SteamcraftBlocks.pipe, 't', new ItemStack(SteamcraftItems.steamcraftCrafting,1,5)));
		BookRecipeRegistry.addRecipe("drill4",new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamDrill,1,SteamcraftItems.steamDrill.getMaxDamage()-1), "xii", "pti", "xpx", 
		        'x', "plateSteamcraftBrass", 'i', "plateSteamcraftIron", 'p', SteamcraftBlocks.pipe, 't', new ItemStack(SteamcraftItems.steamcraftCrafting,1,5)));
		
		BookRecipeRegistry.addRecipe("shovel1",new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamShovel,1,SteamcraftItems.steamShovel.getMaxDamage()-1), "ixi", "ptx", "xpi", 
		        'x', "ingotBrass", 'i', "ingotIron", 'p', SteamcraftBlocks.pipe, 't', new ItemStack(SteamcraftItems.steamcraftCrafting,1,5)));
		BookRecipeRegistry.addRecipe("shovel2",new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamShovel,1,SteamcraftItems.steamShovel.getMaxDamage()-1), "ixi", "ptx", "xpi", 
		        'x', "plateSteamcraftBrass", 'i', "ingotIron", 'p', SteamcraftBlocks.pipe, 't', new ItemStack(SteamcraftItems.steamcraftCrafting,1,5)));
		BookRecipeRegistry.addRecipe("shovel3",new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamShovel,1,SteamcraftItems.steamShovel.getMaxDamage()-1), "ixi", "ptx", "xpi", 
		        'x', "ingotBrass", 'i', "plateSteamcraftIron", 'p', SteamcraftBlocks.pipe, 't', new ItemStack(SteamcraftItems.steamcraftCrafting,1,5)));
		BookRecipeRegistry.addRecipe("shovel4",new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamShovel,1,SteamcraftItems.steamShovel.getMaxDamage()-1), "ixi", "ptx", "xpi", 
		        'x', "plateSteamcraftBrass", 'i', "plateSteamcraftIron", 'p', SteamcraftBlocks.pipe, 't', new ItemStack(SteamcraftItems.steamcraftCrafting,1,5)));
		
		BookRecipeRegistry.addRecipe("axe1",new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamAxe,1,SteamcraftItems.steamAxe.getMaxDamage()-1), "ini", "ptn", "xpi", 
		        'x', "ingotBrass", 'i', "ingotIron", 'p', SteamcraftBlocks.pipe, 't', new ItemStack(SteamcraftItems.steamcraftCrafting,1,5), 'n', "nuggetIron"));
		BookRecipeRegistry.addRecipe("axe2",new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamAxe,1,SteamcraftItems.steamAxe.getMaxDamage()-1), "ini", "ptn", "xpi", 
		        'x', "plateSteamcraftBrass", 'i', "ingotIron", 'p', SteamcraftBlocks.pipe, 't', new ItemStack(SteamcraftItems.steamcraftCrafting,1,5), 'n', "nuggetIron"));
		BookRecipeRegistry.addRecipe("axe3",new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamAxe,1,SteamcraftItems.steamAxe.getMaxDamage()-1), "ini", "ptn", "xpi", 
		        'x', "ingotBrass", 'i', "plateSteamcraftIron", 'p', SteamcraftBlocks.pipe, 't', new ItemStack(SteamcraftItems.steamcraftCrafting,1,5), 'n', "nuggetIron"));
		BookRecipeRegistry.addRecipe("axe4",new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamAxe,1,SteamcraftItems.steamAxe.getMaxDamage()-1), "ini", "ptn", "xpi", 
		        'x', "plateSteamcraftBrass", 'i', "plateSteamcraftIron", 'p', SteamcraftBlocks.pipe, 't', new ItemStack(SteamcraftItems.steamcraftCrafting,1,5), 'n', "nuggetIron"));
		
		
		BookRecipeRegistry.addRecipe("stock",new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamcraftCrafting,1,1), "p  ", " p ", " pp", 
		        'p', "plankWood"));
		BookRecipeRegistry.addRecipe("barrel1",new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamcraftCrafting,1,2), "i  ", " i ", "  i", 
		        'i', "ingotIron"));
		BookRecipeRegistry.addRecipe("barrel2",new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamcraftCrafting,1,2), "i  ", " i ", "  i", 
		        'i', "plateSteamcraftIron"));
		BookRecipeRegistry.addRecipe("blunderBarrel1",new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamcraftCrafting,1,3), "i  ", " i ", "  i", 
		        'i', "ingotBrass"));
		BookRecipeRegistry.addRecipe("blunderBarrel2",new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamcraftCrafting,1,3), "i  ", " i ", "  i", 
		        'i', "plateSteamcraftBrass"));
		if (!Config.expensiveMusketRecipes) {
			BookRecipeRegistry.addRecipe("cartridge1",new ShapelessOreRecipe(new ItemStack(SteamcraftItems.musketCartridge,2,0), "nuggetIron", "nuggetIron", Items.paper, Items.paper, Items.gunpowder));
			BookRecipeRegistry.addRecipe("cartridge2",new ShapelessOreRecipe(new ItemStack(SteamcraftItems.musketCartridge,2,0), "nuggetLead", "nuggetLead", Items.paper, Items.paper, Items.gunpowder));
			BookRecipeRegistry.addRecipe("cartridge3",new ShapelessOreRecipe(new ItemStack(SteamcraftItems.musketCartridge,2,0), "nuggetSteel", "nuggetSteel", Items.paper, Items.paper, Items.gunpowder));
			BookRecipeRegistry.addRecipe("cartridge4",new ShapelessOreRecipe(new ItemStack(SteamcraftItems.musketCartridge,2,0), "nuggetSilver", "nuggetSilver", Items.paper, Items.paper, Items.gunpowder));
		}
		else
		{
			BookRecipeRegistry.addRecipe("cartridge1",new ShapelessOreRecipe(new ItemStack(SteamcraftItems.musketCartridge), "nuggetIron", Items.paper, Items.gunpowder));
			BookRecipeRegistry.addRecipe("cartridge2",new ShapelessOreRecipe(new ItemStack(SteamcraftItems.musketCartridge), "nuggetLead", Items.paper, Items.gunpowder));
			BookRecipeRegistry.addRecipe("cartridge3",new ShapelessOreRecipe(new ItemStack(SteamcraftItems.musketCartridge), "nuggetSteel", Items.paper, Items.gunpowder));
			BookRecipeRegistry.addRecipe("cartridge4",new ShapelessOreRecipe(new ItemStack(SteamcraftItems.musketCartridge), "nuggetSilver", Items.paper, Items.gunpowder));
		}
		BookRecipeRegistry.addRecipe("flintlock1",new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamcraftCrafting,1,4), "f i", "iri", 
		        'i', "ingotIron", 'r', Items.redstone, 'f', Items.flint_and_steel));
		BookRecipeRegistry.addRecipe("flintlock2",new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamcraftCrafting,1,4), "f i", "iri",
		        'i', "plateSteamcraftIron", 'r', Items.redstone, 'f', Items.flint_and_steel));
		BookRecipeRegistry.addRecipe("musket",new ShapedOreRecipe(new ItemStack(SteamcraftItems.musket), "b  ", " bf", "  s", 
		        'b', new ItemStack(SteamcraftItems.steamcraftCrafting,1,2), 'f', new ItemStack(SteamcraftItems.steamcraftCrafting,1,4), 's', new ItemStack(SteamcraftItems.steamcraftCrafting,1,1)));
		BookRecipeRegistry.addRecipe("pistol",new ShapedOreRecipe(new ItemStack(SteamcraftItems.pistol), "b  ", " pf", " p ", 
		        'b', new ItemStack(SteamcraftItems.steamcraftCrafting,1,2), 'f', new ItemStack(SteamcraftItems.steamcraftCrafting,1,4), 'p', "plankWood"));
		BookRecipeRegistry.addRecipe("blunderbuss",new ShapedOreRecipe(new ItemStack(SteamcraftItems.blunderbuss), "b  ", " bf", "  s", 
		        'b', new ItemStack(SteamcraftItems.steamcraftCrafting,1,3), 'f', new ItemStack(SteamcraftItems.steamcraftCrafting,1,4), 's', new ItemStack(SteamcraftItems.steamcraftCrafting,1,1)));
		
		BookRecipeRegistry.addRecipe("spyglass1",new ShapedOreRecipe(new ItemStack(SteamcraftItems.spyglass), "gb ", "bgb", " bb", 
		        'b', "ingotBrass", 'g', Blocks.glass_pane));
		BookRecipeRegistry.addRecipe("spyglass2",new ShapedOreRecipe(new ItemStack(SteamcraftItems.spyglass), "gb ", "bgb", " bb", 
		        'b', "plateSteamcraftBrass", 'g', Blocks.glass_pane));
		
		BookRecipeRegistry.addRecipe("boiler1",new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.boiler), "xxx", "xfx", "xxx", 
		        'x', "ingotBrass", 'f', Blocks.furnace));
		BookRecipeRegistry.addRecipe("boiler2",new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.boiler), "xxx", "xfx", "xxx", 
		        'x', "plateSteamcraftBrass", 'f', Blocks.furnace));
		BookRecipeRegistry.addRecipe("pipe1",new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.pipe,4,0), "xxx", "   ", "xxx", 
		        'x', "ingotBrass"));
		BookRecipeRegistry.addRecipe("pipe2",new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.pipe,4,0), "xxx", "   ", "xxx", 
		        'x', "plateSteamcraftBrass"));
		BookRecipeRegistry.addRecipe("valvePipe",new ShapelessOreRecipe(new ItemStack(SteamcraftBlocks.valvePipe), SteamcraftBlocks.pipe, Blocks.lever));

		BookRecipeRegistry.addRecipe("gauge",new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.meter), " x ", "xrx", " x ",
		        'x', "nuggetBrass", 'r', Items.compass));
		BookRecipeRegistry.addRecipe("disc",new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.ruptureDisc), " x ", "xrx", " x ",
		        'x', "nuggetBrass", 'r', "plateSteamcraftZinc"));
		BookRecipeRegistry.addRecipe("heater1",new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.heater), "ccc", "xfx", " p ", 
		        'x', "ingotBrass", 'c', "nuggetCopper", 'f', Blocks.furnace,'p', SteamcraftBlocks.pipe));
		BookRecipeRegistry.addRecipe("heater2",new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.heater), "ccc", "xfx", " p ", 
		        'x', "plateSteamcraftBrass", 'c', "nuggetCopper", 'f', Blocks.furnace,'p', SteamcraftBlocks.pipe));
		BookRecipeRegistry.addRecipe("hammer1",new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.hammer), " ix", "bix", 
		        'x', "ingotBrass", 'i', "ingotIron", 'b', Blocks.iron_block));
		BookRecipeRegistry.addRecipe("hammer2",new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.hammer), " ix", "bix", 
		        'x', "plateSteamcraftBrass", 'i', "ingotIron", 'b', Blocks.iron_block));
		
		BookRecipeRegistry.addRecipe("itemMortar1",new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.itemMortar), "p p", "pbp", "ccc", 
		        'p', "plateSteamcraftBrass", 'c', "plateSteamcraftCopper", 'b', new ItemStack(SteamcraftItems.steamcraftCrafting,1,0)));
		BookRecipeRegistry.addRecipe("itemMortar2",new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.itemMortar), "p p", "pbp", "ccc", 
		        'p', "ingotBrass", 'c', "plateSteamcraftCopper", 'b', new ItemStack(SteamcraftItems.steamcraftCrafting,1,0)));
		BookRecipeRegistry.addRecipe("itemMortar3",new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.itemMortar), "p p", "pbp", "ccc", 
		        'p', "plateSteamcraftBrass", 'c', "ingotCopper", 'b', new ItemStack(SteamcraftItems.steamcraftCrafting,1,0)));
		BookRecipeRegistry.addRecipe("itemMortar4",new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.itemMortar), "p p", "pbp", "ccc", 
		        'p', "ingotBrass", 'c', "ingotCopper", 'b', new ItemStack(SteamcraftItems.steamcraftCrafting,1,0)));
		
		BookRecipeRegistry.addRecipe("exoHead",new ShapedOreRecipe(new ItemStack(SteamcraftItems.exoArmorHead), "xyx", "p p", "xyx", 
		        'x', "plateSteamcraftBrass", 'y', "nuggetBrass", 'p', new ItemStack(SteamcraftItems.steamcraftCrafting,1,0)));
		BookRecipeRegistry.addRecipe("exoBody",new ShapedOreRecipe(new ItemStack(SteamcraftItems.exoArmorBody,1,SteamcraftItems.exoArmorBody.getMaxDamage()-1), "p p", "ygy", "xxx", 
		        'x', "plateSteamcraftBrass", 'y', "nuggetBrass", 'g', SteamcraftBlocks.meter, 'p', new ItemStack(SteamcraftItems.steamcraftCrafting,1,0)));
		BookRecipeRegistry.addRecipe("exoLegs",new ShapedOreRecipe(new ItemStack(SteamcraftItems.exoArmorLegs), "yxy", "p p", "x x", 
		        'x', "plateSteamcraftBrass", 'y', "nuggetBrass", 'p', new ItemStack(SteamcraftItems.steamcraftCrafting,1,0)));
		BookRecipeRegistry.addRecipe("exoFeet",new ShapedOreRecipe(new ItemStack(SteamcraftItems.exoArmorFeet), "p p", "x x", 
		        'x', "plateSteamcraftBrass", 'y', "nuggetBrass", 'p', new ItemStack(SteamcraftItems.steamcraftCrafting,1,0)));
		
		BookRecipeRegistry.addRecipe("jetpack1",new ShapedOreRecipe(new ItemStack(SteamcraftItems.jetpack), "p p", "ptg", "p p" ,
		        'p', SteamcraftBlocks.pipe, 'g', SteamcraftBlocks.meter, 't', "ingotBrass"));
		BookRecipeRegistry.addRecipe("jetpack2",new ShapedOreRecipe(new ItemStack(SteamcraftItems.jetpack), "p p", "ptg", "p p" ,
		        'p', SteamcraftBlocks.pipe, 'g', SteamcraftBlocks.meter, 't', "plateSteamcraftBrass"));
		
		BookRecipeRegistry.addRecipe("thrusters1",new ShapedOreRecipe(new ItemStack(SteamcraftItems.thrusters), "tnt", "ptp", "tnt" ,
		        'p', SteamcraftBlocks.pipe, 't', "ingotBrass", 'n', "nuggetBrass"));
		BookRecipeRegistry.addRecipe("thrusters2",new ShapedOreRecipe(new ItemStack(SteamcraftItems.thrusters), "tnt", "ptp", "tnt" ,
		        'p', SteamcraftBlocks.pipe, 't', "plateSteamcraftBrass", 'n', "nuggetBrass"));
		
		BookRecipeRegistry.addRecipe("wings1",new ShapedOreRecipe(new ItemStack(SteamcraftItems.wings), "xxx", "ccc", "c c" ,
		        'x',"ingotBrass", 'c', "plateSteamcraftCopper"));
		BookRecipeRegistry.addRecipe("wings2",new ShapedOreRecipe(new ItemStack(SteamcraftItems.wings), "xxx", "ccc", "c c" ,
		        'x',"plateSteamcraftBrass", 'c', "plateSteamcraftCopper"));
		
		BookRecipeRegistry.addRecipe("powerFist1",new ShapedOreRecipe(new ItemStack(SteamcraftItems.powerFist), "b i", "bpi", "b i" ,
		        'i',"ingotIron", 'p',new ItemStack(SteamcraftItems.steamcraftCrafting,1,0),'b', "nuggetBrass"));
		BookRecipeRegistry.addRecipe("powerFist2",new ShapedOreRecipe(new ItemStack(SteamcraftItems.powerFist), "b i", "bpi", "b i" ,
		        'i',"plateSteamcraftIron", 'p',new ItemStack(SteamcraftItems.steamcraftCrafting,1,0),'b', "nuggetBrass"));
		
		BookRecipeRegistry.addRecipe("noFall",new ShapedOreRecipe(new ItemStack(SteamcraftItems.fallAssist), "pbp", "sss",
		        'b',Items.leather_boots, 'p',new ItemStack(SteamcraftItems.steamcraftCrafting,1,0),'s', Items.slime_ball));
		
		BookRecipeRegistry.addRecipe("smasher1",new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.smasher), "bpi", "bpi", "bpi",
		        'i',"plateSteamcraftIron", 'p',new ItemStack(SteamcraftItems.steamcraftCrafting,1,0),'b', "plateSteamcraftBrass"));
		BookRecipeRegistry.addRecipe("smasher2",new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.smasher), "bpi", "bpi", "bpi",
		        'i',"ingotIron", 'p',new ItemStack(SteamcraftItems.steamcraftCrafting,1,0),'b', "ingotBrass"));
		BookRecipeRegistry.addRecipe("smasher3",new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.smasher), "bpi", "bpi", "bpi",
		        'i',"ingotIron", 'p',new ItemStack(SteamcraftItems.steamcraftCrafting,1,0),'b', "plateSteamcraftBrass"));
		BookRecipeRegistry.addRecipe("smasher4",new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.smasher), "bpi", "bpi", "bpi",
		        'i',"plateSteamcraftIron", 'p',new ItemStack(SteamcraftItems.steamcraftCrafting,1,0),'b', "ingotBrass"));
		
		BookRecipeRegistry.addRecipe("thumper1",new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.thumper), "pbp", "ebe", "xix",
		        'i',"blockIron",'b',"blockBrass",'e',SteamcraftBlocks.pipe,'p',new ItemStack(SteamcraftItems.steamcraftCrafting,1,0),'x', "plateSteamcraftBrass"));
		BookRecipeRegistry.addRecipe("thumper2",new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.thumper), "pbp", "ebe", "xix",
		        'i',"blockIron",'b',"blockBrass",'e',SteamcraftBlocks.pipe,'p',new ItemStack(SteamcraftItems.steamcraftCrafting,1,0),'x', "ingotBrass"));
		
		BookRecipeRegistry.addRecipe("flashBoiler1",new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.flashBoiler,2), "xtx", "pbp", "nnn", 
		        'x', "ingotBrass", 'b', SteamcraftBlocks.boiler, 't', new ItemStack(SteamcraftBlocks.tank,1,0), 'n', Blocks.nether_brick, 'p', SteamcraftBlocks.pipe));
		BookRecipeRegistry.addRecipe("flashBoiler2",new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.flashBoiler,2), "xtx", "pbp", "nnn", 
		        'x', "plateSteamcraftBrass", 'b', SteamcraftBlocks.boiler, 't', new ItemStack(SteamcraftBlocks.tank,1,0), 'n', Blocks.nether_brick, 'p', SteamcraftBlocks.pipe));
		//5 8 7 4
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(SteamcraftItems.steamcraftNugget, 9, 0), "ingotCopper"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(SteamcraftItems.steamcraftNugget, 9, 1), "ingotZinc"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(SteamcraftItems.steamcraftNugget, 9, 2), "ingotIron"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(SteamcraftItems.steamcraftNugget, 9, 2), new ItemStack(Items.iron_ingot)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(SteamcraftItems.steamcraftNugget, 9, 3), "ingotBrass"));

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(SteamcraftBlocks.ruptureDisc, 1, 0), "plateSteamcraftZinc", new ItemStack(SteamcraftBlocks.ruptureDisc, 1, 1)));
		
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
