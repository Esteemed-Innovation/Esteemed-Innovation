package flaxbeard.steamcraft.data.village;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.init.blocks.SteamNetworkBlocks;
import flaxbeard.steamcraft.init.items.CraftingComponentItems;
import flaxbeard.steamcraft.init.items.MetalItems;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SteamEngineerCareer extends VillagerRegistry.VillagerCareer {
    private static ItemStack BRASS_PLATE = MetalItems.Items.BRASS_PLATE.createItemStack();
    private static ItemStack TURBINE = CraftingComponentItems.Items.BRASS_TURBINE.createItemStack();
    private static ItemStack PISTON = CraftingComponentItems.Items.BRASS_PISTON.createItemStack();
    private static Item PIPE = Item.getItemFromBlock(SteamNetworkBlocks.Blocks.PIPE.getBlock());
    private static final Random RANDOM = new Random();

    public SteamEngineerCareer() {
        this(Steamcraft.STEAM_ENGINEER_PROFESSION, "steam_engineer");
    }

    public SteamEngineerCareer(VillagerRegistry.VillagerProfession parent, String name) {
        super(parent, name);
    }

    @Override
    public List<EntityVillager.ITradeList> getTrades(int level) {
        return Arrays.asList(new EntityVillager.ITradeList[] {
          new ItemStackForItemStack(new ItemStack(Items.EMERALD, 2), TURBINE),
          new ItemStackForItemStack(new ItemStack(Items.EMERALD, 2), PISTON),
          new ItemStackForItemStack(new ItemStack(Items.EMERALD), new ItemStack(PIPE, 2)),
          new ItemStackForItemStack(BRASS_PLATE, new ItemStack(Items.EMERALD, RANDOM.nextInt(2) + 8))
        });
    }

    /**
     * Probably reinventing the wheel here, but the existing ITradeLists are really, really confusing in their intent.
     */
    private static class ItemStackForItemStack implements EntityVillager.ITradeList {
        private ItemStack in;
        private ItemStack out;

        public ItemStackForItemStack(ItemStack in, ItemStack out) {
            this.in = in;
            this.out = out;
        }

        @Override
        public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random) {
            recipeList.add(new MerchantRecipe(in, out));
        }
    }
}
