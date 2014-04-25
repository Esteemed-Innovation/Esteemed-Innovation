package flaxbeard.steamcraft;

import java.util.ArrayList;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.ArrayUtils;

import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.book.BookPageAlloy;
import flaxbeard.steamcraft.api.book.BookPageCrafting;
import flaxbeard.steamcraft.api.book.BookPageItem;
import flaxbeard.steamcraft.api.book.BookPageText;

public class SteamcraftBook {
	public static void registerBookResearch() {
		SteamcraftRegistry.addCategory("category.Basics.name");
		SteamcraftRegistry.addResearch("research.Book.name","category.Basics.name",new BookPageItem("research.Book.name","research.Book.0", new ItemStack(SteamcraftItems.book)),new BookPageCrafting("",new ItemStack(SteamcraftItems.book),new ItemStack(Items.book),new ItemStack(SteamcraftBlocks.steamcraftOre,1,0),null,new ItemStack(SteamcraftBlocks.steamcraftOre,1,1)));
		SteamcraftRegistry.addResearch("research.Ores.name","category.Basics.name",new BookPageItem("research.Ores.name","research.Ores.0", new ItemStack(SteamcraftBlocks.steamcraftOre,1,0), new ItemStack(SteamcraftBlocks.steamcraftOre,1,1)));
		
		SteamcraftRegistry.addCategory("category.MetalCasting.name");
		ItemStack[] plankOredict = getOreDict("plankWood");
		SteamcraftRegistry.addResearch("research.Crucible.name","category.MetalCasting.name",new BookPageItem("research.Crucible.name","research.Crucible.0", new ItemStack(SteamcraftBlocks.crucible)),new BookPageText("research.Crucible.name","research.Crucible.1"),new BookPageCrafting("",new ItemStack(SteamcraftBlocks.crucible),
				new ItemStack(Items.brick), null, new ItemStack(Items.brick),
				new ItemStack(Items.brick), null, new ItemStack(Items.brick),
				new ItemStack(Items.brick), new ItemStack(Items.brick), new ItemStack(Items.brick)));
		SteamcraftRegistry.addResearch("research.Mold.name","category.MetalCasting.name",new BookPageItem("research.Mold.name","research.Mold.0", new ItemStack(SteamcraftBlocks.mold)),new BookPageText("research.Mold.name","research.Mold.1"),new BookPageCrafting("",new ItemStack(SteamcraftBlocks.mold),
				new ItemStack(Items.brick), new ItemStack(Items.brick), new ItemStack(Items.brick),
				new ItemStack(Items.brick), null, new ItemStack(Items.brick),
				new ItemStack(Items.brick), new ItemStack(Items.brick), new ItemStack(Items.brick)));
		SteamcraftRegistry.addResearch("research.Molds.name","category.MetalCasting.name",new BookPageItem("research.Molds.name","research.Molds.0", new ItemStack(SteamcraftItems.plateMold), new ItemStack(SteamcraftItems.ingotMold), new ItemStack(SteamcraftItems.nuggetMold)),new BookPageCrafting("",new ItemStack(SteamcraftItems.blankMold),
				new ItemStack(Items.brick), new ItemStack(Items.brick)),new BookPageCrafting("",new ItemStack(SteamcraftBlocks.carving),
				plankOredict, new ItemStack(SteamcraftItems.blankMold), plankOredict,
				plankOredict, null, plankOredict,
				plankOredict, plankOredict, plankOredict));
		SteamcraftRegistry.addResearch("research.Plates.name","category.MetalCasting.name",new BookPageItem("research.Plates.name","research.Plates.0", new ItemStack(SteamcraftItems.steamcraftPlate,1,0), new ItemStack(SteamcraftItems.steamcraftPlate,1,1), new ItemStack(SteamcraftItems.steamcraftPlate,1,2), new ItemStack(SteamcraftItems.steamcraftPlate,1,3)));
		SteamcraftRegistry.addResearch("research.Brass.name","category.MetalCasting.name",new BookPageItem("research.Brass.name","research.Brass.0", new ItemStack(SteamcraftItems.steamcraftIngot,1,2)),new BookPageAlloy("",SteamcraftRecipes.liquidBrass,SteamcraftRecipes.liquidBrass.recipe));
		

		SteamcraftRegistry.addCategory("category.SteamPower.name");
		ItemStack[] brassIngotPlate = ArrayUtils.addAll(getOreDict("ingotBrass"), getOreDict("plateBrass"));
		SteamcraftRegistry.addResearch("research.Boiler.name","category.SteamPower.name",new BookPageItem("research.Boiler.name","research.Boiler.0", new ItemStack(SteamcraftBlocks.boiler)),new BookPageCrafting("",new ItemStack(SteamcraftBlocks.boiler),
				brassIngotPlate, brassIngotPlate, brassIngotPlate,
				brassIngotPlate, new ItemStack(Blocks.furnace), brassIngotPlate,
				brassIngotPlate, brassIngotPlate, brassIngotPlate));
		SteamcraftRegistry.addResearch("research.Pipe.name","category.SteamPower.name",new BookPageItem("research.Pipe.name","research.Pipe.0", new ItemStack(SteamcraftBlocks.pipe)),new BookPageCrafting("",new ItemStack(SteamcraftBlocks.pipe,4,0),
				brassIngotPlate, brassIngotPlate, brassIngotPlate,
				null,  null, null,
				brassIngotPlate, brassIngotPlate, brassIngotPlate));
		ItemStack[] nuggetCopper = getOreDict("nuggetCopper");
		SteamcraftRegistry.addResearch("research.Heater.name","category.SteamPower.name",new BookPageItem("research.Heater.name","research.Heater.0", new ItemStack(SteamcraftBlocks.heater)),new BookPageCrafting("",new ItemStack(SteamcraftBlocks.heater),
				nuggetCopper, nuggetCopper, nuggetCopper,
				brassIngotPlate,  new ItemStack(Blocks.furnace), brassIngotPlate,
				null, new ItemStack(SteamcraftBlocks.pipe), null));

	}
	
	private static ItemStack[] getOreDict(String str) {
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
