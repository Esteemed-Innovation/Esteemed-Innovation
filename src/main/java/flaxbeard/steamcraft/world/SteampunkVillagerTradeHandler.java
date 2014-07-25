package flaxbeard.steamcraft.world;

import java.util.Random;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import cpw.mods.fml.common.registry.VillagerRegistry.IVillageTradeHandler;
import flaxbeard.steamcraft.SteamcraftBlocks;
import flaxbeard.steamcraft.SteamcraftItems;

public class SteampunkVillagerTradeHandler implements IVillageTradeHandler {

	@Override
	public void manipulateTradesForVillager(EntityVillager villager,
			MerchantRecipeList recipeList, Random random) {
		recipeList.add(new MerchantRecipe(new ItemStack(SteamcraftItems.steamcraftPlate, 8+random.nextInt(2), 4), new ItemStack(Items.emerald)));
		recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald, 2), new ItemStack(SteamcraftItems.steamcraftCrafting,1,5)));
		recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald, 2), new ItemStack(SteamcraftItems.steamcraftCrafting,1,0)));
		recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald), new ItemStack(SteamcraftBlocks.pipe, 2)));

//		recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald), new ItemStack(ConfigItems.itemResource, 1, 9)));
//		recipeList.add(new MerchantRecipe(new ItemStack(ConfigItems.itemResource, 4 + random.nextInt(3), 3), new ItemStack(Items.emerald)));
//		recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald), new ItemStack(ConfigItems.itemResource, 1, 0)));
//		recipeList.add(new MerchantRecipe(new ItemStack(ConfigItems.itemResource, 4 + random.nextInt(3), 6), new ItemStack(Items.emerald)));
//		recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald), new ItemStack(ConfigItems.itemResource, 1, 1)));
//		recipeList.add(new MerchantRecipe(new ItemStack(ConfigItems.itemNuggetChicken, 24 + random.nextInt(8), 0), new ItemStack(Items.emerald)));
//		recipeList.add(new MerchantRecipe(new ItemStack(Blocks.bookshelf, 2 + random.nextInt(2), 0), new ItemStack(ConfigItems.itemResource, 1, 9)));
//		recipeList.add(new MerchantRecipe(new ItemStack(ConfigItems.itemNuggetChicken, 24 + random.nextInt(8), 0), new ItemStack(Items.emerald)));
//		recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald), new ItemStack(ConfigItems.itemShard, 2 + random.nextInt(2), random.nextInt(6))));
//		recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald), new ItemStack(ConfigItems.itemManaBean, 1 + random.nextInt(2), 0)));
//		recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald), new ItemStack(ConfigItems.itemWispEssence, 1 + random.nextInt(2), 0)));
//		recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald, 7 + random.nextInt(3)), new ItemStack(ConfigItems.itemRingRunic, 1, 0)));
//		recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald, 8 + random.nextInt(3)), new ItemStack(ConfigItems.itemAmuletVis, 1, 0)));
//		recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald, 6 + random.nextInt(3)), new ItemStack(ConfigItems.itemBaubleBlanks, 1, 3 + random.nextInt(6))));
	}
}
