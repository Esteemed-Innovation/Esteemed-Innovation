package flaxbeard.steamcraft;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import flaxbeard.steamcraft.api.ICrucibleMold;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.enhancement.IEnhancement;
import flaxbeard.steamcraft.api.exosuit.ExosuitPlate;
import flaxbeard.steamcraft.integration.BaublesIntegration;
import flaxbeard.steamcraft.item.ItemAstrolabe;
import flaxbeard.steamcraft.item.ItemExosuitArmor;
import flaxbeard.steamcraft.item.ItemExosuitArmor.ExosuitSlot;
import flaxbeard.steamcraft.item.ItemExosuitArmorThaum;
import flaxbeard.steamcraft.item.ItemExosuitJetpack;
import flaxbeard.steamcraft.item.ItemExosuitSidepack;
import flaxbeard.steamcraft.item.ItemExosuitUpgrade;
import flaxbeard.steamcraft.item.ItemExosuitWings;
import flaxbeard.steamcraft.item.ItemIngotMold;
import flaxbeard.steamcraft.item.ItemNuggetMold;
import flaxbeard.steamcraft.item.ItemPlateMold;
import flaxbeard.steamcraft.item.ItemSmashedOre;
import flaxbeard.steamcraft.item.ItemSteamcraftBook;
import flaxbeard.steamcraft.item.ItemSteamcraftCrafting;
import flaxbeard.steamcraft.item.ItemSteamcraftIngot;
import flaxbeard.steamcraft.item.ItemSteamcraftNugget;
import flaxbeard.steamcraft.item.ItemSteamcraftPlate;
import flaxbeard.steamcraft.item.ItemSteamedFood;
import flaxbeard.steamcraft.item.tool.ItemSteamcraftArmor;
import flaxbeard.steamcraft.item.firearm.ItemEnhancementFireMusket;
import flaxbeard.steamcraft.item.firearm.ItemEnhancementRevolver;
import flaxbeard.steamcraft.item.firearm.ItemFirearm;
import flaxbeard.steamcraft.item.tool.ItemSpyglass;
import flaxbeard.steamcraft.item.tool.ItemSteamcraftAxe;
import flaxbeard.steamcraft.item.tool.ItemSteamcraftHoe;
import flaxbeard.steamcraft.item.tool.ItemSteamcraftPickaxe;
import flaxbeard.steamcraft.item.tool.ItemSteamcraftShovel;
import flaxbeard.steamcraft.item.tool.ItemSteamcraftSword;
import flaxbeard.steamcraft.item.tool.steam.ItemSteamAxe;
import flaxbeard.steamcraft.item.tool.steam.ItemSteamDrill;
import flaxbeard.steamcraft.item.tool.steam.ItemSteamShovel;

public class SteamcraftItems {
	public static HashMap<String,Item> tools = new HashMap<String,Item>();
	public static Item musketCartridge;
    public static Item musket;
    public static Item pistol;
    public static Item revolver;
    public static Item blunderbuss;
    public static Item spyglass;
    
    public static Item survivalist;
    
    public static Item astrolabe;
    
    public static Item blankMold;
    public static Item ingotMold;
    public static Item nuggetMold;
    public static Item plateMold;
    
    public static Item steamcraftIngot;
    public static Item steamcraftNugget;
    public static Item steamcraftPlate;
    public static Item steamcraftCrafting;
    
    public static Item enhancementAblaze;
    
    public static Item enhancementRevolver;

    
    public static Item upgradeFlippers;
    
    public static Item book;

    public static Item exoArmorHead;
    public static Item exoArmorBody;
    public static Item exoArmorLegs;
    public static Item exoArmorFeet;
    
    public static Item steamDrill;
    public static Item steamAxe;
    public static Item steamShovel;
	public static Item jetpack;
	public static Item wings;
	public static Item powerFist;
	public static Item thrusters;
	public static Item fallAssist;
	public static Item doubleJump;
	
