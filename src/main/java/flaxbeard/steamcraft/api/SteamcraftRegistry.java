package flaxbeard.steamcraft.api;

import flaxbeard.steamcraft.api.book.BookPage;
import flaxbeard.steamcraft.api.book.ICraftingPage;
import flaxbeard.steamcraft.api.enhancement.IEnhancement;
import flaxbeard.steamcraft.api.enhancement.IRocket;
import flaxbeard.steamcraft.api.exosuit.ExosuitPlate;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.*;

@SuppressWarnings("unused")
public class SteamcraftRegistry {
    /**
     * The Items that can be used on the Carving Table.
     */
    public static ArrayList<Item> molds = new ArrayList<>();

    /**
     * All of the CrucibleLiquids that the mod knows about.
     */
    public static ArrayList<CrucibleLiquid> liquids = new ArrayList<>();

    /**
     * All of the CrucibleLiquid recipes.
     * Key: pair of the item and its metadata, -1 if it does not use metadata.
     * Value: pair of the liquid and the amount created.
     */
    public static HashMap<MutablePair<Item, Integer>, MutablePair<CrucibleLiquid, Integer>> liquidRecipes = new HashMap<>();

    /**
     * All of the Crucible dunking recipes.
     * Key: A triplet of the Item, the metadata, and the CrucibleLiquid.
     * Value: A pair of the required liquid amount and the output ItemStack.
     */
    public static HashMap<Tuple3, MutablePair<Integer, ItemStack>> dunkRecipes = new HashMap<>();

    /**
     * The icons for all of the IEnhancements.
     * Key: A pair of the item, the item as an enhancement.
     * Value: The ResourceLocation for the enhancement.
     */
    public static HashMap<MutablePair<Item, IEnhancement>, ResourceLocation> enhancementIcons = new HashMap<>();

    /**
     * The IEnhancements.
     * Key: Enhancement ID
     * Value: IEnhancement
     */
    public static HashMap<String, IEnhancement> enhancements = new HashMap<>();

    /**
     * All of the rockets that the rocket launcher can use.
     */
    public static ArrayList<IRocket> rockets = new ArrayList<>();

    /**
     * All of the Esteemed Innovation categories.
     */
    public static ArrayList<String> categories = new ArrayList<>();

    /**
     * All of the research entries in Esteemed Innovation book. Each entry is a pair of the name and category.
     */
    public static ArrayList<MutablePair<String, String>> research = new ArrayList<>();

    /**
     * All of the research pages. Key is the entry name, and the value is all of the pages in that entry.
     */
    public static HashMap<String, BookPage[]> researchPages = new HashMap<>();

    /**
     * All of the research pages showcasing crafting recipes.
     * Key: The output item.
     * Value: A pair of the entry name and the page number.
     */
    public static HashMap<ItemStack, MutablePair<String, Integer>> bookRecipes = new HashMap<>();

    /**
     * List of all entries that had entries added to the musing ! syntax. Used by the ctrl/shift click journal feature.
     */
    public static ArrayList<String> entriesWithSubEntries = new ArrayList<>();

    /**
     * All of the registered Exosuit Plates. Key is the plate ID, which is typically the material's
     * name. Value is the actual plate.
     */
    public static HashMap<String, ExosuitPlate> plates = new HashMap<>();

    /**
     * All of the Exosuit Plate icons. Key is a pair of the exosuit slot and the plate. Value is the
     * IIcon for that slot.
     */
    public static HashMap<MutablePair<Integer, ExosuitPlate>, IIcon> plateIcons = new HashMap<>();

    /**
     * All of the custom steaming recipes for the Steam Heater.
     * Key: A pair of the output of the input item and its metadata (eg: cooked porkchop).
     * Value: A pair of the output item and its metadata (eg: steamed porkchop).
     */
    public static HashMap<MutablePair<Item, Integer>, MutablePair<Item, Integer>> steamingRecipes = new HashMap<>();

