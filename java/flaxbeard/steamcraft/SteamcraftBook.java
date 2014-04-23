package flaxbeard.steamcraft;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
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
		SteamcraftRegistry.addResearch("research.Crucible.name","category.MetalCasting.name",new BookPageItem("research.Crucible.name","research.Crucible.0", new ItemStack(SteamcraftBlocks.crucible)),new BookPageText("research.Crucible.name","research.Crucible.1"),new BookPageCrafting("",new ItemStack(SteamcraftBlocks.crucible),
				new ItemStack(Items.brick), null, new ItemStack(Items.brick),
				new ItemStack(Items.brick), null, new ItemStack(Items.brick),
				new ItemStack(Items.brick), new ItemStack(Items.brick), new ItemStack(Items.brick)));
		SteamcraftRegistry.addResearch("research.Mold.name","category.MetalCasting.name",new BookPageItem("research.Mold.name","research.Mold.0", new ItemStack(SteamcraftBlocks.mold)),new BookPageText("research.Mold.name","research.Mold.1"),new BookPageCrafting("",new ItemStack(SteamcraftBlocks.mold),
				new ItemStack(Items.brick), new ItemStack(Items.brick), new ItemStack(Items.brick),
				new ItemStack(Items.brick), null, new ItemStack(Items.brick),
				new ItemStack(Items.brick), new ItemStack(Items.brick), new ItemStack(Items.brick)));
		SteamcraftRegistry.addResearch("research.Brass.name","category.MetalCasting.name",new BookPageItem("research.Brass.name","research.Brass.0", new ItemStack(SteamcraftItems.steamcraftIngot,1,2)),new BookPageAlloy("",SteamcraftRecipes.liquidBrass,SteamcraftRecipes.liquidBrass.recipe));
	}
}
