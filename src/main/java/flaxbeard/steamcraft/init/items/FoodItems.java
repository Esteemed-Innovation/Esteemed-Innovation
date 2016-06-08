package flaxbeard.steamcraft.init.items;

import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.init.IInitCategory;
import flaxbeard.steamcraft.item.ItemSteamedFood;

import static net.minecraft.init.Items.*;
import static flaxbeard.steamcraft.init.misc.OreDictEntries.*;

public class FoodItems implements IInitCategory {
    public enum Items {
        STEAMED_FISH(new ItemSteamedFood((ItemFood) COOKED_FISH), "steamedFish"),
        STEAMED_SALMON(new ItemSteamedFood(new ItemStack(COOKED_FISH, 1, 1)), "steamedSalmon"),
        STEAMED_CHICKEN(new ItemSteamedFood((ItemFood) COOKED_CHICKEN), "steamedChicken"),
        STEAMED_BEEF(new ItemSteamedFood((ItemFood) COOKED_BEEF), "steamedBeef"),
        STEAMED_PORKCHOP(new ItemSteamedFood((ItemFood) COOKED_PORKCHOP), "steamedPorkchop");

        private ItemSteamedFood item;

        Items(ItemSteamedFood item, String name) {
            item.setUnlocalizedName(Steamcraft.MOD_ID + ":" + name);
            item.setCreativeTab(Steamcraft.tab);
            item.setRegistryName(Steamcraft.MOD_ID, name);
            GameRegistry.register(item);
            this.item = item;
        }

        public ItemSteamedFood getItem() {
            return item;
        }
    }

    @Override
    public void oreDict() {
        OreDictionary.registerOre(ALL_FISH_COOKED, Items.STEAMED_FISH.getItem());
        OreDictionary.registerOre(ALL_FISH_COOKED, Items.STEAMED_SALMON.getItem());
        OreDictionary.registerOre(ALL_CHICKEN_COOKED, Items.STEAMED_CHICKEN.getItem());
        OreDictionary.registerOre(ALL_PORK_COOKED, Items.STEAMED_PORKCHOP.getItem());
        OreDictionary.registerOre(ALL_BEEF_COOKED, Items.STEAMED_BEEF.getItem());

        OreDictionary.registerOre(ALL_MEAT_COOKED, Items.STEAMED_BEEF.getItem());
        OreDictionary.registerOre(ALL_MEAT_COOKED, Items.STEAMED_CHICKEN.getItem());
        OreDictionary.registerOre(ALL_MEAT_COOKED, Items.STEAMED_PORKCHOP.getItem());
    }

    @Override
    public void recipes() {
        // No need to use a LOOKUP array because we don't disable any of the food, ever.
        for (Items item : Items.values()) {
            switch (item) {
                case STEAMED_BEEF: {
                    SteamcraftRegistry.addSteamingRecipe(COOKED_BEEF, item.getItem());
                    break;
                }
                case STEAMED_CHICKEN: {
                    SteamcraftRegistry.addSteamingRecipe(COOKED_CHICKEN, item.getItem());
                    break;
                }
                case STEAMED_FISH: {
                    SteamcraftRegistry.addSteamingRecipe(COOKED_FISH, item.getItem());
                    break;
                }
                case STEAMED_PORKCHOP: {
                    SteamcraftRegistry.addSteamingRecipe(COOKED_PORKCHOP, item.getItem());
                    break;
                }
                case STEAMED_SALMON: {
                    SteamcraftRegistry.addSteamingRecipe(COOKED_FISH, 1, item.getItem(), 0);
                    break;
                }
            }
        }
    }
}