    /**
     * Adds a steaming recipe.
     * @param food1 The output of the input item (eg: cooked porkchop).
     * @param i The output of the input item metadata.
     * @param food2 The output item (eg: steamed porkchop).
     * @param j The output item metadata.
     */
    public static void addSteamingRecipe(Item food1, int i, Item food2, int j) {
        steamingRecipes.put(MutablePair.of(food1, i), MutablePair.of(food2, j));
    }

    /**
     * Adds a steaming recipe that does not take metadata.
     * @param food1 output of the The input item (eg: cooked porkchop).
     * @param food2 The output item (eg: steamed porkchop).
     */
    public static void addSteamingRecipe(Item food1, Item food2) {
        steamingRecipes.put(MutablePair.of(food1, 0), MutablePair.of(food2, 0));
    }

    /**
     * Removes a steaming recipe.
     * @see #addSteamingRecipe(Item, int, Item, int) for params.
     */
    public static void removeSteamingRecipe(Item food1, int i) {
        steamingRecipes.remove(MutablePair.of(food1, i));
    }

    /**
     * Removes a steaming recipe with no specific metadata.
     * @see #addSteamingRecipe(Item, Item) for params.
     */
    public static void removeSteamingRecipe(Item food1) {
        removeSteamingRecipe(food1, 0);
    }

    /**
     * Registers an ExosuitPlate.
     * @param plate The plate.
     */
    public static void addExosuitPlate(ExosuitPlate plate) {
        plates.put(plate.getIdentifier(), plate);
    }

    /**
     * Allows the mold to be used on the Carving Table.
     * @param mold The item
     */
    public static void addCarvableMold(Item mold) {
        molds.add(mold);
    }

    /**
     * Removes the mold from the list of Items that can be used on the Carving Table.
     * @param mold The item
     * @return The return value of ArrayList#remove.
     * @see ArrayList#remove(Object)
     */
    public static boolean removeMold(Item mold) {
        return molds.remove(mold);
    }

    /**
     * Adds a research category.
     * @param string The name of the category.
     */
    public static void addCategory(String string) {
        categories.add(string);
    }

    /**
     * Adds a research entry. Typically all of these should be unlocalized, linking up to lang entries.
     * @param string The name of the entry.
     * @param category The category name.
     * @param pages The pages in that entry.
     */
    public static void addResearch(String string, String category, BookPage... pages) {
        if (!category.substring(0, 1).equals("!")) {
            research.add(MutablePair.of(string, category));
            researchPages.put(string, pages);
            int pageNum = 0;
            for (BookPage page : pages) {
                if (page instanceof ICraftingPage) {
                    for (ItemStack craftedItem : ((ICraftingPage) page).getCraftedItem()) {
                        bookRecipes.put(craftedItem, MutablePair.of(string, pageNum));
                    }
                }
                pageNum++;
            }
        } else {
            entriesWithSubEntries.add(category.substring(1));
            BookPage[] targetPages = researchPages.get(category.substring(1));
            int pageNum = targetPages.length;
            for (BookPage page : pages) {
                if (page instanceof ICraftingPage) {
                    for (ItemStack craftedItem : ((ICraftingPage) page).getCraftedItem()) {
                        bookRecipes.put(craftedItem, MutablePair.of(category.substring(1), pageNum));
                    }
                }
                pageNum++;
            }
            ArrayList<BookPage> pages2 = new ArrayList<>(Arrays.asList(targetPages));
            pages2.addAll(Arrays.asList(pages));
            researchPages.put(category.substring(1), pages2.toArray(new BookPage[pages2.size()]));
        }
    }

    /**
     * Gets the given CrucibleLiquid from the name.
     * @param name The liquid's name.
     * @return Null if it cannot find that liquid, otherwise, the liquid.
     */
    public static CrucibleLiquid getLiquidFromName(String name) {
        for (CrucibleLiquid liquid : liquids) {
            if (liquid.name.equals(name)) {
                return liquid;
            }
        }
        return null;
    }

