package flaxbeard.steamcraft;

import java.util.ArrayList;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.Loader;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.book.BookPageAlloy;
import flaxbeard.steamcraft.api.book.BookPageCrafting;
import flaxbeard.steamcraft.api.book.BookPageDip;
import flaxbeard.steamcraft.api.book.BookPageItem;
import flaxbeard.steamcraft.api.book.BookPageText;
import flaxbeard.steamcraft.integration.ThaumcraftIntegration;

public class SteamcraftBook {
	public static void registerBookResearch() {
		
		//Here's a secret for all of you addon devs: Setting the category of a research to the name of an existing research, with a ! at the beginning, will append to that research instead of making its own.
		SteamcraftRegistry.addCategory("category.Basics.name");
		SteamcraftRegistry.addResearch("research.Book.name","category.Basics.name",new BookPageItem("research.Book.name","research.Book.0", new ItemStack(SteamcraftItems.book)),new BookPageCrafting("","book"));
		SteamcraftRegistry.addResearch("research.Ores.name","category.Basics.name",new BookPageItem("research.Ores.name","research.Ores.0", new ItemStack(SteamcraftBlocks.steamcraftOre,1,0), new ItemStack(SteamcraftBlocks.steamcraftOre,1,1)));
		SteamcraftRegistry.addResearch("research.Bits.name","category.Basics.name",new BookPageItem("research.Bits.name","research.Bits.0", new ItemStack(SteamcraftItems.steamcraftCrafting,1,0),new ItemStack(SteamcraftItems.steamcraftCrafting,1,5)),new BookPageCrafting("","piston1","piston2"),new BookPageText("research.Bits.name","research.Bits.1"),new BookPageCrafting("","turbine1","turbine2"));
		SteamcraftRegistry.addResearch("research.Spyglass.name","category.Basics.name",new BookPageItem("research.Spyglass.name","research.Spyglass.0", new ItemStack(SteamcraftItems.spyglass)),new BookPageCrafting("","spyglass1","spyglass2"));
		if (Loader.isModLoaded("Baubles")) {
			SteamcraftRegistry.addResearch("research.Survivalist.name","category.Basics.name",new BookPageItem("research.Survivalist.name","research.SurvivalistBaubles.0", new ItemStack(SteamcraftItems.survivalist)),new BookPageCrafting("","survivalist"));
		}
		else
		{
			SteamcraftRegistry.addResearch("research.Survivalist.name","category.Basics.name",new BookPageItem("research.Survivalist.name","research.Survivalist.0", new ItemStack(SteamcraftItems.survivalist)),new BookPageCrafting("","survivalist"));
		}
		
		SteamcraftRegistry.addCategory("category.Flintlock.name");
		SteamcraftRegistry.addResearch("research.Parts.name","category.Flintlock.name",new BookPageItem("research.Parts.name","research.Parts.0", new ItemStack(SteamcraftItems.steamcraftCrafting,1,1), new ItemStack(SteamcraftItems.steamcraftCrafting,1,2), new ItemStack(SteamcraftItems.steamcraftCrafting,1,3), new ItemStack(SteamcraftItems.steamcraftCrafting,1,4)),
			new BookPageCrafting("","stock"),new BookPageCrafting("","barrel1","barrel2"),new BookPageCrafting("","blunderBarrel1","blunderBarrel2"),new BookPageCrafting("","flintlock1","flintlock2"));
		SteamcraftRegistry.addResearch("research.Musket.name","category.Flintlock.name",new BookPageItem("research.Musket.name","research.Musket.0", new ItemStack(SteamcraftItems.musket)),new BookPageCrafting("","cartridge1","cartridge2","cartridge3","cartridge4"),new BookPageCrafting("","musket"));
		SteamcraftRegistry.addResearch("research.Blunderbuss.name","category.Flintlock.name",new BookPageItem("research.Blunderbuss.name","research.Blunderbuss.0", new ItemStack(SteamcraftItems.blunderbuss)),new BookPageCrafting("","blunderbuss"));
		SteamcraftRegistry.addResearch("research.Pistol.name","category.Flintlock.name",new BookPageItem("research.Pistol.name","research.Pistol.0", new ItemStack(SteamcraftItems.pistol)),new BookPageCrafting("","pistol"));


		SteamcraftRegistry.addCategory("category.MetalCasting.name");
		SteamcraftRegistry.addResearch("research.Crucible.name","category.MetalCasting.name",new BookPageItem("research.Crucible.name","research.Crucible.0", new ItemStack(SteamcraftBlocks.crucible)),new BookPageText("research.Crucible.name","research.Crucible.1"),new BookPageCrafting("","crucible"));
		SteamcraftRegistry.addResearch("research.Mold.name","category.MetalCasting.name",new BookPageItem("research.Mold.name","research.Mold.0", new ItemStack(SteamcraftBlocks.mold)),new BookPageText("research.Mold.name","research.Mold.1"),new BookPageCrafting("","mold"));
		SteamcraftRegistry.addResearch("research.Molds.name","category.MetalCasting.name",new BookPageItem("research.Molds.name","research.Molds.0", new ItemStack(SteamcraftItems.plateMold), new ItemStack(SteamcraftItems.ingotMold), new ItemStack(SteamcraftItems.nuggetMold)),new BookPageCrafting("","blankMold"),new BookPageCrafting("","carving"));
		SteamcraftRegistry.addResearch("research.Plates.name","category.MetalCasting.name",new BookPageItem("research.Plates.name","research.Plates.0", new ItemStack(SteamcraftItems.steamcraftPlate,1,0), new ItemStack(SteamcraftItems.steamcraftPlate,1,1), new ItemStack(SteamcraftItems.steamcraftPlate,1,2), new ItemStack(SteamcraftItems.steamcraftPlate,1,3)));
		SteamcraftRegistry.addResearch("research.Brass.name","category.MetalCasting.name",new BookPageItem("research.Brass.name","research.Brass.0", new ItemStack(SteamcraftItems.steamcraftIngot,1,2)),new BookPageAlloy("",SteamcraftRecipes.liquidBrass,SteamcraftRecipes.liquidBrass.recipe));
		SteamcraftRegistry.addResearch("research.GildedGold.name","category.MetalCasting.name",new BookPageItem("research.GildedGold.name","research.GildedGold.0", new ItemStack(SteamcraftItems.steamcraftIngot,1,3)),new BookPageText("research.GildedGold.name","research.GildedGold.1"),new BookPageDip("",SteamcraftRecipes.liquidGold,1, new ItemStack(Items.iron_ingot), new ItemStack(SteamcraftItems.steamcraftIngot,1,3)));


		SteamcraftRegistry.addCategory("category.SteamPower.name");
		SteamcraftRegistry.addResearch("research.Boiler.name","category.SteamPower.name",new BookPageItem("research.Boiler.name","research.Boiler.0", new ItemStack(SteamcraftBlocks.boiler)),new BookPageCrafting("","boiler1","boiler2"));
		SteamcraftRegistry.addResearch("research.FlashBoiler.name", "category.SteamPower.name", new BookPageItem("research.FlashBoiler.name","research.FlashBoiler.0", new ItemStack(SteamcraftBlocks.flashBoiler)),new BookPageText("research.FlashBoiler.name","research.FlashBoiler.1"), new BookPageCrafting("","flashBoiler1","flashBoiler2"));
		SteamcraftRegistry.addResearch("research.Pipe.name","category.SteamPower.name",new BookPageItem("research.Pipe.name","research.Pipe.0", new ItemStack(SteamcraftBlocks.pipe)),new BookPageCrafting("","pipe1","pipe2"),new BookPageText("research.Pipe.name","research.Pipe.1"),new BookPageCrafting("","valvePipe"));
		SteamcraftRegistry.addResearch("research.RuptureDisc.name","category.SteamPower.name",new BookPageItem("research.RuptureDisc.name","research.RuptureDisc.0", new ItemStack(SteamcraftBlocks.ruptureDisc)),new BookPageText("research.RuptureDisc.name","research.RuptureDisc.1"),new BookPageCrafting("","disc"));
		SteamcraftRegistry.addResearch("research.Gauge.name","category.SteamPower.name",new BookPageItem("research.Gauge.name","research.Gauge.0", new ItemStack(SteamcraftBlocks.meter)),new BookPageCrafting("","gauge"));
		SteamcraftRegistry.addResearch("research.Tank.name","category.SteamPower.name",new BookPageItem("research.Tank.name","research.Tank.0", new ItemStack(SteamcraftBlocks.tank)),new BookPageCrafting("","tank1","tank2"));
		SteamcraftRegistry.addResearch("research.Filler.name","category.SteamPower.name",new BookPageItem("research.Filler.name","research.Filler.0", new ItemStack(SteamcraftBlocks.charger)),new BookPageCrafting("","filler1","filler2"));
		SteamcraftRegistry.addResearch("research.Heater.name","category.SteamPower.name",new BookPageItem("research.Heater.name","research.Heater.0", new ItemStack(SteamcraftBlocks.heater)),new BookPageCrafting("","heater1","heater2"));
		SteamcraftRegistry.addResearch("research.ItemMortar.name","category.SteamPower.name",new BookPageItem("research.ItemMortar.name","research.ItemMortar.0", new ItemStack(SteamcraftBlocks.itemMortar)),new BookPageText("research.ItemMortar.name","research.ItemMortar.1"),new BookPageCrafting("","astrolabe"),new BookPageCrafting("","itemMortar2","itemMortar3"));
		
		SteamcraftRegistry.addCategory("category.SteamPower2.name");
		SteamcraftRegistry.addResearch("research.Hammer.name","category.SteamPower2.name",new BookPageItem("research.Hammer.name","research.Hammer.0", new ItemStack(SteamcraftBlocks.hammer)),new BookPageText("research.Hammer.name","research.Hammer.1"),new BookPageCrafting("","hammer1","hammer2"));
		SteamcraftRegistry.addResearch("research.Screw.name","category.SteamPower2.name",new BookPageItem("research.Screw.name","research.Screw.0", new ItemStack(SteamcraftBlocks.pump)),new BookPageCrafting("","pump1","pump2"));
		SteamcraftRegistry.addResearch("research.Smasher.name", "category.SteamPower2.name", new BookPageItem("research.Smasher.name","research.Smasher.0", new ItemStack(SteamcraftBlocks.smasher)), new BookPageCrafting("","smasher1","smasher2","smasher3","smasher4"));
		SteamcraftRegistry.addResearch("research.Thumper.name", "category.SteamPower2.name", new BookPageItem("research.Thumper.name","research.Thumper.0", new ItemStack(SteamcraftBlocks.thumper)),new BookPageText("research.Thumper.name","research.Thumper.1"), new BookPageCrafting("","thumper1","thumper2"));
		
		SteamcraftRegistry.addCategory("category.SteamTools.name");
		SteamcraftRegistry.addResearch("research.SteamTools.name", "category.SteamTools.name", new BookPageItem("research.SteamTools.name","research.SteamTools.0", new ItemStack(SteamcraftItems.steamDrill), new ItemStack(SteamcraftItems.steamAxe), new ItemStack(SteamcraftItems.steamShovel)),new BookPageText("research.SteamTools.name","research.SteamTools.1"), new BookPageCrafting("","drill1","drill2","drill3","drill4"),
				new BookPageCrafting("","axe1","axe2","axe3","axe4"),
				new BookPageCrafting("","shovel1","shovel2","shovel3","shovel4"));

		SteamcraftRegistry.addCategory("category.Exosuit.name");
		SteamcraftRegistry.addResearch("research.Exosuit.name","category.Exosuit.name",new BookPageItem("research.Exosuit.name","research.Exosuit.0", new ItemStack(SteamcraftItems.exoArmorHead), new ItemStack(SteamcraftItems.exoArmorBody), new ItemStack(SteamcraftItems.exoArmorLegs), new ItemStack(SteamcraftItems.exoArmorFeet)),
				new BookPageText("research.Exosuit.name","research.Exosuit.1"),new BookPageCrafting("","engineering1","engineering2"),new BookPageCrafting("","exoHead"),new BookPageCrafting("","exoBody"),new BookPageCrafting("","exoLegs"),new BookPageCrafting("","exoFeet"));
		SteamcraftRegistry.addResearch("research.ExoPlates.name","category.Exosuit.name",new BookPageText("research.ExoPlates.name","research.ExoPlates.0"));
		SteamcraftRegistry.addResearch("research.PlateCopper.name","!research.ExoPlates.name",new BookPageItem("research.PlateCopper.name","research.PlateCopper.0", true, new ItemStack(SteamcraftItems.steamcraftPlate,1,0)));

		SteamcraftRegistry.addResearch("research.Jetpack.name","category.Exosuit.name",new BookPageItem("research.Jetpack.name","research.Jetpack.0", new ItemStack(SteamcraftItems.jetpack)),new BookPageCrafting("","jetpack1","jetpack2"));
		SteamcraftRegistry.addResearch("research.Wings.name","category.Exosuit.name",new BookPageItem("research.Wings.name","research.Wings.0", new ItemStack(SteamcraftItems.wings)),new BookPageCrafting("","wings1","wings2"));
		SteamcraftRegistry.addResearch("research.Thrusters.name","category.Exosuit.name",new BookPageItem("research.Thrusters.name","research.Thrusters.0", new ItemStack(SteamcraftItems.thrusters)),new BookPageCrafting("","thrusters1","thrusters"));
		if (Loader.isModLoaded("Thaumcraft")) {
			SteamcraftRegistry.addResearch("research.Mask.name","category.Exosuit.name",new BookPageItem("research.Mask.name","research.Mask.0", new ItemStack(ThaumcraftIntegration.goggleUpgrade)),new BookPageCrafting("","mask"));
		}
		SteamcraftRegistry.addResearch("research.Fist.name","category.Exosuit.name",new BookPageItem("research.Fist.name","research.Fist.0", new ItemStack(SteamcraftItems.powerFist)),new BookPageCrafting("","powerFist1","powerFist2"));
		SteamcraftRegistry.addResearch("research.FallAssist.name","category.Exosuit.name",new BookPageItem("research.FallAssist.name","research.FallAssist.0", new ItemStack(SteamcraftItems.fallAssist)),new BookPageCrafting("","noFall"));
	}
	
	public static ItemStack[] getOreDict(String str) {
		ArrayList<ItemStack> planks = new ArrayList<ItemStack>();
		for (ItemStack stack : OreDictionary.getOres(str)) {
			planks.add(stack);
		}
		if (str == "plankWood") {
			planks.add(new ItemStack(Blocks.planks,1,1));
			planks.add(new ItemStack(Blocks.planks,1,2));
			planks.add(new ItemStack(Blocks.planks,1,3));
			planks.add(new ItemStack(Blocks.planks,1,4));
			planks.add(new ItemStack(Blocks.planks,1,5));
		}
		return planks.toArray(new ItemStack[0]);
	}
}
