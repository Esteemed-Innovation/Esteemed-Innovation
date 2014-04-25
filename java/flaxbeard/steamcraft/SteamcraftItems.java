package flaxbeard.steamcraft;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;
import flaxbeard.steamcraft.api.ICrucibleMold;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.enhancement.IEnhancement;
import flaxbeard.steamcraft.item.ItemEnhancementFireMusket;
import flaxbeard.steamcraft.item.ItemExosuitArmor;
import flaxbeard.steamcraft.item.ItemFirearm;
import flaxbeard.steamcraft.item.ItemIngotMold;
import flaxbeard.steamcraft.item.ItemNuggetMold;
import flaxbeard.steamcraft.item.ItemPlateMold;
import flaxbeard.steamcraft.item.ItemSpyglass;
import flaxbeard.steamcraft.item.ItemSteamcraftBook;
import flaxbeard.steamcraft.item.ItemSteamcraftIngot;
import flaxbeard.steamcraft.item.ItemSteamcraftNugget;
import flaxbeard.steamcraft.item.ItemSteamcraftPlate;

public class SteamcraftItems {
	public static Item musketCartridge;
    public static Item musket;
    public static Item pistol;
    public static Item revolver;
    public static Item blunderbuss;
    public static Item spyglass;
    
    public static Item blankMold;
    public static Item ingotMold;
    public static Item nuggetMold;
    public static Item plateMold;
    
    public static Item steamcraftIngot;
    public static Item steamcraftNugget;
    public static Item steamcraftPlate;
    
    public static Item enhancementAblaze;
    
    public static Item book;

    public static Item exoArmorHead;
    public static Item exoArmorBody;
    public static Item exoArmorLegs;
    public static Item exoArmorFeet;
    
    public static void registerItems() {
		
		book = new ItemSteamcraftBook().setUnlocalizedName("steamcraft:book").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:book");
		GameRegistry.registerItem(book, "book");
		
    	musketCartridge = new Item().setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:musketCartridge").setTextureName("steamcraft:cartridge");
		GameRegistry.registerItem(musketCartridge, "musketCartridge");
		musket = new ItemFirearm(20.0F, 84,0.2F, 5.0F, false, 1).setUnlocalizedName("steamcraft:musket").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:weaponMusket");
		GameRegistry.registerItem(musket, "musket");
		pistol = new ItemFirearm(15.0F, 42,0.5F, 2.0F, false, 1).setUnlocalizedName("steamcraft:pistol").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:weaponPistol");
		GameRegistry.registerItem(pistol, "pistol");
		revolver = new ItemFirearm(12.5F, 84,0.7F, 1.0F, false, 6).setUnlocalizedName("steamcraft:revolver").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:weaponRevolver");
		GameRegistry.registerItem(revolver, "revolver");
		blunderbuss = new ItemFirearm(25.0F, 95,3.5F, 7.5F, true, 1).setUnlocalizedName("steamcraft:blunderbuss").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:weaponBlunderbuss");
		GameRegistry.registerItem(blunderbuss, "blunderbuss");
		spyglass = new ItemSpyglass().setUnlocalizedName("steamcraft:spyglass").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:spyglass");
		GameRegistry.registerItem(spyglass, "spyglass");
		SteamcraftRegistry.registerEnhancement((IEnhancement) spyglass);
		enhancementAblaze = new ItemEnhancementFireMusket().setUnlocalizedName("steamcraft:enhancementAblaze").setCreativeTab(Steamcraft.tab).setTextureName("steamcraft:enhancementAblaze");
		GameRegistry.registerItem(enhancementAblaze, "enhancementAblaze");
		SteamcraftRegistry.registerEnhancement((IEnhancement) enhancementAblaze);

		
		exoArmorHead = new ItemExosuitArmor(0).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:exoArmorHead").setTextureName("steamcraft:exoArmorHead");
		GameRegistry.registerItem(exoArmorHead, "exoArmorHead");
		exoArmorBody = new ItemExosuitArmor(1).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:exoArmorBody").setTextureName("steamcraft:exoArmorBody");
		GameRegistry.registerItem(exoArmorBody, "exoArmorBody");
		exoArmorLegs = new ItemExosuitArmor(2).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:exoArmorLegs").setTextureName("steamcraft:exoArmorLegs");
		GameRegistry.registerItem(exoArmorLegs, "exoArmorLegs");
		exoArmorFeet = new ItemExosuitArmor(3).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:exoArmorFeet").setTextureName("steamcraft:exoArmorFeet");
		GameRegistry.registerItem(exoArmorFeet, "exoArmorFeet");
		
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
		
		steamcraftIngot = new ItemSteamcraftIngot().setUnlocalizedName("steamcraft:ingot").setCreativeTab(Steamcraft.tab);
		GameRegistry.registerItem(steamcraftIngot, "steamcraftIngot");
		OreDictionary.registerOre("ingotCopper", new ItemStack(steamcraftIngot,1,0));
		OreDictionary.registerOre("ingotZinc", new ItemStack(steamcraftIngot,1,1));
		OreDictionary.registerOre("ingotBrass", new ItemStack(steamcraftIngot,1,2));
		
		steamcraftNugget = new ItemSteamcraftNugget().setUnlocalizedName("steamcraft:nugget").setCreativeTab(Steamcraft.tab);
		GameRegistry.registerItem(steamcraftNugget, "steamcraftNugget");
		OreDictionary.registerOre("nuggetCopper", new ItemStack(steamcraftNugget,1,0));
		OreDictionary.registerOre("nuggetZinc", new ItemStack(steamcraftNugget,1,1));
		OreDictionary.registerOre("nuggetIron", new ItemStack(steamcraftNugget,1,2));
		OreDictionary.registerOre("nuggetBrass", new ItemStack(steamcraftNugget,1,3));
		
		steamcraftPlate = new ItemSteamcraftPlate().setUnlocalizedName("steamcraft:plate").setCreativeTab(Steamcraft.tab);
		GameRegistry.registerItem(steamcraftPlate, "steamcraftPlate");
		OreDictionary.registerOre("plateCopper", new ItemStack(steamcraftPlate,1,0));
		OreDictionary.registerOre("plateZinc", new ItemStack(steamcraftPlate,1,1));
		OreDictionary.registerOre("plateIron", new ItemStack(steamcraftPlate,1,2));
		OreDictionary.registerOre("plateGold", new ItemStack(steamcraftPlate,1,3));
		OreDictionary.registerOre("plateBrass", new ItemStack(steamcraftPlate,1,4));
    }
}