    /**
     * Registers a Crucible dunking recipe.
     * @param item The input item
     * @param meta The input metadata
     * @param liquid The input liquid.
     * @param liquidAmount The amount of liquid needed.
     * @param result The output ItemStack.
     */
    public static void registerDunkRecipe(Item item, int meta, CrucibleLiquid liquid, int liquidAmount, ItemStack result) {
        dunkRecipes.put(new Tuple3<>(item, meta, liquid), MutablePair.of(liquidAmount, result));
    }

    /**
     * Registers a Crucible dunking recipe with no input metadata.
     * @param item The input item
     * @param liquid The input liquid.
     * @param liquidAmount The amount of the liquid needed.
     * @param result The output ItemStack.
     */
    public static void registerDunkRecipe(Item item, CrucibleLiquid liquid, int liquidAmount, ItemStack result) {
        registerDunkRecipe(item, -1, liquid, liquidAmount, result);
    }

    /**
     * Removes a dunking recipe.
     * @param item The input item.
     * @param meta The input item metadata.
     * @param liquid The input liquid.
     */
    public static void removeDunkRecipe(Item item, int meta, CrucibleLiquid liquid) {
        if (dunkRecipes != null) {
            Iterator<Map.Entry<Tuple3, MutablePair<Integer, ItemStack>>> iter = dunkRecipes.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<Tuple3, MutablePair<Integer, ItemStack>> entry = iter.next();
                Tuple3 key = entry.getKey();
                if (key.first == item && (int) key.second == meta && key.third == liquid) {
                    iter.remove();
                }
            }
        }
    }

    /**
     * Removes a dunking recipe that does not take metadata.
     * @param item The input item.
     * @param liquid The input liquid.
     */
    public static void removeDunkRecipe(Item item, CrucibleLiquid liquid) {
        removeDunkRecipe(item, -1, liquid);
    }

    /**
     * Registers a Crucible dunking recipe using the OreDictionary.
     * @param dict The OreDict tag.
     * @param liquid The input liquid.
     * @param liquidAmount The liquid needed.
     * @param result The result ItemStack.
     */
    public static void registerOreDictDunkRecipe(String dict, CrucibleLiquid liquid, int liquidAmount, ItemStack result) {
        List<ItemStack> ores = OreDictionary.getOres(dict);
        for (ItemStack ore : ores) {
            registerDunkRecipe(ore.getItem(), ore.getItemDamage(), liquid, liquidAmount, result);
        }
    }

    /**
     * Removes a Crucible dunking recipe that uses the OreDictionary.
     * @param dict The input OreDict tag.
     * @param liquid The input liquid.
     */
    public static void removeOreDictDunkRecipe(String dict, CrucibleLiquid liquid) {
        List<ItemStack> ores = OreDictionary.getOres(dict);
        for (ItemStack ore : ores) {
            removeDunkRecipe(ore.getItem(), ore.getItemDamage(), liquid);
        }
    }

    /**
     * Registers a Crucible melting recipe.
     * @param item The input item.
     * @param i The input metadata.
     * @param liquid The output liquid.
     * @param m The output liquid amount.
     */
    public static void registerMeltRecipe(Item item, int i, CrucibleLiquid liquid, int m) {
        liquidRecipes.put(MutablePair.of(item, i), MutablePair.of(liquid, m));
    }

    /**
     * Registers a Crucible melting recipe with no input metadata.
     * @param item The input item.
     * @param liquid The output liquid.
     * @param m The amount of liquid.
     */
    public static void registerMeltRecipe(Item item, CrucibleLiquid liquid, int m) {
        liquidRecipes.put(MutablePair.of(item, -1), MutablePair.of(liquid, m));
    }

    /**
     * Registers a Crucible melting recipe that takes an OreDict entry as input.
     * @param dict The OreDict tag.
     * @param liquid The output liquid.
     * @param m The amount of liquid.
     */
    public static void registerMeltRecipeOreDict(String dict, CrucibleLiquid liquid, int m) {
        List<ItemStack> ores = OreDictionary.getOres(dict);
        for (ItemStack ore : ores) {
            registerMeltRecipe(ore.getItem(), ore.getItemDamage(), liquid, m);
        }
    }

    /**
     * Registers a Crucible melting recipe for a tool.
     * @param item The input item.
     * @param liquid The output liquid.
     * @param m The amount of liquid.
     */
    public static void registerMeltRecipeTool(Item item, CrucibleLiquid liquid, int m) {
        for (int i = 0; i < item.getMaxDamage(); i++) {
            liquidRecipes.put(MutablePair.of(item, i), MutablePair.of(liquid, MathHelper.floor_double(m * ((float) (item.getMaxDamage() - i) / (float) item.getMaxDamage()))));
        }
    }

    /**
     * Removes a melt recipe for an OreDict entry.
     * @param dict The oredict tag.
     * @param liquid The output liquid.
     */
    public static void removeMeltRecipeOreDict(String dict, CrucibleLiquid liquid) {
        List<ItemStack> ores = OreDictionary.getOres(dict);
        for (ItemStack ore : ores) {
            removeMeltRecipe(ore.getItem(), ore.getItemDamage(), liquid);
        }
    }

    /**
     * Removes a melting recipe for a tool.
     * @param item The tool
     * @param liquid The output liquid
     */
    public static void removeMeltRecipeTool(Item item, CrucibleLiquid liquid) {
        for (int j = 0; j < item.getMaxDamage(); j++) {
            removeMeltRecipe(item, j, liquid);
        }
    }

    /**
     * Removes a melting recipe.
     * @param item Input item
     * @param meta Input item metadata.
     * @param liquid Output liquid.
     */
    public static void removeMeltRecipe(Item item, int meta, CrucibleLiquid liquid) {
        MutablePair input = MutablePair.of(item, meta);
        if (liquidRecipes.containsKey(input)) {
            MutablePair output = liquidRecipes.get(input);
            if (output.left == liquid) {
                liquidRecipes.remove(input);
            }
        }
    }

    /**
     * Removes a melting recipe without metadata.
     * @param item Input item.
     * @param liquid Output liquid.
     */
    public static void removeMeltRecipe(Item item, CrucibleLiquid liquid) {
        removeMeltRecipe(item, -1, liquid);
    }

    /**
     * Sets the given CrucibleLiquid as an actual CrucibleLiquid that can be used.
     * @param liquid The liquid
     */
    public static void registerLiquid(CrucibleLiquid liquid) {
        liquids.add(liquid);
    }

    /**
     * Removes the liquid from the list of registered liquids. It also removes all of the liquid's
     * recipes.
     * @param liquid The item
     */
    public static void removeLiquid(CrucibleLiquid liquid) {
        liquids.remove(liquid);
        if (liquidRecipes != null) {
            for (Map.Entry<MutablePair<Item, Integer>, MutablePair<CrucibleLiquid, Integer>> entry : liquidRecipes.entrySet()) {
                if ((entry.getValue()).left == liquid) {
                    liquidRecipes.remove(entry.getKey());
                }
            }
        }
    }

    /**
     * Adds an enhancement to the list of valid enhancements.
     * @param enhancement The IEnhancement to add.
     */
    public static void registerEnhancement(IEnhancement enhancement) {
        enhancements.put(enhancement.getID(), enhancement);
    }

    /**
     * Adds a rocket to the valid rockets for the Rocket Launcher.
     * @param rocket The IRocket to add.
     */
    public static void registerRocket(IRocket rocket) {
        rockets.add(rocket);
    }
}
