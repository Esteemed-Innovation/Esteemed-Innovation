package flaxbeard.steamcraft.init.items;

import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.SteamingRegistry;
import flaxbeard.steamcraft.init.IInitCategory;
import flaxbeard.steamcraft.item.ItemSteamedFood;

import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import static flaxbeard.steamcraft.init.misc.OreDictEntries.*;
import static net.minecraft.init.Items.*;

public class FoodItems implements IInitCategory {
    public enum Items {
        STEAMED_FISH(new ItemSteamedFood((ItemFood) COOKED_FISH), "steamed_fish"),
        STEAMED_SALMON(new ItemSteamedFood(new ItemStack(COOKED_FISH, 1, 1)), "steamed_salmon"),
        STEAMED_CHICKEN(new ItemSteamedFood((ItemFood) COOKED_CHICKEN), "steamed_chicken"),
        STEAMED_BEEF(new ItemSteamedFood((ItemFood) COOKED_BEEF), "steamed_beef"),
        STEAMED_PORKCHOP(new ItemSteamedFood((ItemFood) COOKED_PORKCHOP), "steamed_porkchop"),
        STEAMED_MUTTON(new ItemSteamedFood((ItemFood) COOKED_MUTTON), "steamed_mutton"),
        STEAMED_RABBIT(new ItemSteamedFood((ItemFood) COOKED_RABBIT), "steamed_rabbit"),
        STEAMED_CARROT(new ItemSteamedFood((ItemFood) CARROT), "steamed_carrot"),
        STEAMED_BEETROOT(new ItemSteamedFood((ItemFood) BEETROOT), "steamed_beetroot"),
        STEAMED_POTATO(new ItemSteamedFood((ItemFood) BAKED_POTATO), "steamed_potato");

        private Item item;

        Items(Item item, String name) {
            item.setUnlocalizedName(Steamcraft.MOD_ID + ":" + name);
            item.setCreativeTab(Steamcraft.tab);
            item.setRegistryName(Steamcraft.MOD_ID, name);
            GameRegistry.register(item);
            this.item = item;
        }

        public Item getItem() {
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
        OreDictionary.registerOre(ALL_MEAT_COOKED, Items.STEAMED_FISH.getItem());
        OreDictionary.registerOre(ALL_MEAT_COOKED, Items.STEAMED_SALMON.getItem());
        OreDictionary.registerOre(ALL_MEAT_COOKED, Items.STEAMED_CHICKEN.getItem());
        OreDictionary.registerOre(ALL_MEAT_COOKED, Items.STEAMED_PORKCHOP.getItem());
        OreDictionary.registerOre(ALL_MEAT_COOKED, Items.STEAMED_BEEF.getItem());

        OreDictionary.registerOre(ALL_VEGGIE, Items.STEAMED_CARROT.getItem());
        OreDictionary.registerOre(ALL_VEGGIE, Items.STEAMED_BEETROOT.getItem());
        OreDictionary.registerOre(ALL_VEGGIE, Items.STEAMED_POTATO.getItem());
    }

    @Override
    public void recipes() {
        // No need to use a LOOKUP array because we don't disable any of the food, ever.
        for (Items item : Items.values()) {
            switch (item) {
                case STEAMED_BEEF: {
                    SteamingRegistry.addSteamingRecipe(new ItemStack(BEEF), new ItemStack(item.getItem()));
                    break;
                }
                case STEAMED_CHICKEN: {
                    SteamingRegistry.addSteamingRecipe(new ItemStack(CHICKEN), new ItemStack(item.getItem()));
                    break;
                }
                case STEAMED_FISH: {
                    SteamingRegistry.addSteamingRecipe(new ItemStack(FISH), new ItemStack(item.getItem()));
                    break;
                }
                case STEAMED_PORKCHOP: {
                    SteamingRegistry.addSteamingRecipe(new ItemStack(PORKCHOP), new ItemStack(item.getItem()));
                    break;
                }
                case STEAMED_SALMON: {
                    SteamingRegistry.addSteamingRecipe(new ItemStack(FISH, 1, 1), new ItemStack(item.getItem()));
                    break;
                }
                case STEAMED_MUTTON: {
                    SteamingRegistry.addSteamingRecipe(new ItemStack(MUTTON), new ItemStack(item.getItem()));
                    break;
                }
                case STEAMED_RABBIT: {
                    SteamingRegistry.addSteamingRecipe(new ItemStack(RABBIT), new ItemStack(item.getItem()));
                    break;
                }
                case STEAMED_BEETROOT: {
                    SteamingRegistry.addSteamingRecipe(new ItemStack(BEETROOT), new ItemStack(item.getItem()));
                    break;
                }
                case STEAMED_CARROT: {
                    SteamingRegistry.addSteamingRecipe(new ItemStack(CARROT), new ItemStack(item.getItem()));
                    break;
                }
                case STEAMED_POTATO: {
                    SteamingRegistry.addSteamingRecipe(new ItemStack(POTATO), new ItemStack(item.getItem()));
                    break;
                }
            }
        }
    }
}