	public static Item smashedOre;
	//public static Item fakeOre;
	
	public static Item steamedPorkchop;
	public static Item steamedFish;
	public static Item steamedBeef;
	public static Item steamedChicken;
    
    public static void registerItems() {
		
		book = new ItemSteamcraftBook().setUnlocalizedName("steamcraft:book").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:book");
		GameRegistry.registerItem(book, "book");
		
    	musketCartridge = new Item().setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:musketCartridge").setTextureName("steamcraft:cartridge");
		GameRegistry.registerItem(musketCartridge, "musketCartridge");
		musket = new ItemFirearm(20.0F, 84,0.2F, 5.0F, false, 1, "ingotIron").setUnlocalizedName("steamcraft:musket").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:weaponMusket");
		GameRegistry.registerItem(musket, "musket");
		pistol = new ItemFirearm(15.0F, 42,0.5F, 2.0F, false, 1, "ingotIron").setUnlocalizedName("steamcraft:pistol").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:weaponPistol");
		GameRegistry.registerItem(pistol, "pistol");
		blunderbuss = new ItemFirearm(25.0F, 95,3.5F, 7.5F, true, 1, "ingotBrass").setUnlocalizedName("steamcraft:blunderbuss").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:weaponBlunderbuss");
		GameRegistry.registerItem(blunderbuss, "blunderbuss");
		spyglass = new ItemSpyglass().setUnlocalizedName("steamcraft:spyglass").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:spyglass");
		GameRegistry.registerItem(spyglass, "spyglass");
		SteamcraftRegistry.registerEnhancement((IEnhancement) spyglass);
		//enhancementRevolver = new ItemEnhancementRevolver().setUnlocalizedName("steamcraft:enhancementRevolver").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:enhancementRevolver");
		//GameRegistry.registerItem(enhancementRevolver, "enhancementRevolver");
	//	SteamcraftRegistry.registerEnhancement((IEnhancement) enhancementRevolver);
	//	enhancementAblaze = new ItemEnhancementFireMusket().setUnlocalizedName("steamcraft:enhancementAblaze").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:enhancementAblaze");
		//GameRegistry.registerItem(enhancementAblaze, "enhancementAblaze");
		//SteamcraftRegistry.registerEnhancement((IEnhancement) enhancementAblaze);
		
	 	jetpack = new ItemExosuitJetpack().setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:jetpack").setTextureName("steamcraft:jetpack");
		GameRegistry.registerItem(jetpack, "jetpack");
	 	wings = new ItemExosuitWings().setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:wings").setTextureName("steamcraft:wings");
		GameRegistry.registerItem(wings, "wings");
	 	powerFist = new ItemExosuitUpgrade(ExosuitSlot.bodyHand, "steamcraft:textures/models/armor/fireFist.png",null,0).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:powerFist").setTextureName("steamcraft:powerFist");
		GameRegistry.registerItem(powerFist, "powerFist");
		thrusters = new ItemExosuitSidepack().setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:thrusters").setTextureName("steamcraft:thrusters");
		GameRegistry.registerItem(thrusters, "thrusters");
		fallAssist = new ItemExosuitUpgrade(ExosuitSlot.bootsTop, "steamcraft:textures/models/armor/fallUpgrade.png",null,0).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:fallAssist").setTextureName("steamcraft:fallUpgrade");
		GameRegistry.registerItem(fallAssist, "fallAssist");
	 	//doubleJump = new ItemExosuitUpgrade(ExosuitSlot.bootsTop, "steamcraft:textures/models/armor/fallUpgrade.png",null,0).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:doubleJump").setTextureName("steamcraft:doubleJump");
		//GameRegistry.registerItem(doubleJump, "doubleJump");
		
	 	astrolabe = new ItemAstrolabe().setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:astrolabe").setTextureName("steamcraft:astrolabe").setMaxStackSize(1);
		GameRegistry.registerItem(astrolabe, "astrolabe");
		
		if (!Loader.isModLoaded("Baubles")) {
			survivalist = new Item().setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:survivalist").setTextureName("steamcraft:toolkit").setMaxStackSize(1);
		}
		else
		{
			survivalist = BaublesIntegration.getSurvivalist();
		}
		GameRegistry.registerItem(survivalist, "survivalist");
		
		if (Loader.isModLoaded("Thaumcraft")) {
			exoArmorHead = new ItemExosuitArmorThaum(0).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:exoArmorHead").setTextureName("steamcraft:exoArmorHead");
		}
		else
		{
			exoArmorHead = new ItemExosuitArmor(0).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:exoArmorHead").setTextureName("steamcraft:exoArmorHead");
		}
		GameRegistry.registerItem(exoArmorHead, "exoArmorHead");
		exoArmorBody = new ItemExosuitArmor(1).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:exoArmorBody").setTextureName("steamcraft:exoArmorBody");
		GameRegistry.registerItem(exoArmorBody, "exoArmorBody");
		exoArmorLegs = new ItemExosuitArmor(2).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:exoArmorLegs").setTextureName("steamcraft:exoArmorLegs");
		GameRegistry.registerItem(exoArmorLegs, "exoArmorLegs");
		exoArmorFeet = new ItemExosuitArmor(3).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:exoArmorFeet").setTextureName("steamcraft:exoArmorFeet");
		GameRegistry.registerItem(exoArmorFeet, "exoArmorFeet");
		
		steamDrill = new ItemSteamDrill().setUnlocalizedName("steamcraft:steamDrill").setCreativeTab(Steamcraft.tabTools);
		GameRegistry.registerItem(steamDrill, "steamDrill");
		steamAxe = new ItemSteamAxe().setUnlocalizedName("steamcraft:steamAxe").setCreativeTab(Steamcraft.tabTools);
		GameRegistry.registerItem(steamAxe, "steamAxe");
		steamShovel = new ItemSteamShovel().setUnlocalizedName("steamcraft:steamShovel").setCreativeTab(Steamcraft.tabTools);
		GameRegistry.registerItem(steamShovel, "steamShovel");
		
		ingotMold = new ItemIngotMold().setUnlocalizedName("steamcraft:ingotMold").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:moldIngot");
		GameRegistry.registerItem(ingotMold, "ingotMold");
		SteamcraftRegistry.addCarvableMold((ICrucibleMold) ingotMold);
		nuggetMold = new ItemNuggetMold().setUnlocalizedName("steamcraft:nuggetMold").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:moldNugget");
		GameRegistry.registerItem(nuggetMold, "nuggetMold");
		SteamcraftRegistry.addCarvableMold((ICrucibleMold) nuggetMold);
		plateMold = new ItemPlateMold().setUnlocalizedName("steamcraft:plateMold").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:moldPlate");
		GameRegistry.registerItem(plateMold, "plateMold");
		SteamcraftRegistry.addCarvableMold((ICrucibleMold) plateMold);
		blankMold = new Item().setUnlocalizedName("steamcraft:blankMold").setMaxStackSize(1).setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:moldBlank");
		GameRegistry.registerItem(blankMold, "blankMold");
		
		smashedOre = new ItemSmashedOre().setUnlocalizedName("steamcraft:smashedOre").setMaxStackSize(64).setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:smashedOre");
		GameRegistry.registerItem(smashedOre, "smashedOre");
		
		
//		fakeOre = new Item().setUnlocalizedName("steamcraft:fakeOre");
//		OreDictionary.registerOre("oreTin", new ItemStack(fakeOre,1,0));
//		OreDictionary.registerOre("oreLead", new ItemStack(fakeOre,1,1));
//		OreDictionary.registerOre("oreSilver", new ItemStack(fakeOre,1,2));
//		OreDictionary.registerOre("oreOsmium", new ItemStack(fakeOre, 1,3));
//		OreDictionary.registerOre("oreNickel", new ItemStack(fakeOre, 1,4));
//		OreDictionary.registerOre("oreAluminum", new ItemStack(fakeOre, 1,5));
//		OreDictionary.registerOre("oreCobalt", new ItemStack(fakeOre, 1,6));
//		OreDictionary.registerOre("oreArdite", new ItemStack(fakeOre, 1,7));
		
		
		steamedFish = new ItemSteamedFood((ItemFood) Items.cooked_fished).setUnlocalizedName("steamcraft:steamedFish").setCreativeTab(Steamcraft.tab);
		GameRegistry.registerItem(steamedFish, "steamedFish");
        ItemFishFood.FishType[] afishtype = ItemFishFood.FishType.values();
		SteamcraftRegistry.addSteamFood(Items.cooked_fished, steamedFish);
		steamedChicken = new ItemSteamedFood((ItemFood) Items.cooked_chicken).setUnlocalizedName("steamcraft:steamedChicken").setCreativeTab(Steamcraft.tab);
		GameRegistry.registerItem(steamedChicken, "steamedChicken");
		SteamcraftRegistry.addSteamFood(Items.cooked_chicken, steamedChicken);
		steamedBeef = new ItemSteamedFood((ItemFood) Items.cooked_beef).setUnlocalizedName("steamcraft:steamedBeef").setCreativeTab(Steamcraft.tab);
		GameRegistry.registerItem(steamedBeef, "steamedBeef");
		SteamcraftRegistry.addSteamFood(Items.cooked_beef, steamedBeef);
		steamedPorkchop = new ItemSteamedFood((ItemFood) Items.cooked_porkchop).setUnlocalizedName("steamcraft:steamedPorkchop").setCreativeTab(Steamcraft.tab);
		GameRegistry.registerItem(steamedPorkchop, "steamedPorkchop");
		SteamcraftRegistry.addSteamFood(Items.cooked_porkchop, steamedPorkchop);
		
		steamcraftCrafting = new ItemSteamcraftCrafting().setUnlocalizedName("steamcraft:crafting").setCreativeTab(Steamcraft.tab);
		GameRegistry.registerItem(steamcraftCrafting, "steamcraftCrafting");
		
		steamcraftIngot = new ItemSteamcraftIngot().setUnlocalizedName("steamcraft:ingot").setCreativeTab(Steamcraft.tab);
		GameRegistry.registerItem(steamcraftIngot, "steamcraftIngot");
		OreDictionary.registerOre("ingotCopper", new ItemStack(steamcraftIngot,1,0));
		OreDictionary.registerOre("ingotZinc", new ItemStack(steamcraftIngot,1,1));
		OreDictionary.registerOre("ingotBrass", new ItemStack(steamcraftIngot,1,2));
		
		ToolMaterial brass = EnumHelper.addToolMaterial("BRASS", 2, 191, 7.0F, 2.5F, 14);
		ItemArmor.ArmorMaterial mat = EnumHelper.addArmorMaterial("BRASS", 11, new int[]{2, 7, 6, 3}, 9);
		registerToolSet(brass, "Brass", "ingotBrass", true);
		registerArmorSet(mat, "Brass", "ingotBrass", true);
		registerGildedTools();
		registerGildedArmor();
		
		steamcraftNugget = new ItemSteamcraftNugget().setUnlocalizedName("steamcraft:nugget").setCreativeTab(Steamcraft.tab);
		GameRegistry.registerItem(steamcraftNugget, "steamcraftNugget");
		OreDictionary.registerOre("nuggetCopper", new ItemStack(steamcraftNugget,1,0));
		OreDictionary.registerOre("nuggetZinc", new ItemStack(steamcraftNugget,1,1));
		OreDictionary.registerOre("nuggetIron", new ItemStack(steamcraftNugget,1,2));
		OreDictionary.registerOre("nuggetBrass", new ItemStack(steamcraftNugget,1,3));
		
		steamcraftPlate = new ItemSteamcraftPlate().setUnlocalizedName("steamcraft:plate").setCreativeTab(Steamcraft.tab);
		GameRegistry.registerItem(steamcraftPlate, "steamcraftPlate");
		OreDictionary.registerOre("plateSteamcraftCopper", new ItemStack(steamcraftPlate,1,0));
		OreDictionary.registerOre("plateSteamcraftZinc", new ItemStack(steamcraftPlate,1,1));
		OreDictionary.registerOre("plateSteamcraftIron", new ItemStack(steamcraftPlate,1,2));
		OreDictionary.registerOre("plateSteamcraftGold", new ItemStack(steamcraftPlate,1,3));
		OreDictionary.registerOre("plateSteamcraftBrass", new ItemStack(steamcraftPlate,1,4));
		OreDictionary.registerOre("plateSteamcraftThaumium", new ItemStack(steamcraftPlate,1,5));
		SteamcraftRegistry.addExosuitPlate(new ExosuitPlate("Copper","plateSteamcraftCopper","Copper","Copper"));
		SteamcraftRegistry.addExosuitPlate(new ExosuitPlate("Iron","plateSteamcraftIron","Iron","Iron"));
		SteamcraftRegistry.addExosuitPlate(new ExosuitPlate("Gold","plateSteamcraftGold","Gold","Gold"));

    }
    
