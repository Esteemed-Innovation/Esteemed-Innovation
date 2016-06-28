package flaxbeard.steamcraft.world;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

import flaxbeard.steamcraft.init.blocks.SteamNetworkBlocks;
import flaxbeard.steamcraft.init.items.CraftingComponentItems;
import flaxbeard.steamcraft.init.items.MetalItems;

import java.util.Random;

public class SteampunkVillagerTradeHandler implements IVillageTradeHandler {

    @Override
    public void manipulateTradesForVillager(EntityVillager villager,
                                            MerchantRecipeList recipeList, Random random) {
        recipeList.add(new MerchantRecipe(MetalItems.Items.BRASS_PLATE.createItemStack(8 + random.nextInt(2)), new ItemStack(Items.EMERALD)));
        recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 2), CraftingComponentItems.Items.BRASS_PISTON.createItemStack()));
        recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, 2), CraftingComponentItems.Items.BRASS_TURBINE.createItemStack()));
        recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD), new ItemStack(SteamNetworkBlocks.Blocks.PIPE.getBlock(), 2)));

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
