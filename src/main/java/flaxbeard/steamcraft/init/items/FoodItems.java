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
        STEAMED_FISH(new ItemSteamedFood((ItemFood) COOKED_FISH), "steamed_fish"),
        STEAMED_SALMON(new ItemSteamedFood(new ItemStack(COOKED_FISH, 1, 1)), "steamed_salmon"),
        STEAMED_CHICKEN(new ItemSteamedFood((ItemFood) COOKED_CHICKEN), "steamed_chicken"),
        STEAMED_BEEF(new ItemSteamedFood((ItemFood) COOKED_BEEF), "steamed_beef"),
        STEAMED_PORKCHOP(new ItemSteamedFood((ItemFood) COOKED_PORKCHOP), "steamed_porkchop"),
        STEAMED_MUTTON(new ItemSteamedFood((ItemFood) COOKED_MUTTON), "steamed_mutton"),
        STEAMED_RABBIT(new ItemSteamedFood((ItemFood) COOKED_RABBIT), "steamed_rabbit");

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

        // Right now all food items are meat, be careful of this in the future if adding more foods.
        for (Items item : Items.values()) {
            OreDictionary.registerOre(ALL_MEAT_COOKED, item.getItem());
        }
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
                case STEAMED_MUTTON: {
                    SteamcraftRegistry.addSteamingRecipe(COOKED_MUTTON, item.getItem());
                    break;
                }
                case STEAMED_RABBIT: {
                    SteamcraftRegistry.addSteamingRecipe(COOKED_RABBIT, item.getItem());
                    break;
                }
            }
        }
    }
}