    public static void registerToolSet(ToolMaterial tool, String string, Object repair, boolean addRecipes) {
    	Item pick = new ItemSteamcraftPickaxe(tool, repair).setUnlocalizedName("steamcraft:pick"+string).setCreativeTab(Steamcraft.tabTools).setTextureName("steamcraft:pick"+string);
 		GameRegistry.registerItem(pick, "pick"+string);
 		tools.put("pick"+string,pick);
 		
    	Item axe = new ItemSteamcraftAxe(tool, repair).setUnlocalizedName("steamcraft:axe"+string).setCreativeTab(Steamcraft.tabTools).setTextureName("steamcraft:axe"+string);
 		GameRegistry.registerItem(axe, "axe"+string);
 		tools.put("axe"+string,axe);
 		
    	Item shovel = new ItemSteamcraftShovel(tool, repair).setUnlocalizedName("steamcraft:shovel"+string).setCreativeTab(Steamcraft.tabTools).setTextureName("steamcraft:shovel"+string);
 		GameRegistry.registerItem(shovel, "shovel"+string);
 		tools.put("shovel"+string,shovel);
 		
 	   	Item hoe = new ItemSteamcraftHoe(tool, repair).setUnlocalizedName("steamcraft:hoe"+string).setCreativeTab(Steamcraft.tabTools).setTextureName("steamcraft:hoe"+string);
 		GameRegistry.registerItem(hoe, "hoe"+string);
 		tools.put("hoe"+string,hoe);
 		
 	   	Item sword = new ItemSteamcraftSword(tool, repair).setUnlocalizedName("steamcraft:sword"+string).setCreativeTab(Steamcraft.tabTools).setTextureName("steamcraft:sword"+string);
 		GameRegistry.registerItem(sword, "sword"+string);
 		tools.put("sword"+string,sword);
 		if (addRecipes) {
 			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(pick), "xxx", " s ", " s ", 
 			        'x', repair, 's', "stickWood"));
 			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(axe), "xx", "xs", " s", 
 			        'x', repair, 's', "stickWood"));
 			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(shovel), "x", "s", "s", 
 			        'x', repair, 's', "stickWood"));
 			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(hoe), "xx", " s", " s", 
 			        'x', repair, 's', "stickWood"));
 			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(sword), "x", "x", "s", 
 			        'x', repair, 's', "stickWood"));
 		}
    }
    
    public static void registerArmorSet(ItemArmor.ArmorMaterial tool, String string, Object repair, boolean addRecipes) {
    	Item helm = new ItemSteamcraftArmor(tool, 2, 0, repair, string).setUnlocalizedName("steamcraft:helm"+string).setCreativeTab(Steamcraft.tabTools).setTextureName("steamcraft:helm"+string);
 		GameRegistry.registerItem(helm, "helm"+string);
 		tools.put("helm"+string,helm);
 		
    	Item chest = new ItemSteamcraftArmor(tool, 2, 1, repair, string).setUnlocalizedName("steamcraft:chest"+string).setCreativeTab(Steamcraft.tabTools).setTextureName("steamcraft:chest"+string);
 		GameRegistry.registerItem(chest, "chest"+string);
 		tools.put("chest"+string,chest);
 		
    	Item legs = new ItemSteamcraftArmor(tool, 2, 2, repair, string).setUnlocalizedName("steamcraft:legs"+string).setCreativeTab(Steamcraft.tabTools).setTextureName("steamcraft:legs"+string);
 		GameRegistry.registerItem(legs, "legs"+string);
 		tools.put("legs"+string,legs);
 		
    	Item feet = new ItemSteamcraftArmor(tool, 2, 3, repair, string).setUnlocalizedName("steamcraft:feet"+string).setCreativeTab(Steamcraft.tabTools).setTextureName("steamcraft:feet"+string);
 		GameRegistry.registerItem(feet, "feet"+string);
 		tools.put("feet"+string,feet);
 		
 		if (addRecipes) {
 			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(helm), "xxx", "x x", 
 			        'x', repair));
 			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(chest), "x x", "xxx", "xxx",
 			        'x', repair));
 			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(legs), "xxx", "x x", "x x",
 			        'x', repair));
 			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(feet), "x x", "x x",
 			        'x', repair));
 		}
    }
    
    public static void registerGildedTools() {
		ToolMaterial tool = EnumHelper.addToolMaterial("GILDEDGOLD", 2, 250, 6.0F, 2.0F, 22);
		
    	Item pick = new ItemSteamcraftPickaxe(tool, new ItemStack(steamcraftIngot,1,3)).setUnlocalizedName("steamcraft:pickGildedGold").setCreativeTab(Steamcraft.tabTools).setTextureName("gold_pickaxe");
 		GameRegistry.registerItem(pick, "pickGildedGold");
 		tools.put("pickGildedGold",pick);
 		
    	Item axe = new ItemSteamcraftAxe(tool, new ItemStack(steamcraftIngot,1,3)).setUnlocalizedName("steamcraft:axeGildedGold").setCreativeTab(Steamcraft.tabTools).setTextureName("gold_axe");
 		GameRegistry.registerItem(axe, "axeGildedGold");
 		tools.put("axeGildedGold",axe);
 		
    	Item shovel = new ItemSteamcraftShovel(tool, new ItemStack(steamcraftIngot,1,3)).setUnlocalizedName("steamcraft:shovelGildedGold").setCreativeTab(Steamcraft.tabTools).setTextureName("gold_shovel");
 		GameRegistry.registerItem(shovel, "shovelGildedGold");
 		tools.put("shovelGildedGold",shovel);
 		
 	   	Item hoe = new ItemSteamcraftHoe(tool, new ItemStack(steamcraftIngot,1,3)).setUnlocalizedName("steamcraft:hoeGildedGold").setCreativeTab(Steamcraft.tabTools).setTextureName("gold_hoe");
 		GameRegistry.registerItem(hoe, "hoeGildedGold");
 		tools.put("hoeGildedGold",hoe);
 		
 	   	Item sword = new ItemSteamcraftSword(tool, new ItemStack(steamcraftIngot,1,3)).setUnlocalizedName("steamcraft:swordGildedGold").setCreativeTab(Steamcraft.tabTools).setTextureName("gold_sword");
 		GameRegistry.registerItem(sword, "swordGildedGold");
 		tools.put("swordGildedGold",sword);
 		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(pick), "xxx", " s ", " s ", 
		        'x', new ItemStack(steamcraftIngot,1,3), 's', "stickWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(axe), "xx", "xs", " s", 
		        'x', new ItemStack(steamcraftIngot,1,3), 's', "stickWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(shovel), "x", "s", "s", 
		        'x', new ItemStack(steamcraftIngot,1,3), 's', "stickWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(hoe), "xx", " s", " s", 
		        'x', new ItemStack(steamcraftIngot,1,3), 's', "stickWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(sword), "x", "x", "s", 
		        'x', new ItemStack(steamcraftIngot,1,3), 's', "stickWood"));
    }
    
    public static void registerGildedArmor() {
		ItemArmor.ArmorMaterial tool = EnumHelper.addArmorMaterial("GILDEDGOLD",15, new int[]{2, 6, 5, 2}, 9);

    	Item helm = new ItemSteamcraftArmor(tool, 2, 0, new ItemStack(steamcraftIngot,1,3), "Gilded").setUnlocalizedName("steamcraft:helmGildedGold").setCreativeTab(Steamcraft.tabTools).setTextureName("gold_helmet");
 		GameRegistry.registerItem(helm, "helmGildedGold");
 		tools.put("helmGildedGold",helm);
 		
    	Item chest = new ItemSteamcraftArmor(tool, 2, 1, new ItemStack(steamcraftIngot,1,3), "Gilded").setUnlocalizedName("steamcraft:chestGildedGold").setCreativeTab(Steamcraft.tabTools).setTextureName("gold_chestplate");
 		GameRegistry.registerItem(chest, "chestGildedGold");
 		tools.put("chestGildedGold",chest);
 		
    	Item legs = new ItemSteamcraftArmor(tool, 2, 2, new ItemStack(steamcraftIngot,1,3), "Gilded").setUnlocalizedName("steamcraft:legsGildedGold").setCreativeTab(Steamcraft.tabTools).setTextureName("gold_leggings");
 		GameRegistry.registerItem(legs, "legsGildedGold");
 		tools.put("legsGildedGold",legs);
 		
    	Item feet = new ItemSteamcraftArmor(tool, 2, 3, new ItemStack(steamcraftIngot,1,3), "Gilded").setUnlocalizedName("steamcraft:feetGildedGold").setCreativeTab(Steamcraft.tabTools).setTextureName("gold_boots");
 		GameRegistry.registerItem(feet, "feetGildedGold");
 		tools.put("feetGildedGold",feet);
 		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(helm), "xxx", "x x", 
		        'x', new ItemStack(steamcraftIngot,1,3)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(chest), "x x", "xxx", "xxx",
		        'x', new ItemStack(steamcraftIngot,1,3)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(legs), "xxx", "x x", "x x",
		        'x', new ItemStack(steamcraftIngot,1,3)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(feet), "x x", "x x",
		        'x', new ItemStack(steamcraftIngot,1,3)));
 		
    }
    
    public static Item pick(String string) {
    	return tools.get("pick"+string);
    } 
    public static Item axe(String string) {
    	return tools.get("axe"+string);
    }
    public static Item shovel(String string) {
    	return tools.get("shovel"+string);
    }
    public static Item hoe(String string) {
    	return tools.get("hoe"+string);
    }
    public static Item sword(String string) {
    	return tools.get("sword"+string);
    }
    
    public static Item helm(String string) {
    	return tools.get("helm"+string);
    }
    public static Item chest(String string) {
    	return tools.get("chest"+string);
    }
    public static Item legs(String string) {
    	return tools.get("legs"+string);
    }
    public static Item feet(String string) {
    	return tools.get("feet"+string);
    }
    
    public static void reregisterPlates(){
    	String[] plates = {"Iron","Copper","Zinc","Brass","Thaumium","Gold"};
    	for (String plate : plates){
    		ArrayList<ItemStack> items = OreDictionary.getOres("plate"+plate);
    		for (ItemStack item : items){
    			OreDictionary.registerOre("plateSteamcraft"+plate, item);
    		}
    	}
    	
    }
    
}
